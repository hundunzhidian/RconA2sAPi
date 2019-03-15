package cn.qaq.valveapi.utils;


import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.SocketChannel;

public class TcpTools {
    private static final Logger logger = Logger.getLogger(TcpTools.class);
    private Socket socket ;

    public final static int SERVERDATA_EXECCOMMAND = 2;
    public final static int SERVERDATA_AUTH = 3;
    public final static int SERVERDATA_RESPONSE_VALUE = 0;
    public final static int SERVERDATA_AUTH_RESPONSE = 2;

    final static int RESPONSE_TIMEOUT = 2000;
    final static int MULTIPLE_PACKETS_TIMEOUT = 300;

    public void initTcp(String ip) throws IOException {
        String[] ips=ip.split(":");
        socket=new Socket();
        socket.connect(new InetSocketAddress(ips[0], Integer.parseInt(ips[1])), 1000);
        socket.setSoTimeout(RESPONSE_TIMEOUT);
    }
    public void closeTcp(){
        try {
            if(socket!=null&&!socket.isClosed())
            {socket.getOutputStream().close(); socket.getInputStream().close(); socket.close();}
        } catch (Exception e) {
            // TODO: handle exception
            logger.debug(e.getMessage());
        }
    }

    /**
     * 执行Rcon命令
     * @return  rcon命令返回
     * @param command  控制台命令
     * @param password Rcon密码
     * */
    public String send(String command,String password) throws Exception {
        String response = "";
        //logger.debug("cmd:"+command+"\npasswd:"+password);
        try {
            if (rcon_auth(password)) {
                // We are now authed
                ByteBuffer[] resp = sendCommand(command);
                // Close socket handlers, we don't need them more
                if (resp != null) {
                    response = assemblePackets(resp);
                    if (response.length() == 0) {
                        throw new HuangException("返回值为空!!!");
                    }
                }
            }
            else {
                throw new HuangException("Rcon Passwd Error");
            }
        } catch (SocketTimeoutException timeout) {
            throw timeout;
        } catch (UnknownHostException e) {
            throw  new HuangException("未知的主机名");
        } catch (IOException e) {
            throw  new HuangException("Couldn't get I/O for the connection: ");
        }catch (Exception e) {
            // TODO: handle exception
            throw e;
        }
        return response;
    }
    private static String assemblePackets(ByteBuffer[] packets) {
        // Return the text from all the response packets together

        String response = "";

        for (int i = 0; i < packets.length; i++) {
            if (packets[i] != null) {
                response = response.concat(new String(packets[i].array(), 12, packets[i].position()-14));
            }
        }
        return response;
    }
    private ByteBuffer[] sendCommand(String command) throws Exception {

        byte[] request = contructPacket(2, SERVERDATA_EXECCOMMAND, command);

        ByteBuffer[] resp = new ByteBuffer[128];
        int i = 0;
        try {
            socket.getOutputStream().write(request);
            resp[i] = receivePacket();  // First and maybe the unique response packet
            try {
                // We don't know how many packets will return in response, so we'll
                // read() the socket until TimeoutException occurs.
                socket.setSoTimeout(MULTIPLE_PACKETS_TIMEOUT);
                while (true) {
                    resp[++i] = receivePacket();
                }

            } catch (SocketTimeoutException e) {
                // No more packets in the response, go on
                return resp;
            }

        }catch (Exception e) {
            throw e;
        }
    }

    private boolean rcon_auth(String rcon_password) throws SocketTimeoutException {

        byte[] authRequest = contructPacket(1337, SERVERDATA_AUTH, rcon_password);

        ByteBuffer response = ByteBuffer.allocate(64);
        try {
            socket.getOutputStream().write(authRequest);

            response = receivePacket(); // junk response packet
            response = receivePacket();

            // Lets see if the received request_id is leet enougth ;)
            if ((response.getInt(4) == 1337) && (response.getInt(8) == SERVERDATA_AUTH_RESPONSE)) {
                return true;
            }
        } catch (SocketTimeoutException timeout) {
            throw timeout;
        } catch (Exception e) {
           e.printStackTrace();
        }

        return false;
    }

    private ByteBuffer receivePacket() throws Exception {

        ByteBuffer p = ByteBuffer.allocate(4120);
        p.order(ByteOrder.LITTLE_ENDIAN);

        byte[] length = new byte[4];

        if (socket.getInputStream().read(length, 0, 4) == 4) {
            // Now we've the length of the packet, let's go read the bytes
            p.put(length);
            int i = 0;
            while (i < p.getInt(0)) {
                p.put((byte) socket.getInputStream().read());
                i++;
            }
            return p;
        }
        else {
            return null;
        }
    }
    public static final byte[] contructPacket(int id,int type,String s1)
    {
        /**
         * size 0x ff ff ff ff   最大4096，即0x 10 00 ,数据包里面体现为 0x00 10 00 00，可直接使用int类型，int类型为4个字节
         * id   0x ff ff ff ff    4Byte
         * Type 0x ff ff ff ff    4Byte
         * body 0x00结尾
         * empty 0x00             1Byte
         * */
        ByteBuffer p = null;
        try {
            p = ByteBuffer.allocate(s1.getBytes("UTF-8").length + 16);//申请内存大小
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        p.order(ByteOrder.LITTLE_ENDIAN);//变更字节序为小端,小端字节序为，低位在前，高位在后，即0x10 00 00 00,表示的为正整数16
        try {
            p.putInt(s1.getBytes("UTF-8").length + 12);//存入size大小
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        p.putInt(id);//存入id值
        p.putInt(type);//存入类型
        p.put(s1.getBytes());//存入body的字节码
        // two null bytes at the end
        p.put((byte) 0x00);//最后补0x00
        p.put((byte) 0x00);
        // null string2 (see Source protocol)
        p.put((byte) 0x00);
        p.put((byte) 0x00);
        // if(cmdtype==2) System.err.println(bytesToHexString(p.array()));
        return p.array();
    }
}

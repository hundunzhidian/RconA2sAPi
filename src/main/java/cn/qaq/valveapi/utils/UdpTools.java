package cn.qaq.valveapi.utils;

import lombok.extern.slf4j.Slf4j;
import java.io.IOException;
import java.net.*;

/**
 * <p>
 * 标题: UdpTools.java
 * </p>
 * <p>
 * 描述: 
 * </p>
 * <p>
 * 版权: HUANG All Right
 * </p>
 * <p>
 * 创建时间: 2018-11-21
 * </p>  
 * <p>
 * 作者: HUANG
 * </p>
 * <p>修改历史记录：</p>
 * ====================================================================<br>
 * 维护单：<br>
 * 修改日期：<br>
 * 修改人：<br>
 * 修改内容：<br>       
 */
@Slf4j
public class UdpTools {
    public static final String A2S_PLAYER="FF FF FF FF 55 FF FF FF FF";
    public static final String A2S_INFO="FF FF FF FF 54 53 6F 75 72 63 65 20 45 6E 67 69 6E 65 20 51 75 65 72 79 00";
    public static final String RCON_CHALLENGE="FF FF FF FF 63 68 61 6C 6C 65 6E 67 65 20 72 63 6F 6E";
    public static final byte[] RCON_HEADER=ByteTools.hexStrToBinaryStr("FF FF FF FF 72 63 6F 6E");
    private DatagramSocket datagramSocket;
    private  long time;


    public static final String byteToString(byte[] src)
    {
        try {
            int length = 0;
            for (int i = 0; i < src.length; ++i) {
                if (src[i] == 0) {
                    length = i;
                    break;
                }
            }
            return new String(src, 0, length, "UTF-8");
        } catch (Exception e) {
            log.error(e.toString());
            return "";
        }
    }
    public static final String byteToString(byte[] src,Integer offset)
    {
        try {
            int length = 0;
            for (int i = 0; i < src.length; ++i) {
                if (src[i+offset] == 0) {
                    length = i;
                    break;
                }
            }
            return new String(src, offset, length, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
    public static  final String intToByteString(int src)
    {
        char[] HEX_CHAR = {'0', '1', '2', '3', '4', '5','6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        return String.valueOf(HEX_CHAR[(src<0?src+256:src)/16])+String.valueOf(HEX_CHAR[(src<0?src+256:src)/16]);
    }
    public UdpTools() throws SocketException {

        datagramSocket=new DatagramSocket();
        log.debug("已初始化UDP");
    }
    public UdpTools(int localport) throws SocketException {

        datagramSocket=new DatagramSocket(localport);
        log.debug("已初始化UDP");
    }
    public UdpTools(String localIp,int localPort) throws SocketException, UnknownHostException {

        datagramSocket=new DatagramSocket(localPort, InetAddress.getByName(localIp));
        log.debug("已初始化UDP");
    }
    public byte[] SendData(String ip,int port,byte[] data) throws IOException {
        InetAddress address = InetAddress.getByName(ip);
        DatagramPacket datagramPacket=new DatagramPacket(data,data.length,address,port);
        datagramSocket.setSoTimeout(500);

        time=System.currentTimeMillis();
        datagramSocket.send(datagramPacket);
        //构建一个数据接收对象
        byte[] receBuf = new byte[4096];
        DatagramPacket recePacket = new DatagramPacket(receBuf, receBuf.length);
        datagramSocket.receive(recePacket);
        time=System.currentTimeMillis()-time;
        byte[] resBytes=recePacket.getData();
        return resBytes;
    }
    public  void closeUdp()
    {
        try {
            if(datagramSocket!=null&&!datagramSocket.isClosed())
                datagramSocket.close();
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        log.debug("已成功关闭UDP");
    }

    public long getTime() {
        return time;
    }
}

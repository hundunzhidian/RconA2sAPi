package cn.qaq.valveapi.utils;

import cn.qaq.valveapi.Exception.QaQServiceException;
import cn.qaq.valveapi.a2s.A2sInfo;
import cn.qaq.valveapi.a2s.ServerInfo;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Slf4j
public class UdpServer {

    /**
     * @return 返回服务器的玩家名称(map)、分数score、时间time、索引index
     * @param ip 服务器IP:服务器端口
     * */
    public static final List<HashMap<String,Object>> getPlayers(String ip) throws IOException {
        List<HashMap<String,Object>> list=new ArrayList<>();
        UdpTools udpTools = null;
        try {
            String[] ips=ip.split(":");
            udpTools=new UdpTools();
            byte[] data=udpTools.SendData(ips[0],Integer.parseInt(ips[1]),ByteTools.hexStrToBinaryStr(UdpTools.A2S_PLAYER));//先取回握手包
            data[4]=0x55;
            byte[] tmp=new byte[9];
            for(int i=0;i<9;i++)
            {
                tmp[i]=data[i];
            }
            data=udpTools.SendData(ips[0],Integer.parseInt(ips[1]),tmp);//拿到玩家数据
            int i=6;//从第7位开始, FF FF FF FF 44 05,第6位是玩家总数
            for(int players=0;players<data[5];players++)
            {
                HashMap<String,Object> hashMap=new HashMap<>();
                byte[] str=new byte[100];
                hashMap.put("index",data[i++]);//存入index
                //获取玩家姓名
                for(int j=0;i<data.length;i++,j++)
                {
                    str[j]=data[i];
                    if(data[i]==0x00) {
                        str[j]=data[i++];
                        break;
                    }
                }
                hashMap.put("name",UdpTools.byteToString(str));//存入玩家姓名
                //说明:32位应用程序long占4个字节，16进制为0xff ff
                long longi=ByteTools.getLongBig(data,i,4);
                i+=4;
                hashMap.put("score",longi);//存入分数
                byte[] t=new byte[4];
                for(int j=0;j<4;j++,i++)
                {
                    t[j]=data[i];
                }
                hashMap.put("time", String.valueOf(ByteTools.getFloat(t)*1));
                list.add(hashMap);
            }
        } catch (SocketTimeoutException e) {
            throw  e;
        }finally {
            udpTools.closeUdp();
        }
        return list;
    }
    /**
     * 新引擎
     * */
    public static final void sourceServer(byte[] resBytes,HashMap<String,Object> src) throws Exception
    {
            byte[] tmp=new byte[100];
            int j=6;//初始化从第6个字符开始
            //先处理得到服务器名称
            for(;j<resBytes.length;j++)
            {

                tmp[j-6]=resBytes[j];
                if(resBytes[j]==0)
                    break;
            }
            j++;//跳过等于0的这个字节
            src.put("name", UdpTools.byteToString(tmp));
            for(int i=0;j<resBytes.length;i++,j++)
            {
                tmp[i]=resBytes[j];

                if(resBytes[j]==0)
                    break;
            }
            src.put("map", UdpTools.byteToString(tmp));
            j++;//跳过 0x00这个字节
            Integer k=0;// 0x00出现的次数
            for(;j<resBytes.length;j++)
            {
                //System.out.print(" "+resBytes[j]+" ");
                if(resBytes[j]==0)
                {
                    k++;
                    if(k==2) break;//map后面只有两个string类型的数据
                }
                //
/*                if(resBytes[j-2]==0&&resBytes[j-1]==38&&resBytes[j]==2)
                {
                    // 0 38 2
                    break;
                }*/
            }
            src.put("players", resBytes[j+3]+"/"+resBytes[j+4]);
        if(resBytes[j+8]==(byte) 0x01)  src.put("visibility","private");
    }
    /**
     * 老引擎
     * */
    public static final void oldSourceServer(byte[] resBytes,HashMap<String,Object> src)
    {
        /**
         * Source老引擎：
         *               Address
         *               Name
         *               Map
         *               Folder
         *               Game
         * */
        byte[] tmp=new byte[100];
        int j=6;//初始化从第6个字符开始
        //对于老引擎，这个是address值
        for(;j<resBytes.length;j++)
        {
            tmp[j-6]=resBytes[j];
            if(resBytes[j]==0)
                break;
        }
        j++;//跳过等于0的这个字节
        for(int i=0;j<resBytes.length;i++,j++)
        {
            tmp[i]=resBytes[j];

            if(resBytes[j]==0)
                break;
        }
        j++;//跳过 0x00这个字节
        src.put("name", UdpTools.byteToString(tmp));
        for(int i=0;j<resBytes.length;i++,j++)
        {
            tmp[i]=resBytes[j];

            if(resBytes[j]==0)
                break;
        }
        src.put("map", UdpTools.byteToString(tmp));
        j++;//跳过 0x00这个字节
        Integer k=0;// 0x00出现的次数
        for(;j<resBytes.length;j++)
        {
            //System.out.print(" "+resBytes[j]+" ");
            if(resBytes[j]==0)
            {
                k++;
                if(k==2) break;//map后面只有两个string类型的数据
            }
            //
/*                if(resBytes[j-2]==0&&resBytes[j-1]==38&&resBytes[j]==2)
                {
                    // 0 38 2
                    break;
                }*/
        }
        src.put("players", resBytes[j+1]+"/"+resBytes[j+2]);
        if(resBytes[j+6]==(byte) 0x01)  src.put("visibility","private");
    }
    /**
     * @return 返回服务器信息 名称name 地图map 玩家数players 延迟time
     * @param ip 服务器IP:服务器端口
     * */
    public static final HashMap<String,Object> getServers(String ip)
    {
        HashMap<String,Object> hashMap=new HashMap<>();
        hashMap.put("name","");
        hashMap.put("map","");
        hashMap.put("players","0/0");
        hashMap.put("time","0ms");
        hashMap.put("visibility","public");
        UdpTools udpTools = null;
        try {
            String[] ips=ip.split(":");
            udpTools=new UdpTools();
            byte[] a2s_info=ByteTools.hexStrToBinaryStr(UdpTools.A2S_INFO);
            byte[] resBytes=udpTools.SendData(ips[0],Integer.parseInt(ips[1]),a2s_info);
            if(resBytes[4]==(byte) 0x41)
            {
                byte[] newPacket=new byte[a2s_info.length+4];
                ByteTools.arraycopy(a2s_info,0,newPacket,0,a2s_info.length);
                ByteTools.arraycopy(resBytes,5,newPacket,a2s_info.length,4);
                sourceServer(udpTools.SendData(ips[0],Integer.parseInt(ips[1]),newPacket),hashMap);
            }else if(resBytes[4]==(byte) 0x6d) oldSourceServer(resBytes,hashMap);
                else  sourceServer(resBytes,hashMap);
            hashMap.put("time",(int)udpTools.getTime()+"ms");
        } catch (SocketTimeoutException e) {
            hashMap.put("map", "访问超时");
        } catch (Exception e){
            hashMap.put("map",e.getLocalizedMessage());
        }finally {
            udpTools.closeUdp();
        }
        return hashMap;
    }
    /**
     * @return 返回服务器信息 名称name 地图map 玩家数players 延迟time
     * @param ip 服务器IP:服务器端口
     * */
    @SneakyThrows
    public static final ServerInfo getServersV2(String ip)
    {
        UdpTools udpTools = null;
        try {
            String[] ips=ip.split(":");
            udpTools=new UdpTools();
            byte[] a2s_info=ByteTools.hexStrToBinaryStr(UdpTools.A2S_INFO);
            byte[] resBytes=udpTools.SendData(ips[0],Integer.parseInt(ips[1]),a2s_info);
            if(resBytes[4]==(byte) 0x41)
            {
                byte[] newPacket=new byte[a2s_info.length+4];
                ByteTools.arraycopy(a2s_info,0,newPacket,0,a2s_info.length);
                ByteTools.arraycopy(resBytes,5,newPacket,a2s_info.length,4);
                ServerInfo resInfo = A2sInfo.parseFrom(udpTools.SendData(ips[0], Integer.parseInt(ips[1]), newPacket));
                resInfo.setTime(udpTools.getTime());
                return resInfo;
            }else if(resBytes[4]!=(byte) 0x6d) return A2sInfo.parseFrom(resBytes);
            else throw new QaQServiceException("仅查询起源服务器协议数据");
        } catch (Exception e){
            throw e;
        }finally {
            udpTools.closeUdp();
        }
    }

    public static final String udpRcon(String ip,String password,String cmd)throws Exception
    {
        UdpTools udpTools = null;
        try {
            String[] ips = ip.split(":");
            udpTools = new UdpTools();
            byte[] data = udpTools.SendData(ips[0], Integer.parseInt(ips[1]), ByteTools.hexStrToBinaryStr(UdpTools.RCON_CHALLENGE));//先取回握手包
            Integer i = 0;
            for (; i < data.length; i++) {
                if (data[i] == (byte) 0x20) break;
            }
            i++;
            for (; i < data.length; i++) {
                if (data[i] == (byte) 0x20) break;
            }
            i++;
            //跳过两个 0x20
            byte[] challenge = new byte[20];
            for (Integer j = 0; i < data.length; j++, i++) {
                if (data[i] == (byte) 0x0a) break;
                challenge[j] = data[i];
            }
            //数据包由 RCON_HEADER xx "password" cmd0x00
            byte[] cmds = new byte[UdpTools.byteToString(challenge).getBytes().length +
                    8 + 2 + 2 +
                    password.getBytes("UTF-8").length +
                    cmd.getBytes("UTF-8").length + 1 + 2];
            i = 0;
            for (Integer j = 0; i < UdpTools.RCON_HEADER.length; i++, j++) {
                cmds[i] = UdpTools.RCON_HEADER[j];
            }
            cmds[i++] = (byte) 0x20;
            //下面放置challenge
            for (Integer j = 0; i < challenge.length; i++, j++) {
                cmds[i] = challenge[j];
            }
            cmds[--i] = (byte) 0x20;// 空格
            cmds[++i] = (byte) 0x22;// "
            i++;
            for (Integer j = 0; j < password.getBytes("UTF-8").length; i++, j++) {
                cmds[i] = password.getBytes("UTF-8")[j];
            }
            cmds[i++] = (byte) 0x22;// "
            cmds[i++] = (byte) 0x20;// 空格
            for (Integer j = 0; j < cmd.getBytes("UTF-8").length; i++, j++) {
                cmds[i] = cmd.getBytes("UTF-8")[j];
            }
            cmds[i] = (byte) 0x00;
            byte[] response = udpTools.SendData(ips[0], Integer.parseInt(ips[1]), cmds);//发送命令
            for (i = 0; i < response.length; i++)
            {
                if(response[i]==(byte)0x6C) break;
            }
            return UdpTools.byteToString(response,i+1);
        } catch (SocketTimeoutException e) {
            throw  e;
        }finally {
            udpTools.closeUdp();
        }
    }

}

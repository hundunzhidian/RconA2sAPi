package cn.qaq.valveapi.utils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.*;

public class UdpServer {
    private static final Logger logger = Logger.getLogger(UdpServer.class);

    /**
     * @return 返回服务器的玩家名称(map)、分数score、时间time、索引index
     * @param ip 服务器IP:服务器端口
     * */
    public static final JSONArray getPlayers(String ip) throws IOException {
        JSONArray jsonArray=new JSONArray();
        UdpTools udpTools = null;
        try {
            String[] ips=ip.split(":");
            udpTools=new UdpTools();
            byte[] data=udpTools.SendData(ips[0],Integer.parseInt(ips[1]),UdpTools.hexStrToBinaryStr(UdpTools.A2S_PLAYER));//先取回握手包
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
                JSONObject jsonObject=new JSONObject();
                byte[] str=new byte[100];
                jsonObject.put("index",data[i++]);//存入index
                //获取玩家姓名
                for(int j=0;i<data.length;i++,j++)
                {
                    str[j]=data[i];
                    if(data[i]==0x00) {
                        str[j]=data[i++];
                        break;
                    }
                }
                jsonObject.put("name",UdpTools.byteToString(str));//存入玩家姓名
                //说明:32位应用程序long占4个字节，16进制为0xff ff
                long longi=0;
                for(int j=0;j<4;j++,i++)
                {
                    longi=longi|((long)data[i]<<8*j);
                    // logger.debug((long)data[i]);
                }
                jsonObject.put("socre",longi);//存入分数
                byte[] t=new byte[4];
                for(int j=0;j<4;j++,i++)
                {
                    t[j]=data[i];
                }
                jsonObject.put("time", String.valueOf(UdpTools.getFloat(t)*1));
                jsonArray.add(jsonObject);
            }
        } catch (SocketTimeoutException e) {
            throw  e;
        }finally {
            udpTools.closeUdp();
        }
        return jsonArray;
    }
    /**
     * 新引擎
     * */
    public static final void sourceServer(byte[] resBytes,JSONObject src) throws Exception
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
    public static final void oldSourceServer(byte[] resBytes,JSONObject src)
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
    public static final JSONObject getServers(String ip)
    {

        JSONObject jsonObject=new JSONObject();
        jsonObject.put("name","");
        jsonObject.put("map","");
        jsonObject.put("players","0/0");
        jsonObject.put("time","0ms");
        jsonObject.put("visibility","public");
        UdpTools udpTools = null;
        try {
            String[] ips=ip.split(":");
            udpTools=new UdpTools();
            byte[] resBytes=udpTools.SendData(ips[0],Integer.parseInt(ips[1]),UdpTools.hexStrToBinaryStr(UdpTools.A2S_INFO));
            if(resBytes[4]==(byte) 0x6d) oldSourceServer(resBytes,jsonObject);
                else  sourceServer(resBytes,jsonObject);
            jsonObject.put("time",(int)udpTools.getTime()+"ms");
        } catch (SocketTimeoutException e) {
            jsonObject.put("map", "访问超时");
        } catch (Exception e){
            jsonObject.put("map",e.getLocalizedMessage());
        }finally {
            udpTools.closeUdp();
        }
        return jsonObject;
    }

}

package cn.qaq.valveapi.a2s;


import cn.qaq.valveapi.utils.ByteTools;
import lombok.SneakyThrows;

import java.util.concurrent.atomic.AtomicInteger;

import static cn.qaq.valveapi.utils.ByteTools.*;


/**
 * @program: a2sdefender
 * @description:
 * @author: QAQ
 * @create: 2021-11-16 08:40
 **/
public class A2sInfo {
    public static ServerInfo parseFrom(byte[] src)
    {
        //解码
        AtomicInteger i=new AtomicInteger(0);
        ServerInfo serverInfo=new ServerInfo();
        byte[] head=new byte[5];
        ByteTools.arraycopy(src,i.getAndAdd(5),head,0,5);
        serverInfo.setHead(head);
        serverInfo.setProtocol(src[i.getAndAdd(1)]);
        serverInfo.setServerName(getString(src,i));
        serverInfo.setServerMap(getString(src,i));
        serverInfo.setServerFolder(getString(src,i));
        serverInfo.setFullName(getString(src,i));

        serverInfo.setAppId(getIntegerSmall(src,i,2));
        serverInfo.setPlayers(getIntegerSmall(src,i,1));
        serverInfo.setMaxPlayers(getIntegerSmall(src,i,1));
        serverInfo.setBots(getIntegerSmall(src,i,1));

        serverInfo.setServerType(src[i.getAndAdd(1)]);
        serverInfo.setEnvironment(src[i.getAndAdd(1)]);

        serverInfo.setVisibility(getBoolean(src,i));
        serverInfo.setVac(getBoolean(src,i));


        serverInfo.setVersion(getString(src,i));

        serverInfo.setFlag(src[i.getAndAdd(1)]);
        //下面是edf信息
        {
            if((serverInfo.getFlag()&((byte)0x80))!=0)
            {
                serverInfo.setEdfPort(getIntegerSmall(src,i,2));
            }
            if((serverInfo.getFlag()&((byte)0x10))!=0)
            {
                serverInfo.setEdfSteamid( ByteTools.getLongSmall(src,i.getAndAdd(8),8));//8字节的steamid
            }
            if((serverInfo.getFlag()&((byte)0x40))!=0)
            {
                //soucetv
                SourceTv sourceTv=new SourceTv();
                sourceTv.setPort(getIntegerSmall(src,i,2));
                sourceTv.setName(getString(src,i));
                serverInfo.setEdfSourceTV(sourceTv);
            }
            if((serverInfo.getFlag()&((byte)0x20))!=0)
            {
                serverInfo.setEdfTags(getString(src,i));
            }
            if((serverInfo.getFlag()&((byte)0x01))!=0)
            {
                serverInfo.setEdfAppid(getIntegerSmall(src,i,2));
            }
        }
        return serverInfo;
    }
}



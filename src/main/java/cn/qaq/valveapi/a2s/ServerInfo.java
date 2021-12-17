package cn.qaq.valveapi.a2s;

import cn.qaq.valveapi.utils.ByteTools;
import lombok.Data;

/**
 * @program: a2sdefender
 * @description:
 * @author: QAQ
 * @create: 2021-11-16 09:36
 **/
@Data
public class ServerInfo
{
    byte[] head= ByteTools.hexStrToBinaryStr("FF FF FF FF 49");//默认 FF FF FF FF 49
    byte protocol=(byte)0x11;//协议版本，默认0x11
    String serverName;
    String serverMap;
    String serverFolder;
    String fullName;
    Integer appId;
    Integer players;
    Integer maxPlayers;
    Integer bots;
    byte serverType=(byte)0x64;//默认dedicated服务器
    byte environment=(byte)0x77;//默认windows
    boolean visibility;//需要密码
    boolean vac;//是否开启vac
    String version;//服务器版本
    byte flag;//EDF额外信息
    Integer edfPort;//服务器端口
    Long edfSteamid;//服务器的steamid
    SourceTv edfSourceTV;//sourcetv信息
    String edfTags;//tags信息
    Integer edfAppid;//appid信息
    byte[] finalByte= ByteTools.hexStrToBinaryStr("00 00 00 00 00 00");

}
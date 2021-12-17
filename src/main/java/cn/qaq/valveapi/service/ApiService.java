package cn.qaq.valveapi.service;


import cn.qaq.valveapi.a2s.ServerInfo;
import cn.qaq.valveapi.utils.TcpTools;
import cn.qaq.valveapi.utils.UdpServer;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Slf4j
@Service
public class ApiService {

    @SneakyThrows
    public List<HashMap<String, Object>> getPlayers(String ip)
    {
        return UdpServer.getPlayers(ip);
    }
    public HashMap<String, Object> getServers(String ip)
    {
        return UdpServer.getServers(ip);
    }
    public ServerInfo getServersV2(String ip)
    {
        return UdpServer.getServersV2(ip);
    }
    @SneakyThrows
    public String execRcon(String ip, String cmd, String passwd)
    {
        TcpTools tools =new TcpTools();
        if(tools.initTcp(ip))
            return tools.send(cmd,passwd);
        else
        {
            return UdpServer.udpRcon(ip,passwd,cmd);
        }
    }
}

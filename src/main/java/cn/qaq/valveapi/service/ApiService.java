package cn.qaq.valveapi.service;


import cn.qaq.valveapi.utils.Json;
import cn.qaq.valveapi.utils.TcpTools;
import cn.qaq.valveapi.utils.UdpServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ApiService {

    public Json getPlayers(String ip) {
        try {
            return new Json(true, UdpServer.getPlayers(ip));
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            return new Json(false, "获取出现异常:" + e.getMessage());
        }
    }


    public Json getServers(String ip) {
        try {
            return new Json(true, UdpServer.getServers(ip));
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            return new Json(false, "获取出现异常:" + e.getMessage());
        }
    }


    public Json execRcon(String ip, String cmd, String passwd) {
        Json json = new Json();
        TcpTools tools = new TcpTools();
        try {
            if (tools.initTcp(ip))
                json.setMsg(tools.send(cmd, passwd));
            else {
                json.setMsg(UdpServer.udpRcon(ip, passwd, cmd));
            }
            json.setSuccess(true);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            json = new Json(false, "获取出现异常:" + e.getMessage());
        } finally {
            tools.closeTcp();
        }
        return json;
    }
}
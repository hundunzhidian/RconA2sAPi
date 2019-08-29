package cn.qaq.valveapi.service;


import cn.qaq.valveapi.utils.Json;
import cn.qaq.valveapi.utils.TcpTools;
import cn.qaq.valveapi.utils.UdpServer;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

@Service
public class ApiService {

    private static final Logger logger = Logger.getLogger(ApiService.class);
    public Json getPlayers(String ip)
    {
        Json json=new Json();
        try {
            json.setJsonArray(UdpServer.getPlayers(ip));
            json.setSuccess(true);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            json.setSuccess(false);
            json.setMsg("获取出现异常:"+e.getMessage());
        }
        return json;
    }
    public Json getServers(String ip)
    {
        Json json=new Json();
        try {
            json.setJsonObject(UdpServer.getServers(ip));
            json.setSuccess(true);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            json.setSuccess(false);
            json.setMsg("获取出现异常:"+e.getMessage());
        }
        return json;
    }
    public Json execRcon(String ip,String cmd,String passwd)
    {
        Json json=new Json();
        TcpTools tools =new TcpTools();
        try {
            if(tools.initTcp(ip))
                json.setMsg(tools.send(cmd,passwd));
            else
            {
                json.setMsg(UdpServer.udpRcon(ip,passwd,cmd));
            }
            json.setSuccess(true);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            json.setSuccess(false);
            json.setMsg("执行Rcon出现异常:"+e.getMessage());
        }finally {
            tools.closeTcp();
        }
        return json;
    }
}

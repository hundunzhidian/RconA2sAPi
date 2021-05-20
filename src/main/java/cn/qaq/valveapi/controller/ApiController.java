package cn.qaq.valveapi.controller;

import cn.qaq.valveapi.service.ApiService;
import cn.qaq.valveapi.utils.ResponseBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

@Slf4j
@RestController
public class ApiController {

    @Resource
    private ApiService service;

    @PostMapping("/rcon")
    public ResponseBean execRcon(@RequestBody Map<String, String> map)
    {
        if(map.containsKey("ip")&&map.containsKey("cmd")&&map.containsKey("passwd"))
        {
            return ResponseBean.SUCCESS(service.execRcon(map.get("ip"),map.get("cmd"),map.get("passwd")));
        }else{
            return ResponseBean.ERROR("参数缺失");
        }
    }
    @PostMapping("/players")
    public ResponseBean getPlayers(@RequestBody Map<String, String> map)
    {
        if(map.containsKey("ip"))
        {
            return ResponseBean.SUCCESS(service.getPlayers(map.get("ip")));
        }else{
            return ResponseBean.ERROR("参数缺失");
        }
    }

    @PostMapping("/servers")
    public ResponseBean getServers(@RequestBody Map<String, String> map)
    {
        if(map.containsKey("ip"))
        {
            return ResponseBean.SUCCESS(service.getServers(map.get("ip")));
        }else{
            return ResponseBean.ERROR("参数缺失");
        }
    }
}

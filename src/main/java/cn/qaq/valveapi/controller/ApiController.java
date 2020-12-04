package cn.qaq.valveapi.controller;

import cn.qaq.valveapi.service.ApiService;
import cn.qaq.valveapi.utils.Json;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@RestController
public class ApiController {

    @Autowired
    private ApiService service;

    @PostMapping(value = "/rcon", produces = "application/json;charset=UTF-8")
    public Json execRcon(@RequestBody Map<String, String> map) {
        log.debug(map.toString());

        if (map.containsKey("ip") && map.containsKey("cmd") && map.containsKey("passwd"))
            return service.execRcon(map.get("ip"), map.get("cmd"), map.get("passwd"));

        return new Json(false, "致命错误:参数缺失!!!");
    }


    @PostMapping(value = "/players", produces = "application/json;charset=UTF-8")
    public Json getPlayers(@RequestBody Map<String, String> map) {

        if (map.containsKey("ip"))
            return service.getPlayers(map.get("ip"));

        return new Json(false, "致命错误:参数缺失!!!");
    }


    @PostMapping(value = "/servers", produces = "application/json;charset=UTF-8")
    public Json getServers(@RequestBody Map<String, String> map) {

        if (map.containsKey("ip"))
            return service.getServers(map.get("ip"));

        return new Json(false, "致命错误:参数缺失!!!");
    }
}

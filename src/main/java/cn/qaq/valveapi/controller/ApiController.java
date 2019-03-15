package cn.qaq.valveapi.controller;

import cn.qaq.valveapi.service.ApiService;
import cn.qaq.valveapi.utils.Json;
import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

@RestController
public class ApiController {
    private static final Logger logger = Logger.getLogger(ApiController.class);



    @Resource
    private ApiService service;
    @RequestMapping(value = "/rcon", method = RequestMethod.POST,produces="application/json;charset=UTF-8")
    @ResponseBody
    public Json execRcon(@RequestBody Map<String, String> map)
    {
        Json json =new Json();
        logger.debug(map.toString());
        if(map.containsKey("ip")&&map.containsKey("cmd")&&map.containsKey("passwd"))
        {
            json=service.execRcon(map.get("ip"),map.get("cmd"),map.get("passwd"));
        }else{
            json.setSuccess(false);
            json.setMsg("致命错误:参数缺失!!!");
        }
        return json;
    }
    @RequestMapping(value = "/players", method = RequestMethod.POST,produces="application/json;charset=UTF-8")
    @ResponseBody
    public Json getPlayers(@RequestBody Map<String, String> map)
    {
        Json json =new Json();
        if(map.containsKey("ip"))
        {
            json=service.getPlayers(map.get("ip"));
        }else{
            json.setSuccess(false);
            json.setMsg("致命错误:参数缺失!!!");
        }
        return json;
    }
    @RequestMapping(value = "/servers", method = RequestMethod.POST,produces="application/json;charset=UTF-8")
    @ResponseBody
    public Json getServers(@RequestBody Map<String, String> map)
    {
        Json json =new Json();
        if(map.containsKey("ip"))
        {
            json=service.getServers(map.get("ip"));
        }else{
            json.setSuccess(false);
            json.setMsg("致命错误:参数缺失!!!");
        }
        return json;
    }
}

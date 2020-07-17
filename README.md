# ValveApi

基于Valve的[Rcon](https://developer.valvesoftware.com/wiki/Source_RCON_Protocol)、[A2s_player](https://developer.valvesoftware.com/wiki/Server_queries#A2S_PLAYER)、[A2s_Info](https://developer.valvesoftware.com/wiki/Server_queries#A2S_INFO)的查询接口

## 项目简介
- 基于Spring boot创建，使用UDP、TCP进行服务器数据传输，并对返回数据进行编码
- Rcon库协议基于[rconed](http://rconed.sf.net/)发展而来，修复了大部分错误，以及修复了对中文不兼容的问题


## 使用方法
- 项目基于JDK1.8,请下载JDK1.8及以上版本运行，运行命令:
```shell
java -jar valveapi-0.0.1-SNAPSHOT.jar --server.port=9090
```
*其中，-jar 后面的参数为项目jar文件，9090为web服务端口号*
**注意：若Windows运行出现乱码，请在启动项前加入-Dfile.encoding=utf-8参数，例**
```shell
java -Dfile.encoding=utf-8 -jar valveapi-0.0.1-SNAPSHOT.jar --server.port=9090
```
- API访问仅支持Post JSON格式数据访问，并且**Content-type头必须为application/json;charset=UTF-8**,下面为使用范例：
- - RCON命令：
- - - 请求url为 http://localhost:9090/rcon, post JSON数据为:`{"ip":"服务器ip地址:服务器端口号","cmd":"say 啊啊啊","passwd":"服务器RCON密码"}`
- - - 返回JSON数据为:
```json
                {
                "success": true,
                "msg_title": "",
                "msg": "Console: 啊啊啊\n",
                "jsonObject": { },
                "jsonArray": [ ]
                }
 ```
 - - - success代表执行正常，msg为rcon执行返回数据
 - - - 注意：某些命令下，RCON的返回数据为空的时候，success标志为false
 - - A2s_Info：
 - - - 请求url为 http://localhost:8080/servers, post JSON数据为:`{"ip":"服务器ip地址:服务器端口号"}`
 - - - 返回JSON数据为:
 ```json
{
"success": true,
"msg_title": "",
"msg": "",
"jsonObject": {
"name": "XXXXXXXXX",
"map": "c5m1_waterfront",
"players": "0/14",
"time": "21ms",
"visibility": "public"
},
"jsonArray": [ ]
}
  ```
  - - - A2s_info命令为获取服务器当前信息，返回值为嵌套json数据，name为服务器名称，map为当前地图，players为当前人数/Max人数，time为访问延迟,visibility代表服务器是否需要密码进入(private或者public)
  - - A2s_players：
  - - - 请求url为 http://localhost:9090/players, post JSON数据为:`{"ip":"服务器ip地址:服务器端口号"}`
  - - - 返回JSON数据为:
  ```json
                  {
                  "success": true,
                  "msg_title": "",
                  "msg": "",
                  "jsonObject": { },
                  "jsonArray": [
                               {
                               "index": 0,
                               "name": "XXXX",
                               "score": 0,
                               "time": "45875.816"
                               },
                               {
                               "index": 0,
                               "name": "XXXXXX",
                               "score": 9,
                               "time": "44703.492"
                               }
                  ]}
   ```
   - - - A2S_player为获取服务器当前人数信息，返回值仍然为嵌套json数据，jsonarray为json数组，每一个json对象为一个玩家对象
   - - - index为玩家索引(暂时无效)
   - - - name为玩家姓名
   - - - score为玩家当前得分
   - - - time为玩家在线时长，单位秒

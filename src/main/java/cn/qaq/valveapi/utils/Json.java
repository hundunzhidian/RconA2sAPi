package cn.qaq.valveapi.utils;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Getter
@Setter
@Slf4j
@NoArgsConstructor
public class Json {

    public Json(boolean success, String msg) {
        this.success = success;
        this.msg = msg;
    }

    public Json(boolean success, JSONArray jsonArray) {
        this.success = success;
        this.jsonArray = jsonArray;
    }

    public Json(boolean success, JSONObject jsonObject) {
        this.success = success;
        this.jsonObject = jsonObject;
    }

    private boolean success = false;
    private String msg_title = "";
    private String msg = "";
    private JSONObject jsonObject = new JSONObject();
    private JSONArray jsonArray = new JSONArray();
}

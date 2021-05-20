package cn.qaq.valveapi.utils;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ResponseBean {
    private boolean success = false;
    private String msg = "";
    private Object data;
    public static final ResponseBean ERROR(String msg)
    {
        return  new ResponseBean(false,msg,"");
    }
    public static final ResponseBean SUCCESS(Object o)
    {
        return  new ResponseBean(true,null,o);
    }
}

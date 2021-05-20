package cn.qaq.valveapi.controller;

import cn.qaq.valveapi.utils.ResponseBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.ServletWebRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @program: RconA2sAPi
 * @description:
 * @author: QAQ
 * @create: 2021-05-19 17:04
 **/

@Slf4j
@RestController
@ResponseStatus(code= HttpStatus.ACCEPTED)
@EnableConfigurationProperties({ServerProperties.class})
public class ExceptionController implements ErrorController {
    private ErrorAttributes errorAttributes;

    @Autowired
    private ServerProperties serverProperties;


    /**
     * 初始化ExceptionController
     * @param errorAttributes
     */
    @Autowired
    public ExceptionController(ErrorAttributes errorAttributes) {
        Assert.notNull(errorAttributes, "ErrorAttributes must not be null");
        this.errorAttributes = errorAttributes;
    }


    @RequestMapping(value = "/error")
    @ResponseBody
    public ResponseBean error(HttpServletRequest request) {
        ServletWebRequest requestAttributes =  new ServletWebRequest(request);
        Map<String, Object> attr = this.errorAttributes.getErrorAttributes(requestAttributes, false);
        return ResponseBean.ERROR(attr.get("message").toString());
    }


    @ExceptionHandler(value = {Exception.class})
    @ResponseBody
    public ResponseBean missingRequestHeaderException(Exception e) {
        return ResponseBean.ERROR(e.getLocalizedMessage());
    }

    /**
     * 实现错误路径,暂时无用
     * @return
     */
    @Override
    public String getErrorPath() {
        return "";
    }
}

package cn.qaq.valveapi.controller;

import cn.qaq.valveapi.utils.ResponseBean;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @program: RconA2sAPi
 * @description:
 * @author: QAQ
 * @create: 2021-05-20 09:26
 **/
@ControllerAdvice
public class GlobalExceptionHandler {
    @ResponseBody
    @ExceptionHandler(value = {Exception.class})
    public ResponseBean exceptionDispose(Exception e, HttpServletRequest req, HttpServletResponse res) {
        return ResponseBean.ERROR(e.getMessage());
    }
}

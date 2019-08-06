package com.hello.headler;

import com.alibaba.fastjson.JSONObject;
import com.hello.exception.CodeException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
@ResponseBody
public class ExceptionHeadler{
    @ExceptionHandler(CodeException.class)
    public JSONObject defaultExceptionHandler(HttpServletRequest req, CodeException e) {
        JSONObject result = new JSONObject();
        result.put("result",e.isVerifyPass());
        result.put("msg", e.getMessage());
        result.put("code","500");
        e.printStackTrace();
        return result;
    }
}

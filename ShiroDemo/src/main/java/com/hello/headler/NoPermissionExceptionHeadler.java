package com.hello.headler;

import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * 统一异常处理
 */
@ControllerAdvice
public class NoPermissionExceptionHeadler {
    /*IllegalArgumentException*/
    @ResponseBody
    @ExceptionHandler(UnauthorizedException.class)
    public String handleShiroException(HttpServletRequest req,Exception ex) {
        return "无权限";
    }
    @ResponseBody
    @ExceptionHandler(AuthorizationException.class)
    public String AuthorizationException(HttpServletRequest req,Exception ex) {
        return "权限认证失败";
    }
}

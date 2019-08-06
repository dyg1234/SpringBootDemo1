package com.hello.controller;

import com.hello.entity.User;
import com.hello.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/vue")
public class VueDemo {

    @Autowired
    private UserService userService;

    @RequestMapping("/hello")
    @ResponseBody
    public User hello(){
    return userService.findById("tom");
    }

    @RequestMapping("/index")
    public String index(){//默认跳转到static目录下的静态页面
        return "/vue/index.html";
    }
}

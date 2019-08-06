package com.hello.controller;

import com.hello.entity.Author;
import com.hello.service.JpaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/jpa")
public class JpaDemo {

    @Autowired
    private JpaService jpaService;

    @RequestMapping("/findone")
    @ResponseBody
    public String findOne(){
        String path="";
        Author one = jpaService.findOne("402899816a2e6d6a016a2e6d7fda0000");
        return one.toString();
    }


    //条件匹配查询，查询Author的uname字段包含“张”的对象集合，并分页
    @RequestMapping("/findall")
    @ResponseBody
    public String findAll(){
        List<Author> all = jpaService.findAll();
        return all.toString();
    }
}

package com.hello.controller;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

@Controller
public class TestRestemplate {

    @Autowired
    private RestTemplate restTemplate;

    /**
     * restemplate调用shiroDemo的API
     * @return
     */
    @RequestMapping("/test")
    @ResponseBody
    public String get(){
        String url="http://127.0.0.1:8080/vue/hello";

        HttpHeaders httpHeaders = new HttpHeaders();

        httpHeaders.set("content-type", "application/json");

        HttpEntity entity = new HttpEntity(httpHeaders);

        ResponseEntity<JSONObject> resp = restTemplate.postForEntity(url, entity, JSONObject.class);

        return resp.toString();
    }
}

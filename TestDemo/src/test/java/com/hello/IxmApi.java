package com.hello;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;

@SpringBootTest
@RunWith(SpringRunner.class)
public class IxmApi {
    @Autowired
    private RestTemplate restTemplate;

    /**
     * 思明区工作动态轮播图
     * http://www.siming.gov.cn/xxgk/xwgg/gzdt/tree_3288.js
       https://govh5.ixm.gov.cn/api-gateway/gateway/tcxggbwp/wcm-api-gzdt
     */
    @Test
    public void test1() throws UnsupportedEncodingException {
        String url ="http://www.siming.gov.cn/xxgk/xwgg/gzdt/tree_3288.js";
        ResponseEntity<String> resp = restTemplate.getForEntity(url, String.class);

        String jsonpStr = resp.getBody();
        String jsonStr = jsonpStr.substring(jsonpStr.indexOf("(") + 1, jsonpStr.lastIndexOf(")"));
        System.out.println(jsonStr);

    }

    /**
     * 随手拍列表
     */
    @Test
    public void testGetListSnapShot() {
        //String url = "https://rest.ixm.gov.cn/citizencloud/snapshot/listSnapShot";
        String url = "https://govh5.ixm.gov.cn/api-gateway/gateway/hdkkpffq/listSnapShot";

        //参数
        String params = "{\n" +
                "    \"authParams\": {\n" +
                "        \"timestamp\": 1552274247671, \n" +
                "        \"token\": \"ganxHocaQcVBYxUoyRjdOIeUkLm6HxyS4+YuX3GTIBL6/zfseSbE6C9yp8ZUbVLD/tcyIpHUpkOXM+3mKXkwrL91tuluolUj0FoYrY07i2SZtX2ZktDD4ev1pXx0zPkybAhOOL0yPK0t38PJvmkenUcXJ3YSm4uKZ3lCreOOwGc=\", \n" +
                "        \"deviceId\": \"PC\"\n" +
                "    }, \n" +
                "    \"clientParams\": {\n" +
                "        \"os\": \"PC\", \n" +
                "        \"network\": \"\", \n" +
                "        \"deviceId\": \"PC\", \n" +
                "        \"appVersion\": \"\"\n" +
                "    }, \n" +
                "    \"appId\": \"400000000000000001\", \n" +
                "    \"siteId\": \"a3dffa447660434680b982e54f270d88\", \n" +
                "    \"siteAreaCode\": \"350200\", \n" +
                "    \"snapShotType\": \"1\", \n" +
                "    \"lastdate\": 0, \n" +
                "    \"pageSize\": 10, \n" +
                "    \"type\": \"DOWN\", \n" +
                "    \"topicId\": \"qz\"\n" +
                "}";


        //设置请求头
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        //构造请求
        HttpEntity requ = new HttpEntity(params, httpHeaders);

        //发送请求
        ResponseEntity<String> res = restTemplate.postForEntity(url, requ, String.class);

        //String s = postRespBody(url, params);

        //获取结果
        String body = res.getBody();

        System.out.println(body);

    }

    /**
     * 获取分类
     */
    @Test
    public void testGetType() {
        String url="https://govh5.ixm.gov.cn/api-gateway/gateway/hdkkpffq/listCategory";

        //参数
        String params = "{\"type\":1}";

        //设置请求头
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        //构造请求
        HttpEntity requ = new HttpEntity(params, httpHeaders);

        //发送请求
        ResponseEntity<String> res = restTemplate.postForEntity(url, requ, String.class);

        //String s = postRespBody(url, params);

        //获取结果
        String body = res.getBody();

        System.out.println(body);

    }

    /**POST请求静态方法
     *
     * @param url
     * @param params
     * @param headerList
     * @return
     */
    public static String postRespBody(String url, String params, String... headerList) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.set("accept", "application/json");
            httpHeaders.set("content-type", "application/json;charset=utf-8");
            if (headerList.length > 0) {
                for (String header : headerList) {
                    String[] headerArray = header.split(":");
                    httpHeaders.set(headerArray[0], headerArray[1]);
                }
            }
            HttpEntity entity = new HttpEntity(params, httpHeaders);
            ResponseEntity<String> resp = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
            // return resp.getBody();
            if (resp.getStatusCode().is2xxSuccessful()) {
                byte[] bytes = resp.getBody().getBytes("ISO-8859-1");
                return new String(bytes, "UTF-8");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}

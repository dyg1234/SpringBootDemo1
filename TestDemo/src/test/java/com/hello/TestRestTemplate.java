package com.hello;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hello.entity.CategoryTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest
@RunWith(SpringRunner.class)
public class TestRestTemplate {
    @Autowired
    private RestTemplate restTemplate;

    @Test
    public void test1(){
        String url="http://127.0.0.1:8080/getcode";

        HttpHeaders httpHeaders=new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);

        MultiValueMap params=new LinkedMultiValueMap();
        params.add("","");

        HttpEntity entity=new HttpEntity(params,httpHeaders);

        ResponseEntity<String> res = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
        String body = res.getBody();
        System.out.println(body);

    }



    /**
     * 随手拍分类
     */
    @Test
    public void testGetType() {//200
        //String url = "https://rest.ixm.gov.cn/citizencloud/snapshot/listCategory";
        String url="https://govh5.ixm.gov.cn/api-gateway/gateway/hdkkpffq/listCategory";
        //header
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        //param
        LinkedHashMap<String, Object> params = new LinkedHashMap<>();
        params.put("type", "1");

        //构造请求
        HttpEntity requ = new HttpEntity(params, httpHeaders);

        //发送请求
        ResponseEntity<JSONObject> res = restTemplate.postForEntity(url, requ, JSONObject.class);

        JSONObject body = res.getBody();
        int status = Integer.parseInt(body.get("status").toString());
        if (status != 0) {
            //请求成功,JSONArray转List
            JSONArray arr = body.getJSONArray("content");
            String arrStr = JSONArray.toJSONString(arr);
            List<CategoryTest> list = JSONObject.parseArray(arrStr, CategoryTest.class);
            for (CategoryTest categoryTest : list) {
                System.out.println(categoryTest);
            }

        } else {
            System.out.println("请求失败");
        }


    }

    /**
     * 随手拍列表
     */
    @Test
    public void testGetListSnapShot() {//200
        //String url = "https://rest.ixm.gov.cn/citizencloud/snapshot/listSnapShot";
        String url="https://govh5.ixm.gov.cn/api-gateway/gateway/hdkkpffq/listSnapShot";

        //设置请求头
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

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
        JSONObject jsonParams = JSON.parseObject(params);


        //构造请求
        HttpEntity requ = new HttpEntity(jsonParams, httpHeaders);

        //发送请求
        ResponseEntity<String> res = restTemplate.postForEntity(url, requ, String.class);

        //获取结果
        String bodyStr = res.getBody();
        JSONObject body = JSON.parseObject(bodyStr);
        if (Integer.parseInt(body.get("status").toString()) != 0) {
            JSONArray jsonArray = body.getJSONArray("content");
            System.out.println(jsonArray);
        }
        String s = body.toJSONString();


    }

    /**
     * 随手拍列表
     */
    @Test
    public void testGetListSnapShot1() {//200
        String url = "https://rest.ixm.gov.cn/citizencloud/snapshot/listSnapShot";
        //String url="https://govh5.ixm.gov.cn/api-gateway/gateway/hdkkpffq/listSnapShot";

        //设置请求头
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);


        //分步设置参数
        /**
         * "appId": "400000000000000001",
         "siteId": "a3dffa447660434680b982e54f270d88",
         "siteAreaCode": "350200",
         "snapShotType": "1",  // 诉求件类型 1随手拍  2政企直通车
         "lastdate": 0,  //
         "pageSize": 10,
         "type": "DOWN",  // UP向上拉，取给定时间之前的数据（旧数据）,DOWN向下拉，取给定时间之后的数据（新数据）
         "topicId": "qz" // 主题标识
         */
        LinkedHashMap mapParams=new LinkedHashMap();

        LinkedHashMap mapParams1=new LinkedHashMap();
        mapParams1.put("timestamp","1552274247671");
        mapParams1.put("token","ganxHocaQcVBYxUoyRjdOIeUkLm6HxyS4+YuX3GTIBL6/zfseSbE6C9yp8ZUbVLD/tcyIpHUpkOXM+3mKXkwrL91tuluolUj0FoYrY07i2SZtX2ZktDD4ev1pXx0zPkybAhOOL0yPK0t38PJvmkenUcXJ3YSm4uKZ3lCreOOwGc=");
        mapParams1.put("deviceId","PC");

        mapParams.put("authParams",mapParams1);

        LinkedHashMap mapParams2=new LinkedHashMap();
        mapParams2.put("os","PC");
        mapParams2.put("network","");
        mapParams2.put("deviceId","PC");
        mapParams2.put("appVersion","");

        mapParams.put("clientParams",mapParams2);

        mapParams.put("appId","400000000000000001");
        mapParams.put("siteId","a3dffa447660434680b982e54f270d88");
        mapParams.put("siteAreaCode","350200");
        mapParams.put("snapShotType","1");
        mapParams.put("lastdate","0");
        mapParams.put("pageSize","10");
        mapParams.put("type","DOWN");
        mapParams.put("topicId","qz");


        //构造请求
        HttpEntity requ = new HttpEntity(mapParams, httpHeaders);

        //发送请求
        ResponseEntity<JSONObject> res = restTemplate.postForEntity(url, requ, JSONObject.class);

        //获取结果
        JSONObject body = res.getBody();
        if (Integer.parseInt(body.get("status").toString()) != 0) {
            JSONArray jsonArray = body.getJSONArray("content");
            System.out.println(jsonArray);
        }
        String s = body.toJSONString();


    }

    /**
     * 随手拍列表
     */
    @Test
    public void testGetListSnapShot2() {
        String url = "https://rest.ixm.gov.cn/citizencloud/snapshot/listSnapShot";
        //String url="https://govh5.ixm.gov.cn/api-gateway/gateway/hdkkpffq/listSnapShot";

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
        JSONObject jsonParams = JSON.parseObject(params);

        //设置请求头
        String s = postRespBody(url, jsonParams);
        System.out.println(s);

    }


    /**
     * 网关API
     */
    @Test
    public void testGet() {//400错误
        String url = "https://govh5.ixm.gov.cn/api-gateway/gateway/tcxggbwp/wcm-api-gzdt";
        String respBody = getRespBody(url);
        System.out.println(respBody);
    }


    /**
     * JSONP GET 请求
     */
  /*  @Test
    public void testPost() {//200
        String url ="http://www.siming.gov.cn/xxgk/xwgg/gzdt/tree_3288.js";
        String respBody = getRespBody(url);
        System.out.println(respBody);
    }*/

    /**
     * jsonp GET请求
     */
    @Test
    public void testget1() {
        String url ="http://www.siming.gov.cn/xxgk/xwgg/gzdt/tree_3288.js";
        ResponseEntity<String> resp = restTemplate.getForEntity(url, String.class);

        String jsonpStr = resp.getBody();
        String jsonStr = jsonpStr.substring(jsonpStr.indexOf("(") + 1, jsonpStr.lastIndexOf(")"));
        System.out.println(jsonStr);
    }


    /**工具类
     * GET
     *
     * @param url
     * @param headerList
     * @return
     */
    public static String getRespBody(String url, String... headerList) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.set("accept", "application/json");
            httpHeaders.set("content-type", "application/json");
            if (headerList.length > 0) {
                for (String header : headerList) {
                    String[] headerArray = header.split(":");
                    httpHeaders.set(headerArray[0], headerArray[1]);
                }
            }
            HttpEntity entity = new HttpEntity(httpHeaders);
            ResponseEntity<String> resp = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
            if (resp.getStatusCode().is2xxSuccessful()) {
        //byte[] bytes = resp.getBody().getBytes("UTF-8");
                return resp.getBody();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**工具类
     * POST
     *
     * @param url
     * @param params
     * @param headerList
     * @return
     */
    public static String postRespBody(String url, Object params, String... headerList) {
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
            return resp.getBody();
// if (resp.getStatusCode().is2xxSuccessful()) {
// byte[] bytes = resp.getBody().getBytes("ISO-8859-1");
// return new String(bytes, "UTF-8");
// }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * psot带参数
     * @throws Exception
     */
    @Test
    public void testPostParam() throws Exception {
        String url = "http://localhost:8081/aa";
        //headers
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.add("api-version", "1.0");
        //body
        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("id", "1");
        //HttpEntity
        HttpEntity<MultiValueMap> requestEntity = new HttpEntity<MultiValueMap>(requestBody, requestHeaders);
        //post
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, requestEntity, String.class);
        System.out.println(responseEntity.getBody());

        ResponseEntity<String> responseEntity1 = restTemplate.exchange("http://172.26.186.206:8080/hive/list/schemas?appid=admin_test",
                HttpMethod.GET, requestEntity, String.class);
        System.out.println(responseEntity1.getBody());
    }

    /**
     * get带参数
     */
    @Test
    public void testGetParam() {
        String url = "http://172.26.186.206:8080/syncsql/process";
        String timeStramp = String.valueOf(System.currentTimeMillis());
        HttpHeaders headers = new HttpHeaders();
        headers.add("appid", "");
        headers.add("sign", null);
        headers.add("timestamp", timeStramp);

        JSONObject jsonObj = new JSONObject();

        HttpEntity<String> formEntity = new HttpEntity<String>(null, headers);

        Map<String, Object> maps = new HashMap<String, Object>();
        maps.put("sql", "select * from jingfen.d_user_city");
        maps.put("type", 1);
        maps.put("account", "admin_test");

        ResponseEntity<String> exchange = restTemplate.exchange(url + "?sql={sql}&type={type}&account={account}",
                HttpMethod.GET,
                formEntity, String.class, maps);
        String body = exchange.getBody();

    }
}
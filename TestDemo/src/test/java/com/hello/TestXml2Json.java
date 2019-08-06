package com.hello;

import cn.hutool.json.XML;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by dyg on 2019/7/24 14:13
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class TestXml2Json {
    @Autowired
    private RestTemplate restTemplate;

    /**
     * xml数据转换json
     */
    @Test
    public void testXml2Json() throws Exception {
        citizenCertification("","");
    }

    /**
     * 市民我的证照
     * @param trueName
     * @param sfzh
     * @return
     */
    public JSONArray citizenCertification(String trueName, String sfzh) throws Exception {
        //设置请求头
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);

        Map<String,String> params=new HashMap<>();
        params.put("sfzh",sfzh);
        params.put("name",trueName);
        //构造请求
        HttpEntity requ = new HttpEntity(params, httpHeaders);
        ResponseEntity<String> responseEntity = restTemplate.postForEntity("", requ, String.class);


        String body = responseEntity.getBody();
        JSONObject result= JSONObject.parseObject(body);
        if (result.getIntValue("code")!=0){
            throw new Exception("服务器繁忙,请稍后再试!");
        }
        String xml = result.getString("content");

        cn.hutool.json.JSONObject jsonObject = XML.toJSONObject(xml);
        JSONObject json = JSONObject.parseObject(jsonObject.toString());
        JSONObject xmlResult = json.getJSONObject("Result");
        if (!"000".equals(xmlResult.getString("Status"))){
            throw new Exception(xmlResult.getString("ErrorMsg"));
        }
        String xmlData = xmlResult.getJSONObject("Output").getString("content");
        if (StringUtils.isEmpty(xmlData)){
            return null;
        }
        JSONArray jsonArray = xml3Json(xmlData);

        JSONArray jsonArrayData = jsonArray.getJSONObject(0).getJSONObject("Nodes").getJSONArray("Node");
       /* for (int i = 0; i < jsonArrayData.size(); i++) {
            JSONObject resultJson = jsonArrayData.getJSONObject(i);
            String name = resultJson.getString("Name");//证书名
            String MaxTypeName = resultJson.getString("MaxTypeName");//证书类型
            String DataTime = resultJson.getString("DataTime");//颁发时间
            String LicenseID = resultJson.getString("LicenseID");//证书编号
        }*/
        return jsonArrayData;
    }


    public JSONArray xml3Json(String xmlStr) throws DocumentException {
        JSONArray arr=new JSONArray();
        org.dom4j.Document document = DocumentHelper.parseText(xmlStr);
        List<Element> nodes  = document.getRootElement().elements("Nodes");
        for (org.dom4j.Element node : nodes) {
            xmlToJsonList(node);
            String asXML = node.asXML();
            //正常模式的xml快速转json格式
            cn.hutool.json.JSONObject jsonObject = XML.toJSONObject(asXML);
            JSONObject json = (JSONObject) JSON.parse(jsonObject.toString());

            arr.add(json);
        }
        return arr;
    }

    public void xmlToJsonList(org.dom4j.Element publicEle) {
        List<org.dom4j.Element> elements = publicEle.elements();
        for(org.dom4j.Element element : elements){
            if(element.attribute("value") != null){
                //先得到该值
                String value = element.attributeValue("value");
                //再去除value属性
                element.remove(element.attribute("value"));
                //再为该节点添加值
                element.setText(value);
            }else{
                //递归
                xmlToJsonList(element);
            }
        }
    }
}

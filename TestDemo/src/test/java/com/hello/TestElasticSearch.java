package com.hello;

import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class TestElasticSearch{
    
   /* //@Autowired
    private RestHighLevelClient restHighLevelClient;
    
    //@Autowired
    private RestClient restClient;

    //@Test//使用ES的客户端向ES请求查询索引信息，根据id查询
    public void test1(){
        //定义一个搜索请求对象
        SearchRequest searchRequest = new SearchRequest("esindex");
        //指定type
        searchRequest.types("doc");//定义SearchSourceBuilder
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        //设置使用termQuery
        searchSourceBuilder.query(QueryBuilders.termQuery("name", "张三"));
        //过虑源字段，不用设置源字段，取出所有字段
        //searchSourceBuilder.fetchSource()
        searchRequest.source(searchSourceBuilder);
        //最终要返回的课程信息

        Map<String, User> map = new HashMap<>();
        try {


            SearchResponse search = restHighLevelClient.search(searchRequest);
            SearchHits hits = search.getHits();
            SearchHit[] searchHits = hits.getHits();
            for (SearchHit hit : searchHits) {
                User user = new User();
                //获取源文档的内容
                Map<String, Object> sourceAsMap = hit.getSourceAsMap();
                //课程id
                String name = (String) sourceAsMap.get("name");
                String pw = (String) sourceAsMap.get("pw");
                String permission = (String) sourceAsMap.get("permission");
                user.setName(name);
                user.setPw(pw);
                user.setPermission(permission);

            map.put("user",user);
            }

            System.out.println(map);



        } catch (IOException e) {
            e.printStackTrace();
        }


    }
    
    
    //@Test
    public void test2(){

        //搜索参数
        User userSearchParam=new User();
        userSearchParam.setName("张三");
        //初始化搜索源字段
        String source_field="name,pw";
        //分页起始索引
        int page=1;
        int size=3;
        //索引库名称
        String index="esindex";


        //创建搜索请求对象
        SearchRequest searchRequest = new SearchRequest(index);
        //设置搜索类型
        searchRequest.types("doc");

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        //过虑源字段
        String[] source_field_array = source_field.split(",");
        searchSourceBuilder.fetchSource(source_field_array, new String[]{});
        //创建布尔查询对象
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        //搜索条件
        //根据关键字搜索
        if (StringUtils.isNotEmpty(userSearchParam.getName())) {
            MultiMatchQueryBuilder multiMatchQueryBuilder = QueryBuilders.multiMatchQuery(userSearchParam.getName(), "name", "pw", "permission").minimumShouldMatch("70%").field("name", 10);
            boolQueryBuilder.must(multiMatchQueryBuilder);
        }
        if (StringUtils.isNotEmpty(userSearchParam.getPermission())) {
            boolQueryBuilder.filter(QueryBuilders.termQuery("permission", userSearchParam.getPermission()));
        }
        if (StringUtils.isNotEmpty(userSearchParam.getPw())) {
            boolQueryBuilder.filter(QueryBuilders.termQuery("pw", userSearchParam.getPw()));
        }


        //设置boolQueryBuilder到searchSourceBuilder
        searchSourceBuilder.query(boolQueryBuilder);
        //设置分页参数
        if (page <= 0) {
            page = 1;
        }
        if (size <= 0) {
            size = 12;
        }
        //起始记录下标
        int from = (page - 1) * size;
        searchSourceBuilder.from(from);
        searchSourceBuilder.size(size);

        //设置高亮
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.preTags("<font class='eslight'>");
        highlightBuilder.postTags("</font>");
        //设置高亮字段
        highlightBuilder.fields().add(new HighlightBuilder.Field("name"));
        searchSourceBuilder.highlighter(highlightBuilder);

        searchRequest.source(searchSourceBuilder);


        try {
            //执行搜索
            SearchResponse searchResponse = restHighLevelClient.search(searchRequest);
            //获取响应结果
            SearchHits hits = searchResponse.getHits();
            //匹配的总记录数
            long totalHits = hits.totalHits;

            //添加总记录数
            //queryResult.setTotal(totalHits);
            SearchHit[] searchHits = hits.getHits();

            //设置结果集
            List<User> list = new ArrayList<>();

            for (SearchHit hit : searchHits) {
                User user = new User();
                //源文档
                Map<String, Object> sourceAsMap = hit.getSourceAsMap();
                //取出name
                String name = (String) sourceAsMap.get("name");

                    //取出高亮字段name
                    Map<String, HighlightField> highlightFields = hit.getHighlightFields();
                    if (highlightFields != null) {
                        HighlightField highlightFieldName = highlightFields.get("name");
                        if (highlightFieldName != null) {
                            Text[] fragments = highlightFieldName.fragments();
                            StringBuffer stringBuffer = new StringBuffer();
                            for (Text text : fragments) {
                                stringBuffer.append(text);
                            }
                            name = stringBuffer.toString();
                        }
                    }

                //添加到结果集
                user.setName(name);

                //设置结果集
                String pw = (String) sourceAsMap.get("pw");
                user.setPw(pw);
                String permission = (String) sourceAsMap.get("permission");
                user.setPermission(permission);
                list.add(user);
            }

            System.out.println(list);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }*/
}

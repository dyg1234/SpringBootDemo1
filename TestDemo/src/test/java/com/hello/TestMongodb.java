package com.hello;


import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSDownloadStream;
import com.mongodb.client.gridfs.model.GridFSFile;
import org.apache.commons.io.IOUtils;
import org.bson.types.ObjectId;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.InputStream;

@SpringBootTest
@RunWith(SpringRunner.class)
public class TestMongodb {
    @Autowired
    GridFsTemplate gridFsTemplate;
    @Autowired
    GridFSBucket gridFSBucket;
    @Autowired
    MongoTemplate mongoTemplate;

    //@Test//下载
    public void test1() {
        try {
            //从GridFS中取模板文件内容
            //根据文件id查询文件
            GridFSFile gridFSFile = gridFsTemplate.findOne(Query.query(Criteria.where("_id").is("5a791725dd573c3574ee333f")));

            //打开一个下载流对象
            GridFSDownloadStream gridFSDownloadStream = gridFSBucket.openDownloadStream(gridFSFile.getObjectId());
            //创建GridFsResource对象，获取流
            GridFsResource gridFsResource = new GridFsResource(gridFSFile, gridFSDownloadStream);

            //输出流下载
            //InputStream in = gridFsResource.getInputStream();
            //IOUtils.copy(inputStream,fileOutputStream);

            //从流中取数据
            String content = IOUtils.toString(gridFsResource.getInputStream(), "utf-8");
                System.out.println(content);
            } catch (Exception e) {
                e.printStackTrace();
            }


    }
    //@Test//上传
    public void test2(){
        //保存文件到gridFS
        InputStream inputStream = IOUtils.toInputStream("string");
        ObjectId id = gridFsTemplate.store(inputStream, "名称");
        System.out.println(id);


    }



}

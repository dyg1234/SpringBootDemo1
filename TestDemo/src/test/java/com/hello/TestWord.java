package com.hello;

import com.alibaba.fastjson.JSON;
import org.apdplat.word.WordSegmenter;
import org.apdplat.word.segmentation.Word;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * Created by dyg on 2019/8/6 16:46
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class TestWord {

    @Test
    public void testFenCi1() {

    }
    @Test
    public void testFenCi() {
        String title="我叫李太白，我是一个诗人，我生活在唐朝";

        //移除停用词进行分词
        List<Word> list = WordSegmenter.seg(title);

        System.out.println(JSON.toJSONString(list));

        //保留停用词
        List<Word> lists = WordSegmenter.segWithStopWords(title);
        System.out.println(JSON.toJSONString(lists));



    }

}

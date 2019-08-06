package com.hello;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.TimeUnit;

@SpringBootTest
@RunWith(SpringRunner.class)
public class TestRedisTemplate {
    @Autowired
    RedisTemplate redisTemplate;

    @Test
    public void testRedis(){
        //存字符串类型,设置过期时间30s
        redisTemplate.boundValueOps("StrKey").set("StrVal",30, TimeUnit.SECONDS);

        //获取有效时间
        Long strTimeout = redisTemplate.getExpire("StrKey");
        System.out.println("STR剩余时间："+strTimeout+"秒");

        //存map类型
        redisTemplate.boundHashOps("mapKey").put("key","8");
        //更新生存时间
        redisTemplate.expire("mapKey", 30, TimeUnit.SECONDS);
        //获取有效时间
        Long mapTimeout = redisTemplate.getExpire("mapKey");
        System.out.println("MAP剩余时间："+mapTimeout+"秒");


        //取字符串
        String str = (String) redisTemplate.opsForValue().get("str");
        System.out.println("获取字符串类型值:"+str);
        //取map
        String o = (String) redisTemplate.opsForHash().get("mapKey","key");
        System.out.println("获取map类型值:"+o);


        //String类型自增
        Double incrementSTR = redisTemplate.boundValueOps("num").increment( 1.0);

        //map类型自增
        Double incrementMAP = redisTemplate.boundHashOps("nm").increment("val", 20.0);

        System.out.println(incrementSTR);
        System.out.println(incrementMAP);

    }


    @Test
    public void testStr(){//不存在返回null
        //取字符串
        String str = (String) redisTemplate.opsForValue().get("111111111111111111");

        System.out.println(str);
    }





}

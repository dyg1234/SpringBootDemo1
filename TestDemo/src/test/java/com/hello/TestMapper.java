package com.hello;

import com.hello.dao.UserMapper;
import com.hello.entity.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class TestMapper {
    @Autowired
    private UserMapper userMapper;

    @Test//增加
    public void test1(){
        User u=new User("aaa","bbb","");
        userMapper.updateUserPw(u);
    }

    @Test//保存
    public void test2(){
        double random = Math.random();
        User u=new User("test"+random,"112","admin");
        int roe=userMapper.saveUser(u);
    }
}

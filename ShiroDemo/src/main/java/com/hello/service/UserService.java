package com.hello.service;

import com.hello.dao.UserMapper;
import com.hello.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;

    public User findById(String name){
        return userMapper.findByName(name);
    }

    public int update(User user) {
        return userMapper.updateUserPw(user);
    }

    public int add(User u) {
        return userMapper.saveUser(u);
    }

    public List<User> findAll() {
        return userMapper.findAll();
    }
}

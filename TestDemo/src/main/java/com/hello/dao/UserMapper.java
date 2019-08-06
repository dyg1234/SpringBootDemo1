package com.hello.dao;

import com.hello.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface UserMapper {

    @Insert("insert into User values(#{name},#{pw},#{permission})")
    int saveUser(User user);

    @Update("update User set pw= #{pw} where name= #{name}")
    int updateUserPw(User user);

    @Select("select u.name,u.pw,u.permission from user u where name=#{name}")
    User findByName(String name);

    @Select("select * from user")
    List<User> findAll();
}

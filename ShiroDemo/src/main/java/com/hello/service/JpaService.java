package com.hello.service;

import com.hello.dao.AuthorRepository;
import com.hello.entity.Author;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JpaService {
    @Autowired
    private AuthorRepository authorRepository;

    public Author findOne(String s) {

        return  authorRepository.findById(s).get();
    }

    //条件匹配查询，查询Author的uname字段包含“张”的对象集合，并分页
    public List<Author> findAll() {
        //条件匹配器
        //自定义字符串的匹配器实现模糊查询
        ExampleMatcher exampleMatcher = ExampleMatcher.matching().withMatcher("uname",
                ExampleMatcher.GenericPropertyMatchers.contains());


        Author author = new Author();
        author.setUname("张");

        //创建条件实例
        Example<Author> example = Example.of(author, exampleMatcher);

        //分页
        PageRequest pageRequest = new PageRequest(0, 1);

        Page<Author> all = authorRepository.findAll(example,pageRequest);

        //总条数
        long size = all.getTotalElements();
        //数据集合
        List<Author> lists = all.getContent();

        return lists;
    }
}

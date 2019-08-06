package com.hello.controller;

import com.hello.entity.User;
import com.hello.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 *
 * Shiro的认证注解处理是有内定的处理顺序的，
 * 如果有个多个注解的话，前面的通过了会继续检查后面的，
 * 若不通过则直接返回，处理顺序依次为（与实际声明顺序无关）：
 @RequiresRoles
 @RequiresPermissions
 @RequiresAuthentication  验证用户是否登录
 @RequiresUser            验证用户是否被记忆
 @RequiresGuest           验证是否是一个guest的请求，与@RequiresUser完全相反
 */


@Controller
public class ShiroThymeleafDemo {

    @Autowired
    private UserService userService;

    @RequestMapping("/hello")
    public String testThymaleaf(Model model){
        model.addAttribute("index","欢迎页面！");
        return "testThymeleaf";
    }

    /**@RequiresRoles(value={"admin","user"},logical= Logical.AND)
     * 表示需要admin和user两个角色，and连接
     * @return
     */
    @RequiresRoles(value={"admin","user"},logical= Logical.AND)
    @RequestMapping("/user/add")
    public String add(User u,Model model){
        try {
            System.out.println("用户添加");
            int row=userService.add(u);
            model.addAttribute("msg","添加成功！");
            return "user/add";
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("msg","添加失败！");
            return "user/add";
        }
    }
    @RequestMapping("/user/find")
    public String find(Model model){
        System.out.println("用户查看");
        List<User> users= userService.findAll();
        model.addAttribute("users",users);
        return "user/find";
    }


    @RequestMapping("/user/update")
    public String update(@Param("name") String name,Model model){
        System.out.println("用户更新回显");
        User u = userService.findById(name);
        model.addAttribute("name",u.getName());
        return "user/update";
    }

    @RequestMapping("/user/updateUser")
    public String updateUser(User user, Model model){
        try {
            System.out.println("用户更新提交");
            User u = userService.findById(user.getName());
            if (u.getPw().equals(user.getPw())){
                model.addAttribute("msg","密码相同，更新失败！");
                model.addAttribute("name",user.getName());
                return "user/update";
            }
            int row=userService.update(user);
            model.addAttribute("msg","更新成功！");
            return "user/update";
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("msg","更新失败！");
            model.addAttribute("name",user.getName());
            return "user/update";
        }
    }


    //权限认证
    @RequiresPermissions("perms[user:del]")
    @RequestMapping("/user/del")
    public String del(){//此功能无权限
            System.out.println("用户删除");
            return "user/del";
    }


    @RequestMapping("/tologin")
    public String tologin(){
        System.out.println("用户登陆");
        return "user/login";
    }

    @RequestMapping("/login")
    public String login(User user, Model model){
        //1,获取subject
        Subject sub = SecurityUtils.getSubject();
        //2，封装用户数据
        UsernamePasswordToken token = new UsernamePasswordToken(user.getName(),user.getPw());
        //3，执行登陆方法

        try {
            sub.login(token);
        } catch (UnknownAccountException e) {
            //登陆失败，用户不存在
            model.addAttribute("msg","用户不存在！");
            return "user/login";
        }catch (IncorrectCredentialsException e){
            //登陆失败，密码错误
            model.addAttribute("msg","密码错误!");
            return "user/login";
        }

        //登陆成功
        model.addAttribute("index","欢迎您！"+ user.getName());
        model.addAttribute("name",user.getName());
        return "testThymeleaf";

    }

    @RequestMapping("/noAuth")
    public String noAuth(){
        System.out.println("未授权");
        return "user/noAuth";
    }


}

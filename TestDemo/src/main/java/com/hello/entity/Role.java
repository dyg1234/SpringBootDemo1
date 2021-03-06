package com.hello.entity;

import java.io.Serializable;
import java.util.List;

/**
 * 角色（管理员，普通用户等）
 *
 */

public class Role implements Serializable {
    private Integer id;
    private String rolename;
    private List<Permission> permissionList;// 一个角色对应多个权限
    private List<User> userList;// 一个角色对应多个用户

    public Role() {
    }

    public Role(String rolename, List<Permission> permissionList, List<User> userList) {
        this.rolename = rolename;
        this.permissionList = permissionList;
        this.userList = userList;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRolename() {
        return rolename;
    }

    public void setRolename(String rolename) {
        this.rolename = rolename;
    }

    public List<Permission> getPermissionList() {
        return permissionList;
    }

    public void setPermissionList(List<Permission> permissionList) {
        this.permissionList = permissionList;
    }

    public List<User> getUserList() {
        return userList;
    }

    public void setUserList(List<User> userList) {
        this.userList = userList;
    }
}

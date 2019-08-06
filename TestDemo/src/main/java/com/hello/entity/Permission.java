package com.hello.entity;

import java.io.Serializable;

/**
 * 权限（增删改查等）
 *
 */
public class Permission implements Serializable{
    private Integer id;
    private String permissionname;

    private Role role;// 一个权限对应一个角色

    public Permission() {
    }

    public Permission(String permissionname, Role role) {
        this.permissionname = permissionname;
        this.role = role;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPermissionname() {
        return permissionname;
    }

    public void setPermissionname(String permissionname) {
        this.permissionname = permissionname;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}

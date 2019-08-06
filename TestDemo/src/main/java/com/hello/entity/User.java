package com.hello.entity;


import java.io.Serializable;

public class User implements Serializable {

	private String name;

	private String pw;

	private String permission;

	public User() {
	}

	public User(String name, String pw, String permission) {
		this.name = name;
		this.pw = pw;
		this.permission = permission;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPw() {
		return pw;
	}

	public void setPw(String pw) {
		this.pw = pw;
	}

	public String getPermission() {
		return permission;
	}

	public void setPermission(String permission) {
		this.permission = permission;
	}

	@Override
	public String toString() {
		return "User{" +
				"name='" + name + '\'' +
				", pw='" + pw + '\'' +
				", permission='" + permission + '\'' +
				'}';
	}
}
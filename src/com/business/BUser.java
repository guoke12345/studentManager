package com.business;

import java.util.List;

import com.stuManager.Models.User;

public class BUser {

	/*
	 * 根据用户名查找用户
	 */
	public User searchUserByName(String userName)
	{
		User user=User.dao.findFirst("select * from user where username='"+userName+"'");
		return user;
	}
	/**
	 * 根据id查找用户
	 *
	 *<p>Title:findUserById</p>
	 *@author weichao
	 *@date Mar 22, 2016
	 */
	public User findUserById(int id){
		return User.dao.findById(id);
	}
	/*
	 * 超级管理员可以查找所有的用户
	 */
	public List<User> findAllUser()
	{
		List<User> list=User.dao.find("select * from user");
		return list;
	}
	
	/*
	 * 超级管理员可以删除账号
	 */
	public Boolean deleteUserById(int userId)
	{
		return User.dao.deleteById(userId);
		
	}
	
	/*
	 * 超级管理员可以添加账号
	 */
	public Boolean addUser(String username,String password,int state)
	{
		return new User().set("username", username).set("password", password).set("state", state).save();
	}
}

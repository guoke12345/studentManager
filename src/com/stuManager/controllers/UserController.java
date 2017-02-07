package com.stuManager.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.business.BUser;
import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.mysql.jdbc.Util;
import com.stuManager.Models.User;
import com.stuManager.interceptor.PermissionIntercepter;
import com.stuManager.utils.Utils;

public class UserController extends Controller {

	BUser buser=new BUser();
	public void index(){
		renderFreeMarker("/login.html");
	}
	/*
	 * 登录检验
	 */
	public void login()
	{
		User user=buser.searchUserByName(getPara("username"));
		this.getSession().setMaxInactiveInterval(3*60*60);
		//List<User> list=buser.findAllUser();
		if(user==null)
		{
			setAttr("msg", "用户不存在！");
			//返回登陆页面
			renderFreeMarker("/login.html");
		}
		else
		{
			//判断密码是否正确！
			String pwd = getPara("password");
			String SHAPwd = Utils.getInstace().getSHAStr("gj"+pwd+"jy");
			if(user.getStr("password").equals(Utils.getInstace().getMD5Str(SHAPwd)))
				{					 
				//登陆成功，保存session进入首页
				setSessionAttr("sessionUserName",getPara("username"));
				setSessionAttr("leftStatus", user.getInt("state"));
				if(user.getInt("state") == 0){
					redirect("/stu");
				}else{
					redirect("/upload/toStuTable");
				}
				}
			else
			{
				setAttr("msg", "密码错误！");
				//返回登陆页面
				renderFreeMarker("/login.html");
			}
		}
		
	}
	@Before(PermissionIntercepter.class)
	public void list(){
		List<User> list = buser.findAllUser();
		setAttr("userList", list);
		renderFreeMarker("/userList.html");
	}
	/**
	 * <p>Title:exit</p>
	 *<p>Description: 退出登录</p>
	 *@author weichao
	 *@date Mar 9, 2016
	 */
	public void exit(){
		removeSessionAttr("sessionUserName");
		redirect("/user");
	}
	/**
	 * 删除 用户
	 *
	 *<p>Title:deleteUser</p>
	 *@author weichao
	 *@date Mar 23, 2016
	 */
	@Before(PermissionIntercepter.class)
	public void deleteUser(){
		boolean b = buser.deleteUserById(getParaToInt("id"));
		redirect("/user/list");
	}
	/**
	 * 重置密码
	 *
	 *<p>Title:resetPass</p>
	 *@author weichao
	 *@date Mar 23, 2016
	 */
	@Before(PermissionIntercepter.class)
	public void resetPass(){
		HashMap<String, String> map = new HashMap<String, String>();
		try {
			User user = User.dao.findById(getParaToInt("id"));
			String SHARePwd = Utils.getInstace().getSHAStr("gj000000jy");
			user.set("password",Utils.getInstace().getMD5Str(SHARePwd));
			if(user.update()){
				map.put("message", "重置成功！");
			}else{
				map.put("message", "重置失败！");
			}
		} catch (Exception e) {
			map.put("message", "重置失败！");
		}finally{
			renderJson(map);
		}
	}
	/**
	 * 跳转到添加用户界面
	 *
	 *<p>Title:toAddUser</p>
	 *@author weichao
	 *@date Mar 22, 2016
	 */
	@Before(PermissionIntercepter.class)
	public void toAddUser(){
		renderFreeMarker("/addUser.html");
	}
	/**
	 * 跳转到修改密码界面
	 *
	 *<p>Title:toUpdatePass</p>
	 *@author weichao
	 *@date Mar 23, 2016
	 */
	@Before(PermissionIntercepter.class)
	public void toUpdatePass(){
		String realname = this.getSessionAttr("sessionUserName");
		User user=buser.searchUserByName(realname);
		setAttr("user", user);
		renderFreeMarker("/updatePassword.html");
	}
	/**
	 * 修改密码
	 *
	 *<p>Title:updatePass</p>
	 *@author weichao
	 *@date Mar 23, 2016
	 */
	@Before(PermissionIntercepter.class)
	public void updatePass(){
		User user = getModel(User.class);
		String pwd = getPara("user.password");
		String SHAPwd = Utils.getInstace().getSHAStr("gj"+pwd+"jy");
		user.set("password",Utils.getInstace().getMD5Str(SHAPwd));
		user.update();
		redirect("/stu/index");
	}
	/**
	 * 判断用户是否存在
	 * 0：不存在
	 *	1：存在
	 *<p>Title:judgeUser</p>
	 *@author weichao
	 *@date Mar 22, 2016
	 */
	public void judgeUser(){
		HashMap<String, Integer> map = new HashMap<String, Integer>();
		String userName = getPara("userName");
		User user = new User();
		user = buser.searchUserByName(userName);
		if(user == null){
			map.put("status", 0);
		}else{
			map.put("status", 1);
		}
		renderJson(map);
	}
	/**
	 * 添加用户
	 *
	 *<p>Title:addUser</p>
	 *@author weichao
	 *@date Mar 23, 2016
	 */
	@Before(PermissionIntercepter.class)
	public void addUser(){
		String username = getPara("username");
		String password = getPara("password");
		int state = getParaToInt("status");
		buser.addUser(username, Utils.getInstace().pwdEncryption(password), state);
		redirect("/user/list");
	}
	/**
	 * 判断密码是否与用户匹配
	 * 1:成功
	 * 0：失败
	 *<p>Title:judegPass</p>
	 *@author weichao
	 *@date Mar 23, 2016
	 */
	public void judgePass(){
		HashMap<String, Integer> map = new HashMap<String, Integer>();
		String realname = this.getSessionAttr("sessionUserName");
		User user=buser.searchUserByName(realname);
		String password = user.getStr("password");
		if(Utils.getInstace().pwdEncryption(getPara("password")).equals(password)){
			map.put("status", 1);
		}else{
			map.put("status", 0);
		}
		renderJson(map);
	}
}

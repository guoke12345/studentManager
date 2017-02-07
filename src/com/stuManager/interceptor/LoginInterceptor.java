package com.stuManager.interceptor;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.core.Controller;

public class LoginInterceptor implements Interceptor {

	@Override
	public void intercept(Invocation arg0) {
		// TODO Auto-generated method stub
		/*
		 * 此处拦截登录，应该设置全局拦截器 
		 */
		Controller controller=arg0.getController();
		String realname = controller.getSessionAttr("sessionUserName");
		    if(arg0.getActionKey().indexOf("login")!=-1 || realname!=null){
		    	controller.setAttr("sessionUserName", realname);
		    	controller.setAttr("leftStatus", controller.getSessionAttr("leftStatus"));
		    	arg0.invoke();
		    }else{
		    	controller.setAttr("msg", "亲，登录后才能访问哦！");
		    	controller.renderFreeMarker("/login.html");
		        }
	}
}

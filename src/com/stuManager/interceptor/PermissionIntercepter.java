package com.stuManager.interceptor;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.core.Controller;

public class PermissionIntercepter implements Interceptor{

	@Override
	public void intercept(Invocation arg0) {
		// TODO Auto-generated method stub
		Controller controller=arg0.getController();
		String realname = controller.getSessionAttr("sessionUserName");
		    if(arg0.getActionKey().indexOf("login")!=-1 || realname!=null){
		    	controller.setAttr("sessionUserName", realname);
		    	int permission = controller.getSessionAttr("leftStatus");
		    	if(permission == 0){
		    		arg0.invoke();
		    	}else{
		    		controller.setAttr("msg", "您没有权限访问该页面！");
			    	controller.renderFreeMarker("/permissionError.html");
		    	}
		    }
	}

}

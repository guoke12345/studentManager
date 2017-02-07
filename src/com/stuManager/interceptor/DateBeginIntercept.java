package com.stuManager.interceptor;

import java.util.Date;
import java.util.List;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.render.Render;
import com.mysql.fabric.xmlrpc.base.Data;

public class DateBeginIntercept implements Interceptor {

	public void intercept(Invocation arg0) {
		// TODO Auto-generated method stub
		/*
		 * 此处拦截开学日期，
		 */
		Controller controller=arg0.getController();
//		Data dateNow = new Data();
		String message = "";
		String sql = "select date from setDate order by date desc";
		List<Record> dateList= Db.find(sql);
		Date beginDate = controller.getParaToDate("inputDate");
		if(dateList.isEmpty()){
			arg0.invoke();
		}else{
			if(dateList.get(0).getDate("date").before(beginDate)){
				arg0.invoke();
			}else{
				message = "日期选择错误！";
				controller.renderJson(message);
			}
		}
		
	}
}

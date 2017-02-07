package com.stuManager.interceptor;

import java.util.List;

import com.business.BStudent;
import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.core.Controller;
import com.stuManager.Models.Student;

public class DeleteClassIntercepter implements Interceptor {

	@Override
	public void intercept(Invocation arg0) {
		// TODO Auto-generated method stub
		Controller ct=arg0.getController();
		int classId = ct.getParaToInt("id");
		BStudent bstudent= new BStudent();
	    List<Student>list = bstudent.getStudentsWithClassId(classId);
	    if(list.isEmpty())
	    {
	    	arg0.invoke();
	    }else
	    {
	    	ct.setAttr("message", "该班级存在学生信息，不能删除！");
	    	ct.setAttr("status", 1);
	    	ct.renderJson();
	    }
	}

}

package com.stuManager.interceptor;

import java.util.List;

import com.business.BPro_info;
import com.business.BProject;
import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.core.Controller;
import com.stuManager.Models.Pro_info;

public class DeleteProIntercepter implements Interceptor {

	@Override
	public void intercept(Invocation arg0) {
		// TODO Auto-generated method stub
		
		Controller ct=arg0.getController();
		int proId = ct.getParaToInt("id");
		BProject bproject= new BProject();
		int isParent= bproject.findProById(proId).getInt("parent_id");
		if(isParent==0)
		{
			//这是项目分类，判断该分类下面有没有子类项目
			Boolean mark = bproject.getProjectWithParentId(proId).isEmpty();
			if(mark)
			{
				//表示没有子类，可以该项目删除分类
				ct.setAttr("message", "删除分类成功！");
				ct.setAttr("status", 0);
				arg0.invoke();
			}else
			{
				ct.setAttr("message", "请先删除子项目！");
				ct.setAttr("status", 2);
				ct.renderJson();
			}
			
		}
		else
		{
			 BPro_info bpro_info= new BPro_info();
			 List<Pro_info> list=bpro_info.getInfoWithCondition(proId);
				Boolean mark = list.isEmpty();
				if(mark)
				{
					ct.setAttr("message", "删除成功！");
					ct.setAttr("status", 0);
					arg0.invoke();
				}else
				{
					ct.setAttr("message", "确定删除该项目下的所有项目信息？");
					ct.setAttr("status", 1);
					ct.setAttr("proId", proId);
					ct.renderJson();					
				}
		}
		
	}

}

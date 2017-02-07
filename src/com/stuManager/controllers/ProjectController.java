package com.stuManager.controllers;


import java.util.HashMap;
import java.util.List;

import com.business.BPro_info;
import com.business.BProject;
import com.business.BStudent;
import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.stuManager.Models.Pro_info;
import com.stuManager.Models.Project;
import com.stuManager.interceptor.DeleteProIntercepter;
import com.stuManager.interceptor.PermissionIntercepter;
public class ProjectController extends Controller{
	BProject bp = new BProject();
	
	/*
	 * This is the method default
	 */
	public void index() {
		//renderJson(bp.getAllProject());
		
		setAttr("proList", bp.findPro());
		renderFreeMarker("/project.html");
	}
	
	/*
	 * 删除项目，拦截判断是否有项目信息(或项目子类)
	 */
	@Before(DeleteProIntercepter.class)
	public void deletePro()
	{
		 bp.deletProById(getParaToInt("id"));
		 renderJson();
		 //redirect("/pro/index");
	}
	@Before(PermissionIntercepter.class)
	public void deletProWithAllProInfo()
	{
		BPro_info  bpro_info = new BPro_info();
		int id= getParaToInt("id");
		int mark = bpro_info.deletAllProInfoInPid(id);
		 bp.deletProById(getParaToInt("id"));
		 setAttr("message", "删除成功！");
		 setAttr("status", 0);
		 renderJson();
	}
	
	/*
	 * 跳转到添加项目界面
	 */
	public void toAddProPage()
	{
		setAttr("id", getParaToInt("id"));
		renderFreeMarker("/addPro.html");
	}
	/*
	 *添加项目
	 */
	@Before(PermissionIntercepter.class)
	public void addPro()
	{
		 bp.addProByFatherId(getParaToInt("id"), getPara("pname"),getParaToInt("ifmark"));
		 redirect("/pro");
	}
	/**
	 *  判断  项目名称 是否存在
	 * 1：存在
	 * 2：不存在
	 *
	 *<p>Title:judgeByProjectName</p>
	 *@author weichao
	 *@date Mar 23, 2016
	 */
	public void judgeByProjectName(){
		HashMap<String, Integer> map = new HashMap<String, Integer>();
		int parent_id = getParaToInt("parent_id");
		String pname = getPara("pname");
		List<Project> list =  bp.findWithNameAndParent_id(parent_id, pname);
		if(list.isEmpty()||list.size()==0){
			map.put("statu", 0);
		}else{
			map.put("statu", 1);
		}
		renderJson(map);
	}
	
}

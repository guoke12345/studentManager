package com.stuManager.controllers;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.business.BClasses;
import com.business.BMajor;
import com.business.BStudent;
import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Record;
import com.stuManager.Models.Basicinfo;
import com.stuManager.Models.Classes;
import com.stuManager.Models.Major;
import com.stuManager.Models.Student;
import com.stuManager.interceptor.DateBeginIntercept;
import com.stuManager.interceptor.DeleteClassIntercepter;
import com.stuManager.interceptor.PermissionIntercepter;

public class StuController extends Controller {
	BStudent bs = new BStudent();
	BMajor bm = new BMajor();
	BClasses bc = new BClasses();
	/**
	 * 查询所有学生个人信息
	 *
	 *<p>Title:index</p>
	 *@author weichao
	 *@date Mar 10, 2016
	 */
	@Before(PermissionIntercepter.class)
	public void index()
	{
		List<Student> list = bs.getAllStudent(); 
		setAttr("stuList", list);
		setAttr("marjorList",bm.getAllMarjor());
		renderFreeMarker("/student.html");
//		renderJson(bs.getAllStudent());
	}
	
	@Before(PermissionIntercepter.class)
	public void searchStu()
	{
		String cid= getPara("c_id");
		String mid= getPara("m_id");
		List<Student> list = bs.searchStudent(cid, mid);
		setAttr("stuList", list);
		setAttr("marjorList",bm.getAllMarjor());
		renderFreeMarker("/student.html");
	}
	
	/**
	 * 跳转到添加学生
	 *
	 *<p>Title:toAddStudent</p>
	 *@author weichao
	 *@date Mar 21, 2016
	 */
	public void toAddStudent(){
		setAttr("marjorList",bm.getAllMarjor());
		renderFreeMarker("/addStudent.html");
	}
	/**
	 * 判断 学号是否存在
	 *
	 *<p>Title:judgeStudentSno</p>
	 *@author weichao
	 *@date Mar 23, 2016
	 */
	public void judgeStudentSno(){
		HashMap<String, Integer> map = new HashMap<String, Integer>();
		String sno = getPara("sno");
		Student student = bs.getStudentWithSno(sno);
		if(student==null){
			map.put("statu", 0);
		}else{
			map.put("statu", 1);
		}
		renderJson(map);
	}
	/**
	 * 添加学生
	 *
	 *<p>Title:addStu</p>
	 *@author weichao
	 *@date Mar 21, 2016
	 */
	@Before(PermissionIntercepter.class)
	public void addStu(){
		Student student = getModel(Student.class,"Stu");
		student.save();
		redirect("/stu/index");
	}

	/**
	 * 删除学生
	 *
	 *<p>Title:delete</p>
	 *@author weichao
	 *@date Mar 10, 2016
	 */
	@Before(PermissionIntercepter.class)
	public void delete(){
		int id = getParaToInt("id");
		bs.delete(id);
		redirect("/stu/index"); 
	}
	
	
	/**
	 * 跳转到修改页面
	 *
	 *<p>Title:toEdit</p>
	 *@author weichao
	 *@date Mar 10, 2016
	 */
	public void toEdit(){
		int stuId = getParaToInt("id");
		setAttr("student", bs.getStudentWithId(stuId));
		List<Major> list = bs.getAllMajor(); 
		setAttr("majorList", list);
		renderFreeMarker("/gerenxinxi.html");
	}
	/**
	 *  修改方法
	 *  
	 */
	@Before(PermissionIntercepter.class)
	public void updatestu(){
		Boolean mark = getModel(Student.class,"Stu").update();
	   
	   if(mark)
	   {
		   redirect("/stu/index");
	   }else
	   {
		   
	   }
		
	}	
	
	/*
	 * 跳转到添加专业班级页面
	 */
	public void toAddClass()
	{
		BClasses bclass= new BClasses();
		 List<Map<String, Object>> list = new ArrayList<Map<String, Object>>(); 
		List<Major> majorList=bs.getAllMajor();
		for(Major major : majorList)
		{
			HashMap<String, Object> map = new HashMap<String, Object>(); 
			int mid= major.get("Id");
			List<Classes> clist = bclass.getClassesWithM_id(mid);
			map.put("classlist", clist);
			map.put("majorname", major.getStr("mname"));
			map.put("majorid",mid);
			list.add(map);
		}
		setAttr("allList", list);
		//renderJson();
		renderFreeMarker("/add_Major_Class.html");
	}
	/**
	 * 根据m_id 和 班级 名称 判断 是否存在
	 * 0 不存在 
	 * 1  存在
	 *
	 *<p>Title:judgeByClassNameAndM_id</p>
	 *@author weichao
	 *@date Mar 22, 2016
	 */
	public void judgeByClassNameAndM_id(){
		int m_id = getParaToInt("mid");
		String name = getPara("name").trim();
		HashMap<String, Integer> map = new HashMap<String, Integer>();
		List<Classes> list = bc.getClassByNameAndM_id(m_id, name);		
		if(list.isEmpty()||list.size()==0){
			 map.put("statu", 0);
		 }else{
			 map.put("statu", 1);
		 }
		renderJson(map);
	}
	/**
	 * 根据 专业名称和简称 查询专业是否存在
	 * 0 不存在 
	 * 1  存在
	 * 
	 *<p>Title:judgeByMajorName</p>
	 *@author weichao
	 *@date Mar 22, 2016
	 */
	public void judgeByMajorName(){
		 List<Major> list = bm.getMajorWithName(getPara("mname").trim(),getPara("simpleName").trim());
		 HashMap<String, Integer> map = new HashMap<String, Integer>();
		 if(list.isEmpty()||list.size()==0){
			 map.put("statu", 0);
		 }else{
			 map.put("statu", 1);
		 }
		renderJson(map);
	}
	/*
	 * 根据专业id添加班级
	 */
	@Before(PermissionIntercepter.class)
	public void addClassByMajorId()
	{
		String cname=getPara("nianji")+getPara("banji");
		int m_id=getParaToInt("mid");
	     new Classes().set("cname", cname).set("m_id", m_id).save();
		redirect("/stu/toAddClass");
	}
	
	/*
	 * 跳转到添加专业页面
	 */
	public void toAddMjorPage()
	{
		renderFreeMarker("/addMajor.html");
		
	}
	
	/*
	 * 跳转到添加班级页面
	 */
/*	public void toAddClassPage()
	{
		//处理一下年级和班级
		BClasses bclass=new BClasses();
		List<Classes> classesList= bclass.findAllClass();
		Classes classes= classesList.get(0);
		int cid= Integer.parseInt(classes.getStr("cname"))/100;
		List<Map<String,String>> nlist= new ArrayList<Map<String,String>>();
		for(int i=0;i<10;i++)
		{
			Map<String, String> map= new HashMap<String, String>();
			map.put("nianji", ""+cid);
			cid++;
			nlist.add(map);			
		}
	     setAttr("mid", getPara("id"));	
		setAttr("nlist", nlist);
		renderFreeMarker("/addClass.html");
	}*/
	public void toAddClassPage(){
		BClasses bclass=new BClasses();
//		List<Classes> classesList = bclass.findAllClass();
		Date now = new Date(); 
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy");//可以方便地修改日期格式
		String time = dateFormat.format(now);
//		Classes classes= classesList.get(0);
//		int cid= Integer.parseInt(classes.getStr("cname"))/100;
		setAttr("mid", getPara("id"));
		setAttr("nj", Integer.parseInt(time));
		renderFreeMarker("/addClass.html");
	}
	
	/*
	 * 添加专业
	 */
	@Before(PermissionIntercepter.class)
	public void addMajor()
	{
		Major major=getModel(Major.class);
		Boolean mark = major.save();
		if(mark)
		{
			redirect("/stu/toAddClass");
		}
	}
	
	/*
	 * 根据班级id删除班级
	 */
	@Before(DeleteClassIntercepter.class)
	public void deleteClass()
	{
	  int classid=getParaToInt("id");
	  Boolean mark=	Classes.dao.deleteById(classid);
	  if(mark)
	  {
		  setAttr("message", "删除成功！");
		  setAttr("status", 0);
		  renderJson();
		 // redirect("/stu/toAddClass");
	  }
	}
	/**
	 * 跳转到校历时间设置界面
	 *
	 *<p>Title:toSetData</p>
	 *@author weichao
	 *@date Apr 8, 2016
	 */
	public void toSetData(){ 
		Date date = new Date();
		int year = date.getYear()+1900;
		setAttr("year", year);
		renderFreeMarker("/setDate.html");
	}
	/**
	 * 提交开学日期
	 *
	 *<p>Title:setDate</p>
	 *@author weichao
	 *@date Apr 8, 2016
	 */
	@Before(DateBeginIntercept.class)
	public void setDate(){
		 String message = "";
		 String startDate = getPara("inputDate");
		 String term = getPara("term");
		 if(bs.setBeginDate(startDate,term)){
			 message = "设置成功！";
		 }else{
			 message = "设置失败！";
		 }
		 renderJson("message", message);
	 }
	/**
	 * 周的下拉框
	 *
	 *<p>Title:getZhou</p>
	 *@author weichao
	 *@date Apr 10, 2016
	 *@return void
	 */
	public void getZhou(){
		int id = getParaToInt("id");
		if(id ==0){
			renderJson("");
		}else{
			Date date = bs.getDateWithId(id).getDate("date");
			List<String> zhouList = zhou(date);
			renderJson(zhouList);
		}
	}
	/**
	 * 获取一学期的周时间
	 *
	 *<p>Title:zhou</p>
	 *@author weichao
	 *@date Apr 10, 2016
	 *@return List<Record>
	 */
	public List<String> zhou(Date date){
		List<String> dataList =  new ArrayList<String>();
		SimpleDateFormat dateFm = new SimpleDateFormat("yyyy-MM-dd");
		Calendar   calendar   =   new   GregorianCalendar();
		for(int i = 0;i<21;i++){
			 calendar.setTime(date); 
		     calendar.add(calendar.DATE,7);//把日期往后增加7天.整数往后推,负数往前移动 
		     Date dates = calendar.getTime();
		     String s = dateFm.format(dates)+"|"+dateFm.format(date);
		     dataList.add(s);		     
		     date = calendar.getTime();
		}
		return  dataList;
	}
	
	/*
	 * 跳转到学生详细信息页面,默认显示最近一学期的数据
	 */
	public void getStuInfo()
	{
		int s_id=getParaToInt("stuId");
		Student student=bs.getStudentWithId(s_id);
		//这里返回学生的基本信息，不管有没有学期信息，这部分信息都会存在
		setAttr("student", student);
		//这里返回学生的学期信息，第一次进入页面只会返回最近的一条数据
		List<Basicinfo> bsinfolist = bs.getBasicInfoWithId(s_id);
		if(bsinfolist.isEmpty()||bsinfolist==null)
		{
			//判断一下该学生有没有学期资料
			setAttr("status", 0);
			setAttr("message", "改生还没有学期资料");
		}else
		{
			//有学期资料就返回学期列表
			setAttr("status", 1);
			setAttr("message", " ");			
			Basicinfo bsinfo= bsinfolist.get(0);
			setAttr("info", bsinfo);
			List<String> termList= new ArrayList<String>();
			for(Basicinfo bas : bsinfolist)
			{
				termList.add(bas.getStr("termdate"));
			}
			//学期列表
			setAttr("termList", termList);
			//转换01为是否有无
			panduan(bsinfo);
		}
		
		//renderJson();
		renderFreeMarker("/stuBackgroundInfo.html");//学生信息详情页面路径
	}
	
	/*
	 * 点击学期，局部刷新详情
	 */
	public void stuTermInfo()
	{
		Basicinfo bsinfo = bs.getBasicInfoWithIdAndtermDate(getParaToInt("s_id"), getPara("termdate"));
		//panduan(bsinfo);
		Map<String,Object> map=new HashMap<String, Object>();
		map.put("data", bsinfo);
		renderJson(map);
	}	
	
	//判断几个是否
	public void panduan(Basicinfo  bsinfo)
	{
		//是否有助学贷款
				if(bsinfo.getInt("ifload")==1) 
				{
					setAttr("ifload", "是");
				}else
				{
					setAttr("ifload", "否");
				}
				
				//助学岗位
				if(bsinfo.getInt("ifzhuxue")==1) 
				{
					setAttr("ifzhuxue", "有");
				}else
				{
					setAttr("ifzhuxue", "无");
				}
				
				//是否为优干
				if(bsinfo.getInt("ifyougan")==1) 
				{
					setAttr("ifyougan", "是");
				}else
				{
					setAttr("ifyougan", "否");
				}
				//是否为优团
				if(bsinfo.getInt("ifyoutuan")==1) 
				{
					setAttr("ifyoutuan", "是");
				}else
				{
					setAttr("ifyoutuan", "否");
				}
				//是否三好
				if(bsinfo.getInt("ifsanhao")==1) 
				{
					setAttr("ifsanhao", "是");
				}else
				{
					setAttr("ifsanhao", "否");
				}
				//是否有专升本意向
				if(bsinfo.getInt("ifupgraded")==1) 
				{
					setAttr("ifupgraded", "有");
				}else
				{
					setAttr("ifupgraded", "无");
				}

				//是否有欧美留学意向
				if(bsinfo.getInt("ifoverseas")==1) 
				{
					setAttr("ifoverseas", "有");
				}else
				{
					setAttr("ifoverseas", "无");
				}
				//是否有韩国留学意向
				if(bsinfo.getInt("ifkorea")==1) 
				{
					setAttr("ifkorea", "有");
				}else
				{
					setAttr("ifkorea", "无");
				}
	}
}

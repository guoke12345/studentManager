package com.stuManager.controllers;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

import com.alibaba.fastjson.parser.deserializer.ArrayListTypeFieldDeserializer;
import com.business.BClasses;
import com.business.BMajor;
import com.business.BPro_info;
import com.business.BProject;
import com.business.BStudent;
import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Record;
import com.mysql.fabric.xmlrpc.base.Param;
import com.stuManager.Models.Classes;
import com.stuManager.Models.Pro_info;
import com.stuManager.Models.Project;
import com.stuManager.Models.Student;
import com.stuManager.interceptor.PermissionIntercepter;

public class Pro_infoController extends Controller {
	BPro_info info = new BPro_info();
	BProject bp = new BProject();
	BMajor bm = new BMajor();
	BClasses bc = new BClasses();
	BStudent bs = new BStudent();

	/**
	 * 跳转到考勤信息界面
	 *
	 * <p>
	 * Title:index
	 * </p>
	 * 
	 * @author weichao
	 * @date Mar 12, 2016
	 */
	public void index() {
		setAttr("projectList", bp.getAllProject());
		setAttr("marjorList", bm.getAllMarjor());
		List<Record> dateList = bs.getdateLsit();
		for(int i = 0;i<dateList.size();i++){
			Date date = dateList.get(i).get("date");
			Calendar   calendar   =   new   GregorianCalendar();
			calendar.setTime(date); 
			calendar.add(calendar.DATE,7*20);//把日期往后增加20周.整数往后推,负数往前移动 
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			String endDate= dateFormat.format(calendar.getTime());
			String termDate = date.toString() +"|"+endDate;
			dateList.get(i).set("termDate", termDate);
		}
		setAttr("dateList", dateList);
		renderFreeMarker("/discript.html");
		// renderJson();
	}

	/**
	 * 删除项目信息
	 *
	 * <p>
	 * Title:delete
	 * </p>
	 * 
	 * @author weichao
	 * @date Mar 21, 2016
	 */
	@Before(PermissionIntercepter.class)
	public void delete() {
		int id = getParaToInt("id");
		if (info.deleteInfoById(id)) {
			setAttr("msg", "删除成功！");
		} else {
			setAttr("msg", "删除失败！");
		}
		redirect("/info/index");
	}

	/**
	 * 跳转到更新界面
	 *
	 * <p>
	 * Title:toUpdate
	 * </p>
	 * 
	 * @author weichao
	 * @date Mar 21, 2016
	 */
	public void toUpdate() {
		int id = getParaToInt("id");
		List<Pro_info> list = info.findInfoById(id);
		setAttr("userInfo", list.get(0));
		renderFreeMarker("/updateInfo.html");
	}

	/**
	 * 更新项目信息
	 *
	 * <p>
	 * Title:updateInfo
	 * </p>
	 * 
	 * @author weichao
	 * @date Mar 21, 2016
	 */
	@Before(PermissionIntercepter.class)
	public void updateInfo() {
		Pro_info info = getModel(Pro_info.class, "info");
		info.update();
		redirect("/info/index");
	}

	/**
	 * 跳转到添加信息页面
	 *
	 * <p>
	 * Title:addInfo
	 * </p>
	 * 
	 * @author weichao
	 * @date Mar 21, 2016
	 */
	public void toAddInfo() {
		Student student = bs.getStudentWithId(getParaToInt("id"));
		setAttr("student", student);
		setAttr("proList", bp.findPro());
		renderFreeMarker("/addInfo.html");
	}

	/**
	 * 添加项目信息
	 *
	 * <p>
	 * Title:addInfo
	 * </p>
	 * 
	 * @author weichao
	 * @date Mar 21, 2016
	 */
	@Before(PermissionIntercepter.class)
	public void addInfo() {
		Date now = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm");// 可以方便地修改日期格式
		String time = dateFormat.format(now);
		System.out.println(time);
		Pro_info info = getModel(Pro_info.class, "info");
		info.set("date", time);
		info.save();
		redirect("/info");
	}

	/**
	 * 根据m_id查找classes
	 *
	 * <p>
	 * Title:getClassesWithM_id
	 * </p>
	 * 
	 * @author weichao
	 * @date Mar 13, 2016
	 */
	public void getClassesWithM_id() {
		int m_id = getParaToInt("m_id");
		renderJson(bc.getClassesWithM_id(m_id));
	}

	/**
	 * 根据条件查询info信息 ajax 传值
	 * <p>
	 * Title:getInfo
	 * </p>
	 * 
	 * @author weichao
	 * @date Mar 13, 2016
	 */
	public void getInfo() {
		HashMap<String, List> map = new HashMap<String, List>();
		Date beginTime = getParaToDate("beginTime");
		Date endTime = getParaToDate("endTime");
		int p_id = getParaToInt("p_id");
		List<Pro_info> infoList;
		if (getPara("m_id").equals("") || getPara("m_id").equals(null)) {
			infoList = info.getInfoWithCondition(p_id,beginTime,endTime);
		} else if (getPara("c_id").equals("") || getPara("c_id").equals(null)) {
			int m_id = getParaToInt("m_id");
			infoList = info.getInfoWithCondition(p_id, m_id,beginTime,endTime);
		} else {
			int m_id = getParaToInt("m_id");
			int c_id = getParaToInt("c_id");
			infoList = info.getInfoWithCondition(p_id, c_id, m_id,beginTime,endTime);
		}
		for(Pro_info pro : infoList){
			if(pro.get("sum_add")==null){
				pro.put("sum_add",0);
			}
			if(pro.get("sum_sub")==null){
				pro.put("sum_sub",0);
			}
		}
		map.put("data", infoList);
		renderJson(map);
	}

	/**
	 * 信息统计页面
	 *
	 * <p>
	 * Title:count
	 * </p>
	 * 
	 * @author weichao
	 * @date Mar 28, 2016
	 */
	public void count() {
		setAttr("projectList", bp.getAllProject());
		setAttr("marjorList", bm.getAllMarjor());
		setAttr("pList", bp.findAllPro());
		List<Record> dateList = bs.getdateLsit();
		for(int i = 0;i<dateList.size();i++){
			Date date = dateList.get(i).get("date");
			Calendar calendar = new   GregorianCalendar();
			calendar.setTime(date); 
			calendar.add(calendar.DATE,7*20);//把日期往后增加20周.整数往后推,负数往前移动 
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			String endDate= dateFormat.format(calendar.getTime());
			String termDate = date.toString() +"|"+endDate;
			dateList.get(i).set("termDate", termDate);
		}
		setAttr("dateList", dateList);
		renderFreeMarker("/infoCount.html");
	}
	/**
	 * 
	 * 信息统计
	 * <p>
	 * Title:countInfo
	 * </p>
	 * 
	 * @author weichao
	 * @date Mar 29, 2016
	 */
	public void countInfo(){
		Date beginTime = getParaToDate("beginTime");
		Date endTime = getParaToDate("endTime");
		List<Pro_info> infoList;
		if(getPara("m_id").equals("")){
			infoList = info.countStuInfo(beginTime,endTime);
		}else if(getPara("c_id").equals("")){
			 infoList = info.countStuInfo(getParaToInt("m_id"),beginTime,endTime);
		}else{
			 infoList = info.countStuInfo(getParaToInt("m_id"),getParaToInt("c_id"),beginTime,endTime);
		}
		for(Pro_info pro : infoList){
			if(pro.get("sum_add")==null){
				pro.put("sum_add",0);
			}
			if(pro.get("sum_sub")==null){
				pro.put("sum_sub",0);
			}
		}
		HashMap<String, Object> result = new HashMap<String, Object>();
		result.put("data", infoList);
		renderJson(result);
	}
	
	/*@Before(PermissionIntercepter.class)
	public void countInfo() {
		Pro_info pro_info;
		Date beginTime = getParaToDate("beginTime");
		Date endTime = getParaToDate("endTime");
		int stu_id = 0;
		List<Project> pList = bp.findAllPro();
		List<Pro_info> infoList;
		if(getPara("m_id").equals("")){
			infoList = info.countInfo(beginTime,endTime);
		}else if(getPara("c_id").equals("")){
			 infoList = info.countInfo(getParaToInt("m_id"),beginTime,endTime);
		}else{
			 infoList = info.countInfo(getParaToInt("m_id"),getParaToInt("c_id"),beginTime,endTime);
		}
		double count_grade = 0;
		HashMap<String, Object> map = new HashMap<String, Object>();
		List<HashMap<String, Object>> mapList = new ArrayList<HashMap<String, Object>>();
		// 给mapList赋值
		for (int i = 0; i < infoList.size(); i++) {
			pro_info = infoList.get(i);
			//判断是否是同一个人    
			if (pro_info.getInt("stu_id") == stu_id) {
				//同一个人
				for(int j = 0; j < pList.size(); j++) {
					if(pro_info.getInt("p_id")==pList.get(j).getInt("p_id")){
						map.put(""+pList.get(j).get("p_id") , ""+pro_info.get("sum_grade"));
						count_grade += (Double) pro_info.get("sum_grade");
					}
				}
			}else{
				//不同的人
				if(i!=0 && i!= infoList.size()){
					map.put("count_grade", count_grade);
					mapList.add(map);
					map = new HashMap<String, Object>();
					count_grade = 0;
				}
				map.put("stuname", pro_info.get("stuname"));
				map.put("cname", pro_info.get("cname"));
				for(int j = 0; j < pList.size(); j++) {
					if(pro_info.getInt("p_id")==pList.get(j).getInt("p_id")){
						map.put(""+pList.get(j).get("p_id") , ""+pro_info.get("sum_grade"));
						count_grade += (Double) pro_info.get("sum_grade");
					}else{
						map.put(""+pList.get(j).get("p_id"),"");
					}
				}
			}
			stu_id = pro_info.getInt("stu_id");
			if(i==infoList.size()-1){
				map.put("count_grade", count_grade);
				mapList.add(map);
			}
		}
		HashMap<String, Object> result = new HashMap<String, Object>();
		result.put("data", mapList);
		renderJson(result);
	}*/
	/**
	 *  学生个人信息统计
	 *<p>Title:countStudentInfo</p>
	 *@author weichao
	 *@date Apr 27, 2016
	 *@return void
	 */
	public void countStudentInfo(){
		int stu_id = getParaToInt("id");
		Date beginTime = getParaToDate("beginTime");
		Date endTime = getParaToDate("endTime");
		List<Pro_info> infoList = info.getCountInfoWithS_id(stu_id, beginTime, endTime);
		for(Pro_info pro : infoList){
			if(pro.get("sum_add")==null){
				pro.put("sum_add",0);
			}
			if(pro.get("sum_sub")==null){
				pro.put("sum_sub",0);
			}
		}
		setAttr("infoList", infoList);
		setAttr("beginTime", getPara("beginTime"));
		setAttr("endTime", getPara("endTime"));
		setAttr("stu_id", stu_id);
		setAttr("stuname", infoList.get(0).get("stuname"));
		renderFreeMarker("/infoStudentCount.html");
	}
	/**
	 * 学生单个项目个人信息统计
	 *
	 *<p>Title:countStudentProjrctInfo</p>
	 *@author weichao
	 *@date Apr 27, 2016
	 *@return void
	 */
	public void countStudentProjrctInfo(){
		int stu_id = getParaToInt("stu_id");
		int p_id = getParaToInt("p_id");
		Date beginTime = getParaToDate("beginTime");
		Date endTime = getParaToDate("endTime");
		List<Pro_info> infoList = info.getCountInfoWithS_idAndP_id(p_id, stu_id, beginTime, endTime);
		String infoName = infoList.get(0).get("pname");
		String stuname = infoList.get(0).get("stuname");
		setAttr("stuname", stuname);
		setAttr("infoList", infoList);
		setAttr("infoName", infoName);
		renderFreeMarker("/infoStudentProjectCount.html");
	}

}

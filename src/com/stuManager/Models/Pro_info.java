package com.stuManager.Models;

import com.jfinal.plugin.activerecord.Model;

public class Pro_info extends Model<Pro_info>{
	/**
	 * 创建模型
	 */
	private static final long serialVersionUID = 1L;

	public static final Pro_info dao= new Pro_info();
	
	public Student getStudent(){
		return Student.dao.findById(get("s_id"));
	}
	
	public String getProjectName()
	{
		return  Project.dao.findById(get("p_id")).getStr("pname");
		
	}
}


package com.business;

import java.util.List;

import com.stuManager.Models.Major;

public class BMajor {
	/**
	 * 查询 所有专业
	 *
	 *<p>Title:getAllMarjor</p>
	 *@author weichao
	 *@date Mar 22, 2016
	 */
	public List<Major> getAllMarjor(){
		String content = "id'm_id',mname,simpleName";
		String sql = "select "+content+" from major";
		return Major.dao.find(sql);
	}
	/**
	 * 根据专业名称 和简称查询 专业
	 *
	 *<p>Title:getMajorWithName</p>
	 *@author weichao
	 *@date Mar 22, 2016
	 */
	public List<Major> getMajorWithName(String mname,String simpleName){
		String content = "id'm_id',mname,simpleName";
		String sql = "select "+content+" from major where mname = ? or simpleName = ?";
		return Major.dao.find(sql, mname,simpleName);
	}
}

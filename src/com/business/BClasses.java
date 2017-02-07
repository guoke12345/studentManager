package com.business;

import java.util.List;

import com.stuManager.Models.Classes;

public class BClasses {
	/**
	 * 根据m_id查找班级列表
	 *
	 *<p>Title:getClassesWithId</p>
	 *@author weichao
	 *@date Mar 13, 2016
	 */
	public List<Classes> getClassesWithM_id(int m_id){
		String content = "id'cid',cname,m_id";
		String sql = "select "+ content +" from classes where m_id = "+m_id;
		return Classes.dao.find(sql);
	}
	
	public List<Classes> findAllClass()
	{
		
		return Classes.dao.find("select * from classes ORDER BY cname desc");
	}
	
	public Boolean deleteClass(int CID)
	{
		return Classes.dao.deleteById(CID);
	}
	/**
	 * 根据专业id 和班级名称 查询
	 *
	 *<p>Title:getClassByNameAndM_id</p>
	 *@author weichao
	 *@date Mar 22, 2016
	 */
	public List<Classes> getClassByNameAndM_id(int m_id,String name){
		String count = " c.id'c_id',cname,m_id ";
		String sql = "select "+count+" from classes as c where c.cname =?  and m_id = ?";
		return Classes.dao.find(sql, name,m_id);
	}
	
}

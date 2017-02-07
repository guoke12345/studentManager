package com.business;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.stuManager.Models.Project;

public class BProject {

	/**
	 * project 表的自关联，将父项目与子项目放到同一记录里
	 *
	 *<p>Title:getAllProject</p>
	 *@author weichao
	 *@date Mar 12, 2016
	 */
	public List<Project>  getAllProject(){
		String sql = "select * from (select p.id'pl_id',p.pname'pl_name' from project as p where parent_id = 0) as pl left join (select p.id'pr_id',p.pname'pr_name',p.parent_id'pr_parent_id' from project as p where parent_id != 0) as pr on pr.pr_parent_id = pl.pl_id";
		return Project.dao.find(sql);
	}

	/**
	 * 查找project 所有一级项目
	 *
	 *<p>Title:getAllParentProject</p>
	 *@author weichao
	 *@date Mar 12, 2016
	 */
	public List<Project> getAllParentProject(){
		String sql = "select * from project where parent_id = 0";
		return Project.dao.find(sql);
	}
	/**
	 * 根据parent_id 查找项目
	 *
	 *<p>Title:getProjectWithParentId</p>
	 *@author weichao
	 *@date Mar 12, 2016
	 */
	public List<Project> getProjectWithParentId(int parent_id){
		String sql = "select * from project where parent_id = "+parent_id;
		return Project.dao.find(sql);
	}

	/*
	 * 根据项目id查找其子项目
	 * 
	 */
	public List<Project> getProListById(int ProId)
	{
		return Project.dao.find("select * from project where parent_id='"
                 +ProId+"'");
	}
	
	/*
	 * 根据id删除项目
	 */
	public Boolean deletProById(int proId)
	{
		return Project.dao.deleteById(proId);
		
	}
	
	/*
	 * 根据父项目id添加项目(一级项目的默认父项目id为0)
	 */
	public Boolean addProByFatherId(int fatherId,String proname,int ifmark)
	{
		
		return new Project().set("pname", proname).set("parent_id", fatherId).set("ifmark", ifmark).save();
	}
	
	/*
	 * 查找第一级的项目列表(一级项目的默认父项目id为0)
	 */
	public List<Project> findMainPro()
	{
		
		return Project.dao.find("select * from project where parent_id=0");
	}

	/*
	 * 返回项目的树形结构数据
	 */
	public List<Map<String,Object>> findPro()
	{
		List<Map<String,Object>> Alist= new ArrayList<Map<String,Object>>();
		List<Project> mainList=findMainPro();
		for(Project Mpro :mainList)
		{
			Map<String,Object> map= new HashMap<String, Object>();
			map.put("ParentId", Mpro.getInt("Id"));
			map.put("ParentName", Mpro.getStr("pname"));
			List<Project> chirldProList=getProListById(Mpro.getInt("Id"));
			map.put("chirldList", chirldProList);
			Alist.add(map);
		}
		
		return Alist;
	}
	/**
	 * 根据 parent_id 和 名字查找
	 *
	 *<p>Title:findWithNameAndParent_id</p>
	 *@author weichao
	 *@date Mar 23, 2016
	 */
	public List<Project> findWithNameAndParent_id(int parent_id,String pname){
		String count =" p.id'pid',pname,parent_id ";
		String sql = "select "+count+" from project as p where parent_id = ? and pname = ?";
		return Project.dao.find(sql, parent_id,pname);
	}

	/*
	 * 根据id查找项目
	 */
	public Project findProById(int ProId)
	{
		return Project.dao.findById(ProId);
	}
	/**
	 * 查找添加信息的所有的项目
	 *
	 *<p>Title:findAllPro</p>
	 *@author weichao
	 * @return 
	 *@date Mar 31, 2016
	 */
	public List<Project> findAllPro(){
		String content = " id'p_id',pname ";
		String sql = "select "+content+" from project where id in (select DISTINCT p_id from pro_info)";
		return Project.dao.find(sql);
	}
}

package com.business;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.stuManager.Models.Pro_info;
import com.stuManager.Models.Student;

public class BPro_info {

	/**
	 * 根据 p_id c_id m_id  查找 项目信息
	 *
	 *<p>Title:getInfoWithCondition</p>
	 *@author weichao
	 *@date Mar 14, 2016
	 */
	public List<Pro_info> getInfoWithCondition(int p_id,int c_id,int m_id,Date beginTime,Date endTime){
		String sql = " select  info.id'info_id',s_id,p_id,stuname,concat(simpleName,cname)'classes',simpleName,cname,description,SUM(grade)'count_grade',SUM(add_grade)'sum_add',SUM(sub_grade)'sum_sub',date  "
				+ "from pro_info as info "
				+ "left join student as s on info.s_id = s.id "
				+ "left join classes as c on s.c_id = c.id "
				+ "left join major as m on c.m_id = m.id "
				+ "left join (select id,grade'add_grade' from pro_info where grade > 0) as info_add on info_add.id = info.id "
				+ "left join (select id,grade'sub_grade' from pro_info where grade < 0) as info_sub on info_sub.id = info.id "
				+ " where m.id = ? and c.id = ? and info.p_id = ? and Date(date) between ? and ? "
				+ "GROUP BY s_id";
		return  Pro_info.dao.find(sql, m_id,c_id,p_id,beginTime,endTime);
	}
	/**
	 * 根据 p_id c_id m_id  查找 项目信息
	 * 重载  getInfoWithCondition(int p_id,int c_id,int m_id)
	 *
	 *<p>Title:getInfoWithCondition</p>
	 *@author weichao
	 *@date Mar 14, 2016
	 */
	public List<Pro_info> getInfoWithCondition(int p_id,int m_id,Date beginTime,Date endTime){
		String sql = " select  info.id'info_id',s_id,p_id,stuname,concat(simpleName,cname)'classes',simpleName,cname,description,SUM(grade)'count_grade',SUM(add_grade)'sum_add',SUM(sub_grade)'sum_sub',date  "
				+ "from pro_info as info "
				+ "left join student as s on info.s_id = s.id "
				+ "left join classes as c on s.c_id = c.id "
				+ "left join major as m on c.m_id = m.id "
				+ "left join (select id,grade'add_grade' from pro_info where grade > 0) as info_add on info_add.id = info.id "
				+ "left join (select id,grade'sub_grade' from pro_info where grade < 0) as info_sub on info_sub.id = info.id "
				+ " where m.id = ? and info.p_id = ? and Date(date) between ? and ? "
				+ "GROUP BY s_id";
		return  Pro_info.dao.find(sql,m_id,p_id,beginTime,endTime);
	}
	/**
	 * 根据 p_id  (project表 id) 查找 项目信息
	 *
	 *<p>Title:getInfoWithCondition</p>
	 *@author weichao
	 *@date Mar 14, 2016
	 */
	public List<Pro_info> getInfoWithCondition(int p_id,Date beginTime,Date endTime){
		String sql = " select  info.id'info_id',s_id,p_id,stuname,concat(simpleName,cname)'classes',simpleName,cname,description,SUM(grade)'count_grade',SUM(add_grade)'sum_add',SUM(sub_grade)'sum_sub',date  "
				+ "from pro_info as info "
				+ "left join student as s on info.s_id = s.id "
				+ "left join classes as c on s.c_id = c.id "
				+ "left join major as m on c.m_id = m.id "
				+ "left join (select id,grade'add_grade' from pro_info where grade > 0) as info_add on info_add.id = info.id "
				+ "left join (select id,grade'sub_grade' from pro_info where grade < 0) as info_sub on info_sub.id = info.id "
				+ " where info.p_id = ? and Date(date) between ? and ? "
				+ "GROUP BY s_id";
		return  Pro_info.dao.find(sql,p_id,beginTime,endTime);
	}
	public List<Pro_info> getInfoWithCondition(int p_id){
		String where = "where info.p_id = ? ";
		String content = "info.id'info_id',s_id,p_id,stuname,concat(simpleName,cname)'classes',simpleName,cname,description,grade,date";
		String sql = " select "+content+" from pro_info as info left join student as s on info.s_id = s.id left join classes as c on s.c_id = c.id left join major as m on c.m_id = m.id "+ where;
		return  Pro_info.dao.find(sql,p_id);
	}
	


	/*
	 * 根据学生id查找这个学生的所有项目，按照时间倒序排列
	 * 
	 */
	public Map<String,String> getStuProInfoByStuId(int stuId)
	{
		Map<String,String> parMap=  new HashMap<String, String>();
		//放入学生信息
		Student stu = Pro_info.dao.getStudent();
		parMap.put("StuName", stu.getStr("stuname"));
		parMap.put("StuNumber", stu.getStr("sno"));
		//放入项目名
		parMap.put("ProName", Pro_info.dao.getProjectName());
		//放入项目信息
		Pro_info proInfo=Pro_info.dao.findById(stuId);
		parMap.put("ProDescrib",proInfo.getStr("description"));//项目描述
		parMap.put("ProGrade",proInfo.getStr("grade"));//项目得分
		parMap.put("ProTime",proInfo.getStr("date"));//项目得分
		return parMap;
	}
	/**
	 *  查找某位学生的项目信息
	 *<p>Title:StudentHasInfo</p>
	 *@author weichao
	 *@date Mar 21, 2016
	 */
	public List<Pro_info> getProInfoByStuId(int s_id){
		String sql = "select * from pro_info where s_id ="+s_id;
		return Pro_info.dao.find(sql);
	}
	/**
	 * 通过id删除项目信息
	 *
	 *<p>Title:deleteInfoById</p>
	 *@author weichao
	 *@date Mar 21, 2016
	 */
	public boolean deleteInfoById(int id){
		return Pro_info.dao.deleteById(id);
	}
	/**
	 * select student、info  by  info_id
	 *
	 *<p>Title:findInfoById</p>
	 *@author weichao
	 *@date Mar 22, 2016
	 */
	public List<Pro_info> findInfoById(int id){
		String where = " where info.id = "+id;
		String content = " info.id'info_id',grade,description,p.pname,date,stu.stuname ";
		String sql = "select "+content+"from pro_info as info left join student as stu on info.s_id = stu.id left join project as p on p.id = info.p_id "+where;
		return Pro_info.dao.find(sql);
	}
	
	public int  deletAllProInfoInPid(int PID)
	{
		String sql="DELETE FROM pro_info WHERE p_id = "+PID;
		 return Db.update(sql);
	}
	/**
	 * 统计项目信息
	 *
	 *<p>Title:countInfo</p>
	 *@author weichao
	 *@date Mar 29, 2016
	 */
	public List<Pro_info> countInfo(Date beginTime,Date endTime ){
		String where = " where Date(date) between ? and ? ";
		String content = " s.Id'stu_id',p_id,stuname,mname,concat(simpleName,cname)'cname',pname,sum_grade,count_pid ";
		String sql = "select "+content+" from student as  s left join  (select  *,sum(grade)'sum_grade',count(p_id)'count_pid'  from pro_info group by p_id,s_id) as info on info.s_id = s.id  left join project as p on p.id = info.p_id  left join classes as c on s.c_id = c.id  left join major as m on c.m_id = m.id "+where+" order by stu_id asc" ;
		return Pro_info.dao.find(sql,beginTime,endTime);
	}
	public List<Pro_info> countInfo(int m_id,Date beginTime,Date endTime){
		String where = " where m.id = "+m_id +" and Date(date) between ? and ?";
		String content = " s.Id'stu_id',p_id,stuname,mname,concat(simpleName,cname)'cname',pname,sum_grade,count_pid ";
		String sql = "select "+content+" from student as  s left join  (select  *,sum(grade)'sum_grade',count(p_id)'count_pid'  from pro_info group by p_id,s_id) as info on info.s_id = s.id  left join project as p on p.id = info.p_id  left join classes as c on s.c_id = c.id  left join major as m on c.m_id = m.id "+where+" order by stu_id asc ";
		return Pro_info.dao.find(sql,beginTime,endTime);
	}
	public List<Pro_info> countInfo(int m_id ,int c_id,Date beginTime,Date endTime){
		String where = " where m.id = "+m_id+" && s.c_id = "+c_id +" and Date(date) between ? and ?";
		String content = " s.Id'stu_id',p_id,stuname,mname,concat(simpleName,cname)'cname',pname,sum_grade,count_pid ";
		String sql = "select "+content+" from student as  s left join  (select  *,sum(grade)'sum_grade',count(p_id)'count_pid'  from pro_info group by p_id,s_id) as info on info.s_id = s.id  left join project as p on p.id = info.p_id  left join classes as c on s.c_id = c.id  left join major as m on c.m_id = m.id "+where+" order by stu_id asc ";
		return Pro_info.dao.find(sql,beginTime,endTime);
	}
	
	/**
	 * 学生信息概览
	 *
	 *<p>Title:countStuInfo</p>
	 *@author weichao
	 *@date Apr 25, 2016
	 *@return List<Pro_info>
	 */
	public List<Pro_info> countStuInfo(Date beginTime,Date endTime){
		String sql = "select  s.id'stu_id',stuname,concat(simpleName,cname)'cname', "
				+ "sum(add_grade)'sum_add',"
				+ "sum(sub_grade)'sum_sub',"
				+ "sum(info.grade)'count_grade' "+
				"from student as  s "+
				"left join pro_info  as info on info.s_id = s.id "+ 
				"left join project as p on p.id = info.p_id "+
				"left join classes as c on s.c_id = c.id "+
				"left join major as m on c.m_id = m.id  "+
				"left join (select  id,grade'add_grade' from pro_info where grade > 0) as info_add on info_add.id = info.id "+
				"left join (select id,grade'sub_grade' from pro_info where grade < 0) as info_sub on info_sub.id = info.id "+
				"where p.ifmark = 1 and Date(date) between ? and ? "+
				"group by s_id "+
				"order by stu_id asc ";
		return Pro_info.dao.find(sql,beginTime,endTime);
	}
	 
	public List<Pro_info> countStuInfo(int m_id,Date beginTime,Date endTime){
		String sql = "select  s.id'stu_id',stuname,concat(simpleName,cname)'cname', "
				+ "sum(add_grade)'sum_add',"
				+ "sum(sub_grade)'sum_sub',"
				+ "sum(info.grade)'count_grade' "+
				"from student as  s "+
				"left join pro_info  as info on info.s_id = s.id "+ 
				"left join project as p on p.id = info.p_id "+
				"left join classes as c on s.c_id = c.id "+
				"left join major as m on c.m_id = m.id  "+
				"left join (select  id,grade'add_grade' from pro_info where grade > 0) as info_add on info_add.id = info.id "+
				"left join (select id,grade'sub_grade' from pro_info where grade < 0) as info_sub on info_sub.id = info.id "+
				"where p.ifmark = 1 and m.id = "+m_id +" and Date(date) between ? and ? "+
				"group by s.id "+
				"order by stu_id asc ";
		return Pro_info.dao.find(sql,beginTime,endTime);
	}
	public List<Pro_info> countStuInfo(int m_id ,int c_id,Date beginTime,Date endTime){
		String sql = "select  s.id'stu_id',stuname,concat(simpleName,cname)'cname', "
				+ "sum(add_grade)'sum_add',"
				+ "sum(sub_grade)'sum_sub',"
				+ "sum(info.grade)'count_grade' "+
				"from student as  s "+
				"left join pro_info  as info on info.s_id = s.id "+ 
				"left join project as p on p.id = info.p_id "+
				"left join classes as c on s.c_id = c.id "+
				"left join major as m on c.m_id = m.id  "+
				"left join (select  id,grade'add_grade' from pro_info where grade > 0) as info_add on info_add.id = info.id "+
				"left join (select id,grade'sub_grade' from pro_info where grade < 0) as info_sub on info_sub.id = info.id "+
				"where p.ifmark = 1 and m.id = "+m_id+" and s.c_id = "+c_id +" and Date(date) between ? and ? "+
				"group by s.id "+
				"order by stu_id asc ";
		return Pro_info.dao.find(sql,beginTime,endTime);
	}
	/**
	 *  根据学生id查找学生项目信息
	 *
	 *<p>Title:getCountInfoWith</p>
	 *@author weichao
	 *@date Apr 27, 2016
	 *@return List<Pro_info>
	 */
	public List<Pro_info> getCountInfoWithS_id(int s_id,Date beginTime,Date endTime){
		String sql = " SELECT  stuname,p_id,pname,sum(add_grade)'sum_add',sum(sub_grade)'sum_sub',sum(info.grade)'sum_grade',COUNT(p_id)'times' "
				+ "FROM pro_info as info "
				+ "left join (select id,grade'add_grade' from pro_info where grade > 0) as info_add on info_add.id = info.id "
				+ "left join (select id,grade'sub_grade' from pro_info where grade < 0) as info_sub on info_sub.id = info.id "
				+ "LEFT JOIN student as s ON s.id = info.s_id "
				+ "LEFT JOIN project as p ON p.Id = info.p_id "
				+ "WHERE s_id = " +s_id+" and Date(date) between ? and ? "
				+ "GROUP BY p_id ";
		return Pro_info.dao.find(sql,beginTime,endTime);
	}

	/**
	 * 根据学生id  和项目 id   查找学生项目信息
	 *<p>Title:getCountInfoWithS_idAndP_id</p>
	 *@author weichao
	 *@date Apr 27, 2016
	 *@return List<Pro_info>
	 */
	public List<Pro_info> getCountInfoWithS_idAndP_id(int p_id,int s_id,Date beginTime,Date endTime){
		String sql = "SELECT  stuname,pname,info.grade'grade',description,date "
				+ "FROM pro_info as info "
				+ "LEFT  JOIN project as p ON p.Id = info.p_id "
				+ "LEFT JOIN student as s ON s.Id = info.s_id "
				+ "WHERE  s_id="+s_id+" AND p_id="+p_id+" and Date(date) between ? and ?";
		return Pro_info.dao.find(sql,beginTime,endTime);
	}
	
	/*
	 * 根据项目id 学生id 起止时间查该段时间的项目明细
	 */
	public List<Pro_info> getCountInfoWithS_idAndP_idAndTime(int s_id,int p_id,Date beginTime,Date endTime)
	{
		String SELECT="SELECT s.id ,info.date,info.description,info.grade,s.stuname,p.pname'p_name',p.id'p_id'";
		String CONTENT="FROM project AS p RIGHT JOIN pro_info AS info ON p.id=info.p_idRIGHT JOIN student AS s ON s.id=info.s_id";
		String WHERE="s.id=? AND p.id=? AND Date(date) between ? and ?";
		return Pro_info.dao.find(SELECT+CONTENT+WHERE,s_id,p_id,beginTime,endTime);
	}
}

package com.business;

import java.util.List;

import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.stuManager.Models.Basicinfo;
import com.stuManager.Models.Major;
import com.stuManager.Models.Pro_info;
import com.stuManager.Models.Student;
import com.stuManager.interceptor.DateBeginIntercept;

public class BStudent {
	/**
	 * 通过id 查询某个学生的信息
	 *
	 *<p>Title:getStudentWithId</p>
	 *@author weichao
	 *@date Mar 10, 2016
	 */
	public Student getStudentWithId(int stuId)
	{

		String content = "s.id's_id',sno,stuname,stusex,qq,phone,c_id,position,face,cname,m_id,mname,simpleName,contact_1,contact_2,dorm,highgrade,single_parent";
		String sql = "select "+content+" from student as s left join classes as c on s.c_id = c.id left join major as m on c.m_id = m.id where s.id = " + stuId;
		return  Student.dao.findFirst(sql);
	}
	/**
	 *<p>Description:查询所有学生的信息 </p>
	 *<p>Title:getAllStudent</p>
	 *@author weichao
	 *@date Mar 10, 2016
	 */
	public List<Student> getAllStudent()
	{
		String content = "s.id's_id',sno,stuname,stusex,qq,phone,c_id,position,face,cname,m_id,mname,simpleName";
		String sql = "select "+content+" from student as s left join classes as c on s.c_id = c.id left join major as m on c.m_id = m.id";
		return Student.dao.find(sql);
	}
	
	/*
	 * 根据专业班级搜索学生列表
	 */
	public List<Student>searchStudent(String classId,String majorId)
	{
		String content = "s.id's_id',sno,stuname,stusex,qq,phone,c_id,position,face,cname,m_id,mname,simpleName";
		String condition=" where m_id="+majorId;
		if(classId!=""&&classId!=null&&classId.length()!=0)
		{
			condition=condition+" AND c_id="+classId;
		}
		String sql = "select "+content+" from student as s left join classes as c on s.c_id = c.id left join major as m on c.m_id = m.id"+condition;
		return Student.dao.find(sql);
	}
	/**
	 * 修改学生信息
	 */
	public boolean update(Class<Student> class1, String string) {
		return Student.dao.update();
	}
	/**
	 * 删除学生
	 *
	 *<p>Title:delete</p>
	 *@author weichao
	 *@date Mar 10, 2016
	 */
	public boolean delete(int id){
		/*//首先删除 学生的项目信息
			List<Pro_info> infoList = Pro_info.dao.find("select * from pro_info where s_id = "+id);
			for(int i =0;i<infoList.size();i++){
				infoList.get(i).delete();
			}*/
			return Student.dao.deleteById(id);
	}
	/**
	 * 查找所有的专业
	 *
	 *<p>Title:getAllMajor</p>
	 *@author weichao
	 *@date Mar 23, 2016
	 */
	public List<Major> getAllMajor() {
		String sql= "select * from major";
		return Major.dao.find(sql);
	}
	/**
	 * 根据学号查找 学生
	 *
	 *<p>Title:getWithSno</p>
	 *@author weichao
	 *@date Mar 23, 2016
	 */
	public Student getStudentWithSno(String sno){
		String sql = "select * from student where sno = '" +sno+"'";
		return Student.dao.findFirst(sql);
	}
	/**
	 * 查询所有学生的学号
	 *
	 *<p>Title:getStudentSno</p>
	 *@author weichao
	 *@date Mar 24, 2016
	 */
	public List<Student> getStudentSno(){
		String sql = "select sno from student";
		return Student.dao.find(sql);
	}
	
	/*
	 * 查询所有学生的id和学号
	 */
	public List<Student> getStudentSnoAndStuid(){
		String sql = "select sno,id from student";
		return Student.dao.find(sql);
	}
	/*
	 * 根据班级id查找这个班级的学生
	 */
	public List<Student> getStudentsWithClassId(int classId)
	
	{
		String sql = "SELECT * FROM student WHERE c_id="+classId;
		return Student.dao.find(sql);
	}
	/**
	 * 添加开学日期
	 *
	 *<p>Title:setBeginDate</p>
	 *@author weichao
	 *@date Apr 8, 2016
	 */
	public boolean setBeginDate(String date,String term){
		boolean b = true;
		String sql = "insert into setDate (date,term) values (' "+date+" ',' "+term+" ') ";
		try {
			Db.update(sql);
			
		} catch (Exception e) {
			b = false;
		}
		return b;
	}
	/**
	 * 查找所有开学日期
	 *
	 *<p>Title:getdateLsit</p>
	 *@author weichao
	 *@date Apr 8, 2016
	 *@return List<Record>
	 */
	public List<Record> getdateLsit(){
		String sql = "select * from setDate order by date desc";
		return  Db.find(sql);
	}
	/**
	 * 根据Date id查找date
	 *
	 *<p>Title:getDateWithTerm</p>
	 *@author weichao
	 *@date Apr 10, 2016
	 *@return Record
	 */
	public Record getDateWithId(int id){
		String sql = "select * from setDate where id = "+id;
		return Db.findFirst(sql);
	}	
	/*
	 * 根据学生id和学期查找学生每学期的详情信息
	 */
	public Basicinfo getBasicInfoWithIdAndtermDate(int s_id,String termDate)
	{
		String SELECT="select (case when ifload=0 then '否' else '是' end)'ifload', (case when ifzhuxue=0 then '否' else '是' end)'ifzhuxue', (case when ifyougan=0 then '否' else '是' end)'ifyougan',(case when ifyoutuan=0 then '否' else '是' end)'ifyoutuan',(case when ifsanhao=0 then '否' else '是' end)'ifsanhao',(case when ifupgraded=0 then '否' else '是' end)'ifupgraded',(case when ifoverseas=0 then '否' else '是' end)'ifoverseas',(case when ifkorea=0 then '否' else '是' end)'ifkorea',takeoffice,gradepoint,gatecount,poolstu,ranking,absent,nightout,punish,outsidehonor,schoolhonor,collegehonor,termdate ";
		String CONTENT="from basicinfo where stuid=? and termdate=?";
		return Basicinfo.dao.findFirst(SELECT+CONTENT,s_id,termDate);
	}
	/*
	 *根据学生id获取最近的一次学期记录
	 */
	public List<Basicinfo> getBasicInfoWithId(int s_id)
	{
		return Basicinfo.dao.find("select * from basicinfo where stuid = ? order by termdate desc",s_id);
	}
}

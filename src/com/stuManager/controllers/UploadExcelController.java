package com.stuManager.controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.OfficeXmlFileException;
import org.apache.poi.ss.usermodel.CellStyle;

import com.business.BMajor;
import com.business.BProject;
import com.business.BStudent;
import com.jfinal.core.Controller;
import com.jfinal.upload.UploadFile;
import com.stuManager.Models.Basicinfo;
import com.stuManager.Models.Pro_info;
import com.stuManager.Models.Project;
import com.stuManager.Models.Student;

public class UploadExcelController extends Controller {
	BStudent bs = new BStudent();
	/*
	 * 跳转到导入学生信息的页面
	 */
	
	public void toStuTable()
	{
		BMajor bm = new BMajor();
		setAttr("marjorList",bm.getAllMarjor());
		renderFreeMarker("/addStuExcel.html");
	}
	
	/*
	 * 跳转到导入导入项目信息的页面
	 */
	public void toProTable()
	{
		BProject bp = new BProject();
		setAttr("projectList", bp.getAllProject());
		renderFreeMarker("/addProExcel.html");
	}
	
	/*
	 * 跳转到导入学生详细信息页面
	 */
	public void toDetailTable()
	{
		Date date = new Date();
		int year = date.getYear()+1900;
		setAttr("year", year);
		renderFreeMarker("/addStuDetailExcel.html");
		
	}
	
	
	/*
	 * EXCEL表格批量导入学生信息表数据
	 * 
	 */
	public void indexUpLoad() throws FileNotFoundException, IOException
	{
		//获取sheet（getSheetAt(0)） ；获取表格行（ sheet.getRow(i)）；获取单元格（row.getCell()）；如下：
		// 获取文件
		String message = "";
		UploadFile file = getFile("excel");
		String path = file.getSaveDirectory() + file.getFileName();
		// 处理导入数据
		List<Map<Integer,String>> list = new ArrayList<Map<Integer,String>>();
		
		List<Student> stuList  = bs.getStudentSno(); //获取所有sno
		List<Object> listSno = new ArrayList<Object>();
		for(int z = 0;z<stuList.size();z++){
			listSno.add(stuList.get(z).get("sno"));
		}
		String failSno = "";
		
	        try {
	        	HSSFWorkbook hwb=null;
	        	 hwb = new HSSFWorkbook(new FileInputStream(new File(path)));
	        	 HSSFSheet sheet = hwb.getSheetAt(0); // 获取到第一个sheet中数据
	     		for(int i = 1;i<sheet.getLastRowNum()+1; i++) {// 第二行开始取值，第一行为标题行
	     			HSSFRow row = sheet.getRow(i);// 获取到第i列的行数据(表格行)
	     			Map<Integer, String> map = new HashMap<Integer, String>();
	     			for(int j=0;j<row.getLastCellNum(); j++) {
	     				HSSFCell cell = row.getCell(j);// 获取到第j行的数据(单元格)
	     				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	     				map.put(j, cell.getStringCellValue());
	     			}
	     			list.add(map);
	     		}
	     		for (Map<Integer, String> map : list) { // 遍历取出的数据，并保存
	    			String sno = map.get(1);
	    			
	    			Boolean mark = false;
	    			if (listSno.indexOf(sno) == -1) {
	    				listSno.add(sno);
	    				Student stu = new Student();
	    				if(map.get(0)!=null&&map.get(0).length()!=0)
	    				{
	    					stu.set("stuname", map.get(0));
	    				}
	    				if(map.get(1)!=null&&map.get(1).length()!=0)
	    				{
	    					stu.set("sno", map.get(1));
	    				}else
	    				{
	    					
	    				}
	    				if(map.get(2)!=null&&map.get(2).length()!=0)
	    				{
	    					stu.set("stusex", map.get(2));
	    				}
	    				if(map.get(3)!=null&&map.get(3).length()!=0)
	    				{
	    					stu.set("qq", map.get(3));
	    				}
	    				if(map.get(4)!=null&&map.get(4).length()!=0)
	    				{
	    					stu.set("phone", map.get(4));
	    				}
	    				if(map.get(5)!=null&&map.get(5).length()!=0)
	    				{
	    					stu.set("position", map.get(5));
	    				}
	    				if(map.get(6)!=null&&map.get(6).length()!=0)
	    				{
	    					stu.set("face", map.get(6));
	    				}
	    				if(map.get(7)!=null&&map.get(7).length()!=0)
	    				{
	    					stu.set("contact_1", map.get(7));
	    				}
	    				if(map.get(8)!=null&&map.get(8).length()!=0)
	    				{
	    					stu.set("contact_2", map.get(8));
	    				}
	    				if(map.get(9)!=null&&map.get(9).length()!=0)
	    				{
	    					stu.set("dorm", map.get(9));
	    				}
	    				
	    				if(map.get(10)!=null&&map.get(10).length()!=0)
	    				{
	    					stu.set("highgrade", map.get(10));
	    				}
	    				if(map.get(11)!=null&&map.get(11).length()!=0)
	    				{
	    					stu.set("single_parent", map.get(11));
	    				}
	    				stu.set("c_id", getPara("c_id"));
	    				mark = stu.save();
		    			if (mark==false) {
		    				failSno = failSno + sno + " ";
		    			}
	    			}
	    			 else {
	    				failSno = failSno + sno + " ";
	    				continue;
	    			}
	    		}		
	    		message = "上传成功";
	        }
	        catch (OfficeXmlFileException e) {
				message="文件格式错误，请下载模板文件,重新上传！";
			}
			catch (NullPointerException e) {
				message="表格存在空白行！请复制表格数据，重新上传！";
			}
	        catch (Exception ex) {
	        	/*
	        	 * 文件后缀名错误
	        	 */
	        	message = "导入失败，可能原因：1.文件格式错误（请下载模板文件） 2.字段格式错误";
	        }finally{
	        	file.getFile().delete();
	        	renderJson(failSno=="" ? message : (message = message+" 学号为"+failSno+" 的学生上传失败！"));
	        }
	}

	/**
	 * 导入项目信息
	 *
	 *<p>Title:uploadProInfo</p>
	 *@author weichao
	 *@date Mar 24, 2016
	 */
	public void uploadProInfo() throws FileNotFoundException, IOException
	{
//		double dTime = 0.0000115743621516652;
		String message = "";
		int uploadSuccess=0;
		//获取sheet（getSheetAt(0)） ；获取表格行（ sheet.getRow(i)）；获取单元格（row.getCell()）；如下：
		// 获取文件
		UploadFile file = getFile("excel");
		String path = file.getSaveDirectory() + file.getFileName();
		// 处理导入数据
		List<Map<Integer, String>> list = new ArrayList<Map<Integer, String>>();
		String failSno = "";
		try {
			HSSFWorkbook hwb = null;
			hwb = new HSSFWorkbook(new FileInputStream(new File(path)));
			HSSFSheet sheet = hwb.getSheetAt(0); // 获取到第一个sheet中数据
			int num = sheet.getLastRowNum();
			//这个循环用于过滤空白行。但是好像并不好使！
			//解决删掉后，存留空白行的问题
			/*for(int i = 1;i< sheet.getLastRowNum()+1; i++)
			{
				if(sheet.getRow(i)==null) 
				{
					num--;
				}
			}*/
			for (int i = 1;i< num+1; i++) {// 第二行开始取值，第一行为标题行
				HSSFRow row = sheet.getRow(i);// 获取到第i行的数据(表格行)
				Map<Integer, String> map = new HashMap<Integer, String>();
				
				//short rns = row.getLastCellNum();//这个地方读取的数值不对，excel只有4列 但是读出15列来了
				
				for (int j = 0; j < 4; j++) {
					HSSFCell cell = row.getCell(j);// 获取到第j行的数据(单元格)
					if(j == 3){
						cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
						map.put(j,  celldate(cell));
					}else{
						cell.setCellType(HSSFCell.CELL_TYPE_STRING);
						map.put(j, cell.getStringCellValue());
					}
				}		
				list.add(map);
			}
			
			//获取所有学生学号用于判断
			List<Student> stuList  = bs.getStudentSno(); //获取所有sno
			List<Object> listSno = new ArrayList<Object>();
			for(int z = 0;z<stuList.size();z++){
				listSno.add(stuList.get(z).get("sno"));
			}
			BStudent bstudent = new BStudent();
			for (Map<Integer, String> map : list) { // 遍历取出的数据，并保存
				Pro_info proinfo = new Pro_info();				
			   if(listSno.indexOf(map.get(0))>-1)
			   {
				   int s_id = bstudent.getStudentWithSno(map.get(0)).getInt("Id");
				    proinfo.set("s_id", s_id);
				    
				    if(map.get(2)!=null&&map.get(2).length()!=0)
				    {
				    	proinfo.set("description", map.get(2));
				    }
				    if(map.get(3)!=null&&map.get(3).length()!=0)
				    {
				    	proinfo.set("date",  map.get(3));
				    }
					proinfo.set("p_id", getParaToInt("p_id"));
					//判断这个项目是不是计分项目，如果为不计分项目，则不往数据库存分数
					int ifmark = Project.dao.findById(getParaToInt("p_id")).getInt("ifmark");
					if(ifmark==1)
					{
						proinfo.set("grade", map.get(1));
					}			
					Boolean mark = proinfo.save();
					if (mark==true) {
						/*
						 * 上传成功
						 */
						uploadSuccess++;
					} else {
						/*
						 * 保存用户失败
						 */
						failSno = failSno+"学号为"+map.get(0)+"的记录导入失败   ";
						continue;
					}
			   }
			   else
			   {
				   failSno=failSno+""+map.get(0)+"学生不存在";
				   continue;
			   }
			}
			message = "共上传"+list.size()+"条数据 "+"成功"+uploadSuccess+"  失败"+(list.size()-uploadSuccess);
			
		} 
		catch (OfficeXmlFileException e) {
			message="文件格式错误，请下载模板文件,重新上传！";
		}
		catch (IllegalStateException e) {
			message="excel表时间字段格式错误，请按规定格式上传！";
		}
		catch (NullPointerException e) {
			message="表格存在空白行！请复制表格数据，重新上传！";
		}
		catch (Exception ex) {
			
			ex.printStackTrace();
			/*
			 * 文件后缀名错误
			 */
			message = "导入失败，可能原因：1.文件格式错误（请下载模板文件） 2.字段格式错误";
		} finally {
			file.getFile().delete();
			renderJson(failSno=="" ? message : (message = message+" 失败原因："+failSno));
		}
	}
	
	/*
	 * EXCEL表格批量导入学生学期资料
	 * 
	 */
	public void termDataLoad() throws FileNotFoundException, IOException
	{
		//获取sheet（getSheetAt(0)） ；获取表格行（ sheet.getRow(i)）；获取单元格（row.getCell()）；如下：
		// 获取文件
		String message = "";
		UploadFile file = getFile("excel");
		String path = file.getSaveDirectory() + file.getFileName();
		// 处理导入数据
		List<Map<Integer,String>> list = new ArrayList<Map<Integer,String>>();		
		List<Student> stuList  = bs.getStudentSnoAndStuid(); //获取所有学生
		List<Object> listSno = new ArrayList<Object>();//保存所有的学号
		for(int z = 0;z<stuList.size();z++){
			listSno.add(stuList.get(z).get("sno"));
		}

		//预处理，通过学期参数取出与这学期相关的记录，比提取s_number
		String dateStr=getPara("term");
		//根据termdate查找sid 根据sid 查找 学生学号
		List<Basicinfo> BasList = Basicinfo.dao.find("select sno'stuNmber' from student right join basicinfo on student.id = basicinfo.stuid where termdate=?",dateStr);
		List<String> stunumberList=new ArrayList<String>();
		if(!BasList.isEmpty())
		{
			for(Basicinfo bas : BasList)
			{
				stunumberList.add(bas.getStr("stuNmber"));
			}
		}
		
		String failSno = "";//上传信息记录
		//整理长传的数据
	        try {
	        	HSSFWorkbook hwb=null;
	        	 hwb = new HSSFWorkbook(new FileInputStream(new File(path)));
	        	 HSSFSheet sheet = hwb.getSheetAt(0); // 获取到第一个sheet中数据
	     		for(int i = 1;i<sheet.getLastRowNum()+1; i++) {// 第二行开始取值，第一行为标题行
	     			HSSFRow row = sheet.getRow(i);// 获取到第i列的行数据(表格行)
	     			Map<Integer, String> map = new HashMap<Integer, String>();
	     			for(int j=0;j<row.getLastCellNum(); j++) {
	     				HSSFCell cell = row.getCell(j);// 获取到第j行的数据(单元格)
	     				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	     				map.put(j, cell.getStringCellValue());
	     			}
	     			list.add(map);
	     		}
	     		int uploadSuccess=0;
	     		for (Map<Integer, String> map : list) { // 遍历取出的数据，并保存
	    			String sno = map.get(0);
	    			Boolean mark = false;
	    			//根据学号判断一下数据库有没有这个学生，如果没有该学生，则不允许上传
	    			int stuindex=listSno.indexOf(sno);
	    			if (stuindex>-1){
	    				//如果存在该学生，则取出该学生的id
	    				Student  stu = stuList.get(stuindex);
	    				int stuId = stu.getInt("id");
	    				
	    				//判断一下该学生是不是已经存在本学期的学期资料
	    				if(stunumberList.indexOf(sno)>-1)
	    				{
	    					//如果存在，则不让再次提交
	    					failSno = failSno + sno + "该生本学期信息已存在  ";
	    					continue;
	    				}
	    				else
	    				{
	    					//还没有上传这个学生的信息，网数据库插入这个学生的信息
	    					Basicinfo basic = new Basicinfo();
	    					if(map.get(1)!=null&&map.get(1).length()!=0){
	    						basic.set("ifload", map.get(1));//是否贷款
	    					}
	    					if(map.get(2)!=null&&map.get(2).length()!=0){
	    						basic.set("ifzhuxue", map.get(2));//是否勤工助学
	    					}
	    					if(map.get(3)!=null&&map.get(3).length()!=0){
	    						basic.set("takeoffice", map.get(3));//学生干部任职情况
	    					}
	    					if(map.get(4)!=null&&map.get(4).length()!=0){
	    						basic.set("gradepoint", map.get(4));//学期绩点
	    					}
	    					if(map.get(5)!=null&&map.get(5).length()!=0){
	    						basic.set("gatecount", map.get(5));//学期不及格门数
	    					}
	    					if(map.get(6)!=null&&map.get(6).length()!=0){
	    						basic.set("poolstu", map.get(6));//学期贫困认定情况
	    					}
	    					if(map.get(7)!=null&&map.get(7).length()!=0){
	    						basic.set("ranking", map.get(7));//学期综合素质排名
	    					}
	    					if(map.get(8)!=null&&map.get(8).length()!=0){
	    						basic.set("absent", map.get(8));//学期旷课数
	    					}
	    					if(map.get(9)!=null&&map.get(9).length()!=0){
	    						basic.set("nightout", map.get(9));//学期夜不归宿数
	    					}
	    					if(map.get(10)!=null&&map.get(10).length()!=0){
	    						basic.set("punish", map.get(10));//本学期历次违纪处分记录
	    					}
	    					if(map.get(11)!=null&&map.get(11).length()!=0){
	    						basic.set("ifyougan", map.get(11));//本学期是否优秀班干
	    					}
	    					if(map.get(12)!=null&&map.get(12).length()!=0){
	    						basic.set("ifsanhao", map.get(12));//本学期是否三好学生
	    					}
	    					if(map.get(13)!=null&&map.get(13).length()!=0){
	    						basic.set("ifyoutuan", map.get(13));//本学期是否优秀团员
	    					}
	    					if(map.get(14)!=null&&map.get(14).length()!=0){
	    						basic.set("outsidehonor", map.get(14));//本学期获得的校外荣誉
	    					}
	    					if(map.get(15)!=null&&map.get(15).length()!=0){
	    						basic.set("schoolhonor", map.get(15));//本学期获得的校级荣誉
	    					}
	    					if(map.get(16)!=null&&map.get(16).length()!=0){
	    						basic.set("collegehonor", map.get(16));//本学期获得的院级荣誉
	    					}
	    					if(map.get(17)!=null&&map.get(17).length()!=0){
	    						basic.set("ifupgraded", map.get(17));//是否有专升本意向
	    					}
	    					if(map.get(18)!=null&&map.get(18).length()!=0){
	    						basic.set("ifoverseas", map.get(18));//是否欧美留学意向
	    					}
	    					if(map.get(19)!=null&&map.get(19).length()!=0){
	    						basic.set("ifkorea", map.get(19));//是否有韩国留学意向
	    					}
	    					basic.set("stuid",stuId);//绑定学生id
	    					basic.set("termdate", getPara("term"));//学期
	    					mark = basic.save();	    					
	    					//上传成功，则记录一下
	    					if(mark==true)
	    					{
	    						uploadSuccess++;
	    					}else
	    					{
	    						failSno = failSno + sno + "字段错误 ";
	    					}	
	    				}	    				    							    			
	    			}
	    			 else {
	    				failSno = failSno + sno + "该生不存在   ";
	    				continue;
	    			}
	    		}
	    		
	    		message = "共上传"+list.size()+"条数据 "+"成功"+uploadSuccess+"  失败"+(list.size()-uploadSuccess);
	        }
	        
	        catch (OfficeXmlFileException e) {
				message="文件格式错误，请下载模板文件,重新上传！";
			}
			catch (NullPointerException e) {
				message="表格存在空白行！请复制表格数据，重新上传！";
			}
	        catch (Exception ex) {
	        	/*
	        	 * 其他异常，如字段不能为空
	        	 */
	        	message ="导入失败，可能原因：1.文件格式错误（请下载模板文件） 2.字段格式错误";
	        }finally{
	        	file.getFile().delete();
	        	renderJson(failSno=="" ? message : (message = message+" 失败原因："+failSno));
	        }
	}
	
	/**
	 * excel  日期格式转换
	 */
	private String celldate(HSSFCell cell){
		String result = new String();  
        switch (cell.getCellType()) {  
        case HSSFCell.CELL_TYPE_NUMERIC:// 数字类型  
            if (HSSFDateUtil.isCellDateFormatted(cell)) {// 处理日期格式、时间格式  
                SimpleDateFormat sdf = null;  
                if (cell.getCellStyle().getDataFormat() == HSSFDataFormat.getBuiltinFormat("h:mm")) {  
                    sdf = new SimpleDateFormat("HH:mm");  
                } else {// 日期  
                    sdf = new SimpleDateFormat("yyyy-MM-dd");  
                }
                Date date = cell.getDateCellValue();  
                result = sdf.format(date);  
            } else if (cell.getCellStyle().getDataFormat() == 58) {  
                // 处理自定义日期格式：m月d日(通过判断单元格的格式id解决，id的值是58)  
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");  
                double value = cell.getNumericCellValue();  
                Date date = org.apache.poi.ss.usermodel.DateUtil.getJavaDate(value);  
                result = sdf.format(date);  
            } else {  
                double value = cell.getNumericCellValue();  
                CellStyle style = cell.getCellStyle();  
                DecimalFormat format = new DecimalFormat();  
                String temp = style.getDataFormatString();  
                // 单元格设置成常规  
                if (temp.equals("General")) {  
                    format.applyPattern("#");  
                }  
                result = format.format(value);  
            }  
            break;  
        case HSSFCell.CELL_TYPE_STRING:// String类型  
            result = cell.getRichStringCellValue().toString();  
            break;  
        case HSSFCell.CELL_TYPE_BLANK:  
            result = "";  
        default:  
            result = "";  
            break;  
        }  
        return result;  
    }
}


























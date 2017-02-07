package com.stuManager.Models;

import com.jfinal.plugin.activerecord.Model;

public class Student extends Model<Student> {

	/**
	 * 创建模型
	 */
	private static final long serialVersionUID = 1L;

	public static final Student dao= new Student();
}

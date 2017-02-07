package com.stuManager.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.mysql.jdbc.Util;

public class Utils extends Coder {
	//特殊字符
	private static final String PATTERN_SPECIAL = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）+|{}【】‘；：”“’。，、？]"; 
	private static final String KEY_SHA = "SHA";
	private static final String KEY_MD5 = "MD5";
	
	private static Utils utils = null;

	private Utils() {
	}

	/**
	 * 获取实例
	 *<p>Title:getInstace</p>
	 *@return Utils
	 *@author weichao  @date 2017年1月6日
	 */
	public static Utils getInstace() {
		synchronized (Util.class) {
			if (utils == null) {
				utils = new Utils();
			}
		}
		return utils;
	}
	/**
	 * md5加密
	 *<p>Title:getMD5Str</p>
	 *@param str
	 *@return String
	 *@author weichao  @date 2017年1月6日
	 */
	public synchronized  final String getMD5Str(String str) {
		return getCoderStr(str, KEY_MD5);
	}
	
	/**
	 *SHA 加密 
	 *<p>Title:getSHAStr</p>
	 *@param str
	 *@return String
	 *@author weichao  @date 2017年1月6日
	 */
	 public synchronized  final String getSHAStr(String str) {
		return getCoderStr(str,KEY_SHA);
	}
	 
	 /**
	 * 本地密码加密算法
	 *<p>Title:pwdEncryption</p>
	 *@param pwd
	 *@return String
	 *@author weichao  @date 2017年1月6日
	 */
	public synchronized final String pwdEncryption(String pwd){
		 return getMD5Str(getSHAStr("gj"+pwd+"jy"));
	 }
	
	/**
	 *	字符串非法字符校验
	 *	含有非法字符返回true
	 *<p>Title:validateIllegal</p>
	 *@param str
	 *@return boolean
	 *@author weichao  @date 2017年1月6日
	 */
	public synchronized final boolean validateIllegal(String str){
		boolean b = false;
		Pattern p = Pattern.compile(PATTERN_SPECIAL);
		Matcher m = p.matcher(str);
		if(m.find()){
			b = true;
		}
		return b;
	}
}

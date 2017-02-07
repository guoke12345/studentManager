package com.stuManager.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 基础加密组件
 * 
 * @author 梁栋
 * @version 1.0
 * @since 1.0
 */
public abstract class Coder {
	
	/**
	 * 加密算法
	 *<p>Title:getMD5Str</p>
	 *@param str
	 *@return String
	 *@author weichao  @date 2017年1月6日
	 */
	protected static final String getCoderStr(String str,String key) {
		MessageDigest messageDigest = null;
		try {
			messageDigest = MessageDigest.getInstance(key);
			messageDigest.reset();
			messageDigest.update(str.getBytes());
		} catch (NoSuchAlgorithmException e) {
			System.out.println("md5 error:" + e.getMessage());
		}

		byte[] byteArray = messageDigest.digest();
		StringBuffer md5StrBuff = new StringBuffer();
		for (int i = 0; i < byteArray.length; i++) {
			if (Integer.toHexString(0xFF & byteArray[i]).length() == 1)
				md5StrBuff.append("0").append(Integer.toHexString(0xFF & byteArray[i]));
			else
				md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
		}
		return md5StrBuff.toString();
	}
}
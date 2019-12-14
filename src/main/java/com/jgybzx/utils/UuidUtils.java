package com.jgybzx.utils;

import java.util.UUID;

/**
 * 产生UUID随机字符串工具类
 */
public final class UuidUtils {
	private UuidUtils(){}
	public static String getUuid(){
		//自动生成的是 36的字符，中间有四个横杠，所以需要替换掉
		return UUID.randomUUID().toString().replace("-","");
	}
	/**
	 * 测试
	 */
	public static void main(String[] args) {
		System.out.println(UuidUtils.getUuid());
		System.out.println(UuidUtils.getUuid());
		System.out.println(UuidUtils.getUuid());
		System.out.println(UuidUtils.getUuid());
	}
}

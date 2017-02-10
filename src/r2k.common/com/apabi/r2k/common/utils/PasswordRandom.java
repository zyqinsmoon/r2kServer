package com.apabi.r2k.common.utils;

import org.apache.commons.lang.StringUtils;

public class PasswordRandom {
	
	public static String getRandomPassword(int numberCount){
		StringBuffer passwordString = new StringBuffer("");
		String charString = PropertiesUtil.get("randseeds");//获取配置文件中randseeds密码生成
		if(numberCount > 0 && StringUtils.isNotEmpty(charString)){//随机生成密码的长度大于0且生成密码的字符集不能为空
			int charLength = charString.length()-1;//获得生成密码的字符集下标长度
			for(int i=0;i <numberCount;i++){
				passwordString.append(charString.charAt((int)Math.round(Math.random()*charLength)));//根据字符集的长度随机生成字符集下标，取得字符
			}
		}
		return passwordString.toString();
	}
	
//	public static String  getString(){
//		StringBuffer s = new StringBuffer("0123456789");
//		for(int i= 65;i<=90;i++){
//			s.append((char)i);
//			s.append((char)(i+32));
//		}
//		System.out.println(s);
//		return s.toString();
//}
}

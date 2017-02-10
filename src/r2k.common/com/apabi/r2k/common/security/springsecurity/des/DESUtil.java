package com.apabi.r2k.common.security.springsecurity.des;


import java.security.Key;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
    public class DESUtil {
        public static final String KEY_STRING = "2E76#29J1$c6&a5";//生成密钥的字符串 
        static Key key; 
        // 根据参数生成KEY   
        public static void getKey(String strKey) {     
            try{ 
               KeyGenerator _generator = KeyGenerator.getInstance("DES");   
               _generator.init(new SecureRandom(strKey.getBytes()));  
               key = _generator.generateKey();    
               _generator = null; 
			   }catch (Exception e) {
			   e.printStackTrace();  
			    }     
			  }      
    // 加密String明文输入,String密文输出    

      public static String getEncString(String strMing) { 
		  DESUtil.getKey(KEY_STRING);// 生成密匙
		  byte[] byteMi = null;  
		  byte[] byteMing = null;  
		  String strMi = "";     
		  BASE64Encoder base64en = new BASE64Encoder();
		  try{ 
			  byteMing = strMing.getBytes("UTF8");
			  byteMi = getEncCode(byteMing);   
			  strMi = base64en.encode(byteMi);  
		  }catch (Exception e) {     
		    	e.printStackTrace();      
		  }finally { 
		      base64en = null; 
		      byteMing = null;  
		      byteMi = null;   
		   }     
		   return strMi;  
		 }  
   // 解密 以String密文输入,String明文输出    

      public static String getDesString(String strMi) {  
		 DESUtil.getKey(KEY_STRING);// 生成密匙 
		 BASE64Decoder base64De = new BASE64Decoder();   
		 byte[] byteMing = null;  
		 byte[] byteMi = null;    
		 String strMing = "";
		 try {
			 byteMi = base64De.decodeBuffer(strMi);
			 byteMing = getDesCode(byteMi); 
			 strMing = new String(byteMing, "UTF8");  
		}catch (Exception e) {     
		     e.printStackTrace(); 
		}finally {  
			base64De = null;
			byteMing = null;
			byteMi = null;
		}
		 return strMing;
		}     
// 加密以byte[]明文输入,byte[]密文输出   
     private static byte[] getEncCode(byte[] byteS) {
    	 byte[] byteFina = null; 
         Cipher cipher; 
         try {  
			cipher = Cipher.getInstance("DES");   
			cipher.init(Cipher.ENCRYPT_MODE, key); 
			byteFina = cipher.doFinal(byteS);    
			} catch (Exception e) {     
			e.printStackTrace();   
			}finally { 
			cipher = null; 
			}     
			return byteFina;  
			 }      
     // 解密以byte[]密文输入,以byte[]明文输出  
     private static byte[] getDesCode(byte[] byteD) {     
		Cipher cipher;  
		byte[] byteFina = null; 
		try {  
			cipher = Cipher.getInstance("DES");   
			cipher.init(Cipher.DECRYPT_MODE, key); 
			byteFina = cipher.doFinal(byteD); 
		} catch (Exception e) {  
			e.printStackTrace(); 
		}finally { 
			cipher = null;  
		}    
			return byteFina;    
		  }   
     public static void main(String[] args) {    
	     String strEnc = DESUtil.getEncString("manager12@@  $$$%%  1");// 加密字符串,返回String的密文  
	     System.out.println(strEnc);  
	     String strDes = DESUtil.getDesString("mW98YFWjm7mqJ2+MZ/f8J8zKLqU7YyOB");// 把String 类型的密文解密      
	     System.out.println(strDes); 
	  
	    }     
	}     

   
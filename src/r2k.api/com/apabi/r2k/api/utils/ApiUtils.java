package com.apabi.r2k.api.utils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpHeaders;

import com.apabi.r2k.common.utils.PropertiesUtil;

public class ApiUtils {
	//处理文件下载的头信息
	public static void setHeader(HttpServletResponse response,String lastModified){
		response.setContentType("application/zip");
		response.setHeader("Content-Disposition","attachment; filename=\"menu.zip\""); 
		response.setHeader(HttpHeaders.LAST_MODIFIED,lastModified);
	}
	
	/**
	 * 获取文件GMT格式的lastModified
	 * @param filePath
	 * @return
	 */
	public static String getFileLastModifiedForGMT(File file){
		Date date = new Date(file.lastModified());
		SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss 'GMT'",Locale.US);
		sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
		String gmt = sdf.format(date);
		return gmt;
	}
	
	
	
	/**
	 * 判断是文件并且文件存在 true
	 * @param filepath
	 * @return boolean
	 */
	public static boolean fileExists(String filepath){
		boolean exists = false;
		File file = new File(filepath);
		if(file.isFile()&&file.exists()){
			exists=true;
		}
		return exists;
	}
	public static String getFileBasePath(String filetype) throws Exception{
		String filepath = "";
		if(filetype.equals(PropertiesUtil.get("path.info"))){
			filepath = PropertiesUtil.getValue("path.info.pub");
		}else if(filetype.equals(PropertiesUtil.get("path.menu"))){
			filepath = PropertiesUtil.getValue("path.menu.pub");
		}
		return filepath;
	}
	/*#菜单目录
	path.menu=menu
	#菜单文件上传目录r2kFile/menu/res/orgid/devicetype或者deviceid/文件
	path.menu.res={path.menu}/{path.res}
	#菜单发布目录r2kFile/menu/pub/orgid/devicetype或者deviceid/文件
	path.menu.pub={path.menu}/{path.pub}*/
	public static void main(String[] args) {
		System.out.println(fileExists("d:/test.txt"));
	}

	
}

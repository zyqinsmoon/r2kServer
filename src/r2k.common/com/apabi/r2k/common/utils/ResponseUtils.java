package com.apabi.r2k.common.utils;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

public class ResponseUtils {
	/**
	 * 将xml输出
	 */
	public static void responseOutXml(HttpServletResponse response,String xml)throws Exception
	{
		    PrintWriter out=null;
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/xml");
			out=response.getWriter();
			out.write(xml);
			out.flush();
			out.close();
	}

}

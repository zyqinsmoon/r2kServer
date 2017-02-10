package com.apabi.r2k.msg.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface MsgConnector {
	/**
	 * 服务端调用
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void serverHandler(HttpServletRequest request,HttpServletResponse response) throws Exception;
	/**
	 * 客户端端调用
	 * @param request
	 * @param response
	 * @throw Exception
	 * */
	public void clientHandler(HttpServletRequest request,HttpServletResponse response)throws Exception;

}

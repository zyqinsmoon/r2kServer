<%@ page language="java" import="java.util.*,java.io.*" 
	contentType="text/xml; charset=UTF-8" pageEncoding="UTF-8"%>
	<%
	Exception content = (Exception)request.getAttribute("exception");
	response.setContentType("text/xml;charset=UTF-8");  
    PrintWriter writer = response.getWriter();  
    writer.write(content.getMessage());
    response.flushBuffer();  
	%> 
   	



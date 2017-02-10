<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no">
    <meta name="apple-mobile-web-app-capable" content="yes">
    <meta name="apple-touch-fullscreen" content="yes">
    <link rel="stylesheet" href="${ctx}/r2k/wx/css/base.css">
    <script src="${ctx}/r2k/wx/js/jquery-1.10.2.min.js"></script>
    <script src="${ctx}/r2k/wx/js/wx.js"></script>
<title>方正阿帕比数字阅读</title>
</head>
<body>
<div class="wrap">
  <div class="w_header">推荐阅读</div>
  <ul class="recommond">   
  <s:iterator value="ebooks" var="ebook" status="st">
	<li><a href='/r2k/wx/getBookDetail1.do?metaid=<s:property value="#ebook.Identifier"/>&orgid=<s:property value="orgid"/>'>
	  <img src='<s:property value="#ebook.CoverUrl"/>' title='<s:property value="#ebook.Title"/>' />
      <div>
        <p><s:property value="#ebook.Title"/></p>
        <span><s:property value="#ebook.Creator"/><br/>
        <s:property value="#ebook.Publisher"/></span>
      </div></a>
    </li>
		</s:iterator>
  </ul>
</div>
</body>
</html>
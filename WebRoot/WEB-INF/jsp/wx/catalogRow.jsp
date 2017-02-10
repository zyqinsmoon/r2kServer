<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no">
    <meta name="apple-mobile-web-app-capable" content="yes">
    <meta name="apple-touch-fullscreen" content="yes">
    <link rel="stylesheet" href="${ctx}/r2k/wx/css/base-plus.css"/>
    <script src="${ctx}/r2k/wx/js/jquery-1.10.2.min.js"></script>
    <script src="${ctx}/r2k/wx/js/wx.js"></script>
	<title>方正阿帕比数字阅读</title>
	<script>
	$(function(){
	    $(".goback").click(function(){
	      history.back();
	    });
	});
	</script>
</head>
<body class="listbody">
<header>
  <ul>
    <li><div class="goback"><a href=""></a><span></span></div></li>
    <li>目录</li>
  </ul>
</header>



<div class="wrap">
  <div class="blankh1"></div>
  <ul class="catalog">
		<s:iterator value="catalogRowMapList" var="map" status="st">
		<s:iterator value="#map" var="entry">
			<li><a href='<s:if test="#parameters.urlType[0] == 0">/r2k/wx/ebook.do</s:if><s:else>/r2k/wx/ebook1.do</s:else>?metaid=<s:property value="metaid"/>&pageid=<s:property value="#entry.value"/>&orgid=<s:property value="orgid"/>'>
			<s:if test="#entry.key != '' && #entry.key.length() > 12"><s:property value="#entry.key.substring(0,12)"/>...</s:if>
			<s:else> <s:property value="#entry.key"/></s:else>
			  <span><s:property value="#entry.value"/></span></a>
			</li>
		</s:iterator>
	</s:iterator>
		
  </ul>
</div>

</body>
</html>
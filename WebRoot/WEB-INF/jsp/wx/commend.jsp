<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="r2k" value="${pageContext.request.contextPath}" scope="page" />
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no">
    <meta name="apple-mobile-web-app-capable" content="yes">
    <meta name="apple-touch-fullscreen" content="yes">
    <link rel="stylesheet" href="${ctx}/r2k/wx/css/base-plus.css">
    <script src="${r2k}/wx/js/jquery-1.10.2.min.js"></script>
    <script src="${r2k}/wx/js/wx.js"></script>
	<title>方正阿帕比数字阅读</title>
	<script>
	$(function(){
	   $(".search1").click(function(){
	      $(".seabar").toggle(); 
	   });
	   $(".seabar").delegate("input", "focus", function(){
			$(this).attr("value","");
			$(this).removeClass();
			$(window).keydown(function(event){
				if(event.keyCode == 13){
					openSearch();
				}
			});
	   });
	   $(".seabar #searchBtn").click(function(){
           openSearch();
       });
	
	});
	function openSearch(){
	  var text = $(".seabar input")[0].value;
	  if($.trim(text)== "" || text == $(".seabar input").attr("initval")){
	  	 return;
	  }
  	  location.href = "${r2k}/wx/b/ms-p/<s:property value='orgid'/>/"+text+"/1/20";
    }
	</script>
</head>
<body>
<header>
  <ul>
    <li>&nbsp;</li>
    <li>推荐阅读</li>
    <li><a href='${r2k}/wx/b/ca-p/<s:property value="orgid"/>' class="catalog1"></a>
        <a class="search1"></a>
    </li>
  </ul>
</header>
<nav class="seabar" style="display:none">
	<span><input type="search" value="请输入关键词" initval = "请输入关键词" class="init" /></span>
	<a href="javascript:" class="cancel" id="searchBtn">检索</a>
</nav>
<div class="wrap"> 
  <div class="blankh1"></div>
  <ul class="recommond">   
  <s:iterator value="ebooks" var="ebook" status="st">
	<li><a href='/r2k/wx/getBookDetail.do?metaid=<s:property value="#ebook.Identifier"/>&orgid=<s:property value="orgid"/>'>
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
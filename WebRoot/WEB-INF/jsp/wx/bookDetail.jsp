<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no">
    <meta name="apple-mobile-web-app-capable" content="yes">
    <meta name="apple-touch-fullscreen" content="yes">
    <link rel="stylesheet" href="${ctx}/r2k/wx/css/base-plus.css">
    <script src="${ctx}/r2k/wx/js/jquery-1.10.2.min.js"></script>
    <script src="${ctx}/r2k/wx/js/jquery.cookie.js"></script>
    <script src="${ctx}/r2k/wx/js/wx.js"></script>
    <script src="${ctx}/r2k/wx/js/mobile.js"></script>
    <script>
    function openComment(){
    	
    	var _version = getOsVersion();	
    	var oBorDiv = $("<div class='borrowdiv'></div>");
    	$("body").append(oBorDiv);
    	if(_version == "iOS"){
    		oBorDiv.addClass("bios");
    	}
		oBorDiv.click(function(){
			$(this).remove();		
		});
    }
    function borrow(){
    	$.cookie("apabi_wx_cookie_borrow","_01",{path:"/",expires:1});
    	if(isMicroMessenger()){
    		openComment();
    	}
    	else{
    		window.location.href='<s:property value="ebook.borrowUrl" escape="false"/>';
    	}
    }
    $(function(){
       $(".goback").click(function(){
	      history.back();
	   });
    })
    </script>
<title><s:property value="ebook.Title"/></title>
</head>
<body>
<header>
  <ul>
    <li><div class="goback"><a href=""></a><span></span></div></li>
    <li>书籍详情</li>
    
  </ul>
</header>
<div class="blankh1"></div>
<div class="wrap">
  <!-- <div class="w_header">书籍详情</div> -->
  <div class="detail">
      <a href="#"><img src='<s:property value="ebook.CoverUrl"/>' title='<s:property value="ebook.Title"/>'/></a>
      <div>
        <a href=#><s:property value="ebook.Title"/></a>
        <span><s:property value="ebook.Creator"/><br/>
          <s:property value="ebook.Publisher"/><br/>
        <s:property value="ebook.PublishDate"/>
        </span>
      </div>
  </div>
  <div class="sub_area">
    <a href="javascript:" onclick="borrow();"><input type="button" value="借阅" class="borrowbtn"/></a>
    <a href='/r2k/wx/ebook.do?metaid=<s:property value="ebook.Identifier"/>&orgid=<s:property value="orgid"/>'><input type="button" value="在线阅读" class="readbtn"/></a>
  </div>
  <div class="adstra">
  	<p>&#12288;&#12288;<s:property value="ebook.Abstract"/></p>
  </div>
</div>
</body>
</html>
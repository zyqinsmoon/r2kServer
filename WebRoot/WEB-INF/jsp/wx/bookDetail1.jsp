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
    <script src="${ctx}/r2k/wx/js/jquery.cookie.js"></script>
    <script src="${ctx}/r2k/wx/js/wx.js"></script>
    <script src="${ctx}/r2k/wx/js/mobile.js"></script>
    <script>
    function openComment(){
    	var _version = getOsVersion();	
    	var oBorDiv = $("<div class='borrowdiv'></div>");
    	$("body").append(oBorDiv);
    	if(_version == "iOS"){
    		var oBorDiv = $(".borrowdiv").addClass("bios");
    	}
		oBorDiv.click(function(){
			$(this).remove();		
		});
    }
    
    function borrow(){
     if($.cookie('apabi_wx_cookie_borrow')){
          window.location.href='<s:property value="ebook.borrowUrl" escape="false"/>';
        }else{
            openComment();
			$.cookie("apabi_wx_cookie_borrow","_01",{path:"/",expires:1});
        }
    }
    </script>
<title><s:property value="ebook.Title"/></title>
</head>
<body>
<div class="wrap">
  <div class="w_header">书籍详情</div>
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
    <a href='/r2k/wx/ebook1.do?metaid=<s:property value="ebook.Identifier"/>&orgid=<s:property value="orgid"/>'><input type="button" value="在线阅读" class="readbtn"/></a>
  </div>
  <div class="adstra">
  	<p>&#12288;&#12288;<s:property value="ebook.Abstract"/></p>
  </div>
</div>
</body>
</html>
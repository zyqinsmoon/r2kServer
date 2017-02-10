<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="r2k" value="${pageContext.request.contextPath}" scope="page" />
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no">
    <meta name="apple-mobile-web-app-capable" content="yes">
    <meta name="apple-touch-fullscreen" content="yes">
    <link rel="stylesheet" href="${r2k}/wx/css/base-plus.css">
    <script src="${r2k}/wx/js/jquery-1.10.2.min.js"></script>
    <script src="${r2k}/wx/js/wx.js"></script>
	<title>方正阿帕比数字阅读</title>
	<script>
	$(function(){
	   $("body").css("background","#f9f5f0");
	   $.openChildMenu();
	   $(".goback").click(function(){
	      history.back();
	   });
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

	var isAjax;
	$.openChildMenu = function(){
		$(".category li>span").click(function(){
		  $(".seabar").hide();
		  
		  if(isAjax){
			  return;
		  }
		  
		  $(this).parent().siblings().find(">div").slideUp(300);
		  $(this).parent().siblings().find(">span").removeClass("active");
		  
		  
		  
		  var oChild = $("<div>");
		  
		  oChild.addClass("catloading");
		  $(this).parent().append(oChild);
		  ChangeState($(this).next("div"),$(this));
		  if($(this).next("div").find("a").length>0){
			  return;
		  }
		  
		  var self = this;
		  var xmlUrl = "${r2k}/wx/b/ca-x/<s:property value='orgid'/>/"+$(this).attr("child");
		  
		  $.ajaxData(xmlUrl, function(xml){
		  	$(xml).find("Category > CatItem").each(function(){
		  		var oLink = $("<a>");
		  		oLink.attr("code",$(this).attr("Code"));
		  		oLink.append($(this).attr("Name"));
		  		oChild.append(oLink);
		  		oChild.removeClass("catloading");
		  	});
		  	
		  	//ChangeState(oChild,$(self));
		  });

	   });
	   $(".category li").delegate("div>a","click",function(){
	   	  var _url = "${r2k}/wx/b/cs-p/<s:property value='orgid'/>/"+$(this).attr("code")+"/1/20";
	   	  location.href = _url;
	   });
	   
	   function ChangeState(obj1,obj2){
	   	  obj1.slideToggle(300);
	   	  obj2.toggleClass("active");
	   }
	}
	$.ajaxData = function(link,callback) {
		var _link = link;
		var beginTime, endTime;
		isAjax = true;
		$.ajax({
			url : _link,
			timeout: 15000,
			success : function(responseData) {
				callback(responseData);
				isAjax = false;
			},
			error: function (XMLHttpRequest, textStatus, errorThrown) {
				
			}
		});
	};
	</script>
</head>
<body>
<header>
  <ul>
    <li><div class="goback"><a href=""></a><span></span></div></li>
    <li>图书分类</li>
    <li><a href='${r2k}/wx/b/cl/<s:property value="orgid"/>' class="hot1"></a>
        <a class="search1"></a>
    </li>
  </ul>
</header>
<nav class="seabar" style="display:none">
	<span><input type="search" value="请输入关键词" initval = "请输入关键词"  class="init" /></span>
	<a href="javascript:" class="cancel" id="searchBtn">检索</a>
</nav>
<div class="blankh1"></div>
<div class="wrap">
  
  <ul class="category">
    <s:iterator value="categorys" var="category">
      <s:if test="HasChild == 1">
        <li><span class="selected" child='<s:property value="Code"/>'><s:property value="#category.Name"/></span></li>
      </s:if>
      <s:else>
        <li><span><s:property value="#category.Name"/></span></li>
      </s:else>
	</s:iterator>
  </ul>
</div>

</body>
</html>
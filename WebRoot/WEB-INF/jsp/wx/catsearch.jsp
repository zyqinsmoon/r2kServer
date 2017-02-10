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
    <link rel="stylesheet" href="${r2k}/wx/css/base-plus.css">
    <script src="${r2k}/wx/js/jquery-1.10.2.min.js"></script>
    <script src="${r2k}/wx/js/wx.js"></script>
	<title>方正阿帕比数字阅读</title>
	<script>
	var bflag;
	var page = 2;
	var maxPage = parseInt(<s:property value="TotalCount"/>/20)+1;
	$.ajaxData = function(link,callback) {
		bflag = true;
		var _link = link;
		var beginTime, endTime;
		$.ajax({
			url : _link,
			timeout: 15000,
			success : function(responseData) {
				callback(responseData);
				bflag = false;
			},
			error: function (XMLHttpRequest, textStatus, errorThrown) {	
			}
		});
	};
	window.onscroll = function(){
		if(page>maxPage){
			return;
		}
		var viewHeight = $(window).height();
		var scrollY = document.documentElement.scrollTop || document.body.scrollTop;
		if(viewHeight + scrollY > $(".wrap").height()-50 && !bflag){
		  var _url = location.href;
		  if('<s:property value="Code"/>'){
		  	  var link = '${r2k}/wx/b/cs-x/<s:property value="orgid"/>/<s:property value="Code"/>/'+page+"/20";//r2k/wx/b/cs-x/apabi/010/2/20
		  }
		  else if('<s:property value="Key"/>'){
		  	  var link = '${r2k}/wx/b/ms-x/<s:property value="orgid"/>/<s:property value="Key"/>/'+page+"/20";//r2k/wx/b/ms-x/swhy/北京/1/20
		  }
		  
		  $.ajaxData(link, addNewList);
		}	
	};
	function addNewList(xml){
		$(xml).find("Records>Record").each(function(){
		    var strA = "<a href='${r2k}/wx/getBookDetail.do?metaid="+$(this).find("Identifier").text()+"&orgid=<s:property value="orgid"/>'>";
		    var strImg = "<img src='"+ $(this).find("CoverUrl").text() +"' />";
			var strDiv = "<div><p>"+$(this).find("Title").text()+"</p><span>"+$(this).find("Creator").text()+"<br/>"+$(this).find("Publisher").text()+"<br/></span></div>";
			var oLi = $("<li>");
			oLi.html(strA + strImg + strDiv +"</a>");
			$(".recommond").append(oLi);
			
		});
		page++;
	}
	$(function(){
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
	/* var _url = location.href;
	 var reg = /\/<s:property value='orgid'/>\/(\d+)/i;
	 var r = _url.match(reg);
	 var code = r[1];
	 */
	</script>
</head>
<body>
<header>
  <ul>
    <li><div class="goback"><a href=""></a><span></span></div></li>
    <li>图书列表</li>
    <li>
        <a class="search1"></a>
    </li>
  </ul>
</header>
<nav class="seabar" style="display:none">
	<span><input type="search" value="请输入关键词" initval = "请输入关键词"  class="init" /></span>
	<a href="javascript:" class="cancel" id="searchBtn">检索</a>
</nav>
<div class="wrap">
  <div class="blankh1"></div>
  <p class="catres">总共<span><s:property value="TotalCount"/></span>本图书</p>
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
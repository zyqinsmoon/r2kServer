<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta charset="utf-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no">
    <meta name="apple-mobile-web-app-capable" content="yes">
    <meta name="apple-touch-fullscreen" content="yes">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/pubtemplet/templet/test1/css/common.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/pubtemplet/templet/test1/css/stream.css">
    <style type="text/css">
     .content p{
     	text-indent:2em;
     	padding:4px 0;
     }
    </style>
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-1.10.2.min.js"></script>
<script>
//日期格式化
Date.prototype.format = function(fmt) 
{ //author: meizz 
  var o = { 
    "M+" : this.getMonth()+1,                 //月份 
    "d+" : this.getDate(),                    //日 
    "h+" : this.getHours(),                   //小时 
    "m+" : this.getMinutes(),                 //分 
    "s+" : this.getSeconds(),                 //秒 
    "q+" : Math.floor((this.getMonth()+3)/3), //季度 
    "S"  : this.getMilliseconds()             //毫秒 
  }; 
  if(/(y+)/.test(fmt)) 
    fmt=fmt.replace(RegExp.$1, (this.getFullYear()+"").substr(4 - RegExp.$1.length)); 
  for(var k in o) 
    if(new RegExp("("+ k +")").test(fmt)) 
  fmt = fmt.replace(RegExp.$1, (RegExp.$1.length==1) ? (o[k]) : (("00"+ o[k]).substr((""+ o[k]).length))); 
  return fmt; 
}
//字符串转日期
function getDate(strDate) {    
    var date = eval('new Date(' + strDate.replace(/\d+(?=-[^-]+$)/,    
     function (a) { return parseInt(a, 10) - 1; }).match(/\d+/g) + ')');    
    return date;    
}
$(function(){
	var viewWidth = $(window).width();
	var viewHeight = $(window).height();
	if(viewWidth < viewHeight){
		$(".art_wrap .content").css("column-count",1);
	}
});
window.onload = function(){
	function getData(){
		var url = "${pageContext.request.contextPath}/api/topic/${topicId}/${articleId}";
		bBtn = false;
		$.ajax({ 
			url: url, 
			dataType:"xml", 
			error: function(xml){ 
				//alert('Error loading XML document'+xml);
				//alert('网络不稳定，请稍后重试。');
			}, 
			success:function(xml){ 
				if(xml != null){
					readXML(xml);
					bBtn = true;
				}
			} 
		}); 
	}//end getDetailDate
	function readXML(xml){
		$(".topnav").prepend($(xml).find("TopicName").text());
		$(".art_wrap h2").html($(xml).find("HeadLine").text());
		//var dateStr = $(xml).find("PublishedDate").text().split("T")[0];	
		var pubtime = $(xml).find("PublishedDate").text();
		var dateStr = "";
		if(pubtime != null && pubtime != ""){
			dateStr = new Date(getDate(pubtime)).format("yyyy-MM-dd");	
		}
		$(".art_wrap span").html($(xml).find("PaperName").text() +"&nbsp;"+ dateStr);
		
		$(xml).find("Images > Image").each(function(index){
			var imgnode = $("<div class='center'><img /></div>");
			imgnode.find("img").attr("src", $(this).find("ImageItem").text());
			$(".content").append(imgnode);
		}); 
		
		var pnode = $("<p>");
		pnode.html($(xml).find("Content").text());
		$(".content").append(pnode);
	}//end readxml
	getData();
}
</script>
</head>

<body><!-- window.history.go(-1); -->
<div class="topnav">
<img src="${pageContext.request.contextPath}/pubtemplet/templet/test1/images/icon_back2.png" onclick="window.history.back();" /></div>
<div class="art_wrap">
 <h2></h2>
 <span></span>
 <div class="content"></div>
</div>
</body>
</html>


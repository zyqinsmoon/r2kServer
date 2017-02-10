<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no">
    <meta name="apple-mobile-web-app-capable" content="yes">
    <meta name="apple-touch-fullscreen" content="yes">
    <title>报纸分类</title>
    <link rel="stylesheet" href="${ctx}/r2k/wx/css/paper.css">
    <script src="${ctx}/r2k/wx/js/jquery-1.10.2.min.js"></script>

    <script>
    	var baseUrl = "/r2k/api/";
    	//http://xuning:8080/r2k/api/paper?place=%E5%8C%97%E4%BA%AC
    	$(function(){
    		addTabChange();
			getData(baseUrl+"place");
		});
		function getData(url){
	      	
			$.ajax({ 
				url: url, 
				dataType:"xml", 
				error: function(xml){ 
					alert('网络不稳定，请稍后重试。');
				}, 
				success:function(xml){ 
					if(xml != null){			
						readXML(xml);
						
					}
				} 
			});
		}
		function readXML(xml){
			$(xml).find("Places > Place").length?showRegionList(xml):showTypeList(xml);
			
		}
		function showRegionList(xml){
			$(xml).find("Places > Place").each(function(index){
				var text = $(this).find("Name").text() +"("+ $(this).find("Count").text()+")";
				var newA = $("<a></a>");
				newA.attr("reg",$(this).find("Name").text());
				newA.attr("href","paperlist.jsp?splace="+$(this).find("Name").text());
				newA.append(text);
				$(".region").append(newA);
			});
		}
		function showTypeList(xml){
			$(".papertype").html("");
			$(xml).find("SubjectCodes > SubjectCode").each(function(index){
				var text = $(this).text();
				var newA = $("<a></a>");
				newA.attr("href","paperlist.jsp?type="+text);
				newA.append(text);
				$(".papertype").append(newA);
			});
		}
		var doone = true;
		function addTabChange(){
			$("#tabTitle li:first").addClass("active");
			$("#tabArea >div").not(":first").hide();
			$("#tabTitle li").bind("click", function(){
				if(doone){
					$(".papertype").html("加载中，请稍后...");
					getData(baseUrl+"subjectCode");
					doone = false;
				}
				$(this).siblings().removeClass("active").end().addClass("active");
				var index = $("#tabTitle li").index( $(this) );
				$("#tabArea >div").eq(index).siblings().hide().end().fadeIn("slow");
		    });
		}
    </script>

 
</head>
<body class="classify">
	<ul id="tabTitle">
	   <li>按地区分类</li>
	   <li>按类型分类</li>
	</ul>
	<div id="tabArea">
		<div class="region">

		</div>
		<div class="papertype" style="display:none">
		   <!-- <a>党报</a> -->
		</div>
		<p style="clear:both;"></p>
	</div>

</body>
</html>
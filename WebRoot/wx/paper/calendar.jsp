<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no">
    <meta name="apple-mobile-web-app-capable" content="yes">
    <meta name="apple-touch-fullscreen" content="yes">
    <title>报纸版面信息</title>
    <link rel="stylesheet" href="${ctx}/r2k/wx/css/paper.css">
    <script src="${ctx}/r2k/wx/js/jquery-1.10.2.min.js"></script>
    <script>
		var paperId = "";
		var startdate = "";
		var enddate = "";
		var bflag = true;
		var arrd = [];
		var viewHeight = $(window).height();
		$(function(){
			if(sessionStorage.spageid){
				sessionStorage.clear();
			}
			var thisurl = window.location.search;
			var arrSrh = thisurl.split("&");

			paperId = arrSrh[0].split("paperId=")[1];
			
			var nowDate = new Date();
			enddate = nowDate.getFullYear()+"-"+appendZero(nowDate.getMonth()+1)+"-"+appendZero(nowDate.getDate());
			var reg = /^(\d{4})-(\d{2})-(\d{2})/;
			arrd = enddate.match(reg);
			startdate = arrd[1]+"-"+appendZero(arrd[2]-1)+"-"+arrd[3];

			var link = "/r2k/api/paper/"+paperId+"?startdate="+startdate+"&from=1&to=100";
			getData(link);

		});

		function getData(url,level,id){
			bflag = false;
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
			function readXML(xml,level){

			   var paperId = $(xml).find("R2k > Paper").attr("id");
			   var periodObj = $(xml).find("Periods > Period");
			   if(!periodObj.length){
				 bflag = false;
				 $(".bottom").html("已无数据");
				 return;
			   }
			   var str = "";
			   periodObj.each(function(index){
				  var periodId = $(this).attr("id");
				  var pDate = $(this).find("PublishedDate").text();
				  var iconLink = $(this).find("Icon").text();
				  ///r2k/wx/paper/page.jsp?page=http://59.108.34.174/r2k/api/period/nq.D110000renmrb_20140612&date=2014-06-12&paperId=n.D110000renmrb
				  
				  var pagelink = "/r2k/wx/paper/pages.jsp?period="+periodId;
				  str += "<li><a href='"+pagelink+"'>"+pDate+"</a></li>";
				  
			   });
			   
			   $(".oldpaper ul").html($(".oldpaper ul").html()+str);
			   $(".bottom").show();
			}
			bflag = true;
		}
		function appendZero(n){
			return ("00"+ n).substr(("00"+ n).length-2);
		}
		window.onscroll = function(){
			var scrollY = document.documentElement.scrollTop || document.body.scrollTop;

			if(viewHeight+scrollY > $(".oldpaper").height()+36 && bflag){
			  enddate = arrd[1]+"-"+appendZero(arrd[2]-1)+"-"+(arrd[3]-1);
			  if(arrd[2]==1){
				 arrd[2] = 12;
				 arrd[1]--;
			  }
			  else{
				arrd[2]--;
			  }

			  startdate = arrd[1]+"-"+appendZero(arrd[2]-1)+"-"+arrd[3];
			  var link = "/r2k/api/paper/"+paperId+"?startdate="+startdate+"&enddate="+enddate+"&from=1&to=100";
			  getData(link);
			}
		};
   </script>

</head>
<body>

<header>
	往期浏览
</header>
<div class="blank"></div>
<div class="oldpaper">
   <ul>
   </ul>
   <div class="bottom" style="display:none;">下拉查看更多</div>
</div>

</body>
</html>
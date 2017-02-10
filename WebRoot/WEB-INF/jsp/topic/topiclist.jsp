<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no">
    <meta name="apple-mobile-web-app-capable" content="yes">
    <meta name="apple-touch-fullscreen" content="yes">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/pubtemplet/templet/test1/css/common.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/pubtemplet/templet/test1/css/stream.css">
    

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

window.onload = function(){
	var pagesize = 12, words = 100;
	var from = 1,to = 12;
	var small = 2, big = 3;		//small代表分子， big代表分母
	var page = 0;

	var aUl = document.getElementsByTagName('ul');
	var bBtn = true;
	getData(from, to);
	function getData(from, to){
		var url = "${pageContext.request.contextPath}/api/topics/${topicId}?from="+from+"&to="+to+"&words="+words;
		bBtn = false;
		$.ajax({ 
			url: url, 
			dataType:"xml", 
			error: function(xml){ 
				//alert('网络不稳定，请稍后重试。');
			}, 
			success:function(xml){ 
				if(xml != null){
					readXML(xml);
					bBtn = true;
				}
			} 
		}); 
	}//end getData
	var flag = true;
	function readXML(xml){
		page++;
		//console.log("page:"+page);
		if(flag){
			$(".topnav").prepend($(xml).find("TopicName").text());
			flag = false;
		}
		$(xml).find("Articles > Article").each(function(index){
			var second;
			var linode = $("<li>");
			var imgnode = $("<img>");
			if(page > 1){
				if(index > 5){
					if(index % 2 ==0){
						second = random(40,60,100);
					}else{
						second = random(60,90,100);
					}
				}else{
					if(index % 2 ==0){
						second = random(30,50,100);
					}else{
						second = random(50,70,100);
					}
				}
			}else{
				if(index > 5){
					if(index % 2 ==0){
						second = random(30,50,100);
					}else{
						second = random(50,70,100);
					}
				}else{
					if(index % 2 ==0){
						second = random(10,35,100);
					}else{
						second = random(20,50,100);
					}
				}
			}
			$(imgnode).fadeIn(second);
			var image = $(this).find("Image").text();
			if(image != null && image != ""){
				imgnode.attr("src",image);
				linode.append(imgnode);
			}
			
			var pnode = $("<p>");
			var anode = $("<a>");
			anode.html($(this).find("HeadLine").text());
			var emnode = $("<em>");
			emnode.html($(this).find("PaperName").text());
			var bnode = $("<b>");
			
			var pubtime = $(this).find("PublishedDate").text();
			var dateStr = "";
			if(pubtime != null && pubtime != ""){
				dateStr = new Date(getDate(pubtime)).format("yyyy-MM-dd");	
			}
			bnode.html(dateStr);
			emnode.append(bnode);
			var spannode = $("<span>");
			/*$(spannode).css({opacity:0.2});
			if(page > 1){
				if(index > 5){
					if(index % 2 ==0){
						second = random(70,100,100);
					}else{
						second = random(50,70,100);
					}
				}else{
					if(index % 2 ==0){
						second = random(30,50,100);
					}else{
						second = random(40,75,100);
					}
				}
			}else{
				if(index > 5){
					if(index % 2 ==0){
						second = random(30,50,100);
					}else{
						second = random(40,75,100);
					}
				}else{
					if(index % 2 ==0){
						second = random(10,20,100);
					}else{
						second = random(20,35,100);
					}
				}
			}
			second = random(20,50,100);
			$(spannode).fadeTo(second,1);*/
			spannode.html($(this).find("Content").text()+"...");
			pnode.append(anode).append(emnode).append(spannode);
			linode.append(pnode);	//live	bind
			var articleId = $(this).attr("id");
			$(linode).bind("click", function() {
				window.location = "${pageContext.request.contextPath}/topic/showTopicDetail.do?articleId="+articleId+"&topicId=${topicId}";
			});
			
			/*if(index % 2 ==0){
				$(linode).fadeIn(random(5,20,100));
			}else{
				$(linode).fadeIn(random(20,35,100));
			}*/
			if(index == 0 || index == 1){
				//添加到最短节点
				var minul;
				for(var i=0;i<aUl.length;i++){
					var aLi = aUl[i].getElementsByTagName('li');
					if(i == 0){
						minul = aUl[i];
					}else {
						if(minul.getElementsByTagName('li').length > aLi.length){
							minul = aUl[i];	
						}
					}
				}
				$(minul).append(linode);
			}else{
				var num = (index +1) % 3;
				if(num == 1){
					$("#col1").append(linode);
				}else if(num == 2){
					$("#col2").append(linode);
				}else if(num == 0){
					$("#col3").append(linode);
				}
			}
		}); 
	}//end readxml
	
	//滚动条事件
	window.onscroll = function(){	
		var veiwHeight = document.documentElement.clientHeight;
		var scrollY = document.documentElement.scrollTop || document.body.scrollTop;
		var mintop, col2mintop, col3mintop;
		for(var i=0;i<aUl.length;i++){
			var aLi = aUl[i].getElementsByTagName('li');	
			var lastLi = aLi[aLi.length-1];
			if(lastLi != null){
				var heightval = $(lastLi).offset().top + $(lastLi).height();
				if(i == 0){
					mintop = heightval;
				}else{
					if(mintop > heightval ){
						mintop = heightval;
					}
				}
			}
		}
		if(mintop != null){
//			if(mintop < (veiwHeight + scrollY)*small/big && bBtn){		* small / big
			if( (mintop ) < (veiwHeight + scrollY) && bBtn){	
				from += pagesize;
				to += pagesize;
				//alert("from = " + from + ", to = " + to);
				getData(from, to);
			}
		}
	};	//end window.onscroll
	
	function random(start,end,size){
		var num = Math.random()*(end-start)+start;
		var second = num*size;
		return second;
	}
	$(function(){
		var w1 =$(".container").width();
		var w2 =$(".container ul").css("margin-left").split("px")[0];
		var w3 =$(".container ul").width();
		
		var _listTwo = (w3+w2*2)*3;
		var _listOne = (w3+w2*2)*2;
		
		if(_listOne > w1){
			//一列
			$(".container ul").css("width",(w1-w2*2));
		}
		else if(_listTwo > w1){
			//两列
			$(".container ul").css("width",(w1-w2*6)/2);
		}
	});
};

</script>
</head>

<body><!-- window.history.go(-1); window.history.back();-->
<div class="topnav"><img src="${pageContext.request.contextPath}/pubtemplet/templet/test1/images/icon_back2.png" onclick="window.location='${pageContext.request.contextPath}/topic/showTopicIndex.do';" /></div>
<div id="div1" class="container">
	<ul id="col1"></ul>
	<ul id="col2"></ul>
	<ul id="col3"></ul>
</div>
</body>
</html>


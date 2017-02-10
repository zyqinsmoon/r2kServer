<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no">
    <meta name="apple-mobile-web-app-capable" content="yes">
    <meta name="apple-touch-fullscreen" content="yes">
    <title>报纸列表</title>
    <link rel="stylesheet" href="${ctx}/r2k/wx/css/paper.css">
    <script src="${ctx}/r2k/wx/js/jquery-1.10.2.min.js"></script>
    <script src="${ctx}/r2k/wx/js/jquery.cookie.js"></script> 
    <script src="${ctx}/r2k/wx/js/zepto.js"></script>
    
    
    <script>

	  $.cookie("orgid","swhy",{path:"/"});
	  /*if(!$.cookie("orgid")){
       	  $.cookie("orgid","swhy",{path:"/"});
      }*/
      //var pageUrl = "pages.jsp";
      var url = "/r2k/api/paper";
      var viewHeight = $(window).height();
      var nlist = 1;
      var bflag = true;
      var doone = true;
      var strSeach, strQuery,strType;
      if(sessionStorage.pageid){
	      sessionStorage.clear();
	  }
      $(function(){
        //分析url的search
        createLoad();
      	strSeach = getQueryString("splace");
      	strQuery = getQueryString("query");
      	strType = getQueryString("type");
        if(strSeach){
        	var surl = url +"?place="+strSeach+"&from="+1+"&to="+12;
        }
        else if(strQuery){
        	var surl = url +"?q="+strQuery+"&from="+1+"&to="+12;
        }
        else if(strType){
        	var surl = url +"?subjectCode="+strType+"&from="+1+"&to="+12;
        }
        else{
        	var surl = url +"?from="+1+"&to="+12;
        }
        
        
        
        getData(surl);
        $("header .search").click(function(){
        	$("#seaBar").toggle();
        });
       
        $("nav").delegate("input", "focus", function(){
        	$(this).attr("value","");
			$(this).removeClass();
			$(window).keydown(function(event){
				//alert(event.keyCode)
				if(event.keyCode == 13){
					openSearch();
				}
			});
		});
		$("nav #searchBtn").click(function(){
        	//$("nav input").blur();
        	openSearch();
        });
        
      });
      function getQueryString(name){
		  var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
		  var r = location.search.substr(1).match(reg);
		  if (r!=null) return (r[2]);
		  return null;
	  }
      function getData(url){
      	bflag = false;
		$.ajax({
			url: url,
			dataType:"xml", 
			error: function(xml){ 
				alert('网络不稳定，请稍后重试。'+url);
			}, 
			success:function(xml){ 
				if(xml != null){			
					readXML(xml);
					bflag = true;
				}
			} 
		}); 
		function readXML(xml){
			if(doone && strSeach || doone && strQuery || doone && strType){
				var _count = $(xml).find("R2k").attr("total");
			    var divResult = $("<div class='se_result'></div>");
			    if(_count == undefined){
			    	_count = 0;
			    }
			    //var _strWord = strSeach? decodeURI(strSeach):decodeURI(strQuery);
			    if(strSeach){
		        	var _strWord = decodeURI(strSeach);
		        }
		        else if(strQuery){
		        	var _strWord = decodeURI(strQuery);
		        }
		        else if(strType){
		        	var _strWord = decodeURI(strType);
		        }
			    
			    var _sRes = "检索到<em>"+_strWord+"</em>共<em>"+_count+"</em>份报纸";
				divResult.append(_sRes);
				$(".paperlist").prepend(divResult);
				doone = false;
			}
		
			$(xml).find("Papers > Paper").each(function(index){
			  //获取展示信息
			  var paperId = $(this).attr("id");
			  var paperName = $(this).find(" MetaInfo > PaperName").text();
			  var strImg = $(this).find(" Periods > Period > Icon").text();
			  var strLink = $(this).find(" Periods > Period >Link").text();
			  var pubDate = $(this).find(" Periods > Period> PublishedDate").text();
			  var periodId = $(this).find(" Periods > Period").attr("id");
			  var reg = /^\d{4}-\d{2}-\d{2}/g;
			  pubDate = pubDate.match(reg);
			  //添加dom 
			  var oLi = $("<li><div><a><img src='' /></a><span></span></div></li>");
			  //oLi.find("a").attr("href",pageUrl+"?page="+strLink);
			  oLi.find("img").attr("src",strImg);
			  //oLi.find("span").html(paperName+"</br>"+pubDate);
			  oLi.find("span").html(pubDate);

			  
			  $(".paperlist ul").append(oLi);
			  var awidth = oLi.find("img").width();
			  oLi.find("a").css("height",parseInt(awidth*.6));
		      oLi.find("a").bind('click', function(e) {
		         //window.location.href = "/r2k/wx/paper/pages.jsp?page="+strLink+"&date="+pubDate+"&paperId="+paperId;
		         window.location.href = "/r2k/wx/paper/pages.jsp?period="+periodId;
		      });
			});
			clearLoad();
		}
	  }
	  function createLoad(){
			if($("#loadFld").length == 0){
		    	var oLoad = $("<div id='loadFld'></div>");
				$("body").append(oLoad);
		    }
	  }
	  function clearLoad(){
			if($("#loadFld").length){
				$("#loadFld").remove();
			}
	  }
	  function openSearch(){
	  	  var text = $("nav input")[0].value;
	  	  //location.href = "paperlist.jsp?query="+text;
	  	  window.open("paperlist.jsp?query="+text);
	  }
	  window.onscroll = function(){
		var scrollY = document.documentElement.scrollTop || document.body.scrollTop;
		
		if(nlist>400){ bflag = false;}//目前限制120份报纸
		if(bflag && (viewHeight+scrollY > $(".paperlist").height()-30)){
		    nlist+=12;
		    if(strSeach){
	        	//http://xuning:8080/r2k/api/paper?place=%E5%8C%97%E4%BA%AC  +"&from="+1+"&to="+12
	        	var surl = url +"?place="+strSeach+"&from="+nlist+"&to="+(nlist+11);
	        }
	        else if(strQuery){
	        	var surl = url +"?q="+strQuery+"&from="+nlist+"&to="+(nlist+11);
	        }
	        else if(strType){
	        	var surl = url +"?subjectCode="+strType+"&from="+nlist+"&to="+(nlist+11);
	        }
	        else{
	        	var surl = url +"?from="+nlist+"&to="+(nlist+11);
	        }
		    getData(surl); 
		}
	  };
    </script>

 
</head>
<body class="listbody">

<header>
	<div>
		<a href="classify.jsp" class="catalog"></a>
		数字报纸
		<a class="search"></a>
	</div>
</header>
<div class="blank"></div>

<nav id="seaBar" style="display:none">
	<form>
		<span><input type="search" value="请输入报纸名称" class="init" /></span>
		<!-- <a class="cancel">取消</a> -->
		<a href="javascript:" class="cancel" id="searchBtn">检索</a>
	</form>
</nav>

<div class="paperlist">
  <ul>
  </ul>
</div>

</body>
</html>
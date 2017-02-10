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
    <script src="${ctx}/r2k/wx/js/jquery.cookie.js"></script>
    <script>
      var pageId = 0;
      var articleLink = "article.jsp";
      var viewWidth = $(window).width();
      var imgHeight = parseInt(viewWidth*550/350);
      var noFirst = false;
	  
	  if(sessionStorage.pageid){
	      pageId = Number(sessionStorage.pageid);
	  }
      var arrLink =[];
      function appendZero(n){return ("00"+ n).substr(("00"+ n).length-2);}
      $(function(){
      	createLoad();
      	 $.cookie("orgid","swhy",{path:"/"});
   	  /*if(!$.cookie("orgid")){
          	  $.cookie("orgid","swhy",{path:"/"});
         }*/

        var thisurl = window.location.search;
        var arrSrh = "";
        arrSrh = thisurl.split("&");
        
        //if(arrSrh.length == 3){
       	var pageurl = arrSrh[0].split("?page=")[1];
      	getData(getSubUrl(pageurl));
       	var paperId = arrSrh[2].split("paperId=")[1];
       	var date = arrSrh[1].split("date=")[1];
      	var linkurl = "calendar.jsp?paperId="+paperId+"&date="+date;	
		$(".calendar").attr("href",linkurl);
        //}
        if(getQueryString("pId")){
        	noFirst = true;
        	pageId = Number(getQueryString("pId"));
        	$("#pageBox>div").html('<div id="swipe"><div class="pageimg"></div></div>');
        }
        var obj = $(".paperpage > div");
        
        obj.css({"width":viewWidth*10});
        obj.find(">div").attr("id","swipe"+pageId);
        obj.find(">div").css({"width":viewWidth});
      	obj.find(">div").find(".pageimg").css({"width":viewWidth,"height":imgHeight});
      	
        $(".pageicon").click(function(){
        	$("#pageBox").toggle();
        	$(".pagebmlist").toggle();
        	
        });
        
        
        doSwipeRead("pageBox");

      });
      //去除域名
      function getSubUrl(url){
          var pos = url.indexOf("/r2k/");
	      url = url.substring(pos);
      	  return url;
      }
      function getQueryString(name){
			var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
			var r = location.search.substr(1).match(reg);
			if (r!=null) return unescape(r[2]);
			return null;
		}
      function getData(url,level,id){
		$.ajax({ 
			url: url, 
			dataType:"xml", 
			error: function(xml){ 
				alert('网络不稳定，请稍后重试。'+xml);
			},
			success:function(xml){ 
				if(xml != null){
				   //level?readXML(xml,level,id):readXML(xml);
				   readXML(xml,level,id);
				}
			} 
		}); 
		function readXML(xml,level,id){
		  if(!level){
		  	  var pageObj = $(xml).find("Pages > Page");
		  	  if(pageObj.length == 0){
		  	  	clearLoad();
		  	  	alert("数据有误");
		  	  	return;
		  	  }
		  	  
		  	  pageObj.each(function(index){
		  	  	 var sLink = $(this).find("Link").text();
		  	  	 arrLink.push(sLink);
		  	  	 
		  	  	 var _pNum = $(this).find("PageNumber").text();
			  	 var _pName = $(this).find("PageName").text();
		  	  	 
		  	  	 if(location.href.indexOf("&pId=") == -1){
		  	  	 	var _cUrl = location.href+"&pId="+index;
		  	  	 }
		  	  	 else{
		  	  	 	var _cUrl = location.href.split("&pId=")[0]+"&pId="+index;
		  	  	 }
		  	  	 var _oPList = $("<li><a></a></li>");
		  	  	 _oPList.find("a").attr("href",_cUrl);
		  	  	 _oPList.find("a").append(_pNum+"版： "+_pName);
		  	  	 $(".pagebmlist ul").append(_oPList);
		  	  });
			  getData(getSubUrl(arrLink[pageId]),1,pageId);
			 
		  }
		  else{   
			  //预加载下一版
			  if(level == 1){
			  	  if(pageId == arrLink.length-1){
			  	  	  createNewDom(pageId-1,"pre");
				  	  getData(getSubUrl(arrLink[pageId-1]),3,pageId-1);
			  	  }
			  	  else{
			  	  	  createNewDom(pageId+1,"next");
				  	  getData(getSubUrl(arrLink[pageId+1]),2,pageId+1);
			  	  }
			  	  
			  }
			  else if(level == 3){
			  	  var oSwipePage = document.getElementById("pageBox").children[0];
			  	  translate(oSwipePage,-viewWidth,0);
			  }
				    
			  //版面图  
			  var objdom = $(".paperpage > div");
			  //var strImg = $(xml).find("Page > RealImage").text();
			  var strImg = $(xml).find("Page > BriefImage").text();
			  if(!$.trim(strImg)){
			  	strImg = $(xml).find("Page > BriefImage").text();
			  }
			  var oImg = $("<img src='' />");
			  oImg.attr("src",strImg);
			  oImg.attr("usemap","#articles"+id);
			  oImg.css({"width":viewWidth,"height":imgHeight});
			  objdom.find("#swipe"+id).find(".pageimg").css({"width":viewWidth,"height":imgHeight});
			  objdom.find("#swipe"+id).find(".pageimg").append(oImg);
			  oImg[0].onload = function(){
			  	  imgHeight = oImg.height();
				  
				  // 创建热区
			      var oMap = $('<map></map>');
			      oMap.attr("name","articles"+id);
			      objdom.find("#swipe"+id).append(oMap);
			      
			      //文章列表
				  var oArticle = $(xml).find("Articles > Article");
				  var oBox = $("<div class='listname'></div>");
			  	  var strb = "<p>本版文章</p><ul>";
			  	  oArticle.each(function(index){
			  	      //添加articlelist
				      var title = $(this).find("HeadLine").text();
			  	      var sLink = $(this).find("Link").text();
					  if($.trim(title)){
						  var strLi = "<li><a href='"+ articleLink+"?article="+sLink +"'>"+"◎"+title+"</a></li>";
			  	  	      strb+=strLi;
					  }
			  	  	  
	
			  	      //添加map 
				  	  var strCorr = $(this).find("Region").text();
				  	  var arrCorr = strCorr.split("#");
				  	  var corrds = ""; 
				  	  $.each(arrCorr, function(i,n){ 
				  	  	 var corPair = arrCorr[i].split(",");
				  	  	 var x = parseInt(corPair[0])*viewWidth/100;
				  	  	 var y = parseInt(corPair[1])*imgHeight/100;
				  	  	 i == arrCorr.length-1?corrds += x+","+y:corrds += x+","+y+",";
				  	  });
				  	  var objArea = $('<area shape="poly" />');
				      objArea.attr("coords",corrds);
				      objArea.attr("href",articleLink+"?article="+sLink);
				      oMap.append(objArea);  
				  });
				  
				  oBox.html(strb+"</ul>"); 
				  objdom.find("#swipe"+id).append(oBox);
				  var nowHeight = oBox.height()+imgHeight;
			  	  objdom.find("#swipe"+id).css({"height":nowHeight});
			  	  
			  };
			  clearLoad();
		  }
		}
	}
	function createLoad(obj){
		if($("#loadFld").length == 0){
	    	var oLoad = $("<div id='loadFld'></div>");
	    	if(obj){
	    		obj.append(oLoad);
	    	}
	    	else{
	    		$("body").append(oLoad);
	    	}
			
	    }
	}
	function clearLoad(){
		if($("#loadFld").length){
			$("#loadFld").remove();
		}
	}
    var isScrolling;
   
    function doSwipeRead(id){
    	var oSwipePage = document.getElementById(id).children[0];
    	oSwipePage.addEventListener('touchstart',touch, false);
	    oSwipePage.addEventListener('touchmove',touch, false);
	    oSwipePage.addEventListener('touchend',touch, false);

	    //var direction;
		var start = {};
		var delta = {};
		var doone;
		var flg = true;
    	function touch (event){
	        var event = event || window.event;
			event.stopPropagation();
	        switch(event.type){
	            case "touchstart":
	                 isScrolling = undefined;
					 var touches = event.touches[0];
					 start = {
						x: touches.pageX,
						y: touches.pageY,
						time: +new Date
					 };
					 delta = {
						x: 0,
						y: 0
					  };
					 doone = true;
	                 break;
	            case "touchmove":
	              
	                  if (event.touches.length > 1 || event.scale && event.scale !== 1){ break;}
	                  var touches = event.touches[0];
					  delta = {
						x: touches.pageX - start.x,
						y: touches.pageY - start.y
					  };
		
					  if ( typeof isScrolling == 'undefined') {
						isScrolling = !!( isScrolling || Math.abs(delta.x) < Math.abs(delta.y) );
					  }
					  var len = oSwipePage.children.length;
					  var direction = delta.x < 0;
					  if (!isScrolling) {
					  
						  if(doone){
						  
						      event.preventDefault();
						      //$(".state").html($(".state").html()+"</br>"+start.x+" , "+touches.pageX);
						  	  if(direction){
								  if(pageId >= arrLink.length-1){
								  	  break;
								  }
								  while(len > 3){
									  $(oSwipePage.children[0]).remove();
								  }
							  }
							  else{
							     if(pageId <= 0){
							     	break;
							     }
							     
						    	 
						    	 if(len == 2){
						    	 
						    	 	 if(pageId == arrLink.length-1){
							    	 	 createNewDom(pageId-2,"pre");
								    	 getData(getSubUrl(arrLink[pageId-2]),2,pageId-2);
							    	 }
							    	 else{
							    	 	 createNewDom(pageId-1,"pre");
								    	 getData(getSubUrl(arrLink[pageId-1]),2,pageId-1);
							    	 }
							    	 $(oSwipePage).children().first().css("position","relative");
							    	 createLoad($(oSwipePage).children().first());
						    	 }
							  }
							  doone = false;
						  }


						 if(direction){
						    if(oSwipePage.children.length==2){
						        translate(oSwipePage,delta.x,0);
						    }
						    else if(oSwipePage.children.length==3){
						    	translate(oSwipePage,delta.x-viewWidth,0);
						    }
							 
						 }
						 else{
						    
						    if(pageId == arrLink.length-1){
					    		translate(oSwipePage,delta.x-2*viewWidth,0);
					        	break;
					        }
					    	translate(oSwipePage,delta.x-viewWidth,0);
							
						 }
					  }	
	                
	                 break;
	            case "touchend":
	                  var duration = +new Date - start.time;
					  var isValidSlide = Number(duration) < 250 && Math.abs(delta.x) > 40 || Math.abs(delta.x) > viewWidth/3;
					  if (!isScrolling) {
					  	  var len = oSwipePage.children.length;
						  if (isValidSlide) {
							  var direction = delta.x < 0;
							  
							  if (direction){
							    	//下一页
								    pageId++;

								    if(pageId > arrLink.length-1){
								       pageId = arrLink.length-1;
								       showPrompt("已经是最后一版");
									   break;
									}
									
									
								    if(len == 2){
								    	translate(oSwipePage,-viewWidth,300);
								        if(pageId == arrLink.length-1){
								        	break;
								        }
								       
							        	createNewDom(pageId+1,"next");
										getData(getSubUrl(arrLink[pageId+1]),2,pageId+1);
								        
								    }
								    else if(len == 3){
								    	translate(oSwipePage,-2*viewWidth,300);
								    	
								    		
								    	setTimeout(function(){
								    		if(pageId < arrLink.length-1){
								    			createNewDom(pageId+1,"next");
								    			translate(oSwipePage,-viewWidth,0);
										    	$(oSwipePage.children[0]).remove();
										    	getData(getSubUrl(arrLink[pageId+1]),2,pageId+1);
								    		}
								    		else{
								    			translate(oSwipePage,-viewWidth,0);
								    			$(oSwipePage.children[0]).remove();
								    		}
								    		
								    		
								    	},300);
								    }

							  }
							  else{
								    //上一页
								    if(pageId <= 0){
								    	showPrompt("已经是第一版");
								        break;
								    }
								    pageId--;
								    
								    
								    if(pageId == arrLink.length-2){
							    		translate(oSwipePage,-viewWidth,300);
							        }
							        else{
							        	translate(oSwipePage,0,300);
							        }
							    	
	
							    	setTimeout(function(){
	
							    		if(pageId == arrLink.length-2){
							    			
							    			getData(getSubUrl(arrLink[pageId-2]),2,pageId-2);
							    		}
							    		else{
							    			$(oSwipePage.children[len-1]).remove();
							    			createNewDom(pageId-1,"pre");
							    			translate(oSwipePage,-viewWidth,0);
							    			getData(getSubUrl(arrLink[pageId-1]),2,pageId-1);
							    			
							    		}
							    	    
	   
							    	},300);
	
								  }
						   }
						   else{

							  if(len == 2){
							  	  if(pageId == arrLink.length-1){
							  	  	  break;
							  	  }
							  	  else{
							  	  	  translate(oSwipePage,0,300);
							  	  }
						    	  
						      }
						      else{
						    	  translate(oSwipePage,-viewWidth,300);
						      }
						   }
					   }
					   sessionStorage.pageid = pageId;
					   oSwipePage.removeEventListener('touchmove', event, false);
		     		   oSwipePage.removeEventListener('touchend', event, false);
	                   break;
	            }
         
           }

     }
     function showPrompt(txt){
     	$(".prompt").html(txt);
        $(".prompt").fadeIn(100);
        setTimeout(function(){
       	   $(".prompt").fadeOut(100);
        },1000);
     }
     function createNewDom(id,add){
     
    	 var oNext = $("<div><div class='pageimg'></div></div>");
    	 oNext.attr("id","swipe"+id);
	  	 
	  	 if(add == "next"){
	  	 	oNext.css({"width":viewWidth});
	  	 	$(".paperpage > div").append(oNext);
	  	 }
	  	 else{
	  	 	oNext.css({"width":viewWidth});
	  	 	$(".paperpage > div").prepend(oNext);
	  	 }
	  	   

     }
     function translate(obj,dist,speed) {
		var style = obj && obj.style;
		if (!style) return;
		style.webkitTransitionDuration =
		style.MozTransitionDuration =
		style.msTransitionDuration =
		style.OTransitionDuration =
		style.transitionDuration = speed + 'ms';
		style.webkitTransform = 
		style.msTransform =
		style.MozTransform =
		style.OTransform = 'translate3d('+ dist +'px, 0, 0)';
     }
   //{ BASEURL/paper/paperid?startdate=”2013-10-10”&enddate=”2014-10-10”}这个时间段内这份报纸的所有期信息
   //<div class="state" style="position:fixed; top:100px; left:10px; width:200px; height:100px; background:red; border:2px solid #000; z-index:99;"></div>

   </script>

</head>
<body>

<header>
	<a class="pageicon"></a>
	数字报纸
	<a class="calendar"></a>
	
</header>
<div class="blank"></div>
<div class="paperpage" id="pageBox">
  <div>
	  <div id="aa0">
	  	<div class="pageimg"></div>
	  </div>
  </div>
</div>
<div class="pagebmlist" style="display:none;">
   <ul>
   </ul>
</div>
<div class="prompt" style="display:none;"></div>
</body>
</html>
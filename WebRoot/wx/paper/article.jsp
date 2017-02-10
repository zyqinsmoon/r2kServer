<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no">
    <meta name="apple-mobile-web-app-capable" content="yes">
    <meta name="apple-touch-fullscreen" content="yes">
    <title>文章</title>
    <link rel="stylesheet" href="${ctx}/r2k/wx/css/paper.css">
    <script src="${ctx}/r2k/wx/js/jquery-1.10.2.min.js"></script>
    <script src="${ctx}/r2k/wx/js/jquery.cookie.js"></script>
    <script src="${ctx}/r2k/wx/js/wextend.js"></script>
    <script>
      var artId = 0;
      var artLink = "article.jsp";
      var viewWidth = $(window).width();
      var viewHeight = $(window).height();
      var flag = true;
      var arrLink = [];
      var baseurl;
      
      $(function(){
      
    	  $.cookie("orgid","swhy",{path:"/"});
    	  /*if(!$.cookie("orgid")){
           	  $.cookie("orgid","swhy",{path:"/"});
          }*/
        
        var thisurl = window.location.search;
        createNewDom(0);
        translate($(".article > div")[0],-viewWidth,0);
        if(thisurl.split("?article=")[1]){
        	baseurl = thisurl.split("?article=")[1];
	      	getData(getSubUrl(baseurl),false,0);	
        }
        
      });
      //去除域名
      function getSubUrl(url){
          var pos = url.indexOf("/r2k/");
	      url = url.substring(pos);
      	  return url;
      }
      function getData(url,level,id){
		$.ajax({ 
			url: url, 
			dataType:"xml", 
			error: function(xml){ 
				//alert('网络不稳定，请稍后重试。');
			}, 
			success:function(xml){ 
				if(xml != null){
				   readXML(xml,level,id);
				}
			} 
		}); 
		function readXML(xml,level,id){
		  if(!level){
		  
		    var artObj = $(xml).find("Article");
			var paperName = artObj.find("MetaInfo > Paper> PaperName ").text();
			var pubDate = artObj.find("MetaInfo > Period > PublishedDate ").text();
			
			
			var reg = /^\d{4}-\d{2}-\d{2}/g;
			pubDate = pubDate.match(reg);
			
			var title = artObj.find("HeadLine").text();
			var content = artObj.find("Content").text();
			var oArtImg = artObj.find("Images > Image");
			if(!$.trim(title)){
			    title = "无标题";
			}
			var strTitle = "<h2>"+title+"</h2><div class='subinfo'>"+pubDate+"　　来源："+paperName+"</div>";
			var strContent = "<div class='content' style='display:none'><p>"+content+"</p></div>";
			var imgBox = $("<div class='imgfld'></div>");
			oArtImg.each(function(i){
				var imgLink = $(this).find("ImageItem").text();
				var imgCont = $(this).find("ImageContent").text();			
				if($.trim(imgLink)){
					var _imgObj = $("<img />");					
					_imgObj.attr("src",$.trim(imgLink));
					if($.trim(imgCont)){
						var _comment = "<p>"+imgCont+"</p>";					
						imgBox.append(_imgObj).append(_comment);
					}
					else{
						imgBox.append(_imgObj);
					}
					
				}
			});

			$(".article").find("#swipe"+id).append(strTitle + strContent);
			//$(".article").find("#swipe"+id).find(".content").prepend(imgBox);
			

			$(".article").find("#swipe"+id).find(".content").intopend("prepend",imgBox,function(){
				var $obj = $(".article").find("#swipe"+artId);
				$obj.find(".content").show();
				var height = $obj.find(".content").height()+$obj.find("h2").height()+60;
				if(height<viewHeight){
				    height = viewHeight;
				}
				
				$(".article >div").css({"height":height});
				$(".article").css({"height":height});
				clearLoad();
			});
			var listurl = artObj.find("Page").text();
			if(flag){
				getData(getSubUrl(listurl),true,id);
				flag = false;
			}
		  }
		  else{
		    
		    var oArticle = $(xml).find("Articles>Article");
		    oArticle.each(function(i){
		    	arrLink.push($(this).find("Link").text());
		    	if(baseurl==$(this).find("Link").text()){
				    artId = i;
				    $(".article > div")[0].id = "swipe"+i;
				}
		    });
		    
		    doSwipeRead($(".article")[0]);
		  }
		}
	}
    function getArtList(obj){
    	var PageNumber = obj.find("PageNumber").text();
    	var PageName = obj.find("PageName").text();
    	var pLink = obj.find("Link").text();
    	//获取该版次的文章列表
    	getData(getSubUrl(pLink),2);
    }
    function showPrompt(txt){
     	if(!$(".prompt").length){
     		var oPrompt = $('<div class="prompt" style="display:none;"></div>');
     	}
     	else{
     		var oPrompt = $(".prompt");
     	}
     	oPrompt.html(txt);
     	$("body").append(oPrompt);
        oPrompt.fadeIn(100);
        setTimeout(function(){
       	   oPrompt.fadeOut(100);
        },1000);
    }
    function doSwipeRead(oSwipePage){
    	oSwipePage.addEventListener('touchstart',touch, false);
	    oSwipePage.addEventListener('touchmove',touch, false);
	    oSwipePage.addEventListener('touchend',touch, false);

	    var direction;
		var start = {};
		var delta = {};
		var doone;
		var stop;
    	function touch (event){
	        var event = event || window.event;
			event.stopPropagation();
			if(arrLink.length==0) {return;}
			//alert(artId);
	        switch(event.type){
	            case "touchstart":
	                 isScrolling = undefined;
					 var touches = event.touches[0];
					 start = {
						x: touches.pageX,
						y: touches.pageY,
						time: +new Date
					 };
					 doone = true;
					 stop = false;
					 oSwipePage.addEventListener('touchmove',touch, false);
	    			 oSwipePage.addEventListener('touchend',touch, false);
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
					  
					  direction = delta.x < 0;
					  if (!isScrolling) {
						  if(doone){
						   event.preventDefault();
						  	  if(direction){
						  	   //$(".state").html(artId+","+ arrLink.length);
								  if(artId >= arrLink.length-1) {stop = true;break;}
								  artId++;
								  if(!$("#swipe"+artId).length){
								  	  createNewDom(artId,"next");
									  getData(getSubUrl(arrLink[artId]),false,artId); 
								  }
							  }
							  else{
							     if(artId < 1) {stop = true;break;}
							     artId--;
							         if(!$("#swipe"+artId).length){
							         	createNewDom(artId,"pre");
							     	    getData(getSubUrl(arrLink[artId]),false,artId);
							         }
							    
							    
							  }
							  doone = false;
						  }
					  
					    
					  
		       			 objArr = oSwipePage.children;
						 if(direction){
						 
							 translate(objArr[0],delta.x-viewWidth,0);
							 translate(objArr[1],delta.x,0);
						 }
						 else{
						 	translate(objArr[0],delta.x-2*viewWidth,0);
							translate(objArr[1],delta.x-viewWidth,0);
						 }
					  }	
	                
	                 break;
	            case "touchend":
	                  
	                  var duration = +new Date - start.time;
					  var isValidSlide =  Number(duration) < 250 || Math.abs(delta.x) > 50 || Math.abs(delta.x) > viewWidth/4;
					  if (!isScrolling) {
						  if (isValidSlide) { 
							  direction = delta.x < 0;
							  if (direction){
							    //下一页 
							    if(stop){
							    	showPrompt("已经是最后一篇");
							    	break;
							    }
						        translate(objArr[0],-viewWidth*2,300);
							 	translate(objArr[1],-viewWidth,300);
							 	setTimeout(function(){
							 	    var len = $(".article >div").length;
							 	    
							 	    while(len > 1){
							 	      $(".article >div").first().remove();
							 	      len = $(".article >div").length;
							 	    }
							 	    
							 	},300);
							 	
							  }
							  else{
							    //上一页 
							    if(stop){
							    	showPrompt("已经是第一篇");
							    	break;
							    }
							    translate(objArr[0],-viewWidth,300);
							 	translate(objArr[1],0,300);
							 	setTimeout(function(){
							 	   if($(".article >div").length>1){
							 		  //objArr[1].remove();
							 		  var len = $(".article >div").length;
							 		  while(len > 1){
								 	      $(".article >div").last().remove();
								 	      len = $(".article >div").length;
								 	  }
							 		}
							 	},300);
							  	
							  }	  
						   }
						   else{
						   	  if (direction){
							    //下一页 
							    //alert(pageId);
						        translate(objArr[artId-1],-viewWidth,300);
							 	translate(objArr[artId],0,300);
							 	
							  }
							  else{
							    //上一页 
							    if(pageId<0){
							    	break;
							    }
							    translate(objArr[artId],-viewWidth*2,300);
							 	translate(objArr[artId+1],-viewWidth,300);
							  	
							  }
						   }
					   }

					   oSwipePage.removeEventListener('touchmove', touch, false);
		     		   oSwipePage.removeEventListener('touchend', touch, false);
	                   break;
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
    function createNewDom(id,add){    
    	 var oNext = $("<div></div>");
    	 oNext.attr("id","swipe"+id);
	  	 oNext.css({"left":viewWidth});
	  	 createLoad(oNext);
	  	 if(add == "next")
	  	 	$(".article").append(oNext);
	  	 else
	  	   $(".article").prepend(oNext);

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
     //<div class="state" style="position:fixed; top:100px; left:10px; width:200px; height:100px; background:red; border:2px solid #000; z-index:99;"></div>
     
    </script>

 <style>
 .article >div{ position: absolute; top:0; width:100%; height:100%; background:#fff; overflow:hidden; }
 
 </style>
</head>
<body>
<div class="article">

</div>
</body>
</html>
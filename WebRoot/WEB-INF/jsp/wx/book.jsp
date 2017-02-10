<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no">
    <meta name="apple-mobile-web-app-capable" content="yes">
    <meta name="apple-touch-fullscreen" content="yes">
    <link rel="stylesheet" href="${ctx}/r2k/wx/css/base.css">
    <link rel="stylesheet" href="${ctx}/r2k/wx/css/jslider.css" type="text/css">
    <script src="${ctx}/r2k/wx/js/jquery-1.10.2.min.js"></script>
    <script src="${ctx}/r2k/wx/js/jquery.cookie.js"></script>
    <script src="${ctx}/r2k/wx/js/wx.js"></script>
    <script src="${ctx}/r2k/wx/js/zepto.js"></script>
    <script src="${ctx}/r2k/wx/js/mobile.js"></script>
    
  <!-- end -->
	<script type="text/javascript" src="${ctx}/r2k/wx/js/jshashtable-2.1_src.js"></script>
	<script type="text/javascript" src="${ctx}/r2k/wx/js/jquery.numberformatter-1.2.3.js"></script>
	<script type="text/javascript" src="${ctx}/r2k/wx/js/tmpl.js"></script>
	<script type="text/javascript" src="${ctx}/r2k/wx/js/jquery.dependClass-0.1.js"></script>
	<script type="text/javascript" src="${ctx}/r2k/wx/js/draggable-0.1.js"></script>
	<script type="text/javascript" src="${ctx}/r2k/wx/js/jquery.slider.js"></script>
  <!-- end -->
	<style type="text/css" media="screen">
	 .layout { float:left; width:50%; padding:10px 0 0 2%; font-family: Georgia, serif; }
	 .jslider{top:0.8em;}
	 .layout-slider { margin-bottom: 60px;}
	 .layout-slider-settings { font-size: 12px; padding-bottom: 10px; }
	 .layout-slider-settings pre { font-family: Courier; }
	 .jslider .jslider-pointer { width: 24px; height: 24px; position: absolute; left: 20%; top: -11px; margin-left: -6px; background:#fff; border:1px solid #ccc; border-radius:50px}
	 .jslider .jslider-bg .v{ background:#d46c2f;}
	 #readFld{ height:100%; width:300000px;}
	 #readFld a{ position:absolute; left:0}
	 
	</style>
<title><s:property value="bookName" escape="false"/></title>
<style>

</style>
 <script>
     var pageid=<s:property value="pageid" escape="false"/>;
     var metaid="<s:property value='metaid' escape='false'/>";
     var firstId = pageid;
	 $(function(){
		var viewWidth = $(document).width();
		var viewHeight = $(document).height();
		var viewImgWidth = viewWidth*2;
		var viewImgHeight = viewHeight*2;
		
		$("#readFld a").css({"width":viewWidth,"height":viewHeight});
		$("#readFld a").attr("id","bookImg"+pageid);
		$("#readFld img").css({"width":viewWidth,"height":viewHeight});
		$("#readFld img").attr("src",'<s:property value="onlineurl" escape="false"/>&pageid='+pageid+'&width='+viewImgWidth+'&height='+viewImgHeight);
	    if($.cookie('apabi_wx_cookie_online')){
           $.cookie("apabi_wx_cookie_online","_01",{path:"/",expires:1});
        }
        else{
            var oBlackDiv = $("<div class='blackdiv'></div>");
			$("body").append(oBlackDiv);
			setTimeout(function(){oBlackDiv.remove();},2000);

			oBlackDiv.click(function(){
				$(this).remove();		
			});
			$.cookie("apabi_wx_cookie_online","_01",{path:"/",expires:1});
        }
        $("#SliderSingle").data( "jslider" ).o.pointers[0].set(pageid);
		var preUrl = '<s:property value="onlineurl" escape="false"/>&width='+viewImgWidth+'&height='+viewImgHeight;
		var nextUrl = '<s:property value="onlineurl" escape="false"/>&width='+viewImgWidth+'&height='+viewImgHeight;
		var loadImg = new Image();
		var isScrolling;
		var objArr;
		var oImgFld = document.getElementById("viewBox");
		var direction;
		oImgFld.ontouchstart=function (ev)
		{
		      var start = {};
			  var delta = {};
			  var touches = event.touches[0];
			  start = {
				x: touches.pageX,
				y: touches.pageY,
				time: +new Date
			  };
			  var doone = true;
			   
			  oImgFld.ontouchmove = function(){
				  var touches = event.touches[0];
				  delta = {
					x: touches.pageX - start.x,
					y: touches.pageY - start.y
				  };

				  if ( typeof isScrolling == 'undefined') {
					isScrolling = !!( isScrolling || Math.abs(delta.x) < Math.abs(delta.y) );
				  } 
				  direction = delta.x < 0; 
				  if(doone){
				  	  if(direction){
						  //next page
						  if(pageid>='<s:property value="PageTotalCount"/>'){
						  	return false;
						  }
						  pageid++;
						  loadNewPage();
					  }
					  else{
					      if(pageid<=1){
					        return false;
					      }
					      pageid--;
					      loadNewPage("pre");
					  }
					  doone = false;
				  }
				  if (!isScrolling) {
					 objArr = this.children[0].children;
	       			 event.preventDefault();
	       			 
	       			 
	       			
	       			 
	       			 var objpre = document.getElementById("bookImg"+(pageid-1));
      		         var objmid = document.getElementById("bookImg"+(pageid));
      			     var objnxt = document.getElementById("bookImg"+(pageid+1));
	       			 
				     if(direction){

				       if(firstId == pageid-1){	
				       	 translate(objpre,delta.x,0);
				       	 translate(objmid,delta.x,0);
				       }
				       else if(firstId<pageid-1){
					     translate(objpre,delta.x-viewWidth,0);
					     translate(objmid,delta.x,0);
				       }
				       else{
					     if(firstId == pageid){
					       	 translate(objpre,delta.x+viewWidth,0);
					     	 translate(objmid,delta.x+viewWidth,0);
					     }
					     else{
					     	//$("#div1").html($("#div1").html()+",kkk,"+pageid+"！"+firstId+"#");
					         translate(objpre,delta.x+viewWidth,0);
					         translate(objmid,delta.x+viewWidth*2,0);
					     } 
				       }
				       
					 }
					 else{	/////////////////上一页	   
					   if(firstId == pageid){
					     
					     translate(objmid,delta.x-viewWidth,0); 
					     translate(objnxt,delta.x-viewWidth,0);     
				       }
				       else if(firstId<pageid){
				      
				         translate(objmid,delta.x-viewWidth*2,0);
				         translate(objnxt,delta.x-viewWidth,0); 
				       }
				       else {
				           
					       if(firstId == pageid+1){
					       	 translate(objmid,delta.x,0);
					       	 translate(objnxt,delta.x,0); 
					       }
					       else{
					         translate(objmid,delta.x,0);
					       	 translate(objnxt,delta.x+viewWidth,0);
					       }
				       	 
				       }
				        
					 }
				  }
			  };
			  
			  oImgFld.ontouchend=function (){
				 var isValidSlide = Math.abs(delta.x) > 20 || Math.abs(delta.x) > viewWidth/2;
				 var isRightClick = start.x >= viewWidth*2/3;
				 var isLeftClick = start.x <= viewWidth/3;
				 
				 
				 if (isValidSlide) { 
				 	  direction = delta.x < 0;
					  if (direction){
					    //下一页 
				        toNextPage(300);
					  }
					  else{
					    //上一页 
					  	toPrePage(300);
					  }
					  $(".bottom").hide();
				 }
				 else{
				 	/*
				 	 if(isLeftClick){
				 	   pageid--;
				 	   loadNewPage();
				 	   toPrePage(300);
				 	   $(".bottom").hide();
				 	 }
				 	 else if(isRightClick){
				 	    pageid++;
				 	    loadNewPage();
				 	 	toNextPage(300);
				 	 	$(".bottom").hide();
				 	 }
				 	 */
				 	 
				 	 if(!isLeftClick && !isRightClick){
				 	 	$(".bottom").toggle();
				 	 	return;
				 	 }			 	 
				 }
				 $("#SliderSingle").data( "jslider" ).o.pointers[0].set(pageid);
			
			  };
			
		};
		// $(".mulu").html(pageid+'/<s:property value="PageTotalCount" escape="false"/>');


		 function loadNewPage(str){	
		 	var surl = nextUrl+"&pageid="+pageid;
		 	if(!$("#bookImg"+pageid).length){
				var nextObj = $("<a><img /></a>");
				nextObj.find("img").attr("src",surl);
				nextObj.attr("id","bookImg"+pageid);
				
				$("#readFld").append(nextObj);
				
				nextObj.css({"width":viewWidth,"height":viewHeight});
				nextObj.find("img").css({"width":viewWidth,"height":viewHeight});
				if(firstId !=1 && str =="pre"){
					nextObj.css({"left":-viewWidth});
				}
				else{
					nextObj.css({"left":viewWidth});
				}
				
				var loadImg = new Image();
				loadImg.src = surl;
				setTimeout(function(){
				     if(!loadImg.complete){
					     var oLoadDiv = $("<div class='loadfld'></div>");
					     nextObj.append(oLoadDiv);
					     loadImg.onload = function(){
						    oLoadDiv.remove();	
					   };
				     }
				},1000);
			
			}		
		 }
		 function toNextPage(speed){
      		if(firstId == pageid-1){
	        	translate(document.getElementById("bookImg"+(pageid-1)),-viewWidth,speed);
		    	translate(document.getElementById("bookImg"+pageid),-viewWidth,speed);
	        }
	        else if(firstId<pageid){
	        	translate(document.getElementById("bookImg"+(pageid-1)),-viewWidth*2,speed);
		    	translate(document.getElementById("bookImg"+pageid),-viewWidth,speed);
	        }
	        else{
	        	if(firstId == pageid){
			       	 translate(document.getElementById("bookImg"+(pageid-1)),0,speed);
			     	 translate(document.getElementById("bookImg"+pageid),0,speed);
			     }
			     else{
			         translate(document.getElementById("bookImg"+(pageid-1)),0,speed);
			     	 translate(document.getElementById("bookImg"+pageid),viewWidth,speed);
			     }
	        
	        }
		 
		 }
		 function toPrePage(speed){
		 	if(firstId == pageid){ 
		      translate(document.getElementById("bookImg"+pageid),0,speed);
	       	  translate(document.getElementById("bookImg"+(pageid+1)),0,speed);  
	        }
	        else if(firstId<pageid){
	          translate(document.getElementById("bookImg"+pageid),-viewWidth,speed);
	          translate(document.getElementById("bookImg"+(pageid+1)),0,speed); 
	        }
	        else{
	          if(firstId == pageid+1){ 
		          translate(document.getElementById("bookImg"+pageid),viewWidth,speed);
		          translate(document.getElementById("bookImg"+(pageid+1)),viewWidth,speed); 
	          }
	          else{
	          	  translate(document.getElementById("bookImg"+pageid),viewWidth,speed);
		          translate(document.getElementById("bookImg"+(pageid+1)),viewWidth*2,speed); 
	          }
	          
	        }
		 }
		 
		 function turnNextPage(){
			if(pageid<'<s:property value="PageTotalCount"/>'){
			   var loadImg = new Image();
			   pageid = pageid+1; 
			   loadImg.src = nextUrl+"&pageid="+pageid;	
			   $("#readFld img").attr("src",loadImg.src);
			   window.setTimeout(function(){
			     if(!loadImg.complete){
				         var oLoadDiv = $("<div class='loadfld'></div>");
				         $("body").append(oLoadDiv);
				         loadImg.onload = function(){
						oLoadDiv.remove();
				   };
			   }
			     },1000);

			}
			else{
			  var _version = getOsVersion();
			  if(_version == "iOS"){
	    		var oReadOverDiv = $("<div class='readover readoverios' id='readoverFld'></div>");
	    	  }
	    	  else{
	    	  	var oReadOverDiv = $("<div class='readover' id='readoverFld'></div>");
	    	  }
			   setTimeout(function(){
			   	  $("body").append(oReadOverDiv);
			   	  
			   	  $(".readover").click(function(){
					 $(this).remove();		
				  });
				  
			   	},500);
			}
			
		 };
		 if(document.getElementById("readoverFld")!=null){
		       document.getElementById("readoverFld").addEventListener( 'touchstart', function(){$(".readover").hide();	
		  }, false );
		 }
		 
		 
		 function turnPrePage(){
		 	if(pageid>1){
		 	  //$("#readFld img").attr("src","/r2k/wx/image/round.jpg");
		 	  var loadImg = new Image();
			  pageid = pageid-1;
			  loadImg.src = preUrl+"&pageid="+pageid;
			  $("#readFld img").attr("src",loadImg.src);	   
			   window.setTimeout(function(){
			     if(!loadImg.complete){
			     // alert(new Date());
				         var oLoadDiv = $("<div class='loadfld'></div>");
				         $("body").append(oLoadDiv);
				         loadImg.onload = function(){
				     //alert(new Date());
						oLoadDiv.remove();
				   };
			   }
			     },1000);
			}
		};
		
		
		function translate(obj, dist, speed) {
			var style = obj && obj.style;
			if (!style) return;
			style.webkitTransitionDuration =
			style.MozTransitionDuration =
			style.msTransitionDuration =
			style.OTransitionDuration =
			style.transitionDuration = speed + 'ms';
			style.webkitTransform = 'translate(' + dist + 'px,0)' + 'translateZ(0)';
			style.msTransform =
			style.MozTransform =
			style.OTransform = 'translateX(' + dist + 'px)';
	    }

		
		$("#SliderSingle").data( "jslider" ).o.pointers[0].onmouseup=function(){
             var value =  $("#SliderSingle").data( "jslider" ).getValue();
             pageid = value;
             $("#readFld a").remove();
             firstId = pageid;
             
             var newObj = $("<a><img /></a>");
			 newObj.attr("id","bookImg"+pageid);
			 $("#readFld").append(newObj);
			 newObj.css({"width":viewWidth,"height":viewHeight});
			 newObj.find("img").css({"width":viewWidth,"height":viewHeight});
             
             
     	     var loadImg = new Image();
     	     loadImg.src = '<s:property value="onlineurl" escape="false"/>&pageid='+value+'&width='+viewImgWidth+'&height='+viewImgHeight;
     	     $("#readFld img").attr("src",loadImg.src);

			   window.setTimeout(function(){
			     if(!loadImg.complete){
			     // alert(new Date());
				         var oLoadDiv = $("<div class='loadfld'></div>");
				         $("body").append(oLoadDiv);
				         loadImg.onload = function(){
				     //alert(new Date());
						oLoadDiv.remove();
				   };
			   }
			 },1000); 
        };
        
        $("#changeBtn").click(function(){
        	location.href = "/r2k/wx/ebook1.do?metaid="+metaid+"&pageid="+pageid+'&orgid=<s:property value="orgid"/>';
        });
       
      
      
		 
	 });
	</script>
</head>
<body>
<div class="wrap" id="viewBox">
  <div id="readFld">
    <a><img style="width:100%;"></a>
  </div>  
</div>

<div class="bottom">
<div class="layout">
    <div class="layout-slider">
      <input id="SliderSingle" type="slider" name="price" value="1" />
    </div>
  </div>
  <script>
   jQuery("#SliderSingle").slider({ from: 1, to: '<s:property value="PageTotalCount" escape="false"/>', step: 1, dimension: '&nbsp;' });
       $("#SliderSingle").data( "jslider" ).onstatechange = function(){
         $(".jslider .jslider-bg i.v").css({display:"block",left:"0%",width:$("#SliderSingle").data( "jslider" ).getPrcValue()+'%'});
       };
       
  </script>
  
	<!--/r2k/wx/ebook1.do?metaid=<s:property value="metaid"/> -->
	<a href='javascript:'  class="catabtn" id="changeBtn">流式</a>
	<a href='/r2k/wx/getCatalogRow.do?metaid=<s:property value="metaid"/>&urlType=0&orgid=<s:property value="orgid"/>'  class="catabtn">目录</a>
</div>


<!--  <div class='loadfld'></div>-->
</body>
</html>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!doctype html>
<html>
<head>
  <meta charset="utf-8"/>
  <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no">
  <meta name="apple-mobile-web-app-capable" content="yes">
  <meta name="apple-touch-fullscreen" content="yes">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/pubtemplet/templet/test1/css/common.css">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/pubtemplet/templet/test1/css/stream.css">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/pubtemplet/templet/test1/css/streamv.css">
  <script src="${pageContext.request.contextPath}/pubtemplet/templet/test1/js/jquery-1.10.2.min.js"></script>
  <script>
	$.initLayout = function(){
		$("body").css({"background":"url(${pageContext.request.contextPath}/pubtemplet/templet/test1/images/zt_bg.jpg) no-repeat center", "background-size":" auto 100%"});
		var _scale = 1360/810;
		var viewWidth = $(window).width();
		var viewHeight = $(window).height();
		var nHeight = $(".topic_area").width()*_scale;
		if(nHeight < viewHeight*0.71){
			$(".topic_area").css({"width":viewWidth*.75,"height":nHeight});
			$(".blanktop").css("height",(viewHeight-nHeight)/3);
		}
		else{
			nHeight = viewHeight*0.71;
			$(".topic_area").css({"width":nHeight/_scale,"height":nHeight});
			$(".blanktop").css("height",viewHeight*.12);
		}
	}
	$.ajaxData = function(link,callback) {
		var _link = link;
		var beginTime, endTime;
		$.ajax({
			url : _link,
			timeout: 15000,
			success : function(responseData) {
				callback(responseData);
			},
			error: function (XMLHttpRequest, textStatus, errorThrown) {
				console.log(XMLHttpRequest+" , "+errorThrown);
			}
		});
	};
	$(function(){
		$.initLayout();
		var param = {
			link: "${pageContext.request.contextPath}/api/topics?published=1"
		};
		
		var fillData = function(xml) {
			var aTopic = $(xml).find("Topics > Topic");
			var num = 8;
			for(var i=0; i<num; i++){
				var $topic = $(aTopic[i]);
				var _title = $topic.find("TopicName").text();
				$(".top_block"+(i+1)).find("h5").html(_title);


				$topic.find("Articles > Article").each(function(){
					var oName = $(this).find("HeadLine");
					if(oName){
						var _text = "<p>→ "+ oName.text() +"</p>";
						var obj = $(".top_block"+(i+1)).find("div.text").append(_text);
					}
					
				})
				
				
			
			}
		};
		
		
		$.ajaxData(param.link, fillData);
		
	})
</script>
</head>

<body>
<div class="blanktop"></div>
<div class="topic_area ver_topic" >
  <!--文化娱乐-->
  <div class="top_block1" style="top:0;left:0;width:31%; height:38%;">
    <h5>文化娱乐</h5>
    <p class="blankh"></p>
    <div class="text">
      
    </div>
  </div>
  <!--2--头版头条-->
  <div class="top_block2" style="top:0;left:34%;width:66%; height:38%;">
    <h5></h5>
    <p class="blankh"></p>
    <div class="text">
      
    </div>
  </div>
  <!--3--热点-->
  <div class="top_block3" style="top:40%;left:0%;width:66%; height:19%;">
    <h5></h5>
    <p class="blankh"></p>
    <div class="text">
      
    </div>
  </div>
  <!--4--科技-->
  <div class="top_block4" style="top:61%;left:0%;width:66%; height:19%;">
    <h5></h5>
    <p class="blankh"></p>
    <div class="text">
      
    </div>
  </div>
  
  <!--6--经济-->
  <div class="top_block6" style="top:40%;left:68%;width:32%;height:40%;">
    <h5></h5>
    <p class="blankh"></p>
    <div class="text">
      
    </div>
  </div>

  <!--5--军事-->
  <div class="top_block5" style="top:82%;left:0%;width:31%;height:19%;">
    <h5></h5>
  </div>
  <!--7--体育-->
  <div class="top_block7" style="top:82%;left:34%;width:31%;height:19%;">
    <h5></h5>
  </div>
  <!--8--旅行-->
  <div class="top_block8" style="top:82%;left:68%;width:31%;height:19%;">
    <h5></h5>
  </div>


</div>

</body>
</body>
</html>

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
  <script src="${pageContext.request.contextPath}/pubtemplet/templet/test1/js/zepto.js"></script>
</head>

<body>
<div class="blanktop"></div>
<%--<div id="container"></div>--%>
<script>
var areawidth;
window.onload = function(){
	function getData(){
		var url = "${pageContext.request.contextPath}/api/topics?published=1";
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
					setTop();
				}
			} 
		}); 
	}//end getDetailDate
	function readXML(xml){
		var topics = $(xml).find("Topics > Topic");
		var topiclen = topics.length;
		var pagesize = (topiclen % 8 == 0) ? topiclen / 8 : Math.floor(topiclen / 8) + 1;
		//var pageflag = false;	//判断是否为同一页面标示
		var pageindex = 0;		//当前页码
		var boxnode;			//当前页面容器
		topics.each(function(index){
			//if((index+1) % 8 == 1 && pageflag){
			if((index+1) % 8 == 1){
				//分页处理
				var pagecurrent = ((index+1) % 8 == 0) ? (index+1) / 8 : Math.floor((index+1) / 8) + 1;
				if(pagecurrent > pageindex){
					pageflag = true;
					pageindex += 1;
					//展示只显示一页
					if(pageindex == 2){
						return;
					}
					boxnode = $("<div>");
					$(boxnode).addClass("topic_area");
					$(boxnode).attr("id","topicBox");	//展示
					//$(boxnode).attr("id","topicBox"+pageindex);
				}
				createPart1(this, boxnode, index);
			} else if((index+1) % 8 == 2){
				createPart2(this, boxnode, index);
			} else if((index+1) % 8 == 3){
				createPart3(this, boxnode, index);
			} else if((index+1) % 8 == 4){
				createPart4(this, boxnode, index);
			} else if((index+1) % 8 == 5){
				createPart5(this, boxnode, index);
			} else if((index+1) % 8 == 6){
				createPart6(this, boxnode, index);
			} else if((index+1) % 8 == 7){
				createPart7(this, boxnode, index);
			} else if((index+1) % 8 == 0){
				createPart8(this, boxnode, index);
				if( (index+1) != topiclen){
					$("body").append(boxnode);
					areawidth = $(".topic_area").width();
					//$("#container").append(boxnode);
					//pageflag = false;
					boxnode = null;
				}
			}
			if((index+1) == topiclen){
				if((index+1) % 8 != 0){
					if((index+1) % 8 == 1){
						createPart2(null, boxnode, index);
						createPart3(null, boxnode, index);
						createPart4(null, boxnode, index);
						createPart5(null, boxnode, index);
						createPart6(null, boxnode, index);
						createPart7(null, boxnode, index);
						createPart8(null, boxnode, index);
					} else if((index+1) % 8 == 2){
						createPart3(null, boxnode, index);
						createPart4(null, boxnode, index);
						createPart5(null, boxnode, index);
						createPart6(null, boxnode, index);
						createPart7(null, boxnode, index);
						createPart8(null, boxnode, index);
					} else if((index+1) % 8 == 3){
						createPart4(null, boxnode, index);
						createPart5(null, boxnode, index);
						createPart6(null, boxnode, index);
						createPart7(null, boxnode, index);
						createPart8(null, boxnode, index);
					} else if((index+1) % 8 == 4){
						createPart5(null, boxnode, index);
						createPart6(null, boxnode, index);
						createPart7(null, boxnode, index);
						createPart8(null, boxnode, index);
					} else if((index+1) % 8 == 5){
						createPart6(null, boxnode, index);
						createPart7(null, boxnode, index);
						createPart8(null, boxnode, index);
					} else if((index+1) % 8 == 6){
						createPart7(null, boxnode, index);
						createPart8(null, boxnode, index);
					} else if((index+1) % 8 == 7){
						createPart8(null, boxnode, index);
					}
				}
				
				$("body").append(boxnode);
				areawidth = $(".topic_area").width();
				//$("#container").append(boxnode);
				//pageflag = false;
				boxnode = null;
			}
		}); 
		//节点1对应方法
		function createPart1(dataobj, node, index){
			var h5node = $("<h5>");		//专题标题
			$(h5node).html($(dataobj).find("TopicName").text());
			var pblanknode = $("<p>");
			$(pblanknode).addClass("blankh"); 
			//$(pblanknode).css({"clear":"both", "height":"40%"}); 
			var artdivnode = $("<div>");	//专题文章列表容器
			$(dataobj).find("Articles > Article").each(function(index){
				var artpnode = $("<p>");	//专题文章
				$(artpnode).html("→"+$(this).find("HeadLine").text());
				artdivnode.append(artpnode);
			});
			
			$(artdivnode).addClass("text"); 
			//添加节点
			var div1node = $("<div>");
			div1node.append(h5node);
			div1node.append(pblanknode);
			div1node.append(artdivnode);
			div1node.addClass("top_block1");
			div1node.css({"top":0, "left":0, "width":"19%", "height":"66%"});
			var topicId = $(dataobj).attr("id");
			if(dataobj != null){
				$(div1node).bind("click", function() {
					window.location = "${pageContext.request.contextPath}/topic/showTopicList.do?topicId="+topicId;
				});
			}
			$(node).append(div1node);
		}
		//节点2对应方法
		function createPart2(dataobj, node, index){
			var h5node = $("<h3>");		//专题标题
			$(h5node).html($(dataobj).find("TopicName").text());
			var pblanknode = $("<p>");
			$(pblanknode).addClass("blankh"); 
			//$(pblanknode).css({"clear":"both", "height":"40%"}); 
			var artdivnode = $("<div>");	//专题文章列表容器
			$(dataobj).find("Articles > Article").each(function(index){
				var artpnode = $("<p>");	//专题文章
				$(artpnode).html("→"+$(this).find("HeadLine").text());
				artdivnode.append(artpnode);
			});
			
			$(artdivnode).addClass("text"); 
			
			//添加节点
			var div2node = $("<div>");
			div2node.append(h5node);
			div2node.append(pblanknode);
			div2node.append(artdivnode);
			div2node.addClass("top_block2");
			div2node.css({"top":0, "left":"20%", "width":"40%", "height":"66%"});
			var topicId = $(dataobj).attr("id");
			if(dataobj != null){
				$(div2node).bind("click", function() {
					window.location = "${pageContext.request.contextPath}/topic/showTopicList.do?topicId="+topicId;
				});
			}
			$(node).append(div2node);
		}
		//节点3对应方法
		function createPart3(dataobj, node, index){
			var h5node = $("<h5>");		//专题标题
			$(h5node).html($(dataobj).find("TopicName").text());
			var pblanknode = $("<p>");
			$(pblanknode).addClass("blankh"); 
			//$(pblanknode).css({"clear":"both", "height":"40%"}); 
			var artdivnode = $("<div>");	//专题文章列表容器
			$(dataobj).find("Articles > Article").each(function(index){
				var artpnode = $("<p>");	//专题文章
				$(artpnode).html("→"+$(this).find("HeadLine").text());
				artdivnode.append(artpnode);
			});
			
			$(artdivnode).addClass("text"); 
			
			//添加节点
			var div3node = $("<div>");
			div3node.append(h5node);
			div3node.append(pblanknode);
			div3node.append(artdivnode);
			div3node.addClass("top_block3");
			div3node.css({"top":0, "left":"61%", "width":"39%", "height":"31%"});
			var topicId = $(dataobj).attr("id");
			if(dataobj != null){
				$(div3node).bind("click", function() {
					window.location = "${pageContext.request.contextPath}/topic/showTopicList.do?topicId="+topicId;
				});
			}
			$(node).append(div3node);
		}
		//节点4对应方法
		function createPart4(dataobj, node, index){
			var h5node = $("<h5>");		//专题标题
			$(h5node).html($(dataobj).find("TopicName").text());
			var pblanknode = $("<p>");
			$(pblanknode).addClass("blankh"); 
			//$(pblanknode).css({"clear":"both", "height":"40%"}); 
			var artdivnode = $("<div>");	//专题文章列表容器
			$(dataobj).find("Articles > Article").each(function(index){
				var artpnode = $("<p>");	//专题文章
				$(artpnode).html("→"+$(this).find("HeadLine").text());
				artdivnode.append(artpnode);
			});
			
			$(artdivnode).addClass("text"); 
			
			//添加节点
			var div4node = $("<div>");
			div4node.append(h5node);
			div4node.append(pblanknode);
			div4node.append(artdivnode);
			div4node.addClass("top_block4");
			div4node.css({"top":"68%", "left":"0", "width":"39%", "height":"32%"});
			var topicId = $(dataobj).attr("id");
			if(dataobj != null){
				$(div4node).bind("click", function() {
					window.location = "${pageContext.request.contextPath}/topic/showTopicList.do?topicId="+topicId;
				});
			}
			$(boxnode).append(div4node);
		}
		//节点5对应方法
		function createPart5(dataobj, node, index){
			var h5node = $("<h5>");		//专题标题
			$(h5node).html($(dataobj).find("TopicName").text());
			var pblanknode = $("<p>");
			$(pblanknode).addClass("blankh"); 
			//$(pblanknode).css({"clear":"both", "height":"40%"}); 
			var artdivnode = $("<div>");	//专题文章列表容器
			
			$(artdivnode).addClass("text"); 
			
			//添加节点
			var div5node = $("<div>");
			div5node.append(h5node);
			div5node.append(pblanknode);
			div5node.append(artdivnode);
			div5node.addClass("top_block5");
			div5node.css({"top":"68%", "left":"40%", "width":"20%", "height":"32%"});
			var topicId = $(dataobj).attr("id");
			if(dataobj != null){
				$(div5node).bind("click", function() {
					window.location = "${pageContext.request.contextPath}/topic/showTopicList.do?topicId="+topicId;
				});
			}
			$(node).append(div5node);
		}
		//节点6对应方法
		function createPart6(dataobj, node, index){
			var h5node = $("<h5>");		//专题标题
			$(h5node).html($(dataobj).find("TopicName").text());
			var pblanknode = $("<p>");
			$(pblanknode).addClass("blankh"); 
			//$(pblanknode).css({"clear":"both", "height":"40%"}); 
			var artdivnode = $("<div>");	//专题文章列表容器
			$(dataobj).find("Articles > Article").each(function(index){
				var artpnode = $("<p>");	//专题文章
				$(artpnode).html("→"+$(this).find("HeadLine").text());
				artdivnode.append(artpnode);
			});
			
			$(artdivnode).addClass("text"); 
			
			//添加节点
			var div6node = $("<div>");
			div6node.append(h5node);
			div6node.append(pblanknode);
			div6node.append(artdivnode);
			div6node.addClass("top_block6");
			div6node.css({"top":"33%", "left":"61%", "width":"19%", "height":"67%"});
			var topicId = $(dataobj).attr("id");
			if(dataobj != null){
				$(div6node).bind("click", function() {
					window.location = "${pageContext.request.contextPath}/topic/showTopicList.do?topicId="+topicId;
				});
			}
			$(node).append(div6node);
		}
		//节点7对应方法
		function createPart7(dataobj, node, index){
			var h5node = $("<h5>");		//专题标题
			$(h5node).html($(dataobj).find("TopicName").text());
			var pblanknode = $("<p>");
			$(pblanknode).addClass("blankh"); 
			//$(pblanknode).css({"clear":"both", "height":"40%"}); 
			var artdivnode = $("<div>");	//专题文章列表容器
			
			$(artdivnode).addClass("text"); 
			
			//添加节点
			var div7node = $("<div>");
			div7node.append(h5node);
			div7node.append(pblanknode);
			div7node.append(artdivnode);
			div7node.addClass("top_block7");
			div7node.css({"top":"33%", "left":"81%", "width":"19%", "height":"33%"});
			var topicId = $(dataobj).attr("id");
			if(dataobj != null){
				$(div7node).bind("click", function() {
					window.location = "${pageContext.request.contextPath}/topic/showTopicList.do?topicId="+topicId;
				});
			}
			$(node).append(div7node);
		}
		//节点8对应方法
		function createPart8(dataobj, node, index){
			var h5node = $("<h5>");		//专题标题
			$(h5node).html($(dataobj).find("TopicName").text());
			var pblanknode = $("<p>");
			$(pblanknode).addClass("blankh"); 
			//$(pblanknode).css({"clear":"both", "height":"40%"}); 
			var artdivnode = $("<div>");	//专题文章列表容器
			
			$(artdivnode).addClass("text"); 
			
			//添加节点
			var div8node = $("<div>");
			div8node.append(h5node);
			div8node.append(pblanknode);
			div8node.append(artdivnode);
			div8node.addClass("top_block8");
			div8node.css({"top":"68%", "left":"81%", "width":"19%", "height":"32%"});
			var topicId = $(dataobj).attr("id");
			if(dataobj != null){
				$(div8node).bind("click", function() {
					window.location = "${pageContext.request.contextPath}/topic/showTopicList.do?topicId="+topicId;
				});
			}
			$(node).append(div8node);
		}
	}//end readxml
	$("body").css({"background":" url(${pageContext.request.contextPath}/pubtemplet/templet/test1/images/zt_bg.jpg) no-repeat center","background-size":"100%"});
	getData();
	
	function setTop(){
		var _scale = 1004/1684;
		var viewWidth = $(window).width();
		var viewHeight = $(window).height();
		var nHeight = areawidth*_scale;
		if(nHeight < viewHeight*0.88){
			$(".topic_area").css({"width":viewWidth*.82,"height":nHeight});
			$(".blanktop").css("height",(viewHeight-nHeight)/2);
		}
		else{
			nHeight = viewHeight*0.88;
			$(".topic_area").css({"width":nHeight/_scale,"height":nHeight});
			$(".blanktop").css("height",viewHeight*.06);
		}
	}
}
</script>
</body>
</html>

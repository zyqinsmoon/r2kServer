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
	<link rel="stylesheet" href="${ctx}/r2k/wx/css/idangerous.swiper.css">
    <script src="${ctx}/r2k/wx/js/jquery-1.10.2.min.js"></script>
	<script src="${ctx}/r2k/wx/js/idangerous.swiper-2.1.min.js"></script>
	<script src="${ctx}/r2k/wx/js/jquery.cookie.js"></script>
	<script src="${ctx}/r2k/wx/js/epaper.js"></script>
	<script>
		 $.cookie("orgid","swhy",{path:"/"});
	  /*if(!$.cookie("orgid")){
       	  $.cookie("orgid","swhy",{path:"/"});
      }*/
		var pageFstId = 0;
		var actId = 0;
		var arrLink = [];
		var noSlider;
    	$.PageData = function(link,callback) {
			
			var _link = link;
			$.ajax({
				url : _link,
				dataType : "xml",
				success : function(responseData) {
					//alert("xml success success ");
					callback(responseData);
				},
				error: function (XMLHttpRequest, textStatus, errorThrown) {
					//$(".swiper-slide").css("background","none");
					$.showPrompt("数据加载异常，请重试");
					console.log("网络异常！"+errorThrown);
				}
				
			});

		};
		var data_params = {
			//url参数
			periodId : "period",
			periodLink : "/r2k/api/period/",
			
			getSubUrl : function(url){
				var pos = url.indexOf("/r2k/");
				var _url = pos == -1? "" : url.substring(pos);
				return _url;
			},
			getQueryString: function(name){
				var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
				var r = location.search.substr(1).match(reg);
				if (r!=null) return unescape(r[2]);
				return null;
			},
			getParamUrl: function(name){
				var _purl = this.getQueryString(name);
				return this.getSubUrl(_purl);
			},
			getNormalUrl: function(name){
				return this.periodLink + this.getQueryString(name);
			}
		};
		
		$.addSwipeDiv = function(total, obj){
			var _num = 0;
			for(var i=0; i<total; i++){
				var _oDiv = $("<div>");
				_oDiv.attr("id","swipe"+_num);
				_oDiv.attr("class","swiper-slide");
				obj.append(_oDiv);
				_num++;
			}
			var iLeft;
			
			var mySwiper = new Swiper('.swiper-container',{
				createPagination: false,
				onSlideChangeEnd: function(s){
					var _s = s;
					
					if(!noSlider){
						history.pushState({ title: "title"}, "back", location.href.split("&pageId=")[0] + "&pageId=" + _s.activeIndex);	
					}
					noSlider = false;
				},
				onTouchStart: function(s){
					var _s = s;
				    var _pageId = _s.activeIndex;
					iLeft = $("#swipe"+_pageId).offset().left;
					
				},
				onTouchEnd: function(s){
				//debugger;
					var _s = s;
					var _pageId = _s.activeIndex;
					actId = _s.activeIndex;
					
					var nLeft = $("#swipe"+_pageId).offset().left;
					if(_pageId == 0 && nLeft > iLeft){
						$.showPrompt("已经是第一版");
					}
					else if(_pageId == arrLink.length-1 && nLeft < iLeft){
						$.showPrompt("已经是最后一版");
					}
					$.showPageData(_pageId);
				}
		    });
			return mySwiper;
		};
		$.showPageData = function(id) {
			var _pageId = id;
			if(!$("#swipe"+_pageId).children().length){
				var _infoLink = data_params.getSubUrl(arrLink[_pageId]);
				$.pageInfo(_infoLink,_pageId);
			}
			else{
				//调整高度
				var _height = $("#swipe"+_pageId).children().height();
				$.adjustHeight(_pageId,_height);
			}
		};
		$.adjustHeight = function(id, height){
			var pHeight = height;
			$("#swipe"+id).css("height",pHeight);
			if(actId == id){
				$(".swiper-wrapper").css("height",pHeight);
				$(".swiper-container").css("height",pHeight);
			}
		}
		$.showPrompt = function(txt){
			var oPrompt = $("<div>");
			oPrompt.addClass("prompt");	
			oPrompt.html(txt);
			$("body").append(oPrompt);
			oPrompt.fadeIn(100);
			setTimeout(function(){
			   oPrompt.fadeOut(100);
			},800);
		
		};
		var pageSwiper = null;
		$(function(){
			var pageBox = $("#swiperBox > div");
			var viewWidth = $(window).width();

			//获取参数pageId
			if(data_params.getQueryString("pageId")){
				pageFstId = Number(data_params.getQueryString("pageId"));
				actId = pageFstId;
			}
			
			$.PageData(data_params.getNormalUrl(data_params.periodId), function(data){
				//日历按钮添加链接
				var _paper = $(data).find("MetaInfo > Paper"); 
				if(_paper.length > 0){
					var paperId = _paper.attr("id");
					var paperName = _paper.find("PaperName").text();
					if(paperName.length > 8){
						paperName = paperName.slice(0,8)+"..";
					}
					$("header span").html(paperName);
					var linkurl = "calendar.jsp?paperId="+paperId;	
					$(".calendar").attr("href",linkurl);
				}
				
				var pageObj = $(data).find("Pages > Page");
				if(pageObj.length == 0){					
					alert("数据调整，请查看其它报纸");
					return;
				}

				//左上角按钮显示的版面列表
				pageObj.each(function(index){
					var sLink = $(this).find("Link").text();
					arrLink.push(sLink);

					var _pNum = $(this).find("PageNumber").text();
					var _pName = $(this).find("PageName").text();

					if(location.href.indexOf("&pageId=") == -1){
						var _cUrl = location.href+"&pageId="+index;
					}
					else{
						var _cUrl = location.href.split("&pageId=")[0]+"&pageId="+index;
					}
					var _oPList = $("<li><a></a></li>");
					_oPList.find("a").attr("href",_cUrl);
					_oPList.find("a").append(_pNum+"版： "+_pName);
					$(".pagebmlist ul").append(_oPList);
				});
				$(".pageicon").click(function(){
					$("#swiperBox").toggle();
					$(".pagebmlist").toggle();
					
				});
				
				//加载滑屏Div
				pageSwiper = $.addSwipeDiv(arrLink.length, pageBox);
				pageSwiper.swipeTo(pageFstId, 1);
				
				//加载本页数据
				var _infoLink = data_params.getSubUrl(arrLink[pageFstId]);
				if(_infoLink){
					$.pageInfo(_infoLink,pageFstId);
				}
				else{
					alert("获取版面详细信息失败！");
				}
			});
			$.pageInfo = function (link, id){
				var _link = link;
				var _pid = id;
				$.PageData(_link, function(xml){

					//获取版面简图
					var strImg = $(xml).find("Page > BriefImage").text();
					if(!$.trim(strImg)){
						//版面图不存在，待处理
					}
					var oImg = $("<img>");
					oImg.attr("src",strImg);
					oImg.attr("usemap","#articles"+id);
					
					//创建热区
					var oMap = $('<map>');
					oMap.attr("name","articles"+id);
					
					var _oWrap = $("<div>");
					pageBox.find("#swipe"+id).append(_oWrap);
						
					_oWrap.append2(oImg, function(){
						var imgHeight = _oWrap.find("img").height();

						var _strb = "<div class='listname'><p>本版文章</p><ul>";
						$(xml).find("Articles > Article").each(function(index){
							//文章列表
							var title = $(this).find("HeadLine").text();
							var sLink = $(this).find("Link").text();
							if(!$.trim(title)){
							    title = "无标题";
							}

							var _strLi = "<li><a href='article.jsp?article="+ sLink +"'>"+"◎"+title+"</a></li>";
							_strb += _strLi;
							//添加map坐标
							var strCorr = $(this).find("Region").text();
							var arrCorr = strCorr.split("#");
							var corrds = ""; 
							$.each(arrCorr, function(i){ 
								var corPair = arrCorr[i].split(",");
								var x = parseInt(corPair[0])*viewWidth/100;
								var y = parseInt(corPair[1])*imgHeight/100;
								i == arrCorr.length-1?corrds += x+","+y:corrds += x+","+y+",";
							});
							var objArea = $('<area shape="poly" />');
							objArea.attr("coords",corrds);
							objArea.attr("href", "article.jsp?article="+sLink);
							oMap.append(objArea);
						});
						_strb += "</ul></div>";
						
						_oWrap.append(_strb).append(oMap);
						//if(_oWrap.height() > pHeight){
							
						//}
						var pHeight = _oWrap.height();
						$.adjustHeight(id, pHeight);
						
					}, function(){
						$.showPrompt("图片加载失败，请重试！");
					});			
				});
			};
		});
		
		var fnHashTrigger = function(flag) {
			noSlider = true;
			var _pageId = location.href.split("&pageId=")[1];
			if(_pageId == undefined){
				_pageId = pageFstId;
			}
			pageSwiper.swipeTo(_pageId, 1);
			$.showPageData(_pageId);
		};
		if (history.pushState) {
			window.addEventListener("popstate", function() {
				fnHashTrigger();																
			});
		}
		

	</script>
</head>
<body class="pagebody">

<header>
	<a class="pageicon"></a>
	<span>数字报纸</span>
	<a class="calendar"></a>
	
</header>
<div class="blank"></div>
<div class="swiper-container" id="swiperBox">
	<div class="swiper-wrapper">
		
	</div>
	
</div>
<div class="pagebmlist" style="display:none;">
   <ul>
   </ul>
</div>

</body>
</html>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no">
    <meta name="apple-mobile-web-app-capable" content="yes">
    <meta name="apple-touch-fullscreen" content="yes">
    <link rel="stylesheet" href="${ctx}/r2k/wx/css/base.css" />
	<link rel="stylesheet" href="${ctx}/r2k/wx/css/idangerous.swiper.css">
    <script src="${ctx}/r2k/wx/js/jquery-1.10.2.min.js"></script>
    <script src="${ctx}/r2k/wx/js/jquery.cookie.js"></script>
    <script src="${ctx}/r2k/wx/js/idangerous.swiper.min.js"></script>
	<script src="${ctx}/r2k/wx/js/books.js"></script>
    <title><s:property value="bookName" escape="false"/></title>
    <script>
		var pageId=<s:property value="pageid" escape="false"/>;
		var pageCount = <s:property value="PageTotalCount"/>;
		
		var bookBase = {
			viewWidth: $(window).width(),
			viewHeight: $(window).height(),
			lastPageId: pageId,
			firstPageId: pageId,

			getHtml: function(){
				var htmlurl = "/r2k/wx/getHtml.do?metaid="+this.getQueryString("metaid")+'&orgid=<s:property value="orgid"/>&userName=swhy03&width='+this.viewWidth+"&height="+this.viewHeight;
				return htmlurl;
			},
			getCss: function(){
				var cssurl = "/r2k/wx/getCss.do?metaid="+this.getQueryString("metaid")+"&&orgid=<s:property value="orgid"/>";
				return cssurl;
			},
			getQueryString: function(name){
				var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
				var r = location.search.substr(1).match(reg);
				if (r!=null) return unescape(r[2]);
				return null;
			},
			addStyle: function(data){
				var style = document.createElement('style');
				style.innerHTML = data;		 
				$("head").append($(style));
			}
		};
		
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
					//$.showPrompt("数据加载异常，请重试");
					//alert("ajax error "+errorThrown);
					location.href = "/r2k/wx/ebook.do?metaid="+bookBase.getQueryString("metaid")+"&pageid="+pageId+'&orgid=<s:property value="orgid"/>';
				},
				beforeSend : function(XMLHttpRequest) {
					beginTime = new Date();
					
				},
				complete:function(XMLHttpRequest, textStatus) {
					endTime = new Date();
					//console.log("开始时间：" + beginTime + "  结束时间："+ endTime + "  时间差(s)" + (endTime-beginTime)/1000);
				}
			});
		};

		
		$.BufferData = function(obj, params){
			var _dataBox = obj;
			var _params = params;
			var swiperPicture = null;
			var firstIndex = 0;
			var lastIndex = 0;
			var isAjaxing = false; 
			
			this.getFstIndex = function(){
				return firstIndex;
			}
			this.getLastIndex = function(){
				return lastIndex;
			}
			this.getRealFstIndex = function(){
				var _fstId = Number(_dataBox.children().first().attr("dataindex"));
				return _fstId;
			}
			this.getRealLastIndex = function(){
				var _lastId = Number(_dataBox.children().last().attr("dataindex"));
				return _lastId;
			}
			this.isAjaxing = function(){
				return isAjaxing;
			}
			this.getActiveId = function(index){
				var newId = Number(_dataBox.children().eq(index).attr("dataindex")); 
				return newId;
			}

			this.onInit = function(callback){
				
				var _i = 0;
				var _class = "."+_dataBox.parent().attr("class");
				swiperPicture = new Swiper(_class,{});
				var newSlide = swiperPicture.appendSlide('<div></div>', 'swiper-slide');
				$(newSlide).attr("id", "data_"+ _i);
				$(newSlide).attr("dataindex", _i);
				this.nextMoreBuffer(callback);
			}
			
			this.nextMoreBuffer = function(callback){
				//isAjaxing = true;
				var self = this;
				this.nextBuffer(bookBase.lastPageId, function(){	
					bookBase.lastPageId++;
					pageId = bookBase.lastPageId;
					if(lastIndex < _params.initNum-1){
						self.nextMoreBuffer(callback);
					}
					else{
						callback(swiperPicture);
						//isAjaxing = false;
					}
				});
			};
			
			this.preBuffer = function( pid, callback){
				isAjaxing = true;
	
				var _dataDiv = _dataBox.children().first();
				getData(pid, function(data){
					_dataDiv.find(">div").prepend(data);
					clearLoading(_dataDiv);

					firstIndex = preSplitPage(_dataDiv);
					if(callback){
						callback();
					}
					isAjaxing = false;
				})
			};
			
			this.nextBuffer = function(pid, callback){
				isAjaxing = true;
				var _dataDiv = _dataBox.children().last();
				
				getData(pid, function(data){
					
					_dataDiv.find(">div").append(data);
					clearLoading(_dataDiv);
					lastIndex = nextSplitPage(_dataDiv);
					
					if(callback){
						callback();
					}
					isAjaxing = false;
				})	
			}
			
			var clearLoading = function(obj){
				var _dataDiv = obj;
				_dataDiv.css("background","none");
			}
			
			var getData = function(pid, callback){
				var _pageId = pid;
				$.ajaxData(bookBase.getHtml()+"&page="+_pageId, function(data){
					callback(data);
			
				})
			}

			var preSplitPage = function (obj){
				var $booksView = obj.find(">div");
				var bufferIndex = Number(obj.attr("dataindex"));
				//var $actIndex = actIndex;
				
				while (1) {			
					var $newPage = $.jPageView.splitLastNode($booksView, function($e) {
						//比较函数，完全包含返回-1，相交返回0，相离返回1
						var split = -1;
						
						if ($e.offset().bottom < $booksView.offset().s_bottom - bookBase.viewHeight -50) {
							split = 1;
						} else if ($e.offset().top < $booksView.offset().s_bottom - bookBase.viewHeight - 50) {			
							split = 0;
						}
						return split;
					});
					if ($newPage == null || $newPage.html() == ""){
						break;
					}
					if($booksView.children().length == 0){
						$booksView.html($newPage.html()) ;
						$newPage.remove();
						break;
					}	
					bufferIndex--;
					
					var newSlide = swiperPicture.prependSlide('<div></div>', 'swiper-slide');

					swiperPicture.swipeTo(swiperPicture.activeIndex+1,0);
					
					$(newSlide).find(">div").html($newPage.html());
					clearLoading($(newSlide));
					$(newSlide).attr("id","data_"+ bufferIndex);
					$(newSlide).attr("dataindex", bufferIndex);
					$booksView = $(newSlide).find(">div");
				}

				return bufferIndex;
			
			}
			var nextSplitPage = function (obj){
				
				var $booksView = obj.find(">div");
				var bufferIndex = Number(obj.attr("dataindex"));
				
				while (1) {
					var $newPage = $.jPageView.splitNode($booksView, function($e) {
					//alert($e.offset().top+" , "+$booksView.offset().top+" , "+bookobj.viewHeight+" , "+$e.height()+" ...");
						//比较函数，完全包含返回-1，部分包含0，完全不包含1
						var split = -1;
						
						if ($e.offset().top > $booksView.offset().top + bookBase.viewHeight -30) {
							split = 1;
						} else if ($e.offset().top + $e.height() > $booksView.offset().top + bookBase.viewHeight -30) {
							split = 0;
						}
						return split;
					});
					
					if ($newPage == null || $newPage.html() == "" ){
						break;
					}

					if( $booksView.children().length == 0){
						$booksView.html($newPage.html()) ;
						$newPage.remove();
						break;
					}
					bufferIndex++;
					
					var newSlide = swiperPicture.appendSlide('<div></div>', 'swiper-slide');
					$(newSlide).find(">div").html($newPage.html());
					clearLoading($(newSlide));
					
					$(newSlide).attr("id","data_"+ bufferIndex);
					$(newSlide).attr("dataindex", bufferIndex);
					$booksView = $(newSlide);
				}
				return bufferIndex;
			}
			
		}
		$.LoadController = function(obj, params){
			
			var _params = params;
			
			var loadhandle = function(direction, isMoveing){
				if( _params.dataView.isAjaxing() ){
					return 0;
				}
				var firstIndex = _params.dataView.getFstIndex();
				var lastIndex = _params.dataView.getLastIndex();
				var _s = _params.swiper;
				var _actIndex = _s.activeIndex;
				var _actId = _params.dataView.getActiveId(_actIndex);
				//var nSwitch = 3;
				if(isMoveing == "movenext"){
					_actId++;
				}
				else{
					_actId--;
				}
				if(direction == "next"){//后翻

					if(_actId < lastIndex - _params.bufNum-1  || bookBase.lastPageId == pageCount){
						return 0;
					}
					
				}
				else if(direction == "pre"){//前翻
					if(_actId >= firstIndex + _params.bufNum || bookBase.firstPageId == 1){
						return 0;
					}
					
				}
				return 1;
			};
			
			
			this.createNextBlank = function(direction){
				//其他（0）-不继续加载 ，1-继续
				var isGoon = loadhandle(direction,"movenext");
				if(isGoon!=1){
					return;
				}
				var lastRealIndex = _params.dataView.getRealLastIndex();
				if( $("#_data" + lastRealIndex).find(">div").children().length == 0 ){
					return;
				}
				
				var lastIndex = _params.dataView.getLastIndex();				
				var _s = _params.swiper;
				
				var newSlide = _s.appendSlide('<div></div>', 'swiper-slide');
				$(newSlide).attr("id","data_"+ (lastIndex+1) );
				$(newSlide).attr("dataindex", lastIndex+1 );
			}
			
			var addpre;
			this.createPreBlank = function(direction){
				var isGoon = loadhandle(direction,"movenpre");
				if(isGoon!=1){
					return;
				}
				
				var fstRealIndex = _params.dataView.getRealFstIndex();
				if( $("#data_" + fstRealIndex).find(">div").children().length == 0 ){
					return;
				}
				var firstIndex = _params.dataView.getFstIndex();					
				var _s = _params.swiper;
									
				var newSlide = _s.prependSlide('<div></div>', 'swiper-slide');
				$(newSlide).attr("id","data_"+ (firstIndex-1) );
				$(newSlide).attr("dataindex", firstIndex-1 );
				$(newSlide).attr("rrrrr", firstIndex-1  );
				addpre = true;
			}
			this.loadNextSlider = function(direction){

				var isGoon = loadhandle(direction);
				if(isGoon!=1){
					return;
				}
				
				var self = this;
				_params.dataView.nextBuffer( bookBase.lastPageId+1, function(){
					bookBase.lastPageId++;
					pageId = bookBase.lastPageId;
					//self.loadNextSlider(direction);
				});

			}
			this.loadPreSlider = function(direction){
				var isGoon = loadhandle(direction);
				if(isGoon!=1){
					return;
				}

				if(addpre){
					var _s = _params.swiper;
					_s.swipeTo(_s.activeIndex+1,0);
					addpre = false;
				}
				_params.dataView.preBuffer(bookBase.firstPageId-1, function(){
					bookBase.firstPageId--;
					pageId = bookBase.firstPageId;
				});
				
			}
			
		
		}
			
		$.fn.extend({
			bufferData : function(params){
				params = $.extend({
					
				}, params);	//支持默认参数
				
				return new jQuery.BufferData(this, params);
			},
			pageView : function(params){
				params = $.extend({
					
				}, params);	//支持默认参数
				
				return new jQuery.PageView(this, params);
			}
			
		});
		$.extend({
			loadController : function(params){
				params = $.extend({
					
				}, params);	//支持默认参数
				
				return new jQuery.LoadController(this, params);
			}
		})
		
		
		$(function(){
			
			var param = {
				"initNum": 3
			}
			var dataView = $("#swipeBox .swiper-wrapper").bufferData(param);
			
			$.ajaxData(bookBase.getCss(), function(data){
				//成功获取css并添加到页面
				var _data = data;
				bookBase.addStyle(_data);
				
				//初始化html流式数据
				dataView.onInit(function(swiper){
					$.swiperEvent(swiper);
					
				})
			});
			
			//处理swiper事件
			$.swiperEvent = function(swiper){
				var pageSwiper = swiper;
				var iLeft,sActId;
				var startX, moveX;
				var	direction; //翻页方向,true-右，false-左
				var doOne;
				var params = {
					"bufNum": 3,
					"swiper": pageSwiper,
					"dataView": dataView
				}
				var loadSwipe = $.loadController(params);

				pageSwiper.addCallback('TouchStart', function(s,e){
					
					if(e.type != "touchstart"){
						return;
					}
					
					var _s = s;
					var _actIndex = _s.activeIndex;
					sActId = dataView.getActiveId(_actIndex);
					iLeft = $("#data_"+sActId).offset().left;
					doOne = true;
					
					startX = e.touches[0].pageX;
					moveX = undefined;
					direction = undefined;
				})
				
				pageSwiper.addCallback('TouchMove', function(s,e){
				
					if(doOne){
						var _s = s;
						var _actId = _s.activeIndex;
						var nLeft = $("#data_"+sActId).offset().left;
						
						if(nLeft < iLeft){
							/*if(_pageId == arrLink.length-1){
								//$.showPrompt("已经是最后一版");
							}*/
							direction = "next";
							loadSwipe.createNextBlank(direction);

						}
						else if(nLeft > iLeft){
								
							/*if(_pageId == 0){
								$.showPrompt("已经是第一页");
							}
							*/
							direction = "pre";
							loadSwipe.createPreBlank(direction);

						}
						
						moveX = e.touches[0].pageX;
						
						
						doOne = false;
					}
				});
				pageSwiper.addCallback('TouchEnd', function(s,e){
				
					if(e.type != "touchend"){
						return;
					}
					
					
					//添加页面点击翻页事件
					var isLeftClick,isRightClick,isShowClick;
					if(moveX == undefined){
						if (startX < bookBase.viewWidth/3){
							isLeftClick = true;
							isRightClick = false;
							isShowClick = false;
						}
						else if (startX >= bookBase.viewWidth/3 && startX <= bookBase.viewWidth*2/3){
							isLeftClick = false;
							isRightClick = false;
							isShowClick = true;
						}  
						else if (startX > bookBase.viewWidth*2/3){
							isLeftClick = false;
							isRightClick = true;
							isShowClick = false;
						}
					}

					
					
					
					
					var _s = s;
					if(direction == "next" || isRightClick){
						loadSwipe.loadNextSlider("next");
						if(isRightClick){
							setTimeout(function(){
								console.log("swipeNext")
								_s.swipeNext();
							},200)
						}
						$(".bottom").hide();
						
					}
					else if(direction == "pre" || isLeftClick){
						loadSwipe.loadPreSlider("pre");
						if(isLeftClick){
							setTimeout(function(){
								_s.swipePrev();
							},200)
						}
						$(".bottom").hide();
					}

					else if(isShowClick){
						$(".bottom").toggle();
					}
					
					
				});
			};
			$("#changeBtn").click(function(){
				location.href = "/r2k/wx/ebook.do?metaid="+bookBase.getQueryString("metaid")+"&pageid="+pageId+'&orgid=<s:property value="orgid"/>';
			});
			
		});
		
		
	</script>
	
    

</head>
<body>
<div class="swiper-container" id="swipeBox">
  <div class="swiper-wrapper">
     
  </div>  
</div>

<div class="bottom" style="display:none">
	<a href='javascript:'  class="catabtn"  id="changeBtn">版式</a>
	<a href='/r2k/wx/getCatalogRow.do?metaid=<s:property value="metaid"/>&type=liu&orgid=<s:property value="orgid"/>'  class="catabtn">目录</a>
</div>

<div class="tempdata"></div>
<div style=" display:none; position:absolute; top:0; left:0; z-index:100; background:red; width:100px; height:200px;word-break:break-all;word-wrap:break-word; overflow:hidden; opacity:0.5" id="testfld"></div>
</body>
</html>
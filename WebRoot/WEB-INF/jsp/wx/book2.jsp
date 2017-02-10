<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
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
		var pageid=<s:property value="pageid" escape="false"/>;
		var originId=<s:property value="pageid" escape="false"/>;
		var pageCount = <s:property value="PageTotalCount"/>;
		var bookBase = {
			viewWidth: $(window).width(),
			viewHeight: $(window).height(),

			getHtml: function(){
				var htmlurl = "/r2k/wx/getHtml.do?metaid="+this.getQueryString("metaid")+"&orgId=swhy&userName=swhy03&rights=1-0_00"+"&width="+this.viewWidth+"&height="+this.viewHeight;
				return htmlurl;
			},
			getCss: function(){
				var cssurl = "/r2k/wx/getCss.do?metaid="+this.getQueryString("metaid")+"&orgId=swhy";
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
			$.ajax({
				url : _link,
				success : function(responseData) {
					callback(responseData);
				},
				error: function (XMLHttpRequest, textStatus, errorThrown) {
					//$.showPrompt("数据加载异常，请重试");
					console.log("网络异常！"+errorThrown);
				}
			});
		};

		var isAjaxing = false;
		$.BufferData = function(obj, params){
			var _dataBox = obj;
			var _params = params;
			
			var firstIndex = 0;
			var lastIndex = 0;
			this.getFstIndex = function(){
				return firstIndex;
			}
			this.getLastIndex = function(){
				return lastIndex;
			}
			
			
			this.preBuffer = function(index, pid, callback){
				isAjaxing = true;
				var _dataDiv = createDiv(index, pid);
				getData(pid, function(data){
					_dataDiv.html(data);
					_dataBox.prepend(_dataDiv);
					firstIndex = preSplitPage(index);
					if(callback){
						callback();
					}
					isAjaxing = false;
				})
				
				//return firstIndex;
			}
			this.nextBuffer = function(index, pid, callback){
				isAjaxing = true;
				var _dataDiv = createDiv(index, pid);
				
				getData(pid, function(data){
					_dataDiv.html(data);
					_dataBox.append(_dataDiv);
					lastIndex = nextSplitPage(index);
					if(callback){
						callback();
					}
					isAjaxing = false;
				})
				//return lastIndex;
				
			}
			var createDiv = function(index, id){
				var _dataIndex = index;
				//var _pageid = id;
				var dataDiv = $("<div></div>");
				dataDiv.attr("id","data_"+_dataIndex);
				return dataDiv;
			}
			var getData = function(pid, callback){
				var _pageId = pid;
				$.ajaxData(bookBase.getHtml()+"&page="+_pageId, function(data){
					callback(data);
			
				})
			}
			
			
			
			
			var preSplitPage = function (index){
				var bufferIndex = index;
				var $booksView = $("#data_"+ bufferIndex);
				
				while (1) {			
					var $newPage = $.jPageView.splitLastNode($booksView, function($e) {
						//比较函数，完全包含返回-1，相交返回0，相离返回1
						var split = -1;
						
						if ($e.offset().bottom < $booksView.offset().s_bottom - bookBase.viewHeight -30) {
							split = 1;
						} else if ($e.offset().top < $booksView.offset().s_bottom - bookBase.viewHeight-30) {
							split = 0;
						}
						return split;
					});
					if ($newPage == null){
						break;
					}
					if($booksView.children().length == 0){
						$booksView.html($newPage.html()) ;
						$newPage.remove();
						break;
					}	
					bufferIndex--;

					$newPage.attr("id","data_"+ bufferIndex);

					_dataBox.prepend($newPage);
					$booksView = $newPage;
				}
				return bufferIndex;
			
			}
			var nextSplitPage = function (index){
				var bufferIndex = index;
				var $booksView = $("#data_"+ bufferIndex);
					
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
					
					bufferIndex++;
					if ($newPage == null ){}
						break;

					if( $booksView.children().length == 0){
						$booksView.html($newPage.html()) ;
						$newPage.remove();
						break;
					}
					
					$newPage.attr("id","data_"+bufferIndex);

					
					_dataBox.append($newPage);
					$booksView = $newPage;
				}
				return bufferIndex;
			}
			
		}
		$.PageView = function(obj, params){
			var _box = obj;
			var _params = params;
			
			
			this.onFstInit = function(num){
			    for(var i=0; i<num; i++){
					var newDiv = createDiv(i);
					_box.append(newDiv);
					this.fillData(i);
				}
				var _class = "."+_box.parent().attr("class");
				var swiperPicture= new Swiper(_class,{});
				
				return swiperPicture;
			}
			this.fillData = function(pindex){
				var _pageIndex = pindex;
				//成功加载
				$("#page_"+ _pageIndex).html( $("#data_"+ _pageIndex).html() );
				/*
				if(_pageIndex < bindex){
					
				}
				
				else{
					//无数据可取，缓存区需要加载新数据
					
					callback(_pageIndex);
					return true;
				}
				*/
				
			}
			
			var createDiv = function(index){
				var _pageIndex = index;
				
				var dataDiv = $("<div class='swiper-slide'></div>");
				dataDiv.attr("id","page_"+_pageIndex);
				dataDiv.attr("data-index",_pageIndex);
				return dataDiv;
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
		
		
		
		$(function(){
			var headIndex,lastIndex = 0, //缓存区的最前、最后一个index
				frontPageId = pageid, backPageId = pageid-1, //缓存区的最前、最后一个pageid。
			    firstViewIndex,lastViewIndex,//显示区域的最前、最后一个index
				isLeft = undefined;
			var pageSwiper = null;
			

			var buffer = $(".tempdata").bufferData();
			var pView = $(".swiper-container .swiper-wrapper").pageView();
			
			//ajax获取css及html流式数据
			$.ajaxData(bookBase.getCss(), function(data){
				var _data = data;
				bookBase.addStyle(_data);
				
				
				var initNum = 3;
				$.getNewBuffer(initNum,function(){
					
					//显示3个pageview
					lastViewIndex = initNum-1;
					pageSwiper = pView.onFstInit(initNum);
					
					var iLeft,sActId;
					pageSwiper.addCallback('TouchStart', function(s){
						var _s = s;
						sActId = _s.activeIndex;
						iLeft = $("#page_"+sActId).offset().left;
					})
					pageSwiper.addCallback('TouchEnd', function(s){
						var _s = s;
						var _actId = _s.activeIndex;
						
						var nLeft = $("#page_"+sActId).offset().left;
						
						if(nLeft > iLeft){
							/*if(_pageId == 0){
								$.showPrompt("已经是第一页");
							}
							*/
							isLeft = true;
						}
						else if(nLeft < iLeft){
							/*if(_pageId == arrLink.length-1){
								//$.showPrompt("已经是最后一版");
							}*/
							isLeft = false;
						}
					})
					pageSwiper.addCallback('SlideChangeEnd', function(s){
						$.swiperChangeEnd(s);
					});
					
					
					
					
					
				
				});
			});
			
			//获取buffer数据
			$.getNewBuffer = function(num, callback){
				var _num = num;
				var beginTime = new Date(),endTime;
				var timer = setInterval(function(){initBuffer(_num)},200);
				function initBuffer(num){
					lastIndex = buffer.getLastIndex();
					if(!isAjaxing && lastIndex < num){
						backPageId++;
						buffer.nextBuffer(lastIndex, backPageId);
					}
					else if(!isAjaxing && lastIndex >= num){
						clearInterval(timer);
						callback();
						
					}
					
					endTime = new Date();
					if((endTime - beginTime) > 120000){
						alert("初始化获取数据超时！稍后重试");
						clearInterval(timer);
					}
				}
			}
			
			$.swiperChangeEnd = function(s){
				var _s = s;
				var _actId = _s.activeIndex;

				if(_actId == lastViewIndex && !isAjaxing){
					
					
					
					var creatSlider = function (){
						var newSlide = _s.createSlide('');
						//newSlide.html($("#data_"+lastViewIndex).html());
						newSlide.append();
						lastViewIndex++;
						$(newSlide).attr("id","page_"+lastViewIndex);
					}
					creatSlider();
					
					$.getNewBuffer(lastViewIndex+1, function(){
						pView.fillData(lastViewIndex);
						//creatSlider();
						
					});
					
				}


			};
			
			
						
			
		});
		
		//<div class="swiper-slide"></div>
	</script>
	
    

</head>
<body>
<div class="swiper-container" id="swipeBox">
  <div class="swiper-wrapper">
     
  </div>  
</div>

<div class="bottom" style="display:none">
	<a href='javascript:'  class="catabtn"  id="changeBtn">版式</a>
	<a href='/r2k/wx/getCatalogRow.do?metaid=<s:property value="metaid"/>&type=liu'  class="catabtn">目录</a>
</div>

<div class="tempdata"></div>
<div style=" display:none; position:absolute; top:0; left:0; z-index:100; background:red; width:100px; height:200px;word-break:break-all;word-wrap:break-word; overflow:hidden; opacity:0.5" id="testfld"></div>
</body>
</html>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" scope="page" />

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
	<head>
		<title>创建专题</title>
		<%@ include file="/commons/meta.jsp" %>
		<style type="text/css">
			#new_sort{
				width:40%;
			}
			#preview{
				filter: progid:DXImageTransform.Microsoft.AlphaImageLoader(sizingMethod=scale);
			}
		</style>
		<script >
			var checkBlankRegex = /^[\s\S]+$/;	//非空正则表达式
			var checkNumRegex = /^[1-9]\d*$/;	//数字正则表达式
			
			$(function() {	
				//信息提示框
		  	  	$( "#dialog-message" ).dialog({
					autoOpen: false,
					modal: true,
					buttons: {
						Ok: function() {
							$( this).dialog( "close" );
						}
					}
				});//end message
				
				//保存
			  	$( "#topicsave" )
			  	  .button()
			      .click(function(e) {
			    	  e.preventDefault();
			    	  var flag = checkValidate();
			    	  if(flag){
			    	  	$("#form-save").attr("action","${ctx}/topic/save.do").submit();
			    	  }
			      });
				
			  	//清空
			  	$( "#cleanTest" )
			      .button()
			      .click(function(e) {
			    	  e.preventDefault();
			    	  $(".field-input").val("");
			    	  $("#preview").attr("src","");
			      });
			      
			});//end init function
		
		//图片预览
     function setImagePreview() {
		var docObj = document.getElementById("image");

		var imgObjPreview = document.getElementById("preview");
		if (docObj.files && docObj.files[0]) {
			var localImagId = document.getElementById("iePreview");
			localImagId.style.display='none';
			//火狐下，直接设img属性
			imgObjPreview.style.display = 'block';
			imgObjPreview.style.width = '300px';
			imgObjPreview.style.height = '200px';
			//imgObjPreview.src = docObj.files[0].getAsDataURL();

			//火狐7以上版本不能用上面的getAsDataURL()方式获取，需要一下方式  
			imgObjPreview.src = window.URL.createObjectURL(docObj.files[0]);

		} else {
			//IE下，使用滤镜
			docObj.select();
			var localImagId = document.getElementById("iePreview");
			if(document.selection == null){
				imgObjPreview.src = "";
				return true;
			}
			localImagId.style.display='block';
			imgObjPreview.style.display = 'none';
			//必须设置初始大小
			localImagId.style.width = "300px";
			localImagId.style.height = "200px";
			//兼容IE9
			localImagId.focus();
			var imgSrc = document.selection.createRange().text;
			//图片异常的捕捉，防止用户修改后缀来伪造图片
			try {
				localImagId.style.filter = "progid:DXImageTransform.Microsoft.AlphaImageLoader(sizingMethod=scale)";
				localImagId.filters
						.item("DXImageTransform.Microsoft.AlphaImageLoader").src = imgSrc;
			} catch (e) {
				alert("您上传的图片格式不正确，请重新选择!");
				return false;
			}
			imgObjPreview.style.display = 'none';
			document.selection.empty();
		}
		return true;
	}
			//复选框选中更改隐藏域里对应的值
			function checkBoxChange(chkbox, hidden, msgname){
			   if($("#"+chkbox).is(":checked")){
				  $("#"+hidden).attr("value","1"); 
			   }else{
				 $("#"+hidden).attr("value","0"); 
			   }
			   $("#"+msgname).html("");
			}
			//获取焦点事件
			function focusIn(id){
				$("#"+id+"_msg").html("");
			}
			//保存验证
			function checkValidate(){
				var flag = true;
				var topicNameTxt = $("#new_topicName").attr("value");
				if(!checkBlankRegex.test(topicNameTxt)){
					$("#new_topicName_msg").text("*专题名称不能为空");
					flag = false;
				}
				var resultsTxt = $("#new_results").attr("value");
				if(resultsTxt != "" && !checkNumRegex.test(resultsTxt)){
					$("#new_results_msg").text("*专题结果必须为正整数");
					flag = false;
				}
				
				var queryTxt = $("#new_query").attr("value");
				if(!checkBlankRegex.test(queryTxt)){
					$("#new_query_msg").text("*生成条件不能为空");
					flag = false;
				}
				
				return flag;
			}
			
			function changeOrder(){
				var sort = $.trim($("#new_sort").val());
				if(sort != ''){
					var order = $("#order").val();
					$("#topicSort").val(sort+" "+order);
				}
			}
		</script>
	</head>
	<body>
		<%@ include file="/commons/header.jsp" %>
		<div class="wrapper">
			<div class="left">
				<%@ include file="/commons/menu.jsp" %>
			</div>
		<div id="navLink">
			<a href="/r2k/topic/showTopics.do" target="_self"><s:text name="r2k.menu.topic"></s:text></a> &gt; 创建专题 
		</div>
		<div class="content">
				<div id="xml-container" class="ui-widget" style="margin: 20px 0; width: 1000px">
					<form id="form-save" method="post" enctype="multipart/form-data">
					  	<div class="regbox">
					  		<div class="field"><label class="field-label">专题名称<span class="mstInput">*</span>:</label>
					       		<input class="ui-input field-input" name="topic.topicName" id="new_topicName" onfocus="focusIn('new_topicName')" />
					        	<span class="ui-tips-error" id="new_topicName_msg"></span>
						  	</div>
						  	<div class="field">
						  		<label class="field-label">生成结果数:</label>
					        	<input class="ui-input field-input" name="topic.results" id="new_results" onfocus="focusIn('new_results')" />
					        	<span class="ui-tips-error" id="new_results_msg"></span>
						  	</div>
						  	<div class="field">
						  		<label class="field-label">生成方式：</label>
						  		<input type="hidden" name="topic.autoCreate" id="autoCreate" value="0"/>
						  		<input type="hidden" name="topic.incrCreate" id="incrCreate" value="0"/>
						  		<input type="checkbox" id="autoChkbox" onclick="checkBoxChange('autoChkbox','autoCreate','new_autoCreate_msg');"/> 自动生成
						  		<input type="checkbox" id="incrChkbox" onclick="checkBoxChange('incrChkbox','incrCreate','new_autoCreate_msg');"/> 增量生成
					        	<span class="ui-tips-error" id="new_autoCreate_msg"></span>
						  	</div>
						  	<div class="field">
						  		<label class="field-label">专题类型：</label>
						  		<input type="radio" id="genRadio" name="topic.topicType" checked="checked" value="0"/> 普通
						  		<input type="radio" id="advRadio" name="topic.topicType" value="1"/> 高级
					        	<span class="ui-tips-error" id="new_topicType_msg"></span>
						  	</div>
						  	<div class="field">
						  		<label class="field-label">生成条件<span class="mstInput">*</span>:</label>
					        	<input class="ui-input field-input" name="topic.condition.query" id="new_query" onfocus="focusIn('new_query')" />
					        	<span class="ui-tips-error"  id="new_query_msg"></span>
						  	</div>
						  	<div class="field">
						  		<input id="topicSort" type="hidden" name="topic.condition.sort" />
						  		<label class="field-label">排序条件:</label>
					        	<input class="ui-input  field-input" id="new_sort" onfocus="focusIn('new_sort')" onblur="changeOrder()" />
					        	<select class="ui-input" id="order" onchange="changeOrder()">
					        		<option value="asc">升序</option>
					        		<option value="desc">降序</option>
					        	</select>
					        	<span class="ui-tips-error" id="new_sort_msg"></span>
						  	</div>
						  	<div class="field">
						  		<label class="field-label">专题描述:</label>
					        	<input class="ui-input field-input" name="topic.description" id="new_description" onfocus="focusIn('new_description')" />
					        	<span class="ui-tips-error" id="new_description_msg"></span>
						  	</div>
						  	<div class="field">
						  		<label class="field-label">专题图标:</label>
					        	<input class="ui-input field-input" type="file" name="icon" id="image" onchange="javascript:setImagePreview(this);">
					       		<span class="ui-tips-error" id="new_icon_msg"></span>
						  	</div>
						  	<div class="field">
						  		<label class="field-label">图标预览:</label>
						  		<div id="localImag">
						  			<div id="iePreview" style="display:none;"></div>
					        		<img id="preview" width="300px" height="200px" style="diplay: none" />
					        	</div>
						  	</div>
						  	<div class="field">
						  		<div class="form-button">
						  		<button id="topicsave">保存</button>
						  		<button id="cleanTest">清空</button>
						  		</div>
						  	</div>
						</div>
					</form>
				</div>
			</div>
		</div>
		<div class="footer"><%@ include file="/commons/footer.jsp" %></div>
	</body>
</html>
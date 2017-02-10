<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" scope="page" />

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
	<head>
		<title>添加图片</title>
		<%@ include file="/commons/meta.jsp" %>
		<link href="${ctx}/css/base/list.css" rel="stylesheet">
		<link href="${ctx}/css/colstyle.css" rel="stylesheet">
		<style type="text/css">
				#preview{
				filter: progid:DXImageTransform.Microsoft.AlphaImageLoader(sizingMethod=scale);
			}
		</style>
	
	</head>
	<body>
	<%@include file="/commons/header.jsp" %>
	<div class="wrapper">
			<div class="left">
			<%@include file="/commons/menu.jsp" %></div>
			<div id="navLink">
					<s:if test="deviceId != null">
					<a href="/r2k/dev/show.do" target="_self"><s:text name="r2k.menu.device"></s:text></a> &gt; <a href="/r2k/pub/device.do?deviceId=${deviceId}&deviceName=${deviceName}&devType=${devType}" target="_self"><s:text name="r2k.menu.pub"></s:text>(${deviceName})</a>&gt; 添加图片(${deviceName})
					</s:if>
					<s:else>
						<a href="/r2k/pub/${actionName}.do" target="_self"><s:text name="r2k.menu.pub"></s:text>(${devTypeName}) </a> &gt; 添加图片(${devTypeName})
					</s:else>
				</div>
		<div class="content">
			<div id="content-page">
					<div id="xml-container" class="ui-widget"
						style="margin: 20px 0; width: 1000px">
						<form id="form-col-save" action="/r2k/pub/savePic.do" enctype="multipart/form-data" method="post" >
							<input type="hidden" name="column.parentId" value="${parentId}">
							<input type="hidden" name="column.type" value="2">
							<input type="hidden" name="type" value="11">
							<input type="hidden" name="deviceId" value="${deviceId}">
							<input type="hidden" name="devType" value="${devType}">
							<input type="hidden" name="deviceName" value="${deviceName}" id=""/>
							<s:if test="deviceId != null">
							<input type="hidden" name="column.deviceId" value="${deviceId}" />
							</s:if>
							<s:else>
							<input type="hidden" name="column.deviceType" value="${devType}" />
							</s:else>
							<fieldset>
								<div class="field">
									<label class="field-label">
										<em style="color:red;">&nbsp;</em>名称:
									</label>
									<input class="ui-input field-input" name="column.title" id="title" />
									<span class="ui-tips-error" id="error-title"></span>
								</div>
								<div class="field">
									<label class="field-label">
										<em style="color:red;">&nbsp;</em>描述:
									</label>
									<input class="ui-input field-input" name="column.content" id="content" />
									<span class="ui-tips-error" id="error-content"></span>
								</div>
								<div class="field">
									<label class="field-label">
										&nbsp;&nbsp;&nbsp;上传图片:
									</label>
									<input class="ui-input field-input" type="file" name="image" id="image" onchange="javascript:setImagePreview(this);">
								</div>
								<div class="field">
									<label class="field-label">
										&nbsp;&nbsp;&nbsp;图片预览:
									</label>
									<div id="localImag">
										<div id="iePreview" style="display:none;"></div>
										<img id="preview" width="300px" height="200px" style="diplay: none" />
									</div>
								</div>
							</fieldset>
							<p style="text-align: center;">
								<button id="button-save">保存</button>
							</p>
						</form>
					</div>
				</div>
			</div>
		</div>
		<div class="footer">
			<%@include file="/commons/footer.jsp" %>
		</div>
	<script type="text/javascript">
		$(function() {
			//保存
		  	$( "#button-save" )
		      .button()
		      .click(function(e) {
		    	  e.preventDefault();
		    	  upload();
	  		  });
		});
	function upload() {
		var titleStr = $("#title").val();
		var content = $("#content").val();
		if (titleStr == null || titleStr == "") {
			$("#error-title").text("标题不能为空");
		} else if (titleStr.length > 40) {
			$("#error-title").text("标题不能超过40字");
		} else if (content.length > 200) {
			$("#error-content").text("描述不能超过200字");
		} else {
			$("form:first").submit();
		}
	};

//图片预览
		function setImagePreview() {
		var docObj = document.getElementById("image");

		var imgObjPreview = document.getElementById("preview");
		if (docObj.files && docObj.files[0]) {
			if (docObj.files[0].type == "image/png" || docObj.files[0].type == "image/jpeg" || docObj.files[0].type == "image/bmp"
				|| docObj.files[0].type == "image/gif") {
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
				alert("您上传的图片格式不正确，请重新选择!");
				docObj.value = '';
			}

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
				docObj.outerHTML += '';
				return false;
			}
			imgObjPreview.style.display = 'none';
			document.selection.empty();
		}
		return true;
	}
</script>
	</body>
</html>
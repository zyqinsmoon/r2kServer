<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" scope="page" />
<%
	pageContext.setAttribute("random",Math.random());
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
	<head>
		<title>栏目更新</title>
		<%@ include file="/commons/meta.jsp" %>
		<link href="${ctx}/css/base/list.css" rel="stylesheet">
		<link href="${ctx}/css/colstyle.css" rel="stylesheet">
		<style type="text/css">
			#preview{
				filter: progid:DXImageTransform.Microsoft.AlphaImageLoader(sizingMethod=scale);
			}
		</style>
		<script>
	$(function() {
		// 微调控制项
		var spinner = $(".spinner").spinner();
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
		var summaryStr = $("#summary").val();
		if (titleStr == null || titleStr == "") {
			$("#error-title").text("标题不能为空");
		} else if (titleStr.length > 40) {
			$("#error-title").text("标题不能超过40字");
		} else if (summaryStr.length > 200) {
			$("#error-summary").text("摘要不能超过200字");
		} else {
			<s:if test="(deviceId == null && devType != 'ORG') || deviceId != null">
			var templateId = $("#template").val();
			if(templateId != null && templateId != ""){
			$("#colTemplate").val(templateId);
			$("form:first").submit();
			}
			</s:if>
			<s:else>
			$("form:first").submit();
			</s:else>
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
	</head>

	<body>
	 <%@ include file="/commons/header.jsp" %>
		<div class="wrapper">
			<div class="left">
				<%@ include file="/commons/menu.jsp" %>
			</div>
			<div id="navLink">
					<s:if test="deviceId != null">
					<a href="/r2k/dev/show.do" target="_self"><s:text name="r2k.menu.device"></s:text></a> &gt; <a href="/r2k/pub/device.do?deviceId=${deviceId}&deviceName=${deviceName}&devType=${devType}" target="_self"><s:text name="r2k.menu.pub"></s:text>(${deviceName})</a>&gt; 更新栏目(${deviceName})
					</s:if>
					<s:else>
						<a href="/r2k/pub/${actionName}.do" target="_self"><s:text name="r2k.menu.pub"></s:text>(${devTypeName}) </a> &gt; 更新机构公用栏目(${devTypeName})
					</s:else>
				</div>
		<div class="content">
			<div id="content-page">
					<div id="xml-container" class="ui-widget"
						style="margin: 20px 0; width: 1000px">
						<form id="form-col-update" enctype="multipart/form-data" method="post" action="/r2k/pub/updateArtCol.do">
							<input type="hidden" name="devType" value="${devType}" id="">
							<input type="hidden" name="deviceId" value="${deviceId}" id="">
							<input type="hidden" name="deviceName" value="${deviceName}" id=""/>
							<input type="hidden" name="column.id" value="${column.id}">
							<input type="hidden" name="column.type" value="${column.type}">
							<input type="hidden" name="column.sort" value="${column.sort}">
							<input type="hidden" name="column.image" value="${column.image}">
							<s:if test="(deviceId == null && devType != 'ORG') || deviceId != null">
							<input type="hidden" name="column.infoTemplate.id" id="colTemplate-id" value="${column.infoTemplate.id}">
							<input type="hidden" name="column.infoTemplate.setNo" id="colTemplate-setNo" value="${column.infoTemplate.setNo}">
							<input type="hidden" name="templateId" id="colTemplate">
							</s:if>
							<s:if test="column.deviceId != null">
								<input type="hidden" name="column.deviceId" value="${column.deviceId}" />
							</s:if>
							<s:else>
								<input type="hidden" name="column.deviceType" value="${column.deviceType}" />
							</s:else>
							<fieldset>
								<div class="field">
									<label class="field-label">
										<em style="color:red;">*&nbsp;</em>标题:
									</label>
									<input class="ui-input field-input" id="title" name="column.title"
										value="${column.title}" />
									<span class="ui-tips-error" id="error-title"></span>
								</div>
								<s:if test="devType != 'ORG' || deviceId != null">
								<div class="field">
									<label class="field-label">
										&nbsp;&nbsp;&nbsp;选择模板：
									</label>  
									<s:if test="infoTemplates != null && infoTemplates.size > 0">
									<select id="template">
										<s:iterator value="infoTemplates" var="temp">
											<s:if test="#temp.id == column.infoTemplate.id">
												<option value="${temp.id}" selected="selected">${temp.name}</option>
											</s:if>
											<s:else>
												<option value="${temp.id}">${temp.name}</option>
											</s:else>
										</s:iterator>
									</select>
									</s:if>
									<s:else>
									<span class="ui-tips-error" id="show-temp">当前模板不包含文章栏目模板，请更换模板后再添加!</span>
									</s:else>
									<span class="ui-tips-error" id="error-temp"></span>
								</div>
								</s:if>
								<div class="field">
									<label class="field-label">
										&nbsp;&nbsp;&nbsp;摘要：
									</label>
									<textarea class="ui-input field-input" name="column.summary"
										id="summary" rows="10" style="float: none;">${column.summary}</textarea>
									<span class="ui-tips-error" id="error-summary"></span>
								</div>
								<div class="field">
									<label class="field-label">
									&nbsp;&nbsp;&nbsp;原图片：
									</label>
									<s:if test="column.image != null && !column.image.isEmpty()">
									<img width="300px" height="200px" src="${column.link}?random=${random}" style="diplay: none" />
									</s:if>
									<s:else>
									<img width="300px" height="200px" style="diplay: none" />
									</s:else>
								</div>
								<div class="field">
									<label class="field-label">
										&nbsp;&nbsp;&nbsp;替换图片:
									</label>
									<input class="ui-input field-input" type="file" name="image" id="image" onchange="javascript:setImagePreview(this);">
								</div>
								<div class="field">
									<label class="field-label">
										&nbsp;&nbsp;&nbsp;替换预览:
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
		<div class="footer"><%@ include file="/commons/footer.jsp" %></div>
	</body>
</html>
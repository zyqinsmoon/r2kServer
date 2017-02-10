<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" scope="page" />

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
	<head>
		<title>文章更新</title>
		<%@ include file="/commons/meta.jsp" %>
		<script type="text/javascript" src="${ctx}/js/swfupload/swfupload.js"></script>
		<script type="text/javascript" src="${ctx}/js/swfupload/handlers.js"></script>
		<link href="${ctx}/css/base/list.css" rel="stylesheet">
		<link href="${ctx}/css/colstyle.css" rel="stylesheet">
		<script>
		var delIds = new Array();
	$(function() {
		var array = "${column.image}".split(";");
		for ( var i = 0; i < array.length; i++) {
			if (array[i] != "") {
				$("#imageEdit")
						.append(
								"<span id='" + i + "'><img width='150' src='" + array[i] + 
						"'>&nbsp;&nbsp;<a href='javascript:remove("
										+ i
										+ ");' style='font-size: 1.4em;'> 删除</a>&nbsp;&nbsp;</span>");
			}
		}

		//保存
		$("#button-save").button().click(function(e) {
			e.preventDefault();
			upload();
		});
	});

	function remove(id) {
		/*var str = "#" + id;
		$(str).remove();*/
		var str = "#img-" + id;
		$(str).remove();
		$("#form-list").append("<input type='hidden' name='delIds["+delIds.length+"]' value='"+id+"'/>");
		delIds[delIds.length]=id;
	};

	var picChkMsg = "";
	function upload() {
		var titleStr = $("#title").val();
		var summaryStr = $("#summary").val();
		if (titleStr == null || titleStr == "") {
			$("#error-title").text("标题不能为空");
		} else if (titleStr.length > 40) {
			$("#error-title").text("标题不能超过40字");
		} else if (summaryStr.length > 200) {
			$("#error-summary").text("摘要不能超过200字");
		} else if(!checkPicDesc()){
			$("#error-pic").text(picChkMsg);
		} else {
			<s:if test="(deviceId == null && devType != 'ORG') || deviceId != null">
			var selTemp = $("#template").val();
			var templateId = -1;
			if(selTemp != null){
			var selId = $("#template").val();
			templateId = $("#"+selId).attr("tempId");
			//$("#colTemplate").val(templateId);
			}else{
			templateId = $("#showTemp").attr("templateId");
			}
			if(templateId != -1){
			$("#newTempId").val(templateId);
			$(".pic_col").each(function(i){
				var id = $(this).find(".pic_id").val();
				var name = $(this).find(".pic_name").val();
				var description=$(this).find(".pic_desc").val();
				$("#form-list").append("<input type='hidden' name='pictures["+i+"].name' value='"+name+"'/>");
				$("#form-list").append("<input type='hidden' name='pictures["+i+"].description' value='"+description+"'/>");
				$("#form-list").append("<input type='hidden' name='pictures["+i+"].id' value='"+id+"'/>");
			});
			$(".add_pic_col").each(function(i){
				var path=$(this).find("div.upload_img img").attr("src");
				var name=$(this).find("div#upload_info .imgname").val();
				var description=$(this).find("div#upload_info .imgdesc").val();
				$("#form-list").append("<input type='hidden' name='newPictures["+i+"].name' value='"+name+"'/>");
				$("#form-list").append("<input type='hidden' name='newPictures["+i+"].description' value='"+description+"'/>");
				$("#form-list").append("<input type='hidden' name='newPictures["+i+"].path' value='"+path+"'/>");
			});
			$("form:first").submit();
			}
			</s:if>
			<s:else>
			$(".pic_col").each(function(i){
				var id = $(this).find(".pic_id").val();
				var name = $(this).find(".pic_name").val();
				var description=$(this).find(".pic_desc").val();
				$("#form-list").append("<input type='hidden' name='pictures["+i+"].name' value='"+name+"'/>");
				$("#form-list").append("<input type='hidden' name='pictures["+i+"].description' value='"+description+"'/>");
				$("#form-list").append("<input type='hidden' name='pictures["+i+"].id' value='"+id+"'/>");
			});
			$(".add_pic_col").each(function(i){
				var path=$(this).find("div.upload_img img").attr("src");
				var name=$(this).find("div#upload_info .imgname").val();
				var description=$(this).find("div#upload_info .imgdesc").val();
				$("#form-list").append("<input type='hidden' name='newPictures["+i+"].name' value='"+name+"'/>");
				$("#form-list").append("<input type='hidden' name='newPictures["+i+"].description' value='"+description+"'/>");
				$("#form-list").append("<input type='hidden' name='newPictures["+i+"].path' value='"+path+"'/>");
			});
			$("form:first").submit();
			</s:else>
		}
	};

	function checkPicDesc(){
		var chk = true;
		$(".add_pic_col").each(function(i){
			var name=$(this).find("div#upload_info .imgname").val();
			if(name.length > 20){
				chk = false;
				picChkMsg = "图片"+(i+1)+"名称多于20个字";
				return chk;
			}
			var description=$(this).find("div#upload_info .imgdesc").val();
			if(description.length > 200){
				chk=false;
				picChkMsg = "图片"+(i+1)+"描述多于200个字";
				return chk;
			}
		});
		return chk;
	}
	
	var swfu;
	window.onload = function() {
		swfu = new SWFUpload(
				{
					upload_url : "${ctx}/pub/upload.do",
					file_post_name : "file",
					// File Upload Settings
					file_size_limit : "50 MB", // 1000MB
					file_types : "*.jpg;*.gif;*.jpeg;*.png",//设置可上传的类型
					file_types_description : "所有文件",
					file_upload_limit : "0",

					file_queue_error_handler : fileQueueError,//选择文件后出错
					file_dialog_complete_handler : fileDialogComplete,//选择好文件后提交
					file_queued_handler : picQueued,
					upload_progress_handler : uploadProgress,
					upload_error_handler : uploadError,
					upload_success_handler : uploadPicSuccess,
					upload_complete_handler : uploadComplete,

					// Button Settings
					button_image_url : "${ctx}/images/swfupload/SmallSpyGlassWithTransperancy_17x18.png",
					button_placeholder_id : "spanButtonPlaceholder",
					button_width : 100,
					button_height : 19,
					button_text : '<span class="button">上传图片</span>',
					button_text_style : '.button { font-family: Helvetica, Arial, sans-serif; font-size: 14pt; } .buttonSmall { font-size: 14pt; }',
					button_text_top_padding : 0,
					button_text_left_padding : 18,
					button_window_mode : SWFUpload.WINDOW_MODE.TRANSPARENT,
					button_cursor : SWFUpload.CURSOR.HAND,

					// Flash Settings
					flash_url : "${ctx}/js/swfupload/swfupload.swf",

					custom_settings : {
						upload_target : "divFileProgressContainer"
					},
					// Debug Settings
					debug : false
				//是否显示调试窗口
				});
	};
	function startUploadFile() {
		swfu.startUpload();
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
					<a href="/r2k/dev/show.do" target="_self"><s:text name="r2k.menu.device"></s:text></a> &gt; <a href="/r2k/pub/device.do?deviceId=${deviceId}&deviceName=${deviceName}&devType=${devType}" target="_self"><s:text name="r2k.menu.pub"></s:text>(${deviceName})</a>&gt; 更新文章(${deviceName})
					</s:if>
					<s:else>
						<a href="/r2k/pub/${actionName}.do" target="_self"><s:text name="r2k.menu.pub"></s:text>(${devTypeName}) </a> &gt; 更新文章(${devTypeName})
					</s:else>
				</div>
			<div class="content">
				<div id="content-page">
					<div id="xml-container" class="ui-widget"
						style="margin: 20px 0; width: 1000px">
						<form id="form-list" action="/r2k/pub/updateArt1.do" method="post"
							enctype="multipart/form-data">
							<input type="hidden" name="devType" value="${devType}" id="">
							<input type="hidden" name="deviceId" value="${deviceId}" id="">
							<input type="hidden" name="deviceName" value="${deviceName}" id=""/>
							<input type="hidden" name="oldImage" id="oldImage" value="" />
							<input type="hidden" name="newImage" id="newImage" value="" />
							<input type="hidden" name="column.type" value="${column.type}" />
							<input type="hidden" name="column.id" value="${column.id}" />
							<input type="hidden" name="column.sort" value="${column.sort}">
							<s:if test="devType != 'ORG' || deviceId != null">
							<s:if test="column.infoTemplate != null">
							<input type="hidden" name="column.infoTemplate.id" value="${column.infoTemplate.id }"/>
							</s:if>
							<input type="hidden" name="templateId" id="newTempId" value="${column.infoTemplate.id }"/>
							</s:if>
							<s:if test="column.deviceId != null">
								<input type="hidden" name="column.deviceId" value="${column.deviceId}" />
							</s:if>
							<s:else>
								<input type="hidden" name="column.deviceType" value="${column.deviceType}" />
							</s:else>
							<div class="field">
								<label class="field-label">
									<em style="color:red;">*&nbsp;</em>标题:
								</label>
								<input class="ui-input field-input" name="column.title" id="title"
									value="${column.title}" />
								<span class="ui-tips-error" id="error-title"></span>
							</div>
							<s:if test="devType != 'ORG' || deviceId != null">
							<div class="field">
								<label class="field-label">
									&nbsp;&nbsp;&nbsp;模板：
								</label>
								<s:if test="infoTemplates != null">
								<select id="template">
										<s:iterator value="infoTemplates" var="temp">
											<s:if test="#temp.id == column.infoTemplate.id">
											<option id="opt-${temp.id}" value="opt-${temp.id}" tempId="${temp.id}" selected="selected">${temp.name}</option>
											</s:if>
											<s:else>
											<option id="opt-${temp.id}" value="opt-${temp.id}" tempId="${temp.id}">${temp.name}</option>
											</s:else>
										</s:iterator>
								</select>
								</s:if>
								<s:elseif test="column.infoTemplate != null">
								<span id="showTemp" templateId="${column.infoTemplate.id}">${column.infoTemplate.name}</span>
								</s:elseif>
								<s:else>
								<span id="show-temp" templateId="-1" class="ui-tips-error">当前模板不包含文章模板，请返回资讯内容管理页选择模板，否则无法更新文章</span>
								</s:else>
								<span class="ui-tips-error" id="error-temp"></span>
							</div>
							</s:if>
							<div class="field">
								<label class="field-label">
									&nbsp;&nbsp;&nbsp;摘要：
								</label>
								<textarea class="ui-input field-input" id="summary"
									name="column.summary" rows="5" style="float: none;">${column.summary}</textarea>
								<span class="ui-tips-error" id="error-summary"></span>
							</div>
							<div class="field">
							<label class="field-label">
								&nbsp;&nbsp;&nbsp;正文内容：
							</label>
								<textarea class="ui-input field-input" name="column.content" rows="10"
									style="float: none;">${column.content}</textarea>
							</div>
							<div class="field">
								<label class="field-label">
									&nbsp;&nbsp;&nbsp;图片上传:
								</label>
								<div id="divFileProgressContainer" style="width: 200; display: none;"></div>
								<div id="thumbnails">
									<div id="infoDiv" style="margin-left: 8%; margin-right: 22%; border: solid 1px #aaaaaa; padding: 45px; margin-top: 8px; height: auto !important; min-height: 100px !important;" >
									<s:iterator value="column.columns" var="col" begin="0">
									<s:property value=''/>
										<div id="img-${col.id}" class='pic_col' style="border:solid #000000 1px;height:112px;margin-bottom:10px;">
											<input class="pic_id" type="hidden" value="${col.id}" />
											<div class="upload_img" style="float:left;margin-right:10px;">
												<img width="150" height="112px" src="${col.image}">
											</div>
											<div id="upload_info" class="upload_info" style="height:100%;padding-top:10px;">
												<div style="margin-bottom:10px;">
												<label>名称:</label><input class="pic_name" type="text" name="imgname" value="${col.title}" style="width:300px;"/>
												</div>
												<div style="margin-bottom:10px;">
												<label>描述:</label><input class="pic_desc" type="text" name="imgdesc" value="${col.content}" style="width:300px;"/>
												</div>
												<div>
												<a href="javascript:remove(${col.id});" style="font-size: 1.4em;"> 删除</a>
												</div>
											</div>
										</div>
									</s:iterator>
									</div>
								</div>
								<div id="error-pic" class="ui-tips-error"></div>
							</div>
							<div class="field">
								<span id="multiPicUpload">
								<span id="spanButtonPlaceholder"></span>
								</span>
								<button id="button-save">
									保存
								</button>
							</div>
						</form>
					</div>
				</div>
			</div>
		</div>
		<div id="loading"><img src="${ctx}/images/ui-anim_basic_16x16.gif" alt="" /> <span style="font-weight: bolder;">正在上传图片,请稍候...</span></div>
		<div class="footer"><%@ include file="/commons/footer.jsp" %></div>
	</body>
</html>
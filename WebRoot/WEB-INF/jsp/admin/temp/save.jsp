<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" scope="page" />

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
	<head>
		<title>模板添加</title>
		<%@ include file="/commons/meta.jsp" %>
		<meta http-equiv="pragma" content="no-cache">
		<meta http-equiv="cache-control" content="no-cache">
		<meta http-equiv="expires" content="0">
		<style type="text/css">
			#loading{
				position: absolute; 
				background-color: rgb(204, 204, 204); 
				z-index: 10; width: 100%; 
				left: 0px; top: 0px; 
				display: none; 
				opacity: 0.7; 
				height: 954px;
				padding-top: 20%;
				padding-left: 45%;
			}
		</style>
		<script type="text/javascript" src="${ctx}/js/admin/template.js"></script>
		<script type="text/javascript" src="${ctx}/js/swfupload/swfupload.js"></script>
		<script type="text/javascript" src="${ctx}/js/swfupload/handlers.js"></script>
	</head>

	<body>
	 <%@ include file="/commons/header.jsp" %>
		<div class="wrapper">
			<div class="left">
				<%@ include file="/commons/menu.jsp" %>
			</div>
			<div id="navLink">
				<a href="/r2k/temp/index.do" target="_self"><s:text name="r2k.menu.temp"></s:text></a>&gt;添加模板
			</div>
			<div class="content">
				<div id="content-page">
					<div id="xml-container" class="ui-widget" style="margin: 20px 0; width: 1000px">
						<form id="zip-info" method="post">
							<input type="hidden" name="templateSet.deviceType" id="tempDevType">
							<input type="hidden" name="templateSet.defaultType" id="tempDefaultType">
							<s:if test="#session.SPRING_SECURITY_CONTEXT.Authentication.Principal.authUser.currentOrg.orgId =='apabi'">
								<input type="hidden" id="currentScope" name="templateSet.scope" value="0">
							</s:if>
							<s:else>
								<input type="hidden" id="currentScope" name="templateSet.scope" value="1">
							</s:else>
							<input type="hidden" name="tempListLenStr" id="templist">
							<div id="upload-btn" class="field">
								<label class="field-label">
									上传模板zip文件<em style="color: red;">*</em>：
								</label>
								<span id="spanButtonPlaceholder"></span>
								<div id="divFileProgressContainer" style="width: 200; display: none;"></div>
								<span class="ui-tips-error" id="error-name"></span>
							</div>
							<div id="list"></div>
							<div class="field">
								<label class="field-label">
									适用设备类型<em style="color: red;">*</em>：
								</label>
								<s:iterator value="deviceTypeMap.keySet()" status="type" var="key">
									<input name="devType" type="checkbox" value='<s:property value="#key"/>' onchange="js_check.opCheck(this)"/><s:property value="deviceTypeMap.get(#key).value"/>
								</s:iterator>
								<span class="ui-tips-error" id="error-name"></span>
							</div>
							<div class="field">
								<label class="field-label">
									是否默认：
								</label>
								<s:iterator value="deviceTypeMap.keySet()" status="type" var="key">
									<input name="defaultType" type="checkbox" value='<s:property value="#key"/>' disabled="true" /><s:property value="deviceTypeMap.get(#key).value"/>
								</s:iterator>
								<span class="ui-tips-error" id="error-name"></span>
							</div>
							<div class="field">
								<label class="field-label">
									描述：
								</label>
								<textarea id="desc" name="templateSet.description" rows="10" style="width: 35%;"></textarea>
								<span class="ui-tips-error" id="error-name"></span>
							</div>
							<p style="text-align: center;">
								<button type="button" id="button-save">
									保存
								</button>
								<button type="button" id="button-back">
									返回
								</button>
							</p>
						</form>
					</div>
				</div>
			</div>
		</div>
		<div id="loading"><img src="${ctx}/images/ui-anim_basic_16x16.gif" alt="" /> <span style="font-weight: bolder;">正在上传数据,请稍候...</span></div>
		<div id="dialog-message" title="提示信息">
			<p id="txt-message"></p>
		</div>
		<div class="footer"><%@ include file="/commons/footer.jsp" %></div>
		<script>
	$(function() {
		//保存
		$("#button-save").button().click(function(){
			if ($('#newSetNo').length == 0) {
				$("#txt-message").html("").html("请上传文件后保存！");
          		$( "#dialog-message" ).dialog("open");
          		return;
			} else if (!js_check.checkTxtLength('desc', 100)) {
				$("#txt-message").html("").html("描述超过字符限制，请重新输入！");
          		$( "#dialog-message" ).dialog("open");
          		return;
			} else {
				var nametxt = $.trim($('#templateNameId').attr('value'));
				if(nametxt == ''){
					$("#txt-message").html("").html("模板套名不能为空！");
	          		$( "#dialog-message" ).dialog("open");
	          		return;
				}
				//验证复选框
				var checkflag = js_check.isCheck();
				if(!checkflag){
					return;
				}
				//设备模板默认设备类型字串
				var defaultTypeTxt = '';
				var defaultChecknode = $('input[name="defaultType"]:checked');
				for(var i = 0, len = defaultChecknode.length; i < len; i++){
					defaultTypeTxt = defaultTypeTxt + $(defaultChecknode[i]).attr('value') + '#';
				}
				defaultTypeTxt = defaultTypeTxt.substring(0,defaultTypeTxt.length-1);
				$('#tempDefaultType').attr('value',defaultTypeTxt);
				//检查默认模板是否存在
				var orgId = $('#currentOrgId').attr('value');
				var scope = $('#currentScope').attr('value');
				var defaultflag = js_check.checkDefault(orgId, scope, null, defaultTypeTxt, checkDeal);
			}
		});
		//返回
		$("#button-back").button().click(function(){
			window.location = '${ctx}/temp/index.do';
		});
		$( "#dialog-message" ).dialog({
			autoOpen: false,
			modal: true,
			buttons: {
				Ok: function() {
					$( this).dialog( "close" );
					}
				}
			});
	});//end init

	//文件上传
	var swfu;
	var settings_object = {
					upload_url : "${ctx}/temp/uploadZip.do",
					file_post_name : "zip",
					// File Upload Settings
					file_size_limit : "50 MB", // 1000MB
					file_types : "*.zip",//设置可上传的类型
					file_types_description : "所有文件",
					file_upload_limit : "10",

					file_queue_error_handler : fileQueueError,//选择文件后出错
					file_dialog_complete_handler : fileDialogComplete,//选择好文件后提交
					file_queued_handler : fileQueued,
					upload_progress_handler : uploadProgress,
					upload_error_handler : uploadError,
					upload_success_handler : uploadSuccess,
					upload_complete_handler : uploadComplete,

					// Button Settings
					button_image_url : "${ctx}/images/swfupload/SmallSpyGlassWithTransperancy_17x18.png",
					button_placeholder_id : "spanButtonPlaceholder",
					button_width : 100,
					button_height : 19,
					button_text : '<span class="button">上传zip文件</span>',
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
	};
	
	settings_object.upload_success_handler = function(file, serverData){
		var result = eval('('+serverData+')');
		var data = result.filemap;
		if(data.status == -1){
			$("#txt-message").html("").html("上传zip包没有主页！");
      		$("#dialog-message").dialog("open");
		}else if(data.status == -2){
			$("#txt-message").html("").html("上传zip包存在文件名命名错误的文件！");
      		$("#dialog-message").dialog("open");
		}else if(data.status == -3){
			$("#txt-message").html("").html("上传zip包存在子页缺子页对应的默认模板！");
      		$("#dialog-message").dialog("open");
		}else if(data.status == -4){
			$("#txt-message").html("").html("上传zip包未包含默认模板！");
      		$("#dialog-message").dialog("open");
		} else if(data.status == 1){
			$('#upload-btn').hide();
			var setno, setname;
			var disableStyle = '#f0f0f0';
			$.each(data, function (key, value) {  
				if(key.indexOf('setno') >= 0){
					setno = key.substring(key.indexOf('_')+1);
				} else if(key.indexOf('setname') >= 0){
					setname = key.substring(key.indexOf('_')+1);
				}
	        });  
			var contain = $('#list');
			var divnode = $('<div>').attr('class','field');
			var descnode = $('<label>').html('上传文件列表：').css({'width':'13%','margin-left':'10%'}); 
			$(divnode).append(descnode);
			$(contain).append(divnode);
			//原文件名模块
			var node = $('<div>').attr('class','field');
			var labelnode = $('<label>').html('文件名：').attr('class','field-label'); 
			var setinputnode = $('<input>').attr('class','ui-input').attr({'value': setname,'name':'templateSet.setName','id':'templateNameId'});
			$(node).append(labelnode).append(setinputnode);
			var spannode = $('<span>').attr('class','ui-tips-error'); 
			$(node).append(spannode);
			$(contain).append(node);
			//套id模块
			var node = $('<div>').attr('class','field');
			var labelnode = $('<label>').html('套id：').attr('class','field-label'); 
			var setinputnode = $('<input>').attr('class','ui-input').attr({'value': setno,'name':'templateSet.setNo','readonly':'readonly', 'id':'newSetNo'}).css('background-color',disableStyle);
			$(node).append(labelnode).append(setinputnode);
			var spannode = $('<span>').attr('class','ui-tips-error'); 
			$(node).append(spannode);
			$(contain).append(node);
			//非列表模板
			$.each(data, function (key, value) {  
				if(key.indexOf('setno') >= 0){
					setno = key.substring(key.indexOf('_')+1);
				} else if(key.indexOf('setname') >= 0){
					setname = key.substring(key.indexOf('_')+1);
				} else if(key == 'status'){
					
				} else {
					var node = $('<div>').attr({'class':'field','tempmark':'list'});
					var labelnode = $('<label>').html(data[key] + '：').attr('class','field-label'); 
					var modelinputnode = $('<input>').attr({'class':'ui-input', 'value': key, 'readonly':'readonly'}).css('background-color',disableStyle);
					$(node).append(labelnode).append(modelinputnode);
					var spannode = $('<span>').attr('class','ui-tips-error'); 
					$(node).append(spannode);
					$(contain).append(node);
				}
	        });  
		}//end data.status
	};
	window.onload = function() {
		swfu = new SWFUpload(settings_object);
	};
	//end fileupload
	
	//显示等待弹层
	function ShowDiv() {
       $("#loading").show();
	}
	
	function isNull(str) {
		if (str == "")
			return true;
		var regu = "^[ ]+$";
		var re = new RegExp(regu);
		return re.test(str);
	}
	
	//保存处理方法
	var checkDeal = function (){
		save();
	};
	//保存方法
	var save = function (){
		ShowDiv();
		//生成模板字串
		var templist = '';
		var tempdivlist = $('div[tempmark="list"]');
		for ( var i = 0; i < tempdivlist.length; i++) {
			var divnode = tempdivlist[i];
			var inputnode = $(divnode).find(':text');
			templist = templist + $(inputnode[0]).attr('value');
			templist = templist + '#';
		}
		templist = templist.substring(0,templist.length-1);
		$('#templist').attr('value',templist);
		//生成设备类型字串
		var devTypeTxt = '';
		var checknode = $('input[name="devType"]:checked');
		for(var i = 0, len = checknode.length; i < len; i++){
			devTypeTxt = devTypeTxt + $(checknode[i]).attr('value') + '#';
		}
		devTypeTxt = devTypeTxt.substring(0,devTypeTxt.length-1);
		$('#tempDevType').attr('value',devTypeTxt);
		//提交
		$("#zip-info").attr('action','${ctx}/temp/save.do').submit();
	};
</script>
	</body>
</html>
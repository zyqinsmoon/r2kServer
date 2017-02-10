<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" scope="page" />

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
	<head>
		<title>版本修改</title>
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
	</head>

	<body>
	 <%@ include file="/commons/header.jsp" %>
		<div class="wrapper">
			<div class="left">
				<%@ include file="/commons/menu.jsp" %>
			</div>
			<div id="navLink">
				<a href="/r2k/ver/index.do" target="_self">版本管理</a>&gt;修改版本
			</div>
			<div class="content">
				<div id="content-page">
					<div id="xml-container" class="ui-widget" style="margin: 20px 0; width: 1000px">
						<form id="" action="/r2k/ver/update.do" method="post" enctype="multipart/form-data">
							<input type="hidden" name="publish"/>
							<input type="hidden" name="id" value="${clientVersion.id}"/>
							<div class="field">
								<label class="field-label">
									版本号：
								</label>
								<input class="ui-input field-input" name="version" value="${clientVersion.version}" readonly="readonly"/>
							</div>
							<div class="field">
								<label class="field-label">修改文件：</label>
								<input class="ui-input field-input" type="file" name="client" id="client">
								<span class="ui-tips-error" id="error-file"></span>
							</div>
							<div class="field">
								<label class="field-label">
									备注：
								</label>
								<textarea class="ui-input field-input" name="description" rows="10">${clientVersion.description}</textarea>
							</div>
							<p style="text-align: center;">
								<button type="button" id="button-save">
									保存
								</button>
								<button type="button" id="button-publish">
									保存并发布
								</button>
							</p>
						</form>
					</div>
				</div>
			</div>
		</div>
		<div id="loading"><img src="${ctx}/images/ui-anim_basic_16x16.gif" alt="" /> <span style="font-weight: bolder;">正在上传数据,请稍候...</span></div>
		<div class="footer"><%@ include file="/commons/footer.jsp" %></div>
		<script>
	$(function() {
		//保存
		$("#button-save").button().click(function() {
			update();
		});
		//保存并发布
		$("#button-publish").button().click(function() {
			publish();
		});
	});
	
	function isNull(str) {
		if (str == "")
			return true;
		var regu = "^[ ]+$";
		var re = new RegExp(regu);
		return re.test(str);
	}
	
	function update(){
		var path = $("input[name='client']").val();
		if(isNull(path)){
			$("form:first").submit();
			ShowDiv();
		}else{
			var extension = path.substr(path.lastIndexOf(".")).toLowerCase();
			if(extension == '.apk'||extension == '.ipa'){
				$("form:first").submit();
				ShowDiv();
			}else{
				$("#error-file").text("文件格式必须为apk或ipa");
			}
		}
	}
	
	function publish(){
		var path = $("input[name='client']").val();
		if(isNull(path)){
			$("input[name='publish']").val('publish');
			$("form:first").submit();
			ShowDiv();
		}else{
			var extension = path.substr(path.lastIndexOf(".")).toLowerCase();
			if(extension == '.apk'||extension == '.ipa'){
				$("input[name='publish']").val('publish');
				$("form:first").submit();
				ShowDiv();
			}else{
				$("#error-file").text("文件格式必须为apk或ipa");
			}
		}
	}
	
	//显示等待弹层
	function ShowDiv() {
       $("#loading").show();
   }
</script>
	</body>
</html>
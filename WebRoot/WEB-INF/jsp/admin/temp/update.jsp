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
		<script type="text/javascript" src="${ctx}/js/admin/template.js"></script>
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
				<a href="/r2k/temp/index.do" target="_self"><s:text name="r2k.menu.temp"></s:text></a>&gt;修改模板(模板套id：${template.setNo}, 模板文件名：${template.type}.ftl)
			</div>
			<div class="content">
				<div id="content-page">
					<div id="xml-container" class="ui-widget" style="margin: 20px 0; "><!-- width: 1000px -->
						<form id="update-form" action="/r2k/temp/update.do" method="post" enctype="multipart/form-data">
						`	<input type="hidden" name="template.id" value="${template.id}" />
							<input type="hidden" name="template.path" value="${template.path}" />
							<div style="float: left; width: 40%;">
							<div class="field">
								<label class="field-label">
									文件名<em style="color: red;">*</em>：
								</label>
								<input class="ui-input field-input" value="${template.name}.ftl" readonly="readonly" style="background-color:#f0f0f0"/>
								<span class="ui-tips-error" id="error-name"></span>
							</div>
							<div class="field">
								<label class="field-label">
									模板套id<em style="color: red;">*</em>：
								</label>
								<input class="ui-input field-input" name="template.setNo" value="${template.setNo}" readonly="readonly"  style="background-color:#f0f0f0"/>
								<span class="ui-tips-error" id="error-name"></span>
							</div>
							<div class="field">
								<label class="field-label">
									模板类型<em style="color: red;">*</em>：
								</label>
								<input class="ui-input field-input" id="desc" name="template.type" value="${template.type}" readonly="readonly"  style="background-color:#f0f0f0"/>
								<span class="ui-tips-error" id="error-name"></span>
							</div>
							<div class="field">
								<label class="field-label">
									描述：
								</label>
								<textarea class="ui-input field-input" id="desc" name="template.description" rows="10">${template.description}</textarea>
							</div>
							</div>
							<div style="float: left; width: 58%;">
								在线修改模板：
								<textarea name="temptext" cols="110" rows="27" id="temptext" wrap="OFF" style="width: 100%">${temptext}</textarea>
							</div>
							<div style="clear: both;"></div>
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
		<div class="footer"><%@ include file="/commons/footer.jsp" %></div>
		<script>
	$(function() {
		//保存
		$("#button-save").button().click(function(){
			if(js_check.checkTxtLength('desc',200)){
				ShowDiv();
				$("#update-form").submit();
			}
		});
		//返回
		$("#button-back").button().click(function(){
			window.location = '${ctx}/temp/index.do';
		});
		
	});

	function isNull(str) {
		if (str == "")
			return true;
		var regu = "^[ ]+$";
		var re = new RegExp(regu);
		return re.test(str);
	}
	
	//显示等待弹层
	function ShowDiv() {
       $("#loading").show();
   }
</script>
	</body>
</html>
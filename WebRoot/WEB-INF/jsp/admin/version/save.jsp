<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" scope="page" />

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
	<head>
		<title>版本添加</title>
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
				<a href="/r2k/ver/index.do" target="_self">版本管理</a>&gt;添加版本
			</div>
			<div class="content">
				<div id="content-page">
					<div id="xml-container" class="ui-widget" style="margin: 20px 0; width: 1000px">
						<form id="" action="/r2k/ver/save.do" method="post" enctype="multipart/form-data">
							<input type="hidden" name="publish"/>
							<div class="field">
								<label class="field-label">
									设备类型：
								</label>
<%--								触摸屏--%>
<%--								<input type="hidden" name="devType" value="Android-Large#Android-Portrait">--%>
								<s:select list="select" name="devType" cssStyle="width: 50%;"></s:select>
							</div>
							<div class="field">
								<label class="field-label">
									版本号<em style="color: red;">*</em>:
								</label>
								<input class="ui-input field-input" name="version" onfocus="showTip();"/>
								<span id="versonTip">版本格式为#.#.#，#为数字</span>
								<span class="ui-tips-error" id="error-version"></span>
							</div>
							<div class="field">
								<label class="field-label">
									文件<em style="color:red">*</em>:
								</label>
								<input class="ui-input field-input" type="file" name="client">
								<span class="ui-tips-error" id="error-file"></span>
							</div>
							<div class="field">
								<label class="field-label">
									备注：
								</label>
								<textarea class="ui-input field-input" name="description" rows="10"></textarea>
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
		$("#button-save").button().click(function(){
			save();
		});
		//保存并发布
		$("#button-publish").button().click(function(){
			save('publish');
		});
	});

	function isNull(str) {
		if (str == "")
			return true;
		var regu = "^[ ]+$";
		var re = new RegExp(regu);
		return re.test(str);
	}
	//检查版本号格式是否正确
	function matchVersion(str){
		if (str == "")
			return false;
		var regu = "^[0-9]+.[0-9]+.[0-9]+$";
		var re = new RegExp(regu);
		return re.test(str);
	}
	
	function save(str){
		//判断版本号
		if (!matchVersion($("input[name='version']").val())) {
			$("#versonTip").text("");
			$("#error-version").text("版本格式为#.#.#，#为数字");
		}else{
			var version = $("input[name='version']").val();
			var devType = $("select[name='devType']").val();
			$.get("${ctx}/ver/checkVersion.do", {version: version,devType:devType},
        		function(data){
        			if(data == 1){
        				//如果不重复,判断上传文件
						if(isNull($("input[name='client']").val())) {
							$("#error-file").text("文件不能为空");
						} else {
							var path = $("input[name='client']").val();
							var extension = path.substr(path.lastIndexOf(".")).toLowerCase();
							if(extension == '.apk'||extension == '.ipa'){
								//是否需要发布
								if('publish' == str){
									$("input[name='publish']").val('publish');
								}
								$("form:first").submit();
								ShowDiv();
							}else{
								$("#error-file").text("文件格式必须为apk或ipa");
							}
						}
        			}else{
        				$("#versonTip").text("");
        				$("#error-version").text("该版本已经存在");
        			}
        	});
		}
	}
	
	//显示等待弹层
	function ShowDiv() {
       $("#loading").show();
   	}
	//显示提示
	function showTip(){
		$("#error-version").text("");;
		$("#versonTip").text("版本格式为#.#.#，#为数字");
	}
</script>
	</body>
</html>
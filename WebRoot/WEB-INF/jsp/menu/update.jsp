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
		<title>菜单编辑</title>
		<%@ include file="/commons/meta.jsp" %>
		<script type="text/javascript" src="${ctx}/js/admin/menu.js"></script>
		<script type="text/javascript" src="${ctx}/js/utils/file_utils.js"></script>
		<meta http-equiv="pragma" content="no-cache">
		<meta http-equiv="cache-control" content="no-cache">
		<meta http-equiv="expires" content="0">
		<script>
		$(function() {
			//保存
		  	$( "#button-save" ).button().click(function() {
				if(isNull($("input[name='title']").val())){
		  			$("#error-title").text("标题不能为空");	
		  		}else if($("input[name='title']").val()>20){
		  			$("#error-title").text("标题不能超过20字");	
		  		}else if(isNull($("input[name='link']").val())){
		  			$("#error-link").text("链接地址不能为空");
		  		}else{
		  			$("form:first").submit();
		  		}
	  		  });
		});
		
		function isNull( str ){ 
			if ( str == "" ) return true; 
			var regu = "^[ ]+$"; 
			var re = new RegExp(regu); 
			return re.test(str); 
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
					<a href="/r2k/dev/show.do" target="_self"><s:text name="r2k.menu.device"></s:text></a> &gt; <a href="/r2k/menu/device.do?deviceId=${deviceId}&deviceName=${deviceName}&devType=${devType}" target="_self">设备菜单管理 </a> &gt; 编辑${deviceName}菜单 
				</s:if>
				<s:else>
					<a href="/r2k/menu/${actionName}.do" target="_self">菜单管理(${devTypeName}) </a> &gt; 编辑菜单(${devTypeName})
				</s:else>
			</div>
			<div class="content">
			<div id="content-page">
				<div id="xml-container" class="ui-widget"
					style="margin: 20px 0; width: 1000px">
					<form id="" action="/r2k/menu/update.do" method="post" enctype="multipart/form-data">
						<input type="hidden" name="menu.id" value="${menu.id}"/>
						<input type="hidden" name="deviceId" value="${deviceId}"/>
						<input type="hidden" name="devType" value="${devType}"/>
						<input type="hidden" name="deviceName" value="${deviceName}" />
						<input type="hidden" name="menu.iconBackground" value="${menu.iconBackground}" />
						<input type="hidden" name="menu.normal" value="${menu.normal}" />
						<input type="hidden" name="menu.sort" value="${menu.sort}" />
						<input type="hidden" id="is_hide_menu" name="menu.hide" value="${menu.hide}" />
						<input type="hidden" id="is_home_page" name="menu.homePage" value="${menu.homePage}" />
						<s:if test="menu.deviceId != null">
							<input type="hidden" name="menu.deviceId" value="${menu.deviceId}"/>
						</s:if>
						<input type="hidden" name="menu.deviceType" value="${menu.deviceType}" />
						<div class="field">
							<input id="hide_menu" class="menu-input-radio" type="radio" onclick="Menu.setIsHideMenu()" <s:if test="menu.hide==0">checked="checked"</s:if> />隐藏本菜单
							<s:if test="deviceId != null">
							<input id="home_page" class="menu-input-radio" type="radio" onclick="Menu.setDevHomePage('${devType}','${deviceId}')" <s:if test="menu.homePage==1">checked="checked"</s:if>/>设置为主页
							</s:if>
							<s:else>
							<input id="home_page" class="menu-input-radio" type="radio" onclick="Menu.setOrgHomePage('${devType}')" <s:if test="menu.homePage==1">checked="checked"</s:if>/>设置为主页
							</s:else>
							<hr class="menu-hr"/>
						</div>
						<div class="field">
							<label class="field-label">
								标题<em style="color:red;">*</em>:
							</label>
							<input class="ui-input field-input" name="menu.title" value="${menu.title}" <s:if test="menu.menuType != 'link'">readonly="readonly"</s:if>/>
							<span class="ui-tips-error" id="error-title"></span>
						</div>
						<div class="field">
							<label class="field-label">
								链接地址<em style="color:red;">*</em>:
							</label>
							<input class="ui-input field-input" name="menu.link" value="${menu.link}" <s:if test="menu.menuType != 'link'">readonly="readonly"</s:if>/>
							<span class="ui-tips-error" id="error-link"></span>
						</div>
						<div class="field">
								<label class="field-label">
									菜单描述：
								</label>
								<textarea id="desc" class="ui-input field-input" rows="10" cols="50" name="menu.description" onchange="" <s:if test="menu.menuType != 'link'">readonly="readonly"</s:if>>${menu.description}</textarea>
								<span class="ui-tips-error" id="error-desc"></span>
							</div>
						<div class="field">
							<label class="field-label">
								菜单背景图片:
							</label>
							<input class="ui-input field-input" type="file" name="iconBackground" onchange="FileUtils.setImagePreview(this,'png',['png'],50);">
						</div>
						<div class="field">
							<label class="field-label">
								原图片：
							</label>
							<div>
								<s:if test="menu.iconBackground != null && !menu.iconBackground.isEmpty()">
								<img width="130px" height="130px" src="${menu.iconBackgroundPath}?random=${random}" style="diplay:none;float:left;" />
								</s:if>
								<s:else>
								<img width="130px" height="130px" style="diplay:none;float:left;" />
								</s:else>
							</div>
						</div>
						<div class="field">
							<label class="field-label">
								替换预览:
							</label>
							<div id="iconBackground-local">
								<div id="iconBackground-iePreview" style="display:none;"></div>
								<img id="iconBackground-preview" width="130px" height="130px" style="diplay: none" />
							</div>
						</div>
						<div class="field">
							<label class="field-label">
								菜单图标:
							</label>
							<input class="ui-input field-input" type="file" name="normal" onchange="FileUtils.setImagePreview(this,'png',['png'],50);">
						</div>
						<div class="field">
							<label class="field-label">
								原图片：
							</label>
							<div>
								<s:if test="menu.normal != null && !menu.normal.isEmpty()">
								<img width="130px" height="130px" src="${menu.normalPath}?random=${random}" style="diplay:none;float:left;" />
								</s:if>
								<s:else>
								<img width="130px" height="130px" style="diplay:none;float:left;" />
								</s:else>
							</div>
						</div>
						<div class="field">
							<label class="field-label">
								替换预览:
							</label>
							<div id="normal-local">
								<div id="normal-iePreview" style="display:none;"></div>
								<img id="normal-preview" width="130px" height="130px" style="diplay: none" />
							</div>
						</div>
						<p style="text-align: center;">
							<button type="button" id="button-save">保存</button>
						</p>
					</form>
				</div>
			</div>
		</div>
		</div>
		<div class="footer"><%@ include file="/commons/footer.jsp" %></div>
	</body>
</html>
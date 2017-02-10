<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib tagdir="/WEB-INF/tags/simpletable" prefix="simpletable"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" scope="page"/>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<title>创建角色</title>
		<%@ include file="/commons/meta.jsp" %>
	</head>
	<body>
		<!-- 页面头部 -->
		<%@ include file="/commons/header.jsp" %>
		<div class="wrapper">
			<div class="left">
				<%@ include file="/commons/menu.jsp" %>
			</div>
			<div id="navLink">
			</div>
			<div class="content">
				<div id="content-page">
					<div id="form-container" class="ui-widget" style="margin: 20px 0; width: 1000px">
						<form id="form-role-save" action="" method="post" enctype="multipart/form-data">
							<div class="field">
								<label class="field-label">
									<em style="color:red;">*&nbsp;</em>角色名称:
								</label>
								<input class="ui-input field-input" name="authRole.roleName" id="roleName" />
								<span class="ui-tips-error" id="error-role-name"></span>
							</div>
							<div class="field">
								<label class="field-label">
									<em style="color:red;">*&nbsp;</em>设备类型:
								</label>
								<select>
								</select>
								<span class="ui-tips-error" id="error-res-url"></span>
							</div>
							<div class="field">
								<label class="field-label">
									<em style="color:red;">*&nbsp;</em>角色类型:
								</label>
								<input type="radio" name="authRole.type" value="0">后台
								<input type="radio" name="authRole.type" value="1">前台
								<span class="ui-tips-error" id="error-entity"></span>
							</div>
							<div class="field">
								<label class="field-label">
									<em style="color:red;">*&nbsp;</em>菜单类型:
								</label>
								<select>
								</select>
								<span class="ui-tips-error" id="error-res-url"></span>
							</div>
							<div class="field">
								<label class="field-label">关联资源:</label>
								<!-- 可选资源 -->
								<div id="selectable-res">
								</div>
								<!-- 已选资源 -->
								<div id="selected-res">
								</div>
							</div>
							<div class="field">
								<button id="button-save">保存</button>
							</div>
						</form>
					</div>
				</div>
			</div>
		</div>
		<div class="footer"><%@ include file="/commons/footer.jsp" %></div>
	</body>
</html>
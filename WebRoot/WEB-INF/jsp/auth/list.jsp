<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib tagdir="/WEB-INF/tags/simpletable" prefix="simpletable"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" scope="page"/>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<title>权限模块列表</title>
		<%@ include file="/commons/meta.jsp" %>
		<style type="text/css">
			#table-expandable {margin: 0;border-collapse: collapse;width: 100%;}
			#table-expandable td,#table-expandable th {border: 1px solid #eee;padding: .6em 10px;text-align: left;}
			#table-expandable th {font-weight: bold;}
		</style>
		<script type="text/javascript" src="${ctx}/js/utils/dialog_utils.js"></script>
		<script type="text/javascript" src="${ctx}/js/auth/auth.js"></script>
		<script type="text/javascript">
			$(function(){
				//init save dialog
				js_auth.dlgSaveId = 'dialog-save-entity';
				js_auth.saveUrl = '';
				DialogUtils.initDialog(js_auth.dlgSaveId, false, 350, 700, true, js_auth.saveButtons, function(){ js_auth.close( js_auth.dlgSaveId ); }); 
				//init update dialog
				js_auth.dlgUpdateId = 'dialog-update-entity';
				js_auth.updateUrl = '';
				DialogUtils.initDialog(js_auth.dlgUpdateId, false, 350, 700, true, js_auth.updateButtons, function(){ js_auth.close( js_auth.dlgUpdateId );}); 
				//init save button
				$("#entity-save").button().click(function() {
			    	  $('#dialog-save-entity').dialog('open');
			      });
			});//end initfunction
		</script>
	</head>
	<body>
		<!-- 页面头部 -->
		<%@ include file="/commons/header.jsp" %>
		<div class="wrapper">
			<!-- 左侧导航菜单 -->
			<div class="left">
	  			<%@ include file="/commons/menu.jsp" %>
	  		</div>
	  		<!-- 面包屑导航 -->
	  		<div id="navLink">
				<a href="#" target="_self"><s:text name="r2k.menu.auth"></s:text></a>
	  		</div>
	  		<!-- 页面主体 -->
	  		<div class="content">
	  			<!-- 页面工具栏 -->
	  			<div id="toolbar" class="ui-widget-header ui-corner-all" style=" float:left; width:100%;">
	  				<button id="entity-save" style="float:left" >新建模块</button>
	  			</div>
	  			<!-- 页面主体列表 -->
	  			<div id="container" class="ui-widget">
	  				<form id="form-list" action="" method="post">
	  				<table id="table-expandable" class="ui-widget ui-widget-content gridBody">
	  					<thead>
	  						<tr class="ui-widget-header">
	  							<th>模块名</th>
	  							<th>最后更新时间</th>
	  							<th>模块描述</th>
	  							<th>显示顺序</th>
	  							<th>操作</th>
	  						</tr>
	  					</thead>
	  					<tbody>
	  						<s:iterator value="page.result" var="entity">
	  						<tr>
	  							<td>${entityName}</td>
	  							<td><s:date name="lastUpdate" format="yyyy-MM-dd"/></td>
	  							<td>${entityDesc}</td>
	  							<td class="tdSpinner">
	  								<input name="${id}" class="spinner" style="width: 30px" value="${sort}" min="1" max="999">
	  							</td>
	  							<td>
	  								<a href="#">[修改信息]</a>
	  								<a href="#">[资源管理]</a>
	  								<a href="#">[删除]</a>
	  							</td>
	  						</tr>
	  						</s:iterator>
	  					</tbody>
	  				</table>
	  				<!-- 页码 -->
	  				<simpletable:pageToolbar page="${page}"></simpletable:pageToolbar>
	  				</form>
	  			</div>
	  		</div>
		</div>
		
		<!-- ########页面弹框开始######## -->
		<!-- 新建权限模块弹框 -->
		<div id="dialog-save-entity" title="新建权限模块">
			<form action="" method="post">
				<div class="field">
					<label class="field-label">模块名称:</label>
					<input class="ui-input field-input" name="authEntity.entityName" id="new_entity_name" />
					<span class="ui-tips-error" id="new_entity_name_msg"></span>
				</div>
				<div class="field">
					<label class="field-label">模块描述:</label>
					<input class="ui-input field-input" name="authEntity.entityCode" id="new_entity_dese" />
					<span class="ui-tips-error" id="new_entity_dese_msg"></span>
				</div>
				<div class="field">
					<label class="field-label">模块顺序:</label>
					<input class="ui-input field-input" name="authEntity.order" id="new_entity_order" />
					<span class="ui-tips-error" id="new_entity_order_msg"></span>
				</div>
			</form>
		</div>
		
		<!-- 更新权限模块名称 -->
		<div id="dialog-update-entity" title="修改权限模块">
			<form action="" method="post">
				<div class="field">
					<label class="field-label">模块名称:</label>
					<input class="ui-input field-input" name="authEntity.entityName" id="update_entity_name" />
					<span class="ui-tips-error" id="update_entity_name_msg"></span>
				</div>
				<div class="field">
					<label class="field-label">模块描述:</label>
					<input class="ui-input field-input" name="authEntity.entityCode" id="update_entity_dese" />
					<span class="ui-tips-error" id="update_entity_name_msg"></span>
				</div>
				<div class="field">
					<label class="field-label">模块顺序:</label>
					<input class="ui-input field-input" name="authEntity.order" id="update_entity_order" />
					<span class="ui-tips-error" id="update_entity_name_msg"></span>
				</div>
			</form>
		</div>
		<!-- ########页面弹框结束######## -->
		<!-- 页面尾部 -->
		<div class="footer"><%@ include file="/commons/footer.jsp" %></div>
	</body>
</html>
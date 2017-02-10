<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib tagdir="/WEB-INF/tags/simpletable" prefix="simpletable"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" scope="page"/>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<title>角色列表</title>
		<%@ include file="/commons/meta.jsp" %>
		<style type="text/css">
			#table-expandable {margin: 0;border-collapse: collapse;width: 100%;}
			#table-expandable td,#table-expandable th {border: 1px solid #eee;padding: .6em 10px;text-align: left;}
			#table-expandable th {font-weight: bold;}
		</style>
		<script type="text/javascript" src="${ctx}/js/utils/dialog_utils.js"></script>
		<script type="text/javascript">
			$(function(){
				//init save dialog
				saveButtons ={};
				DialogUtils.initDialog('dialog-save-entity', false, 350, 700, true, saveButtons, function(){$('#dialog-save-entity').dialog("close");}); 
				//init update dialog
				updateButtons ={};
				DialogUtils.initDialog('dialog-update-entity', false, 350, 700, true, updateButtons, function(){$('#dialog-update-entity').dialog("close");}); 
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
	  				<button id="entity-save" style="float:left" >新建角色</button>
	  			</div>
	  			<!-- 页面主体列表 -->
	  			<div id="container" class="ui-widget">
	  				<form id="form-list" action="" method="post">
	  				<table id="table-expandable" class="ui-widget ui-widget-content gridBody">
	  					<thead>
	  						<tr class="ui-widget-header">
	  							<th>角色名称</th>
	  							<th>类型</th>
	  							<th>设备类型</th>
	  							<th>前台菜单类型</th>
	  							<th>是否启用</th>
	  						</tr>
	  					</thead>
	  					<tbody>
	  						<s:iterator value="page.result" var="role">
	  						<tr>
	  							<td>${roleName}</td>
	  						</tr>
	  						</s:iterator>
	  					</tbody>
	  				</table>
	  				</form>
	  			</div>
	  		</div>
		</div>
		<!-- 页面尾部 -->
		<div class="footer"><%@ include file="/commons/footer.jsp" %></div>
	</body>
</html>
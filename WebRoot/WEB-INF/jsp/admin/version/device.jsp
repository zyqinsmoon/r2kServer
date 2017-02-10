<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib tagdir="/WEB-INF/tags/simpletable" prefix="simpletable"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" scope="page" />

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
	<head>
		<title>版本管理</title>
		<%@ include file="/commons/meta.jsp" %>
		<meta http-equiv="pragma" content="no-cache">
		<meta http-equiv="cache-control" content="no-cache">
		<meta http-equiv="expires" content="0">
		
		<style type="text/css">
			#table-expandable {
				margin: 0;
				border-collapse: collapse;
				width: 100%;
			}
			
			#table-expandable td,#table-expandable th {
				border: 1px solid #eee;
				padding: .6em 10px;
				text-align: left;
			}
			
			#table-expandable th {
				font-weight: bold;
			}
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
	 <%@include file="/commons/header.jsp" %>
		<div class="wrapper">
			<div class="left">
				<%@include file="/commons/menu.jsp" %>
			</div>
			<div id="navLink">
				<a href="/r2k/ver/index.do" target="_self">版本管理</a>&gt;${orgId}设备版本
			</div>
			<div class="content">
				<div id="container" class="ui-widget">
					<div id="toolbar" class="ui-widget-header ui-corner-all">
					</div>
				<form id="form-list" action="/r2k/ver/device.do" method="post">
					<table id="table-expandable"
						class="ui-widget ui-widget-content gridBody">
						<thead>
							<tr class="ui-widget-header ">
								<th>
									设备名称
								</th>
								<th>
									设备ID
								</th>
								<th>
									设备类型
								</th>
								<th>
									所属机构
								</th>
								<th>
									现版本
								</th>
								<th>
									升级版本
								</th>
							</tr>
						</thead>
						<tbody>
							<s:iterator value="page.result">
								<tr>
									<td>${deviceName}</td>
									<td>${deviceId}</td>
									<td><s:property value="deviceTypeMap.get(deviceType).value"/></td>
									<td>${orgId}</td>
									<td>${curVersion}</td>
									<td>${toVersion}</td>
								</tr>
							</s:iterator>
						</tbody>
					</table>
					<simpletable:pageToolbar page="${page}"></simpletable:pageToolbar>
				</form>
			</div>
		</div>
		<div class="footer"><%@include file="/commons/footer.jsp" %></div>
		
		<script type="text/javascript">
			$(function(){
				//创建simpleTable
		  		var sortColumns = "<s:property value='#parameters.sortColumns'/>";
		  			if(sortColumns == null || sortColumns == ""){
		  				sortColumns = " d.ID desc";
		  			}
		  		window.simpleTable = new SimpleTable('form-list','${page.thisPageNumber}','${page.pageSize}',sortColumns);
			});
		</script>
	</body>
</html>
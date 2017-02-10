<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib tagdir="/WEB-INF/tags/simpletable" prefix="simpletable"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" scope="page" />

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
	<head>
		<title>版本管理</title>
		<%@include file="/commons/meta.jsp" %>
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
				<a href="/r2k/ver/index.do" target="_self">版本管理</a>&gt;${version}版本回滚
			</div>
			<div class="content">
				<div id="container" class="ui-widget">
					<div id="toolbar" class="ui-widget-header ui-corner-all">
					<button id="rise">回滚</button>
					</div>
				<form id="form-list" action="/r2k/ver/rollbackPage.do" method="post">
					<input name="id" value="${id}" type="hidden">
					<input name="version" value="${version}" type="hidden">
					<input name="devType" value="${devType}" type="hidden">
					<table id="table-expandable"
						class="ui-widget ui-widget-content gridBody">
						<thead>
							<tr class="ui-widget-header ">
								<th>
									<input onclick="selectAll();" type="checkbox" id="controlAll"/> 全选
								</th>
								<th>
									机构简称
								</th>
								<th>
									版本号
								</th>
								<th>
									更新时间
								</th>
								<th>
									操作
								</th>
							</tr>
						</thead>
						<tbody>
							<s:iterator value="page.result">
								<tr>
									<td><input name="checked" value="${id}" type="checkbox"></td>
									<td>${orgId}</td>
									<td>${version}</td>
									<td>
										<s:date name="lastDate" format="yyyy-MM-dd HH:mm:ss" />
									</td>
									<td>
										<a href="/r2k/ver/device.do?orgId=${orgId}">[查看设备版本]
									</td>
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
				
				$("#rise").button().click(function(){
					$("#form-list").attr("action","/r2k/ver/updateVersion.do");
					$("#form-list").submit();
				});
			});
			
			function selectAll(){
				 var checklist = document.getElementsByName("checked");
				   if(document.getElementById("controlAll").checked)
				   {
				   for(var i=0;i<checklist.length;i++)
				   {
				      checklist[i].checked = 1;
				   } 
				 }else{
				  for(var j=0;j<checklist.length;j++)
				  {
				     checklist[j].checked = 0;
				  }
				 }
				};
		</script>
	</body>
</html>
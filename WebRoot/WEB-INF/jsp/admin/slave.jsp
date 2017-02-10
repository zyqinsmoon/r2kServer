<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib tagdir="/WEB-INF/tags/simpletable" prefix="simpletable"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" scope="page" />
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
	<head>
		<title>子节点管理</title>
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
	 <%@ include file="/commons/header.jsp" %>
		<div class="wrapper">
			<div class="left">
				<%@ include file="/commons/menu.jsp" %>
			</div>
	    <div id="navLink">
			<a href="/r2k/dev/slave.do" target="_self">子节点管理</a>
	    </div>			
		<div class="content">
				<div id="container" class="ui-widget">
					<div id="toolbar" class="ui-widget-header ui-corner-all">
					</div>

					<form id="form-list" action="/r2k/dev/slave.do" method="post">
						<table id="table-expandable"
							class="ui-widget ui-widget-content gridBody">
							<thead>
								<tr class="ui-widget-header ">
									<th>设备ID</th>
									<th>机构ID</th>
									<th>是否在线</th>
									<th>最后在线时间</th>
									<th>操作</th>
								</tr>
							</thead>
							<tbody>
								<s:iterator value="page.result">
									<tr>
										<td>${deviceId}</td>
										<td>${orgId}</td>
										<td>${isOnline}</td>
										<td><s:date name="lastDate" format="yyyy-MM-dd HH:mm:ss"/></td>
										<td><a href="#" onclick="deleteMenu('${id}');">[删除]</a></td>
									</tr>
								</s:iterator>
							</tbody>
						</table>
						<simpletable:pageToolbar page="${page}"></simpletable:pageToolbar>
					</form>
				</div>
			</div>
		</div>
		<div id="dialogdelete" title="删除">
			<input id="deleteid" name="delete" type="hidden" />
			<p>
				确认删除？
			</p>
		</div>
		<div class="footer"><%@ include file="/commons/footer.jsp" %></div>
		<script type="text/javascript">
		$(function(){
			//创建simpleTable
			var sortColumns = "<s:property value='#parameters.sortColumns'/>";
			if(sortColumns == null || sortColumns == ""){
				sortColumns = " SORT asc";
			}
			window.simpleTable = new SimpleTable('form-list',${page.thisPageNumber},${page.pageSize},sortColumns);
			
			//设置删除对话框
		    $( "#dialogdelete" ).dialog({
		      autoOpen: false,
		      resizable: false,
		      height:140,
		      modal: true,
		      buttons: {
		        "确定": function() {
		        	var id = $("#deleteid").attr("value");
		        	$.get("${ctx}/dev/delete.do", {id: id},
		        		function(data){
		        		location.reload();
		        	});
		        },
		        "取消": function() {
		          $( this ).dialog( "close" );
		        }
		      }
		    });
		});
		
		 //打开删除对话框
		function deleteMenu(id){
	        $("#deleteid").attr("value", id);
			$("#dialogdelete").dialog( "open" );	 
	   }
	</script>
	</body>
</html>

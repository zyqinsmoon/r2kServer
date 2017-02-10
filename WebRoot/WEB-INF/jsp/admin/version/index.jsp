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
	 <%@ include file="/commons/header.jsp" %>
		<div class="wrapper">
			<div class="left">
				<%@ include file="/commons/menu.jsp" %>
			</div>
			<div id="navLink">
				<a href="/r2k/ver/index.do" target="_self">版本管理</a>
			</div>
			<div class="content">
				<div id="container" class="ui-widget">
					<div id="toolbar" class="ui-widget-header ui-corner-all">
					<button id="save">添加版本</button>
						<div style="float: right;margin-right:20px;">
						<form id="form-search" action="${ctx}/ver/index.do" method="post">
							<s:select cssClass="ui-input" id="devType" name="devType" list="select"  headerKey="" headerValue="全部"
							 onchange="check()" />
<%--							<select id="devType" name="devType" onchange="check()">--%>
<%--								<option value ="">全部</option>--%>
<%--								<option value ="Android-Large">触摸横屏</option>--%>
<%--								<option value ="Android-Portrait">触摸竖屏</option>--%>
<%--							</select> --%>
						</form>
						</div>
					</div>
				<form id="form-list" action="/r2k/ver/index.do" method="post">
					<table id="table-expandable"
						class="ui-widget ui-widget-content gridBody">
						<thead>
							<tr class="ui-widget-header ">
								<th style="width: 15%;">
									设备类型
								</th>
								<th style="width: 15%;">
									版本号
								</th>
								<th style="width: 20%;">
									创建时间
								</th>
								<th style="width: 25%;">
									描述
								</th>
								<th>
									操作
								</th>
							</tr>
						</thead>
						<tbody>
							<s:iterator value="page.result">
								<tr>
									<td>${devName}</td>
									<td>${version}</td>
									<td>
										<s:date name="crtDate" format="yyyy-MM-dd HH:mm:ss" />
									</td>
									<td>${description}</td>
									<td>
										<a href="/r2k/ver/updatePage.do?id=${id}">[修改]</a>
										<s:if test="devType != 'iPad' && devType != 'iPhone'">
											<s:if test="%{devType.indexOf('#') > 0}">
												<a href="/r2k/ver/risePage.do?id=${id}&version=${version}&devType=<s:property value='devType.replace("#","%23")'/>">[升级版本]</a>
												<a href="/r2k/ver/rollbackPage.do?id=${id}&version=${version}&devType=<s:property value='devType.replace("#","%23")'/>">[回滚版本]</a>
											</s:if>
										</s:if>
										<a href="${qrcode}" target="_blank">[查看二维码]</a>
										<a href="#" onclick="deleteVersion('${id}');">[删除]</a>
									</td>
								</tr>
							</s:iterator>
						</tbody>
					</table>
					<simpletable:pageToolbar page="${page}"></simpletable:pageToolbar>
				</form>
			</div>
		</div>
		<div class="footer"><%@ include file="/commons/footer.jsp" %></div>
		<div id="dialogdelete" title="删除">
			<input id="deleteid" name="delete" type="hidden" />
			<p>
				确认删除？
			</p>
		</div>
		<script type="text/javascript">
			$(function(){
				//创建simpleTable
		  		var sortColumns = "<s:property value='#parameters.sortColumns'/>";
		  			if(sortColumns == null || sortColumns == ""){
		  				sortColumns = " d.ID desc";
		  			}
		  		window.simpleTable = new SimpleTable('form-list','${page.thisPageNumber}','${page.pageSize}',sortColumns);
				
				$("#save").button().click(function(){
					window.location.href='/r2k/ver/savePage.do';
				});
				
				//设置删除对话框
			    $( "#dialogdelete" ).dialog({
			      autoOpen: false,
			      resizable: false,
			      height:140,
			      modal: true,
			      buttons: {
			        "确定": function() {
			        	var id = $("#deleteid").attr("value");
			        	$.get("${ctx}/ver/delete.do", {id: id},
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
			
			function check(){
				$("#form-search").submit();
			}
			
			 //打开删除对话框
			function deleteVersion(id){
		           $("#deleteid").attr("value", id);
				$("#dialogdelete").dialog( "open" );	 
		   }
		</script>
	</body>
</html>
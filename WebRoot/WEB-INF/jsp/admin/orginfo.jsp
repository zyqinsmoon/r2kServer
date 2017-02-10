<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib tagdir="/WEB-INF/tags/simpletable" prefix="simpletable"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" scope="page" />

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
	<head>
		<title>机构信息</title>
		<%@ include file="/commons/meta.jsp" %>
		<link href="${ctx}/css/base/list.css" rel="stylesheet" />
		<link href="${ctx}/css/colstyle.css" rel="stylesheet"/>
		<script type="text/javascript" src="${ctx}/js/swfupload/swfupload.js"></script>
		<script type="text/javascript" src="${ctx}/js/swfupload/handlers.js"></script>
		<script>
			$(function() {
				//创建simpleTable
	  			var sortColumns = "<s:property value='#parameters.sortColumns'/>";
	  			if(sortColumns == null || sortColumns == ""){
	  				sortColumns = " Position asc";
	  			}
	  			window.simpleTable = new SimpleTable('form-list','${page.thisPageNumber}','${page.pageSize}',sortColumns);
				//保存
				$("#button-save").button().click(function(e) {
					e.preventDefault();
					upload();
				});
				//查询
			  	$( "#user-search" )
			      .button()
			      .click(function() {
			    	  $("#form-search").submit();
		  		  });
			});
			//输入框获取焦点事件
			function focusIn(type, id){
				$("#"+type+"_"+id+"_msg").html("");
			}
		</script>
		<style type="text/css">
			.labeltd{width: 50%; height:30px; text-align: right; padding-right: 10px;font-weight: bold;}
			.infotd{}
		</style>
	</head>

	<body>
		<%@ include file="/commons/header.jsp" %>
		<div class="wrapper">
			<div class="left">
				<%@ include file="/commons/menu.jsp" %>
			</div>
				<div id="navLink">
					<a href="${ctx}/org/findOrgList.do" target="_self"><s:text name="r2k.menu.org"></s:text></a>&gt;本机构信息
				</div>
			<div class="content">
				<div id="container" class="ui-widget">
				  <div id="toolbar" class="ui-widget-header ui-corner-all" style="height: 26px;">
				 	<div style="float: right;">
					  <form id="searchUser" action="/r2k/org/getOrgInfo.do" method="post">
					  	用户查询：<input class="ui-input" name="s_userOrLoginName" value="<s:property value='#parameters.s_userOrLoginName' />"/>
					  	<button id="user-search">查询</button>	
					  </form>
				 	</div>
				  </div>
				<div id="xml-container" class="ui-widget" style="margin: 20px auto; width: 1000px">
					<table width="100%">
						<tr>
							<td style="width: 15%; font-weight: bold;" align="right">机构ID：</td>
							<td style="width: 15%;" align="left"><s:property value="org.orgId"/></td>
							<td style="width: 15%; font-weight: bold;" align="right">机构名称：</td>
							<td style="width: 15%;" align="left"><s:property value="org.orgName"/></td>
						</tr>
						<tr>
						<s:iterator value="enumlist" var="enum" status="status">
							<s:if test="#status.index != 0 && #status.index % 2 == 0">
								</tr><tr>
							</s:if>
							<td style="width: 15%; font-weight: bold;" align="right"><s:property value="#enum.enumName"/>授权：</td>
							<td>
								<s:set var="enumflag" value="false"></s:set>
								<s:iterator value="org.enumAuthList" var="node">
									<s:if test="#enum.enumCode == #node">
										<s:set var="enumflag" value="true"></s:set>
									</s:if>
								</s:iterator>
								<s:if test="#enumflag">已授权</s:if>
								<s:else>未授权</s:else>
							</td>
						</s:iterator>
						</tr>
						<tr>
							<td style="width: 15%; font-weight: bold;" align="right">触摸屏数量：</td>
							<td class="infotd"><s:property value="org.deviceNum"/></td>
							<td style="width: 15%; font-weight: bold;" align="right"></td>
							<td class="infotd"></td>
						</tr>
					</table>
				</div>
				 <form id="form-list" action="/r2k/org/getOrgInfo.do" method="post">
				 	<input type="hidden" name="s_q" value="<s:property value='#parameters.s_q' />" />
				  <table id="table-list" class="ui-widget ui-widget-content gridBody">
				    <thead>
				      <tr class="ui-widget-header ">
				        <th width="10%" class="sortCol" sortColumn="USER_NAME">用户名</th>
				        <th width="10%" class="sortCol" sortColumn="LOGIN_NAME">登录名</th>
				        <th width="10%" class="sortCol" sortColumn="ORG_ID">所在机构</th>
				        <th width="8%">用户类型</th>
				        <th width="10%" >移动电话</th>
				        <th width="10%" >办公电话</th>
				        <th width="10%" >电子邮件</th>
				        <th width="8%" class="sortCol" sortColumn="ENABLED">用户状态</th>
				        <th width="13%" >用户描述</th>
			            <th>操作</th>
				      </tr>
				    </thead>
				    <tbody>
				      <s:iterator value="page.result" var="user">
						<tr>
							<td><s:property value="userName"/></td>
							<td><s:property value="loginName"/></td>
							<td><s:property value="authOrgId"/></td>
							<td>
								<s:if test="isAdmin == 0">超级管理员</s:if>
								<s:if test="isAdmin == -1">普通用户</s:if> 
							</td>
							<td><s:property value="mobile"/></td>
							<td><s:property value="officePhone"/></td>
							<td><s:property value="email"/></td>
							<td>
								<s:if test="enabled == 0">正常</s:if>
								<s:if test="enabled == -1">禁用</s:if> 
							</td>
							<td><s:property value="userDesc"/></td>
							<td>
								<%--<a href="#" onclick="toUpdateUser('${id}')">[修改信息]</a>--%>
								<a href="#" onclick="toUpdatePwd('${id}')">[修改密码]</a>
<%--								<a href="#" onclick="toResetPwd('${id}')">[重置密码]</a>--%>
							</td>
						</tr>
					  </s:iterator>
				    </tbody>
				  </table>
				  <simpletable:pageToolbar page="${page}"></simpletable:pageToolbar>
				  </form>
				</div>
			</div>
		</div>
		
		<div class="footer"><%@ include file="/commons/footer.jsp" %></div>
	</body>
</html>
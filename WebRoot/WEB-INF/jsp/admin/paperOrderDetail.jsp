<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib tagdir="/WEB-INF/tags/simpletable" prefix="simpletable"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" scope="page"/>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
  <head>
    <title>订单明细状态</title>
    <%@ include file="/commons/meta.jsp" %>
	<script type="text/javascript">
		$(function(){
			//创建simpleTable
  			var sortColumns = "<s:property value='#parameters.sortColumns'/>";
  			if(sortColumns == null || sortColumns == ""){
  				sortColumns = " PAPER_ID desc";
  			}
  			window.simpleTable = new SimpleTable('form-list','${page.thisPageNumber}','${page.pageSize}',sortColumns);
  			//信息弹层
  		  	$( "#dialog-message" ).dialog({
  				autoOpen: false,
  				modal: true,
  				buttons: {
  					"确定": function() {
  						$( this).dialog( "close" );
  					}
  				},
  		     	close: function() {
  		     		$( this).dialog( "close" );
  		        }
  			});//end message dialog	
  			$("#org-search").button().click(function(){
  				//$("form:first").submit();
  				$("#form-search").submit();
  			});
		});//end init function
	</script>
  </head>
  
  <body>
  <%@ include file="/commons/header.jsp" %>
   <div class="wrapper">
	  <div class="left">
	  <%@ include file="/commons/menu.jsp" %>
	  </div>
	  <div id="navLink">
		<a href="/r2k/paper/showOrder.do" target="_self"><s:text name="r2k.menu.res.paper.order"></s:text></a> &gt; ${orderId}报纸订单明细
	  </div>
	  <div class="content">
	    <div id="container" class="ui-widget">
			<div id="toolbar" class="ui-widget-header ui-corner-all" style=" float:left; width:100%;">
				<div style="float: right; margin-right:20px;"><!-- 
				  	<s:form action="/paper/show.do" method="post" theme="simple" id="form-search">
				  		资源名称：<input class="ui-input" name="s_paperName" value="<s:property value='#parameters.s_paperName' />"/>
				  		<button id="org-search">查询</button>
					</s:form> -->
				</div>
			</div>
	    
	    	<form id="form-list" action="/r2k/paper/showDetail.do?orgId=${orgId}&orderId=${orderId}" method="post">
	    	<input type="hidden" name="s_orgId" value="<s:property value='#parameters.s_orgId'/>">
	    	<input type="hidden" name="s_paperName" value="<s:property value='#parameters.s_paperName' />"/>
	    	<table id="table-list" class="ui-widget ui-widget-content gridBody">
	    		<thead>
				<tr class="ui-widget-header">
					<th style="width: 10%;">报纸ID</th>
					<th style="width: 10%;">报纸名称</th>
					<th style="width: 10%;"class="sortCol" sortColumn= "START_DATE">授权开始时间</th>
					<th style="width: 10%;"class="sortCol" sortColumn= "END_DATE">授权结束时间</th>
					<th style="width: 10%;">报纸状态</th>
					<th style="width: 10%;"class="sortCol" sortColumn= "READ_START_DATE">报纸可阅开始时间</th>
					<th style="width: 10%;"class="sortCol" sortColumn= "READ_END_DATE">报纸可阅结束时间</th>
				</tr>
				</thead>
				<tbody>
				<s:iterator value="page.result">
					<tr>
						<td><s:property value="paperId" /></td>
						<td><s:property value="paperName"/></td>
						<td><s:date name="order.startDate" format="yyyy-MM-dd" /></td>
						<td><s:date name="order.endDate" format="yyyy-MM-dd" /></td>
						<td>
							<s:if test="status == 1">未生效</s:if>
							<s:elseif test="status == 2">已生效</s:elseif>
							<s:elseif test="status == 3">已过期</s:elseif>
							<s:elseif test="status == 4">已删除</s:elseif>
						</td>
						<td><s:date name="readStartDate" format="yyyy-MM-dd" /></td>
						<td><s:date name="readEndDate" format="yyyy-MM-dd" /></td>

					</tr>
				</s:iterator>
				</tbody>
			</table>
			<simpletable:pageToolbar page="${page}"></simpletable:pageToolbar>
			</form>
	    </div>
	    <div id="dialog-spinner" title="提示信息"></div>
		<div id="dialog-message" title="提示信息"></div>
	   </div>
   	</div>
	<div class="footer"><%@ include file="/commons/footer.jsp" %></div>
  </body>
</html>

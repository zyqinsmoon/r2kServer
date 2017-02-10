<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib tagdir="/WEB-INF/tags/simpletable" prefix="simpletable"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" scope="page"/>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
  <head>
     <title>报纸订单信息</title>
    <%@ include file="/commons/meta.jsp" %>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
<script>
        //删除弹层
	  $(function() {
	  		//创建simpleTable
	  		<s:if test="#parameters.sortColumns == null || #parameters.sortColumns == ''">
	  		var sortColumns = '';
	  		</s:if>
	  		<s:else>
  				var sortColumns = "<s:property value='#parameters.sortColumns'/>";
	  		</s:else>
  			window.simpleTable = new SimpleTable('form-list','${page.thisPageNumber}','${page.pageSize}',sortColumns);

	  $( "#dialog-delete" ).dialog({
			autoOpen: false,
			modal: true,
			buttons: {
				"确定": function() {
					window.location = "${ctx}/paper/deleteOrder.do?orderId="+$("#deleteorderId").attr("value") + "&orgId=" + $("#deleteorgId").attr("value");
				},
				"取消": function() {
					$( this).dialog( "close" );
				}
			},
	     	close: function() {
	     		$( this).dialog( "close" );
	        }
		});

		$( "#dialog-message" ).dialog({
			autoOpen: false,
			modal: true,
			buttons: {
				"确定": function() {
					$( this).dialog( "close" );
				}
			}
		});
		$("#order-search")
			.button()
      		.click(function() {
    	  		$("#form-search").submit();
	 	 	});
		});

		 function deleteOrder(orderId, orgId)
        {       
          $("#deleteorderId").attr("value",orderId);
          $("#deleteorgId").attr("value",orgId);
		  $("#dialog-delete").dialog( "open" );
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
		<a href="/r2k/paper/showOrder.do" target="_self"><s:text name="r2k.menu.res.paper.order"></s:text></a>
	  </div>
	  <div class="content">
	    <div id="container" class="ui-widget">
			<div id="toolbar" class="ui-widget-header ui-corner-all" style=" float:left; width:100%;">				  	
				<div style="float: right;">
				  	<form action="/r2k/paper/showOrder.do" method="post" id="form-search">
				  		机构ID：<input class="ui-input" name="s_orgId" value="<s:property value='#parameters.s_orgId' />"/>
				  		<button id="order-search">查询</button>
					</form>
				</div>
			</div>
	    
	    	<form id="form-list" action="/r2k/paper/showOrder.do" method="post">
	    	<input type="hidden" name="s_orgId" value="<s:property value='#parameters.s_orgId'/>">
	    	<input type="hidden" name="s_paperName" value="<s:property value='#parameters.s_paperName' />"/>
	    	<table id="table-list" class="ui-widget ui-widget-content gridBody">
	    		<thead>
				<tr class="ui-widget-header">				    
					<th style="width: 10%;"class="sortCol" sortColumn="ORDER_ID">订单ID</th>
					<th style="width: 10%;" >订单状态</th>
					<th style="width: 10%;"class="sortCol" >机构ID</th>										
					<th style="width: 10%;"class="sortCol" >授权开始时间</th>
					<th style="width: 10%;"class="sortCol" >授权结束时间</th>	
					<th style="width: 10%;"class="sortCol" >创建人</th>	
					<th style="width: 10%;"class="sortCol" sortColumn="CRT_DATE">创建时间</th>							
					<th style="width: 25%;" >操作</th>
				</tr>
				</thead>
				<tbody>
				<s:iterator value="page.result">
					<tr>					    
						<td><s:property value="orderId" /></td>
						<td>
								<s:if test="status == 1">
									未生效
								</s:if>
								<s:if test="status == 2">
									已生效
								</s:if>
								<s:if test="status == 3">
									已过期
								</s:if>
								<s:if test="status == 4">
									已删除
								</s:if>
								</td>
						<td><s:property value="orgId"/></td>
						<td><s:date name="startDate" format="yyyy-MM-dd" /></td>
						<td><s:date name="endDate" format="yyyy-MM-dd" /></td>                        						
						<td><s:property value="operator"/></td>
						<td><s:date name="crtDate" /></td>
						<td>
						<s:if test="status != 4">
						  <a href="/r2k/paper/showDetail.do?orderId=${orderId}&orgId=${orgId}">[查看明细]</a>
                          <a href="#" onclick="deleteOrder('${orderId}','${orgId}');">[删除]</a>
						</s:if>                          
						</td>
					</tr>
				</s:iterator>
				</tbody>
			</table>
			<simpletable:pageToolbar page="${page}"></simpletable:pageToolbar>
			</form>
	    </div>
	    <div id="dialog-delete" title="提示信息">
		<input type="hidden" id="deleteorderId">
		<input type="hidden" id="deleteorgId">
		<p style="margin-top: 20px">确定要删除当前订单吗？</p>
	    </div>
	   </div>
   	</div>
	<div class="footer"><%@ include file="/commons/footer.jsp" %></div>
  </body>
</html>

<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib tagdir="/WEB-INF/tags/simpletable" prefix="simpletable"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" scope="page"/>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
  <head>
    <title>资源授权查询</title>
    <%@ include file="/commons/meta.jsp" %>
	<script type="text/javascript">
		$(function(){
			//不允许输入非数字
			$('.spinner').keydown(function(e){
	            if ($.browser.msie) {  // 判断浏览器
	                   if ( ((event.keyCode > 47) && (event.keyCode < 58)) || (event.keyCode == 8) || ((event.keyCode > 95) && (event.keyCode < 106)) ) {
	                          return true;  
	                    } else {  
	                          return false;  
	                   }
	             } else {  
	                if ( ((e.which > 47) && (e.which < 58)) || (e.which == 8) || (event.keyCode == 17) || ((event.keyCode > 95) && (event.keyCode < 106)) ) {  
	                         return true;  
	                 } else {  
	                         return false;  
	                 }  
	             }}).focus(function() {
	                     this.style.imeMode='disabled';   // 禁用输入法,禁止输入中文字符
			});
			// 微调控制项
		    $('.spinner').spinner({          
			    stop:function(e,ui){
			    	var paperId = $(this).attr("name");
			        var sort  = $(this).attr("value");
			        if(parseInt(sort)!= NaN && 0 < parseInt(sort)&& parseInt(sort) < 1000){
			        	$.get("${ctx}/recommend/updatePaperSort.do", {"sort": sort, "paperId":paperId} );
			        }else{
			        	$("#dialog-spinner").html("").html("数字必须在1~999之间。");
		            	$("#dialog-spinner").dialog("open");
			        	//window.location.reload();
			        }
			        
			    }
			});
			//创建simpleTable
  			<s:if test="#parameters.sortColumns == null || #parameters.sortColumns == ''">
	  		var sortColumns = '';
	  		</s:if>
	  		<s:else>
  				var sortColumns = "<s:property value='#parameters.sortColumns'/>";
	  		</s:else>
  			window.simpleTable = new SimpleTable('form-list','${page.thisPageNumber}','${page.pageSize}',sortColumns);
  			//微调信息提示弹层
  		  	$( "#dialog-spinner" ).dialog({
  				autoOpen: false,
  				modal: true,
  				buttons: {
  					"确定": function() {
  						window.location.reload();
  						$( this).dialog( "close" );
  					}
  				},
  		     	close: function() {
  		     		window.location.reload();
  		     		$( this).dialog( "close" );
  		        }
  			});//end message dialog
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
  			//导出报纸列表
  		  	$( "#paper-export" )
  		      .button()
  		      .click(function() {
  		    	  window.location = '${ctx}/paper/export.do';
  	  		  });
		});//end init function
		
		//是否置顶
		  function setTop(val, orgId, paperId, id){
			  $.ajax({
		  		url:"${ctx}/recommend/setTopPaper.do",
		  		type:'post',
		  		data:{"type":val, "orgId":orgId, "paperId":paperId},
		  		success:function(data){
		  			if(data != null){
		  				if(data.flag == '1'){
				  			if(data.paperAuth.type == '0'){
				  				$("#sortcontain"+id).hide();
				  			}else if(data.paperAuth.type == '1'){
				  				$("#sort"+id).attr("value",data.paperAuth.position);
				  				$("#sortcontain"+id).show();
				  			}
		  				}else if(data.flag == '0'){
		  					$("#dialog-message").html("").html("获取顺序信息失败。");
			            	$("#dialog-message").dialog("open");
		  				}else if(data.flag == '-1'){
		  					$("#dialog-message").html("").html("获取顺序信息失败。");
			            	$("#dialog-message").dialog("open");
		  				}
		  			}else{
		  				$("#dialog-message").html("").html("获取顺序信息失败。");
		            	$("#dialog-message").dialog("open");
		  			}
		  		}
		  	});
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
		<a href="/r2k/paper/show.do" target="_self"><s:text name="r2k.menu.res.paper"></s:text></a>
	  </div>
	  <div class="content">
	    <div id="container" class="ui-widget">
			<div id="toolbar" class="ui-widget-header ui-corner-all" style=" float:left; width:100%;">
				<button id="paper-export">导出报纸列表</button>
				<div style="float: right; margin-right:20px;">
				  	<s:form action="/paper/show.do" method="post" theme="simple" id="form-search">
				  		资源名称：<input class="ui-input" name="s_paperName" value="<s:property value='#parameters.s_paperName' />"/>
				  		<button id="org-search">查询</button>
					</s:form>
				</div>
			</div>
	    
	    	<form id="form-list" action="/r2k/paper/show.do" method="post">
	    	<input type="hidden" name="s_orgId" value="<s:property value='#parameters.s_orgId'/>">
	    	<input type="hidden" name="s_paperName" value="<s:property value='#parameters.s_paperName' />"/>
	    	<table id="table-list" class="ui-widget ui-widget-content gridBody">
	    		<thead>
				<tr class="ui-widget-header">
					<th style="width: 10%;">报纸ID</th>
					<th style="width: 10%;">报纸名称</th>
					<th style="width: 10%;"class="sortCol" sortColumn= "START_DATE">授权开始时间</th>
					<th style="width: 10%;"class="sortCol" sortColumn= "END_DATE">授权结束时间</th>
					<th style="width: 10%;"class="sortCol" sortColumn= "READ_START_DATE">报纸可阅开始时间</th>
					<th style="width: 10%;"class="sortCol" sortColumn= "READ_END_DATE">报纸可阅结束时间</th>
					<th style="width: 5%;" >是否置顶</th>
		        	<th style="width: 5%;" >显示顺序</th>
				</tr>
				</thead>
				<tbody>
				<s:iterator value="page.result">
					<tr>
						<td><s:property value="paperId" /></td>
						<td><s:property value="paperName"/></td>
						<td><s:date name="order.startDate" format="yyyy-MM-dd" /></td>
						<td><s:date name="order.endDate" format="yyyy-MM-dd" /></td>
						<td><s:date name="readStartDate" format="yyyy-MM-dd" /></td>
						<td><s:date name="readEndDate" format="yyyy-MM-dd" /></td>
						<td>
							<select id="top<s:property value="id"/>" onchange="setTop(this.options[this.options.selectedIndex].value, '${orgId}', '${paperId}', '${id}')"> 
								<s:if test="recommend.sort != null && recommend.sort != 0">
									<option value="0">否 
									<option value="1" selected="selected">是 
								</s:if>
								<s:else>
									<option value="0" selected="selected">否 
									<option value="1">是 
								</s:else>
							</select> 
						</td>
						<td class="tdSpinner">
							<s:if test="recommend.sort != null && recommend.sort != 0">
								<div id="sortcontain${id}" style="display: block;">
									<input id="sort${id}" name="${paperId}" class="spinner" style="width: 30px" value="${recommend.sort}" min="1" max="999"/>
								</div>
							</s:if>
							<s:else>
								<div id="sortcontain${id}" style="display: none;">
									<input id="sort${id}" name="${paperId}" class="spinner" style="width: 30px" value="0" min="1" max="999"/>
								</div>
							</s:else>
						</td>
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

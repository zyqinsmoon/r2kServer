<#assign className = table.className>   
<#assign classNameLower = className?uncap_first>
<%@ page import="${basepackage}.model.${className}" %>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib tagdir="/WEB-INF/tags/simpletable" prefix="simpletable"%>
<c:set var="ctx" value="${'${'}pageContext.request.contextPath${'}'}" scope="page"/>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
  	<title><s:text name="r2k.menu.${namespace}"></title>
    <meta http-equiv="X-UA-Compatible" content="IE=8"/>
    <%@ include file="/commons/header.jsp" %>
		<script>
			var checkBlankRegex = /^[\s\S]+$/;	//非空正则表达式
  			var checkNumRegex = /^[1-9]\d*$/;	//数字正则表达式
  			$(function(){
  				//创建simpleTable
  				var sortColumns = "<s:property value='#parameters.sortColumns'/>";
  				if(sortColumns == null || sortColumns == ""){
  					sortColumns = " ${table.idColumn} desc";
  				}
  				window.simpleTable = new SimpleTable('form-list',${'${'}page.thisPageNumber${'}'},${'${'}page.pageSize${'}'},sortColumns,"ajax");
  			
  				//新增
  				$( "#${classNameLower}-save" )
  					.button()
  					.click(function() {
  						window.location.href = "${'${'}ctx${'}'}/${namespace}/toSave.do";
  					});
  				
  				//删除弹层
  			  	$( "#dialog-delete" ).dialog({
  					autoOpen: false,
  					modal: true,
  					buttons: {
  						"确定": function() {
  							$("#form-list").attr("action","${'${'}ctx${'}'}/${namespace}/delete.do");
  		        			$("#form-list").submit();
  						},
  						"取消": function() {
  							$( this).dialog( "close" );
  						}
  					},
  			     	close: function() {
  			     		$( this).dialog( "close" );
  			        }
  				});
  				
  				//复选框全选
  	  	    	$("#checkAll").click(function(){
  	  	    		$('input[type="checkbox"][name="${classNameLower}Ids"]').attr("checked",this.checked);
  				});
  				
  				//批量删除按钮
  	  	  		$( "#batDel" )
  	      			.button()
  	      			.click(function() {
  	    	  			if(checkSelect()){
  		    	  			$( "#dialog-delete" ).dialog( "open" );
  	    	  			}
  	      		});
  	      		
  	      		//信息提示框
  	  	  		$( "#dialog-message" ).dialog({
  					autoOpen: false,
  					modal: true,
  					buttons: {
  						Ok: function() {
  							$( this).dialog( "close" );
  						}
  					}
  				});
  	      		
  	  	  //查询
  	  	  	$( "#${classNameLower}-search" )
  	  	      .button()
  	  	      .click(function() {
  	  	    	  $("#form-search").submit();
  	    		  });
  			});
  			
  			//判断复选框是否空选
  		  	function checkSelect(){
  			  var boxFlag = false;
  			  var boxs = $('input[type="checkbox"][name="${classNameLower}Ids"]');
  			  for(var i = 0, len = boxs.length; i < len; i++){
  				  if(boxs[i].checked ) boxFlag = true; 
  			  }
  			  if(!boxFlag){
  				  $("#tipmsg").html("").html("勾选<%=${className}.TABLE_ALIAS%>不能为空。");
  				  $( "#dialog-message" ).dialog( "open" );
  			  }
  			  return boxFlag;
  		  	}
  			
  		  	function update(id){
  		  		window.location.href = "${'${'}ctx${'}'}/${namespace}/toUpdate.do?${classNameLower}.id="+id;
  		  	}
		</script>
	</head>
	<body>
<div class="wrapper">
	<div class="left">
		<%@ include file="/commons/menu.jsp" %>
	</div>
	<div class="content">
		<!-- 页面主体 -->
		<div id="container"  class="ui-widget">
			<div id="toolbar" class="ui-widget-header ui-corner-all">
		  		<button id="${classNameLower}-save" >创建<%=${className}.TABLE_ALIAS%></button>	
		  		<button id="batDel" >批量删除</button>
		 		<div style="float: right;">
			 		<form id="search${className}" action="${'${'}ctx${'}'}/${namespace}/pageQuery.do" method="post">
			  			按<%=${className}.TABLE_ALIAS%>Id：<input class="ui-input" name="s_${table.idColumn.columnNameLower}" value="<s:property value='#parameters.s_${table.idColumn.columnNameLower}' />"/>
		  				<button id="${classNameLower}-search" type="submit">查询</button>
			  		</form>
		 		</div>
		  	</div>
	 		 <!-- 数据列表 -->
	 		 <form id="form-list" action="${'${'}ctx${'}'}/${namespace}/pageQuery.do" method="post">
	 		 <table id="table-list" class="ui-widget ui-widget-content gridBody">
	 		 	<thead>
	 		 		<tr class="ui-widget-header">
	 		 			<th width="5%"><input id="checkAll" type="checkbox"/>全选</th>
	 		 			<#list table.columns as column>
	 		 			<th field="${column.columnNameLower}" sortColumn="${column.sqlName}"><%=${className}.ALIAS_${column.constantName}%></th>
	 		 			</#list>
	 		 			<th>操作</th>
	 		 		</tr>
	 		 	</thead>
	 		 	<tbody>
	 		 		<s:iterator value="page.result" var="${classNameLower}">
	 		 			<tr>
	 		 				<td><input name="${classNameLower}Ids" type="checkbox" value="${'${'}id${'}'}"/></td>
	 		 				<#list table.columns as column>
	 		 				<td><s:property value="${column.columnNameLower}"/></td>
	 		 				</#list>
	 		 				<td>
								<a href="#" onclick="update(${'${'}id${'}'})">[修改]</a>
							</td>
	 		 			</tr>
	 		 		</s:iterator>
	 		 	</tbody>
	 		 </table>
	 		 <simpletable:pageToolbar page="${'${'}page${'}'}"></simpletable:pageToolbar>
	 		 </form>
		</div>
		<div id="dialog-delete" title="提示信息">
			<div style="margin-top: 20px">确定要删除当前<%=${className}.TABLE_ALIAS%>信息吗？</div>
		</div>
		<div id="dialog-message" title="提示信息">
			<span id="tipmsg" style="margin:5% auto;"></span>
		</div>
</div>
</div>
<div class="footer"><%@ include file="/commons/footer.jsp" %></div>
</body>
</html>
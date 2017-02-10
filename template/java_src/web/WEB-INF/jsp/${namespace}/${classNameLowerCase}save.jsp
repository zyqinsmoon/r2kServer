<#assign className = table.className>
<#assign classNameLower = className?uncap_first> 
<#assign fullModelName = basepackage + ".model." + table.className>
<%@ page language="java" import="java.util.*,${fullModelName}" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="ctx" value="${'${'}pageContext.request.contextPath${'}'}" scope="page" />

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<title>创建<%=${className}.TABLE_ALIAS%></title>
		<meta http-equiv="X-UA-Compatible" content="IE=8"/>
		<%@ include file="/commons/header.jsp" %>
		<script >
			var checkBlankRegex = /^[\s\S]+$/;	//非空正则表达式
			var checkNumRegex = /^[1-9]\d*$/;	//数字正则表达式
			
			$(function() {	
				//信息提示框
		  	  	$( "#dialog-message" ).dialog({
					autoOpen: false,
					modal: true,
					buttons: {
						Ok: function() {
							$( this).dialog( "close" );
						}
					}
				});//end message
				
				//保存
			  	$( "#${classNameLower}save" )
			  	  .button()
			      .click(function(e) {
			    	  e.preventDefault();
			    	  var flag = checkValidate();
			    	  if(flag){
						$("#form-save").attr("action","${'${'}ctx${'}'}/${namespace}/save.do").submit();
					  }
			      });
				
			  	//清空
			  	$( "#cleanTest" )
			      .button()
			      .click(function(e) {
			    	  e.preventDefault();
			    	  
			      });
			      
			});
			//获取焦点事件
			function focusIn(id){
				$("#"+id+"_msg").html("");
			}
			//保存验证
			function checkValidate(){
				var flag = true;
				//验证过程
				return flag;
			}
		</script>
	</head>
	<body>
		<div class="wrapper">
			<div class="left">
				<%@ include file="/commons/menu.jsp" %>
			</div>
		<div id="navLink">
			 <a href="${'${'}ctx${'}'}/${namespace}/pageQuery.do" target="_self"><s:text name="r2k.menu.${namespace}"></s:text></a> &gt; 创建<%=${className}.TABLE_ALIAS%>
		</div>
		<div class="content">
				<div id="xml-container" class="ui-widget" style="margin: 20px 0; width: 1000px">
					<form id="form-save" method="post">
					  	<div class="regbox">
					  		<#list table.columns as column>
					  		<div class="field"><label class="field-label"><%=${className}.ALIAS_${column.constantName}%><span class="mstInput">*</span>:</label>
					       		<input class="ui-input field-input" name="${classNameLower}.${column.columnNameLower}" id="new_${column.columnNameLower}" onfocus="focusIn('new_${column.columnNameLower}')" />
					        	<span class="ui-tips-error" id="new_${column.columnNameLower}_msg"></span>
						  	</div>
					  		</#list>
						  	<div class="field">
						  		<div class="form-button">
						  		<button id="${classNameLower}save">保存</button>
						  		<button id="cleanTest">清空</button>
						  		</div>
						  	</div>
						</div>
					</form>
				</div>
			</div>
		</div>
		<div class="footer"><%@ include file="/commons/footer.jsp" %></div>
	</body>
</html>
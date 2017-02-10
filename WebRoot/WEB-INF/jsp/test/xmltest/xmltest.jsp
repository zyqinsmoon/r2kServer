<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" scope="page"/>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>XML自动化测试</title>
    
	<jsp:include page="/WEB-INF/jsp/commons/meta.jsp"></jsp:include>
	<link href="${ctx}/css/base/list.css" rel="stylesheet">
	<style type="text/css">
		div#result-contain{
			width: 900px;
    		margin: 20px 0;
		}
		#results {
    		margin: 1em 0;
    		border-collapse: collapse;
    		width: 100%;
		}

		#results td, #results th {
    		border: 1px solid #eee;
    		padding: .6em 10px;
    		text-align: left;
		}
	</style>
  <script>
  var checkBlankRegex = /^[\s\S]+$/;	//非空正则表达式
  var checkNumRegex = /^[1-9]\d*$/;	//数字正则表达式
	  
  $(function(){
	//新建对话框函数
		$( "#dialog-form-save" ).dialog({
			autoOpen: false,
			height: 350,
			width: 450,
			modal: true,
			buttons:{
				"保存": function() {
					var xmlTestName = $("#new_xmlName").val();
					var xmlUrl = $("#new_xmlUrl").val();
					if(xmlTestName == null || xmlTestName == ""){
						$("#new_xmlName_msg").text("xml测试名称不能为空");
					}else if(xmlUrl == null || xmlUrl == ""){
						$("#new_xmlUrl_msg").text("xml文件地址不能为空");
					}else{
						$("#form-save").submit();
					}
				},
				"取消": function() {
					$( this ).dialog( "close" );
				}
			},
			close: function() {
				$( this ).dialog( "close" );
			}
		});
		$("#xml-save").button().click(function(){
				$("#form-save").attr("action", "${ctx}/test/xml_add.do");
      		$( "#dialog-form-save" ).dialog( "open" );
			});
  });
	  //删除机构
	  function deleteXmlTest(ctx,xmlTestName){
		var returnVal = window.confirm("是否删除此测试？", "删除测试");
		if(returnVal) {
			window.location =  ctx+"/test/xml_delete.do?xmlTest.xmlName="+xmlTestName;	
		}
	  }
  </script>
  </head>
  
  <body id="body">
	<div id="dialog-form-save" title="添加测试XML">
	  <form id="form-save" method="post">
		  <fieldset>
		  	<label>测试名称:</label>
	        <input class="ui-input" name="xmlTest.xmlName" id="new_xmlName" /><br/>
	        <span class="ui-tips-error" id="new_xmlName_msg"></span><br/>
		  	<label>XML文件地址:</label>
	        <input class="ui-input" name="xmlTest.xmlUrl" id="new_xmlUrl" /><br/>
	        <span class="ui-tips-error" id="new_xmlUrl_msg"></span><br/>
	        <label>选择校验XSD:</label>
	        <s:select list="staticXsds" name="xmlTest.xsd.name" id="new_xsdName" listKey="key" listValue="key" ></s:select><br/>
		  </fieldset>
	  </form>
	</div>
	 
	<div id="xml-container" class="ui-widget" style="margin: 20px 0;width: 1000px">
	  <button id="xml-save" >添加测试XML</button>	
	<form action="/r2k/test/xml_autoXmlTest.do" method="post">
	  <table id="table-list" class="ui-widget ui-widget-content">
	    <thead>
	      <tr class="ui-widget-header ">
	        <th field="xmlName">XML测试名称</th>
	        <th field="xmlUrl">XML文件地址</th>
            <th field="xsdName" >校验文件名称</th>
            <th field="xsdUrl" >XSD文件地址</th>
            <th>操作</th>
	      </tr>
	    </thead>
	    <tbody>
	      <s:iterator value="staticXmlTests" var="xmlTest">
			<tr>
				<td><s:property value="xmlName"/></td>
				<td><s:property value="xmlUrl"/></td>
				<td><s:property value="xsd.name"/></td>
				<td><s:property value="xsd.url"/></td>
				<td>
					<a href="#" onclick="deleteXmlTest('${ctx}','<s:property value="xmlName"/>');">删除</a>
				</td>
			</tr>
		  </s:iterator>
	    </tbody>
	  </table>
	  <input class="ui-input" type="submit" value="开始测试" />
	</form>
	</div>
	
	<div id="result-contain" class="ui-widget"  style="margin: 20px 0;width: 1000px"><!--onclick="saveOrg('${ctx}');" -->
	  <label>测试结果</label><br />	
	  <table id="results" class="ui-widget ui-widget-content">
	    <thead>
	      <tr class="ui-widget-header ">
	        <th field="xmlName">XML测试名称</th>
            <th field="xsdName" >校验名称</th>
            <th field="result" >测试结果</th>
	      </tr>
	    </thead>
	    <tbody>
	      <s:iterator value="staticXmlTests" var="xmlTest">
			<tr>
				<s:if test="isTested == 1">
				<td><s:property value="xmlName"/></td>
				<td><s:property value="xsd.name"/></td>
				<td>
					<s:if test="result == null">
						有效
					</s:if>
					<s:else>
						无效,<s:property value="result"/>
					</s:else>
				</td>
				</s:if>
			</tr>
		  </s:iterator>
	    </tbody>
	  </table>
	</div>
  </body>
</html>

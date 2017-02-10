<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib tagdir="/WEB-INF/tags/simpletable" prefix="simpletable"%>

<c:set var="ctx" value="${pageContext.request.contextPath}" scope="page"/>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
  <head>
    <title>已授权专题列表</title>
    <%@ include file="/commons/meta.jsp" %>
	<script>
	$(function() {
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
		    	var topicId = $(this).attr("name");
		        var sort  = $(this).attr("value");
		        if(parseInt(sort)!= NaN && 0 < parseInt(sort)&& parseInt(sort) < 1000){
		        	$.get("${ctx}/recommend/updateTopicSort.do", {"sort": sort, "topicId":topicId} );
		        }else{
		        	$("#dialog-spinner").html("").html("数字必须在1~999之间。");
	            	$("#dialog-spinner").dialog("open");
		        	//window.location.reload();
		        }
		        
		    }
		});
		var sortColumns = "<s:property value='#parameters.sortColumns'/>";
  			/*if(sortColumns == null || sortColumns == ""){
  				sortColumns = " START_DATE desc";
  			}*/
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
		//查询
	  	$( "#topic-search" )
	      .button()
	      .click(function() {
	    	  $("#form-search").submit();
  		  });
	});//end init function
	  //获取焦点事件
	  function focusIn(id){
		  $("#"+id+"_msg").html("");
	  }
	  
	  function putTopicContent(id){
	  	window.location.href = "/r2k/topic/putTopicContent.do?topic.id="+id;
	  }
	  
	  function topicOperation(id,topicName,desc,query,updateTime,pageType,url){
	  	$("#t_id").attr("value",id);
	  	$("#t_name").attr("value",topicName);
	  	$("#t_desc").attr("value",desc);
	  	$("#t_query").attr("value",query);
	  	$("#t_updateTime").attr("value",updateTime);
	  	$("#t_pageType").attr("value",pageType);
	  	$("#selectedTopic").attr("action",url);
	  	$("#selectedTopic").submit();
	  }
	  
	  function showTopicContents(id,topicName,desc,query,updateTime,pageType){
	  	topicOperation(id,topicName,desc,query,updateTime,pageType,"/r2k/topic/showContent.do");
	  }
	  
	  function checkBeforeCreate(id,topicName,desc,query,updateTime,pageType){
	  	topicOperation(id,topicName,desc,query,updateTime,pageType,"/r2k/topic/checkBeforeCreate.do");
	  }
	  
	  function updateTopic(id){
	  	window.location.href = "/r2k/topic/toUpdateTopic.do?topic.id="+id;
	  }
	  //是否置顶
	  function setTop(val, orgId, topicId, id){
		  $.ajax({
	  		url:"${ctx}/recommend/setTopTopic.do",
	  		type:'post',
	  		data:{"type":val, "orgId":orgId, "topicId":topicId},
	  		success:function(data){
	  			if(data != null){
	  				if(data.flag == '1'){
			  			if(data.topicAuth.type == '0'){
			  				$("#sortcontain"+id).hide();
			  			}else if(data.topicAuth.type == '1'){
			  				$("#sort"+id).attr("value",data.topicAuth.position);
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
  
  <body id="body">
  <%@ include file="/commons/header.jsp" %>
	<div class="wrapper">
	<div class="left">
		<%@ include file="/commons/menu.jsp" %>
	</div>
	<div id="navLink">
		<a href="/r2k/topic/showTopicAuthList.do" target="_self"><s:text name="r2k.menu.res.topic"></s:text></a>
	</div>
	
	<div class="content">
		<div id="container" class="ui-widget">
		 <div id="toolbar" class="ui-widget-header ui-corner-all" style=" float:left; width:100%;">
		 	<div class="form-search-div">
			  <form id="form-search" action="${ctx}/topic/showTopicAuthList.do" method="post">
			  	专题查询：<input class="ui-input" name="s_q" value="<s:property value='#parameters.s_q' />"/>
			  	<button id="topic-search">查询</button>	
			  </form>
		 	</div>
		  </div>
		 <form id="form-list" action="${ctx}/topic/showTopicAuthList.do" method="post">
		 <input type="hidden" name="s_q" value="<s:property value='#parameters.s_q' />"/>
		  <table id="table-list" class="ui-widget ui-widget-content gridBody">
		    <thead>
		      <tr class="ui-widget-header ">
		        <th style="width: 15%;" >专题名称</th>
		        <th style="width: 15%;" >专题描述</th>
		        <th style="width: 15%;" field="lastDate" sortColumn="LAST_DATE">最后更新时间</th>
		        <th style="width: 15%;" field="lastDate" sortColumn="LAST_DATE">授权时间</th>
		        <th style="width: 15%;" field="startDate" sortColumn="START_DATE">授权开始时间</th>
		        <th style="width: 15%;" field="endDate" sortColumn="END_DATE">授权结束时间</th>
		        <th style="width: 5%;" >是否置顶</th>
		        <th style="width: 5%;" >显示顺序</th>
		      </tr>
		    </thead>
		    <tbody>
		      <s:iterator value="page.result" var="topicAuth">
				<tr>
					<td><s:property value="topic.topicName"/></td>
					<td><s:property value="topic.description"/></td>
					<td><s:date name="topic.updateTime" format="yyyy-MM-dd"/></td>
					<td><s:date name="crtDate" format="yyyy-MM-dd"/></td>
					<td><s:date name="startDate" format="yyyy-MM-dd"/></td>
					<td><s:date name="endDate" format="yyyy-MM-dd"/></td>
					<td>
						<select id="top<s:property value="id"/>" onchange="setTop(this.options[this.options.selectedIndex].value, '${orgId}', '${topicId}', '${id}')"> 
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
								<input id="sort${id}" name="${topicId}" class="spinner" style="width: 30px" value="${recommend.sort}" min="1" max="999"/>
							</div>
						</s:if>
						<s:else>
							<div id="sortcontain${id}" style="display: none;">
								<input id="sort${id}" name="${topicId}" class="spinner" style="width: 30px" value="0" min="1" max="999"/>
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
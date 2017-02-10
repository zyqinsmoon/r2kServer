<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib tagdir="/WEB-INF/tags/simpletable" prefix="simpletable"%>

<c:set var="ctx" value="${pageContext.request.contextPath}" scope="page"/>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
  <head>
    <title>专题列表</title>
    <%@ include file="/commons/meta.jsp" %>
    <style type="text/css">
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
			    	var id = $(this).attr("name");
			        var position  = $(this).attr("value");
			        if(parseInt(position)!= NaN && 0 < parseInt(position)){
			        	var data = eval("("+$(this).attr("data-topic")+")");
			        	$.get("${ctx}/topic/updateTopic.do", { "topic.id": data.id,"topic.topicName":data.topicName,"topic.description": data.description,"topic.org":data.org, "topic.icon":data.icon,"topic.autoCreate":data.autoCreate,"topic.incrCreate":data.incrCreate,"topic.results":data.results,"topic.createDate":data.createDate,"topic.isPublished":data.isPublished,"topic.condition.query":data.query,"topic.condition.sort":data.sort,"topic.position": position} );
			        }else{
			        	alert("数字必须大于0");
			        }
			    }
			});
  	    	//创建simpleTable
  			var sortColumns = "<s:property value='#parameters.sortColumns'/>";
  			if(sortColumns == null || sortColumns == ""){
  				sortColumns = " Position asc";
  			}
  			window.simpleTable = new SimpleTable('form-list',${page.thisPageNumber},${page.pageSize},sortColumns);
  			
  		//保存
	  	$( "#topic-save" )
	      .button()
	      .click(function() {
	         window.location.href = "${ctx}/topic/toSaveTopic.do";
	      });
	    
	    //删除弹层
	  	$( "#dialog-delete" ).dialog({
			autoOpen: false,
			modal: true,
			buttons: {
				"确定": function() {
					$("#form-list").attr("action","/r2k/topic/deleteTopics.do");
        			$("#form-list").submit();
				},
				"取消": function() {
					$( this).dialog( "close" );
				}
			},
	     	close: function() {
	     		$( this).dialog( "close" );
	        }
		});//end delete dialog
		
		//查询
	  	$( "#topic-search" )
	      .button()
	      .click(function() {
	    	  $("#form-search").submit();
  		  });
  		  
  		  //复选框全选
  	    	$("#checkAll").click(function(){
  	    		$('input[type="checkbox"][name="topicIds"]').attr("checked",this.checked);
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
	});
	  
	  function putTopicContent(id){
	  	var url = "/r2k/topic/putTopicContent.do?topic.id="+id;
	  	$.ajax({
	  		url:url,
	  		dataType:"json",
	  		beforeSend: function () {
	        	ShowDiv();
	        },
	        complete: function () {
	        	HiddenDiv();
	        },
	  		success:function(data){
	  			//console.log(data.result[0].code);
	  			if(data.result[0].code == "1000"){
	  				window.location.href = "/r2k/topic/showTopics.do";
	  			}else{
	  				$("#tipmsg").html("").html("生成专题失败");
			  		$( "#dialog-message" ).dialog( "open" );
	  			}
	  		}
	  	});
	  }
	  
	  function topicOperation(id,topicName,desc,query,sort,updateTime,pageType,url){
	  	$("#t_id").attr("value",id);
	  	$("#t_name").attr("value",topicName);
	  	$("#t_desc").attr("value",desc);
	  	$("#t_query").attr("value",query);
	  	$("#t_sort").attr("value",sort);
	  	$("#t_updateTime").attr("value",updateTime);
	  	$("#t_pageType").attr("value",pageType);
	  	$("#selectedTopic").attr("action",url);
	  	$("#selectedTopic").submit();
	  }
	  
	  function showTopicContents(id,topicName,desc,query,sort,updateTime,pageType){
	  	topicOperation(id,topicName,desc,query,sort,updateTime,pageType,"/r2k/topic/showContent.do");
	  }
	  
	  function checkBeforeCreate(id,topicName,desc,query,sort,updateTime,pageType){
	  	topicOperation(id,topicName,desc,query,sort,updateTime,pageType,"/r2k/topic/checkBeforeCreate.do");
	  }

	  function updateTopic(id){
	  	window.location.href = "/r2k/topic/toUpdateTopic.do?topic.id="+id;
	  }
	  
	  //判断复选框是否空选
	  	function checkSelect(){
		  var boxFlag = false;
		  var boxs = $('input[type="checkbox"][name="topicIds"]');
		  for(var i = 0, len = boxs.length; i < len; i++){
			  if(boxs[i].checked ) boxFlag = true; 
		  }
		  if(!boxFlag){
			  $("#tipmsg").html("").html("勾选专题不能为空。");
			  $( "#dialog-message" ).dialog( "open" );
		  }
		  return boxFlag;
	  	}
	  	//显示等待弹层
		 function ShowDiv() {
             $("#loading").show();
         }
		//关闭等待弹层
         function HiddenDiv() {
             $("#loading").hide();
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
		<a href="/r2k/topic/showTopics.do" target="_self"><s:text name="r2k.menu.topic"></s:text></a>
	  </div>
	<div class="content">
		<div id="container" class="ui-widget">
		  <div id="toolbar" class="ui-widget-header ui-corner-all">
		  	<button id="topic-save" >新增专题</button>	
		  	<button id="batDel" >批量删除</button>
		 	<div style="float: right;">
			  <form id="searchTopic" action="${ctx}/topic/showTopics.do" method="post">
			  	专题查询：<input class="ui-input" name="s_q" value="<s:property value='#parameters.s_q' />"/>
			  	<button id="topic-search">查询</button>	
			  </form>
		 	</div>
		  </div>
		 <form id="form-list" action="${ctx}/topic/showTopics.do" method="post">
		 	<input type="hidden" name="s_q" value="<s:property value='#parameters.s_q' />" />
		  <table id="table-list" class="ui-widget ui-widget-content gridBody">
		    <thead>
		      <tr class="ui-widget-header ">
		      	<th width="5%"><input id="checkAll" type="checkbox"/>全选</th>
		        <th width="12%">专题名称</th>
		        <th width="23%">简介</th>
		        <th width="15%" >最后更新时间</th>
		        <th width="5%" >状态</th>
		        <th width="5%" >类型</th>
		        <th width="5%" <s:if test="pageType == 'show'">class="sortCol"</s:if> sortColumn="Position">显示顺序</th>
	            <th>操作</th>
		      </tr>
		    </thead>
		    <tbody>
		      <s:iterator value="page.result" var="topic">
				<tr>
					<td><s:if test="isPublished != 2"><input name="topicIds" type="checkbox" value="${id}" /></s:if></td>
					<td><s:property value="topicName"/></td>
					<td><s:property value="description"/></td>
					<td><s:date name="updateTime" format="yyyy-MM-dd HH:mm:ss"/></td>
					<td>
						<s:if test="isPublished == 0">未发布</s:if>
						<s:if test="isPublished == 1">已发布</s:if> 
						<s:if test="isPublished == 2">已删除</s:if> 
					</td>
					<td>
						<s:if test="topicType == 0">普通</s:if>
						<s:if test="topicType == 1">高级</s:if> 
					</td>
					<td>
						<s:if test="isPublished != 2">
							<input name="${id}" class="spinner" style="width:30px" value="${position}" min="1" data-topic="{'id':'${id}','topicName':'${topicName}','description':'${description}','org':'${org}','icon':'${icon}','autoCreate':'${autoCreate}','incrCreate':'${incrCreate}','results':'${incrCreate}','createDate':'<s:date name="createDate" format="yyyy-MM-dd HH:mm:ss" timezone="UTC"/>','query':'${condition.query}','sort':'${condition.sort}','isPublished':'${isPublished}'}">
						</s:if>
						<s:if test="isPublished == 2">
							<input type="text" style="width:30px;border:none;margin-left:5px;" value="${position}" readonly="readonly" />
						</s:if>
					</td>
					<td>
						<s:if test="isPublished != 2">
						<a href="#" onclick="checkBeforeCreate('${id}','${topicName}','${description }','${condition.query}','${condition.sort}','<s:date name="updateTime" format="yyyy-MM-dd HH:mm:ss" />','check');">[查看生成条件]</a>
						<s:if test="autoCreate == 0">
						<a href="#" onclick="putTopicContent('${id}');">
							<s:if test="isPublished == 0">[生成]</s:if>
							<s:elseif test="isPublished == 1">[重新生成]</s:elseif>
						</a>
						</s:if>
						</s:if>
						<s:if test="isPublished == 1">
							<a href="#" onclick="showTopicContents('${id}','${topicName}','${description }','${condition.query}','${condition.sort}','<s:date name="updateTime" format="yyyy-MM-dd HH:mm:ss"/>','show');">[查看专题内容]</a>
						</s:if>
						<s:if test="isPublished != 2">
						<a href="#" onclick="updateTopic('${id}');">[修改]</a></s:if>
					</td>
				</tr>
			  </s:iterator>
		    </tbody>
		  </table>
		  <simpletable:pageToolbar page="${page}"></simpletable:pageToolbar>
		  </form>
		</div>
		<div id="topicInfo" style="display:none;" >
			<form id="selectedTopic" action="" method="post">
				<input id="t_id" type="hidden" name="topic.id" value="" />
				<input id="t_name" type="hidden" name="topic.topicName" value="" />
				<input id="t_desc" type="hidden" name="topic.description" value="" />
				<input id="t_query" type="hidden" name="topic.condition.query" value="" />
				<input id="t_sort" type="hidden" name="topic.condition.sort" value="" />
				<input id="t_updateTime" type="hidden" name="topic.updateTime" value="" />
				<input id="t_pageType" type="hidden" name="pageType" value="" />
			</form>
		</div>
		<div id="dialog-delete" title="提示信息">
			<input type="hidden" id="deleteId">
			<div style="margin-top: 20px">确定要删除当前专题信息吗？</div>
		</div>
		<div id="dialog-message" title="提示信息">
			<span id="tipmsg" style="margin:5% auto;"></span>
		</div>
	</div>
	</div>
	<div id="loading"><img src="${ctx}/images/ui-anim_basic_16x16.gif" alt="" /> <span style="font-weight: bolder;">正在生成专题,请稍候...</span></div>
	<div class="footer"><%@ include file="/commons/footer.jsp" %></div>
  </body>
</html>
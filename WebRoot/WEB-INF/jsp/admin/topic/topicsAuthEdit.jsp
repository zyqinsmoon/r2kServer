<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib tagdir="/WEB-INF/tags/simpletable" prefix="simpletable"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" scope="page"/>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
  <head>
    <title>专题授权</title>
    <%@ include file="/commons/meta.jsp" %>
	<script>
	$(function() {
		//创建simpleTable
  		/*var sortColumns = "<s:property value='#parameters.sortColumns'/>";
  			if(sortColumns == null || sortColumns == ""){
  				sortColumns = " ID desc";
  			}
  		window.simpleTable = new SimpleTable('form-list',${page.thisPageNumber},${page.pageSize},sortColumns);*/
  		$('#startDateTemp').datepicker({
  		  changeMonth: true,
      	  changeYear: true,
      	  minDate:0,
      	  onSelect:function(dataText){
      		  var endDate = $('#endDateTemp').val();
      		  var startTime = new Date(dataText);
      		  if(endDate != null && endDate != ""){
      		  	  var endTime = new Date(endDate);
      			  if(endTime.getTime() < startTime.getTime()){
      			  	$("#tipmsg").html("").html("授权结束时间不能早于授权开始时间");
              	 	$( "#dialog-message" ).dialog("open");
      			  }
      		  }
      		  $('#endDateTemp').datepicker('option', 'changeMonth', true); 
  		  	  $('#endDateTemp').datepicker('option', 'changeYear', true); 
      		  $('#endDateTemp').datepicker('option', 'minDate', dataText); 
      	  }
      	});  
        $('#endDateTemp').datepicker({
        	changeMonth: true,
        	changeYear: true,
        	minDate:0}); 
  		
  		//新增设备
  		$( "#dialog-form-save" ).dialog({
  	      autoOpen: false,
  	      /*height: 350,	width: 450*/
  	      width: 520,
  	      modal: true,
  	      buttons: {
  	        "保存": function() {
  	        	$("#form-save").attr("action","${ctx}/topic/save.do").submit();
  	        	/*checkNewValidate();*/
  	        },
  	        "取消": function() {
  	        	cleanNewDevice();
  	        	$( this ).dialog( "close" );
  	        }
  	      },
  	      close: function() {
			cleanNewDevice();
  	    	$( this ).dialog( "close" );
  	      }
  	    });//end save dialog
  	    
  		//批量授权选择时间
  		$( "#dialog-auth" ).dialog({
  	      autoOpen: false,
  	      width: 800,
	      height:400,
  	      modal: true,
  	      buttons: {
  	        "保存": function() {
  	        	var startDate = $("#startDateTemp").attr("value");
  	        	var endDate = $("#endDateTemp").attr("value");
  	        	if(startDate > endDate){
  	        		$("#tipmsg").html("").html("开始日期不能大于结束日期，请重新输入。");
  					$( "#dialog-message" ).dialog( "open" );
  	        	}else if(startDate == null || startDate == ""){
  	        		$("#tipmsg").html("").html("开始日期不能为空，请重新输入。");
  					$( "#dialog-message" ).dialog( "open" );
  	        	}else if(endDate == null || endDate == ""){
  	        		$("#tipmsg").html("").html("结束日期不能为空，请重新输入。");
  					$( "#dialog-message" ).dialog( "open" );
  	        	}else{
	  	        	$("#startDate").attr("value",startDate);
	  	        	$("#endDate").attr("value",endDate);
	  	        	var checkboxs = $("input[name='topicAuthIds']:checked");
	  	        	var topicIds = [];
	  	        	for(var i = 0, len = checkboxs.length; i < len; i++){
	  	        		topicIds.push(checkboxs[i].value);
	  	        	}
	  	        	var org = "<s:property value='#parameters.topicAuthOrg' />";
	  	        	$.ajax({
		  	  			type:'post',
		  	  			traditional:true,
		  	  			url:"${ctx}/topic/saveTopicAuth.do",
		  	  			data:{"topicAuthIds":topicIds, "topicAuth.startDate":startDate, "topicAuth.endDate":endDate,"topicAuth.orgId":org},
		  	  			success:function(data){
		  	  				$("[aria-describedby='dialog-turn']").find(".ui-dialog-buttonset").css("width","200px");
			  	  			if(data == '1'){
			  	  				$("#turnmsg").html("").html("专题授权成功。");
								$( "#dialog-turn" ).dialog( "open" );
							}else if(data == '0'){
								$("#tipmsg").html("").html("专题授权失败。");
								$( "#dialog-message" ).dialog( "open" );
							}else if(data == '-1'){
								$("#tipmsg").html("").html("系统错误。");
								$( "#dialog-message" ).dialog( "open" );
							}
		  	  			}
	  	        	});
	  	        	
  	        	}
  	        },
  	        "取消": function() {
  	        	$( this ).dialog( "close" );
  	        }
  	      },
  	      close: function() {
  	    	$( this ).dialog( "close" );
  	      }
  	    });//end dialog-auth
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
  	    //信息提示框
  	  	$( "#dialog-turn" ).dialog({
			autoOpen: false,
			modal: true,
			buttons: {
				"确定": function() {
					$( this).dialog( "close" );
					location.reload();
				},
				"关闭": function() {
					$( this).dialog( "close" );
					location.reload();
				}/*,
				"已授权专题":function() {
					$( this).dialog( "close" );
					$.cookie("dynatree-active","_6",{path:"/"});
					$.cookie("dynatree-expand","_4",{path:"/"});
					$.cookie("dynatree-focus","_6",{path:"/"});
					window.location = "${ctx}/topic/showTopicAuthList.do";
				}*/
			}
		});//end message
  	    
  		//保存
	  	$( "#topic-save" )
	      .button()
	      .click(function() {
	          $( "#dialog-form-save" ).dialog( "open" );
	      });
		//复选框全选
  	    $("#checkAll").click(function(){
  	    	$('input[type="checkbox"][name="topicAuthIds"]').attr("checked",this.checked);
		});
	  //批量导入按钮
  	  $( "#saveTopicAuth" )
      .button()
      .click(function() {
    	  if(checkSelect()){
	    	  $( "#dialog-auth" ).dialog( "open" );
    	  }
      });
	  
	  $( "#startDateTemp" ).datepicker({changeMonth: true,changeYear: true}).datepicker("option","dateFormat","yy-mm-dd").datepicker($.datepicker.regional["zh-TW"]);
	  $( "#endDateTemp" ).datepicker({changeMonth: true,changeYear: true}).datepicker("option","dateFormat","yy-mm-dd").datepicker($.datepicker.regional["zh-TW"]);
	});//end init function 
	
	
	  //获取焦点事件
	  function focusIn(id){
		  $("#"+id+"_msg").html("");
	  }
	  //判断复选框是否空选
	  function checkSelect(){
		  var boxs = $('input[type="checkbox"][name="topicAuthIds"]');
		  var boxFlag = false;
		  if(boxs != null && boxs.length > 0){
			  for(var i = 0, len = boxs.length; i < len; i++){
				  if(boxs[i].checked ) boxFlag = true; 
			  }
			  
			  if(!boxFlag){
				  $("#tipmsg").html("").html("勾选专题不能为空。");
				  $( "#dialog-message" ).dialog( "open" );
			  }
		  }
		  return boxFlag;
	  }
	</script>
  </head>
  
  <body id="body">
   <%@ include file="/commons/header.jsp" %>
	<div class="left">
		<%@ include file="/commons/menu.jsp" %>
	</div>
	<div id="navLink">
		<a href="/r2k/org/findOrgList.do" target="_self"><s:text name="r2k.menu.org"></s:text></a> &gt; 专题授权
	</div>
	<div class="content">
		<div id="container" class="ui-widget">
		  <div id="toolbar" class="ui-widget-header ui-corner-all">
		  	<button id="saveTopicAuth" >批量授权</button>	
		  </div>
		  <form id="form-list" action="" method="post">
		  <input type="hidden" name="topicAuth.startDate" id="startDate">
		  <input type="hidden" name="topicAuth.endDate" id="endDate">
		  <table id="table-list" class="ui-widget ui-widget-content gridBody">
		    <thead>
		      <tr class="ui-widget-header ">
		      	<th><input id="checkAll" type="checkbox"/>全选</th>
		        <th>专题名称</th>
		        <th>专题描述</th>
		        <th>最后更新时间</th>
		      </tr>
		    </thead>
		    <tbody>
		      <s:iterator value="page.result" var="list">
				<tr>
					<td><input name="topicAuthIds" type="checkbox" value="${id}"/></td>
					<td><s:property value="topicName"/></td>
					<td><s:property value="description"/></td>
					<td><s:date name="updateTime" format="yyyy-MM-dd"/></td>
				</tr>
			  </s:iterator>
		    </tbody>
		  </table>
		  <simpletable:pageToolbar page="${page}"></simpletable:pageToolbar>
		  </form>
		</div>
		<div id="dialog-auth" title="批量授权">
			<div style="margin: 10% 10%;">
				<span style="float: left; width: 230px">
				授权开始时间：<input id="startDateTemp">
				</span>
				<span style="margin-left: 15%;">
				授权结束时间：<input id="endDateTemp">
				</span>
			</div>
		</div>
		<div id="dialog-message" title="提示信息">
			<span id="tipmsg" style="margin:5% auto;"></span>
		</div>
		<div id="dialog-turn" title="提示信息">
			<span id="turnmsg" style="margin:5% auto;"></span>
		</div>
	</div>
	<div class="footer"><%@ include file="/commons/footer.jsp" %></div>
  </body>
</html>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="com.apabi.r2k.admin.model.Device" %>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib tagdir="/WEB-INF/tags/simpletable" prefix="simpletable"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" scope="page"/>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
  <head>
    <title>设备列表</title>
    <%@ include file="/commons/meta.jsp" %>
	<script>
	  var checkBlankRegex = /^[\s\S]+$/;	//非空正则表达式
	  var checkNumRegex = /^[1-9]\d*$/;	//数字正则表达式
	  $(function() {
	   //创建simpleTable
  		var sortColumns = "<s:property value='#parameters.sortColumns'/>";
  			if(sortColumns == null || sortColumns == ""){
  				sortColumns = " d.ID desc";
  			}
  		window.simpleTable = new SimpleTable('form-list','${page.thisPageNumber}','${page.pageSize}',sortColumns);
  		//新增设备
  		$( "#dialog-form-save" ).dialog({
  	      autoOpen: false,
  	      /*height: 350,	width: 450*/
  	      width: 700,
  	      height: 400,
  	      modal: true,
  	      buttons: {
  	        "保存": function() {
  	        	checkNewValidate();
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
  		
  	    //修改设备
  	    $( "#dialog-form-update" ).dialog({
	      autoOpen: false,
	      width: 700,
 	      height: 400,
	      modal: true,
	      buttons: {
	        "保存": function() {
	        	checkUpdateValidate();
	        },
	        "取消": function() {
	        	cheanUpdateDevice();  
	        	$( this ).dialog( "close" );
	        }
	      },
	      close: function() {
	    	  cheanUpdateDevice();
	    	  $( this ).dialog( "close" );
	      }
	    });//end update dialog
  		//导入弹层	    
  	  $( "#dialog-form-import" ).dialog({
	      autoOpen: false,
	      height: 260,
	      width: 550,
	      modal: true,
	      buttons: {
	        "保存": function() {
	        	if($("#devfile").attr("value") == ''){
	        		$("#dialog-message").html("").html("上传内容不能为空。");
	            	$("#dialog-message").dialog("open");
	        	}else{
			        var loading=new ol.loading({id:"body"});
			        loading.show();
				    $("#devAuth-import").ajaxSubmit({
			            async: false,
			            error: function(request) {
			            	$("#devAuth-import").resetForm();
			            	$( "#dialog-form-import" ).dialog( "close" );
			            	loading.hide();
			            	$("#dialog-message").html("").html("上传异常。");
			            	$( "#dialog-message" ).dialog("open");
			            },
			            success: function(data) {
			            	$("#devAuth-import").resetForm();
			            	$("#dialog-form-import").dialog( "close" );
			            	loading.hide();
			            	$("#dialog-import-message").html("").html(data.replace(/"/g,''));
			            	$( "#dialog-import-message" ).dialog("open");
			            }
		        	});
	        	}
	        },
	        "取消": function() {
	        	$("#devAuth-import").resetForm();
	          	$( this ).dialog( "close" );
	        }
	      },
	      close: function() {
	    	$("#devAuth-import").resetForm();
	        $("#resAuth-import").resetForm();
	      }
	    });//end import dialog
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
	    //信息弹层
	  	$( "#dialog-import-message" ).dialog({
			autoOpen: false,
			modal: true,
			buttons: {
				"确定": function() {
					$( this).dialog( "close" );
					location.reload();
				}
			},
	     	close: function() {
	     		$( this).dialog( "close" );
	     		location.reload();
	        }
		});//end message dialog
		//删除弹层
	  	$( "#dialog-delete" ).dialog({
			autoOpen: false,
			height: 200,
		    width: 350,
			modal: true,
			buttons: {
				"确定": function() {
					var idTxt = $("#deleteId").attr("value");
					var bAll = $('#isDeleteAll').is(':checked');
					var btxt = (bAll ? 1 : 0); 
					window.location = "${ctx}/dev/delete.do?id="+idTxt+"&isDeleteAll="+btxt;
				},
				"取消": function() {
					$( this).dialog( "close" );
				}
			},
	     	close: function() {
	     		$( this).dialog( "close" );
	        }
		});//end message dialog
  	    //保存
	  	$( "#device-save" )
	      .button()
	      .click(function() {
	          $( "#dialog-form-save" ).dialog( "open" );
	      });
	  	//查询
	  	$( "#device-search" )
	      .button()
	      .click(function() {
	    	  $("#form-search").submit();
  		  });
	  	//打开上传窗口
	  	$( "#device-list-save" )
	      .button()
	      .click(function() {
		  	  $("#dialog-form-import").dialog( "open" ); 
  		  });
  		//查询文本框回车事件
  	  	$( "#queryIdOrName" )
  	      .keyup(function(event){
  	    	  if(event.keyCode ==13){
  	    	    $("#form-search").submit();
  	    	  }
  	    	});
	  });//end init function 
	  
	  //打开机构修改对话框
	  function updateDevice(id){
	  	$.ajax({
			type:'post',
			url:"${ctx}/dev/toUpdate.do",
			data:{"id":id},
			success:function(data){
				if(data != null){
					$("#update_id").attr("value", data.id);
					$("#update_deviceId").attr("value", data.deviceId);
					$("#update_temp_name").attr("value", data.deviceName);
					$("#update_deviceName").attr("value", data.deviceName);
					$("#update_orgId").attr("value", data.orgId);
					$("#update_groupName").attr("value", data.groupName);
					$("#update_mac").attr("value", data.mac);
					$("#update_makerId option[value='"+data.makerId+"']").attr("selected", true);
					if(data.isOnline == 1){
						$("#update_isOnline").attr("value","1");
					}else{
						$("#update_isOnline").attr("value","0");
					} 
					
					$("#dialog-form-update").dialog( "open" );
					$("#update_deviceType option[value='"+data.deviceType.id+"']").attr("selected", true);
				}else{
					alert("获取机构信息失败！");
				}
			}
		}); 
	  }
	  //删除设备
	  function deleteDevice(id){
		  $("#deleteId").attr("value",id);
		  $("#dialog-delete").dialog( "open" ); 
	  }
	  //获取焦点事件
	  function focusIn(id){
		  $("#"+id+"_msg").html("");
	  }
	  //验证设备号是否存在
	  function checkDeviceId(deviceId){
		  var deviceIdVal = $("#"+deviceId).val();
		  if( checkBlankRegex.test(deviceIdVal)){
			  $.ajax({
				type:'post',
				url:"${ctx}/dev/checkDeviceId.do",
				data:{"deviceId":deviceIdVal},
				success:function(data){
					if(data == '1'){
						
					}else if(data == '0'){
						$("#"+deviceId+"_msg").html("").html("设备Id已存在，请重新输入。");
					}else if(data == '-1'){
						$("#"+deviceId+"_msg").html("").html("系统错误。");
					}
				}
			  });
		  }else{
			  $("#"+deviceId+"_msg").html("").html("设备Id不能为空。");
		  }
	  }
	  //验证设备号是否存在
	  function checkDeviceName(deviceName){
		  var deviceNameVal = $("#"+deviceName).val();
		  if( checkBlankRegex.test(deviceNameVal)){
			  var flag = true;
			  if(deviceName == "update_deviceName"){
				  if(deviceNameVal == $("#update_temp_name").attr("value")){
					  flag = false;
				  }
			  }
			  if(flag){
				  $.ajax({
					type:'post',
					url:"${ctx}/dev/checkDeviceName.do",
					data:{"deviceName":deviceNameVal},
					success:function(data){
						if(data == '1'){
							
						}else if(data == '0'){
							$("#"+deviceName+"_msg").html("").html("设备名称已存在，请重新输入");
						}else if(data == '-1'){
							$("#"+deviceName+"_msg").html("").html("系统错误。");
						}
					}
				  });
			  }
		  }else{
			  $("#"+deviceName+"_msg").html("").html("设备名称不能为空。");
		  }
	  }
	  //清空新增设备信息
	  function cleanNewDevice(){
		  $("#new_deviceId").attr("value","");
		  $("#new_deviceName").attr("value","");
	      $("#new_groupName").attr("value","");
	      $("#new_orgId").attr("value","");
	      $("#new_mac").attr("value","");
	      
		  $("#new_deviceId_msg").attr("value","");
		  $("#new_deviceName_msg").attr("value","");
	      $("#new_groupName_msg").attr("value","");
	      $("#new_orgId_msg").attr("value","");
	      $("#new_mac_msg").attr("value","");
	  }
	  //清空错误提示信息
	  function cheanUpdateDevice(){
		  $("#update_deviceId_msg").html("");
		  $("#update_deviceName_msg").html("");
	      $("#update_orgId_msg").html("");
	      $("#update_groupName_msg").html("");
	      $("#update_mac_msg").html("");
	  }
	  //验证设备id和设备名称唯一性
	  //新增验证
	  function checkNewValidate(){
		  checkValidate($("#new_deviceId").attr("value"), $("#new_deviceName").attr("value"), 'new');
	  }
	  //更新验证
	  function checkUpdateValidate(){
		  checkValidate($("#update_deviceId").attr("value"), $("#update_deviceName").attr("value"), 'update');
	  }
	  //验证方法
	  function checkValidate(id, name, type){
		  var devNameVal = $("#"+type+"_deviceName").attr("value");
		  if( checkBlankRegex.test(devNameVal) ){
			  if(type == 'new'){
				  var devIdVal = $("#"+type+"_deviceId").attr("value");
				  if( checkBlankRegex.test(devIdVal) ){
					  $.ajax({
						type:'post',
						url:"${ctx}/dev/checkDeviceId.do",
						data:{"deviceId":id},
						success:function(data){
							if(data == '1'){
								$.ajax({
									type:'post',
									url:"${ctx}/dev/checkDeviceName.do",
									data:{"deviceName":name},
									success:function(data){
										if(data == '1'){
											$("#deviceInfo-save").attr("action", "${ctx}/dev/save.do");
											$("#deviceInfo-save").submit();
										}else if(data == '0'){
											$("#"+type+"_deviceName_msg").html("").html("设备名称已存在。");
											return;
										}else if(data == '-1'){
											$("#"+type+"_deviceName_msg").html("").html("设备名称验证失败。");
											return;
										}
									}
								});
							}else if(data == '0'){
								$("#"+type+"_deviceId_msg").html("").html("设备id已存在。");
								return;
							}else if(data == '-1'){
								$("#"+type+"_deviceId_msg").html("").html("设备id验证失败。");
								return;
							}
						}
					  });//end ajax
				  }else{
					  $("#"+type+"_deviceId_msg").html("").html("设备Id不能为空。");
				  }
			  }else if(type == 'update'){
				  var flag = true;
					if(name == $("#update_temp_name").attr("value")){
						$("#deviceInfo-update").attr("action","${ctx}/dev/update.do");
					  	$("#deviceInfo-update").submit();
						flag = false;
					}
					if(flag){
					  	$.ajax({
							type:'post',
							url:"${ctx}/dev/checkDeviceName.do",
							data:{"deviceName":name},
							success:function(data){
								if(data == '1'){
									$("#deviceInfo-update").attr("action","${ctx}/dev/update.do");
								  	$("#deviceInfo-update").submit();
								}else if(data == '0'){
									$("#"+type+"_deviceName_msg").html("").html("设备名称已存在。");
									return;
								}else if(data == '-1'){
									$("#"+type+"_deviceName_msg").html("").html("设备名称验证失败。");
									return;
								}
							}
						});
					  }
					}
		  }else{
			  $("#"+type+"_deviceName_msg").html("").html("设备名称不能为空。");
			  return;
		  }
	  }
	  //显示修改框
	  function showUpdate(spanNode, inputNode, updateButton, saveButton, hideId, canselButton){
		  $('#'+spanNode).hide();
		  $('#'+saveButton).hide();
		  var txt = $('#' + hideId).attr('value');
		  $('#'+inputNode).attr('value', txt).css('display','block');
		  $('#'+updateButton).show();
		  $('#'+canselButton).show();
	  }
	  //修改设备名称
	  function updateDeviceName(devId, nodeId, hideId, spanNode, updateButton, saveButton, canselButton){
		  var nodetxt = $('#'+nodeId).attr('value');
		  var hidetxt = $('#'+hideId).attr('value');
		  if(nodetxt != hidetxt){
			  $.ajax({
				  type:'post',
				  url:'${ctx}/dev/updateDeviceName.do',
				  data:{'id':devId, 'deviceName':nodetxt},
				  success:function(data){
					if(data == -1){
						$("#dialog-message").html("更新设备名称错误。");
		            	$("#dialog-message").dialog("open");
					} else if(data == 0){
						$("#dialog-message").html("设备名称已存在，请重新输入。");
		            	$("#dialog-message").dialog("open");
					} else if(data == 1){
						$('#'+hideId).attr('value', nodetxt);
						$('#'+nodeId).hide();
						$('#'+updateButton).hide();
						$('#'+canselButton).hide();
						$('#'+spanNode).html(nodetxt).show();
						$('#'+saveButton).show();
					}
				  }
			  });
		  } else {
			$('#'+spanNode).show();
			$('#'+saveButton).show();
			$('#'+nodeId).hide();
			$('#'+updateButton).hide();
			$('#'+canselButton).hide();
		  }
	  }
	  //取消修改设备名称
	  function updateNameCansel(spanNode, inputNode, updateButton, saveButton, canselButton){
		$('#'+spanNode).show();
		$('#'+saveButton).show();
		$('#'+inputNode).attr('value','').hide();
		$('#'+updateButton).hide();
		$('#'+canselButton).hide();
		
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
		  <a href="/r2k/dev/show.do" target="_self"><s:text name="r2k.menu.device"></s:text></a>
	  </div>	
	  <div class="content">
		  <div id="toolbar" class="ui-widget-header ui-corner-all" style=" float:left; width:100%;">
		  	<%--<button id="device-save" style="float:left">新增设备</button>	
		  	<button id="device-list-save" style="float:left">导入设备列表</button>	
		 	--%>
		 	<div style="float: right; margin-right:20px;">
			  <form id="form-search" action="${ctx}/dev/show.do" method="post"><%--
			  	按<%=Device.ALIAS_DEVICE_ID%>：<input class="ui-input" name="s_deviceId" value="<s:property value='#parameters.s_deviceId' />"/>
			  	按<%=Device.ALIAS_DEVICE_NAME%>：<input class="ui-input" name="s_deviceName" value="<s:property value='#parameters.s_deviceName' />"/>
			  	--%>
			  	按设备id或设备名称:<input class="ui-input" id="queryIdOrName" name="s_queryIdOrName" value="<s:property value='#parameters.s_queryIdOrName' />"/>
			  	<button id="device-search">查询</button>	
			  </form>
		 	</div>
		  </div>
		  <div id="container" class="ui-widget">
		  <form id="form-list" action="${ctx}/dev/show.do" method="post">
		  <table id="table-list" class="ui-widget ui-widget-content gridBody">
		    <thead>
		      <tr class="ui-widget-header ">
		        <th field="deviceId" sortColumn="DEVICE_ID" style="width: 20%;"><%=Device.ALIAS_DEVICE_ID%></th>
		        <th field="deviceName" sortColumn="DEVICE_NAME" style="width: 13%;"><%=Device.ALIAS_DEVICE_NAME%></th>
		        <th style="width: 8%;">设备类型</th>
		        <th field="isOnline" sortColumn="IS_ONLINE" style="width: 5%;"><%=Device.ALIAS_IS_ONLINE%></th>
		        <th field="ipv4" sortColumn="IPV4" style="width: 10%;">IP地址</th>
		        <th field="lastDate" sortColumn="LAST_DATE" style="width: 12%;"><%=Device.ALIAS_LAST_DATE%></th>
		        <th style="width: 5%;">现版本</th>
		        <th style="width: 6%;">待升级版本</th>
	            <th style="width: 15%;">操作</th>
		      </tr>
		    </thead>
		    <tbody>
		      <s:iterator value="page.result" var="dev">
				<tr>
					<td><s:property value="deviceId"/></td>
					<td>
						<span id="spanName${id}"><s:property value="deviceName"/></span>
						<s:if test="deviceType != 'slave'">
						<input id="deviceName${id}" type="text" value="<s:property value="deviceName"/>" style="display: none;margin-bottom: 5px">
						<input id="hideName${id}" type="hidden" value="<s:property value="deviceName"/>">
						<a id="updateName${id}" href="#" style="display: none;color: #1c769a;" 
							onclick="updateDeviceName('${id}', 'deviceName${id}', 'hideName${id}', 'spanName${id}', 'updateName${id}', 'saveName${id}', 'canselSaveName${id}');">[保存]</a>
						<a id="canselSaveName${id}" href="#" style="display: none;color: #1c769a;" 
							onclick="updateNameCansel('spanName${id}', 'deviceName${id}', 'updateName${id}', 'saveName${id}', 'canselSaveName${id}');">[取消]</a>
						<a id="saveName${id}" style="float: right; color: #1c769a;" href="#" 
							onclick="showUpdate('spanName${id}', 'deviceName${id}', 'updateName${id}', 'saveName${id}','hideName${id}', 'canselSaveName${id}');">[修改]</a>
						</s:if>
					</td>
					<td>
						<s:property value="deviceTypeMap.get(deviceType).value"/>
					</td>
					<s:if test="isOnline == '在线'">
						<td class='status-true'><s:property value="isOnline"/></td>
					</s:if><s:elseif test="isOnline == '离线'">
						<td class='status-false'><s:property value="isOnline"/></td>
					</s:elseif>
					<td><s:property value="ipv4"/></td>
					<td><s:date name="lastDate" format="yyyy-MM-dd HH:mm:ss"/></td>
					<td><s:property value="curVersion"/></td>
					<td><s:property value="toVersion"/></td>
					<td>
						<%--<a href="#" onclick="updateDevice('${id}');">[修改]</a>
						--%><a href="#" onclick="deleteDevice('${id}');">[删除]</a>
						<s:if test="deviceType == 'Android-Large'|| deviceType == 'Android-Portrait'">
							<a href="${ctx}/pub/device.do?deviceId=${deviceId}&deviceName=${deviceName}&devType=${deviceType}" >[内容管理]</a>
							<a href="${ctx}/menu/device.do?deviceId=${deviceId}&deviceName=${deviceName}&devType=${deviceType}" >[菜单管理]</a>
							<a href="${ctx}/config/showByDevId.do?deviceId=${deviceId}&deviceName=${deviceName}&deviceType=${deviceType}" >[设置]</a>
						</s:if>
					</td>
				</tr>
			  </s:iterator>
		    </tbody>
		  </table>
		  <simpletable:pageToolbar page="${page}"></simpletable:pageToolbar>
		  </form>
		</div>
		
		<div id="dialog-message" title="提示信息">
		</div>
		<div id="dialog-import-message" title="提示信息">
		</div>
		<div id="dialog-delete" title="提示信息">
			<input type="hidden" id="deleteId">
			<div style="margin-top: 20px">确定要删除当前设备信息吗？</div>
			<div style="margin-top: 7%;">
			<input type="checkbox" id="isDeleteAll"><strong>清空设置</strong>
			<font style="color: red; display: block;">(勾选将删除设备相关的菜单、内容、发布、设置等全部信息)</font>
			</div>
		</div>
		</div>
	</div>
	</div>
	<div class="footer"><%@ include file="/commons/footer.jsp" %></div>
  </body>
</html>
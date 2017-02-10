<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="com.apabi.r2k.admin.model.Device" %>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib tagdir="/WEB-INF/tags/simpletable" prefix="simpletable"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" scope="page"/>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
  <head>
    <title>触摸屏设置</title>
    <%@ include file="/commons/meta.jsp" %>
    <style type="text/css">
    	.config_panel{margin-left: 5%; margin-top: 5%;}
    	.config_hr{width: 85%; margin-top: 1%; margin-bottom: 1%;}
    	.config_div_label{margin-bottom: 2%;}
    	.config_label{margin-left: 5%;}
    </style>
  </head>
    <body>
     <%@ include file="/commons/header.jsp" %>
	  <div class="wrapper">
	  <div class="left">
	  <%@ include file="/commons/menu.jsp" %>
	  </div>
	  <div id="navLink">
	  	<s:if test="redirectAction == 'showByLarge'">
		  <a href="/r2k/config/showByLarge.do" target="_self">触摸横屏设置</a>
	  	</s:if>
	  	<s:elseif test="redirectAction == 'showByPortrait'">
		  <a href="/r2k/config/showByPortrait.do" target="_self">触摸竖屏设置</a>
	  	</s:elseif>
	  	<s:elseif test="redirectAction == 'showByDevId'">
	  		<a href="/r2k/dev/show.do" target="_self"><s:text name="r2k.menu.device"></s:text></a> > 触摸屏设置(${deviceName})
<%--		  <a href="/r2k/config/showByDevId.do" target="_self">触摸屏设置</a>--%>
	  	</s:elseif>
	  </div>	
	  <div class="content">
		  <div id="container" class="ui-widget">
		  	<input type="hidden" id="DBStartTime" value="${deviceStartTime}">
		  	<input type="hidden" id="DBEndTime" value="${deviceEndTime}">
		  	<input type="hidden" id="DBwifiConfig" value="${deviceHotspot}">
			  <div id="info" style="display: block;">
			  	<div class="config_panel">
			  		<strong>触摸屏自动开关机设置：</strong>
			  		<s:if test="(deviceStartTime != null && deviceStartTime != '')|| (deviceEndTime != null && deviceEndTime != '')">
				  		<input name="timeSwitch" type="radio" value="0" disabled="disabled" class="config_label" checked="checked">开启
					    <input name="timeSwitch" type="radio" value="1" disabled="disabled" class="config_label">关闭
			  		</s:if>
			  		<s:else>
				  		<input name="timeSwitch" type="radio" value="0" disabled="disabled" class="config_label">开启
					    <input name="timeSwitch" type="radio" value="1" disabled="disabled" class="config_label" checked="checked">关闭
			  		</s:else>
			  		<hr class="config_hr">
			  		<div id="timeContainShow">
				  		<div class="config_div_label">
				  		<label class="config_label">开机时间：</label>
				  		<s:property value="deviceStartTime"/>
				  		</div>
				  		<div class="config_div_label">
				  		<label class="config_label">关机时间：</label>
				  		<s:property value="deviceEndTime"/>
				  		</div>
			  		</div>
		        </div>
		        <div class="config_panel">
			  		<strong>wifi设置：</strong>
			  		<hr class="config_hr">
			  		<s:if test="deviceHotspot == 0">
			  			<div class="config_div_label">
				        <input type="radio" value="0" disabled="disabled" class="config_label" checked="checked">开启
				        <input type="radio" value="1" disabled="disabled" class="config_label">关闭
				     	</div>
			  		</s:if>
			  		<s:elseif test="deviceHotspot == 1">
			  			<div class="config_div_label">
				        <input type="radio" value="0" disabled="disabled" class="config_label">开启
				        <input type="radio" value="1" disabled="disabled" class="config_label" checked="checked">关闭
				     	</div>
			  		</s:elseif>
			  		<s:else>
			  			<div class="config_div_label">
				        <input type="radio" value="0" disabled="disabled" class="config_label">开启
				        <input type="radio" value="1" disabled="disabled" class="config_label" checked="checked">关闭
				     	</div>
			  		</s:else>
		        </div>
		         <div id="toolbar" style="margin: 5% 0 2% 15%;">
		        	<span style="margin-left: 2%;">
		        	<button id="config-update">修改</button>
		        	</span>
		        </div>
			  </div>
			  <div id="update" style="display: none;">
				<form id="form-info" method="post">
					<input type="hidden" name="deviceType" value="${deviceType}">
					<input type="hidden" name="deviceId" value="${deviceId}">
					<input type="hidden" name="deviceName" value="${deviceName}">
					<input type="hidden" name="redirectAction" id="redirectAction" value="${redirectAction}">
					<div class="config_panel">
				  		<strong>触摸屏自动开关机设置：</strong>
				  		<input name="timeControl" type="radio" value="0" class="config_label" onclick="changeTimeSwitch(this,'timeContainControl')">开启
					    <input name="timeControl" type="radio" value="1" class="config_label" onclick="changeTimeSwitch(this,'timeContainControl')">关闭
				  		<hr class="config_hr">
				  		<div id="timeContainControl">
					  		<div class="config_div_label">
					  		<label class="config_label">开机时间：</label>
					        <input name="deviceStartTime" id="startTime" value="${deviceStartTime}">
					  		<label class="config_label">(时间格式为：0-23小时，0-59分钟，中间用冒号分割，如：07:20)</label>
					        </div>
					  		<div class="config_div_label">
					  		<label class="config_label">关机时间：</label>
					        <input name="deviceEndTime" id="endTime" value="${deviceEndTime}">
					  		<label class="config_label">(时间格式为：0-23小时，0-59分钟，中间用冒号分割，如：07:20)</label>
					        </div>
				  		</div>
			        </div>
			        <div class="config_panel">
				  		<strong>wifi设置：</strong>
				  		<hr class="config_hr">
				  		<s:if test="deviceHotspot == 0">
				  			<div class="config_div_label">
					        <input name="deviceHotspot" type="radio" value="0" class="config_label" checked="checked">开启
					        <input name="deviceHotspot" type="radio" value="1" class="config_label">关闭
					        </div>
				  		</s:if>
				  		<s:elseif test="deviceHotspot == 1">
				  			<div class="config_div_label">
					        <input name="deviceHotspot" type="radio" value="0" class="config_label">开启
					        <input name="deviceHotspot" type="radio" value="1" class="config_label" checked="checked">关闭
					        </div>
				  		</s:elseif>
				  		<s:else>
				  			<div class="config_div_label">
					        <input name="deviceHotspot" type="radio" value="0" class="config_label">开启
					        <input name="deviceHotspot" type="radio" value="1" class="config_label" checked="checked">关闭
					        </div>
				  		</s:else>
			        </div>
				</form>
		        <div style="margin: 5% 0 2% 15%;">
		        	<span style="margin-left: 2%;">
		        	<button id="config-save">保存</button>
		        	</span>
		        	<span style="margin-left: 2%;">
		        	<button id="config-back">返回</button>
		        	</span>
		        </div>
			  </div>
		  </div>
		</div>
	</div>
	</div>
	<div id="dialog-message" title="提示信息">
		<p id="txt-message" style="margin-top: 20px"></p>
	</div>
	<div class="footer"><%@ include file="/commons/footer.jsp" %></div>
	<script type="text/javascript">
	$(function(){
		//信息提示弹层
		$('#dialog-message').dialog({
			autoOpen: false,
			modal: true,
			buttons: {
				Ok: function() {
					$(this).dialog('close');
				}
			}
		}); 
		
		showTime('timeSwitch', 'timeContainShow', 'timeControl', 'timeContainControl');
	});//init function
	
	//是否显示开关机设置
	function showTime(radioName, containId, timeRadioName, timeContainId){
		var txt = $('input[name="'+radioName+'"]:checked').attr('value');
		if(txt == 1){
			$('#'+containId).css('display','none');
			$('#'+timeContainId).css('display','none');
		} else if(txt == 0){
			$('#'+containId).css('display','block');
			$('#'+timeContainId).css('display','block');
		}
		$('input[name="'+timeRadioName+'"][value="'+txt+'"]').attr('checked',true);
	}
	//单选按钮：控制开关机设置
	function changeTimeSwitch(node, timeContainId){
		var txt = $(node).attr('value');
		//关闭
		if(txt == 1){
			$('#'+timeContainId).css('display','none');
			$('#startTime').attr('value','');
			$('#endTime').attr('value','');
		} else if(txt == 0){
			$('#'+timeContainId).css('display','block');
		}
	}
	
	$('#config-update')
	    .button()
	    .click(function() {
	    	$('#update').show();
	    	$('#info').hide();
	    	$('#toolbar').hide();
	    });
	$('#config-save')
	    .button()
	    .click(function() {
	    	var txt = $('input[name="timeControl"]:checked').attr('value');
			//关闭
			if(txt == 1){
	    		var actionName = $('#redirectAction').attr('value');
	    		if(actionName == 'showByLarge' || actionName == 'showByPortrait'){
			    	$('#form-info').attr('action', '${ctx}/config/saveByDevType.do').submit();
	    		} else if(actionName == 'showByDevId'){
			    	$('#form-info').attr('action', '${ctx}/config/saveByDevId.do').submit();
	    		}
			} else if(txt == 0){
		    	var startFlag = checkTime('startTime');
		    	if(startFlag){
			    	var endFlag = checkTime('endTime');
			    	if(endFlag){
			    		if(checkStartEndTime('startTime','endTime')){
				    		var actionName = $('#redirectAction').attr('value');
				    		if(actionName == 'showByLarge' || actionName == 'showByPortrait'){
						    	$('#form-info').attr('action', '${ctx}/config/saveByDevType.do').submit();
				    		} else if(actionName == 'showByDevId'){
						    	$('#form-info').attr('action', '${ctx}/config/saveByDevId.do').submit();
				    		}
			    		} else {
			    			$('#txt-message').html("开机时间不能大于关机时间！");
							$('#dialog-message').dialog('open');
			    		}
			    	}
		    	}
				
			}
	    });
	$('#config-back')
	    .button()
	    .click(function() {
	    	$('#startTime').attr('value',$('#DBStartTime').attr('value'));
	    	$('#endTime').attr('value',$('#DBEndTime').attr('value'));
	    	var hotspotVal = $('#DBwifiConfig').attr('value');
	    	if(hotspotVal != '' && hotspotVal != null && hotspotVal != undefined){
		    	$('input[name="deviceHotspot"][value="'+hotspotVal+'"]').attr('checked',true);
	    	}
	    	$('#update').hide();
	    	$('#info').show();
	    	$('#toolbar').show();
	    });
		//检查时间格式
		function checkTime(id){
			var flag = true;
			var txt = $('#' + id).attr('value');
			var timeRegex = /^(([01]?[0-9])|(2[0-3])):[0-5]?[0-9]$/;
			if(!timeRegex.test(txt)){
				$('#txt-message').html("输入的时间格式不正确！");
				$('#dialog-message').dialog('open');
				flag = false;
			}
			return flag;
		}
		//判断开机时间是否小于关机时间
		function checkStartEndTime(startId, endId){
			var flag = true;
			var startval = $('#'+startId).attr('value');
			var endval = $('#'+endId).attr('value');
			var starttime = startval.split(':'); 
			var endtime = endval.split(':');
			var starthourval = parseInt(starttime[0]);
			var endhourval = parseInt(endtime[0]);
			if(starthourval > endhourval){
				flag = false;	
			} else if(starthourval == endhourval){
				var startminval = parseInt(starttime[1]);
				var endminval = parseInt(endtime[1]);
				if(startminval > endminval){
					flag = false;
				}
			}
			return flag;
		}
	</script>
  </body>
</html>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib tagdir="/WEB-INF/tags/simpletable" prefix="simpletable"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" scope="page"/>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
  <head>
    <title>组织机构列表</title>
      <%@ include file="/commons/meta.jsp" %>
  <script>
  $(document).ready(function() {     
      $('#authStartDate').datepicker({
    	  changeMonth: true,
    	  changeYear: true,
    	  minDate:0,
    	  dateFormat:"yy/mm/dd",
    	  onSelect:function(dataText){
    		  var endDate = $('#authEndDate').val();
    		  var startTime = new Date(dataText);
    		  if(endDate != null && endDate != ""){
    		  	  var endTime = new Date(endDate);
    			  if(endTime.getTime() < startTime.getTime()){
    			  	$("#upload-message").html("").html("授权结束时间不能早于授权开始时间");
            	 	$( "#dialog-message" ).dialog("open");
    			  }
    		  }
    		  $('#authEndDate').datepicker('option', 'changeMonth', true); 
    		  $('#authEndDate').datepicker('option', 'changeYear', true); 
    		  $('#authEndDate').datepicker('option', 'minDate', dataText); 
    	  }
    	});  
      $('#authEndDate').datepicker({
    	  changeMonth: true,
      	  changeYear: true,
    	  minDate:0,
    	  dateFormat:"yy/mm/dd"}); 
      $('#authStartDate').datepicker('option', 'showOn', 'button'); 
      $('#authEndDate').datepicker('option', 'showOn', 'button');
  }); 
  var checkBlankRegex = /^[\s\S]+$/;	//非空正则表达式
  var checkNumRegex = /^\d+$/;	//数字正则表达式
  var chkOrgId = true;
  var chkOrgName = true;
  var chkDevNum = true;
  var oldAuth = new Array(4);
  var newAuth = new Array(4);
	  $(function() {
	   //创建simpleTable
  		var sortColumns = "<s:property value='#parameters.sortColumns'/>";
  			/*if(sortColumns == null || sortColumns == ""){
  				sortColumns = " ORG_ID desc";
  			}*/
  		window.simpleTable = new SimpleTable('form-list','${page.thisPageNumber}','${page.pageSize}',sortColumns);
	  });
	  $(function() {
	    $( "#dialog-form-save" ).dialog({
	      autoOpen: false,
	      height: 350,
	      width: 700,
	      modal: true,
	      buttons: {
	        "保存": function() {
	        	//为隐藏的areaCode赋值
	        	getAreaCode('save-province', 'save-city', 'save-district', 'save-areaCode');
	        	
	        	var loading=new ol.loading({id:"body"});
	          var bValid = checkValidate("new_orgName","new","new_deviceNum");
	          if (bValid && chkOrgId && chkOrgName && chkDevNum) {
	        	  $.ajax({
	      			type:'post',
	      			url: "${ctx}/org/checkOrgId.do",
	      			data:{"orgId":$("#new_orgId").val()},
	      			success:function(data){
	      				if(data == 1){
	      					$.ajax({
	      		      			type:'post',
	      		      			url: "${ctx}/org/checkOrgName.do",
	      		      			data:{"orgName":$("#new_orgName").val()},
	      		      			success:function(data){
		      		      			if(data == 1){
				      					$.ajax({
				      		                cache: true,
				      		                type: "POST",
				      		                url:'${ctx}/org/saveOrg.do',
				      		                data:$('#orgInfo-save').serialize(),// 你的formid
				      		                async: false,
				      		                error: function(request) {
				      		                    alert("保存错误。");
				      		                },
				      		              	beforeSend:function(){ 
					      		      			loading.show();
					      		      		}, 
					      		      		complete:function(){ 
					      		      			loading.hide();
					      		      		},
				      		                success: function(data) {
				      		                	$("#dialog-pwd").dialog("open");
				      							$("#input-pwd").attr("value",data);
				      		              		$("#cp-btn").zclip({
				      								path:"${ctx}/js/zeroClipboard/ZeroClipboard.swf",
				      		              			copy:$("#input-pwd").attr("value")
				      							});
				      		                }
				      		            });
				      					
		      		      			}else if(data == 0){
		      		      				$("#upload-message").html("").html("机构保存失败");
		      	                		$( "#dialog-message" ).dialog("open");
		    	      				}else if(data == -1){
		      		      				$("#upload-message").html("").html("机构保存错误");
		      	                		$( "#dialog-message" ).dialog("open");
		    	      				}
	      		      			}
	      					});
	      				}else if(data == 0){
	      					$("#upload-message").html("").html("机构保存失败");
  	                		$( "#dialog-message" ).dialog("open");
	      				}else if(data == -1){
  		      				$("#upload-message").html("").html("机构保存错误");
  	                		$( "#dialog-message" ).dialog("open");
	      				}
	      			}
	      		  });
	          }
	        },
	        "取消": function() {
	        	cleanNewOrg();
	        	$( this ).dialog( "close" );
	        }
	      },
	      close: function() {
	      	  cleanNewOrg();
	    	  $( this ).dialog( "close" );
	      }
	    });
	    
	    $( "#dialog-form-update" ).dialog({
		      autoOpen: false,
		      height: 350,
		      width: 700,
		      modal: true,
		      buttons: {
		        "保存": function() {
		        	getAreaCode('update-province', 'update-city', 'update-district', 'update-areaCode');
		          var bValid = checkValidate("update_orgName","update","update_deviceNum");
		          if (bValid && chkOrgId && chkOrgName && chkDevNum ) {
		        	  var orgNameVal = $("#update_orgName").val();
		        		  var nodes = $('#orgInfo-update input[name="enumCodes"]');
		        		  $(nodes).each(function(index,element){
	        	  	  			if(this.checked){
	        	  	  				newAuth[index] = 1;
	        	  	  			}else{
	        	  	  				newAuth[index] = 0;
	        	  	  			}
		        	  	  	});
		        		  $("#oldauth").attr("value",oldAuth);
		        		  $("#newauth").attr("value",newAuth);
		        	  if(orgNameVal == $("#update_temp_orgName").attr("value")){
		        		  $("#orgInfo-update").submit();
					  }else{
				          $.ajax({
	    		      			type:'post',
	    		      			url: "${ctx}/org/checkOrgName.do",
	    		      			data:{"orgName":$("#update_orgName").val()},
	    		      			success:function(data){
		      		      			if(data == 1){
		      		      				$("#orgInfo-update").submit();
		      		      			}else if(data == 0){
		      		      				$("#upload-message").html("").html("机构名称已存在，请重新输入。");
		      	                		$( "#dialog-message" ).dialog("open");
		    	      				}else if(data == -1){
		      		      				$("#upload-message").html("").html("机构保存错误");
		      	                		$( "#dialog-message" ).dialog("open");
		    	      				}
	    		      			}
	    					});
					  }
		          }
		        },
		        "取消": function() {
		        	cheanUpdateOrg();  
		        	$( this ).dialog( "close" );
		        }
		      },
		      close: function() {
		    	  cheanUpdateOrg();
		    	  $( this ).dialog( "close" );
		      }
		    });
		    
		$( "#dialog-message" ).dialog({
			autoOpen: false,
			modal: true,
			buttons: {
				Ok: function() {
					$( this).dialog( "close" );
					}
				}
			});
		//复制密码
		$( "#dialog-pwd" ).dialog({
			autoOpen: false,
			modal: true,
			buttons: {
				Ok: function() {
					history.go(0);
					}
				},
			close: function() {
				history.go(0);
	        }
		});
	 	//删除弹层
	  	$( "#dialog-delete" ).dialog({
			autoOpen: false,
			modal: true,
			buttons: {
				"确定": function() {
					window.location = "${ctx}/org/deleteOrg.do?org.id="+$("#deleteId").attr("value")+"&org.orgId="+$("#deleteOrgId").attr("value");
				},
				"取消": function() {
					$( this).dialog( "close" );
				}
			},
	     	close: function() {
	     		$( this).dialog( "close" );
	        }
		});//end delete dialog

	 	$( "#dialog-form-import" ).dialog({
		      autoOpen: false,
		      height: 400,
		      width: 700,
		      modal: true,
		      buttons: {
		        "保存": function() {
		        var loading=new ol.loading({id:"body"});
		        loading.show();
			    $("#resAuth-import").ajaxSubmit({
                error: function(request) {
                	$("#resAuth-import").resetForm();
                	$( "#dialog-form-import" ).dialog( "close" );
                	loading.hide();
                	$("#upload-message").html("").html("资源导入错误。");
              		$( "#dialog-message" ).dialog("open");
                },
                success: function(data) {
                	var text = eval(data);
                	//console.log(text);
                	$("#resAuth-import").resetForm();
                	$("#dialog-form-import").dialog( "close" );
                	loading.hide();
                	$("#upload-message").html("").html(text);   
                	var n = $("#upload-message");
                	$( "#dialog-message" ).dialog("open");
                }
            	});
		        },
		        "取消": function() {
		        	$("#resAuth-import").resetForm();
		          	$( this ).dialog( "close" );
		        }
		      },
		      close: function() {
		        $("#resAuth-import").resetForm();
		      }
		    });
	    
	    $( "#org-save" )
	      .button()
	      .click(function() {
	    	  initSelect('save-province', 'save-city', 'save-district', 'save-areaCode');
	    	  $("#orgInfo-save").attr("action", "${ctx}/org/saveOrg.do");
	          $("#dialog-form-save").dialog("open");
	      });
	    $( "#org-search" )
	      .button()
	      .click(function() {
	        $("#form-search").submit();
	      });
	  	//查询文本框回车事件
  	  	$( "#queryIdOrName" )
  	      .keyup(function(event){
  	    	  if(event.keyCode ==13){
  	    	    $("#form-search").submit();
  	    	  }
  	    	});
  	  	$('input[name="enumCodes"]').each(function(element){
  	  		$(this).change(function(){
  	  			if(this.checked){
  	  				$("#new_authType_msg").html("");
  	  				$("#update_authType_msg").html("");
  	  			}
  	  		});
  	  	});
  	  		
	    /*$( "#down-example" )
	      .button()
	      .click(function() {
	    	  window.location = "${ctx}/paper/downExaple.do";
	      });*/
	  });
	  
	  //导入资源授权列表
	  function importRes(orgId,orgName){
	  	$("#message-orgId").text(orgName);
	  	$("#message-orgName").text(orgId);
		$("#import_orgId").attr("value",orgId);
		$('#authStartDate').datepicker('option', 'minDate', 0); 
	    $('#authEndDate').datepicker('option', 'minDate', 0);
		$("#dialog-form-import").dialog( "open" );  
	  }
	  //专题授权
	  function authTopic(auth, topicAuthOrg){
		  if(auth == 0){
			  $("#upload-message").html("").html("专题未授权。");
        	  $( "#dialog-message" ).dialog("open");
		  }else if(auth == 1){
			  window.location='${ctx}/topic/showAllTopics.do?topicAuthOrg='+topicAuthOrg;
		  }
	  }
	  //验证orgId在数据库中是否存在
	  function checkOrgId(ctx, orgId){
		  var orgIdVal = $("#"+orgId).val();
		  if( checkBlankRegex.test(orgIdVal)){
			  $.ajax({
				type:'post',
				url:ctx + "/org/checkOrgId.do",
				data:{"orgId":orgIdVal},
				success:function(data){
					if(data == '1'){
						chkOrgId = true;
					}else if(data == '0'){
						$("#"+orgId+"_msg").html("").html("机构Id已存在，请重新输入。");
						chkOrgId = false;
					}else if(data == '-1'){
						$("#"+orgId+"_msg").html("").html("系统错误。");
						chkOrgId = false;
					}
				}
			  });
		  }else{
			  $("#"+orgId+"_msg").html("").html("机构Id不能为空。");
			  chkOrgId = false;
		  }
	  }
	  
	  //验证orgName在数据库中是否存在
	  function checkOrgName(orgName){
		  var orgNameVal = $("#"+orgName).val();
		  if( checkBlankRegex.test(orgNameVal)){
			  var lenByte = orgNameVal.replace(/[^\x00-\xff]/gi,'xx').length;
			  if(lenByte >= 200){
				  $("#"+orgName+"_msg").html("").html("机构名称长度超过200字符。");
				  chkOrgName = false;
			  }else{
				  $("#"+orgName+"_msg").html("");
				  var flag = true;
				  if(orgName == "update_orgName"){
					  if(orgNameVal == $("#update_temp_orgName").attr("value")){
						  flag = false;
					  }
				  }
				  if(flag){
					  $.ajax({
						type:'post',
						url:"${ctx}/org/checkOrgName.do",
						data:{"orgName":orgNameVal},
						success:function(data){
							if(data == '1'){
								chkOrgName = true;
							}else if(data == '0'){
								$("#"+orgName+"_msg").html("").html("机构名称已存在，请重新输入。");
								chkOrgName = false;
							}else if(data == '-1'){
								$("#"+orgName+"_msg").html("").html("系统错误。");
								chkOrgName = false;
							}
						}
					  });
				  }
			  }
		  }else{
			  $("#"+orgName+"_msg").html("").html("机构名称不能为空。");
			  chkOrgName = false;
		  }
	  }
	  function checkDeviceNum(deviceNum){
		  var deviceNumVal = $("#"+deviceNum).val();
		  if(!checkBlankRegex.test(deviceNumVal)){
			  $("#"+deviceNum+"_msg").html("").html("屏数量不能为空。");
			  deviceNumFlag = false;
			  chkDevNum = false;
		  }else{
			  if(checkNumRegex.test(deviceNumVal)){
				  if(deviceNumVal > 100000){
					  $("#"+deviceNum+"_msg").html("").html("屏数量超过限制。");
				  }else{
					  $("#"+deviceNum+"_msg").html("");
					  chkDevNum = true;
				  }
			  }else{
				  $("#"+deviceNum+"_msg").html("").html("输入格式不正确，请输入数字。");
				  deviceNumFlag = false;
				  chkDevNum = false;
			  }
		  }
	  }
	  
	  //清空机构注册信息
	  function cleanNewOrg(){
		  $("#new_paper").attr("value","0");
		  $("#new_ebook").attr("value","0");
		  $("#chkbox_new_paper").attr("checked",false);
		  $("#chkbox_new_ebook").attr("checked",false);
		  
		  $("#new_orgId").attr("value","");
	      $("#new_orgName").attr("value","");
	      $("#new_deviceNum").attr("value","");
	      
	      $("#new_orgId_msg").html("");
	      $("#new_orgName_msg").html("");
	      $("#new_authorizationType_msg").html("");
	      $("#new_deviceNum_msg").html("");
	      
	      $("#new_makerId option[value='1']").attr("selected", true);
	  }
	  
	  //清空错误提示信息
	  function cheanUpdateOrg(){
		  $("#update_orgId_msg").html("");
	      $("#update_orgName_msg").html("");
	      $("#update_authorizationType_msg").html("");
	      $("#update_deviceNum_msg").html("");
	  }
	  
	  //打开机构修改对话框
	  function updateOrg(ctx, orgId){
		resetUpdateOrg();
	  	$.ajax({
			type:'post',
			url:ctx + "/org/toUpdateOrg.do",
			data:{"orgId":orgId},
			cache:false,
			success:function(data){
				if(data != null){
					initSelect('update-province', 'update-city', 'update-district', 'update-areaCode', data.areaCode);
					$("#orgInfo-update").attr("action", ctx + "/org/updateOrg.do");
					$("#update_id").attr("value", data.id);
					$("#update_orgId").attr("value", data.orgId);
					$("#update_orgName").attr("value", data.orgName);
					$("#update_temp_orgName").attr("value", data.orgName);
					$("#update_deviceNum").attr("value", data.deviceNum);
					var enumlist = $("#orgInfo-update input[name='enumCodes']");
					var enumauths = data.enumAuthList;
					for(var i = 0, len = enumlist.length; i < len; i++){
						for(var j = 0, authlen = enumauths.length; j < authlen; j++){
							if(enumauths[j] != null){
								if($(enumlist[i]).attr("value") == enumauths[j]){
									$(enumlist[i]).attr("checked",true);
								}
							}
						}
					};
					for(var i = 0; i < 5; i++){
						oldAuth[i] = 0;
					}
					
					for(var i = 0, len = enumauths.length; i < len; i++){
						oldAuth[enumauths[i]-1] = 1;
					}
					
					$("#makerId option[value='"+data.makerId+"']").attr("selected", true);
					$("#dialog-form-update").dialog( "open" );				
				}else{
					$("#upload-message").html("").html("获取机构信息失败！");
              		$( "#dialog-message" ).dialog("open");
				}
			}
		}); 
	  }
	  
	  //复选框选中更改隐藏域里对应的值
	  function checkBoxChange(type){
		  var flag = false;
		  var boxs = $('input[name="enumCodes"]');
		  //console.log(boxs);
		  var boxFlag = false;
		  if(boxs != null && boxs.length > 0){
			  for(var i = 0, len = boxs.length; i < len; i++){
				  if(boxs[i].checked ) {
					  boxFlag = true; 
					  break;
				  }
			  }
			  if(!boxFlag){
				  $("#"+type+"_authType_msg").html("").html("请选择授权类型！");
				  //console.log($("#"+type+"_authType_msg"));
			  }
		  }
		  return boxFlag;
	  }
	  
	  //删除设备
	  /*function deleteOrg(orgId, id){
		  $("#deleteId").attr("value",id);
		  $("#deleteOrgId").attr("value",orgId);
		  $("#dialog-delete").dialog( "open" ); 
	  }
	  */
	  //获取焦点事件
	  function focusIn(id){
		  $("#"+id+"_msg").html("");
	  }
	  
	  //表单提交验证
	  function checkValidate(orgName, type, deviceNum){
		  //机构名称长度验证
		  var orgNameVal = $("#"+orgName).val();
		  if(checkBlankRegex.test(orgNameVal)){
			  var lenByte = orgNameVal.replace(/[^\x00-\xff]/gi,'xx').length;
			  if(lenByte >= 200){
				  $("#"+orgName+"_msg").html("").html("机构名称长度超过200字符。");
				  return false;
			  }else{
				  $("#"+orgName+"_msg").html("");
			  }
		  }else{
			  $("#"+orgName+"_msg").html("").html("机构名称不能为空。");
			  return false;
		  }
		  
		  //复选框空选验证
		  var boxFlag = false;
		  boxFlag = checkBoxChange(type);
		  if(!boxFlag){
			  return;
		  }
		  
		  //设备数的数字验证
		  var deviceNumFlag = true;
		  var deviceNumVal = $("#"+deviceNum).val();
		  if(!checkBlankRegex.test(deviceNumVal)){
			  $("#"+deviceNum+"_msg").html("").html("屏数量不能为空。");
			  deviceNumFlag = false;
		  }else{
			  if(checkNumRegex.test(deviceNumVal)){
				  if(deviceNumVal > 100000){
					  $("#"+deviceNum+"_msg").html("").html("屏数量超过限制。");
					  deviceNumFlag = false;
				  }else{
					  $("#"+deviceNum+"_msg").html("");
				  }
			  }else{
				  $("#"+deviceNum+"_msg").html("").html("输入格式不正确，请输入数字。");
				  deviceNumFlag = false;
			  }
		  }
		  
		  return deviceNumFlag;
	  }
	  //授权链接选择
	  function selectAuth(code, orgId, orgName){
		  if(code == 1){
			  
		  }else if(code == 2){
			  importRes(orgId, orgName);
		  }else if(code == 3){
			  window.location='${ctx}/topic/showAllTopics.do?topicAuthOrg='+orgId;
		  }else if(code == 4){
			  
		  }
	  }
	  
	  //重置更新机构对话框
	  function resetUpdateOrg(){
		  $(".update-enum").attr("checked",false);
	  }
	  
	  function copy(){
			var obj = document.getElementById("input-pwd"); 
			obj.select();    
			js = obj.createTextRange();    
			js.execCommand("Copy");
		}
	  //选择省
	  function selectProvince(id,cityId, districtId){
		  var txt = $('#'+id).attr('value');
		  var pid = (txt.split('_'))[0];
		  var url = '${ctx}/org/selectProvince.do';
		  $.ajax({
			  url:url,
			  type:'post',
			  data:{'provinceId':pid},
			  success:function(data){
				  if(data != null){
					  fillCity(cityId, data.citylist);
					  fillDistrict(districtId, data.districtlist);
				  }
			  }
		  });
	  }
	  //选择市
	  function selectCity(cityId, districtId){
		  var txt = $('#'+cityId).attr('value');
		  var pid = (txt.split('_'))[0];
		  var url = '${ctx}/org/selectCity.do';
		  $.ajax({
			  url:url,
			  type:'post',
			  data:{'provinceId':pid},
			  success:function(data){
				  if(data != null){
					  fillDistrict(districtId, data.districtlist);
				  }
			  }
		  });
	  }
	  //初始化select
	  function initSelect(provinceId, cityId, districtId, hideCodeId, updateCode){
		  if(updateCode != undefined){
			  var param = {'provinceId':updateCode};
			  $('#' + hideCodeId).attr('value',updateCode);
		  } else {
			  var param = {};
			  $('#' + hideCodeId).attr('value','101010100');
		  }
		  var url = '${ctx}/org/initSelect.do';
		  $.ajax({
			  url:url,
			  type:'post',
			  data:param,
			  success:function(data){
				  if(data != null){
					  fillProvince(provinceId, data.provincelist, data.provinceCode);
					  fillCity(cityId, data.citylist, data.cityCode);
					  fillDistrict(districtId, data.districtlist, data.districtCode);
				  }
			  }
		  });
	  }
	  //填充省下拉列表
	  function fillProvince(provinceId, provincelist, provinceCode){
		  var provincenode = $('#'+provinceId).empty();
		  $(provincelist).each(function(index,ele){
			  var opt = $('<option>');
			  var txt = ele.id + '_' + ele.areaCode;
			  if(txt == provinceCode){
				  $(opt).attr({'value':txt, 'selected':'selected'}).html(ele.areaName);
			  } else {
				  $(opt).attr({'value':txt}).html(ele.areaName);
			  }
			  $(provincenode).append(opt);
		  });
	  }
	  //填充市下拉列表
	  function fillCity(cityId, citylist, cityCode){
		  var citynode = $('#'+cityId).empty();
		  $(citylist).each(function(index,ele){
			  var opt = $('<option>');
			  var txt = ele.id + '_' + ele.areaCode;
			  if(txt == cityCode){
				  $(opt).attr({'value':txt, 'selected':'selected'}).html(ele.areaName);
			  } else {
				  $(opt).attr({'value':txt}).html(ele.areaName);
			  }
			  $(citynode).append(opt);
		  });
	  }
	  //填充县下拉列表
	  function fillDistrict(districtId, districtlist, districtCode){
		  var districtnode = $('#'+districtId).empty();
		  $(districtlist).each(function(index,ele){
			  var opt = $('<option>');
			  var txt = ele.id + '_' + ele.areaCode;
			  if(txt == districtCode){
				  $(opt).attr({'value':txt, 'selected':'selected'}).html(ele.areaName);
			  } else {
				  $(opt).attr({'value':txt}).html(ele.areaName);
			  }
			  $(districtnode).append(opt);
		  });
	  }
	  //为隐藏的areaCode赋值
	  function getAreaCode(provinceId, cityId, districtId, hideCodeId){
		  var areaCode;
		  var provinceTxt = $('#' + provinceId).attr('value').split('_');
		  var cityTxt = $('#' + cityId).attr('value').split('_');
		  var districtTxt = $('#' + districtId).attr('value').split('_');
		  var provinceCode = provinceTxt[1];
		  var cityCode = cityTxt[1];
		  var districtCode = districtTxt[1];
		  if(provinceCode < 10105){
			  areaCode = provinceCode + districtCode + cityCode;
		  } else {
			  areaCode = provinceCode + cityCode + districtCode;
		  }
		  $('#' + hideCodeId).attr('value', areaCode);
		  return areaCode;
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
			<a href="/r2k/org/findOrgList.do" target="_self"><s:text name="r2k.menu.org"></s:text></a>
	  </div>
	  <div class="content">
		  <div id="toolbar" class="ui-widget-header ui-corner-all" style=" float:left; width:100%;">
			  <button id="org-save" style="float:left" >新建机构</button>	
			  <div style="float: right; margin-right:20px;">
				  <form id="form-search" action="${ctx}/org/findOrgList.do" method="post">
				  	按机构id或机构名称:<input class="ui-input" id="queryIdOrName" name="s_queryIdOrName" value="<s:property value='#parameters.s_queryIdOrName' />"/>
				  	<button id="org-search" type="submit">查询</button>	
				  </form>
			  </div>
		  </div>
		  <div id="container" class="ui-widget">
		  <form id="form-list" action="/r2k/org/findOrgList.do" method="post">
		  <input type="hidden" name="s_orgId" value="<s:property value='#parameters.s_orgId'/>">
		  <input type="hidden" name="s_orgName" value="<s:property value='#parameters.s_orgName'/>">
		  <table id="table-list" class="ui-widget ui-widget-content gridBody">
		    <thead>
		      <tr class="ui-widget-header ">
		        <th style="width: 8%;" class="sortCol" field="orgId" sortColumn="ORG_ID">机构ID</th>
		        <th style="width: 10%;" class="sortCol" field="orgName" sortColumn="ORG_NAME">机构名称</th>
	            <th style="width: 10%;" class="sortCol" field="crtDate" sortColumn="CRT_DATE">创建时间</th>
				<th style="width: 8%;" class="sortCol">读书授权</th>
				<th style="width: 8%;" class="sortCol">报纸授权</th>
				<th style="width: 8%;" class="sortCol">专题授权</th>
				<th style="width: 8%;" class="sortCol">资讯授权</th>
				<th style="width: 8%;" class="sortCol">图片授权</th>
	            <th style="width: 8%;" class="sortCol" field="deviceNum" sortColumn="DEVICE_NUM">触摸屏数量</th>
	            <th >操作</th>
		      </tr>
		    </thead>
		    <tbody>
		      <s:iterator value="page.result" var="org">
				<tr>
					<td><s:property value="orgId"/></td>
					<td><s:property value="orgName"/></td>
					<td><s:date name="crtDate" format="yyyy-MM-dd"/></td>
					<s:if test="ebook==0">
						<td class="status-false">未授权</td>
					</s:if>
					<s:elseif test="ebook==1">
						<td class="status-true">已授权</td>
					</s:elseif>
					<s:if test="paper==0">
						<td class="status-false">未授权</td>
					</s:if>
					<s:elseif test="paper==1">
						<td class="status-true">已授权</td>
					</s:elseif>
					<s:if test="topic==0">
						<td class="status-false">未授权</td>
					</s:if>
					<s:elseif test="topic==1">
						<td class="status-true">已授权</td>
					</s:elseif>
					<s:if test="publish==0">
						<td class="status-false">未授权</td>
					</s:if>
					<s:elseif test="publish==1">
						<td class="status-true">已授权</td>
					</s:elseif>
					<s:if test="picture==0">
						<td class="status-false">未授权</td>
					</s:if>
					<s:elseif test="picture==1">
						<td class="status-true">已授权</td>
					</s:elseif>
					<td><s:property value="deviceNum"/></td>
					<td>
						<s:if test="isAdmin != 1">
							<a href="#" onclick="updateOrg('${ctx}', '${orgId}');">[修改]</a>
							<%--<a href="#" onclick="deleteOrg('${orgId}','${id}');">[删除]</a>--%>
						</s:if>
						<s:if test="paper==1">	
							<a href='#'" onclick="importRes('${orgId}','${orgName}');">[报纸授权]</a>
						</s:if>
						<s:if test="topic==1">	
							<a href='#'" onclick="authTopic('<s:property value="topic"/>', '<s:property value="orgId"/>');">[专题授权]</a>
						</s:if><!-- 
						<s:if test="publish==1">	
							<a href='#'">[资讯授权]</a>
						</s:if> -->
					</td>
				</tr>
			  </s:iterator>
		    </tbody>
		  </table>
		  <simpletable:pageToolbar page="${page}"></simpletable:pageToolbar>
		  </form>
		</div>
			
		<div style="display: none;">
			<div id="dialog-form-save" title="新建机构">
			  <form id="orgInfo-save" method="post" class="dialog-form">
			  	<input id="save-areaCode" name="org.areaCode" type="hidden" />
			  	<div class="field"><label class="field-label">机构id:</label>
		        <input class="ui-input field-input" name="org.orgId" id="new_orgId" onfocus="focusIn('new_orgId')" onblur="checkOrgId('${ctx}', 'new_orgId');" />
		        <span class="ui-tips-error" id="new_orgId_msg"></span></div>
				<div class="field"><label class="field-label">机构名称:</label>
		        <input class="ui-input field-input" name="org.orgName" id="new_orgName" onfocus="focusIn('new_orgName')" onblur="checkOrgName('new_orgName');"/>
		        <span class="ui-tips-error" id="new_orgName_msg"></span></div>
		        <div class="field"><label class="field-label">授权类型:</label>
		        <s:iterator value="enumlist" var="enum">
		        	<input name="enumCodes" id="new_authType_'<s:property value="enumCode"/>'" type="checkbox" value='<s:property value="enumCode"/>'/><s:property value="enumName"/>
		        </s:iterator>
		        <span class="ui-tips-error" id="new_authType_msg"></span></div>
<%--		        <div class="field"><label class="field-label">模板id:</label>--%>
<%--		        <s:select list="#{'1':'1','2':'2','3':'3'}" name="org.makerId" id="new_makerId" listKey="key" listValue="value" cssStyle="width: 50%;"></s:select></div>--%>
		        <div class="field"><label class="field-label">触摸屏数量:</label>
		        <input class="ui-input field-input" name="org.deviceNum" id="new_deviceNum" onfocus="focusIn('new_deviceNum')" onblur="checkDeviceNum('new_deviceNum');"/>
		        <span class="ui-tips-error" id="new_deviceNum_msg"></span></div>
		        <div class="field"><label class="field-label">所属区域:</label>
		        	<select id="save-province" name="province" onchange="selectProvince('save-province','save-city','save-district');"></select>
			        <select id="save-city" name="city" style="width: 100px;"	onchange="selectCity('save-city','save-district');"></select>
			        <select id="save-district" name="district" style="width: 100px;"></select>
			        <span class="ui-tips-error" id="new_orgId_msg"></span>
		        </div>
			  </form>
			</div>
			
			<div id="dialog-form-update" title="更新机构">
				<input id="update_temp_orgName" type="hidden">
			  <form id="orgInfo-update" method="post" class="dialog-form">
			  	<input id="update-areaCode" name="org.areaCode" type="hidden" />
			  	<input id="update_id" name="org.id" type="hidden" />
				<input id="oldauth" name="oldauth" type="hidden" />
			  	<input id="newauth" name="newauth" type="hidden" />
				<div class="field"><label class="field-label">机构id:</label>
		        <input class="ui-input field-input" name="org.orgId" id="update_orgId" readonly="readonly"/></div>
				<div class="field"><label class="field-label">机构名称:</label>
		        <input class="ui-input field-input" name="org.orgName" id="update_orgName" onfocus="focusIn('update_orgName')" onblur="checkOrgName('update_orgName');"/>
		        <span class="ui-tips-error" id="update_orgName_msg"></span></div>
		        <div class="field"><label class="field-label">授权类型:</label>
		        <s:iterator value="enumlist" var="enum">
		        	<input class="update-enum" name="enumCodes" id="update_authType_'<s:property value="enumCode"/>'" type="checkbox" value='<s:property value="enumCode"/>'/><s:property value="enumName"/>
		        </s:iterator>
		        <span class="ui-tips-error" id="update_authType_msg"></span></div>
<%--		        <div class="field"><label class="field-label">模板id:</label>--%>
<%--		        <s:select list="#{'1':'1','2':'2','3':'3'}" name="org.makerId" id="makerId" listKey="key" listValue="value" cssStyle="width: 50%;"></s:select></div>--%>
		        <div class="field"><label class="field-label">触摸屏数量:</label>
		        <input class="ui-input field-input" name="org.deviceNum" id="update_deviceNum" onfocus="focusIn('update_deviceNum')" onblur="checkDeviceNum('update_deviceNum');"/>
		        <span class="ui-tips-error" id="update_deviceNum_msg"></span></div>
		        <div class="field"><label class="field-label">所属区域:</label>
		        	<select id="update-province" name="province" onchange="selectProvince('update-province','update-city','update-district');"></select>
			        <select id="update-city" name="city" style="width: 100px;" onchange="selectCity('update-city','update-district');"></select>
			        <select id="update-district" name="district" style="width: 100px;"></select>
			        <span class="ui-tips-error" id="new_orgId_msg"></span>
		        </div>
			  </form>
			</div> 
			
		    <div id="dialog-form-import" title="导入授权列表" class="ui-dialog-content ui-widget-content">
				<form id="resAuth-import" name="admin" action="${ctx}/paper/upload.do" method="post"
					enctype="multipart/form-data" class="dialog-form">
					<s:hidden id="import_orgId" name="orgId"/>
					<div class="field"><label class="field-label">机构ID:</label>
					<span id="message-orgName"></span>
					</div>	
					<div class="field"><label class="field-label">机构名称:</label>
					<span id="message-orgId"></span>
					</div >
					<div class="field">
					<label class="field-label">授权开始时间:</label>
					<input class="ui-input field-input" type="text" id="authStartDate"  name="authStartDate" readonly="readonly"/>
					</div>
					<div class="field">
					<label class="field-label">授权结束时间:</label>
					<input class="ui-input field-input" type="text" id="authEndDate"  name="authEndDate" readonly="readonly" />
					</div>
					<div class="field">
						<label class="field-label">
							上传授权:
						</label>
						<input class="ui-input field-input" type="file" name="resource" id="efiles">
						<s:fielderror />
					</div>
					<div class="field">
						<label class="field-label">
							授权示例:
						</label>
						<a class="field-input"  href='#' onclick="window.location='${ctx}/paper/downExaple.do'">[<font color="red">下载 </font>]</a>
					</div>
				</form>
			</div>
			<div id="dialog-message" title="提示信息">
				<p id="upload-message"></p>
			</div>
			<div id="dialog-delete" title="提示信息">
				<input type="hidden" id="deleteId">
				<input type="hidden" id="deleteOrgId">
				<div style="margin-top: 20px">确定要删除当前机构信息吗？</div>
			</div>
			<div id="dialog-pwd" title="用户密码">
				<form method="post" class="dialog-form">
				<div class="field">
					<label class="field-label">
						密码:
					</label>
					<input class="ui-input field-input" id="input-pwd" readOnly="true"/>
					<input type="button" value="复制" id="cp-btn" onclick="copy()"/>
				</div>
				</form>
			</div>
		</div>
		</div>
	  </div>
	<div class="footer"><%@ include file="/commons/footer.jsp" %></div>
  </body>
</html>

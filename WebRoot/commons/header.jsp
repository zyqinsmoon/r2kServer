<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="s" uri="/struts-tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" scope="page"/>
<script type="text/javascript" src="${ctx}/js/zeroClipboard/jquery.zclip.js"></script>
 <script type="text/javascript" >
 	$(function(){
 		$( "#changeOrgDialog" ).dialog({
	      autoOpen: false,
	      height: 200,
	      width: 450,
	      modal: true
	    });
 		//信息提示框
  		$( "#dialog-changepsw-message" ).dialog({
		  autoOpen: false,
		  modal: true,
		  buttons: {
			"确定": function() {
				$( this).dialog( "close" );
			}
		  }
		});
		$("#dialog-resetpwd").dialog({
			autoOpen: false,
		  	modal: true
		});
		//重置密码复制
		$("#dialog-resetpwd").dialog({
			autoOpen:false,
			height: 180,
	      	width: 700,
			modal:true
			/*buttons:{
				"复制":function(){
				if(navigator.userAgent.toLowerCase().indexOf('ie') > -1){
					var obj = document.getElementById("reset_pwd");    
					obj.select();    
					js = obj.createTextRange();    
					js.execCommand("Copy");
				}
				}
			}*/	
		});
		//
		
		//重置密码消息提示框
		$("#dialog-resetpsw-message").dialog({
		  autoOpen: false,
		  modal: true,
		  buttons: {
			"确定": function() {
				//自动生成按钮
				var id = $("#pwdId").attr("value");
				$( this ).dialog( "close" );
				$.ajax({
				type:'post',
				url:"/r2k/user/resetsPwd.do",
				data:{"user.id":id},
				success:function(data){	
					if(data.flag == '1'){
						$("#dialog-resetpwd").dialog("open");
						$("#reset_pwd").attr("value",data.password);
						if(navigator.userAgent.toLowerCase().indexOf('ie') <= -1){
						// var clip = new ZeroClipboard.Client(); //初始化对象  
      					//	 clip.setHandCursor(true); //设置手型  
      					//	 clip.setText($("#reset_pwd").attr("value"));
      					//	 alert(clip.clipText);
     					//	 clip.glue("copy_button");
     					//	 alert("sss");
     					$("#copy_button").zclip({
     						path:"${ctx}/js/zeroClipboard/ZeroClipboard.swf",
     						copy:$("#reset_pwd").attr("value"),
     						afterCopy:function(){}
     					});
						}
					}else if(data.flag == '0'){
						$("#pwdtipmsg").html("").html(" 重置密码失败。");
						$( "#dialog-changepsw-message" ).dialog( "open" );
					}else if(data.flag == '-1'){
						$("#pwdtipmsg").html("").html(" 重置密码错误。");
						$( "#dialog-changepsw-message" ).dialog( "open" );
					}
				}
			});
			},
			"取消":function(){
				$( this).dialog( "close" );
			}
		  }
		});
 		//修改密码
		$( "#dialog-updatepwd" ).dialog({
	      autoOpen: false,
	      height: 280,
	      width: 700,
	      modal: true,
	      buttons: {
	        "保存": function() {
	        	var flag = checkPwd("update", "password");
	        	if(!flag){
	        		return;
	        	}
	        	flag = checkPwd("update", "pwd");
	        	if(!flag){
	        		return;
	        	}
	        	flag = checkPwd2("update", "pwd", "pwd2");
	        	if(!flag){
	        		return;
	        	}
	        	var id = $("#pwdId").attr("value");
	        	var pwd = $("#update_pwd").attr("value");
	        	var password = $("#update_password").attr("value");
	        	$( this ).dialog( "close" );
	        	$.ajax({
				type:'post',
				url:"/r2k/user/updatePwd.do",
				data:{"user.password":pwd,"user.id":id,"password":password},
				success:function(data){
					if(data == '1'){
						$("#pwdtipmsg").html("").html(" 修改密码成功。");
						$( "#dialog-changepsw-message" ).dialog( "open" );
					}else if(data == '0'){
						$("#pwdtipmsg").html("").html(" 修改密码失败。");
						$( "#dialog-changepsw-message" ).dialog( "open" );
					}else if(data == '-1'){
						$("#pwdtipmsg").html("").html(" 修改密码错误。");
						$( "#dialog-changepsw-message" ).dialog( "open" );
					}
				}
			});
	        	updatePwdClean();
	        },
	        "取消": function() {
	        	updatePwdClean();
	        	$( this ).dialog( "close" );
	        }
	      },
	      close: function() {
	    	updatePwdClean();
			$( this ).dialog( "close" );
	      }
	    });//end update user
 	});
 	//打开修改密码对话框
	function toUpdatePwd(id){
		$("#pwdId").attr("value",id);
		$("#dialog-updatepwd").dialog("open");
	}
	//代开重置密码对话框
	function toResetPwd(id){
		$("#pwdId").attr("value",id);
		$("#dialog-resetpsw-message").dialog("open");
		$("#resetpwdtipmsg").html("").html("确认进行密码重置。");
	}
	function openChangeOrgDialog(){
		$("#changeOrgDialog").dialog("open");
	}
	function updatePwdClean(){
		$("#update_password").attr("value","");
    	$("#update_pwd").attr("value","");
   		$("#update_pwd2").attr("value","");
    	$("#update_password_msg").html("");
   		$("#update_pwd_msg").html("");
   		$("#update_pwd2_msg").html("");
	}
	//复制完密码清空
	function resetPwdClean(){
		$("reset_pwd").attr("value","");
	}
	//验证密码是否正确
	function checkPwd(type, pwdid){
		var pwdval = $("#"+type+"_"+pwdid).attr("value");
		if(pwdval == ''){
			$("#"+type+"_"+pwdid+"_msg").html("").html("密码不能为空。");
			return false;
		}
		if(pwdval.length < 6){
			$("#"+type+"_"+pwdid+"_msg").html("").html("密码长度不能小于6。");
			return false;
		}
		return true;
	}
	//验证密码是否正确
	function checkPwd2(type, pwdid, pwd2id){
		var pwdval = $("#"+type+"_"+pwdid).attr("value");
		if(pwdval == ''){
			$("#"+type+"_"+pwdid+"_msg").html("").html("密码不能为空。");
			return false;
		}
		var pwd2val = $("#"+type+"_"+pwd2id).attr("value");
		if(pwd2val == ''){
			$("#"+type+"_"+pwd2id+"_msg").html("").html("确认密码不能为空。");
			return false;
		}
		if(pwdval != pwd2val){
			$("#"+type+"_"+pwd2id+"_msg").html("").html("两次密码不一致。");
			return false;
		}
		return true;
	}
	function copy(){
		var obj = document.getElementById("reset_pwd"); 
		obj.select();    
		js = obj.createTextRange();    
		js.execCommand("Copy");
	}
 </script>
<div class="header" >
	<div id="logo" style="float:left;">
		<img alt="" src="../images/logo.jpg" height=60px/>
	</div>
	<div id="info-div" style="right:0.5%;float:right;font-size:14px;">
		<div id="userinfo" >
			<span style="margin-right:10px;">当前用户：<s:property value="#session.SPRING_SECURITY_CONTEXT.Authentication.Principal.authUser.userName" /></span>
			<span><a href="#" onclick="toUpdatePwd('<s:property value="#session.SPRING_SECURITY_CONTEXT.Authentication.Principal.authUser.id" />')">[修改密码]</a></span>
			<span><a id="a-logout" href="${ctx}/admin/j_spring_security_logout">[退出]</a></span>
		</div>
		<s:if test="#session.SPRING_SECURITY_CONTEXT.Authentication.Principal.authUser.authOrg.orgId == 'apabi'">
		<div id="swaporg">
			<span>当前机构：<s:property value="#session.SPRING_SECURITY_CONTEXT.Authentication.Principal.authUser.currentOrg.orgId" /></span>
			<span><a id="a-change-org" onclick="openChangeOrgDialog();">[修改]</a></span>
		</div>
		</s:if>
	</div>
</div>
	<div style="display: none;">
		<div id="dialog-updatepwd" title="修改密码">
			<form method="post" class="dialog-form">
		  	<input type="hidden" id="pwdId" name="user.id" value="${id}"/>
			<div class="field">
				<label class="field-label">
					<em style="color:red;">*&nbsp;</em>旧密码:
				</label>
				<input class="ui-input field-input" name="password" id="update_password" type="password" onfocus="focusIn('update','password')" onblur="checkPwd('update','password')"/>
				<span class="ui-tips-error" id="update_password_msg"></span>
			</div>
			<div class="field">
				<label class="field-label">
					<em style="color:red;">*&nbsp;</em>新密码:
				</label>
				<input class="ui-input field-input" name="user.password" id="update_pwd" type="password" onfocus="focusIn('update','pwd')" onblur="checkPwd('update','pwd')"/>
				<span class="ui-tips-error" id="update_pwd_msg"></span>
			</div>
			<div class="field">
				<label class="field-label">
					<em style="color:red;">*&nbsp;</em>确认密码:
				</label>
				<input class="ui-input field-input" name="user.password2" id="update_pwd2" type="password" onfocus="focusIn('update','pwd2')" onblur="checkPwd2('update','pwd','pwd2')"/>
				<span class="ui-tips-error" id="update_pwd2_msg"></span>
			</div>
			</form>
		</div>
	</div>
	<div style="display: none;">
		<div id="dialog-resetpwd" title="重置密码">
			<form method="post" class="dialog-form">
			<div class="field">
				<label class="field-label">
					新密码:
				</label>
				<input class="ui-input field-input" name="user.password" id="reset_pwd" onfocus="focusIn('reset','pwd')" onblur="checkPwd('reset','pwd')" readOnly="true"/>
				<input type="button" value="复制" id="copy_button" onclick="copy()"/>
			</div>
			</form>
		</div>
	</div>
	<div style="display: none;">
	<div id="dialog-changepsw-message" title="提示信息">
			<span id="pwdtipmsg" style="margin:5% auto;"></span>
		</div>
	</div>
	<div style="display: none;">
	<div id="dialog-resetpsw-message" title="提示信息">
			<span id="resetpwdtipmsg" style="margin:5% auto;"></span>
		</div>
	</div>

<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib tagdir="/WEB-INF/tags/simpletable" prefix="simpletable"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
  <head>
    <title>用户列表</title>
    <%@ include file="/commons/meta.jsp" %>
    <style>
	.ui-autocomplete-loading {
		background: white url('../images/ui-anim_basic_16x16.gif') right center no-repeat;
	}
	#save_authOrgId-div{overflow:auto;	}
	#update_authOrgId-div{overflow:auto;}
	.ui-autocomplete{ border:1px solid #999; margin-top:20px; max-height:400px; overflow-y:auto;}
	.save_ddl{ position:relative; width:300px; height:28px; border:1px solid #555;margin-right:15px;margin-left:0;}
    .save_ddl input{ border:0; padding-left:5px; width:270px; height:26px;}
    .save_ddl a.ddlarrow{ position:absolute; right:1px; top:2px; display:block; width:24px; height:23px; background:url(../images/ddl_arrow.jpg) no-repeat; cursor:pointer;}
	.ui-dialog{ overflow:visible;}
	</style>
  <script>
  	var mobileRegex = /^(13|14|15|18)[0-9]{9}$/;
	var emailRegex = /^\w+((-\w+)|(\.\w+))*\@[A-Za-z0-9]+((\.|-)[A-Za-z0-9]+)*\.[A-Za-z0-9]+$/;
	var officePhoneRegex = /^(([0\+]\d{2,3}-)?(0\d{2,3})-)?(\d{7,8})(-(\d{3,}))?$/;
		$(function() {
  	    	//创建simpleTable
  			var sortColumns = "<s:property value='#parameters.sortColumns'/>";
  			if(sortColumns == null || sortColumns == ""){
  				sortColumns = " AUTH_USER.ID desc";
  			}
  			window.simpleTable = new SimpleTable('form-list','${page.thisPageNumber}','${page.pageSize}',sortColumns);
  			$("#save_authOrgId").focus(function(){
  				focusIn('save','authOrgId');
  			});
  			$("#save_authOrgId").blur(function(){
  				focusOut('save','authOrgId');
  			});
  			$("#update_authOrgId").focus(function(){
  				focusIn('update','authOrgId');
  			});
  			$("#update_authOrgId").blur(function(){
  				focusOut('update','authOrgId');
  			});
  			//新建机构模糊查询
  			$("#save_authOrgId").autocomplete({
			source: function( request, response ) {
				$.ajax({
					url: "/r2k/org/suggest.do",
					dataType: "jsonp",
					data: {
						featureClass: "P",
						style: "full",
						maxRows: 12,
						name_startsWith: request.term
					},
					success: function( data ) {
						response( $.map( data.orgList, function( item ) {
							return {
								label: item.orgName +  ", " + item.orgId,
								value: item.orgId,
								name:item.id
							}
						}));
					}
				});
			},
			minLength: 2,
			select: function( event, ui ) {
				//console.log("id = " + ui.item.name + ", orgId = " +  ui.item.value);
				if(ui.item){
					$("#saveAuthOrgId").attr("value",ui.item.value);
					$("#save_authOrgId").attr("value", ui.item.value);
				}
			},
			open: function() {
				$( this ).removeClass( "ui-corner-all" ).addClass( "ui-corner-top" );
				var sWid = $( ".search_ddl" ).width()-5;
				$(".ui-autocomplete").css({"width":sWid});
			},
			close: function() {
				$( this ).removeClass( "ui-corner-top" ).addClass( "ui-corner-all" );
			}
		});
  			//更新机构模糊查询
  			$("#update_authOrgId").autocomplete({
			source: function( request, response ) {
				$.ajax({
					url: "/r2k/org/suggest.do",
					dataType: "jsonp",
					data: {
						featureClass: "P",
						style: "full",
						maxRows: 12,
						name_startsWith: request.term
					},
					success: function( data ) {
						response( $.map( data.orgList, function( item ) {
							return {
								label: item.orgName +  ", " + item.orgId,
								value: item.orgId,
								name:item.id
							}
						}));
					}
				});
			},
			minLength: 2,
			select: function( event, ui ) {
				//console.log("id = " + ui.item.name + ", orgId = " +  ui.item.value);
				if(ui.item){
					$("#updateAuthOrgId").attr("value",ui.item.name);
					$("#update_authOrgId").attr("value", ui.item.value);
				}
			},
			open: function() {
				$( this ).removeClass( "ui-corner-all" ).addClass( "ui-corner-top" );
				var sWid = $( ".search_ddl" ).width()-5;
				$(".ui-autocomplete").css({"width":sWid});
			},
			close: function() {
				$( this ).removeClass( "ui-corner-top" ).addClass( "ui-corner-all" );
			}
		});
  			//新建用户
  			$( "#dialog-form-save" ).dialog({
  		      autoOpen: false,
  		      height: 750,
  		      width: 700,
  		      modal: true,
  		      buttons: {
  		        "保存": function() {
  		        	checkValidate('save');
  		        },
  		        "取消": function() {
  		        	cleanTest();
  		        	$( this ).dialog( "close" );
  		        }
  		      },
  		      close: function() {
				cleanTest();
  		    	$( this ).dialog( "close" );
  		      }
  		    });//end save user
  			//更新用户
  			$( "#dialog-form-update" ).dialog({
  		      autoOpen: false,
  		      height: 700,
  		      width: 700,
  		      modal: true,
  		      buttons: {
  		        "保存": function() {
  		        	checkUpdateValidate('update');
  		        },
  		        "取消": function() {
  		        	$( this ).dialog( "close" );
  		        }
  		      },
  		      close: function() {
  		    	$( this ).dialog( "close" );
  		      }
  		    });//end update user
  			
		    //删除弹层
		  	$( "#dialog-delete" ).dialog({
				autoOpen: false,
				modal: true,
				buttons: {
					"确定": function() {
						$("#form-list").attr("action","/r2k/user/deleteUsers.do");
	        			$("#form-list").submit();
					},
					"取消": function() {
						$( this).dialog( "close" );
					}
				},
		     	close: function() {
		     		$(this).dialog( "close" );
		        }
			});//end delete dialog
			//新建用户
			$( "#user-save" )
		      .button()
		      .click(function() {
		          $("#dialog-form-save").dialog("open");
		      });
			//查询
		  	$( "#user-search" )
		      .button()
		      .click(function() {
		    	  $("#form-search").submit();
	  		  });
  		  
  		  	//复选框全选
  	    	$("#checkAll").click(function(){
  	    		$('input[type="checkbox"][name="userIds"]').attr("checked",this.checked);
			});
			
			 //批量删除按钮
  	  		$( "#batDel" )
      			.button()
      			.click(function() {
    	  			if(checkSelect()){
	    	  			$( "#dialog-delete" ).dialog( "open" );
    	  			}
      			});
      		
		});//end init function
	
		//清空
		function cleanTest(){
			$(".field-input").val("");
			$(".ui-tips-error").html("");
			$("#save_genRadio").attr("checked",true);
			$("#save_enableRadio").attr("checked",true);
			$("#save_authOrgId option").eq("0").attr("selected",true);
		}
	  
	  //判断复选框是否空选
	  	function checkSelect(){
		  var boxFlag = false;
		  var boxs = $('input[type="checkbox"][name="userIds"]');
		  for(var i = 0, len = boxs.length; i < len; i++){
			  if(boxs[i].checked ){
				  boxFlag = true;
				  break;
			  }
		  }
		  if(!boxFlag){
			  $("#tipmsg").html("").html("勾选专题不能为空。");
			  $( "#dialog-message" ).dialog( "open" );
		  }
		  return boxFlag;
	  	}
	  //输入框获取焦点事件
		function focusIn(type, id){
			$("#"+type+"_"+id+"_msg").html("");
		}
		//输入框失去焦点事件
		function focusOut(type, id){
			var orgId;
			var orgIdmsg;
			var authOrgId;
			var flagId;
			if(type == 'save'){
				flagId = 'save_' + id;
				orgId = $("#save_authOrgId").attr("value");
				orgIdmsg = 'save_authOrgId_msg';
				authOrgId = $("#saveAuthOrgId").attr("value");
			}else if(type == 'update'){
				flagId = 'update_' + id;
				orgId = $("#update_authOrgId").attr("value");
				orgIdmsg = 'update_authOrgId_msg';
				authOrgId = $("#updateAuthOrgId").attr("value");
			}
			var idval = $("#"+flagId).attr("value");
			if($.trim(orgId)  == ''){
				$("#"+orgIdmsg).html("").html("机构不能为空。");
			}else{
				if(type == 'save'){
					if(id == 'password'){
						checkPwd(type,id);
					}else if(id == 'password2'){
						checkPwd2(type,"password", id);
					}
				}
				if(id == 'userName'){
					if(idval != ''){
						var userNameTemp = $("#updateUserNameTemp").attr("value");
						if(type = 'update' && idval == userNameTemp){
							return;
						}else{
							$.ajax({
								type:'post',
								url:"/r2k/user/checkUserName.do",
								data:{"user.userName":idval,"authOrgId":authOrgId},
								success:function(data){
									if(data == '1'){
										$("#"+flagId+"_msg").html("");
									}else if(data == '0'){
										$("#"+flagId+"_msg").html("").html("用户名已存在。");
									}else if(data == '-1'){
										$("#"+flagId+"_msg").html("").html("系统错误。");
									}
								}
							});
						}
					}else{
						$("#"+flagId+"_msg").html("").html("用户名不能为空。");
					}
				}else if(id == 'loginName'){
					if(idval != ''){
						var loginNameTemp = $("#updateLoginNameTemp").attr("value");
						if(type = 'update' && idval == loginNameTemp){
							return;
						}else{
							$.ajax({
								type:'post',
								url:"/r2k/user/checkLoginName.do",
								data:{"user.loginName":idval,"authOrgId":authOrgId},
								success:function(data){
									if(data == '1'){
										$("#"+flagId+"_msg").html("");
									}else if(data == '0'){
										$("#"+flagId+"_msg").html("").html("登录名已存在。");
									}else if(data == '-1'){
										$("#"+flagId+"_msg").html("").html("系统错误。");
									}
								}
							});
						}
					}else{
						$("#"+flagId+"_msg").html("").html("登录名不能为空。");
					}
				}else if(id == 'mobile'){
					checkMobile(type,id);
				}else if(id == 'officePhone'){
					checkOfficePhone(type,id);
				}else if(id == 'email'){
					checkEmail(type,id);
				}else if(id == 'userDesc'){
					checkDesc(type,id);
				}
			}//end is orgId
		}
		//表单提交验证
		function checkValidate(type){
			var flag;
			flag = checkPwd(type,"password");
			if(!flag){
				return;
			}
			flag = checkPwd2(type,"password","password2");
			if(!flag){
				return;
			}
			flag = checkMobile(type,"mobile");
			if(!flag){
				return;
			}
			flag = checkOfficePhone(type,"officePhone");
			if(!flag){
				return;
			}
			flag = checkEmail(type,"email");
			if(!flag){
				return;
			}
			flag = checkDesc(type,"userDesc");
			if(!flag){
				return;
			}
			var authOrgId;
			var usernameval ;
			var loginnameval ;
			var orgIdmsg;
			var usernamemsg;
			var loginnamemsg;

			orgIdmsg = 'save_authOrgId_msg';
			usernamemsg = 'save_userName_msg';
			loginnamemsg = 'save_loginName_msg';
			authOrgId = $("#saveAuthOrgId").attr("value");
			usernameval = $("#save_userName").attr("value");
			loginnameval = $("#save_loginName").attr("value");
			var orgIdval = $("#save_authOrgId").attr("value");
			
			if($.trim(authOrgId) == '' || $.trim(orgIdval) == ''){
				if($.trim(orgIdval) == ''){
					$("#"+orgIdmsg).html("").html("机构不能为空。");
					return;
				}
				if($.trim(authOrgId) == ''){
					$("#"+orgIdmsg).html("").html("输入机构不存在。");
					return;
				}
			}else{
				if(usernameval != ''){
					if(loginnameval != ''){
						//验证用户名是否存在
						$.ajax({
							type:'post',
							url:"/r2k/user/checkUserName.do",
							data:{"user.userName":usernameval,"authOrgId":authOrgId},
							success:function(data){
								if(data == '1'){
									$("#"+usernamemsg).html("");
									
									//验证登录名是否存在
									$.ajax({
										type:'post',
										url:"/r2k/user/checkLoginName.do",
										data:{"user.loginName":loginnameval,"authOrgId":authOrgId},
										success:function(data){
											if(data == '1'){
												$("#"+loginnamemsg).html("");
												$("#userInfo-save").attr("action","/r2k/user/saveUser.do").submit();
											}else if(data == '0'){
												$("#"+loginnamemsg).html("").html("登录名已存在。");
											}else if(data == '-1'){
												$("#"+loginnamemsg).html("").html("系统错误。");
											}
										}
									});
								}else if(data == '0'){
									$("#"+usernamemsg).html("").html("用户名已存在。");
								}else if(data == '-1'){
									$("#"+usernamemsg).html("").html("系统错误。");
								}
							}
						});
					}else{
						$("#"+loginnamemsg).html("").html("登录名不能为空。");
					}
				}else{
					$("#"+usernamemsg).html("").html("用户名不能为空。");
				}
			}//end is orgId 
		}
		
		// 更新表单提交验证
		function checkUpdateValidate(type){
			var flag;
			flag = checkMobile(type,"mobile");
			if(!flag){
				return;
			}
			flag = checkOfficePhone(type,"officePhone");
			if(!flag){
				return;
			}
			flag = checkEmail(type,"email");
			if(!flag){
				return;
			}
			flag = checkDesc(type,"userDesc");
			if(!flag){
				return;
			}
			var authOrgId;
			var usernameval ;
			var loginnameval ;
			var orgIdmsg;
			var usernamemsg;
			var loginnamemsg;
			orgIdmsg = 'update_authOrgId_msg';
			usernamemsg = 'update_userName_msg';
			loginnamemsg = 'update_loginName_msg';
			authOrgId = $("#updateAuthOrgId").attr("value");
			usernameval = $("#update_userName").attr("value");
			loginnameval = $("#update_loginName").attr("value");
			var orgIdval = $("#update_authOrgId").attr("value");
			
			if($.trim(orgIdval) == ''){
				$("#"+orgIdmsg).html("").html("机构不能为空。");
				return;
			}else{
				if(authOrgId == $("#updateAuthOrgIdTemp").attr("value") && $.trim(orgIdval) != $("#update_authOrgIdTemp").attr("value")){
					$("#"+orgIdmsg).html("").html("输入机构不存在。");
					return;
				}
				
				if(usernameval != ''){
					if(loginnameval != ''){
						var userNameTemp = $("#updateUserNameTemp").attr("value");
						var loginNameTemp = $("#updateLoginNameTemp").attr("value");
						if(usernameval == userNameTemp){
							if(loginnameval == loginNameTemp){
								$("#userInfo-update").attr("action","/r2k/user/updateUser.do").submit();
							}else{
								//验证登录名是否存在
								$.ajax({
									type:'post',
									url:"/r2k/user/checkLoginName.do",
									data:{"user.loginName":loginnameval,"authOrgId":authOrgId},
									success:function(data){
										if(data == '1'){
											$("#"+loginnamemsg).html("");
											$("#userInfo-update").attr("action","/r2k/user/updateUser.do").submit();
										}else if(data == '0'){
											$("#"+loginnamemsg).html("").html("登录名已存在。");
										}else if(data == '-1'){
											$("#"+loginnamemsg).html("").html("系统错误。");
										}
									}
								});
							}
						}else{
							//验证用户名是否存在
							$.ajax({
								type:'post',
								url:"/r2k/user/checkUserName.do",
								data:{"user.userName":usernameval,"authOrgId":authOrgId},
								success:function(data){
									if(data == '1'){
										$("#"+usernamemsg).html("");
										if(loginnameval != loginNameTemp){
										//验证登录名是否存在
										$.ajax({
											type:'post',
											url:"/r2k/user/checkLoginName.do",
											data:{"user.loginName":loginnameval,"authOrgId":authOrgId},
											success:function(data){
												if(data == '1'){
													$("#"+loginnamemsg).html("");
													$("#userInfo-update").attr("action","/r2k/user/updateUser.do").submit();
												}else if(data == '0'){
													$("#"+loginnamemsg).html("").html("登录名已存在。");
												}else if(data == '-1'){
													$("#"+loginnamemsg).html("").html("系统错误。");
												}
											}
										});
										}else{
											$("#"+loginnamemsg).html("");
											$("#userInfo-update").attr("action","/r2k/user/updateUser.do").submit();
										}
									}else if(data == '0'){
										$("#"+usernamemsg).html("").html("用户名已存在。");
									}else if(data == '-1'){
										$("#"+usernamemsg).html("").html("系统错误。");
									}
								}
							});
						}
					}else{
						$("#"+loginnamemsg).html("").html("登录名不能为空。");
					}
				}else{
					$("#"+usernamemsg).html("").html("用户名不能为空。");
				}
			}//end is orgId 
		}//end check update
		
		//验证手机号是否正确
		function checkMobile(type, id){
			var idval = $("#"+type+"_"+id).attr("value");
			if(idval != ''){
				if(!mobileRegex.test(idval)){
					$("#"+type+"_"+id+"_msg").html("").html("移动电话格式不正确。");
					return false;
				}
			}
			return true;
		}
		//验证办公电话是否正确
		function checkOfficePhone(type, id){
			var idval = $("#"+type+"_"+id).attr("value");
			if(idval != ''){
				if(!officePhoneRegex.test(idval)){
					$("#"+type+"_"+id+"_msg").html("").html("办公电话格式不正确。");
					return false;
				}
			}
			return true;
		}
		//验证邮件是否正确
		function checkEmail(type, id){
			var idval = $("#"+type+"_"+id).attr("value");
			if(idval != ''){
				if(!emailRegex.test(idval)){
					$("#"+type+"_"+id+"_msg").html("").html("电子邮件格式不正确。");
					return false;
				}
			}
			return true;
		}
		//验证用户描述是否过长
		function checkDesc(type,id){
			var idval = $("#"+type+"_"+id).attr("value");
			if(idval != ''){
				if(idval.length > 1990){
					$("#"+type+"_"+id+"_msg").html("").html("用户描述过长。");
					return false;
				}
			}
			return true;
		}
		//打开更新用户弹层
		function toUpdateUser(id){
			//更新用户
			$.ajax({
				type:'post',
				url:"/r2k/user/toUpdateUser.do",
				data:{"id":id},
				success:function(data){
					if(data != null){
						$("#updateUserNameTemp").attr("value",data.userName);
						$("#updateLoginNameTemp").attr("value",data.loginName);
						$("#update_userName").attr("value",data.userName);
						$("#update_loginName").attr("value",data.loginName);
						$("#updateAuthOrgId").attr("value",data.authOrgId);	
						$("#updateAuthOrgIdTemp").attr("value",data.authOrgId);	
						$("#update_authOrgId").attr("value",data.authOrgId);	
						$("#update_authOrgIdTemp").attr("value",data.authOrgId);	
						$("#updateUserId").attr("value",id);	
						
						$("#update_mobile").attr("value",data.mobile);
						$("#update_officePhone").attr("value",data.officePhone);
						$("#update_email").attr("value",data.email);
						$("#update_userDesc").attr("value",data.userDesc);
						
						if(data.isAdmin == -1){
							$("#update_genRadio").attr("checked", true);
						}else if(data.isAdmin == 0){
							$("#update_advRadio").attr("checked", true);
						}
						if(data.enabled == 0){
							$("#update_enableRadio").attr("checked", true);
						}else if(data.enabled == -1){
							$("#update_disableRadio").attr("checked", true);
						}
						$("#dialog-form-update").dialog("open");
					}else{
						$("#tipmsg").html("").html("获取用户信息失败。");
						$("#dialog-message").dialog("open");
					}
				}
			});
		}// end toUpdateUser
		//复选框全选
    	$("#checkAll").click(function(){
    		$('input[type="checkbox"][name="topicIds"]').attr("checked",this.checked);
		});
	</script>
  </head>
  
  <body id="body">
  	<%@ include file="/commons/header.jsp" %>
	<div class="wrapper">
	<div class="left">
		<%@ include file="/commons/menu.jsp" %>
	</div>
	  <div id="navLink">
			<a href="${ctx}/org/findOrgList.do" target="_self"><s:text name="r2k.menu.org"></s:text></a> &gt; 用户管理
	  </div>
	<div class="content">
		<div id="container" class="ui-widget">
		  <div id="toolbar" class="ui-widget-header ui-corner-all">
		  	<button id="user-save" >新建用户</button>	
		  	<button id="batDel" >批量删除</button>
		 	<div style="float: right;">
			  <form id="searchUser" action="/r2k/user/showAll.do" method="post">
			  	用户查询：<input class="ui-input" name="s_userOrLoginName" value="<s:property value='#parameters.s_userOrLoginName' />"/>
			  	<button id="user-search">查询</button>	
			  </form>
		 	</div>
		  	<s:if test="#session.user.org.orgId == 'apabi'">
		  	</s:if>
		  </div>
		 <form id="form-list" action="/r2k/user/showAll.do" method="post">
		 	<input type="hidden" name="s_q" value="<s:property value='#parameters.s_q' />" />
		  <table id="table-list" class="ui-widget ui-widget-content gridBody">
		    <thead>
		      <tr class="ui-widget-header ">
		      	<th width="5%"><input id="checkAll" type="checkbox"/>全选</th>
		        <th width="8%" class="sortCol" sortColumn="USER_NAME">用户名</th>
		        <th width="8%" class="sortCol" sortColumn="LOGIN_NAME">登录名</th>
		        <th width="8%" class="sortCol" sortColumn="ORG_ID">所在机构</th>
		        <th width="5%">用户类型</th>
		        <th width="8%" >移动电话</th>
		        <th width="8%" >办公电话</th>
		        <th width="15%" >电子邮件</th>
		        <th width="5%" class="sortCol" sortColumn="ENABLED">用户状态</th>
		        <th width="15%" >用户描述</th>
	            <th width="15%">操作</th>
		      </tr>
		    </thead>
		    <tbody>
		      <s:iterator value="page.result" var="user">
				<tr>
					<td>
					<s:if test="isAdmin == -1">
					<input name="userIds" type="checkbox" value="${id}" />
					</s:if>
					</td>
					<td><s:property value="userName"/></td>
					<td><s:property value="loginName"/></td>
					<td><s:property value="authOrgId"/></td>
					<td>
						<s:if test="isAdmin == 0">超级管理员</s:if>
						<s:if test="isAdmin == -1">普通用户</s:if> 
					</td>
					<td><s:property value="mobile"/></td>
					<td><s:property value="officePhone"/></td>
					<td><s:property value="email"/></td>
					<td>
						<s:if test="enabled == 0">正常</s:if>
						<s:if test="enabled == -1">禁用</s:if> 
					</td>
					<td><s:property value="userDesc"/></td>
					<td>
						<a href="#" onclick="toUpdateUser('${id}')">[修改信息]</a>
						<a href="#" onclick="toUpdatePwd('${id}')">[修改密码]</a>
						<s:if test="isAdmin == -1">
						<a href="#" onclick="toResetPwd('${id}')">[重置密码]</a>
						</s:if>
					</td>
				</tr>
			  </s:iterator>
		    </tbody>
		  </table>
		  <simpletable:pageToolbar page="${page}"></simpletable:pageToolbar>
		  </form>
		</div>
		<div style="display: none;">
			<div id="dialog-form-save" title="新建用户">
			  <form id="userInfo-save" method="post" class="dialog-form">
			  	<input type="hidden" id="saveAuthOrgId" name="user.authOrgId" />
  				<div class="regbox">
  					<div class="field">
						<label class="field-label">
							<em style="color:red;">*&nbsp;</em>机构:
						</label>
						<div id="save_authOrgId-div" class="ui-front" >
							<div class="save_ddl">
							    <input id="save_authOrgId" class="field-input"/>
							</div>
						</div>
						<span class="ui-tips-error" id="save_authOrgId_msg"></span>
					</div>
					<div class="field">
						<label class="field-label">
							<em style="color:red;">*&nbsp;</em>用户名:
						</label>
						<input class="ui-input field-input" name="user.userName" id="save_userName" onfocus="focusIn('save','userName')" onblur="focusOut('save','userName')"/>
						<span class="ui-tips-error" id="save_userName_msg"></span>
					</div>
					<div class="field">
						<label class="field-label">
							<em style="color:red;">*&nbsp;</em>登录名:
						</label>
						<input class="ui-input field-input" name="user.loginName" id="save_loginName" onfocus="focusIn('save','loginName')" onblur="focusOut('save','loginName')"/>
						<span class="ui-tips-error" id="save_loginName_msg"></span>
					</div>
					<div class="field">
						<label class="field-label">
							<em style="color:red;">*&nbsp;</em>密码:
						</label>
						<input class="ui-input field-input" name="user.password" id="save_password" type="password" onfocus="focusIn('save','password')" onblur="focusOut('save','password')"/>
						<span class="ui-tips-error" id="save_password_msg"></span>
					</div>
					<div class="field">
						<label class="field-label">
							<em style="color:red;">*&nbsp;</em>确认密码:
						</label>
						<input class="ui-input field-input" name="user.password2" id="save_password2" type="password" onfocus="focusIn('save','password2')" onblur="focusOut('save','password2')"/>
						<span class="ui-tips-error" id="save_password2_msg"></span>
					</div>
					<div class="field">
						<label class="field-label">移动电话:</label>
						<input class="ui-input field-input" name="user.mobile" id="save_mobile"  onfocus="focusIn('save','mobile')" onblur="focusOut('save','mobile')"/>
						<span class="ui-tips-error" id="save_mobile_msg"></span>
					</div>
					<div class="field">
						<label class="field-label">办公电话:</label>
						<input class="ui-input field-input" name="user.officePhone" id="save_officePhone"  onfocus="focusIn('save','officePhone')" onblur="focusOut('save','officePhone')"/>
						<span class="ui-tips-error" id="save_officePhone_msg"></span>
					</div>
					<div class="field">
						<label class="field-label">电子邮件:</label>
						<input class="ui-input field-input" name="user.email" id="save_email"  onfocus="focusIn('save','email')" onblur="focusOut('save','email')"/>
						<span class="ui-tips-error" id="save_email_msg"></span>
					</div>
					<div class="field">
						<label class="field-label">用户状态:</label>
						<input type="radio" id="save_enableRadio" name="user.enabled" checked="checked" value="0"/> 正常
				  		<input type="radio" id="save_disableRadio" name="user.enabled" value="-1"/> 禁用
						<span class="ui-tips-error" id="error-title"></span>
					</div>
					<div class="field">
						<label class="field-label">用户描述:</label>
						<textarea class="ui-input field-input" name="user.userDesc" id="save_userDesc" rows="10" onfocus="focusIn('save','userDesc')" onblur="focusOut('save','userDesc')"></textarea>
						<span class="ui-tips-error" id="save_userDesc_msg"></span>
					</div>
				</div>
			  </form>
			</div>
			
			<div id="dialog-form-update" title="更新用户">
			  	<input type="hidden" id="updateUserNameTemp" />
			  	<input type="hidden" id="updateLoginNameTemp" />
			  	<input type="hidden" id="updateAuthOrgIdTemp" />
			  	<input type="hidden" id="update_authOrgIdTemp" />
				<form id="userInfo-update" method="post" class="dialog-form">
			  	<input type="hidden" id="updateUserId" name="user.id" />
			  	<input type="hidden" id="updateAuthOrgId" name="user.authOrgId" />
  				<div class="regbox">
  					<div class="field">
						<label class="field-label">
							<em style="color:red;">*&nbsp;</em>机构:
						</label>
						<div id="update_authOrgId-div" class="ui-front" >
							<div class="save_ddl">
							    <input id="update_authOrgId" class="field-input" readonly="readonly"/>
							</div>
						</div>
						<span class="ui-tips-error" id="update_authOrgId_msg"></span>
					</div>
					<div class="field">
						<label class="field-label">
							<em style="color:red;">*&nbsp;</em>用户名:
						</label>
						<input class="ui-input field-input" name="user.userName" id="update_userName" onfocus="focusIn('update','userName')" onblur="focusOut('update','userName')"/>
						<span class="ui-tips-error" id="update_userName_msg"></span>
					</div>
					<div class="field">
						<label class="field-label">
							<em style="color:red;">*&nbsp;</em>登录名:
						</label>
						<input class="ui-input field-input" name="user.loginName" id="update_loginName" onfocus="focusIn('update','loginName')" onblur="focusOut('update','loginName')"/>
						<span class="ui-tips-error" id="update_loginName_msg"></span>
					</div>
					<div class="field">
						<label class="field-label">移动电话:</label>
						<input class="ui-input field-input" name="user.mobile" id="update_mobile" onfocus="focusIn('update','mobile')" onblur="focusOut('update','mobile')"/>
						<span class="ui-tips-error" id="update_mobile_msg"></span>
					</div>
					<div class="field">
						<label class="field-label">办公电话:</label>
						<input class="ui-input field-input" name="user.officePhone" id="update_officePhone" onfocus="focusIn('update','officePhone')" onblur="focusOut('update','officePhone')"/>
						<span class="ui-tips-error" id="update_officePhone_msg"></span>
					</div>
					<div class="field">
						<label class="field-label">电子邮件:</label>
						<input class="ui-input field-input" name="user.email" id="update_email" onfocus="focusIn('update','email')" onblur="focusOut('update','email')"/>
						<span class="ui-tips-error" id="update_email_msg"></span>
					</div>
					<div class="field">
						<label class="field-label">用户状态:</label>
						<input type="radio" id="update_enableRadio" name="user.enabled" value="0"/> 正常
				  		<input type="radio" id="update_disableRadio" name="user.enabled" value="-1"/> 禁用
						<span class="ui-tips-error" id="error-title"></span>
					</div>
					<div class="field">
						<label class="field-label">用户描述:</label>
						<textarea class="ui-input field-input" name="user.userDesc" id="update_userDesc" rows="10" onfocus="focusIn('update','userDesc')" onblur="focusOut('update','userDesc')"></textarea>
						<span class="ui-tips-error" id="update_userDesc_msg"></span>
					</div>
				</div>
			  </form>
			</div> 
			
			<div id="dialog-delete" title="提示信息">
				<input type="hidden" id="deleteId">
				<span style="margin:5% auto;">确定要删除选中用户信息吗？</span>
			</div>
		</div>
	</div>
	</div>
	<div class="footer"><%@ include file="/commons/footer.jsp" %></div>
  </body>
</html>
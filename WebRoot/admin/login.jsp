<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="org.apache.commons.lang.StringUtils"%>
<%@page import="org.springframework.security.web.authentication.AbstractProcessingFilter"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="s" uri="/struts-tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" scope="page"/>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>阅知登录</title>
<script src="${ctx}/js/jquery-1.10.2.min.js"></script>
<script src="${ctx}/js/jquery-ui-1.10.4.custom.js"></script>
<script src="${ctx}/js/jquery.ui.core.min.js"></script>
<script src="${ctx}/js/jquery.ui.widget.min.js"></script>
<link rel="stylesheet" href="${ctx}/css/base/jquery.ui.all.css">
<link href="${ctx}/css/register.css" rel="stylesheet">
<script type="text/javascript">
var err = null;
<%
	Exception e = (Exception)session.getAttribute(AbstractProcessingFilter.SPRING_SECURITY_LAST_EXCEPTION_KEY);
	if(e != null){
	String err=e.getMessage();
	if("Bad credentials".equals(err)){
		err="用户名或密码错误!";
		System.out.println(err);
	}
	session.removeAttribute(AbstractProcessingFilter.SPRING_SECURITY_LAST_EXCEPTION_KEY);
%>
	<%if(err != null && err.trim().length() > 0){%>
	err= "<%=err%>";
	<%}%>
<%
	} else {
		String errorStr = (String)session.getAttribute("errorMsg");
		if (!StringUtils.isBlank(errorStr)) {			 
			session.removeAttribute("errorMsg");
			System.out.println(errorStr);
		}%>
		<%if(errorStr != null && errorStr.trim().length() > 0 ){%>
		err = "<%=errorStr%>";
		<%}%>
		<%
	}
%>
$(function(){
	//信息提示框
	$( "#dialog-message" ).dialog({
		autoOpen: false,
		modal: true,
		buttons: {
			Ok: function() {
				$(this).dialog("close");
			}
		}
	});
	if(err != null && err != ""){
		//console.log(err !== null);
		$("#tipmsg").html("").html(err);
		$( "#dialog-message" ).dialog("open");
	}
	$(".login-btn").click(function(){
		verifyName();
		verifyPsw();
		if(chkName && chkPsw){
			$("#signup_form").submit();
		}
	});
	$(".login-btn").keydown(function(e){
		verifyName();
		verifyPsw();
		if(e.keyCode==13){
			if(chkName && chkPsw){
				$("#signup_form").submit();
			}
		}
	});
	$("#signup_name").focus();
});

document.onkeydown = function(e){
	if(e.keyCode==13){
		verifyName();
		verifyPsw();
		if(chkName && chkPsw){
			$("#signup_form").submit();
		}
	}
};

function verify() {
	
	var loginName = $("#signup_name").val();	
	if ($.trim(loginName).length == 0) {
      	$("#msg").text("登录名不能为空！");
      	return false;
   	} else {
   	   	$("#msg").text("");
   	}
	
	var password = $("#signup_password").val();
   	if ($.trim(password).length == 0) {
   		$("#msg").text("密码不能为空！");
      	return false;
   	} else {
   	   	$("#msg").text("");
   	}
   	return true;
}
var chkName = false;
var chkPsw = false;

function verifyName(){
	var loginName = $("#signup_name").val();
	$("#tip-name").attr("class","");
	if ($.trim(loginName).length == 0) {
		$("#tip-name").addClass("tip");
		$("#tip-name").addClass("error");
		$("#signup_name").attr("placeholder","用户名不能为空");
		chkName = false;
   	} else {
   		$("#tip-name").addClass("tip");
		$("#tip-name").addClass("ok");
		chkName = true;
   	}
}

function verifyPsw(){
	var psw = $("#signup_password").val();	
	$("#tip-psw").attr("class","");
	if ($.trim(psw).length == 0) {
		$("#tip-psw").addClass("tip");
		$("#tip-psw").addClass("error");
		$("#signup_password").attr("placeholder","密码不能为空");
		chkPsw = false;
   	} else {
   		$("#tip-psw").addClass("tip");
		$("#tip-psw").addClass("ok");
		chkPsw = true;
   	}
}

</script>
</head>
<body>
<div class='signup_container'>

    <h1 class='signup_title'>用户登陆</h1>
    <img src='${ctx}/images/people.png' id='admin'/>
    <div id="signup_forms" class="signup_forms clearfix">
            <form class="signup_form_form" id="signup_form" method="post" action="${ctx}/admin/j_spring_security_check">
                    <div class="form_row first_row">
                        <label id="lsn" for="signup_name">请输入用户名</label><div id="tip-name"></div>
                        <input type="text" name="j_username" placeholder="请输入用户名" id="signup_name" data-required="required" onblur="verifyName();" autocomplete="off" >
                    </div>
                    <div class="form_row">
                        <label id="lsp" for="signup_password">请输入密码</label><div id="tip-psw" ></div>
                        <input type="password" name="j_password" placeholder="请输入密码" id="signup_password" data-required="required" onblur="verifyPsw();">
                    </div>
           </form>
    </div>
    <div class="login-btn-set"><a class='login-btn'></a></div>
</div>
<div id="dialog-message" title="提示信息">
	<span id="tipmsg" style="margin:5% auto;"></span>
</div>
</body>
</html>
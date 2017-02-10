<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" scope="page"/>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<link href="${ctx}/css/loading/loading.css" rel="stylesheet">
<link rel="stylesheet" href="${ctx}/css/base/jquery.ui.all.css">
<link href="${ctx}/css/style.css" rel="stylesheet">
<link href="${ctx}/css/alert-form.css" rel="stylesheet">
<link href="${ctx}/css/base/list.css" rel="stylesheet">
<link href="${ctx}/css/ui.dynatree.css" rel="stylesheet">
<link href="${ctx}/css/menu-css.css" rel="stylesheet">
<script src="${ctx}/js/jquery-1.10.2.min.js"></script>
<script src="${ctx}/js/jquery-ui-1.10.4.custom.js"></script>
<script src="${ctx}/js/jquery.ui.core.min.js"></script>
<script src="${ctx}/js/jquery.ui.widget.min.js"></script>
<script src="${ctx}/js/jquery.ui.button.min.js"></script>
<script src="${ctx}/js/form/jquery-migrate-1.2.1.js"></script>
<script src="${ctx}/js/loading/loading-min.js"></script>
<script src="${ctx}/js/loading/jquery.bgiframe.js"></script>
<script src="${ctx}/js/simpletable.js"></script>
<script src="${ctx}/js/jquery.ui.spinner.min.js"></script>
<script src="${ctx}/js/jquery.mousewheel.min.js"></script>
<script src="${ctx}/js/jquery.ui.datepicker-zh-TW.js"></script>
<script src="${ctx}/js/jquery.dynatree.js"></script>
<script src="${ctx}/js/jquery.cookie.js"></script>
<script src="${ctx}/js/form/jquery.form.js"></script>
<script type="text/javascript" src="${ctx}/js/Treetable_files/jquery.treetable.js"></script>
<style>
*{margin:0; padding:0;}
body{overflow-x:hidden;}
.header{ margin:0 auto; width:100%; border:.1em solid #e2e2e2;margin-bottom:2px;margin-left:1px;margin-right:1px;overflow-x:hidden;}
.footer{ margin:0 auto; width:100%; height:100px;border:.1em solid #e2e2e2;margin-left:1px;margin-right:1px;overflow-x:hidden;text-align:center;line-height:100px;}
.wrapper{clear:both;overflow-y:auto; overflow-x:hidden; width:100%;margin-bottom:2px;display:block;}
.left{ float:left; width:13.3%; max-height:99%;overflow:hidden;border:.1em solid #e2e2e2;margin-left:1px;}
#navLink{float:right;width:86%; border:.1em solid #e2e2e2;margin-right:1px;display:block; margin-bottom:2px;font-size:13px;}
.content{ float:right; width:86%; border:.1em solid #e2e2e2;margin-right:1px;display:block;}
#navLink a{color:#000000;font-size:13px;}
#navLink a:hover{color:red;font-size:13px;}
</style>
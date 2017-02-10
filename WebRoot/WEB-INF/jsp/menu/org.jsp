<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib tagdir="/WEB-INF/tags/simpletable" prefix="simpletable"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" scope="page" />
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
	<head>
		<title>菜单管理</title>
		<%@ include file="/commons/meta.jsp" %>
		<meta http-equiv="pragma" content="no-cache">
		<meta http-equiv="cache-control" content="no-cache">
		<meta http-equiv="expires" content="0">
		
		<style type="text/css">
			#table-expandable {
				margin: 0;
				border-collapse: collapse;
				width: 100%;
			}
			
			#table-expandable td,#table-expandable th {
				border: 1px solid #eee;
				padding: .6em 10px;
				text-align: left;
			}
			
			#table-expandable th {
				font-weight: bold;
			}
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
		
	<script type="text/javascript">
		$(function(){
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
			        var sort  = $(this).attr("value");
			        if(parseInt(sort)!= NaN && 0 < parseInt(sort)&& parseInt(sort) < 1000){
			        	$.get("${ctx}/menu/sort.do", { id: id, sort: sort , devType:'${devType}'},
			        		function(data){
			        		if(data == 1){
			        			$("#status-" + id).text("未发布");
			        		}
			        	});
			        }else{
			        	alert("数字必须在1~999之间");
			        	window.location.reload();
			        }
			        
			    }
			});
			//增加菜单
		    $("#menu-save").button().click(function(){
		    	window.location.href='/r2k/menu/savePage.do?devType=${devType}';
		    });
			
		  //发布
	        $( "#publish" ).button().click(function() {
	        	$.ajax({
	       			type:'post',
	       			url: "${ctx}/menu/publish.do?devType=${devType}",
	       			beforeSend: function () {
	       				ShowDiv();
	                },
	                complete: function () {
	                	HiddenDiv();
	                },
	       			success:function(data){
	       				if(data == 1){
	       					$("#dialog-publish").html("").html("按设备类型发布菜单成功！");
	    	            	$("#dialog-publish").dialog("open");
	       				}else if(data == 0){
	       					$("#dialog-publish").html("").html("按设备类型发布菜单失败！");
	    	            	$("#dialog-publish").dialog("open");
	       				}else if(data == 2){
	       					$("#dialog-publish").html("").html("按设备类型发布菜单失败:没有设置主页！");
	    	            	$("#dialog-publish").dialog("open");
	       				}
	       			}
	       		  });
	        });
	      //发布
		  	$( "#dialog-publish" ).dialog({
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
		        }
			});
			
			//创建simpleTable
			var sortColumns = "<s:property value='#parameters.sortColumns'/>";
			if(sortColumns == null || sortColumns == ""){
				sortColumns = " SORT asc";
			}
			window.simpleTable = new SimpleTable('form-list',${page.thisPageNumber},${page.pageSize},sortColumns);
			
			//设置删除对话框
		    $( "#dialogdelete" ).dialog({
		      autoOpen: false,
		      resizable: false,
		      height:140,
		      modal: true,
		      buttons: {
		        "确定": function() {
		        	var id = $("#deleteid").attr("value");
		        	$.get("${ctx}/menu/delete.do", {id: id,devType:'${devType}'},
		        		function(data){
		        		location.reload();
		        	});
		        },
		        "取消": function() {
		          $( this ).dialog( "close" );
		        }
		      }
		    });
		});
		  
	      //显示等待弹层
			 function ShowDiv() {
	            $("#loading").show();
	        }
			//关闭等待弹层
	        function HiddenDiv() {
	            $("#loading").hide();
	        }
		
		 //打开删除对话框
		function deleteMenu(id){
	           $("#deleteid").attr("value", id);
			$("#dialogdelete").dialog( "open" );	 
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
	    	<a href="/r2k/menu/${actionName}.do" target="_self">菜单管理(${devTypeName})</a>
			<!-- <a href="/r2k/menu/org.do" target="_self">触摸屏菜单管理</a> -->
	    </div>			
		<div class="content">
				<div id="container" class="ui-widget">
					<div id="toolbar" class="ui-widget-header ui-corner-all">
					<button id="menu-save">添加自定义菜单</button>
					<button id="publish">发布</button>
					</div>

					<form id="form-list" action="/r2k/menu/org.do" method="post">
						<table id="table-expandable"
							class="ui-widget ui-widget-content gridBody">
							<thead>
								<tr class="ui-widget-header ">
									<th style="width: 10%;">
										菜单标题
									</th>
									<th style="width: 10%;">
										类型
									</th>
									<th style="width: 6%;">
										是否显示
									</th>
									<th style="width: 6%;">
										是否主页
									</th>
									<th style="width: 8%;">
										状态
									</th>
									<th style="width: 28%;">
										链接地址
									</th>
									<th style="width: 8%;">
										显示顺序
									</th>
									<th style="width: 24%;">
										操作
									</th>
								</tr>
							</thead>
							<tbody>
								<s:iterator value="page.result">
									<tr>
										<td>${title}</td>
										<td>
											<s:if test="menuType == 'link'">自定义菜单</s:if>
											<s:else>系统菜单</s:else>
										</td>
										<td>
											<s:if test="hide == 1">显示</s:if>
											<s:else>隐藏</s:else>
										</td>
										<td>
											<s:if test="homePage == 1"><span style="color:green;">是</span></s:if>
											<s:else><span style="color:red;">否</span></s:else>
										</td>
										<td>
											<s:if test="status==0">未发布</s:if>
											<s:elseif test="status==1">发布</s:elseif>
										</td>
										<td>${link}</td>
										<td class="tdSpinner">
											<input name="${id}" class="spinner" style="width: 30px" value="${sort}" min="1" max="999">
										</td>
										<td>
											<s:if test="menuType == 'navigation'">
												<a href="/r2k/menu/updateNavPage.do?id=${id}&devType=${devType}">[修改]</a>
											</s:if>
											<s:else>
												<a href="/r2k/menu/updatePage.do?id=${id}&devType=${devType}">[修改]</a>
											</s:else>
											<s:if test="menuType == 'link'">
												<a href="#" onclick="deleteMenu('${id}');">[删除]</a>
											</s:if>
										</td>
									</tr>
								</s:iterator>
							</tbody>
						</table>
						<simpletable:pageToolbar page="${page}"></simpletable:pageToolbar>
					</form>
				</div>
			</div>
		</div>
		<div id="dialogdelete" title="删除">
			<input id="deleteid" name="delete" type="hidden" />
			<p>
				确认删除？
			</p>
		</div>
		<div id="dialog-publish" title="提示信息"></div>
		<div id="loading"><img src="${ctx}/images/ui-anim_basic_16x16.gif" alt="" /> <span style="font-weight: bolder;">正在加载数据,请稍候...</span></div>
		<div class="footer"><%@ include file="/commons/footer.jsp" %></div>
	</body>
</html>

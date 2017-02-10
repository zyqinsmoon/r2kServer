<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib tagdir="/WEB-INF/tags/simpletable" prefix="simpletable"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" scope="page"/>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
  <head>
  	<title>栏目引用</title>
  	<%@ include file="/commons/meta.jsp" %>
	  <script type="text/javascript" src="${ctx}/js/Treetable_files/jquery.treetable.js"></script>
			<!--这个是现实表格的文件-->
			<link rel="stylesheet" type="text/css"
				href="${ctx}/css/Treetable_files/jquery.treetable.css" />
			<link rel="stylesheet"
				href="${ctx}/css/Treetable_files/jquery.treetable.theme.default.css" />
			<link rel="stylesheet" href="${ctx}/css/Treetable_files/screen.css"
				media="screen" />
     <style type="text/css">
		div#result-contain{
			width: 100%;
    		margin: 0;
		}
		#table-expandable {
    		margin: 0;
    		border-collapse: collapse;
    		width: 100%;
		}

		#table-expandable td, #table-expandable th {
    		border: 1px solid #eee;
    		padding: .6em 10px;
    		text-align: left;
		}
		#table-expandable th{
			font-weight: bold;
		}
		
	</style>
	<script type="text/javascript">
		$(function(){
			//创建simpleTable
  			var sortColumns = "<s:property value='#parameters.sortColumns'/>";
  			if(sortColumns == null || sortColumns == ""){
  				sortColumns = " SORT asc";
  			}
  			window.simpleTable = new SimpleTable('form-list',${page.thisPageNumber},${page.pageSize},sortColumns);
  			
			//treetable插件
			$("#table-expandable").treetable({
			 	expandable: true 
			 });
			
			$("#save").button().click(function(){
				<s:if test="deviceId != null">
				var url = "/r2k/quote/addDevQuoted.do";
				</s:if>
				<s:else>
				var url = "/r2k/quote/addOrgQuoted.do";
				</s:else>
				$("form:first").attr("action",url);
				$("form:first").submit();
			});
		});
		
		function selectAll(){
			 var checklist = document.getElementsByName ("checked");
			   if(document.getElementById("controlAll").checked)
			   {
			   for(var i=0;i<checklist.length;i++)
			   {
			      checklist[i].checked = 1;
			   } 
			 }else{
			  for(var j=0;j<checklist.length;j++)
			  {
			     checklist[j].checked = 0;
			  }
			 }
			};
		
		function getChilds(pid){
        	var expand = $("#getchilds-"+pid).attr("title");
        	if(expand=="Collapse"){
        		$("tr[data-tt-parent-id='"+pid+"']").each(function(){
        			collapse(this);
        		});
        		$("tr[data-tt-parent-id='"+pid+"']").remove();
        		$("#node-"+pid).removeClass("expanded");
        		$("#node-"+pid).addClass("collapsed");
        		$("#getchilds-"+pid).removeAttr("title");
        		$("#getchilds-"+pid).attr("title","Expand");
        	}else{
        	$.ajax({
        		type:"post",
        		url:"/r2k/pub/getChilds.do",
        		data:{"parentId":pid},
        		dataType:"json",
        		success:function(data){
        			if(data != null){
        				$("tr[data-tt-parent-id='"+pid+"']").remove();
        				var size = data.length;
        				var level = parseInt($("#node-"+pid).attr("level")) + 1;
        				var rgb = parseInt('ff',16) - (level-1)*15;
        				var color = rgb.toString(16) + rgb.toString(16) + rgb.toString(16);
        				for(var i = 0; i < size; i++){
        					var col = data[i];
        					var tr = document.createElement("tr");
        					$("#node-"+pid).after(tr);
        					tr.setAttribute("id","node-"+col.id);
        					tr.setAttribute("data-tt-id",col.id);
        					tr.setAttribute("data-tt-parent-id", pid);
        					tr.setAttribute("level",level);
        					tr.setAttribute("style","background-color:#"+color+";");
        					var childs = col.childs;
        					var cls = "collapsed";
        					var span = document.createElement("span");
        					span.setAttribute("class","indenter");
        					var styles = $("#node-"+pid+" span[class='indenter']").attr("style").split(";");
        					var styleSize = styles.length;
        					var pxLeft = 0;
        					for(var inx = 0; inx < styleSize; inx++){
        						var style = styles[inx];
        						var locate = style.indexOf("padding-left:");
        						if(locate != -1){
        							pxLeft = parseInt(style.replace("padding-left:","").replace("px","")) + 19;
        							break;
        						}
        					}
        					span.setAttribute("style","padding-left:"+pxLeft+"px;");
        					var tdTitle = document.createElement("td");
        					var title = document.createTextNode(col.title==null?"":col.title);
        					tdTitle.appendChild(span);
        					tdTitle.appendChild(title);
        					tr.appendChild(tdTitle);
        					colType = col.type;
        					if(childs == 0 || colType == 1 || colType == 2 || colType == 3){
        						cls = cls + " leaf";
        					}else{
        						cls = cls + " branch isparent";
        						var aExpand = document.createElement("a");
        						aExpand.setAttribute("id","getchilds-"+col.id);
        						aExpand.setAttribute("href","#");
        						aExpand.setAttribute("title","Expand");
        						aExpand.innerHTML="&nbsp;";
        						span.appendChild(aExpand);
        						var id = col.id;
        						$("#getchilds-"+id).click(function(){
        							getChilds($(this).attr("id").replace("getchilds-",""));
        						});
        						tr.removeAttribute("class");
        						tr.setAttribute("class","branch isparent collapsed");
        					}
        					tr.setAttribute("class",cls);
        					var tdType = document.createElement("td");
        					var type;
        					var updateUrl;
        					if(col.type == 1){
        						type = "文章";
        					}else if(col.type == 2){
        						type = "图片";
        					}else if(col.type == 3){
        						type = "视频";
        					}else if(col.type == 4){
        						type = "图集";
        					}else if(col.type == 11){
        						type = "文章栏目";
        					}else if(col.type == 14){
        						type = "图集列表";
        					}else if(col.type == 13){
        						type = "视频集";
        					}
        					tdType.innerText = type;
        					tr.appendChild(tdType);
        					var tdParentTitle = document.createElement("td");
        					tdParentTitle.innerText = col.parentTitle==null?"":col.parentTitle;
        					tr.appendChild(tdParentTitle);
        					
        					var tdDate = document.createElement("td");
        					tdDate.innerText = col.crtDate.replace("T"," ");
        					tr.appendChild(tdDate);
        					var checkbox = document.createElement("td");
        					tr.appendChild(checkbox);
        				}
        				$("#node-"+pid).removeClass("collapsed");
        				$("#node-"+pid).addClass("expanded");
        			}
        		}
        	});
        		$("#getchilds-"+pid).removeAttr("title");
        		$("#getchilds-"+pid).attr("title","Collapse");
        	}
        }
		
		function collapse(node){
        	var clz = node.getAttribute("class");
        	if(clz.indexOf("isparent") != -1){
        		var id = node.getAttribute("id").replace("node-","");
        		$("tr[data-tt-parent-id='"+id+"']").each(function(){
        			collapse(this);
        		});
        		$("tr[data-tt-parent-id='"+id+"']").remove();
        	}
        }
		
		function bindGetChilds(aChilds,pid){
        	aChilds.onclick = getChilds(pid);
        }

        function initBindGetChilds(){
        	$("#table-expandable tbody tr").each(function(){
        		var childs = this.getAttribute("data-tt-childs");
        		if(childs > 0){
        			var id = this.getAttribute("data-tt-id");
        			var td = this.getElementsByTagName("td")[0];
        			var span = td.getElementsByTagName("span")[0];
        			var aExpand = document.createElement("a");
        			aExpand.setAttribute("id","getchilds-"+id);
        			aExpand.setAttribute("href","#");
        			aExpand.setAttribute("title","Expand");
        			aExpand.innerHTML="&nbsp;";
        			span.appendChild(aExpand);
        			$("#getchilds-"+id).click(function(){
        				getChilds(id);
        			});
        			this.removeAttribute("class");
        			this.setAttribute("class","branch isparent collapsed");
        		}
        	});
        }
        function load(){
        	initBindGetChilds();
        }
        window.onload = load;
	</script>
  </head>
 <body>
 <%@ include file="/commons/header.jsp" %>
<div class="wrapper">
  <div class="left">
  <%@ include file="/commons/menu.jsp" %>
  </div>
  		<div id="navLink">
  			<s:if test="deviceId != null">
  				<a href="/r2k/dev/show.do" target="_self"><s:text name="r2k.menu.device"></s:text></a> &gt; <a href="/r2k/pub/device.do?deviceId=${deviceId}&deviceName=${deviceName}&devType=${devType}" target="_self"><s:text name="r2k.menu.pub"></s:text>(${deviceName})</a> &gt; 栏目引用(${deviceName})
  			</s:if>
  			<s:else>
			<a href="/r2k/pub/${actionName}.do" target="_self"><s:text name="r2k.menu.pub"></s:text>(${devTypeName})</a> &gt; 栏目引用(${devTypeName})
  			</s:else>
	    </div>
<div class="content">
<div id="result-contain" class="ui-widget">
	<div id="toolbar" class="ui-widget-header ui-corner-all">
  		<button id ="save">保存引用</button>
  	</div>
  		<s:if test="deviceId != null">
	  	<form id="form-list" action="/r2k/quote/index.do" method="post">
  		</s:if>
  		<s:else>
  		<form id="form-list" action="/r2k/quote/orgQuoted.do" method="post">
  		</s:else>
	  		<input type="hidden" name="deviceId" value="${deviceId}">
	  		<input type="hidden" name="devType" value="${devType}">
	  		<input type="hidden" name="deviceName" value="${deviceName}">
	  		<input type="hidden" name="setNo" value="${setNo}">
	  		<input type="hidden" name="homeName" value="${homeName}">
	    	<table id="table-expandable" class="ui-widget ui-widget-content gridBody">
		    <thead>
		      <tr class="ui-widget-header ">
		        <th>栏目名</th>
		        <th>类型</th>
		        <th>上级目录名</th>
		        <th>创建时间</th>
		        <th><input onclick="selectAll();" type="checkbox" id="controlAll"/> 全选</th>
		      </tr>
		    </thead>
		  	<tbody>
		      <s:iterator value="page.result">
				<s:if test="type == 1 || type == 2 || type == 3">
					<tr id="node-${id}" data-tt-id="${id}"
					data-tt-parent-id="${parentId==0?'':parentId}"
					class="leaf collapsed" quoteid="${quoteId}"
					data-tt-childs="0" level="1">
				</s:if>
				<s:else>
				<tr id="node-${id}" data-tt-id="${id}"
					data-tt-parent-id="${parentId==0?'':parentId}"
					class="${childs==0 ?'leaf':'isparent branch'} collapsed" quoteid="${quoteId}"
					data-tt-childs="${childs}" level="1">
				</s:else>
					<td>
						<s:property value="title"/>
					</td>
					<td>
						<s:if test="type==0">欢迎页</s:if>
						<s:elseif test="type==1">文章</s:elseif>
						<s:elseif test="type==2">图片</s:elseif>
						<s:elseif test="type==3">视频</s:elseif>
						<s:elseif test="type==4">图集</s:elseif>
						<s:elseif test="type==11">文章栏目</s:elseif>
						<s:elseif test="type==13">视频集</s:elseif>
						<s:elseif test="type==14">图集列表</s:elseif>
					</td>
					<td>
						<s:property value="parentTitle" />
					</td>
					<td><s:date name="crtDate" format="yyyy-MM-dd HH:mm:ss"/></td>
					<td>
						<input name="checked" value="${id}" type="checkbox">
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
</div>
<div class="footer"><%@ include file="/commons/footer.jsp" %></div>
  </body>
</html>
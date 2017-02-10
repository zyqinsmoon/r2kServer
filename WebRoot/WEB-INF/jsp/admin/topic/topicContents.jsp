<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib tagdir="/WEB-INF/tags/simpletable" prefix="simpletable"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" scope="page"/>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
  <head> 
  	<s:if test="pageType == 'show'">
    <title>专题内容列表</title>
  	</s:if>
  	<s:if test="pageType == 'check'">
    <title>查看专题生成条件</title>
  	</s:if>
  	<%@ include file="/commons/meta.jsp" %>
  	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
	<script type="text/javascript" src="${ctx}/js/Treetable_files/jquery.treetable.js"></script><!--这个是现实表格的文件-->
    <link rel="stylesheet" type="text/css" href="${ctx}/css/Treetable_files/jquery.treetable.css" />
    <link rel="stylesheet" href="${ctx}/css/Treetable_files/jquery.treetable.theme.default.css" />
    <link rel="stylesheet" href="${ctx}/css/Treetable_files/screen.css" media="screen" />

    <style type="text/css">
    	div#infos{
    		overflow:auto;
    	}
		div#infoHead{
			padding-top:5px;
			font-size: 20px;
			border-right: 1px solid black;
			float:left;
			width:10%;
			height:40px;
		}
		div#infoHead span{
			margin-bottom:15px;
		}
		div#infoContent{
			<s:if test="pageType == 'show'">width:82%;</s:if>
			<s:if test="pageType == 'check'">width:90%;</s:if>
			padding-left:1%;
			font-size: 15px;
			font-weight: normal;
			<s:if test="pageType == 'show'">float:left;
			border-right: 1px solid black;</s:if>
		}
		span.infoLeft{
			width:50%;
			float:left;
		}
		span.infoRight{
			width:50%;
		}
		.infoLabel{
			font-weight: bold;
		}
		div#tools{
			width:6%;
			float:right;
		}
	</style>
	<script>
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
			        var position  = $(this).attr("value");
			        if(parseInt(position)!= NaN && 0 < parseInt(position)){
			        	var topicId = $("#topicId").attr("value");
			        	$.get("${ctx}/topic/updateContent.do", { "article.id": id, "article.position": position, "article.topic":topicId, "topic.id":topicId } );
			        }else{
			        	alert("数字必须大于0");
			        }
			    }
			});
			
			//创建simpleTable
  			var sortColumns = "<s:property value='#parameters.sortColumns'/>";
  			if(sortColumns == null || sortColumns == ""){
  				sortColumns = "<s:property value='topic.condition.sort'/>";
  				if(sortColumns == null || sortColumns == ""){
  					sortColumns = " Position asc";
  				}
  			}
  			window.simpleTable = new SimpleTable('form-list',${page.thisPageNumber},${page.pageSize},sortColumns);
	        
	        $( "#dialogdelete" ).dialog({
      			autoOpen: false,
      			resizable: false,
      			height:140,
      			modal: true,
      			buttons: {
        			"确定": function() {
        				$("#form-list").attr("action","/r2k/topic/deleteTopicContent.do");
        				$("#form-list").submit();
        			},
        			"取消": function() {
          				$( this ).dialog( "close" );
        			}
      			}
    		}); 
    		
    		//复选框全选
  	    	$("#checkAll").click(function(){
  	    		$('input[type="checkbox"][name="topicContentIds"]').attr("checked",this.checked);
			});
			
			 //批量删除按钮
  	  		$( "#batDel" )
      			.button()
      			.click(function() {
    	  			if(checkSelect()){
	    	  			$( "#dialogdelete" ).dialog( "open" );
    	  			}
      		});
      		
      		//信息提示框
  	  		$( "#dialog-message" ).dialog({
				autoOpen: false,
				modal: true,
				buttons: {
					Ok: function() {
						$( this).dialog( "close" );
					}
				}
			});
		});
	  		
	  	//判断复选框是否空选
	  	function checkSelect(){
		  var boxFlag = false;
		  var boxs = $('input[type="checkbox"][name="topicContentIds"]');
		  for(var i = 0, len = boxs.length; i < len; i++){
			  if(boxs[i].checked ) boxFlag = true; 
		  }
		  if(!boxFlag){
			  $("#tipmsg").html("").html("勾选专题内容不能为空。");
			  $( "#dialog-message" ).dialog( "open" );
		  }
		  return boxFlag;
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
  	<s:if test="pageType == 'show'">
  	<a href="/r2k/topic/showTopics.do" target="_self"><s:text name="r2k.menu.topic"></s:text></a> &gt; 专题内容列表
  	</s:if>
  	<s:if test="pageType == 'check'">
  	<a href="/r2k/topic/showTopics.do" target="_self"><s:text name="r2k.menu.topic"></s:text></a> &gt; 查看专题生成条件
  	</s:if>
  </div>
<div class="content">
<div id="container" class="ui-widget">
	<div id="infos" class="ui-widget-header ui-corner-all">
		<div id="infoBar">
		<div id="infoHead">
		<span>所属专题信息</span>
		</div>
		<div id="infoContent">
		<div>
			<span class="infoLeft"><label class="infoLabel">专题名称:</label>
			<s:property value="topic.topicName" /></span>
			<span class="infoRight"><label class="infoLabel">专题描述:</label>
			<s:property value="topic.description" /></span>
		</div>
		<div>
			<span class="infoLeft"><label class="infoLabel">生成条件:</label>
			<s:property value="topic.condition.query" /></span>
			<span class="infoRight"><label class="infoLabel">生成时间:</label>
			<s:date name="topic.updateTime" format="yyyy-MM-dd HH:mm:ss"/></span>
		</div>
		</div>
		<s:if test="pageType == 'show'">
		<div id="tools">
			<button id="batDel" >批量删除</button>	
		</div>
		</s:if>
		</div>
	</div>
		<s:if test="pageType == 'show'">
		<form id="form-list" action="/r2k/topic/showContent.do" method="post">
		<input type="hidden" id="topicId" name="topic.id" value='<s:property value="topic.id"/>' />
		<input type="hidden" name="topic.topicName" value='<s:property value="topic.topicName"/>' />
		<input type="hidden" name="topic.description" value='<s:property value="topic.description"/>' />
		<input type="hidden" name="topic.condition.query" value='<s:property value="topic.condition.query"/>' />
		<input type="hidden" name="topic.updateTime" value='<s:date name="topic.updateTime" format="yyyy-MM-dd HH:mm:ss"/>' />
		<input type="hidden" id="pageType" name="pageType" value='<s:property value="#parameters.pageType"/>'></s:if>
    	<table id="table-list" class="ui-widget ui-widget-content gridBody">
	    <thead>
	      <tr class="ui-widget-header ">
	      	<s:if test="pageType == 'show'">
	      	<th width="5%"><input id="checkAll" type="checkbox"/>全选</th>
	      	</s:if>
	        <th width="27.5%" <s:if test="pageType == 'show'">class="sortCol"</s:if> >文章标题</th>
	        <th width="10%">作者</th>
			<th width="10%" <s:if test="pageType == 'show'">class="sortCol"</s:if> sortColumn="PaperName">所属报纸</th>
			<th width="27.5%">期次</th>
			<th width="10%">出版形式</th>
			<s:if test="pageType == 'show'">
				<th width="10%" <s:if test="pageType == 'show'">class="sortCol"</s:if> sortColumn="Position">显示顺序</th>        
			</s:if>
	      </tr>
	    </thead>
	  	<tbody>
	      <s:iterator value="page.result">
			<tr id="node-${id}" data-tt-id="${id}" data-tt-parent-id="${parentId==0?'':parentId}" class="${childs==0?'leaf':'isparent branch'} collapsed" data-tt-childs="${childs}" level="1">
				<s:if test="pageType == 'show'">
				<td><input name="topicContentIds" type="checkbox" value="${id}"/></td>
				</s:if>
				<td>
					<s:if test="headLine == ''">(无标题)</s:if>
					<s:else><s:property value="headLine"/></s:else> 
				</td>
				<td><s:property value="author"/></td>
				<td><s:property value="paperName"/></td>
				<td><s:property value="period"/></td>
				<td><s:property value="periodType"/></td>
				<s:if test="pageType == 'show'">
					<td><input name="${id}" class="spinner" style="width:30px" value="${position}" min="1"></td>
				</s:if>
			</tr>
		  </s:iterator>
	    </tbody>
	  </table>
	  <s:if test="pageType == 'show'">
	  <simpletable:pageToolbar page="${page}"></simpletable:pageToolbar>
	  </form>
	  </s:if>
</div>
</div>
</div>
	<div id="dialogdelete" title="删除">
           <p>确认删除？</p>
	</div> 
	<div id="dialog-message" title="提示信息">
			<span id="tipmsg" style="margin:5% auto;"></span>
		</div>
	<s:if test="pageType == 'show'">
	<div id="deleteDiv" style="display:none;">
		<form id="form-del" action="/r2k/topic/deleteTopicContent.do" method="post">
		<input type="hidden" id="topicId" name="topic.id" value='<s:property value="topic.id"/>' />
		<input type="hidden" name="topic.topicName" value='<s:property value="topic.topicName"/>' />
		<input type="hidden" name="topic.description" value='<s:property value="topic.description"/>' />
		<input type="hidden" name="topic.condition['Query']" value='<s:property value="topic.condition['Query']"/>' />
		<input type="hidden" name="topic.updateTime" value='<s:property value="topic.updateTimeString"/>' />
		<input type="hidden" id="pageType" name="pageType" value='<s:property value="#parameters.pageType"/>'>
		<input id="deleteid" name="article.id" type="hidden"  />
		</form>
	</div>
	</s:if>
 <div class="footer"><%@ include file="/commons/footer.jsp" %></div>
</body>
</html>

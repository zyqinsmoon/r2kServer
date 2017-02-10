<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib tagdir="/WEB-INF/tags/simpletable" prefix="simpletable"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" scope="page" />
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
	<head>
		<title>文章管理列表</title>
		<%@ include file="/commons/meta.jsp" %>
		<meta http-equiv="pragma" content="no-cache">
		<meta http-equiv="cache-control" content="no-cache">
		<meta http-equiv="expires" content="0">
		
		<!--这个是现实表格的文件-->
		<script	src="${ctx}/js/Treetable_files/jquery.treetable.js"></script>
		
		<link rel="stylesheet" type="text/css"
			href="${ctx}/css/Treetable_files/jquery.treetable.css" />
		<link rel="stylesheet"
			href="${ctx}/css/Treetable_files/jquery.treetable.theme.default.css" />
		<link rel="stylesheet" href="${ctx}/css/Treetable_files/screen.css"
			media="screen" />
			

		<style type="text/css">
			div#result-contain {
				width: 100%;
				margin: 0;
			}
			
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
			        	$.get("${ctx}/pub/sort.do", { id: id, sort: sort },
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
			
			//发布
	        $( "#publish" ).button().click(function() {
	        	$.ajax({
	       			type:'post',
	       			url: "${ctx}/pub/publish.do",
	       			data:{"devType":"${devType}"},
	       			beforeSend: function () {
	                    ShowDiv();
	                },
	                complete: function () {
	                    HiddenDiv();
	                },
	                dataType:'json',
	       			success:function(data){
	       				if(data.length == 0){
	       					$("#dialog-publish").html("").html("按机构发布模板成功！");
	    	            	$("#dialog-publish").dialog("open");
	       				}else{
	       					var msgs = "";
	       					var len = data.length;
	       					for(var i = 0; i < len; i++){
	       						msg = data[i];
	       						msgs = msgs + msg + "<br>";
	       					}
	       					$("#dialog-publish").html("").html(msgs);
		    	            $("#dialog-publish").dialog("open");
	       				}
	       				/*if(data == 1){
	       				}else if(data == 0){
	       					$("#dialog-publish").html("").html("按机构发布模板失败！");
	    	            	$("#dialog-publish").dialog("open");
	       				}*/
	       			}
	       		  });
		       	$( "#dialogdelete" ).dialog({
			      autoOpen: false,
			      resizable: false,
			      height:140,
			      modal: true,
			      buttons: {
			        "取消": function() {
			          $( this ).dialog( "close" );
			        }
			      }
			    });
	        });
	      //一键发布
	        $( "#publishAll" ).button().click(function() {
	        	$.ajax({
	       			type:'post',
	       			url: "${ctx}/pub/publishAll.do",
	       			beforeSend: function () {
	       				ShowDiv();
	                },
	                complete: function () {
	                	HiddenDiv();
	                },
	       			success:function(data){
	       				if(data == 1){
	       					$("#dialog-publish").html("").html("一键发布模板成功！");
	    	            	$("#dialog-publish").dialog("open");
	       				}else if(data == 0){
	       					$("#dialog-publish").html("").html("一键发布模板失败！");
	    	            	$("#dialog-publish").dialog("open");
	       				}
	       			}
	       		  });
	        });
	      	//查看首页
	        $( "#showHome" ).button().click(function() {
	        	window.open('<c:url value="/pub/showHome.do?devType=${devType}"/>','','');
	        });
	        //增加欢迎页
	        $( "#wel-save" ).button().click(function() {
	        	var url = '/r2k/pub/saveWelcome.do?orgId=${orgId}&devType=${devType}';
	        	window.location.href=url;
	        });
	        //增加顶级栏目
	        $("#col-save").button().click(function(){
	        	<s:if test="devType != 'ORG'">
	        	var homeName = "";
	        	<s:if test="defaultTemplate != null">
	        	homeName="${defaultTemplate.name}";
	        	</s:if>
	        	var setNo = $("#template").val();
	        	var url = '/r2k/pub/toSaveTopCol.do?orgId=${orgId}&devType=${devType}&setNo='+setNo+'&homeName='+homeName;
	        	</s:if>
	        	<s:else>
	        	var url = '/r2k/pub/toSaveTopCol.do?orgId=${orgId}&devType=${devType}';
	        	</s:else>
	        	window.location.href=url;
	        });
	        
	        $( "#art-save" ).button().click(function() {
	        	<s:if test="devType != 'ORG'">
	        	var homeName = "";
	        	<s:if test="defaultTemplate != null">
	        	homeName="${defaultTemplate.name}";
	        	</s:if>
	        	var setNo = $("#template").val();
	          	var url = '/r2k/pub/toSaveTopArt.do?orgId=${orgId}&devType=${devType}&top=1&setNo='+setNo+'&homeName='+homeName;
	        	</s:if>
	        	<s:else>
	        	var url = '/r2k/pub/toSaveTopArt.do?orgId=${orgId}&devType=${devType}';
	        	</s:else>
	        	window.location.href=url;
	      	});
	         
	        $( "#piccol-save" ).button().click(function() {
	        	<s:if test="devType != 'ORG'">
	        	var homeName = "";
	        	<s:if test="defaultTemplate != null">
	        	homeName="${defaultTemplate.name}";
	        	</s:if>
	        	var setNo = $("#template").val();
	          	var url = '/r2k/pub/toSaveTopPicsCol.do?orgId=${orgId}&devType=${devType}&setNo='+setNo+'&homeName='+homeName;
	        	</s:if>
	        	<s:else>
	        	var url = '/r2k/pub/toSaveTopPicsCol.do?orgId=${orgId}&devType=${devType}';
	        	</s:else>
	        	window.location.href=url;
	      	});
	      //栏目引用
	        $("#col-quote").button().click(function(){
	        	var setNo = $("#template").val();
	        	var homeName = $("#homeTemplate").val();
	        	if(setNo == "0"){
	        		$( "#dialog-message" ).html("").html("请先选择模板");
	        		$( "#dialog-message" ).dialog("open");
	        	}else if(homeName == "0"){
	        		$( "#dialog-message" ).html("").html("请先选择首页模板");
	        		$( "#dialog-message" ).dialog("open");
	        	}else{
        		window.location.href='/r2k/quote/orgQuoted.do?parentId=0&devType=${devType}&setNo='+setNo+'&homeName='+homeName;
	        	}
	        	
	        });
	      	$("#save-temp").button().click(function(){
	      		$("#dialogtemplate").dialog("open");
	      	});
		});
		
		//添加
		function addWelcome(){
			$("#art-parentId").val(id);
			$( "#dialog-art-save" ).dialog("option","title",title + "/文章添加").dialog( "open" );
		}
		//信息弹层
		$(function() {
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
			$(function() {
			  	$( "#dialog-publish" ).dialog({
					autoOpen: false,
					modal: true,
					buttons: {
						"确定": function() {
							$( this).dialog( "close" );
							window.location.reload();
						}
					},
			     	close: function() {
			     		$( this).dialog( "close" );
			        }
				});//end message dialog
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
		//设置删除对话框
	  $(function() {
	    $( "#dialogdelete" ).dialog({
	      autoOpen: false,
	      resizable: false,
	      height:140,
	      modal: true,
	      buttons: {
	        "确定": function() {
	        	var id = $("#deleteid").attr("value");
	        	var type = $("#colType").attr("value");
	        	$.ajax({
	           	 type:"GET",
	           	 async:false,
	           	 url:"${ctx}/pub/deleteOrgColumn.do?id=" + id +"&devType=${devType}&type="+type
	             });
	             location.reload();
	        },
	        "取消": function() {
	          $( this ).dialog( "close" );
	        }
	      }
	    });
	    
	    $("#dialogtemplate").dialog({
	    	autoOpen: false,
		      resizable: false,
		      height:140,
		      modal: true,
		      buttons: {
		        "确定": function() {
		        	var setNo = $("#template").val();
		        	var homeName = $("#homeTemplate").val();
		        	if(setNo != "0"){
		        		$("#choseSetNo").val(setNo);
		        		$("#homeName").val(homeName);
		        		$("#tempForm").submit();
		        	}else{
		        		$( this ).dialog( "close" );
		        		$("#dialog-message").html("").html("请先选择模板再保存");
		        		$( "#dialog-message" ).dialog("open");
		        	}
		        },
		        "取消": function() {
		          $( this ).dialog( "close" );
		        }
		      }
	    });
	    
	    $("#dialogcoltemplate").dialog({
	    	autoOpen: false,
		      resizable: false,
		      height:140,
		      modal: true,
		      buttons: {
		        "确定": function() {
		          $("#colTempForm").submit();
		        },
		        "取消": function() {
		          $( this ).dialog( "close" );
		        }
		      }
	    });
	  });
		    //打开删除对话框
		function deleteColumn(ctx, id,type){
            $("#deleteid").attr("value", id);
            $("#colType").attr("value", type);
			$("#dialogdelete").dialog( "open" );	 
	  }
        function getChilds(pid,quoteId){
        	var parentid = pid;
        	if(quoteId > 0){
        		parentid = quoteId;
        	}
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
        		url:"/r2k/pub/getOrgChilds.do",
        		data:{"parentId":parentid,"devType":"${devType}"},
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
        					tr.setAttribute("quoteid",quoteId>0?col.id:0);
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
        					var title = document.createTextNode(col.title==null?col.quoteTitle:col.title);
        					tdTitle.appendChild(span);
        					tdTitle.appendChild(title);
        					tr.appendChild(tdTitle);
        					var colType = col.type;
        					if(childs == 0 || colType == 1 || colType == 2 || colType == 3 ){
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
        							getChilds($(this).attr("id").replace("getchilds-",""),quoteId>0?col.id:0);
        						});
        						tr.removeAttribute("class");
        						tr.setAttribute("class","branch isparent collapsed");
        					}
        					tr.setAttribute("class",cls);
        					var tdType = document.createElement("td");
        					var type;
        					var updateUrl = null;
        					if(col.type == 1){
        						type = "文章";
        						updateUrl = "/r2k/pub/updateArt.do?id="+col.id+"&devType=${devType}";
        					}else if(col.type == 2){
        						type = "图片";
        						updateUrl = "/r2k/pub/toUpdatePic.do?id="+col.id+"&devType=${devType}";
        					}else if(col.type == 3){
        						type = "视频";
        						updateUrl = "/r2k/pub/toUpdateVideo.do?id="+col.id+"&devType=${devType}";
        					}else if(col.type == 4){
        						type = "图集";
        						updateUrl = "/r2k/pub/toUpdatePics.do?id="+col.id+"&devType=${devType}";
        					}else if(col.type == 11){
        						type = "文章栏目";
        						updateUrl = "/r2k/pub/toUpdateArtCol.do?id="+col.id+"&devType=${devType}";
        					}else if(col.type == 14){
        						type = "图集列表";
        						updateUrl = "/r2k/pub/toUpdatePicsCol.do?id="+col.id+"&devType=${devType}";
        					}else if(col.type == 13){
        						type = "视频集";
        						updateUrl = "/r2k/pub/toUpdateVideos.do?id="+col.id+"&devType=${devType}";
        					}
        					tdType.innerText = type;
        					tr.appendChild(tdType);
        					<s:if test="devType != 'ORG'">
        					var tdStatus = document.createElement("td");
        					if(col.type != 2){
        					tdStatus.setAttribute("id","status-" + col.id);
        					var status = "";
        					if(col.status == 0){
        						status = "未发布";
        					}else if(col.status == 1){
        						status = "发布";
        					}
        					tdStatus.innerText = status;
        					}
        					tr.appendChild(tdStatus);
        					</s:if>
        					var tdDate = document.createElement("td");
        					tdDate.innerText = col.crtDate.replace("T"," ");
        					tr.appendChild(tdDate);
        					var tdOP = document.createElement("td");
        					if(col.type != 3 && quoteId==0){
        						var aModi = document.createElement("a");
            					//aModi.setAttribute("href",updateUrl);
            					aModi.innerText="[修改]";
            					aModi.setAttribute("style","margin-right:4px");
            					aModi.setAttribute("href",updateUrl);
            					tdOP.appendChild(aModi);
        					}
        					if(quoteId == 0){
        					var aDel = document.createElement("a");
        					aDel.setAttribute("id","a-del-"+col.id);
        					aDel.setAttribute("href","#");
        					aDel.setAttribute("onclick","deleteColumn('${ctx}', " + col.id + ");");
        					aDel.innerText="[删除]";
        					aDel.setAttribute("style","margin-right:4px");
        					tdOP.appendChild(aDel);
        					}
        					<s:if test="devType != 'ORG'">
        					var aPreview = document.createElement("a");
        					aPreview.setAttribute("id","a-view-"+col.id);
        					aPreview.setAttribute("href","#");
        					aPreview.setAttribute("onclick","window.open('/r2k/pub/preview.do?devType=${devType}&id="+col.id + "','','');");
        					if(col.type !=2){
        					aPreview.innerText="[预览]";
        					aPreview.setAttribute("style","margin-right:4px");
        					}
        					tdOP.appendChild(aPreview);
        					</s:if>
        					if(quoteId == 0){
        					if(col.type == 11){
        						<s:if test="devType != 'ORG'">
        						var aCol = document.createElement("a");
        						aCol.setAttribute("href","/r2k/pub/toSaveCol.do?id="+col.id+"&devType=${devType}&templateId="+col.infoTemplate.id);
        						aCol.innerText="[添加子栏目]";
        						aCol.setAttribute("style","margin-right:4px");
        						tdOP.appendChild(aCol);
        						var aArt = document.createElement("a");
        						aArt.setAttribute("href","/r2k/pub/saveArt.do?id="+col.id+"&devType=${devType}&setNo="+col.infoTemplate.setNo+"&templateName="+col.infoTemplate.name);
        						aArt.innerText="[添加文章]";
        						aArt.setAttribute("style","margin-right:4px");
        						tdOP.appendChild(aArt);
        						</s:if>
        						<s:else>
        						var aCol = document.createElement("a");
        						aCol.setAttribute("href","/r2k/pub/toSaveCol.do?id="+col.id+"&devType=${devType}");
        						aCol.innerText="[添加子栏目]";
        						aCol.setAttribute("style","margin-right:4px");
        						tdOP.appendChild(aCol);
        						var aArt = document.createElement("a");
        						aArt.setAttribute("href","/r2k/pub/saveArt.do?id="+col.id+"&devType=${devType}");
        						aArt.innerText="[添加文章]";
        						aArt.setAttribute("style","margin-right:4px");
        						tdOP.appendChild(aArt);
        						</s:else>
        					}else if(col.type == 14){
        						<s:if test="devType != 'ORG'">
        						var aArt = document.createElement("a");
        						aArt.setAttribute("href","/r2k/pub/toSavePics.do?id="+col.id+"&devType=${devType}&setNo="+col.infoTemplate.setNo+"&templateName="+col.infoTemplate.name);
        						aArt.innerText="[添加子图集]";
        						aArt.setAttribute("style","margin-right:4px");
        						tdOP.appendChild(aArt);
        						</s:if>
        						<s:else>
        						var aArt = document.createElement("a");
        						aArt.setAttribute("href","/r2k/pub/toSavePics.do?id="+col.id+"&devType=${devType}");
        						aArt.innerText="[添加子图集]";
        						aArt.setAttribute("style","margin-right:4px");
        						tdOP.appendChild(aArt);
        						</s:else>
        					}else if(col.type == 13){
        						<s:if test="devType != 'ORG'">
        						var aCol = document.createElement("a");
        						aCol.setAttribute("href","/r2k/pub/toSaveVideos.do?id="+col.id+"&devType=${devType}&templateId="+col.infoTemplate.id);
        						aCol.innerText="[添加子视频集]";
        						aCol.setAttribute("style","margin-right:4px");
        						tdOP.appendChild(aCol);
        						var aArt = document.createElement("a");
        						aArt.setAttribute("href","/r2k/pub/toSaveVideo.do?id="+col.id+"&devType=${devType}&setNo="+col.infoTemplate.setNo+"&templateName="+col.infoTemplate.name);
        						aArt.innerText="[添加视频]";
        						aArt.setAttribute("style","margin-right:4px");
        						tdOP.appendChild(aArt);
        						</s:if>
        						<s:else>
        						var aCol = document.createElement("a");
        						aCol.setAttribute("href","/r2k/pub/toSaveVideos.do?id="+col.id+"&devType=${devType}");
        						aCol.innerText="[添加子视频集]";
        						aCol.setAttribute("style","margin-right:4px");
        						tdOP.appendChild(aCol);
        						var aArt = document.createElement("a");
        						aArt.setAttribute("href","/r2k/pub/toSaveVideo.do?id="+col.id+"&devType=${devType}");
        						aArt.innerText="[添加视频]";
        						aArt.setAttribute("style","margin-right:4px");
        						tdOP.appendChild(aArt);
        						</s:else>
        					}else if(col.type == 4){
        						var aCol = document.createElement("a");
        						aCol.setAttribute("href","/r2k/pub/toSavePic.do?id="+col.id+"&devType=${devType}");
        						aCol.innerText="[添加图片]";
        						aCol.setAttribute("style","margin-right:4px");
        						tdOP.appendChild(aCol);
        					}
        					}
        					tr.appendChild(tdOP);
        					var devType = "${devType}";
        					if(devType != "ORG"){
        					var tdTemp = document.createElement("td");
        					if(col.type == 11 || col.type == 14 || col.type == 13){
        						var sel = document.createElement("select");
        						sel.setAttribute("id","colTemp-"+col.id);
        						sel.setAttribute("onclick","getColTemplates('"+col.type+"','"+col.id+"')");
        						sel.setAttribute("onchange","selectColTemplate('"+col.id+"')");
        						var temp = col.infoTemplate;
        						var opt = document.createElement("option");
        						var optText = "-请选择-";
        						opt.setAttribute("selected","selected");
        						if(temp != null){
        							opt.setAttribute("value",temp.id);
        							optText=temp.name;
        						}
        						opt.innerText=optText;
        						sel.appendChild(opt);
        						tdTemp.appendChild(sel);
        					}else{
        					}
        					if(col.infoTemplate != null){
        						tdTemp.innerText=col.infoTemplate.name;
        					}
        					tr.appendChild(tdTemp);
        					}
        					<s:if test="devType != 'ORG'">
        					var tdSort = document.createElement("td");
        					if(type != 0 && quoteId == 0){
        						var input = document.createElement("input");
        						input.setAttribute("name",col.id);
        						input.setAttribute("class","spinner");
        						input.setAttribute("style","width:30px");
        						input.setAttribute("value",col.sort);
        						input.setAttribute("min",1);
        						tdSort.appendChild(input);
        					}
        					tr.appendChild(tdSort);
        					</s:if>
        				}
        				$("#node-"+pid).removeClass("collapsed");
        				$("#node-"+pid).addClass("expanded");
        				<s:if test="devType != 'ORG'">
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
        				        	$.get("${ctx}/pub/sort.do", { id: id, sort: sort },
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
        			    </s:if>
        			}
        		}
        	});
        		$("#getchilds-"+pid).removeAttr("title");
        		$("#getchilds-"+pid).attr("title","Collapse");
        	}
        }
		
        function initBindGetChilds(){
        	$("#table-expandable tbody tr").each(function(){
        		var childs = this.getAttribute("data-tt-childs");
        		var quoteid = this.getAttribute("quoteid");;
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
           				getChilds(id,quoteid);
           			});
        			this.removeAttribute("class");
        			this.setAttribute("class","branch isparent collapsed");
        		}
        	});
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
        
        function aa(){
        	document.getElementById("").getElementsByTagName("");
        }
        
        function load(){
        	initBindGetChilds();
        }
        window.onload = load;
        
        function getTemplates(){
        	$.ajax({
        		type:"get",
        		url:"/r2k/temp/getOrgAllTemplates.do",
        		data:{"devType":"${devType}"},
        		dataType:"json",
        		success:function(data){
        			var length = data.length;
        			if(length > 0){
        				var defaultSetNo = $("#template").val();
        				$("#template").empty();
        				for(var i = 0; i < length; i++){
        					var template = data[i];
        					var newOpt = '<option value="'+template.setNo+'">'+template.setNo+'/'+template.setName+'</option>';
        					if(template.setNo == defaultSetNo){
        						newOpt = '<option value="'+template.setNo+'" selected="selected">'+template.setNo+'/'+template.setName+'</option>';
        						$("#homeTemplate").empty();
        						var homeOpt = '<option value="'+template.name+'" selected="selected">'+template.name+'</option>';
        						$("#homeTemplate").append(homeOpt);
        					}
        					$("#template").append(newOpt);
        				}
        			}
        		}
        	});
        }
        
        function selectTemplates(){
        	$("#dialogtemplate").dialog("open");
        }
        
        function getColTemplates(type,id){
        	var setNo = $("#template").val();
        	if(setNo == null || setNo == ""){
        		$("dialog-message").html("").html("请先选择使用哪套模板");
        		$("dialog-message").dialog("open");
        	}else{
        	$.ajax({
        		type:"get",
        		url:"/r2k/temp/getOrgColTemplate.do",
        		data:{"setNo":setNo,"columnType":type},
        		dataType:"json",
        		success:function(data){
        			var length = data.length;
        			if(length > 0){
        				var selid="#colTemp-"+id;
        				var tempName = $(selid).val();
        				$(selid).empty();
        				for(var i = 0; i < length; i++){
        					var template = data[i];
        					var newOpt = '<option value="'+template.id+'">'+template.name+'</option>';
        					if(template.name == tempName){
        						newOpt = '<option value="'+template.id+'" selected="selected">'+template.name+'</option>';
        					}
        					$(selid).append(newOpt);
        				}
        			}
        		}
        	});
        	}
        }
        
        function selectColTemplate(id){
        	var tempId = $("#colTemp-"+id).val();
        	if(tempId != 0){
        	var setNo = $("#template").val();
        	$("#colSetNo").val(setNo);
        	$("#choseCol").val(id);
        	$("#choseTemp").val(tempId);
        	$("#dialogcoltemplate").dialog("open");
        	}
        }
        
        function getHomeTemplates(){
        	var setNo = $("#template").val();
        	if(setNo == null || setNo == ""){
        		$("dialog-message").html("").html("请先选择使用哪套模板");
        		$("dialog-message").dialog("open");
        	}else{
        	$.ajax({
        		type:"get",
        		url:"/r2k/temp/getHomeTemplates.do",
        		data:{"setNo":setNo},
        		dataType:"json",
        		success:function(data){
        			var length = data.length;
        			if(length > 0){
        				var selid="#homeTemplate";
        				var tempName = $(selid).val();
        				$(selid).empty();
        				for(var i = 0; i < length; i++){
        					var template = data[i];
        					var newOpt = '<option value="'+template.name+'">'+template.name+'</option>';
        					if(template.name == tempName){
        						newOpt = '<option value="'+template.name+'" selected="selected">'+template.name+'</option>';
        					}
        					$(selid).append(newOpt);
        				}
        			}
        		}
        	});
        	}
        }
        function toUpdatePage(url){
        	<s:if test="devType != 'ORG'">
        	var setNo=$("#template").val();
        	window.location=url+"&setNo="+setNo;
        	</s:if>
        	<s:else>
        	window.location=url;
        	</s:else>
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
			<a href="/r2k/pub/${actionName}.do" target="_self"><s:text name="r2k.menu.pub"></s:text>(${devTypeName})</a>
	    </div>			
		<div class="content">
					<div id="container" class="ui-widget">
						<div id="toolbar" class="ui-widget-header ui-corner-all">
							<button id="wel-save" <c:if test="${addWelcome==false}">style="display:none"</c:if>>
								添加欢迎页
							</button>
							<button id="col-save">
								添加顶级文章栏目
							</button>
							<button id="art-save">
								添加顶级文章
							</button>
							<button id="piccol-save">
								添加顶级图集列表
							</button><!-- 
							<button id="videocol-save">
								添加顶级视频集
							</button> -->
							<s:if test="devType != 'ORG'">
							<button id="col-quote">
								引用公用内容
							</button>
							<button id="publish">
								发布
							</button>
							<button id="showHome">
								查看首页
							</button>
							</s:if>
						</div>
						<s:if test="devType != 'ORG'">
						<div id="template-bar" class="ui-widget-header ui-corner-all">
							<label for="template">选择模板:</label>
							<select name="template" id="template" onchange="getHomeTemplates()" >
								<s:if test="templateSetList == null || templateSetList.size <= 0">
									<option value="0">-----------请选择-----------</option>
								</s:if>
								<s:else>
									<s:iterator value="templateSetList" var="it">
										<s:if test="%{defaultTemplate != null && #it.setNo == defaultTemplate.setNo}">
											<option value="${it.setNo}" selected="selected">${it.setNo}/${it.setName}</option>
										</s:if>
										<s:else>
											<option value="${it.setNo}">${it.setNo}/${it.setName}</option>
										</s:else>
									</s:iterator>
								</s:else>
							</select>
							<label for="template">选择首页模板:</label>
							<select name="template" id="homeTemplate" >
								<s:if test="defaultTemplates == null || defaultTemplates.size ==0 || defaultTemplates['home']==null">
									<option value="0">--请选择--</option>
								</s:if>
								<s:else>
									<s:iterator value="defaultTemplates['home']" var="hit">
										<s:if test="defaultTemplate != null && #hit.name == defaultTemplate.name">
											<option value="${hit.name}" selected="selected">${hit.name}</option>
										</s:if>
										<s:else>
											<option value="${hit.name}">${hit.name}</option>
										</s:else>
									</s:iterator>
								</s:else>
							</select>
							<button id="save-temp">确定</button>
						</div>
						</s:if>
						<s:if test="devType == 'ORG'">
							<form id="form-list" action="/r2k/pub/org.do" method="post">
						</s:if>
						<s:elseif test="devType == 'Android-Large'">
							<form id="form-list" action="/r2k/pub/showLarge.do" method="post">
						</s:elseif>
						<s:elseif test="devType == 'Android-Portrait'">
							<form id="form-list" action="/r2k/pub/showPortrait.do" method="post">
						</s:elseif>
						<s:elseif test="devType == 'Android-Phone'">
							<form id="form-list" action="/r2k/pub/showAndroidPhone.do" method="post">
						</s:elseif>
						<s:elseif test="devType == 'Android-Pad'">
							<form id="form-list" action="/r2k/pub/showAndroidPad.do" method="post">
						</s:elseif>
						<s:elseif test="devType == 'iPad'">
							<form id="form-list" action="/r2k/pub/showIPad.do" method="post">
						</s:elseif>
						<s:elseif test="devType == 'iPhone'">
							<form id="form-list" action="/r2k/pub/showIPhone.do" method="post">
						</s:elseif>
							<input type="hidden" name="devType" value="${devType}">
							<table id="table-expandable"
								class="ui-widget ui-widget-content gridBody">
								<thead>
									<tr class="ui-widget-header ">
										<th style="width: 18%;">
											栏目名
										</th>
										<th style="width: 8%;">
											类型
										</th>
										<s:if test="devType != 'ORG'">
										<th style="width: 10%;">
											状态
										</th>
										</s:if>
										<th style="width: 18%;">
											创建时间
										</th>
										<th style="width: 30%;">
											操作
										</th>
										<s:if test="devType != 'ORG'">
										<th style="width: 18%">
											选择模板
										</th>
										</s:if>
										<s:if test="devType != 'ORG'">
										<th style="width: 8%;">
											显示顺序
										</th>
										</s:if>
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
												<s:if test="quoteId == 0">
													<s:property value="title" />
												</s:if>
												<s:else>
													<s:property value="parentTitle" />
												</s:else>
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
											<s:if test="devType != 'ORG'">
											<td id="status-${id}">
												<s:if test="type != 2">
												<s:if test="status==0">未发布</s:if>
												<s:elseif test="status==1">发布</s:elseif>
												</s:if>
											</td> 
											</s:if>
											<td>
												<s:date name="crtDate" format="yyyy-MM-dd HH:mm:ss" />
											</td>
											<td>
												<s:if test="quoteId == 0">
												<s:if test="type==0">
													<a href="/r2k/pub/updateWelcome.do?id=${id}&devType=${devType}">[修改]</a>
												</s:if>
												<s:elseif test="type==1">
													<a href="/r2k/pub/toUpdateTopArt.do?id=${id}&devType=${devType}<s:if test="devType != 'ORG'">&setNo=${defaultTemplate.setNo}</s:if>">[修改]</a>
												</s:elseif>
												<s:elseif test="type==2">
													<a href="/r2k/pub/toUpdatePic.do?id=${id}&devType=${devType}<s:if test="devType != 'ORG'">&setNo=${defaultTemplate.setNo}</s:if>">[修改]</a>
												</s:elseif>
												<s:elseif test="type==3">
													<a href="/r2k/pub/toUpdateTopVideos.do?id=${id}&devType=${devType}<s:if test="devType != 'ORG'">&setNo=${defaultTemplate.setNo}</s:if>">[修改]</a>
												</s:elseif>
												<s:elseif test="type==4">
													<a href="/r2k/pub/toUpdatePics.do?id=${id}&devType=${devType}<s:if test="devType != 'ORG'">&setNo=${defaultTemplate.setNo}</s:if>">[修改]</a>
												</s:elseif>
												<s:elseif test="type==11">
													<a href="/r2k/pub/toUpdateTopArtCol.do?id=${id}&devType=${devType}<s:if test="devType != 'ORG'">&setNo=${defaultTemplate.setNo}</s:if>">[修改]</a>
												</s:elseif>
												<s:elseif test="type==14">
													<a href="/r2k/pub/toUpdateTopPicsCol.do?id=${id}&devType=${devType}<s:if test="devType != 'ORG'">&setNo=${defaultTemplate.setNo}</s:if>">[修改]</a>
												</s:elseif>
												<s:elseif test="type==13">
													<a href="/r2k/pub/toUpdateVideo.do?id=${id}&devType=${devType}<s:if test="devType != 'ORG'">&setNo=${defaultTemplate.setNo}</s:if>">[修改]</a>
												</s:elseif>
												</s:if>
												<!-- modify('${type}','${id}','${title}','${sort}','${summary}','${content}','${image}') -->
												<a href="#" onclick="deleteColumn('${ctx}', '${id}','${type}')">[删除]</a>
												<s:if test="type != 0 && devType !='ORG'">
												<a href="#" onclick="window.open('<c:url value="/pub/preview.do?id=${id}&devType=${devType}"/>','','')">[预览]</a>
												</s:if>
												<s:if test="quoteId == 0">
												<s:elseif test="type == 4 ">
													<a href="/r2k/pub/toSavePic.do?id=${id}&devType=${devType}<s:if test="devType != 'ORG'">&setNo=${infoTemplate.setNo}&templateName=${infoTemplate.name}</s:if>">[添加图片]</a>
												</s:elseif>
												<s:if test="type == 11 ">
													<a href="/r2k/pub/toSaveCol.do?id=${id}&devType=${devType}<s:if test="devType != 'ORG'">&templateId=${infoTemplate.id}</s:if>">[添加子栏目]</a>
													<a href="/r2k/pub/toSaveArt.do?id=${id}&devType=${devType}<s:if test="devType != 'ORG'">&setNo=${infoTemplate.setNo}&templateName=${infoTemplate.name}</s:if>">[添加文章]</a>
												</s:if>
												<s:elseif test="type == 14 ">
													<!-- <a href="/r2k/pub/toSavePicsCol.do?id=${id}&devType=${devType}&templateId=${infoTemplate.id}">[添加子图集列表]</a> -->
													<a href="/r2k/pub/toSavePics.do?id=${id}&devType=${devType}<s:if test="devType != 'ORG'">&setNo=${infoTemplate.setNo}&templateName=${infoTemplate.name}</s:if>">[添加子图集]</a>
												</s:elseif>
												<s:elseif test="type == 13 ">
													<a href="/r2k/pub/toSaveVideos.do?id=${id}&devType=${devType}<s:if test="devType != 'ORG'">&templateId=${infoTemplate.id}</s:if>">[添加子视频集]</a>
													<a href="/r2k/pub/toSaveVideo.do?id=${id}&devType=${devType}<s:if test="devType != 'ORG'">&setNo=${infoTemplate.setNo}&templateName=${infoTemplate.name}</s:if>">[添加视频]</a>
												</s:elseif>
												</s:if>
											</td>
											<s:if test="devType != 'ORG'">
											<td>
												<s:if test="type == 1 || type == 4 || type == 11 || type == 12 || type == 14">
													<select id="colTemp-${id}" onchange="selectColTemplate('${id}')">
													<s:if test="defaultTemplates[colTempTypeMap[type]] != null">
														<s:iterator value="defaultTemplates[colTempTypeMap[type]]" var="st">
															<s:if test="infoTemplate != null && infoTemplate.name == #st.name">
															<option value="${st.id}" selected="selected">${st.name}</option>
															</s:if>
															<s:else>
															<option value="${st.id}">${st.name}</option>
															</s:else>
														</s:iterator>
													</s:if>
													<s:else>
														<option selected="selected">-请选择-</option>
													</s:else>
													</select>
												</s:if>
												<s:else>
													${infoTemplate.name}
												</s:else>
											</td>
											</s:if>
											<s:if test="devType != 'ORG'">
											<td class="tdSpinner">
												<s:if test="type!=0">
													<input name="${id}" class="spinner" style="width: 30px" value="${sort}" min="1" max="999">
												</s:if>
											</td>
											</s:if>
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
			<input id="colType" name="type" type="hidden" />
			<p>
				各设备对此的引用也会被删除，确认删除？
			</p>
		</div>
		<div id="dialogtemplate" title="修改模板">
			<input id="templateSetNo" name="setNo" type="hidden" />
			<p>
				各内容对应的模板将会被修改，原有的发布信息会被全部覆盖，是否继续？
			</p>
		</div>
		<form id="tempForm" action="/r2k/temp/selectOrgTemplate.do" method="post">
			<input type="hidden" name="devType" value="${devType}" />
			<input type="hidden" name="homeName" id="homeName"/>
			<input id="choseSetNo" type="hidden" name="setNo" />
		</form>
		<div id="dialogcoltemplate" title="修改栏目模板">
			<input id="templateSetNo" name="setNo" type="hidden" />
			<p>
				原有的发布信息会被覆盖，是否继续？
			</p>
		</div>
		<form id="colTempForm" action="/r2k/temp/selectOrgColTemplate.do" method="post">
			<input type="hidden" name="devType" value="${devType}" />
			<input id="colSetNo" type="hidden" name="setNo" />
			<input id="choseTemp" type="hidden" name="id" />
			<input id="choseCol" type="hidden" name="colId" />
		</form>
		<div id="dialog-message" title="提示信息"></div>
		<div id="dialog-publish" title="提示信息"></div>
		<div id="loading"><img src="${ctx}/images/ui-anim_basic_16x16.gif" alt="" /> <span style="font-weight: bolder;">正在加载数据,请稍候...</span></div>
		<div class="footer"><%@ include file="/commons/footer.jsp" %></div>
	</body>
</html>

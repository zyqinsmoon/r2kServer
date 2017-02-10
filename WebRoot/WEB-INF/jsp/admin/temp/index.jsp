<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib tagdir="/WEB-INF/tags/simpletable" prefix="simpletable"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" scope="page" />

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
	<head>
		<title>模板管理</title>
		<%@ include file="/commons/meta.jsp" %>
		<meta http-equiv="pragma" content="no-cache">
		<meta http-equiv="cache-control" content="no-cache">
		<meta http-equiv="expires" content="0">
		<script type="text/javascript" src="${ctx}/js/admin/template.js"></script>
		<script type="text/javascript" src="${ctx}/js/swfupload/swfupload.js"></script>
		<script type="text/javascript" src="${ctx}/js/swfupload/handlers.js"></script>
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
			
			.expand{background: url("${ctx}/images/Treetable_files/expand.png") no-repeat; padding-left: 16px;}
			.contract{background: url("${ctx}/images/Treetable_files/contract.png") no-repeat; padding-left: 16px;}
			.text-center{text-align:center}
			.child-style{background-color: #f0f0f0}
			.select-row{background-color: #91DAF6}
		</style>
	</head>

	<body>
	 <%@ include file="/commons/header.jsp" %>
		<div class="wrapper">
			<div class="left">
				<%@ include file="/commons/menu.jsp" %>
			</div>
			<div id="navLink">
				<a href="/r2k/temp/index.do" target="_self"><s:text name="r2k.menu.temp"></s:text></a>
			</div>
			<div class="content">
				<div id="toolbar" class="ui-widget-header ui-corner-all">
					<button id="save-record">新建模板</button>
				</div>
				<input type="hidden" id="currentOrgId" value='<s:property value="#session.SPRING_SECURITY_CONTEXT.Authentication.Principal.authUser.currentOrg.orgId"/>'/>
				<div id="container" class="ui-widget">
				<form id="form-list" action="/r2k/temp/index.do" method="post">
					<table id="table-expandable"
						class="ui-widget ui-widget-content gridBody">
						<thead>
							<tr class="ui-widget-header ">
								<th width="8%" class="text-center">
									模版套名
								</th>
								<th width="7%" class="text-center">
									文件名
								</th>
								<th width="8%" class="text-center">
									模版类型
								</th>
								<th width="5%" class="text-center">
									适用范围
								</th>
								<th width="15%" class="text-center">
									适用设备类型
								</th>
								<th width="15%" class="text-center">
									设备类型默认模版
								</th>
								<th width="20%" class="text-center">
									描述
								</th>
								<th width="12%" class="text-center">
									操作
								</th>
							</tr>
						</thead>
						<tbody>
							<s:iterator value="page.result">
								<tr id="tr_${setNo}_${scope}" status="contract" pid="0">
									<td><a id="a_${setNo}_${scope}" href="#" class="contract" onclick="getTree('${orgId}','${setNo}','${scope}');">${setNo}</a></td>
									<td>${setName}</td>
									<td>
										<s:if test="scope == 0">
											引用
										</s:if>
										<s:else>
											自定义
										</s:else>
									</td>
									<td>
										<s:if test="scope == 0">
											全部
										</s:if>
										<s:else>
											本机构
										</s:else>
									</td>
									<td id="type_${setNo}_${scope}">
										<s:iterator value="deviceType.split('#')" var="data">
											<s:property value="deviceTypeMap.get(#data).value"/>&nbsp;
										</s:iterator>
									</td>
									<td  id="defType_${setNo}_${scope}">
										<s:iterator value="defaultType.split('#')" var="defType">
											<s:property value="deviceTypeMap.get(#defType).value"/>&nbsp;
										</s:iterator>
									</td>
									<td id="td_${setNo}_${scope}_desc" onmouseover="descOver('desc_${setNo}_${scope}');" onmouseout="descOut('desc_${setNo}_${scope}');">
										<span>
											<s:if test="description.length() < 20">
												<s:property value="description" />
											</s:if>
											<s:else>
												<s:property value="description.substring(0,20)+'...'" />    
											</s:else>
										</span>
										<div id="desc_${setNo}_${scope}" style="display: none;">${description}</div>
									</td>
									<td>
										<a id="download-record" href="${ctx}/temp/downloadZip.do?templateSet.scope=${scope}&templateSet.setNo=${setNo}&templateSet.orgId=${orgId}&templateSet.setName=${setName}">[下载]</a>
										<s:if test="scope == 1">
											<a id="updateAll-record" href="#" onclick="updateSetTemp('${id}', '${setNo}','${setName}','${deviceType}','${defaultType}','${orgId}','${scope}');">[修改]</a>
											<a id="delete-record" href="#" onclick="deleteBySetNo('${id}');">[删除]</a>
										</s:if>
										<s:if test="scope == 0 && #session.SPRING_SECURITY_CONTEXT.Authentication.Principal.authUser.currentOrg.orgId =='apabi'">
											<a id="updateAll-record" href="#" onclick="updateSetTemp('${id}', '${setNo}','${setName}','${deviceType}','${defaultType}','${orgId}','${scope}');">[修改]</a>
											<a id="delete-record" href="#" onclick="deleteBySetNo('${id}');">[删除]</a>
										</s:if>
									</td>
								</tr>
							</s:iterator>
						</tbody>
					</table>
					<div id="divFileProgressContainer" style="width: 200; display: none;"></div>
					<simpletable:pageToolbar page="${page}"></simpletable:pageToolbar>
				</form>
				</div>
			</div>
		<div id="dialog-message" title="提示信息">
			<p id="txt-message" style="margin-top: 20px"></p>
		</div>
		<div id="dialog-delete" title="提示信息">
			<input type="hidden" id="deleteId">
			<div style="margin-top: 20px">确定要删除当前模板信息吗？</div>
		</div>
		<div id="dialog-update" title="修改信息">
			<form id="updateForm" method="post" enctype="multipart/form-data">
				<input type="hidden" id="updateId" name="templateSet.id"/>
				<input type="hidden" id="updateOrgId" name="templateSet.orgId"/>
				<input type="hidden" id="updateScope" name="templateSet.scope"/>
				<input type="hidden" id="updateDevType" name="templateSet.deviceType"/>
				<input type="hidden" id="updateDefaultType" name="templateSet.defaultType"/>
			<div class="field">
				<label class="field-label">
					模板套id<em style="color: red;">*</em>：
				</label>
				<input id="updateSetNo" name="templateSet.setNo" readonly="readonly" style="background-color: #f0f0f0"/>
			</div>
			<div class="field">
				<label class="field-label">
					模板文件名<em style="color: red;">*</em>：
				</label>
				<input id="updateSetName" name="templateSet.setName"/>
			</div>
			<div class="field">
				<label class="field-label">
					适用设备类型<em style="color: red;">*</em>：
				</label>
				<s:iterator value="deviceTypeMap.keySet()" status="type" var="key">
					<input name="devType" type="checkbox" value='<s:property value="#key"/>' onchange="js_check.opCheck(this)"/><s:property value="deviceTypeMap.get(#key).value"/>
				</s:iterator>
			</div>
			<div class="field">
				<label class="field-label">
					是否默认：
				</label>
				<s:iterator value="deviceTypeMap.keySet()" status="type" var="key">
					<input name="defaultType" type="checkbox" value='<s:property value="#key"/>' disabled="true" /><s:property value="deviceTypeMap.get(#key).value"/>
				</s:iterator>
			</div>
			<div class="field">
				<label class="field-label">
					整套替换：
				</label>
				<input type="file" id="zip" name="zip">
			</div>
			<div class="field">
				<label class="field-label">
					模板描述：
				</label>
				<textarea id="updateDesc" name="templateSet.description" rows="8" cols="60"></textarea>
			</div>
			</form>
		</div>
		<div id="loading"><img src="${ctx}/images/ui-anim_basic_16x16.gif" alt="" /> <span style="font-weight: bolder;">正在上传数据,请稍候...</span></div>
		<div class="footer"><%@ include file="/commons/footer.jsp" %></div>
		<script type="text/javascript">
			$(function(){
				//创建simpleTable
		  		var sortColumns = "<s:property value='#parameters.sortColumns'/>";
		  			if(sortColumns == null || sortColumns == ""){
		  				sortColumns = " d.ID desc";
		  			}
		  		window.simpleTable = new SimpleTable('form-list','${page.thisPageNumber}','${page.pageSize}',sortColumns);
				
				$("#save-record").button().click(function(){
					window.location.href='${ctx}/temp/savePage.do';
				});
				//信息提示弹层
				$( "#dialog-message" ).dialog({
					autoOpen: false,
					modal: true,
					buttons: {
						Ok: function() {
							$( this).dialog( "close" );
							}
						}
					});
				//删除弹层
			  	$( "#dialog-delete" ).dialog({
					autoOpen: false,
					modal: true,
					buttons: {
						"确定": function() {
							var deleteId = $("#deleteId").attr("value");
							window.location = "${ctx}/temp/delete.do?templateSet.id="+deleteId;
						},
						"取消": function() {
							$( this).dialog( "close" );
						}
					},
			     	close: function() {
			     		$( this).dialog( "close" );
			        }
				});//end delete dialog
				//修改弹层
			  	$( "#dialog-update" ).dialog({
					autoOpen: false,
					height: 430,
				    width: 700,
					modal: true,
					buttons: {
						"确定": function() {
							var nametxt = $.trim($('#updateSetName').attr('value'));
							if(nametxt == ''){
								$("#txt-message").html("").html("模板套名不能为空！");
				          		$( "#dialog-message" ).dialog("open");
				          		return;
							} else if (!js_check.checkTxtLength('updateDesc', 100)) {
								$("#txt-message").html("").html("描述超过字符限制，请重新输入！");
				          		$( "#dialog-message" ).dialog("open");
				          		return;
							}
							//验证复选框
							var checkflag = js_check.isCheck();
							if(!checkflag){
								return;
							}
							//设备模板默认设备类型字串
							var defaultTypeTxt = '';
							var defaultChecknode = $('input[name="defaultType"]:checked');
							for(var i = 0, len = defaultChecknode.length; i < len; i++){
								defaultTypeTxt = defaultTypeTxt + $(defaultChecknode[i]).attr('value') + '#';
							}
							defaultTypeTxt = defaultTypeTxt.substring(0,defaultTypeTxt.length-1);
							var oldDefType = $('#updateDefaultType').attr('value');
							if(defaultTypeTxt != oldDefType){
								//检查默认模板是否存在
								var orgId = $('#updateOrgId').attr('value');
								var scope = $('#updateScope').attr('value');
								var setNo = $('#updateSetNo').attr('value');
								var defaultflag = js_check.checkDefault(orgId, scope, setNo, defaultTypeTxt, checkDeal);
							} else {
								checkDeal();
							}
						},
						"取消": function() {
							$('#zip').attr('value','');
							$( this).dialog( "close" );
						}
					},
			     	close: function() {
			     		$('#zip').attr('value','');
			     		$( this).dialog( "close" );
			        }
				});//end update dialog
				
				<s:iterator value="tempTypeMap.keySet()" var="key">
					js_treeTable.tempTypes.push({'name':'<s:property value="#key"/>','desc':'<s:property value="tempTypeMap.get(#key).value"/>'});
				</s:iterator>
			});//end initfunction
			
			//按模板套号删除
			function deleteBySetNo(deleteId){
				$("#deleteId").attr("value",deleteId);
				$("#dialog-delete").dialog('open');
			}
			//修改套模板信息
			function updateSetTemp(id, setNo, setName, devType, defaultType, orgId, scope){
				$("#updateId").attr("value",id);
				$("#updateSetNo").attr("value",setNo);
				$("#updateSetName").attr("value",setName);
				$("#updateOrgId").attr("value",orgId);
				$("#updateScope").attr("value",scope);
				$("#updateDevType").attr("value",devType);
				$("#updateDefaultType").attr("value",defaultType);
				var descTxt = $('#desc_'+setNo+'_'+scope).html();
				$("#updateDesc").html(descTxt);
				var devTypeList = devType.split('#');
				var defaultTypeList = defaultType.split('#');
				//初始化复选框
				$('input[name="devType"]').attr('checked', false);
				$('input[name="defaultType"]').attr('checked', false);
				for ( var i = 0, len = devTypeList.length; i < len; i++) {
					$('input[name="devType"][value="'+ devTypeList[i] +'"]').attr('checked', true);
				}
				for ( var i = 0, len = defaultTypeList.length; i < len; i++) {
					$('input[name="defaultType"][value="'+ defaultTypeList[i] +'"]').attr('checked', true);
				}
				$('input[name="devType"]').each(function(){
					if($(this).attr('checked')){
						var txt = $(this).attr('value');
						$('input[name="defaultType"][value="'+ txt +'"]').attr('disabled', false);
					}
				});
				$("#dialog-update").dialog('open');
			}
			
			function getTree(orgId, setNo, scope){
				js_treeTable.getTree(orgId, setNo, scope, '${ctx}');
			}
			//显示等待弹层
			function ShowDiv() {
		       $("#loading").show();
			}
			//保存处理方法
			var checkDeal = function (){
				var txt = $('#zip').attr('value');
				if(txt != ''){
					var surfix = txt.substring(txt.lastIndexOf('.')); 
					if(surfix == '.zip'){
						save();
					} else {
						$('#zip').attr('value','');
						$('#txt-message').html('上传文件格式不正确。请上传zip文件。');
						$('#dialog-message').dialog('open');
					}
				} else {
					var defaultTypeTxt = '';
					var defaultChecknode = $('input[name="defaultType"]:checked');
					for(var i = 0, len = defaultChecknode.length; i < len; i++){
						defaultTypeTxt = defaultTypeTxt + $(defaultChecknode[i]).attr('value') + '#';
					}
					defaultTypeTxt = defaultTypeTxt.substring(0,defaultTypeTxt.length-1);
					$('#updateDefaultType').attr('value',defaultTypeTxt);
					save();
				}
			};
			//保存方法
			var save = function (){
				ShowDiv();
				//生成设备类型字串
				var devTypeTxt = '';
				var checknode = $('input[name="devType"]:checked');
				for(var i = 0, len = checknode.length; i < len; i++){
					devTypeTxt = devTypeTxt + $(checknode[i]).attr('value') + '#';
				}
				devTypeTxt = devTypeTxt.substring(0,devTypeTxt.length-1);
				$('#updateDevType').attr('value',devTypeTxt);
				
				//提交
				$("#updateForm").attr('action','${ctx}/temp/updateSet.do').submit();
				$("#dialog-update").dialog('close');
			};
			
			function descOver(id){
				var txt = $.trim($('#'+id).html());
				if(txt.length > 0){
					var node = $('#'+id).parent();
					var leftval = $(node).offset().left;
					var topval = $(node).offset().top;
					var heightval = $(node).height();
					var widthval = $(node).width();
					$('#'+id).offset({top:top+heightval,left:leftval});
					$('#'+id).css({'position':'absolute', 'width':widthval, 'margin':'1em 0', 'padding':'1em 10px', 'background-color':'#E6E3E3', 'display':'block'});
				}
			}
			function descOut(id){
				$('#'+id).css('display','none');
			}
		</script>
	</body>
</html>
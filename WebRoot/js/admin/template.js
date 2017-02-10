var js_treeTable = new TreeTable();
function TreeTable() {
	
	this.scopeAllTxt = '全部';
	this.scopeOrgTxt = '本机构';
	this.updateTempTxt = '编辑模板';
	this.tempTypes = new Array();
	
	this.getTree = function (orgId, setNo, scope, urlPrefix){
		var childs = $('#tr_'+setNo+'_'+scope).nextAll('[pid='+setNo+'_'+scope+']');
		js_treeTable.switchStatus(setNo, scope, childs);
		if(childs.length <= 0){
			var path = urlPrefix + '/temp/findTempListBySetNo.do';
			 $.ajax({
	        	url:path,
	        	type: 'POST',
	        	dataType: 'json',
	        	data: {"template.orgId":orgId,"template.setNo":setNo},
	        	success: function(result){
	        		if(result){
	        			js_treeTable.addChilds(result.templist, setNo, urlPrefix, scope);
	        		}
	        	}
	        });
		}
	};
	
	this.switchStatus = function (setNo, scope, childs){
		var status = $('#tr_'+setNo+'_'+scope).attr('status');
		if(status == 'expand'){
			$('#tr_'+setNo+'_'+scope).attr('status','contract'); 
			$('#a_' + setNo+'_'+scope).attr('class','contract');
			$(childs).hide();
		}else if(status == 'contract'){
			$('#tr_'+setNo+'_'+scope).attr('status','expand'); 
			$('#a_' + setNo+'_'+scope).attr('class','expand');
			$(childs).show();
		}
	};
	
	this.addChilds = function (children, setNo, urlPrefix, scope){
		$(children).each(function(index, element){
			js_treeTable.addChild(element, setNo, urlPrefix, scope);
		});
	};
	
	this.addChild = function (child, setNo, urlPrefix, scope){
		//行节点
		var trnode = $('<tr>').attr({'id':child.id, 'pid':setNo+'_'+scope}).addClass('child-style');
		//套id节点
		var setNoTdNode = $('<td>');
		//模板文件名节点
		var setNameTdNode = $('<td>').html(child.name+'.ftl');
		//模板类型节点
		var typeTdNode = $('<td>').html( getTempType(child.type) );
		//模板适用范围节点
		var scopetxt = (scope == 0 ? js_treeTable.scopeAllTxt : js_treeTable.scopeOrgTxt);
		var scopeTdNode = $('<td>').html(scopetxt);
		//模板适用设备类型
		var devType = $('#type_'+setNo+'_'+scope).html();
		var devTypeTdNode = $('<td>').html(devType);
		//设备类型默认模板
		var defaultType = $('#defType_'+setNo+'_'+scope).html();
		var defaultTypeTdNode = $('<td>').html(defaultType);
		//模板描述节点
		var descTdNode = $('<td>').html(child.description);
		//模板操作节点
		var dealTdNode = $('<td>');
		var deleteANode;
		var currentOrgId = $('#currentOrgId').attr('value');
		if(scope == 0 && currentOrgId == 'apabi'){
			var updateANode = $('<a>').attr({'href':urlPrefix + '/temp/updatePage.do?id=' + child.id}).html('['+js_treeTable.updateTempTxt+']');
			$(dealTdNode).append(updateANode);
		}else if(scope == 1){
			var updateANode = $('<a>').attr({'href':urlPrefix + '/temp/updatePage.do?id=' + child.id}).html('['+js_treeTable.updateTempTxt+']');
			$(dealTdNode).append(updateANode);
		}
		$(trnode).append(setNoTdNode).append(setNameTdNode).append(typeTdNode).append(scopeTdNode)
			.append(devTypeTdNode).append(defaultTypeTdNode).append(descTdNode).append(dealTdNode);
		$('#tr_'+setNo+"_"+scope).after(trnode);
		$('#'+child.id).live('click',function(){
			$('tr.select-row').each(function(){
				$(this).removeClass('select-row').addClass('child-style');
			});
			$(this).removeClass('child-style');
			$(this).addClass('select-row');
		});
	};
	
	function getTempType(type){
		var typename = '';
		for(var i = 0, len = js_treeTable.tempTypes.length; i < len; i++){
			var node = js_treeTable.tempTypes[i];
			if(node.name == type){
				typename = node.desc;
				break;
			}
		}
		return typename;
	}
}//end tree

var js_check = new CheckUtil();
function CheckUtil(){
	
	this.msgDialogId = 'dialog-message';
	this.msgDialogTxtId = 'txt-message';
	
	//检查设备类型
	this.isCheck = function (){
		var boxs = $('input[name="devType"]');
		var boxFlag = false;
		if(boxs != null && boxs.length > 0){
			for(var i = 0, len = boxs.length; i < len; i++){
				if(boxs[i].checked ) {
			  		boxFlag = true; 
			  		break;
				}
			}
			if(!boxFlag){
				$("#txt-message").html("").html("请选择模板适用设备类型！");
          		$("#dialog-message").dialog("open");
			}
		}
		return boxFlag;
	};
	//设置默认类型复选框是否可用属性
	this.opCheck = function (node){
		var flag = node.checked;
		var txt = $(node).attr('value');
		var opNode = $('input[name="defaultType"][value="'+txt+'"]');
		$(opNode).attr('disabled',!flag); 
		if(!flag){
			$(opNode).attr('checked',false);
		}
	};
	/**
	 * 判断文本是否超过最大长度
	 * @param id
	 * @param maxLen
	 * @returns {Boolean} true 成功(未超过)，false 失败(超过)
	 */
	this.checkTxtLength = function (id, maxLen){
		var txt = $('#'+id).attr('value');
		if(js_check.getLength(txt) >= maxLen){
			return false;
		}
		return true;
	};
	
	//获取字符串长度
	this.getLength = function (str) {
		//获得字符串实际长度，中文2，英文1
		var realLength = 0, len = str.length, charCode = -1;
		for (var i = 0; i < len; i++) {
			charCode = str.charCodeAt(i);
			if (charCode >= 0 && charCode <= 128) realLength += 1;
			else realLength += 2;
		}
		return realLength;
	};
	
	/**
	 * 检查是否已存在默认类型
	 * @param defaultType
	 * @param callback
	 */
	this.checkDefault = function (orgId, scope, setNo, defaultType, callback){
		var path = '';
		var data;
		if(scope == 0){
			path = '${ctx}/temp/checkDefaultType.do';
			data = {'templateSet.scope':0, 'templateSet.defaultType':defaultType, 'templateSet.setNo':setNo};
		} else if(scope == 1){
			path = '${ctx}/temp/checkOrgDefaultType.do';
			data = {'templateSet.scope':1, 'templateSet.defaultType':defaultType, 'templateSet.setNo':setNo};
		}
		$.ajax({
		    type: 'POST',
		    url: path ,
		    data: data ,
		    async:false,
		    success: function(result){
		    	if(result == 1) {
		    		callback();
		    		return true;
		    	} else if(result == 0) {
		    		$("#" + js_check.msgDialogTxtId).html("").html("默认类型模板已存在！");
		      		$("#" + js_check.msgDialogId).dialog("open");
//		      		$('input[name="defaultType"]').each(function(){
//		      			$(this).attr('disabled',true);
//		      			$(this).attr('checked',false);
//		      		});
		      		return false;
		    	} else if(result == -1) {
		    		$("#" + js_check.msgDialogTxtId).html("").html("选择默认类型模板错误！");
		      		$("#" + js_check.msgDialogId).dialog("open");
//		      		$('input[name="defaultType"]').each(function(){
//		      			$(this).attr('disabled',true);
//		      			$(this).attr('checked',false);
//		      		});
		      		return false;
		    	}
		    }
		});
	};
}//end check
var js_auth = new Auth();
function Auth () {
	this.dlgSaveId = '';
	this.dlgUdpateId = '';
	this.saveUrl = '';
	this.udpateUrl = '';
	
	this.saveButtons = {
		'保存' : function(){ js_auth.saveFunction(); },
		'取消' : function (){ js_auth.close(js_auth.dlgSaveId); }
	},
	this.updateButtons = {
		'保存' : function(){ js_auth.udpateFunction(); },
		'取消' : function(){ js_auth.close(js_auth.dlgUdpateId); }
	};
	this.saveFunction = function(){},
	this.udpateFunction = function(){},
	this.close = function(id) {
    	$('#'+id).dialog('close');
    };
};

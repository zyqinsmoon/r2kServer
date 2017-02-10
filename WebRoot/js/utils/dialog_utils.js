var DialogUtils = {
		//初始化页面对话框
		//dialogId:对话框id
		//autoOpen:是否自动打开(true/false)
		//height:对话框高度
		//width:对话框宽度
		//modal:是否模态窗口(true/false)
		//buttons:对话框按钮列表({"btn1":fun1(),"btn2":fun2()...})
		//closeFun:对话框关闭操作
		initDialog : function(dialogId, autoOpen, height, width, modal, buttons,closeFun){
			$("#"+dialogId).dialog({
				autoOpen: autoOpen,
				height: height,
				width: width,
				modal: modal,
				buttons: buttons,
				close: function(){
					if(closeFun != null && closeFun != ""){
						closeFun();
					}
				}
			});
		}
};
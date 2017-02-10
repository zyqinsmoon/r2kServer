var Menu = {
	//设置菜单是否隐藏，如果菜单隐藏则菜单不能设置为主页
	setIsHideMenu : function(){
		var msg = "";
		var isHide = $("#is_hide_menu").val();
		if(isHide == 1){		//设置为隐藏
			var isHome = $("#is_home_page").val();
			if(isHome == 1){
				msg = "本菜单已经设置为主页，不能设置为隐藏";
				isHide = 1;
				$("#hide_menu").removeAttr("checked");
				Menu.showMenuMsg(msg);
			}else{
				isHide = 0;
				$("#hide_menu").attr("checked","checked");
			}
		}else{		//设置为不隐藏
			isHide = 1;
			$("#hide_menu").removeAttr("checked");
		}
		$("#is_hide_menu").val(isHide);
	},
	//设置机构主页
	setOrgHomePage : function(deviceType){
		var msg = "";
		var isHome = $("#is_home_page").val();
		if(isHome == 1){
			Menu.setIsHomePage();
		}else{
			if(!Menu.checkOrgHomePage(deviceType)){		//不存在主页时才能设置主页
				Menu.setIsHomePage();
			}else{
				$("#home_page").removeAttr("checked");
				$("#is_home_page").val(0);
				msg = "当前机构已经设置主页，请不要重复设置，或者取消原主页再设置";
				Menu.showMenuMsg(msg);
			}
		}
	},
	//设置设备主页
	setDevHomePage : function(devType, deviceId){
		var msg = "";
		var isHome = $("#is_home_page").val();
		if(isHome == 1){
			Menu.setIsHomePage();
		}else{
			if(!Menu.checkDevHomePage(devType, deviceId)){		//不存在主页时才能设置主页
				Menu.setIsHomePage();
			}else{
				$("#home_page").removeAttr("checked");
				$("#is_home_page").val(0);
				msg = "当前机构已经设置主页，请不要重复设置，或者取消原主页再设置";
				Menu.showMenuMsg(msg);
			}
		}
	},
	//设置是否为主页，如果要设置为主页，菜单不能设置为隐藏
	setIsHomePage : function(){
		var msg = "";
		var isHome = $("#is_home_page").val();
		if(isHome == 0){	//设置为主页
			var isHide = $("#is_hide_menu").val();
			if(isHide == 0){
				msg = "本菜单已经设置为隐藏，不能设置为主页";
				isHome = 0;
				$("#home_page").removeAttr("checked");
				Menu.showMenuMsg(msg);
			}else{
				isHome = 1;
				$("#home_page").attr("checked","checked");
			}
		}else{		//设置为非主页
			isHome = 0;
			$("#home_page").removeAttr("checked");
		}
		$("#is_home_page").val(isHome);
	},
	//检查该设备类型是否已有主页
	checkOrgHomePage : function(deviceType){
		var hasHome = false;
		$.ajax({
			type:"get",
			url:"/r2k/menu/checkHomePage.do?devType="+deviceType,
			async:false,
			success:function(data){
				if(data != null){
					hasHome = true;
				}
			}
		});
		return hasHome;
	},
	//检查该设备是否已有主页
	checkDevHomePage : function(devType,deviceId){
		var hasHome = false;
		$.ajax({
			type:"get",
			url:"/r2k/menu/checkHomePage.do?devType="+devType+"&deviceId="+deviceId,
			async:false,
			success:function(data){
				if(data != null){
					hasHome = true;
				}
			}
		});
		return hasHome;
	},
	//显示菜单页面提示信息
	showMenuMsg : function(msg){
		var msgTitle = $("#dialog-msg").attr("title");
		if(msgTitle == null){
			$("body").append("<div id='dialog-msg' title='提示信息'></div>");
		}else{
			$("body").remove("#dialog-msg");
		}
		$( "#dialog-msg" ).dialog({
			autoOpen: false,
			modal: true,
			buttons: {
				"取消": function() {
					$(this).dialog( "close" );
				}
			},
			close: function() {
				$(this).dialog( "close" );
			}
		});
		$("#dialog-msg").html("").html(msg);
		$("#dialog-msg").dialog("open");
	}
}
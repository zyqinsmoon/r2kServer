var FileUtils = {
		//sizeLimit:文件大小限制，0为不限制，单位：kb
		setImagePreview : function(docObj,imgType,imgTypeNames,sizeLimit){
			if(sizeLimit != 0){
				var size = docObj.files[0].size;
				if(size != null && size != ""){
					if(size > sizeLimit*1024){
						alert("图片大小不能大于"+sizeLimit+"KB!!请更换图片后再上传");
						return;
					}
				}
			}
			var name = docObj.name;
			var imgObjPreview = document.getElementById(name + "-preview");
			if (docObj.files && docObj.files[0]) {
				if (docObj.files[0].type == "image/"+imgType) {
					var localImagId = document.getElementById(name +"-iePreview");
					localImagId.style.display='none';
					//火狐下，直接设img属性
					imgObjPreview.style.display = 'block';
					imgObjPreview.style.width = '130px';
					imgObjPreview.style.height = '130px';
					//imgObjPreview.src = docObj.files[0].getAsDataURL();

					//火狐7以上版本不能用上面的getAsDataURL()方式获取，需要一下方式  
					imgObjPreview.src = window.URL.createObjectURL(docObj.files[0]);
				} else {
					alert("只支持"+imgType+"格式的图片");
					docObj.value = '';
				}
			} else {
				//IE下，使用滤镜
				docObj.select();
				var localImagId = document.getElementById(name + "-local");
				if(document.selection == null){
					imgObjPreview.src = "";
					return true;
				}
				localImagId.style.display='block';
				imgObjPreview.style.display = 'none';
				//必须设置初始大小
				localImagId.style.width = "300px";
				localImagId.style.height = "200px";
				//兼容IE9
				localImagId.focus();
				var imgSrc = document.selection.createRange().text;
				var extension = imgSrc.substr(imgSrc.lastIndexOf("."))
						.toLowerCase();
				var checkTypeName = function(extend, imgTypeNames){
					var check = false;
					var length = imgTypeNames.length;
					if(imgTypeName != null && length > 0){
						for(var i = 0; i < length; i++){
							var typeName = "." + imgTypeNames[i];
							if(extend == typeName){
								check = true;
								break;
							}
						}
					}
					return check;
				};
				if (checkTypeName(extendsion)) {
					alert("只支持"+imgType+"格式的图片");
					docObj.outerHTML += '';
					return false;
				}
				//图片异常的捕捉，防止用户修改后缀来伪造图片
				try {
					localImagId.style.filter = "progid:DXImageTransform.Microsoft.AlphaImageLoader(sizingMethod=scale)";
					localImagId.filters
							.item("DXImageTransform.Microsoft.AlphaImageLoader").src = imgSrc;
				} catch (e) {
					alert("您上传的图片格式不正确，请重新选择!");
					docObj.outerHTML += '';
					return false;
				}
				imgObjPreview.style.display = 'none';
				document.selection.empty();
			}
			return true;
		}
}
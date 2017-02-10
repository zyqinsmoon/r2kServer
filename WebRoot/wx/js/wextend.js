	/**
	 * 图书html翻页浏览
	 */
	/**JQuery扩展*/
	(function($) {
		
		$.fn.extend({
			"intopend" : function(pos, s, callback) {
				self = this;
				//判断img是否加载成功
				var isLoaded = function(){
					var loaded = true;
					self.find("img").each(function(){
						if (!this.complete){
							loaded = false;
							return false;
						}					
					});
					return loaded;
				};
				if(pos == "append"){
					this.append(s);	
				}
				else if(pos == "prepend"){
					this.prepend(s);
				}
				
				
				if (isLoaded()){
					callback();
				}else{
					//监听load事件
					this.find("img").on("load", function(){
						if (isLoaded()){
							callback();
							//卸载事件
							self.find("img").off("load");
						}
					});				
				}
			}

		});
	})(jQuery);

	
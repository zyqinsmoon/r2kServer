	(function($) {
		$.fn.extend({
			"isNull" : function() {
				return this[0] == null;
			},
			
			"append2" : function(s, sucess, error) {
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

				this.append(s);
				
				if (isLoaded()){
					sucess();
				}else{
					//监听load事件
					self.find("img").on("load", function(){
						if (isLoaded()){
							sucess();
							//卸载事件
							self.find("img").off("load");
						}
					});
					self.find("img").on("error", function(){
						error();
					});	
				}
			}
		});
	
		
	})(jQuery);
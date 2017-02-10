	/**
	 * 图书html翻页浏览
	 */
	/**JQuery扩展*/
	(function($) {
		var _height = $.fn.height;
		var _offset = $.fn.offset;
		$.fn.extend({
			"type" : function() {
				return this[0].nodeType;
			},
			"name" : function() {
				return this[0].nodeName;
			},
			//重定义height，支持textNode			
			"height" : function() {
				switch (this[0].nodeType) {
				case 3: //textNode
					v = 0;
					break;
				case 8: //comment
					v = 0;
					break;
				default:
					v = _height.apply(this, arguments);
				}
				return v;
			},

			offset : function(options) {
				var r = _offset.apply(this, arguments);
				r.bottom = r.top + this.height();
				r.right = r.left + this.width();
				r.s_bottom = r.top + this[0].scrollHeight;
				r.s_right = r.left + this[0].scrollWidth;
				return r;
			},
			
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

		/**
		 *	翻页浏览实现
		 */
		$.jPageView = {
			/**
			 *	对文本节点进行分拆
			 */
			splitTextNode : function($child, compare) {
				//文本逐字进行截断，可以采用二分查找进行优化
				var text = $child.text();
				//如果设置了$child的height，进行清除
				$child.css('height', '');
				while (compare($child) >= 0) {
					var s = $child.text();
					$child.text(s.substring(0, s.length - 1));
					if (s.length == 0 || $child.text().length == 0) {
						//删除空节点
						$child.remove();
						break;
					}
				}
				var $new = $child.clone();
				$new.text(text.substring($child.text().length, text.length))
					.wrap("<em></em>")
					.parent().css("text-indent", "0");	//被拆成新的一行，去掉首行缩进
				//$new.text(text.substring($child.text().length, text.length));
				return $new.parent();
				
			},

			/**
			 *	对文本节点进行向前分拆
			 */
			splitLastTextNode : function($child, compare) {
				//文本逐字进行截断，可以采用二分查找进行优化
				var text = $child.text();
				//如果设置了$child的height，进行清除
				$child.css('height', '');
				while (compare($child) >= 0) {
					var s = $child.text();
					$child.text(s.substring(1, s.length));
					if (s.length == 0 || $child.text().length == 0) {
						//删除空节点
						$child.remove();
						break;
					}
				}				
				var $new = $child.clone();
				$new.text(text.substring(0, text.length - $child.text().length));
				$child.wrap("<em></em>")
					.parent().css("text-indent", "0");	//被拆成新的一行，去掉首行缩进
				return $new;
			},

			wrapTextNode : function($obj) {
				$obj = $obj.wrap("<o:o></o:o>").parent();
				if ($obj.height() == 0 || $obj.width() == 0) {//大小为0，删除
					$obj.remove();
					$obj = null;
				}
				return $obj;
			},

			/**
			 * 广度优先搜索测试,找到分拆点，然后分拆
			 *	$obj:被分拆对象
			 *	compare:分拆条件回调函数，-1表示包含，0表示相交，1表示相离
			 */

			splitNode : function($obj, compare) {
				var $new = null;
				for (var node = $obj[0].firstChild; node;) {
					var $child = $(node);
					//提前赋值，否则node发生改变后，无法得到下一个。
					node = node.nextSibling;
					//为了计算方便，规格化节点，对于textNode等节点，变成element节点
					if ($child.type() != 1) {
						$child = this.wrapTextNode($child);
						if ($child == null)//大小为0，删除
							continue;
					}
					//遍历，查找分拆点，从分拆点开始分拆				
					if ($new != null) {
						//已找到分拆点,分拆兄弟节点
						$child.remove();
						$new.append($child);
					} else {
						var cmp = ($child.height()> 0 && $child.width()> 0) ?compare($child):-1;
						if (cmp >= 0) { 
							//找到分拆点，复制父节点
							$new = $child.parent().clone();
							$new.empty();
						}
						if (cmp == 0) {
							//相交关系，递归分拆		
							if ($child.children().length > 0) {
								//如果有子节点，递归分拆
								$new.append(this.splitNode($child, compare));
							} else {
								//判断类型，对于text，进行逐字删除，对于img进行缩放
								if ($child.name() == "IMG") {
									//图像进行缩放一半，否则分拆
									$child.height($child.height() / 2);
									if (compare($child) >= 0) {
										//恢复原始大小
										$child.height($child.height() * 2);

										$child.remove();
										$new.append($child);
									}
								} else {
									$new.append(this.splitTextNode($child, compare));
								}
							}
						} else if (cmp == 1) {
							//相离关系
							$child.remove();
							$new.append($child);
						}
					}
				}
				return $new;
			},
			/**
			 * 广度优先搜索测试,从后向前搜索，找到分拆点，然后分拆
			 *	$obj:被分拆对象
			 *	compare:分拆条件回调函数，-1表示包含，0表示相交，1表示相离
			 */
			splitLastNode : function($obj, compare) {
				var $new = null;
				//从后向前搜索
				for (var node = $obj[0].lastChild; node;) {
					var $child = $(node);
					//提前赋值，否则node发生改变后，无法得到下一个。
					node = node.previousSibling;
					//为了计算方便，规格化节点，对于textNode等节点，变成element节点
					if ($child.type() != 1) {
						$child = this.wrapTextNode($child);
						if ($child == null)//大小为0，删除
							continue;
					}
					//遍历，查找分拆点，从分拆点开始分拆				
					if ($new != null) {
						//已找到分拆点,分拆兄弟节点
						$child.remove();
						$new.prepend($child);
					} else {
						var cmp = compare($child);
						if (cmp >= 0) {
							//找到分拆点，复制父节点
							$new = $child.parent().clone();
							$new.empty();
						}
						if (cmp == 0) {
							//相交关系，递归分拆		
							if ($child.children().length > 0) {
								//如果有子节点，递归分拆
								$new.prepend(this.splitLastNode($child, compare));
							} else {
								//判断类型，对于text，进行逐字删除，对于img进行缩放
								if ($child.name() == "IMG") {
									//图像进行缩放一半，否则分拆
									$child.height($child.height() / 2);
									if (compare($child) >= 0) {
										//恢复原始大小
										$child.height($child.height() * 2);
										$child.remove();
										$new.prepend($child);
									}
								} else {
									$new.prepend(this.splitLastTextNode($child, compare));
								}
							}
						} else if (cmp == 1) {
							//相离关系
							$child.remove();
							$new.prepend($child);
						}
					}
				}
				return $new;
			}
		};
		
	})(jQuery);
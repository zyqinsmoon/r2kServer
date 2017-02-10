<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="s" uri="/struts-tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" scope="page"/>

<script type="text/javascript">
	$(function(){
		$.ajax({
			url:"/r2k/auth/getAuthTree.do",
			dataType:"json",
			cache:false,
			success:function(data){
				var ul = $('<ul style="overflow-x:hidden;"></ul>');
				var entitySize = data.length;
				for(var i = 0; i < entitySize; i++){
					var entity = data[i];
					var li = $('<li class="folder">'+entity.entityName+'</li>')
					var resUl = $("<ul></ul>");
					$(li).append(resUl);
					var reses = entity.authReses;
					var resSize = reses.length;
					for(var j = 0; j < resSize; j++){
						var res = reses[j];
						var resLi = $("<li></li>");
						$(resUl).append(resLi);
						var resA = $('<a target="_self" href="/r2k'+res.resUrl+'" >'+res.resName+'</a>');
						$(resLi).append(resA);
					}
					$(ul).append(li);
				}
				$("#menu").append(ul);
				$("#menu").dynatree({
					autoFocus:true,
					minExpandLevel:1,
					autoCollapse:true,
					generateIds:true,
					idPrefix: "menu-",
					fx:{ height: "toggle", duration: 150 },
					persist:true,
					cookie:{path:"/"},
					onPostInit: function(isReloading, isError) {
						this.reactivate();
					},
					onClick: function(node){
						if(node.data.href){
							window.location.href = $("#menu-"+node.data.key+" span a").attr("href");
						}
					}
				});
			}
		});
		
	});
</script>

<div class="menu" id="menu" >
</div>
<div id="changeOrgDialog" title="更换机构">
	<%@include file="remote.jsp" %>
</div>

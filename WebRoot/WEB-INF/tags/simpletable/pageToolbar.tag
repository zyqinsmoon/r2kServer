<%@ tag pageEncoding="UTF-8"%>
<%@ attribute name="page" required="true" type="cn.org.rapid_framework.page.Page" description="Page.java" %>
<%@ attribute name="pageSizeSelectList" type="java.lang.Number[]" required="false"  %>
<%@ attribute name="isShowPageSizeList" type="java.lang.Boolean" required="false"  %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" scope="page"/>
<%
	// set default values
	Integer[] defaultPageSizes = new Integer[]{10,50,100};
	if(jspContext.getAttribute("pageSizeSelectList") == null) {
		jspContext.setAttribute("pageSizeSelectList",defaultPageSizes); 
	}
	
	if(jspContext.getAttribute("isShowPageSizeList") == null) {
		jspContext.setAttribute("isShowPageSizeList",true); 
	} 
%>
<style>
	#ptoolbar {
		padding: 1px;
		font-size: 14px;
	}
	#pagebar {
		float: right;
	}
</style>
<script type="text/javascript">
	$(function(){
		$("#first").button({
			text: false,
			icons: {
				primary: "ui-icon-seek-start"
			}
		});
		$("#pre").button({
			text: false,
			icons: {
				primary: "ui-icon-seek-prev"
			}
		});
		$("#next").button({
			text: false,
			icons: {
				primary: "ui-icon-seek-next"
			}
		});
		$("#last").button({
			text: false,
			icons: {
				primary: "ui-icon-seek-end"
			}
		});
	});
</script> 
<table width="100%"  border="0" cellspacing="0" class="gridToolbar">
  <tr>
	<td>
		<div id="ptoolbar">
		
			<div  class="leftControls" >
				<jsp:doBody/>
			</div>
			
			<div id="pagebar" class="paginationControls">
				<span class="buttonLabel">${page.thisPageFirstElementNumber} - ${page.thisPageLastElementNumber} of ${page.totalCount}</span>
				
				<c:choose>
				<c:when test="${page.firstPage}"><button id="first" disabled="disabled" class="simple-page"></button></c:when>
				<c:otherwise><button id="first" onclick="javascript:simpleTable.togglePage(1);" class="simple-page"></button></c:otherwise>
				</c:choose>
				
				<c:choose>
				<c:when test="${page.hasPreviousPage}"><button id="pre" onclick="javascript:simpleTable.togglePage(${page.previousPageNumber});" class="simple-page"></button></c:when>
				<c:otherwise><button id="pre" disabled="disabled" class="simple-page"></button></c:otherwise>
				</c:choose>
				
				<c:forEach var="item" items="${page.linkPageNumbers}">
				<c:choose>
				<c:when test="${item == page.thisPageNumber}">${item}</c:when>
				<c:otherwise><a href="javascript:simpleTable.togglePage(${item});">${item}</a></c:otherwise>
				</c:choose>
				</c:forEach>
				
				<c:choose>
				<c:when test="${page.hasNextPage}"><button id="next" onclick="javascript:simpleTable.togglePage(${page.nextPageNumber});" class="simple-page"></button></c:when>
				<c:otherwise><button id="next" disabled="disabled" class="simple-page"></button></c:otherwise>
				</c:choose>
				
				<c:choose>
				<c:when test="${page.lastPage}"><button id="last" disabled="disabled" class="simple-page"></button></c:when>
				<c:otherwise><button id="last" onclick="javascript:simpleTable.togglePage(${page.lastPageNumber});" class="simple-page"></button></c:otherwise>
				</c:choose>
			</div>
		</div>
	</td>
  </tr>
</table>
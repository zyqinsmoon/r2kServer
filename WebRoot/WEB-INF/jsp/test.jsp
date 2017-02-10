<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" scope="page"/>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>Insert title here</title>
<script type="text/javascript" src="${ctx}/js/jquery-1.10.2.min.js"></script>
<script src="${ctx}/js/jquery-ui-1.10.4.custom.js"></script>
<script src="${ctx}/js/jquery.ui.core.min.js"></script>
<script src="${ctx}/js/jquery.ui.widget.min.js"></script>
<script src="${ctx}/js/jquery.ui.button.min.js"></script>
<script src="${ctx}/js/form/jquery-migrate-1.2.1.js"></script>
<script src="${ctx}/js/loading/loading-min.js"></script>
<script src="${ctx}/js/loading/jquery.bgiframe.min.js"></script>
<script src="${ctx}/js/simpletable.js"></script>
<script src="${ctx}/js/jquery.ui.spinner.min.js"></script>
<script src="${ctx}/js/jquery.mousewheel.min.js"></script>
<script src="${ctx}/js/jquery.ui.datepicker-zh-TW.js"></script>
<script src="${ctx}/js/jquery.dynatree.js"></script>
<script src="${ctx}/js/jquery.cookie.js"></script>
<script src="${ctx}/js/form/jquery.form.js"></script>
<script type="text/javascript" src="${ctx}/js/Treetable_files/jquery.treetable.js"></script>
<script type="text/javascript">
	$(function(){
		//创建simpleTable
  			var sortColumns = "<s:property value='#parameters.sortColumns'/>";
  			if(sortColumns == null || sortColumns == ""){
  				sortColumns = " SORT asc";
  			}
  			window.simpleTable = new SimpleTable('form-list',"","",sortColumns);
		//treetable插件
		$("#table-expandable").treetable({
			 expandable: true 
		});
	});
</script>
</head>
<body>
	<form id="form-list" action="/r2k/api/put/email" enctype="text/xml">
		<input type="file" />
		<input type="submit" value="submit" />
	</form>
	<table id="table-expandable">
		<thead>
			<tr><th>标题一</th></tr>
		</thead>
		<tbody>
			<tr>
				<td>内容1</td>
			</tr>
		</tbody>
	</table>
	<s:iterator value="testList" var="map" status="st">
		<s:property value="#st.index"/>
		<s:iterator value="#map" var="entry">
			<h1><s:property value="#entry.key"/></h1>
			<h1><s:property value="#entry.value"/></h1><br>
		</s:iterator>
	</s:iterator>
</body>
</html>
 <html xmlns="http://www.w3.org/1999/xhtml">
 <head>
 <script type="text/javascript" language="javascript"> 

 function PreviewImg(imgFile){ 
    
      var newPreview = document.getElementById("newPreview");     
      var imgDiv = document.createElement("div"); 
      document.body.appendChild(imgDiv); 
      imgDiv.style.width = "118px";     imgDiv.style.height = "127px"; 
      imgDiv.style.filter="progid:DXImageTransform.Microsoft.AlphaImageLoader(sizingMethod = scale)";    
      imgDiv.filters.item("DXImageTransform.Microsoft.AlphaImageLoader").src = imgFile.value; 
      newPreview.appendChild(imgDiv);     
      var showPicUrl = document.getElementById("showPicUrl"); 
      showPicUrl.innerText=imgFile.value; 
      newPreview.style.width = "80px"; 
      newPreview.style.height = "60px"; 
 } 
 
 </script> 
 <script type="text/javascript" language="javascript">

    function PreviewImg(imgFile) {
 
        var imgDiv = document.getElementById("gggg");

        imgDiv.style.filter = "progid:DXImageTransform.Microsoft.AlphaImageLoader(sizingMethod = scale)";
         imgDiv.filters("DXImageTransform.Microsoft.AlphaImageLoader").src = imgFile.value;
    } 
 </script>
 </head>
 <body>
     <form id="form1" runat="server">
     <div>
         <asp:FileUpload ID="fp" onchange="javascript:PreviewImg(this);" runat="server" />
    </div>
        <div id="gggg" style="width: 300px; height: 300px">
     </div>       
     </form>
 </body>
 </html>

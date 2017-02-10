<!doctype html>
<html>
<head>
    <meta charset="utf-8"/>
    <link rel="stylesheet" href="css/readcss.css">
    <title>阅知系统</title>
</head>
<body class="index">

<div class="header">
  <div class="version"><span>阅知1.0</span></div>
</div>

<div class="r2kwrap">
   <ul  class="download">
      <li  class="android">
      <#if androidLarge??>
         <a href="${androidLarge.path}"></a>	
      	 <div class="qrcode">
            <img src="${(androidLarge.qrcode)?default("")}" height="94" width="94"/>
            拍摄二维码下载
         </div>
         <div class="update">
            <span>版本号：</span>${androidLarge.version}
         </div>
        </#if>
      </li>
      <li  class="apple">
      	<#if iPad??>
      	 <a href="${iPad.path}" class="apple"></a>
      	 <div class="qrcode">
            <img src="${(iPad.qrcode)?default("")}" height="94" width="94"/>
            拍摄二维码下载
         </div>
         <div class="update">
            <span>版本号：</span>${iPad.version}
         </div>
         </#if>
      </li>
      <li class="ebook">
      	 <a href="">微书苑</a>
      	 <div class="qrcode">
            <img src="images/icon_code.jpg" height="94" width="94"/>
            拍摄二维码关注
         </div>
         <div class="update">
            <span>版本号：</span>1.0.0
         </div>
      </li>
   </ul>
   
   <ul  class="siteright">
      <li class="l1">
     	  <a href="${r2kUrl}">后台地址</a>
      </li>
      <li class="l2">
     	  <a href="http://59.108.34.174/temp/1%E5%B1%820%E4%BD%8D.html">楼宇导航</a>
      </li>
      <li class="l3">
     	  <a href="#"><p>关于我们</p><span>联系我们</span></a>
      </li>
   </ul>
   
   <ul  class="download">
      <li  class="bigscreen">
         <a href="${asstUrl}"></a>	
      	 <div class="qrcode">
            <img src="images/asst.png" height="94" width="94"/>
            拍摄二维码下载
         </div>
         <div class="update">
            <span>更新时间：</span>2014.11.11 
         </div>
      </li>
    </ul>
</div>

<div class="footer">
版权所有 北京方正阿帕比技术有限公司
京ICP证10038239号 
</div>

</body>
</html>
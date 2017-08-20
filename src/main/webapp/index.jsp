<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>My JSP 'index.jsp' starting page</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
  </head>
  
  <body>
    This is my JSP page. <br>
    springmvc上传文件
	<form name="form1" action="/manage/product/upload.do" method="post" enctype="multipart/form-data">
	    <input type="file" name="upload_file" />
	    <input type="submit" value="springmvc上传文件" />
	</form>


	富文本图片上传文件
	<form name="form2" action="/manage/product/richtext_img_upload.do" method="post" enctype="multipart/form-data">
	    <input type="file" name="upload_file" />
	    <input type="submit" value="富文本图片上传文件" />
	</form>
  </body>
</html>

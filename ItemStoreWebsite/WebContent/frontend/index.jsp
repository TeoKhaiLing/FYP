<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Items - Online Items Store</title>
<link rel="stylesheet" href="css/style.css" >
</head>
<body>
	<jsp:directive.include file="header.jsp" />
	
	<div class="center">		
		<div>
			<h2>New Items:</h2>
			<c:forEach items="${listNewItems}" var="item">
				<jsp:directive.include file="item_group.jsp" />
			</c:forEach>
		</div>
		<div class="next-row">
			<h2>Best-Selling Items:</h2>
			<c:forEach items="${listBestSellingItems}" var="item">
				<jsp:directive.include file="item_group.jsp" />
			</c:forEach> 			
		</div>
		<div class="next-row">
			<h2>Most-favored Items:</h2>
			<c:forEach items="${listFavoredItems}" var="item">
				<jsp:directive.include file="item_group.jsp" />
			</c:forEach>		
		</div>
		<br/><br/>
	</div>
	
	<jsp:directive.include file="footer.jsp" />
</body>
</html>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<title>Manage Items - Evergreen Itemstore Administration</title>
	<link rel="stylesheet" href="../css/style.css" >
	<script type="text/javascript" src="../js/jquery-3.5.0.min.js"></script>
	<script type="text/javascript" src="../js/jquery.validate.min.js"></script>	
</head>
<body>
	<jsp:directive.include file="header.jsp" />
	
	<div align="center">
		<h2 class="pageheading">Items Management</h1>
		<h3><a href="new_item">Create New Item</a></h3>
	</div>
	
	<c:if test="${message != null}">
	<div align="center">
		<h4 class="message">${message}</h4>
	</div>
	</c:if>
	
	<div align="center">
		<table border="1" cellpadding="5">
			<tr>
				<th>Index</th>
				<th>ID</th>
				<th>Image</th>
				<th>Name</th>
				<th>Seller</th>
				<th>Category</th>
				<th>Price</th>
				<th>Last Updated</th>
				<th>Actions</th>
			</tr>
			<c:forEach var="item" items="${listItems}" varStatus="status">
			<tr>
				<td>${status.index + 1}</td>
				<td>${item.itemId}</td>
				
				<td>
					<img src="data:image/jpg;base64,${item.base64Image}" width="84" height="110" />
				</td>
				
				<td>${item.name}</td>
				<td>${item.seller}</td>
				<td>${item.category.name}</td>
				<td>$${item.price}</td>
				<td><fmt:formatDate pattern='MM/dd/yyyy' value='${item.lastUpdateTime}' /></td>
				<td>
					<a href="edit_item?id=${item.itemId}">Edit</a> &nbsp;
					<a href="javascript:void(0);" class="deleteLink" id="${item.itemId}">Delete</a>
				</td>
			</tr>
			</c:forEach>
		</table>
	</div>
	
	
	<jsp:directive.include file="footer.jsp" />
	
	<script>
		$(document).ready(function() {
			$(".deleteLink").each(function() {
				$(this).on("click", function() {
					itemId = $(this).attr("id");
					if (confirm('Are you sure you want to delete the item with ID ' +  itemId + '?')) {
						window.location = 'delete_item?id=' + itemId;
					}					
				});
			});
		});
	</script>
</body>
</html>
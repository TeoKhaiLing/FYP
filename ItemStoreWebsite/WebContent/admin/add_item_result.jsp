<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Add Item to Order</title>
</head>
<body>
	<div align="center">
		<h2>
			The item <i>${item.name}</i> has been added to the order ID <b>${order.orderId}</b>
		</h2>
		<input type="button" value="Close" onclick="javascript: self.close();" />
	</div>
	<script>
		window.onunload = function() {
			window.opener.location.reload();
		}
	</script>
</body>
</html>
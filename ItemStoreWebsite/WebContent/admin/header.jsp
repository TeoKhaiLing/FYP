<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div align="center">
	<div>
		<a href="${pageContext.request.contextPath}/admin/">
			<img src="../images/1banner.png" />
		</a>
	</div>
	<br>
	<div>
		Welcome, <c:out value="${sessionScope.useremail}" /> | <a href="logout">Logout</a>
		<br/><br/>
	</div>
	<div id="headermenu">
		<div>
			<a href="list_users">
				<img src="../images/1user-512.png" /><br/>Users
			</a>
		</div>
		<div>
			<a href="list_category">
				<img src="../images/category.png" /><br/>Categories
			</a>
		</div>
		<div>
			<a href="list_items">
				<img src="../images/1skalierbar-eblocker-feature-icon-340x340.png" /><br/>Items
			</a>
		</div>
		<div>
			<a href="list_customer">
				<img src="../images/customer.png" /><br/>Customers
			</a>
		</div>
		<div>
			<a href="list_review">
				<img src="../images/1icon.jpg" /><br/>Reviews
			</a>
		</div>
		<div>
			<a href="list_order"><img src="../images/1cart.png" /><br/>
				Orders
			</a>
		</div>
	</div>
</div>
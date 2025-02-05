<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>	
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>${item.name}- Online Items Store</title>
<link rel="stylesheet" href="css/style.css">
<script type="text/javascript" src="js/jquery-3.5.0.min.js"></script>
</head>
<body>
	<jsp:directive.include file="header.jsp" />

	<div class="center">
		<table class="item">
			<tr>
				<td colspan="3" align="left">
					<p id="item-name">${item.name}</p>
					 by <span id="seller">${item.seller}</span>
				</td>
			</tr>
			<tr>
				<td rowspan="2">
					<img class="item-large" src="data:image/jpg;base64,${item.base64Image}" />
				</td>
				<td valign="top" align="left">
					<jsp:directive.include file="item_rating.jsp" />&nbsp;&nbsp;
					<a href="#reviews">${fn:length(item.reviews)} Reviews</a>
				</td>
				<td valign="top" rowspan="2" width="20%">
					<h2>$${item.price}</h2>
					<br/><br/>
					<button id="buttonAddToCart">Add to Cart</button>
				</td>
			</tr>
			<tr>
				<td id="description">
					${item.description}
				</td>
			</tr>
			<tr><td>&nbsp;</td></tr>
			<tr>
				<td><h2><a id="reviews">Customer Reviews</a></h2></td>
				<td colspan="2" align="center">
					<button id="buttonWriteReview">Write a Customer Review</button>
				</td>
			</tr>
			
			<tr>
				<td colspan="3" align="left">
					<table class="normal">
						<c:forEach items="${item.reviews}" var="review">
							<tr>
								<td>
									<c:forTokens items="${review.stars}" delims="," var="star">
										<c:if test="${star eq 'on'}">
											<img src="images/rating_on.png" />
										</c:if>
										<c:if test="${star eq 'off'}">
											<img src="images/rating_off.png" />
										</c:if>
									</c:forTokens>
									- <b>${review.headline}</b>
								</td>
							</tr>
							<tr>
								<td>
									by ${review.customer.fullname} on ${review.reviewTime}
								</td>
							</tr>
							<tr><td><i>${review.comment}</i></td></tr>
							<tr><td>&nbsp;</td></tr>
						</c:forEach>
					</table>
				</td>
			</tr>			
		</table>


	</div>

	<jsp:directive.include file="footer.jsp" />
	<script type="text/javascript">
		$(document).ready(function() {
			$("#buttonWriteReview").click(function() {
				window.location = 'write_review?item_id=' + ${item.itemId};
			});
			
			$("#buttonAddToCart").click(function() {
				window.location = 'add_to_cart?item_id=' + ${item.itemId};
			});				
		});
	</script>
</body>
</html>
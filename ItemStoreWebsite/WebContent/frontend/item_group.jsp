<div class="item">
	<div>
		<a href="view_item?id=${item.itemId}"> 
		<img class="item-small"
			src="data:image/jpg;base64,${item.base64Image}" />
		</a>
	</div>
	<div>
		<a href="view_item?id=${item.itemId}"> <b>${item.name}</b>
		</a>
	</div>
	<div>
		<jsp:directive.include file="item_rating.jsp" />				
	</div>
	<div>
		<i>by ${item.seller}</i>
	</div>
	<div>
		<b>$${item.price}</b>
	</div>
</div>
package com.itemstore.service;

import static com.itemstore.service.CommonUtility.forwardToPage;
import static com.itemstore.service.CommonUtility.showMessageBackend;
import static com.itemstore.service.CommonUtility.showMessageFrontend;

import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.itemstore.controller.frontend.shoppingcart.ShoppingCart;
import com.itemstore.dao.OrderDAO;
import com.itemstore.entity.Item;
import com.itemstore.entity.ItemOrder;
import com.itemstore.entity.Customer;
import com.itemstore.entity.OrderDetail;

public class OrderServices {
	private OrderDAO orderDao;
	private HttpServletRequest request;
	private HttpServletResponse response;

	public OrderServices(HttpServletRequest request, HttpServletResponse response) {
		this.request = request;
		this.response = response;
		this.orderDao = new OrderDAO();
	}

	public void listAllOrder() throws ServletException, IOException {
		listAllOrder(null);
	}
	
	public void listAllOrder(String message) throws ServletException, IOException {
		List<ItemOrder> listOrder = orderDao.listAll();				
		request.setAttribute("listOrder", listOrder);
		forwardToPage("order_list.jsp", message, request, response);
	}

	public void viewOrderDetailForAdmin() throws ServletException, IOException {
		int orderId = Integer.parseInt(request.getParameter("id"));
		
		ItemOrder order = orderDao.get(orderId);
		
		if (order != null) {
			request.setAttribute("order", order);
			forwardToPage("order_detail.jsp", request, response);
		} else {
			String message = "Could not find order with ID " + orderId;
			showMessageBackend(message, request, response);
		}
	}

	public void showCheckoutForm() throws ServletException, IOException {
		forwardToPage("frontend/checkout.jsp", request, response);
	}

	public void placeOrder() throws ServletException, IOException {
		String recipientName = request.getParameter("recipientName");
		String recipientPhone = request.getParameter("recipientPhone");
		String address = request.getParameter("address");
		String city = request.getParameter("city");
		String zipcode = request.getParameter("zipcode");
		String country = request.getParameter("country");
		String paymentMethod = request.getParameter("paymentMethod");
		String shippingAddress = address + ", " + city + ", " + zipcode + ", " + country;
		
		ItemOrder order = new ItemOrder();
		order.setRecipientName(recipientName);
		order.setRecipientPhone(recipientPhone);
		order.setShippingAddress(shippingAddress );
		order.setPaymentMethod(paymentMethod);
		
		HttpSession session = request.getSession();
		Customer customer = (Customer) session.getAttribute("loggedCustomer");
		order.setCustomer(customer);
		
		ShoppingCart shoppingCart = (ShoppingCart) session.getAttribute("cart");
		Map<Item, Integer> items = shoppingCart.getItems();
		
		Iterator<Item> iterator = items.keySet().iterator();
		
		Set<OrderDetail> orderDetails = new HashSet<>();
		
		while (iterator.hasNext()) {
			Item item = iterator.next();
			Integer quantity = items.get(item);
			float subtotal = quantity * item.getPrice();
			
			OrderDetail orderDetail = new OrderDetail();
			orderDetail.setItem(item);
			orderDetail.setItemOrder(order);
			orderDetail.setQuantity(quantity);
			orderDetail.setSubtotal(subtotal);
			
			orderDetails.add(orderDetail);
		}
		
		order.setOrderDetails(orderDetails);
		order.setTotal(shoppingCart.getTotalAmount());
		
		orderDao.create(order);
		
		shoppingCart.clear();
		
		String message = "Thank you. Your order has been received."
				+ "We will deliver your items within a few days.";
		showMessageFrontend(message, request, response);
	}

	public void listOrderByCustomer() throws ServletException, IOException {
		HttpSession session = request.getSession();
		Customer customer = (Customer) session.getAttribute("loggedCustomer");
		List<ItemOrder> listOrders = orderDao.listByCustomer(customer.getCustomerId());
		
		request.setAttribute("listOrders", listOrders);
		forwardToPage("frontend/order_list.jsp", request, response);
	}

	public void showOrderDetailForCustomer() throws ServletException, IOException {
		int orderId = Integer.parseInt(request.getParameter("id"));
		
		HttpSession session = request.getSession();
		Customer customer = (Customer) session.getAttribute("loggedCustomer");
		
		ItemOrder order = orderDao.get(orderId, customer.getCustomerId());
		request.setAttribute("order", order);
		forwardToPage("frontend/order_detail.jsp", request, response);
	}

	public void showEditOrderForm() throws ServletException, IOException {
		Integer orderId = Integer.parseInt(request.getParameter("id"));		
		ItemOrder order = orderDao.get(orderId);
		
		if (order == null) {
			String message = "Could not find order with ID " + orderId;
			showMessageBackend(message, request, response);
			return;
		}
		
		HttpSession session = request.getSession();
		Object isPendingItem = session.getAttribute("NewItemPendingToAddToOrder");
		
		if (isPendingItem == null) {			
			session.setAttribute("order", order);
		} else {
			session.removeAttribute("NewItemPendingToAddToOrder");
		}
		
		forwardToPage("order_form.jsp", request, response);
	}

	public void updateOrder() throws ServletException, IOException {
		HttpSession session = request.getSession();
		ItemOrder order = (ItemOrder) session.getAttribute("order");
		
		String recipientName = request.getParameter("recipientName");
		String recipientPhone = request.getParameter("recipientPhone");
		String shippingAddress = request.getParameter("shippingAddress");
		String paymentMethod = request.getParameter("paymentMethod");
		String orderStatus = request.getParameter("orderStatus");
		
		order.setRecipientName(recipientName);
		order.setRecipientPhone(recipientPhone);
		order.setShippingAddress(shippingAddress);
		order.setPaymentMethod(paymentMethod);
		order.setStatus(orderStatus);
		
		String[] arrayItemId = request.getParameterValues("itemId");
		String[] arrayPrice = request.getParameterValues("price");
		String[] arrayQuantity = new String[arrayItemId.length];
		
		for (int i = 1; i <= arrayQuantity.length; i++) {
			arrayQuantity[i - 1] = request.getParameter("quantity" + i);
		}
		
		Set<OrderDetail> orderDetails = order.getOrderDetails();
		orderDetails.clear();
		
		float totalAmount = 0.0f;
		
		for (int i = 0; i < arrayItemId.length; i++) {
			int itemId = Integer.parseInt(arrayItemId[i]);
			int quantity = Integer.parseInt(arrayQuantity[i]);
			float price = Float.parseFloat(arrayPrice[i]);
			
			float subtotal = price * quantity;
			
			OrderDetail orderDetail = new OrderDetail();
			orderDetail.setItem(new Item(itemId));
			orderDetail.setQuantity(quantity);
			orderDetail.setSubtotal(subtotal);
			orderDetail.setItemOrder(order);
			
			orderDetails.add(orderDetail);
			
			totalAmount += subtotal;
		}
		
		order.setTotal(totalAmount);
		
		orderDao.update(order);
		
		String message = "The order " + order.getOrderId() + " has been updated successfully";
		
		listAllOrder(message);
	}

	public void deleteOrder() throws ServletException, IOException {
		Integer orderId = Integer.parseInt(request.getParameter("id"));
		
		ItemOrder order = orderDao.get(orderId);
		
		if (order != null) {		
			orderDao.delete(orderId);
		
			String message = "The order ID " + orderId + " has been deleted.";
			listAllOrder(message);
		} else {
			String message = "Could not find order with ID " + orderId +
					", or it might have been deleted by another admin.";
			showMessageBackend(message, request, response);
		}
	}
}

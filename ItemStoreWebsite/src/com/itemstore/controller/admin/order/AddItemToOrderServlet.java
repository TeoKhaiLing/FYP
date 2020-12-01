package com.itemstore.controller.admin.order;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.itemstore.dao.ItemDAO;
import com.itemstore.entity.Item;
import com.itemstore.entity.ItemOrder;
import com.itemstore.entity.OrderDetail;

@WebServlet("/admin/add_item_to_order")
public class AddItemToOrderServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public AddItemToOrderServlet() {
		super();
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		int itemId = Integer.parseInt(request.getParameter("itemId"));
		int quantity = Integer.parseInt(request.getParameter("quantity"));
		
		ItemDAO itemDao = new ItemDAO();
		Item item = itemDao.get(itemId);
		
		HttpSession session = request.getSession();
		ItemOrder order = (ItemOrder) session.getAttribute("order");
		
		float subtotal = quantity * item.getPrice();
		
		OrderDetail orderDetail = new OrderDetail();
		orderDetail.setItem(item);
		orderDetail.setQuantity(quantity);
		orderDetail.setSubtotal(subtotal);
		
		float newTotal = order.getTotal() + subtotal;
		order.setTotal(newTotal);
		
		order.getOrderDetails().add(orderDetail);
		
		request.setAttribute("item", item);
		session.setAttribute("NewItemPendingToAddToOrder", true);
		
		String resultPage = "add_item_result.jsp";
		RequestDispatcher dispatcher = request.getRequestDispatcher(resultPage);
		dispatcher.forward(request, response);
	}

}

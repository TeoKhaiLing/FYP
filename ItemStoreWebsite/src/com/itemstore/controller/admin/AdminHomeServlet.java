package com.itemstore.controller.admin;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.itemstore.dao.ItemDAO;
import com.itemstore.dao.CustomerDAO;
import com.itemstore.dao.OrderDAO;
import com.itemstore.dao.ReviewDAO;
import com.itemstore.dao.UserDAO;
import com.itemstore.entity.ItemOrder;
import com.itemstore.entity.Review;

@WebServlet("/admin/")
public class AdminHomeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public AdminHomeServlet() {
		super();
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet(req, resp);
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		UserDAO userDao = new UserDAO();
		OrderDAO orderDao = new OrderDAO();
		ReviewDAO reviewDao = new ReviewDAO();
		ItemDAO itemDao = new ItemDAO();
		CustomerDAO customerDao = new CustomerDAO();
		
		List<ItemOrder> listMostRecentSales = orderDao.listMostRecentSales();
		List<Review> listMostRecentReviews = reviewDao.listMostRecent();
		
		long totalUsers = userDao.count();
		long totalItems = itemDao.count();
		long totalCustomers = customerDao.count();
		long totalReviews = reviewDao.count();
		long totalOrders = orderDao.count();
		
		request.setAttribute("listMostRecentSales", listMostRecentSales);
		request.setAttribute("listMostRecentReviews", listMostRecentReviews);
		
		request.setAttribute("totalUsers", totalUsers);
		request.setAttribute("totalItems", totalItems);
		request.setAttribute("totalCustomers", totalCustomers);
		request.setAttribute("totalReviews", totalReviews);
		request.setAttribute("totalOrders", totalOrders);
		
		String homepage = "index.jsp";
		RequestDispatcher dispatcher = request.getRequestDispatcher(homepage);
		dispatcher.forward(request, response);
	}

}

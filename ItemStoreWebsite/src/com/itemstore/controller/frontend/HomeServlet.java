package com.itemstore.controller.frontend;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.itemstore.dao.ItemDAO;
import com.itemstore.entity.Item;

@WebServlet("")
public class HomeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public HomeServlet() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		ItemDAO itemDAO = new ItemDAO();
		
		List<Item> listNewItems = itemDAO.listNewItems();
		List<Item> listBestSellingItems = itemDAO.listBestSellingItems();
		List<Item> listFavoredItems = itemDAO.listMostFavoredItems();
		
		request.setAttribute("listNewItems", listNewItems);
		request.setAttribute("listBestSellingItems", listBestSellingItems);
		request.setAttribute("listFavoredItems", listFavoredItems);
		
		String homepage = "frontend/index.jsp";
		RequestDispatcher dispatcher = request.getRequestDispatcher(homepage);
		dispatcher.forward(request, response);
	}

}

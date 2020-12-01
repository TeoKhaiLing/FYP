package com.itemstore.controller.admin.order;

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

@WebServlet("/admin/add_item_form")
public class ShowAddItemFormServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public ShowAddItemFormServlet() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		ItemDAO itemDao = new ItemDAO();
		List<Item> listItem = itemDao.listAll();
		request.setAttribute("listItem", listItem);
		
		String addFormPage = "add_item_form.jsp";
		RequestDispatcher dispatcher = request.getRequestDispatcher(addFormPage);
		dispatcher.forward(request, response);
		
	}

}

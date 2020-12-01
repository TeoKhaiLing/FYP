package com.itemstore.service;

import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import com.itemstore.dao.ItemDAO;
import com.itemstore.dao.CategoryDAO;
import com.itemstore.dao.OrderDAO;
import com.itemstore.entity.Item;
import com.itemstore.entity.Category;
import static com.itemstore.service.CommonUtility.*;

public class ItemServices {
	private ItemDAO itemDAO;
	private CategoryDAO categoryDAO;
	private HttpServletRequest request;
	private HttpServletResponse response;

	public ItemServices(HttpServletRequest request, HttpServletResponse response) {
		this.request = request;
		this.response = response;
		itemDAO = new ItemDAO();
		categoryDAO = new CategoryDAO();
	}

	public void listItems() throws ServletException, IOException {
		listItems(null);
	}
	
	public void listItems(String message) throws ServletException, IOException {
		List<Item> listItems = itemDAO.listAll();
		request.setAttribute("listItems", listItems);
		
		if (message != null) {
			request.setAttribute("message", message);
		}
		
		forwardToPage("item_list.jsp", message, request, response);
	}
	
	public void showItemNewForm() throws ServletException, IOException {
		List<Category> listCategory = categoryDAO.listAll();
		request.setAttribute("listCategory", listCategory);
		forwardToPage("item_form.jsp", request, response);
	}

	public void createItem() throws ServletException, IOException {
		String name = request.getParameter("name");
		
		Item existItem = itemDAO.findByName(name);
		
		if (existItem != null) {
			String message = "Could not create new item because the name '"
					+ name + "' already exists.";
			listItems(message);
			return;
		}
		
		Item newItem = new Item();
		readItemFields(newItem);
		
		Item createdItem = itemDAO.create(newItem);
		
		if (createdItem.getItemId() > 0) {
			String message = "A new item has been created successfully.";
			listItems(message);
		}
	}

	public void readItemFields(Item item) throws ServletException, IOException {
		String name = request.getParameter("name");
		String seller = request.getParameter("seller");
		String description = request.getParameter("description");
		String sku = request.getParameter("sku");
		float price = Float.parseFloat(request.getParameter("price"));
		
		DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		Date publishDate = null;
		
		try {
			publishDate = dateFormat.parse(request.getParameter("publishDate"));
		} catch (ParseException ex) {
			ex.printStackTrace();
			throw new ServletException("Error parsing publish date (format is MM/dd/yyyy)");
		}
				
		item.setName(name);
		item.setSeller(seller);
		item.setDescription(description);
		item.setSku(sku);
		item.setPublishDate(publishDate);
		
		Integer categoryId = Integer.parseInt(request.getParameter("category"));
		Category category = categoryDAO.get(categoryId);
		item.setCategory(category);
		
		item.setPrice(price);
		
		Part part = request.getPart("itemImage");
		
		if (part != null && part.getSize() > 0) {
			long size = part.getSize();
			byte[] imageBytes = new byte[(int) size];
			
			InputStream inputStream = part.getInputStream();
			inputStream.read(imageBytes);
			inputStream.close();
			
			item.setImage(imageBytes);
		}
		
	}
	
	public void editItem() throws ServletException, IOException {
		Integer itemId = Integer.parseInt(request.getParameter("id"));
		Item item = itemDAO.get(itemId);
		
		if (item != null) {
			List<Category> listCategory = categoryDAO.listAll();
			
			request.setAttribute("item", item);
			request.setAttribute("listCategory", listCategory);
			forwardToPage("item_form.jsp", request, response);
		} else {
			String message = "Could not find item with ID " + itemId;
			showMessageBackend(message, request, response);			
		}
		
	}

	public void updateItem() throws ServletException, IOException {
		Integer itemId = Integer.parseInt(request.getParameter("itemId"));
		String name = request.getParameter("name");
		
		Item existItem = itemDAO.get(itemId);
		Item itemByName = itemDAO.findByName(name);
		
		if (itemByName != null && !existItem.equals(itemByName)) {
			String message = "Could not update item because there's another item having same name.";
			listItems(message);
			return;
		}
		
		readItemFields(existItem);
		
		itemDAO.update(existItem);
		
		String message = "The item has been updated successfully.";
		listItems(message);
	}

	public void deleteItem() throws ServletException, IOException {
		Integer itemId = Integer.parseInt(request.getParameter("id"));
		Item item =  itemDAO.get(itemId);
		
		if (item == null) {
			String message = "Could not find item with ID " + itemId 
					+ ", or it might have been deleted";
			showMessageBackend(message, request, response);
			
		} else {			
			if (!item.getReviews().isEmpty()) {
				String message = "Could not delete the item with ID " + itemId
						+ " because it has reviews";
				showMessageBackend(message, request, response);
			} else {
				OrderDAO orderDAO = new OrderDAO();
				long countByOrder = orderDAO.countOrderDetailByItem(itemId);
				
				if (countByOrder > 0) {
					String message = "Could not delete item with ID " + itemId
							+ " because there are orders associated with it.";
					showMessageBackend(message, request, response);
				} else {
					String message = "The item has been deleted successfully.";
					itemDAO.delete(itemId);			
					listItems(message);
				}
			}
		}
	}

	public void listItemsByCategory() throws ServletException, IOException {
		int categoryId = Integer.parseInt(request.getParameter("id"));
		Category category = categoryDAO.get(categoryId);
		
		if (category == null) {
			String message = "Sorry, the category ID " + categoryId + " is not available.";
			showMessageFrontend(message, request, response);			
			return;
		}
		
		List<Item> listItems = itemDAO.listByCategory(categoryId);
		
		request.setAttribute("listItems", listItems);
		request.setAttribute("category", category);
		
		forwardToPage("frontend/items_list_by_category.jsp", request, response);	
	}

	public void viewItemDetail() throws ServletException, IOException {
		Integer itemId = Integer.parseInt(request.getParameter("id"));
		Item item = itemDAO.get(itemId);
		
		if (item != null) {
			request.setAttribute("item", item);
			forwardToPage("frontend/item_detail.jsp", request, response);
		} else {			
			String message = "Sorry, the item with ID " + itemId + " is not available.";
			showMessageFrontend(message, request, response);			
		}
	}

	public void search() throws ServletException, IOException {
		String keyword = request.getParameter("keyword");
		List<Item> result = null; 
				
		if (keyword.equals("")) {
			result = itemDAO.listAll();
		} else {
			result = itemDAO.search(keyword);
		}
		
		request.setAttribute("keyword", keyword);
		request.setAttribute("result", result);
		forwardToPage("frontend/search_result.jsp", request, response);
	}
}

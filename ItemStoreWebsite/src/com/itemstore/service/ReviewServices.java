package com.itemstore.service;

import static com.itemstore.service.CommonUtility.forwardToPage;
import static com.itemstore.service.CommonUtility.showMessageBackend;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.itemstore.dao.ItemDAO;
import com.itemstore.dao.ReviewDAO;
import com.itemstore.entity.Item;
import com.itemstore.entity.Customer;
import com.itemstore.entity.Review;

public class ReviewServices {
	private ReviewDAO reviewDAO;
	private HttpServletRequest request;
	private HttpServletResponse response;
	
	public ReviewServices(HttpServletRequest request, HttpServletResponse response) {
		this.request = request;
		this.response = response;
		this.reviewDAO = new ReviewDAO();
	}
	
	public void listAllReview() throws ServletException, IOException {
		listAllReview(null);
	}
	
	public void listAllReview(String message) throws ServletException, IOException {
		List<Review> listReviews = reviewDAO.listAll();
		
		request.setAttribute("listReviews", listReviews);
		forwardToPage("review_list.jsp", message, request, response);		
	}

	public void editReview() throws ServletException, IOException {
		Integer reviewId = Integer.parseInt(request.getParameter("id"));
		Review review = reviewDAO.get(reviewId);
		
		if (review != null) {		
			request.setAttribute("review", review);		
			forwardToPage("review_form.jsp", request, response);
		} else {
			String message = "Could not find review with ID " + reviewId;
			showMessageBackend(message, request, response);
		}
	}

	public void updateReview() throws ServletException, IOException {
		Integer reviewId = Integer.parseInt(request.getParameter("reviewId"));
		String headline = request.getParameter("headline");
		String comment = request.getParameter("comment");
		
		Review review = reviewDAO.get(reviewId);
		review.setHeadline(headline);
		review.setComment(comment);
		
		reviewDAO.update(review);
		
		String message = "The review has been updated successfully.";
		
		listAllReview(message);
		
	}

	public void deleteReview() throws ServletException, IOException {
		Integer reviewId = Integer.parseInt(request.getParameter("id"));
		Review review = reviewDAO.get(reviewId);
		
		if (review != null) {
			reviewDAO.delete(reviewId);
			String message = "The review has been deleted successfully.";
			listAllReview(message);
		} else {
			String message = "Could you find review with ID " + reviewId
					+ ", or it might have been deleted by another admin";
			showMessageBackend(message, request, response);
		}		
	}

	public void showReviewForm() throws ServletException, IOException {
		Integer itemId = Integer.parseInt(request.getParameter("item_id"));
		ItemDAO itemDao = new ItemDAO();
		Item item = itemDao.get(itemId);
		
		HttpSession session = request.getSession(); 
		session.setAttribute("item", item);
		
		Customer customer = (Customer) session.getAttribute("loggedCustomer");
		
		Review existReview = reviewDAO.findByCustomerAndItem(customer.getCustomerId(), itemId);
		
		String targetPage = "frontend/review_form.jsp";
		
		if (existReview != null) {
			request.setAttribute("review", existReview);
			targetPage = "frontend/review_info.jsp";
		}

		forwardToPage(targetPage, request, response);	
	}

	public void submitReview() throws ServletException, IOException {
		Integer itemId = Integer.parseInt(request.getParameter("itemId"));
		Integer rating = Integer.parseInt(request.getParameter("rating"));
		String headline = request.getParameter("headline");
		String comment = request.getParameter("comment");
		
		Review newReview = new Review();
		newReview.setHeadline(headline);
		newReview.setComment(comment);
		newReview.setRating(rating);
		
		Item item = new Item();
		item.setItemId(itemId);
		newReview.setItem(item);
		
		Customer customer = (Customer) request.getSession().getAttribute("loggedCustomer");
		newReview.setCustomer(customer);
		
		reviewDAO.create(newReview);
		
		forwardToPage("frontend/review_done.jsp", request, response);		
	}
}

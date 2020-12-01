package com.itemstore.dao;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.itemstore.entity.Item;
import com.itemstore.entity.Customer;
import com.itemstore.entity.Review;

public class ReviewDAOTest {

	private static ReviewDAO reviewDao;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		reviewDao = new ReviewDAO();
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		reviewDao.close();
	}

	@Test
	public void testCreateReview() {
		Review review = new Review();
		Item item = new Item();
		item.setItemId(3);
		
		Customer customer = new Customer();
		customer.setCustomerId(7);
		
		review.setItem(item);
		review.setCustomer(customer);
		
		review.setHeadline("Excellent item!3");
		review.setRating(4);
		review.setComment("A comprehensive item about Spring framework.3");
		
		Review savedReview = reviewDao.create(review);
		
		assertTrue(savedReview.getReviewId() > 0);
	}

	@Test
	public void testGet() {
		Integer reviewId = 1;
		Review review = reviewDao.get(reviewId);
		
		assertNotNull(review);
	}

	@Test
	public void testUpdateReview() {
		Integer reviewId = 1;
		Review review = reviewDao.get(reviewId);
		review.setHeadline("Excellent item");
		
		Review updatedReview = reviewDao.update(review);
		
		assertEquals(review.getHeadline(), updatedReview.getHeadline());
		
	}
	
	@Test
	public void testDeleteReview() {
		int reviewId = 2;
		reviewDao.delete(reviewId);
		
		Review review = reviewDao.get(reviewId);
		
		assertNull(review);
		
	}

	@Test
	public void testListAll() {
		List<Review> listReview = reviewDao.listAll();
		
		for (Review review : listReview) {
			System.out.println(review.getReviewId() + " - " + review.getItem().getName()
					+ " - " + review.getCustomer().getFullname()
					+ " - " + review.getHeadline() + " - " + review.getRating());
		}
		
		assertTrue(listReview.size() > 0);
	}

	@Test
	public void testCount() {
		long totalReviews = reviewDao.count();
		System.out.println("Total Reviews: " + totalReviews);
		assertTrue(totalReviews > 0 );
	}

	@Test
	public void testFindByCustomerAndItemNotFound() {
		Integer customerId = 100;
		Integer itemId = 99;
		
		Review result = reviewDao.findByCustomerAndItem(customerId, itemId);
		
		assertNull(result);
	}
	
	@Test
	public void testFindByCustomerAndItemFound() {
		Integer customerId = 8;
		Integer itemId = 2;
		
		Review result = reviewDao.findByCustomerAndItem(customerId, itemId);
		
		assertNotNull(result);
	}
	
	@Test
	public void testListMostRecent() {
		List<Review> recentReviews = reviewDao.listMostRecent();
		
		assertEquals(3, recentReviews.size());
	}

	@Test
	public void testCountByCustomerNotFound() {
		int customerId = 999;
		long reviewCount = reviewDao.countByCustomer(customerId);
		
		assertEquals(0, reviewCount);
	}
	
	@Test
	public void testCountByCustomerFound() {
		int customerId = 6;
		long reviewCount = reviewDao.countByCustomer(customerId);
		
		assertEquals(3, reviewCount);
	}	
}

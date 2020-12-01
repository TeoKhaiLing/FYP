package com.itemstore.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.itemstore.entity.Item;
import com.itemstore.entity.Category;

public class ItemDAOTest {
	private static ItemDAO itemDao;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		itemDao = new ItemDAO();
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		itemDao.close();
	}

	@Test
	public void testUpdateItem() throws ParseException, IOException {
		Item existItem = new Item();
		existItem.setItemId(1);
		
		Category category = new Category("Java Core");
		category.setCategoryId(1);
		existItem.setCategory(category );
		
		existItem.setName("Effective Java (3rd Edition)");
		existItem.setSeller("Joshua Bloch");
		existItem.setDescription("New coverage of generics, enums, annotations, autoboxing");
		existItem.setPrice(40f);
		existItem.setSku("0321356683");
		
		DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");		
		Date publishDate = dateFormat.parse("05/28/2008");
		existItem.setPublishDate(publishDate);
		
		String imagePath = "D:\\ItemStoreProject\\dummy-data\\items\\Effective Java.JPG";
		
		byte[] imageBytes = Files.readAllBytes(Paths.get(imagePath));
		existItem.setImage(imageBytes);
		
		Item updatedItem = itemDao.update(existItem);
		
		assertEquals(updatedItem.getName(), "Effective Java (3rd Edition)");
	}
	
	@Test
	public void testCreateItem() throws ParseException, IOException {
		Item newItem = new Item();
		
		Category category = new Category("Advanced Java");
		category.setCategoryId(2);
		newItem.setCategory(category );
		
		newItem.setName("Effective Java (2nd Edition)");
		newItem.setSeller("Joshua Bloch");
		newItem.setDescription("New coverage of generics, enums, annotations, autoboxing");
		newItem.setPrice(38.87f);
		newItem.setSku("0321356683");
		
		DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");		
		Date publishDate = dateFormat.parse("05/28/2008");
		newItem.setPublishDate(publishDate);
		
		String imagePath = "C:\\Users\\User\\eclipse-workspace\\dummy-data-items\\items\\Effective Java.JPG";
		
		byte[] imageBytes = Files.readAllBytes(Paths.get(imagePath));
		newItem.setImage(imageBytes);
		
		Item createdItem = itemDao.create(newItem);
		
		assertTrue(createdItem.getItemId() > 0);
	}

	@Test
	public void testCreate2ndItem() throws ParseException, IOException {
		Item newItem = new Item();
		
		Category category = new Category("Java Core");
		category.setCategoryId(1);
		newItem.setCategory(category);
		
		newItem.setName("Java 8 in Action");
		newItem.setSeller("Alan Mycroft");
		newItem.setDescription("Java 8 in Action is a clearly written guide to the new features of Java 8");
		newItem.setPrice(36.72f);
		newItem.setSku("1617291994");
		
		DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");		
		Date publishDate = dateFormat.parse("08/28/2014");
		newItem.setPublishDate(publishDate);
		
		String imagePath = "C:\\Users\\User\\eclipse-workspace\\dummy-data-items\\items\\Java 8 in Action.JPG";
		
		byte[] imageBytes = Files.readAllBytes(Paths.get(imagePath));
		newItem.setImage(imageBytes);
		
		Item createdItem = itemDao.create(newItem);
		
		assertTrue(createdItem.getItemId() > 0);
	}
	
	@Test(expected = EntityNotFoundException.class)
	public void testDeleteItemFail() {
		Integer itemId = 100;
		itemDao.delete(itemId);
	}
	
	@Test
	public void testGetItemFail() {
		Integer itemId = 99;
		Item item = itemDao.get(itemId);
		
		assertNull(item);
	}
	
	@Test
	public void testGetItemSuccess() {
		Integer itemId = 2;
		Item item = itemDao.get(itemId);
		
		assertNotNull(item);
	}	
	
	@Test
	public void testListAll() {
		List<Item> listItems = itemDao.listAll();
		
		for (Item aItem : listItems) {
			System.out.println(aItem.getName() + " - " + aItem.getSeller()); 
		}
		
		assertFalse(listItems.isEmpty());
	}
	
	@Test
	public void testFindByNameNotExist() {
		String name = "Thinkin in Java";
		Item item = itemDao.findByName(name);
		
		assertNull(item);
	}
	
	@Test
	public void testFindByNameExist() {
		String name = "Java 8 in Action";
		Item item = itemDao.findByName(name);
		
		System.out.println(item.getSeller());
		System.out.println(item.getPrice());
		
		assertNotNull(item);
		
	}
	
	@Test
	public void testCount() {
		long totalItems = itemDao.count();
		
		assertEquals(2, totalItems);
	}
	
	@Test
	public void testDeleteItemSuccess() {
		Integer itemId = 1;
		itemDao.delete(itemId);
		
		assertTrue(true);
	}	
	
	@Test
	public void testListNewItems() {
		List<Item> listNewItems = itemDao.listNewItems();
		for (Item aItem : listNewItems) {
			System.out.println(aItem.getName() + " - " + aItem.getPublishDate());
		}
		assertEquals(4, listNewItems.size());
	}
	
	@Test
	public void testSearchItemInName() {
		String keyword = "Java";
		List<Item> result = itemDao.search(keyword);
		
		for (Item aItem : result) {
			System.out.println(aItem.getName());
		}
		
		assertEquals(6, result.size());
	}
	
	@Test
	public void testSearchItemInSeller() {
		String keyword = "Kathy";
		List<Item> result = itemDao.search(keyword);
		
		for (Item aItem : result) {
			System.out.println(aItem.getName());
		}
		
		assertEquals(2, result.size());
	}	
	
	@Test
	public void testSearchItemInDescription() {
		String keyword = "The Big Picture";
		List<Item> result = itemDao.search(keyword);
		
		for (Item aItem : result) {
			System.out.println(aItem.getName());
		}
		
		assertEquals(1, result.size());
	}		
	
	@Test
	public void testListByCategory() {
		int categoryId = 4;
		
		List<Item> listItems = itemDao.listByCategory(categoryId);
		
		assertTrue(listItems.size() > 0);
	}
	
	@Test
	public void testCountByCategory() {
		int categoryId = 1;
		long numOfItems = itemDao.countByCategory(categoryId);
		
		assertTrue(numOfItems == 7);
	}
	
	@Test
	public void testListBestSellingItems() {
		List<Item> topBestSellingItems = itemDao.listBestSellingItems();
		
		for (Item item : topBestSellingItems) {
			System.out.println(item.getName());
		}
		
		assertEquals(4, topBestSellingItems.size());
	}
	
	@Test
	public void testListMostFavoredItems() {
		List<Item> topFavoredItems = itemDao.listMostFavoredItems();

		for (Item item : topFavoredItems) {
			System.out.println(item.getName());
		}
		
		assertEquals(4, topFavoredItems.size());
	}
}

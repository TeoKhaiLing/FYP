package com.itemstore.dao;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.itemstore.entity.ItemOrder;

public class OrderDAO extends JpaDAO<ItemOrder> implements GenericDAO<ItemOrder> {

	@Override
	public ItemOrder create(ItemOrder order) {
		order.setOrderDate(new Date());		
		order.setStatus("Processing");
		
		return super.create(order);
	}

	@Override
	public ItemOrder update(ItemOrder order) {
		return super.update(order);
	}

	@Override
	public ItemOrder get(Object orderId) {
		return super.find(ItemOrder.class, orderId);
	}

	public ItemOrder get(Integer orderId, Integer customerId) {
		Map<String, Object> parameters = new HashMap<>();
		parameters.put("orderId", orderId);
		parameters.put("customerId", customerId);
		
		List<ItemOrder> result = super.findWithNamedQuery("ItemOrder.findByIdAndCustomer", parameters );
		
		if (!result.isEmpty()) {
			return result.get(0);
		}
		return null;
	}
	
	@Override
	public void delete(Object orderId) {
		super.delete(ItemOrder.class, orderId);		
	}

	@Override
	public List<ItemOrder> listAll() {
		return super.findWithNamedQuery("ItemOrder.findAll");
	}

	@Override
	public long count() {	
		return super.countWithNamedQuery("ItemOrder.countAll");
	}
	
	public List<ItemOrder> listByCustomer(Integer customerId) {
		return super.findWithNamedQuery("ItemOrder.findByCustomer", 
				"customerId", customerId);
	}
	
	public List<ItemOrder> listMostRecentSales() {
		return super.findWithNamedQuery("ItemOrder.findAll", 0, 3);
	}

	public long countOrderDetailByItem(int itemId) {
		return super.countWithNamedQuery("OrderDetail.countByItem", "itemId", itemId);
	}
	
	public long countByCustomer(int customerId) {
		return super.countWithNamedQuery("ItemOrder.countByCustomer", "customerId", customerId);
	}
}

package com.qa.ims.persistence.domain;

import java.util.HashMap;
import java.time.LocalDate;

public class Order {
	
	private Long id;
	private Long customerId;
	private LocalDate date;
	
	private HashMap<Long, Long> items = new HashMap<Long, Long>();
	// Format of <item_id, quantity>
	
	// Constructor variations
	public Order(Long id, Long customerId, LocalDate date, HashMap<Long, Long> items) {
		this.id = id;
		this.customerId = customerId;
		this.date = date;
		this.items = items;
	}

	public Order(Long id, Long customerId, HashMap<Long, Long> items) {
		this(id, customerId, LocalDate.now(), items);
	}
	
	public Order(Long customerId, LocalDate date, HashMap<Long, Long> items) {
		this.customerId = customerId;
		this.date = date;
		this.items = items;
	}
	
	public Order(Long customerId, HashMap<Long, Long> items) {
		this(customerId, LocalDate.now(), items);
	}
	// End constructor
	
	// Start getter/setters
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public HashMap<Long, Long> getItems() {
		return items;
	}
	
	public Long addItem(Long itemId, Long quantity) {
		if (items.putIfAbsent(itemId, quantity) != null) 
			items.put(itemId, items.get(itemId) + quantity);
		return items.get(itemId);
	}
	
	public Long removeItem(Long itemId, Long quantity) {
		// if you try to remove more items than there exists in the list, it will return null
		if (items.keySet().contains(itemId)) {
			if (items.get(itemId) > quantity) {
				items.put(itemId, items.get(itemId) - quantity);
				return items.get(itemId);
			} else if (items.get(itemId) == quantity) {
				items.remove(itemId);
				return 0L;
			}
		}
		return null;
	}
	
	public Long getItemQuantity(Long itemId) {
		if (items.keySet().contains(itemId)) {
			return items.get(itemId);
		}
		return 0L;
	}
	// End getters/setters
	
	public String toString() {
		StringBuilder orderString = new StringBuilder("orderId: " + id + ", customerId: " + customerId + ", date placed: " + date);
		items.forEach((k, v) -> orderString.append("\n- item id: " + k + ", quantity: " + v));
		return orderString.toString();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Order other = (Order) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (customerId == null) {
			if (other.customerId != null)
				return false;
		} else if (!customerId.equals(other.customerId))
			return false;
		if (date == null) {
			if (other.date != null)
				return false;
		} else if (!date.equals(other.date))
			return false;
		if (items == null) {
			if (other.items != null)
				return false;
		} else if (!items.equals(other.items))
			return false;
		return true;
	}
	
}

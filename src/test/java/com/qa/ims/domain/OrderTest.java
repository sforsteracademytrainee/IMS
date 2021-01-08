package com.qa.ims.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.time.LocalDate;
import java.util.HashMap;

import org.junit.Test;

import com.qa.ims.persistence.domain.Order;

public class OrderTest {
	
	@Test
	public void testGetItemQuantity() {
		HashMap<Long, Long> items = new HashMap<Long, Long>();
		items.put(1L, 1L);
		items.put(2L, 4L);
		Order order = new Order(1L, items);
		
		assertEquals(Long.valueOf(4L), order.getItemQuantity(2L));
	}
	
	@Test
	public void testGetItemQuantityFalse() {
		HashMap<Long, Long> items = new HashMap<Long, Long>();
		Order order = new Order(1L, items);
		assertEquals(Long.valueOf(0L), order.getItemQuantity(2L));
	}
	
	@Test
	public void testSetId() {
		HashMap<Long, Long> items = new HashMap<Long, Long>();
		Order order = new Order(1L, items);
		order.setId(2L);
		assertEquals(Long.valueOf(2L), order.getId());
	}
	
	@Test
	public void testSetDate() {
		HashMap<Long, Long> items = new HashMap<Long, Long>();
		Order order = new Order(1L, items);
		order.setDate(LocalDate.of(1999, 11, 19));
		assertEquals(LocalDate.of(1999, 11, 19), order.getDate());
	}
	
	@Test
	public void testRemoveItem() {
		HashMap<Long, Long> items = new HashMap<Long, Long>();
		Order order = new Order(1L, items);
		assertNull(order.removeItem(1L, 1L));
	}
}

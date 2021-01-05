package com.qa.ims.controllers;

import static org.junit.Assert.assertEquals;

import java.util.List;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import com.qa.ims.controller.OrderController;
import com.qa.ims.persistence.dao.CustomerDAO;
import com.qa.ims.persistence.dao.ItemDAO;
import com.qa.ims.persistence.dao.OrderDAO;
import com.qa.ims.persistence.domain.Customer;
import com.qa.ims.persistence.domain.Item;
import com.qa.ims.persistence.domain.Order;
import com.qa.ims.utils.Utils;

@RunWith(MockitoJUnitRunner.class)
public class OrderControllerTest {
	
	@Mock
	private Utils utils;
	
	@Mock
	private OrderDAO orderDAO;
	
	@Mock
	private CustomerDAO customerDAO; //?
	
	@Mock
	private ItemDAO itemDAO;
	
	@InjectMocks
	private OrderController controller;
	
	@Test
	public void testCreate() {
		
		// includes exceeding item stock
		
		HashMap<Long, Long> items = new HashMap<Long, Long>();
		items.put(1L, 4L);
		items.put(2L, 3L);
		final Order created = new Order(1L, items);
		
		final Item item1 = new Item(1L, "Frying Pan", 15.99f, 1L, 46L);
		final Item item2 = new Item(2L, "Kettle", 24.99f, 1L, 57L);
		
		List<Item> itemList = new ArrayList<>();
		itemList.add(item1);
		itemList.add(item2);
		
		Mockito.when(itemDAO.readAll()).thenReturn(itemList);
		Mockito.when(utils.getLong()).thenReturn(1L, 50L, 1L, created.getItems().get(1L), 2L, created.getItems().get(2L), 0L, created.getCustomerId());
		
		Mockito.when(orderDAO.create(created)).thenReturn(created);
		
		assertEquals(created, controller.create());
		
		Mockito.verify(itemDAO, Mockito.times(1)).readAll();
		Mockito.verify(utils, Mockito.times(8)).getLong();
		Mockito.verify(orderDAO, Mockito.times(1)).create(created);
	}
	// can make a 1 or 2 create fails
	
	@Test
	public void testReadAll() {
		final Item item1 = new Item(1L, "Frying Pan", 15.99f, 1L, 46L);
		final Item item2 = new Item(2L, "Kettle", 24.99f, 1L, 57L);
		List<Item> itemList = new ArrayList<>();
		itemList.add(item1);
		itemList.add(item2);
		
		List<Customer> customers = new ArrayList<>();
		customers.add(new Customer(1L, "Jordan", "Harrison", "jharrison@qa.com"));
		
		HashMap<Long, Long> items = new HashMap<Long, Long>();
		items.put(1L, 2L);
		items.put(2L, 1L);
		final Order order = new Order(1L, 1L, LocalDate.of(2020, 12, 18), items);
		List<Order> orders = new ArrayList<>();
		orders.add(order);
		
		Mockito.when(itemDAO.readAll()).thenReturn(itemList);
		Mockito.when(customerDAO.readAll()).thenReturn(customers);
		Mockito.when(orderDAO.readAll()).thenReturn(orders);
		
		assertEquals(orders, controller.readAll());
		
		Mockito.verify(itemDAO, Mockito.times(1)).readAll();
		Mockito.verify(customerDAO, Mockito.times(1)).readAll();
		Mockito.verify(orderDAO, Mockito.times(1)).readAll();
	}
	
	@Test
	public void testPrintFullOrder() {
		final Item item1 = new Item(1L, "Frying Pan", 15.99f, 1L, 46L);
		final Item item2 = new Item(2L, "Kettle", 24.99f, 1L, 57L);
		List<Item> itemList = new ArrayList<>();
		itemList.add(item1);
		itemList.add(item2);
		
		HashMap<Long, Long> items = new HashMap<Long, Long>();
		items.put(1L, 2L);
		items.put(2L, 1L);
		final Order order = new Order(1L, 1L, LocalDate.of(2020, 12, 18), items);
		
		final String result = " / Customer: Jordan Harrison, Date: 2020-12-18, Order id: 1\n"
				+ " |   2 Frying Pan £15.99 £31.98\n"
				+ " |   1 Kettle £24.99 £24.99\n"
				+ " |   Total: £56.97";
		
		Mockito.when(itemDAO.readAll()).thenReturn(itemList);
		Mockito.when(customerDAO.read(1L)).thenReturn(new Customer(1L, "Jordan", "Harrison", "jharrison@qa.com"));
		
		assertEquals(result, controller.printFullOrder(order));
		
		Mockito.verify(itemDAO, Mockito.times(1)).readAll();
		Mockito.verify(customerDAO, Mockito.times(1)).read(1L);
	}
	
	@Test
	public void testDelete() {
		final Long ID = 1L;
		
		Mockito.when(utils.getLong()).thenReturn(ID);
		Mockito.when(orderDAO.delete(ID)).thenReturn(1);
		
		assertEquals(1, controller.delete());
		
		Mockito.verify(utils, Mockito.times(1)).getLong();
		Mockito.verify(orderDAO, Mockito.times(1)).delete(ID);
	}
	
	@Test
	public void testUpdateItems() {
		final Long ID = 1L;
		
		final Item item1 = new Item(1L, "Frying Pan", 15.99f, 1L, 46L);
		final Item item2 = new Item(2L, "Kettle", 24.99f, 1L, 57L);
		List<Item> itemList = new ArrayList<>();
		itemList.add(item1);
		itemList.add(item2);
		
		
		HashMap<Long, Long> items = new HashMap<Long, Long>();
		items.put(1L, 2L);
		items.put(2L, 1L);
		final Order order = new Order(1L, 1L, LocalDate.of(2020, 12, 18), items);
		
		HashMap<Long, Long> items2 = new HashMap<Long, Long>();
		items2.put(1L, 3L);
		items2.put(2L, 3L);
		final Order expected = new Order(1L, 1L, LocalDate.of(2020, 12, 18), items);
		
		Mockito.when(utils.getLong()).thenReturn(ID, 2L, 1L, 1L, 1L, 2L, 3L, 1L, 2L, 0L);
		Mockito.when(utils.getString()).thenReturn("items", "remove", "add", "read", "stop", "stop");
		Mockito.when(orderDAO.read(ID)).thenReturn(order);
		Mockito.when(itemDAO.readAll()).thenReturn(itemList);
		Mockito.when(orderDAO.update(expected)).thenReturn(expected);
		Mockito.when(customerDAO.read(1L)).thenReturn(new Customer(1L, "Jordan", "Harrison", "jharrison@qa.com"));
		
		assertEquals(expected, controller.update());
		
		Mockito.verify(utils, Mockito.times(10)).getLong();
		Mockito.verify(utils, Mockito.times(6)).getString();
		Mockito.verify(itemDAO, Mockito.times(7)).readAll();
		Mockito.verify(orderDAO, Mockito.times(1)).read(ID);
		Mockito.verify(orderDAO, Mockito.times(1)).update(expected);
		Mockito.verify(customerDAO, Mockito.times(5)).read(1L);
	}
	
	@Test
	public void testUpdateCustomer() {
		final Long ID = 1L;
		
		final Item item1 = new Item(1L, "Frying Pan", 15.99f, 1L, 46L);
		final Item item2 = new Item(2L, "Kettle", 24.99f, 1L, 57L);
		List<Item> itemList = new ArrayList<>();
		itemList.add(item1);
		itemList.add(item2);
		
		HashMap<Long, Long> items = new HashMap<Long, Long>();
		items.put(1L, 2L);
		items.put(2L, 1L);
		final Order order = new Order(1L, 1L, LocalDate.of(2020, 12, 18), items);
		final Order order2 = new Order(1L, 2L, LocalDate.of(2020, 12, 18), items);
		
		
		final Customer cust1 = new Customer(1L, "Jordan", "Harrison", "jharrison@qa.com");
		final Customer cust2 = new Customer(2L, "Patrick", "Star", "pstar@hotmail.co.uk");
		final List<Customer> custs = new ArrayList<>();
		custs.add(cust1);
		custs.add(cust2);
		
		
		Mockito.when(utils.getLong()).thenReturn(ID, 2L);
		Mockito.when(utils.getString()).thenReturn("customer" ,"stop");
		Mockito.when(itemDAO.readAll()).thenReturn(itemList);
		Mockito.when(orderDAO.read(ID)).thenReturn(order);
		Mockito.when(orderDAO.update(order2)).thenReturn(order2);
		Mockito.when(customerDAO.readAll()).thenReturn(custs);
		Mockito.when(customerDAO.read(ID)).thenReturn(cust1);
		
		assertEquals(order2, controller.update());
		
		Mockito.verify(utils, Mockito.times(2)).getLong();
		Mockito.verify(utils, Mockito.times(2)).getString();
		Mockito.verify(orderDAO, Mockito.times(1)).read(ID);
		Mockito.verify(orderDAO, Mockito.times(1)).update(order2);
		Mockito.verify(customerDAO, Mockito.times(1)).readAll();
	}
	
	@Test
	public void testDeleteFalse() {
		final Long ID = 2143215254L;
		
		Mockito.when(utils.getLong()).thenReturn(ID);
		Mockito.when(orderDAO.delete(ID)).thenReturn(0);
		
		assertEquals(0, controller.delete());
		
		Mockito.verify(utils, Mockito.times(1)).getLong();
		Mockito.verify(orderDAO, Mockito.times(1)).delete(ID);
	}
}

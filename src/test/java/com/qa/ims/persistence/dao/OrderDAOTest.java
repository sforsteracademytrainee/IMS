package com.qa.ims.persistence.dao;

import static org.junit.Assert.assertEquals;

import java.util.List;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.qa.ims.persistence.domain.Order;
import com.qa.ims.utils.DBUtils;

public class OrderDAOTest {
	
	private final OrderDAO orderDAO = new OrderDAO();
	
	@BeforeClass
	public static void init() {
		DBUtils.connect();
	}
	
	@Before
	public void setup() {
		DBUtils.getInstance().init("src/test/resources/sql-schema.sql", "src/test/resources/sql-data.sql");
	}
	
	@Test
	public void testCreate() {
		HashMap<Long, Long> items = new HashMap<Long, Long>();
		items.put(1L, 3L);
		items.put(2L, 2L);
		final Order created = new Order(2L, 1L, items);
		assertEquals(created, orderDAO.create(created));
	}
	
	@Test
	public void testReadAll() {
		List<Order> expected = new ArrayList<>();
		HashMap<Long, Long> items = new HashMap<Long, Long>();
		items.put(1L, 2L);
		items.put(2L, 1L);
		expected.add(new Order(1L, 1L, LocalDate.of(2020, 12, 18), items));
		assertEquals(expected, orderDAO.readAll());
	}
	
	@Test
	public void testReadLatest() {
		HashMap<Long, Long> items = new HashMap<Long, Long>();
		items.put(1L, 2L);
		items.put(2L, 1L);
		assertEquals(new Order(1L, 1L, LocalDate.of(2020, 12, 18), items), orderDAO.readLatest());
	}
	
	@Test
	public void testRead() {
		HashMap<Long, Long> items = new HashMap<Long, Long>();
		items.put(1L, 2L);
		items.put(2L, 1L);
		assertEquals(new Order(1L, 1L, LocalDate.of(2020, 12, 18), items), orderDAO.read(1L));
	}
	
	@Test
	public void testUpdate() {
		HashMap<Long, Long> items = new HashMap<Long, Long>();
		items.put(1L, 2L);
		items.put(2L, 3L);
		final Order updated = new Order(1L, 1L, LocalDate.of(2020, 12, 18), items);
		assertEquals(updated, orderDAO.update(updated));
	}
	
	@Test
	public void testDelete() {
		assertEquals(1, orderDAO.delete(1L));
	}
	
	@Test
	public void testDeleteFalse() {
		assertEquals(0, orderDAO.delete(12512412412L));
	}
	
}

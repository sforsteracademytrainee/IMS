package com.qa.ims.persistence.dao;

import static org.junit.Assert.assertEquals;

import java.util.List;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.qa.ims.Details;
import com.qa.ims.persistence.domain.Item;
import com.qa.ims.utils.DBUtils;

public class ItemDAOTest {
	
	private final ItemDAO DAO = new ItemDAO();
	
	@BeforeClass
	public static void init() {
		String[] details = Details.getDetails();
		DBUtils.connect(details[0], details[1]);
	}
	
	@Before
	public void setup() {
		DBUtils.getInstance().init("src/test/resources/sql-schema.sql", "src/test/resources/sql-data.sql");
	}
		
	@Test
	public void testCreate() {
		final Item created = new Item(3L, "Football", 14.99f, 1L, 64L);
		assertEquals(created, DAO.create(created));
	}
	
	@Test
	public void testReadAll() {
		List<Item> expected = new ArrayList<>();
		expected.add(new Item(1L, "Frying Pan", 15.99f, 1L, 46L));
		expected.add(new Item(2L, "Kettle", 24.99f, 1L, 57L));
		// add extra sql-data items if we add test data
		assertEquals(expected, DAO.readAll());
	}
	
	@Test
	public void testReadLatest() {
		assertEquals(new Item(2L, "Kettle", 24.99f, 1L, 57L), DAO.readLatest());
	}
	
	@Test
	public void testRead() {
		final long ID = 1L;
		assertEquals(new Item(1L, "Frying Pan", 15.99f, 1L, 46L), DAO.read(ID));
	}
	
	@Test
	public void testUpdate() {
		final Item updated = new Item(1L, "Used Frying Pan", 14.99f, 1L, 48L);
		assertEquals(updated, DAO.update(updated));
	}
	
	@Test
	public void testDelete() {
		assertEquals(1, DAO.delete(1L));
	}
	
}

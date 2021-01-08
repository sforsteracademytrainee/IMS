package com.qa.ims.persistence.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.List;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.qa.ims.persistence.domain.Item;
import com.qa.ims.utils.DBUtils;

public class ItemDAOTestFail {
	
	private final ItemDAO DAO = new ItemDAO();
	
	@BeforeClass
	public static void init() {
		DBUtils.connectFail();
	}
	
	@Before
	public void setup() {
		DBUtils.getInstance().init("src/test/resources/sql-schema.sql", "src/test/resources/sql-data.sql");
	}
		
	@Test
	public void testCreate() {
		final Item created = new Item(3L, "Football", 14.99f, 1L, 64L);
		assertNull(DAO.create(created));
	}
	
	@Test
	public void testReadAll() {
		assertEquals(new ArrayList<>(), DAO.readAll());
	}
	
	@Test
	public void testReadLatest() {
		assertNull(DAO.readLatest());
	}
	
	@Test
	public void testRead() {
		final long ID = 1L;
		assertNull(DAO.read(ID));
	}
	
	@Test
	public void testUpdate() {
		final Item updated = new Item(1L, "Used Frying Pan", 14.99f, 1L, 48L);
		assertNull(DAO.update(updated));
	}
	
	@Test
	public void testDelete() {
		assertEquals(0, DAO.delete(1L));
	}
	
}

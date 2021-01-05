package com.qa.ims.controllers;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import com.qa.ims.controller.ItemController;
import com.qa.ims.persistence.dao.ItemDAO;
import com.qa.ims.persistence.domain.Item;
import com.qa.ims.utils.DBUtils;
import com.qa.ims.utils.Utils;

@RunWith(MockitoJUnitRunner.class)
public class ItemControllerTest {
	
	@Mock
	private Utils utils;
	
	@Mock
	private ItemDAO dao;
	
	@InjectMocks
	private ItemController controller;
	
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
		final String NAME = "Football";
		final float VALUE = 14.99f;
		final Long CATEGORY = 1L, QUANTITY = 64L;
		final Item created = new Item(NAME, VALUE, CATEGORY, QUANTITY);
		
		Mockito.when(utils.getString()).thenReturn(NAME);
		Mockito.when(utils.getFloat()).thenReturn(VALUE);
		Mockito.when(utils.getLong()).thenReturn(CATEGORY, QUANTITY);
		Mockito.when(dao.create(created)).thenReturn(created);
		
		assertEquals(created, controller.create());
		
		Mockito.verify(utils, Mockito.times(1)).getString();
		Mockito.verify(utils, Mockito.times(1)).getFloat();
		Mockito.verify(utils, Mockito.times(2)).getLong();
		
		Mockito.verify(dao, Mockito.times(1)).create(created);
	}
	
	@Test
	public void testReadAll() {
		List<Item> items = new ArrayList<>();
		items.add(new Item(1L, "Frying Pan", 15.99f, 1L, 46L));
		
		Mockito.when(dao.readAll()).thenReturn(items);
		
		assertEquals(items, controller.readAll());
		
		Mockito.verify(dao, Mockito.times(1)).readAll();
	}
	
	@Test
	public void testUpdate() { 
		Item updated = new Item(1L, "Football", 14.99f, 1L, 64L);
		
		Mockito.when(utils.getLong()).thenReturn(updated.getId(), updated.getCategoryId(), updated.getQuantity());
		Mockito.when(utils.getString()).thenReturn(updated.getName());
		Mockito.when(utils.getFloat()).thenReturn(updated.getValue());
		Mockito.when(this.dao.update(updated)).thenReturn(updated);
		
		assertEquals(updated, controller.update());
		
		Mockito.verify(utils, Mockito.times(3)).getLong();
		Mockito.verify(utils, Mockito.times(1)).getString();
		Mockito.verify(utils, Mockito.times(1)).getFloat();
		Mockito.verify(dao, Mockito.times(1)).update(updated);
	}
	
	@Test
	public void testDelete() {
		final long ID = 1L;

		Mockito.when(utils.getLong()).thenReturn(ID);
		Mockito.when(dao.delete(ID)).thenReturn(1);

		assertEquals(1, this.controller.delete());

		Mockito.verify(utils, Mockito.times(1)).getLong();
		Mockito.verify(dao, Mockito.times(1)).delete(ID);
	}
}

package com.qa.ims;

import java.util.HashMap;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.qa.ims.persistence.dao.CustomerDAO;
import com.qa.ims.persistence.dao.ItemDAO;
import com.qa.ims.persistence.dao.OrderDAO;
import com.qa.ims.persistence.domain.Customer;
import com.qa.ims.persistence.domain.Item;
import com.qa.ims.persistence.domain.Order;
import com.qa.ims.utils.DBUtils;

public class Runner {

	public static final Logger LOGGER = LogManager.getLogger();

	public static void main(String[] args) {
		
		//test();
		
		start();
		
	}
	
	private static void start() {
		IMS ims = new IMS();
		ims.imsSystem();
		LOGGER.info("	SO LONG!");
	}
	
	private static void test() {
		DBUtils.connect();
		CustomerDAO customerDao = new CustomerDAO();
		customerDao.create(new Customer("Patrick", "Star", "pstar@hotmail.com"));
		
		ItemDAO itemDao = new ItemDAO();
		itemDao.create(new Item("Fork", 3.99f, 1L, 88L));
		
		HashMap<Long,Long> items = new HashMap<Long,Long>();
		items.put(1L, 1L);
		items.put(2L, 8L);
		
		OrderDAO orderDao = new OrderDAO();
		Order order1 = orderDao.read(1L);
		orderDao.create(new Order(2L, items));
		Order order2 = orderDao.read(2L);
		
		order1.addItem(3L, 4L);
		orderDao.update(order1);
		order2.removeItem(1L, 1L);
		orderDao.update(order2);
		
		for (Order o : orderDao.readAll()) {
			LOGGER.info(o.toString());
		}
		
	}


}

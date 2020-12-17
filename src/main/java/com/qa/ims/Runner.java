package com.qa.ims;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.qa.ims.persistence.dao.ItemDAO;
import com.qa.ims.persistence.domain.Item;
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
		LOGGER.info("SO LONG!");
	}
	
//	private static void test() {
//		DBUtils.connect();
//		Item item = new Item("Pan", 24.99f, 1, 60);
//		ItemDAO itemDao = new ItemDAO();
//		itemDao.create(item);
//		for (Item ite : itemDao.readAll()) {
//			System.out.println(ite);
//		}
//		itemDao.delete(2);
//		Item it = itemDao.read(1L);
//		it.setQuantity(it.getQuantity() + 8);
//		itemDao.update(it);
//	}


}

package com.qa.ims.controller;

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
import com.qa.ims.utils.Utils;

public class OrderController implements CrudController<Order> {
	
	public static final Logger LOGGER = LogManager.getLogger();
	
	private OrderDAO orderDAO;
	private CustomerDAO customerDAO;
	private ItemDAO itemDAO;
	private Utils utils;
	
	public OrderController(OrderDAO orderDAO, CustomerDAO customerDAO, ItemDAO itemDAO, Utils utils) {
		super();
		this.orderDAO = orderDAO;
		this.customerDAO = customerDAO;
		this.itemDAO = itemDAO;
		this.utils = utils;
	}
	
	@Override
	public List<Order> readAll() {
		List<Order> orders = orderDAO.readAll();
		HashMap<Long, Item> items = getItemMap();
		
		HashMap<Long, Customer> customers = new HashMap<Long, Customer>();
		for (Customer c: customerDAO.readAll()) {
			customers.put(c.getId(), c);
		}
		
		for (Order o: orders) {
			StringBuilder itemString = new StringBuilder();
			Customer cust = customers.get(o.getCustomerId());
			float total = 0f;
			for (Long l: o.getItems().keySet()) {
				Item item = items.get(l);
				float sum = item.getValue()*o.getItems().get(l);
				itemString.append("\n  " + o.getItems().get(l) + " " + item.getName() + " £" + item.getValue() + " £" + sum);
				total += sum;
			}
			itemString.insert(0, "Customer: " + cust.getFirstName() + " " + cust.getSurname() + ", Date: " + o.getDate() + ", Order id: " + o.getId() + "\n  Total: £" + total);
			LOGGER.info(itemString);
		}
		
		LOGGER.info("");
		return orders;
	}
	
	private void printFullOrder(Order o) {
		HashMap<Long, Item> items = getItemMap();
		
		StringBuilder itemString = new StringBuilder();
		Customer cust = customerDAO.read(o.getCustomerId());
		float total = 0f;
		for (Long l: o.getItems().keySet()) {
			Item item = items.get(l);
			float sum = item.getValue()*o.getItems().get(l);
			itemString.append("\n  " + o.getItems().get(l) + " " + item.getName() + " £" + item.getValue() + " £" + sum);
			total += sum;
		}
		itemString.insert(0, "Customer: " + cust.getFirstName() + " " + cust.getSurname() + ", Date: " + o.getDate() + ", Order id: " + o.getId() + "\n  Total: £" + total);
		LOGGER.info(itemString);
	}
	
	@Override
	public Order create() {
		HashMap<Long, Item> itemList = getItemMap();
		
		boolean finished = false;
		HashMap<Long, Long> items = new HashMap<Long, Long>();
		float total = 0f;
		do {
			LOGGER.info("Please enter the item id");
			Long id = utils.getLong(); 
			if (itemList.keySet().contains(id)) {
				Item item = itemList.get(id);
				Long stockQ = item.getQuantity();
				LOGGER.info("There are currently " + stockQ + " " + item.getName() + " in stock");
				LOGGER.info("How many would you like to order?");
				Long quantity = utils.getLong();
				if (quantity <= stockQ) {
					items.put(id, quantity);
				} else {
					LOGGER.info("Not enough " + item.getName() + "in stock to fulfill this order");
				}
			}
			LOGGER.info("Would you like to add another item? (yes/no)");
			String choice = utils.getString().toLowerCase();
			if (choice.equals("no")) {
				finished = true;
			}
		} while (!finished);
		for (Long l: items.keySet()) {
			total += items.get(l)*itemList.get(l).getValue();
		}
		LOGGER.info("Total cost of order is: £" + total);
		
		LOGGER.info("Please enter a valid customer id (enter 0 to cancel order)"); // maybe replace with email as it's more accessible?
		Long customerId = utils.getLong();
		
		if (items.isEmpty()) {
			LOGGER.info("Cannot create an empty order\n");
			return null;
		} else if (customerId == 0L) {
			LOGGER.info("Cancelled order\n");
			return null;
		} else {
			LOGGER.info("Order placed\n");
			return orderDAO.create(new Order(customerId, items));
		}
	}

	@Override
	public Order update() {
		LOGGER.info("What order id would you like to update?");
		Long id = utils.getLong();
		Order order = orderDAO.read(id);
		boolean finished = false;
		do {
			LOGGER.info("What would you like to update?");
			LOGGER.info("CUSTOMER: To edit customer id");
			LOGGER.info("ITEMS: To edit items");
			LOGGER.info("STOP: To stop editing");
			String choice = utils.getString().toLowerCase();
			switch (choice) {
			case "customer":
				order = updateCustomerId(order);
				break;
			case "items":
				order = updateItems(order);
				break;
			case "stop":
				finished = true;
				break;
			default:
				break;
			}
		} while (!finished);
		orderDAO.update(order);
		// what would you like to update? 
		return order;
	}
	
	private Order updateCustomerId(Order order) {
		LOGGER.info("The current customer id is: " + order.getCustomerId());
		LOGGER.info("What would you like to change it to?");
		Long id = utils.getLong();
		boolean exists = false;
		for (Customer cust: customerDAO.readAll()) {
			if (cust.getId() == id) {exists = true; break;}
		}
		
		if (exists) {
			order.setCustomerId(id);
			LOGGER.info("Succesfully updated customer id");
		} else LOGGER.info("Not a valid customer id");
		return order;
	}
	
	private Order updateItems(Order order) {
		boolean finished = false;
		
		do {
			int size = order.getItems().size();
			LOGGER.info("What would you like to do");
			LOGGER.info("ADD: To add another item");
			LOGGER.info("REMOVE: To remove an item");
			LOGGER.info("READ: To read order");
			LOGGER.info("STOP: To stop editing items");
			String choice = utils.getString().toLowerCase();
			switch (choice) {
			case "add":
				order = addItems(order);
				break;
			case "remove":
				order = removeItems(order);
				break;
			case "read":
				printFullOrder(order);
				break;
			case "stop":
				finished = true;
				break;
			default:
				break;
			}
		} while (!finished);
		return order;
	}
	
	private Order addItems(Order order) {
		HashMap<Long, Item> items = getItemMap();
		boolean finished = false;
		do {
			LOGGER.info("What item id would you like to add (enter 0 to stop adding)");
			Long id = utils.getLong();
			if (items.keySet().contains(id)) {
				Item item = items.get(id);
				LOGGER.info("What quantity of " + item.getName() + " would you like to add? There are " + item.getQuantity() + " in stock");
				Long quantity = utils.getLong();
				order.addItem(id, quantity);
				LOGGER.info("Successfully added " + quantity + " " + item.getName());
			} else if (id == 0L) {
				finished = true;
			} else {
				LOGGER.info("Not a valid item id");
			}
		} while (!finished);
		return order;
	}
	
	private Order removeItems(Order order) {
		HashMap<Long, Item> items = getItemMap();
		boolean finished = false;
		do {
			int size = order.getItems().size();
			if (size == 1) {
				for (Long id: order.getItems().keySet()) {
					if (order.getItems().get(id) <= 1) {
						LOGGER.info("Cannot remove any items, order would be empty (delete or add items if you want to modify this order)");
						return order;
					}
				}
			}
			LOGGER.info("What item id would you like to remove (enter 0 to stop removing)");
			LOGGER.info("Note: Cannot leave an order empty so it must contain at least 1 item at all times");
			Long id = utils.getLong();
			if (order.getItems().keySet().contains(id)) {
				if (size > 1) {
					Long orderQ = order.getItems().get(id);
					LOGGER.info("You may remove up to " + orderQ + " " + items.get(id).getName());
					Long quantity = utils.getLong();
					if (order.removeItem(id, quantity) != null) {
						LOGGER.info("Successfully removed " + quantity + " " + items.get(id).getName());
					} else {
						LOGGER.info("Tried to remove too many items");
					}
				} else if (size == 1) {
					Long orderQ = order.getItems().get(id);
					if (orderQ > 1L) {
						LOGGER.info("You may remove up to " + (orderQ - 1) + " " + items.get(id).getName());
						Long quantity = utils.getLong();
						if (quantity <= orderQ - 1) {
							order.removeItem(id, quantity);
						} else {
							LOGGER.info("Tried to remove too many items");
						}
					} else {
						LOGGER.info("Cannot remove any more items from this order");
						// Could delete the order in this scenario??
					}
					
				}
			} else if (id == 0L) {
				finished = true;
			} else {
				LOGGER.info("Not a valid id to remove");
			}
		} while (!finished);
		
		return order;
	}

	@Override
	public int delete() {
		LOGGER.info("Please enter the order id you wish to delete");
		Long id = utils.getLong();
		int value = orderDAO.delete(id);
		if (value == 0) {
			LOGGER.info("Could not delete that order id");
		}
		return value;
	}
	
	private HashMap<Long, Item> getItemMap() {
		HashMap<Long, Item> items = new HashMap<Long, Item>();
		for (Item i: itemDAO.readAll()) {
			items.put(i.getId(), i);
		}
		return items;
	}
	
}

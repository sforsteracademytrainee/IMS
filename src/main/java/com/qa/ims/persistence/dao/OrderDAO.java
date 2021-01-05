package com.qa.ims.persistence.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.qa.ims.persistence.domain.Order;
import com.qa.ims.utils.DBUtils;

public class OrderDAO implements Dao<Order> {
	
	public static final Logger LOGGER = LogManager.getLogger();

	@Override
	public Order modelFromResultSet(ResultSet resultSet) throws SQLException{
		// for orders, the require resultSet is actually a set of order_items joined
		if (resultSet.next()) {
			Long orderId = resultSet.getLong("order_id");
			Long customerId = resultSet.getLong("customer_id");
			LocalDate date = resultSet.getDate("date").toLocalDate();
			HashMap<Long, Long> items = new HashMap<Long, Long>();
			do {
				items.put(resultSet.getLong("item_id"), resultSet.getLong("quantity"));
			} while (resultSet.next());
			return new Order(orderId, customerId, date, items);
		}
		return null;
	}

	@Override
	public List<Order> readAll() {
		List<Order> orders = new ArrayList<Order>();
		try (Connection connection = DBUtils.getInstance().getConnection();
				Statement statement = connection.createStatement();
				ResultSet resultSet = statement.executeQuery("SELECT o.order_id, o.customer_id, o.date, oi.item_id, oi.quantity "
						+ "FROM orders AS o JOIN order_items AS oi ON o.order_id = oi.order_id ORDER BY o.order_id");) {
			while (resultSet.next()) {
				boolean found = false;
				for (Order o : orders) {
					if (o.getId() == resultSet.getLong("order_id")) {
						found = true;
						o.addItem(resultSet.getLong("item_id"), resultSet.getLong("quantity"));
						break;
					}
				}
				if (!found) {
					HashMap<Long, Long> items = new HashMap<Long, Long>();
					items.put(resultSet.getLong("item_id"), resultSet.getLong("quantity"));
					orders.add(new Order(resultSet.getLong("order_id"), resultSet.getLong("customer_id"), resultSet.getDate("date").toLocalDate(), items));
				}
			}
			return orders;
		} catch (Exception e) {
			LOGGER.debug(e);
			LOGGER.error(e.getMessage());
		}
		return new ArrayList<>();
	}

	@Override
	public Order readLatest() {
		// TODO better, make it in 1 query
		try (Connection connection = DBUtils.getInstance().getConnection();
				Statement statement = connection.createStatement();
				ResultSet resultSet = statement.executeQuery("SELECT order_id FROM orders ORDER BY order_id DESC LIMIT 1");) {
			resultSet.next();
			Long orderId = resultSet.getLong("order_id");	
			return read(orderId);
		} catch (Exception e) {
			LOGGER.debug(e);
			LOGGER.error(e.getMessage());
		}
		return null;
	}

	@Override
	public Order read(Long id) {
		try (Connection connection = DBUtils.getInstance().getConnection();
				Statement statement = connection.createStatement();
				ResultSet resultSet = statement.executeQuery("SELECT o.order_id, o.customer_id, o.date, oi.item_id, oi.quantity "
						+ "FROM orders AS o JOIN order_items AS oi ON o.order_id = oi.order_id "
						+ "WHERE o.order_id = " + id);) {
			
			return modelFromResultSet(resultSet);
		} catch (Exception e) {
			LOGGER.debug(e);
			LOGGER.error(e.getMessage());
		}
		return null;
	}

	@Override
	public Order create(Order order) {
		try (Connection connection = DBUtils.getInstance().getConnection();
				Statement statement = connection.createStatement();) {
			statement.executeUpdate("INSERT INTO orders(customer_id, date) values(" 
					+ order.getCustomerId() + ",'" + order.getDate() + "')");
			
			// get newly created order entry id
			ResultSet rs = statement.executeQuery("SELECT order_id FROM orders ORDER BY order_id DESC LIMIT 1");
			rs.next();
			Long id = rs.getLong("order_id");
			
			StringBuilder itemsSql = new StringBuilder("INSERT INTO order_items(order_id, item_id, quantity) VALUES ");
			boolean isFirst = true;
			for (Long k: order.getItems().keySet()) {
				if (isFirst) isFirst = false;
				else itemsSql.append(",");
				itemsSql.append("(" + id + ", "+ k + ", " + order.getItemQuantity(k) + ")");
			}
			itemsSql.append(";");
			statement.executeUpdate(itemsSql.toString());
			return readLatest();
		} catch (Exception e) {
			LOGGER.debug(e);
			LOGGER.error(e.getMessage());
		}
		return null;
	}

	@Override
	public Order update(Order order) {
		try (Connection connection = DBUtils.getInstance().getConnection();
				Statement statement = connection.createStatement();) {
			statement.executeUpdate("UPDATE orders SET customer_id=" + order.getCustomerId() + 
					", date='" + order.getDate() +"' WHERE order_id=" + order.getId());
			statement.executeUpdate("DELETE FROM order_items WHERE order_id=" + order.getId());
			
			// Insert new order line values from scratch
			StringBuilder itemsSql = new StringBuilder("INSERT INTO order_items(order_id, item_id, quantity) VALUES ");
			boolean isFirst = true;
			for (Long k: order.getItems().keySet()) {
				if (isFirst) isFirst = false;
				else itemsSql.append(",");
				itemsSql.append("(" + order.getId() + ", "+ k + ", " + order.getItemQuantity(k) + ")");
			}
			itemsSql.append(";");
			statement.executeUpdate(itemsSql.toString());
			
			return read(order.getId());
		} catch (Exception e) {
			LOGGER.debug(e);
			LOGGER.error(e.getMessage());
		}
		return null;
	}

	@Override
	public int delete(long id) {
		try (Connection connection = DBUtils.getInstance().getConnection();
				Statement statement = connection.createStatement();) {
			return statement.executeUpdate("DELETE FROM orders WHERE order_id=" + id);
		} catch (Exception e) {
			LOGGER.debug(e);
			LOGGER.error(e.getMessage());
		}
		return 0;
	}
	

}

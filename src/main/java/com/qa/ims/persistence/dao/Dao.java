package com.qa.ims.persistence.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public interface Dao<T> {

	T modelFromResultSet(ResultSet resultSet) throws SQLException;
	
	List<T> readAll();
	
	T readLatest(); 
	
	T read(Long id);
	
	T create(T t);

	T update(T t);

	int delete(long id);

}

package com.qa.ims.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;
import java.util.stream.Stream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DBUtils {

	private static final Logger LOGGER = LogManager.getLogger();

	private final String DB_USER;

	private final String DB_PASS;

	private final String DB_URL;

	private DBUtils(String address, String username, String password) {
		this.DB_USER = username;
		this.DB_PASS = password;
		this.DB_URL = "jdbc:mysql://" + address + ":3306/ims";
		init();
	}

	public int init() {
		return this.init("src/main/resources/sql-schema.sql", "src/main/resources/sql-data.sql");
	}

	public int init(String... paths) {
		int modified = 0;

		for (String path : paths) {
			modified += executeSQLFile(path);
		}

		return modified;
	}

	public int executeSQLFile(String file) {
		int modified = 0;
		try (Connection connection = this.getConnection();
				BufferedReader br = new BufferedReader(new FileReader(file));) {
			String fileAsString = br.lines().reduce((acc, next) -> acc + next).orElse("");
			String[] queries = fileAsString.split(";");
			modified += Stream.of(queries).map(string -> {
				try (Statement statement = connection.createStatement();) {
					return statement.executeUpdate(string);
				} catch (Exception e) {
					LOGGER.debug(e);
					return 0;
				}
			}).reduce((acc, next) -> acc + next).orElse(0);
		} catch (Exception e) {
			LOGGER.debug(e);
		}
		return modified;
	}

	public Connection getConnection() throws SQLException {
		return DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
	}

	public static DBUtils instance;

	public static DBUtils connect() {
		String[] details = new String[3];
		try {
			File myObj = new File("src\\main\\resources\\details");
			Scanner myReader = new Scanner(myObj);
			details[0] = myReader.nextLine();
			details[1] = myReader.nextLine();
			details[2] = myReader.nextLine();
			myReader.close();
		} catch (FileNotFoundException e) {
			LOGGER.debug(e);
		}
		instance = new DBUtils(details[0], details[1], details[2]);
		return instance;
	}

	public static DBUtils getInstance() {
		if (instance == null) {
			instance = new DBUtils("", "", "");
		}
		return instance;
	}

}

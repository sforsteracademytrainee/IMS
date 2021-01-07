package com.qa.ims;

public class Details {
	
	private static final String DB_USER = "youruser";
	
	private static final String DB_PASS = "yourpass";
	
	public static String[] getDetails() {
		String[] details = {DB_USER, DB_PASS};
		return details;
	}
	
}

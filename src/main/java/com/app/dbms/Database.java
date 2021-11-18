package com.app.dbms;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Map;
import java.util.Properties;

public class Database {
	private static Database instance;

	private Database() {
		try {
			Class.forName("org.postgresql.Driver");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static synchronized Database getInstance() {
		return instance == null ? new Database() : instance;
	}

	public Connection getConnection() {
		try {
			String url = getProperties().get("url").toString();
			String username = getProperties().get("username").toString();
			String password = getProperties().get("password").toString();

			Connection c = DriverManager.getConnection(url, username, password);
			return c;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public Map<?, ?> getProperties() {
		try {
			Properties p = new Properties();
			InputStream is = getClass().getClassLoader().getResourceAsStream("jdbc.properties");
			p.load(is);
			return p;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}

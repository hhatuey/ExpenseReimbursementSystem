package com.app.dbms;

import java.sql.Connection;
import java.sql.DriverManager;
import com.app.config.EnvironmentConfig;

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
			String url = EnvironmentConfig.getDbUrl();
			String username = EnvironmentConfig.getDbUsername();
			String password = EnvironmentConfig.getDbPassword();

			Connection c = DriverManager.getConnection(url, username, password);
			return c;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}


}

package com.app.models;

import java.beans.Transient;
import java.io.Serializable;

public class User implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static enum RoleType{
		EMPLOYEE,
		MANAGER
	}
	
	private int id;
	private String username;
	private String password;
	private String first_name;
	private String last_name;
	private String email;
	private int role_id;
	private User.RoleType role_type;
	
	public User() {
		
	}
	
	public User(int id, String username, String password, String first_name, String last_name, String email,
			int role_id) {
		super();
		this.id = id;
		this.username = username;
		this.password = password;
		this.first_name = first_name;
		this.last_name = last_name;
		this.email = email;
		this.role_id = role_id;
	}

	public int getID() {
		return id;
	}

	public void setID(int id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	@Transient
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFirstName() {
		return first_name;
	}

	public void setFirstName(String first_name) {
		this.first_name = first_name;
	}

	public String getLastName() {
		return last_name;
	}

	public void setLastName(String last_name) {
		this.last_name = last_name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getRoleID() {
		return role_id;
	}

	public void setRoleID(int role_id) {
		this.role_id = role_id;
	}
	
	public User.RoleType getRoleType(){
		return User.RoleType.values()[this.role_id -1];
	}
	
	public void setRoleType(User.RoleType role_type) {
		this.role_id = role_type.ordinal() +1;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("User [id=");
		builder.append(id);
		builder.append(", username=");
		builder.append(username);
		builder.append(", password=");
		builder.append(password);
		builder.append(", first_name=");
		builder.append(first_name);
		builder.append(", last_name=");
		builder.append(last_name);
		builder.append(", email=");
		builder.append(email);
		builder.append(", role_id=");
		builder.append(role_id);
		builder.append("]");
		return builder.toString();
	}
}

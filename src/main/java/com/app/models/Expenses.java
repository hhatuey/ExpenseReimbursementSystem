package com.app.models;

import java.sql.Date;

public class Expenses {
	private int id;
	private double amount;
	private Date submitted;
	private Date resolved;
	private String description;
	private int receipt;
	private String role1;
	private String author;
	private String role2;
	private String resolver;
	private String status;
	private String type;

	public Expenses() {
		
	}
	
	public Expenses(int id, double amount, Date submitted, Date resolved, String description, int receipt, String role1,
			String author, String role2, String resolver, String status, String type) {
		super();
		this.id = id;
		this.amount = amount;
		this.submitted = submitted;
		this.resolved = resolved;
		this.description = description;
		this.receipt = receipt;
		this.role1 = role1;
		this.author = author;
		this.role2 = role2;
		this.resolver = resolver;
		this.status = status;
		this.type = type;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public Date getSubmitted() {
		return submitted;
	}

	public void setSubmitted(Date submitted) {
		this.submitted = submitted;
	}

	public Date getResolved() {
		return resolved;
	}

	public void setResolved(Date resolved) {
		this.resolved = resolved;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getReceipt() {
		return receipt;
	}

	public void setReceipt(int receipt) {
		this.receipt = receipt;
	}

	public String getRole1() {
		return role1;
	}

	public void setRole1(String role1) {
		this.role1 = role1;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getRole2() {
		return role2;
	}

	public void setRole2(String role2) {
		this.role2 = role2;
	}

	public String getResolver() {
		return resolver;
	}

	public void setResolver(String resolver) {
		this.resolver = resolver;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Expenses [id=");
		builder.append(id);
		builder.append(", amount=");
		builder.append(amount);
		builder.append(", submitted=");
		builder.append(submitted);
		builder.append(", resolved=");
		builder.append(resolved);
		builder.append(", description=");
		builder.append(description);
		builder.append(", receipt=");
		builder.append(receipt);
		builder.append(", role1=");
		builder.append(role1);
		builder.append(", author=");
		builder.append(author);
		builder.append(", role2=");
		builder.append(role2);
		builder.append(", resolver=");
		builder.append(resolver);
		builder.append(", status=");
		builder.append(status);
		builder.append(", type=");
		builder.append(type);
		builder.append("]");
		return builder.toString();
	}
	
	
}

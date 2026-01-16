package com.app.models;

import java.io.Serializable;
import java.sql.Date;
import java.util.Base64;

public class Reimbursement implements Serializable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static class Type {
		int id;
		String name;
		
		public Type() {}

		public Type(int id, String name) {
			super();
			this.id = id;
			this.name = name;
		}

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		@Override
		public String toString() {
			StringBuilder builder = new StringBuilder();
			builder.append("Type [id=");
			builder.append(id);
			builder.append(", name=");
			builder.append(name);
			builder.append("]");
			return builder.toString();
		}
		

	}
	
	public static class Status {
		int id;
		String name;
		
		public Status() {}

		public Status(int id, String name) {
			super();
			this.id = id;
			this.name = name;
		}

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		@Override
		public String toString() {
			StringBuilder builder = new StringBuilder();
			builder.append("Status [id=");
			builder.append(id);
			builder.append(", name=");
			builder.append(name);
			builder.append("]");
			return builder.toString();
		}
		
		
	}
	
//	public static enum Status {
//		PENDING, APPROVED, DENIED
//	}
//
//	public static enum Type {
//		LODGING, TRAVEL, FOOD, OTHER
//	}

	private int id;
	private double amount;
	private Date submitted;
	private Date resolved;
	private String description;
	private int receipt;
	private int author_id;
	private int resolver_id;
	private int status_id;
	private int type_id;
	private String sign;

	public Reimbursement() {
	}

	public Reimbursement(int id, double amount, Date submitted, Date resolved, String description, int receipt,
			int author_id, int resolver_id, int status_id, int type_id, String sign) {
		super();
		this.id = id;
		this.amount = amount;
		this.submitted = submitted;
		this.resolved = resolved;
		this.description = description;
		this.receipt = receipt;
		this.author_id = author_id;
		this.resolver_id = resolver_id;
		this.status_id = status_id;
		this.type_id = type_id;
		this.sign = sign;
	}

	public int getID() {
		return id;
	}

	public void setID(int id) {
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

	public int getAuthorID() {
		return author_id;
	}

	public void setAuthorID(int author_id) {
		this.author_id = author_id;
	}

	public int getResolverID() {
		return resolver_id;
	}

	public void setResolverID(int resolver_id) {
		this.resolver_id = resolver_id;
	}

	public int getStatusID() {
		return status_id;
	}

	public void setStatusID(int status_id) {
		this.status_id = status_id;
	}

	public int getTypeID() {
		return type_id;
	}

	public void setTypeID(int type_id) {
		this.type_id = type_id;
	}

	public String getSign() {
		String s = this.author_id + this.amount + this.description;
		byte[] enc = Base64.getEncoder().encode(s.getBytes());
		this.sign = new String(enc);
		return this.sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Reimbursement [id=");
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
		builder.append(", author_id=");
		builder.append(author_id);
		builder.append(", resolver_id=");
		builder.append(resolver_id);
		builder.append(", status_id=");
		builder.append(status_id);
		builder.append(", type_id=");
		builder.append(type_id);
		builder.append(", sign=");
		builder.append(sign);
		builder.append("]");
		return builder.toString();
	}

}

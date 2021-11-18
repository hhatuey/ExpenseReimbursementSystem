package com.app.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.app.dbms.Database;
import com.app.models.Expenses;

public class ExpensesDao {

	Database db = Database.getInstance();
	String view = "reimb_view";
	
	public List<Expenses> getAll() {
		List<Expenses> list = new ArrayList<>();
		String sql = new StringBuilder("select * from ").append(view).toString();
		try (PreparedStatement ps = db.getConnection().prepareStatement("call create_view()")) {
			Statement st = ps.getConnection().createStatement();	
			ResultSet rs = st.executeQuery(sql);
			Expenses e = null;
			while (rs.next()) {
				e = new Expenses();
				e.setId(rs.getInt(1));
				e.setAmount(rs.getDouble(2));
				e.setSubmitted(rs.getDate(3));
				e.setResolved(rs.getDate(4));
				e.setDescription(rs.getString(5));
				e.setReceipt(rs.getInt(6));
				e.setRole1(rs.getString(7));
				e.setAuthor(rs.getString(8));
				e.setRole2(rs.getString(9));
				e.setResolver(rs.getString(10));
				e.setStatus(rs.getString(11));
				e.setType(rs.getString(12));
				list.add(e);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list.isEmpty()? null : list;
	}

	public List<Expenses> getAllWhere(String condition) {
		List<Expenses> list = new ArrayList<>();
		String sql = new StringBuilder("select * from ").append(view).append(" where ").append(condition).toString();
		try (PreparedStatement ps = db.getConnection().prepareStatement("call create_view()")) {
			Statement st = ps.getConnection().createStatement();
			ResultSet rs = st.executeQuery(sql);
			Expenses e = null;
			while (rs.next()) {
				e = new Expenses();
				e.setId(rs.getInt(1));
				e.setAmount(rs.getDouble(2));
				e.setSubmitted(rs.getDate(3));
				e.setResolved(rs.getDate(4));
				e.setDescription(rs.getString(5));
				e.setReceipt(rs.getInt(6));
				e.setRole1(rs.getString(7));
				e.setAuthor(rs.getString(8));
				e.setRole2(rs.getString(9));
				e.setResolver(rs.getString(10));
				e.setStatus(rs.getString(11));
				e.setType(rs.getString(12));
				list.add(e);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list.isEmpty()? null : list;
	}

}

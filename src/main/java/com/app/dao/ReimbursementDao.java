package com.app.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.app.dbms.Database;
import com.app.models.Reimbursement;

public class ReimbursementDao implements Dao<Reimbursement> {

	private String table = "reimbursement";
	private Database db = Database.getInstance();

	public String getColumns() {
		List<String> list = new ArrayList<>();
		try (Connection c = db.getConnection();) {
			ResultSet rs = c.getMetaData().getColumns(null, null, table, null);
			while (rs.next()) {
				list.add(rs.getString("COLUMN_NAME"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		list.remove(0);
		return list.toString().replace('[', '(').replace(']', ')');
	}

	@Override
	public List<Reimbursement> getAll() {
		List<Reimbursement> list = new ArrayList<>();
		String sql = new StringBuilder("select * from ").append(table).toString();
		try (PreparedStatement ps = db.getConnection().prepareStatement(sql)) {
			ResultSet rs = ps.executeQuery();
			Reimbursement r = null;
			while (rs.next()) {
				r = new Reimbursement();
				r.setID(rs.getInt(1));
				r.setAmount(rs.getDouble(2));
				r.setSubmitted(rs.getDate(3));
				r.setResolved(rs.getDate(4));
				r.setDescription(rs.getString(5));
				r.setReceipt(rs.getInt(6));
				r.setAuthorID(rs.getInt(7));
				r.setResolverID(rs.getInt(8));
				r.setStatusID(rs.getInt(9));
				r.setTypeID(rs.getInt(10));
//				r.setSign(rs.getString(11));
				list.add(r);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	public List<Reimbursement> getAllByAuthorID(int id) {
		List<Reimbursement> list = new ArrayList<>();
		String sql = new StringBuilder("select * from ").append(table).append(" where reimb_author =?").toString();
		try (PreparedStatement ps = db.getConnection().prepareStatement(sql)) {
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			Reimbursement r = null;
			while (rs.next()) {
				r = new Reimbursement();
				r.setID(rs.getInt(1));
				r.setAmount(rs.getDouble(2));
				r.setSubmitted(rs.getDate(3));
				r.setResolved(rs.getDate(4));
				r.setDescription(rs.getString(5));
				r.setReceipt(rs.getInt(6));
				r.setAuthorID(rs.getInt(7));
				r.setResolverID(rs.getInt(8));
				r.setStatusID(rs.getInt(9));
				r.setTypeID(rs.getInt(10));
//				r.setSign(rs.getString(11));
				list.add(r);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list.isEmpty()? null : list;
	}
	
	public List<Reimbursement.Type> getTypes(){
		List<Reimbursement.Type> list = new ArrayList<>();
		String sql = "select * from reimbursement_type";
		try (PreparedStatement ps = db.getConnection().prepareStatement(sql);){
			ResultSet rs = ps.executeQuery();
			Reimbursement.Type rt = null;
			while(rs.next()) {
				rt = new Reimbursement.Type();
				rt.setId(rs.getInt(1));
				rt.setName(rs.getString(2));
				list.add(rt);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list.isEmpty()? null: list;
	}
	
	public List<Reimbursement.Status> getStatus(){
		List<Reimbursement.Status> list = new ArrayList<>();
		String sql = "select * from reimbursement_status";
		try(PreparedStatement ps = db.getConnection().prepareStatement(sql);){
			ResultSet rs = ps.executeQuery();
			Reimbursement.Status ra = null;
			while(rs.next()) {
				ra = new Reimbursement.Status();
				ra.setId(rs.getInt(1));
				ra.setName(rs.getString(2));
				list.add(ra);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list.isEmpty()? null : list;
	}

	@Override
	public Reimbursement getByID(int id) {
		Reimbursement r = null;
		String sql = new StringBuilder("select * from ").append(table).append(" where reimb_id =").append(id)
				.toString();
		try (PreparedStatement ps = db.getConnection().prepareStatement(sql)) {
			ResultSet rs = ps.executeQuery();
			r = new Reimbursement();
			if (rs.next()) {
				r.setID(rs.getInt(1));
				r.setAmount(rs.getDouble(2));
				r.setSubmitted(rs.getDate(3));
				r.setResolved(rs.getDate(4));
				r.setDescription(rs.getString(5));
				r.setReceipt(rs.getInt(6));
				r.setAuthorID(rs.getInt(7));
				r.setResolverID(rs.getInt(8));
				r.setStatusID(rs.getInt(9));
				r.setTypeID(rs.getInt(10));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return r;
	}

	public Reimbursement getBySign(String sign) {
		Reimbursement r = null;
		String sql = new StringBuilder("select * from ").append(table).append(" where reimb_sign =?").toString();
		try (PreparedStatement ps = db.getConnection().prepareStatement(sql);) {
			ps.setString(1, sign);
			ResultSet rs = ps.executeQuery();
			r = new Reimbursement();
			if (rs.next()) {
				r.setID(rs.getInt(1));
				r.setAmount(rs.getDouble(2));
				r.setSubmitted(rs.getDate(3));
				r.setResolved(rs.getDate(4));
				r.setDescription(rs.getString(5));
				r.setReceipt(rs.getInt(6));
				r.setAuthorID(rs.getInt(7));
				r.setResolverID(rs.getInt(8));
				r.setStatusID(rs.getInt(9));
				r.setTypeID(rs.getInt(10));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return r;
	}

	@Override
	public int create(Reimbursement r) {
		int id = 0;
		String sql = new StringBuilder("insert into ").append(table).append(getColumns())
				.append(" values(?,?,?,?,?,?,nullif(?,0),?,?,?)").append(" returning reimb_id").toString();
		try (Connection c = db.getConnection();) {
			c.setAutoCommit(false);
			try (PreparedStatement ps = c.prepareStatement(sql);) {
				ps.setDouble(1, r.getAmount());
				ps.setDate(2, r.getSubmitted());
				ps.setDate(3, r.getResolved());
				ps.setString(4, r.getDescription());
				ps.setInt(5, r.getReceipt());
				ps.setInt(6, r.getAuthorID());
				ps.setInt(7, r.getResolverID());//nullif value = 0
				ps.setInt(8, r.getStatusID());
				ps.setInt(9, r.getTypeID());
				ps.setString(10, r.getSign());
				ResultSet rs = ps.executeQuery();
				c.commit();
				if (rs.next())
					id = rs.getInt(1);
			} catch (SQLException e) {
				c.rollback();
				e.printStackTrace();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return id;
	}

	@Override
	public boolean update(Reimbursement r) {
		boolean out = false;
		String sql = new StringBuilder("update ").append(table).append(" set").append(getColumns())
				.append(" =(?,?,?,?,?,?,?,?,?,?)").append(" where reimb_id =?").toString();
		try (Connection c = db.getConnection();) {
			c.setAutoCommit(false);
			try (PreparedStatement ps = c.prepareStatement(sql);) {
				ps.setDouble(1, r.getAmount());
				ps.setDate(2, r.getSubmitted());
				ps.setDate(3, r.getResolved());
				ps.setString(4, r.getDescription());
				ps.setInt(5, r.getReceipt());
				ps.setInt(6, r.getAuthorID());
				ps.setInt(7, r.getResolverID());
				ps.setInt(8, r.getStatusID());
				ps.setInt(9, r.getTypeID());
				ps.setString(10, r.getSign());
				ps.setInt(11, r.getID());
				out = ps.executeUpdate() != 0;
				c.commit();
			} catch (SQLException e) {
				c.rollback();
				e.printStackTrace();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return out;
	}

	@Override
	public boolean delete(Reimbursement r) {
		boolean out = false;
		String sql = new StringBuilder("delete from ").append(table).append(" where reimb_id =?").toString();
		try (Connection c = db.getConnection();) {
			c.setAutoCommit(false);
			try (PreparedStatement ps = c.prepareStatement(sql);) {
				ps.setInt(1, r.getID());
				out = ps.executeUpdate() != 0;
				c.commit();
			} catch (SQLException e) {
				c.rollback();
				e.printStackTrace();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return out;
	}

}

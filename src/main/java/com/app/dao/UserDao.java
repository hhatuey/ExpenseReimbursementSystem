package com.app.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.app.dbms.Database;
import com.app.models.User;

public class UserDao implements Dao<User> {
	private static final String table = "ers_user";
	private static Database db = Database.getInstance();

	private String getColumns() {
		List<String> list = new ArrayList<>();
		try (Connection c = db.getConnection();) {
			ResultSet rs = c.getMetaData().getColumns(null, null, table, null);
			while (rs.next()) {
				list.add(rs.getString("COLUMN_NAME"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		list.remove(0); // remove the id
		return list.toString().replace('[', '(').replace(']', ')');
	}

	@Override
	public List<User> getAll() {
		User u = null;
		List<User> list = new ArrayList<>();
		String sql = new StringBuilder("select * from ").append(table).toString();
		try (ResultSet rs = db.getConnection().createStatement().executeQuery(sql)) {
			while (rs.next()) {
				u = new User();
				u.setID(rs.getInt(1));
				u.setUsername(rs.getString(2));
				u.setPassword(rs.getString(3));
				u.setFirstName(rs.getString(4));
				u.setLastName(rs.getString(5));
				u.setEmail(rs.getString(6));
				u.setRoleID(rs.getInt(7));
				list.add(u);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public User getByID(int id) {
		User u = null;
		String sql = new StringBuilder("select * from ").append(table).append(" where ers_user_id =?").toString();
		try (PreparedStatement ps = db.getConnection().prepareStatement(sql);) {
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				u = new User();
				u.setID(rs.getInt(1));
				u.setUsername(rs.getString(2));
				u.setPassword(rs.getString(3));
				u.setFirstName(rs.getString(4));
				u.setLastName(rs.getString(5));
				u.setEmail(rs.getString(6));
				u.setRoleID(rs.getInt(7));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return u;
	}

	public User getByUsername(String username) {
		User u = null;
		String sql = new StringBuilder("select * from ").append(table).append(" where ers_username =?").toString();

		try (Connection c = db.getConnection();) {
			try (PreparedStatement ps = c.prepareStatement(sql);) {
				ps.setString(1, username);
				ResultSet rs = ps.executeQuery();
				if (rs.next()) {
					u = new User();
					u.setID(rs.getInt(1));
					u.setUsername(rs.getString(2));
					u.setPassword(rs.getString(3));
					u.setFirstName(rs.getString(4));
					u.setLastName(rs.getString(5));
					u.setEmail(rs.getString(6));
					u.setRoleID(rs.getInt(7));
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return u;
	}

	public User getByEmail(String email) {
		User u = null;
		String sql = new StringBuilder("select * from ").append(table).append(" where user_email =?").toString();
		try (Connection c = db.getConnection();) {
			try (PreparedStatement ps = c.prepareStatement(sql);) {
				ps.setString(1, email);
				ResultSet rs = ps.executeQuery();
				if (rs.next()) {
					u = new User();
					u.setID(rs.getInt(1));
					u.setUsername(rs.getString(2));
					u.setPassword(rs.getString(3));
					u.setFirstName(rs.getString(4));
					u.setLastName(rs.getString(5));
					u.setEmail(rs.getString(6));
					u.setRoleID(rs.getInt(7));
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return u;
	}

	@Override
	public int create(User u) {
		int id = 0;
		String sql = new StringBuilder("insert into ").append(table).append(getColumns()).append(" values(?,?,?,?,?,?)")
				.append(" returning ers_user_id").toString();
		try (Connection c = db.getConnection();) {
			c.setAutoCommit(false);
			try (PreparedStatement ps = c.prepareStatement(sql);) {
				ps.setString(1, u.getUsername());
				ps.setString(2, u.getPassword());
				ps.setString(3, u.getFirstName());
				ps.setString(4, u.getLastName());
				ps.setString(5, u.getEmail());
				ps.setInt(6, u.getRoleID());
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
	public boolean update(User u) {
		boolean out = false;
		String sql = new StringBuilder("update ").append(table).append(getColumns()).append(" set(?,?,?,?,?,?)")
				.toString();
		try (Connection c = db.getConnection();) {
			c.setAutoCommit(false);
			try (PreparedStatement ps = c.prepareStatement(sql);) {
				ps.setString(1, u.getUsername());
				ps.setString(2, u.getPassword());
				ps.setString(3, u.getFirstName());
				ps.setString(4, u.getLastName());
				ps.setString(5, u.getEmail());
				ps.setInt(6, u.getRoleID());
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
	public boolean delete(User u) {
		boolean out = false;
		String sql = new StringBuilder("delete from ").append(table).append(" where ers_user_id =?").toString();
		try (Connection c = db.getConnection();) {
			c.setAutoCommit(false);
			try (PreparedStatement ps = c.prepareStatement(sql);) {
				ps.setInt(1, u.getID());
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

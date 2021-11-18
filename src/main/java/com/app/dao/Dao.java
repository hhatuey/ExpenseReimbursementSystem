package com.app.dao;

import java.util.List;

public interface Dao<T> {
	List<T> getAll();
	T getByID(int id);
	int create(T t);
	boolean update(T t);
	boolean delete(T t);
	
}

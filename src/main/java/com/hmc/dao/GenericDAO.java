package com.hmc.dao;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public interface GenericDAO<T, PK extends Serializable> {

	
	public T findById(PK id);
	
	public PK save(T object);
	
	public void saveOrUpdate(T object);
	
	public void saveOrUpdate(List<T> objects);
	
	public T merge(T object);
	
	public void delete(T object);
	
	public List<T> findList();
	
	public List<T> findList(int first, int pageSize, Map<String, Object> filters, Map<String, String> sortOrders);
	
	public List<T> findList(int first, int pageSize, Map<String, Object> filters);
	
	public List<T> findListByIsActive(int isActive);
	
	public int count(Map<String, Object> filters);
	
}

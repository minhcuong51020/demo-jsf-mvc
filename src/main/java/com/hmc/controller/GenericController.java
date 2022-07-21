package com.hmc.controller;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.primefaces.model.LazyDataModel;
import org.springframework.beans.factory.annotation.Autowired;

import com.hmc.dao.GenericDAO;
import com.hmc.lazy.LazyDataModelBaseNew;

public class GenericController<T, PK extends Serializable> {
	
	@Autowired
	protected GenericDAO<T, PK> daoService;
	protected LazyDataModel<T> lazyDataModel;
	protected T newObj;
	protected T selectedObj;
	protected T searchObj;
	
	
	@PostConstruct
	public void onStart() {
		Map<String, Object> filters = new HashMap<String, Object>();
		lazyDataModel = new LazyDataModelBaseNew<>(daoService, filters);
	}
	
	public void onSearch() {
		
	}
	
	public void saveOrUpdate() {
		
	}
	
	public void delete(T object) {
		
	}
	
	public void clear() {
	}
	
}

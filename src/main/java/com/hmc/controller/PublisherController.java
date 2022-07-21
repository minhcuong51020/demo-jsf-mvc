package com.hmc.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.ManagedBean;
import javax.faces.bean.RequestScoped;

import org.primefaces.model.LazyDataModel;
import org.springframework.beans.factory.annotation.Autowired;

import com.hmc.dao.PublisherService;
import com.hmc.entity.Publisher;
import com.hmc.lazy.LazyDataModelBaseNew;

@ManagedBean
@RequestScoped
public class PublisherController {

	@Autowired
	private PublisherService daoService;
	private LazyDataModel<Publisher> lazyDataModel;
	private Publisher newObj;
	private Publisher selectedObj;
	private Publisher searchObj;
	
	public void onStart() {
		clear();
		Map<String, Object> filters = new HashMap<>();
		lazyDataModel = new LazyDataModelBaseNew<>(daoService, filters);
	}
	
	public List<Publisher> findAll() {
		return daoService.findList();
	}
	
	public void clear() {
		newObj = new Publisher();
		selectedObj = new Publisher();
		searchObj = new Publisher();
	}
	
	public LazyDataModel<Publisher> getLazyDataModel() {
		return lazyDataModel;
	}

	public void setLazyDataModel(LazyDataModel<Publisher> lazyDataModel) {
		this.lazyDataModel = lazyDataModel;
	}

	public Publisher getNewObj() {
		return newObj;
	}

	public void setNewObj(Publisher newObj) {
		this.newObj = newObj;
	}

	public Publisher getSelectedObj() {
		return selectedObj;
	}

	public void setSelectedObj(Publisher selectedObj) {
		this.selectedObj = selectedObj;
	}

	public Publisher getSearchObj() {
		return searchObj;
	}

	public void setSearchObj(Publisher searchObj) {
		this.searchObj = searchObj;
	}
	
}

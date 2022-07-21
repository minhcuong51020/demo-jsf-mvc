package com.hmc.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.ManagedBean;
import javax.annotation.PostConstruct;
import javax.faces.bean.RequestScoped;

import org.primefaces.model.LazyDataModel;
import org.springframework.beans.factory.annotation.Autowired;

import com.hmc.dao.AuthorService;
import com.hmc.entity.Author;
import com.hmc.lazy.LazyDataModelBaseNew;

@ManagedBean
@RequestScoped
public class AuthorController {
	
	@Autowired
	private AuthorService daoService;
	private LazyDataModel<Author> lazyDataModel;
	private Author newObj;
	private Author selectedObj;
	private Author searchObj;
	
	@PostConstruct
	public void onStart() {
		Map<String, Object> filters = new HashMap<String, Object>();
		lazyDataModel = new LazyDataModelBaseNew<>(daoService, filters);
	}
	
	public void search() {
		
	}
	
	public List<Author> findAll() {
		return daoService.findList();
	}

	public LazyDataModel<Author> getLazyDataModel() {
		return lazyDataModel;
	}

	public void setLazyDataModel(LazyDataModel<Author> lazyDataModel) {
		this.lazyDataModel = lazyDataModel;
	}

	public Author getNewObj() {
		return newObj;
	}

	public void setNewObj(Author newObj) {
		this.newObj = newObj;
	}

	public Author getSelectedObj() {
		return selectedObj;
	}

	public void setSelectedObj(Author selectedObj) {
		this.selectedObj = selectedObj;
	}

	public Author getSearchObj() {
		return searchObj;
	}

	public void setSearchObj(Author searchObj) {
		this.searchObj = searchObj;
	}
	
}

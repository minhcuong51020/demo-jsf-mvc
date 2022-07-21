package com.hmc.controller;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.ManagedBean;
import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;

import org.apache.commons.lang3.StringUtils;
import org.ocpsoft.shade.org.apache.commons.beanutils.BeanUtils;
import org.ocpsoft.shade.org.apache.commons.beanutils.BeanUtilsBean;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.context.RequestContext;
import org.primefaces.model.LazyDataModel;
import org.springframework.beans.factory.annotation.Autowired;

import com.hmc.dao.CategoryService;
import com.hmc.entity.Category;
import com.hmc.lazy.LazyDataModelBaseNew;
import com.hmc.util.MessageUtil;

@ManagedBean
@javax.faces.bean.ViewScoped
public class CategoryController {

	@Autowired
	CategoryService daoService;

	private LazyDataModel<Category> lazyDataModel;
	private Category selectedObj;
	private Category newObj;
	private Category searchObj;
	private boolean isEdit;

	@PostConstruct
	public void onStart() {
		clear();
		Map<String, Object> filters = new HashMap<String, Object>();
		lazyDataModel = new LazyDataModelBaseNew<>(daoService, filters);
	}
	
	public void search() {
		Map<String, Object> filters = new HashMap<>();
		
		if(StringUtils.isNotEmpty(searchObj.getName())) {
			filters.put("name", searchObj.getName());
		}
		if(searchObj.getIsActive() != null) {
			filters.put("isActive", searchObj.getIsActive());
		}
		lazyDataModel = new LazyDataModelBaseNew<>(daoService, filters);
	}
	
	public void saveOrUpdate() {
		System.out.println("in save");
		String msg = "";
		try {
			selectedObj = new Category();
			BeanUtilsBean.getInstance().getConvertUtils().register(false, false, 0);
			BeanUtils.copyProperties(selectedObj, newObj);
			msg = "Cập nhật thành công";
			if(!isEdit) {
				msg = "Thêm mới thành công";
				selectedObj.setId(null);
			}
			daoService.saveOrUpdate(selectedObj);
			MessageUtil.setInfoMessage(msg);
			RequestContext.getCurrentInstance().update("form:objectTable");
			clear();
		} catch (Exception e) {
			MessageUtil.setErrorMessage("Cập nhật, thêm mới thất bại");
			e.printStackTrace();
		}
	}
	
	public void delete(Category category) {
		try {
			daoService.delete(category);
			MessageUtil.setErrorMessage("Xóa thành công");
		} catch (Exception e) {
			MessageUtil.setErrorMessage("Xóa thất bại");
			e.printStackTrace();
		}
	}
	
	public void clickTest() {
		System.out.println("test add or update success");
	}
	
	public void prepareEdit() {
		isEdit = true;
		BeanUtilsBean.getInstance().getConvertUtils().register(false, false, 0);
		try {
			BeanUtils.copyProperties(newObj, selectedObj);
		} catch (IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
		}
	}
	
	public void clear() {
		this.isEdit = false;
		this.newObj = new Category();
		this.selectedObj = new Category();
		this.searchObj = new Category();
	}
	
	public List<Category> findAll() {
		List<Category> categories = daoService.findList();
		return categories;
	}

	public Category getSelectedObj() {
		return selectedObj;
	}

	public void setSelectedObj(Category selectedObj) {
		this.selectedObj = selectedObj;
	}

	public Category getNewObj() {
		return newObj;
	}

	public void setNewObj(Category newObj) {
		this.newObj = newObj;
	}
	
	public Category getSearchObj() {
		return searchObj;
	}
	
	public void setSearchObj(Category searchObj) {
		this.searchObj = searchObj;
	}

	public boolean getIsEdit() {
		return isEdit;
	}

	public void setIsEdit(boolean isEdit) {
		this.isEdit = isEdit;
	}
	
	public LazyDataModel<Category> getLazyDataModel() {
		return lazyDataModel;
	}
	
	public void setLazyDataModel(LazyDataModel<Category> lazyDataModel) {
		this.lazyDataModel = lazyDataModel;
	}
}

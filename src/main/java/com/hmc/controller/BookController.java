package com.hmc.controller;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.ManagedBean;
import javax.annotation.PostConstruct;
import javax.faces.bean.RequestScoped;

import org.ocpsoft.shade.org.apache.commons.beanutils.BeanUtils;
import org.ocpsoft.shade.org.apache.commons.beanutils.BeanUtilsBean;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.UploadedFile;
import org.springframework.beans.factory.annotation.Autowired;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.hmc.dao.BookService;
import com.hmc.entity.Book;
import com.hmc.lazy.LazyDataModelBaseNew;
import com.hmc.util.CloudinaryUntil;
import com.hmc.util.MessageUtil;

@ManagedBean
@RequestScoped
public class BookController {

	@Autowired
	private BookService daoService;
	private LazyDataModel<Book> lazyDataModel;
	private Book newObj;
	private Book selectedObj;
	private Book searchObj;
	private boolean isEdit;
	
	@PostConstruct
	public void onStart() {
		clear();
		Map<String, Object> filters = new HashMap<String, Object>();
		lazyDataModel = new LazyDataModelBaseNew<>(daoService, filters);
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
	
	public void search() {
		
	}
	
	public void fileHandleListener(FileUploadEvent e) {
		this.newObj.setFile(e.getFile());
	}
	
	public void saveOrUpdate() {
		String msg = "";
		try {
			selectedObj = new Book();
			BeanUtilsBean.getInstance().getConvertUtils().register(false, false, 0);
			BeanUtils.copyProperties(selectedObj, newObj);
			String sourceFileUpLoad = uploadImageToCloud(selectedObj.getFile());
			msg = "Cập nhật thành công";
			if(sourceFileUpLoad != null && sourceFileUpLoad.length() != 0) {
				selectedObj.setThumbnail(sourceFileUpLoad);
			}
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
	
	public String uploadImageToCloud(UploadedFile file) {
		String source = "";
		if(file != null) {
			Cloudinary c = CloudinaryUntil.cloudinary();
			try {
				Map<String, String> result = c.uploader().upload(file.getContents(), ObjectUtils.asMap("resource_type", "auto"));
				source = result.get("secure_url");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return source;
	}
	
	public void clear() {
		this.isEdit = false;
		this.newObj = new Book();
		this.selectedObj = new Book();
	}

	public LazyDataModel<Book> getLazyDataModel() {
		return lazyDataModel;
	}

	public void setLazyDataModel(LazyDataModel<Book> lazyDataModel) {
		this.lazyDataModel = lazyDataModel;
	}

	public Book getNewObj() {
		return newObj;
	}

	public void setNewObj(Book newObj) {
		this.newObj = newObj;
	}

	public Book getSelectedObj() {
		return selectedObj;
	}

	public void setSelectedObj(Book selectedObj) {
		this.selectedObj = selectedObj;
	}
	
	public Book getSearchObj() {
		return searchObj;
	}
	
	public void setSearchObj(Book searchObj) {
		this.searchObj = searchObj;
	}

	public boolean getIsEdit() {
		return isEdit;
	}

	public void setIsEdit(boolean isEdit) {
		this.isEdit = isEdit;
	}
	
	
}

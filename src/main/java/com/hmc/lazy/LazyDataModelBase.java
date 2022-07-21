package com.hmc.lazy;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import com.hmc.dao.GenericDAO;

public class LazyDataModelBase<T, PK extends Serializable> extends LazyDataModel<T> {

	private static final long serialVersionUID = 2060069039496830078L;
	private GenericDAO<T, PK> daoService;
	
	public LazyDataModelBase(GenericDAO<T, PK> daoService) {
		this.daoService = daoService;
	}
	

	public void setRowIndex(int rowIndex) {
		if(rowIndex == -1 || getPageSize() == 0) {
			super.setRowIndex(-1);
		} else {
			super.setRowIndex(rowIndex % getPageSize());
		}
	}
	
	public T getRowData(PK rowKey) {
		T object = null;
		try {
			object = this.daoService.findById(rowKey);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return object;
	}
	
	@Override
	public List<T> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
		List data = new ArrayList();
		int dataSize = 0;
		try {
			data = this.daoService.findList(first, pageSize, filters);
			dataSize = this.daoService.count(filters);
		} catch (Exception e) {
		}
		setRowCount(dataSize);
		System.out.println("base lazy new: " + dataSize);
		return data;
	}

	public GenericDAO<T, PK> getDaoService() {
		return daoService;
	}
	
	public void setDaoService(GenericDAO<T, PK> daoService) {
		this.daoService = daoService;
	}
}

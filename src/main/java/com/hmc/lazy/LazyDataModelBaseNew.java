package com.hmc.lazy;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.primefaces.model.SortOrder;

import com.hmc.dao.GenericDAO;
import com.hmc.dao.impl.GenericDAOImpl;

public class LazyDataModelBaseNew<T, PK extends Serializable> extends LazyDataModelBase<T, PK> {

	private static final long serialVersionUID = 1L;
	private GenericDAO<T, PK> daoService;
	private Map<String, Object> filters;
	private LinkedHashMap<String, String> orders;
	
	@SuppressWarnings("unchecked")
	public LazyDataModelBaseNew(GenericDAO<T, PK> daoService, Object... filtersOrOrders) {
		super(daoService);
		if(daoService != null) {
			this.daoService = daoService;
		} else {
			this.daoService = new GenericDAOImpl<T, PK>() {
			};
		}
		
		if(filtersOrOrders != null) {
			switch (filtersOrOrders.length) {
			case 1:
				if(filtersOrOrders[0] instanceof Map<?, ?>) {
					this.filters = (Map<String, Object>) filtersOrOrders[0];
				}
				break;
			case 2:
				if(filtersOrOrders[0] != null && filtersOrOrders[0] instanceof Map<?, ?>) {
					this.filters = (Map<String, Object>) filtersOrOrders[0];
				}
				if(filtersOrOrders[1] != null && filtersOrOrders[1] instanceof Map<?, ?>) {
					this.orders = (LinkedHashMap<String, String>) filtersOrOrders[1];
				}
				break;
			default:
				// no sort or filter
				break;
			}
		}
	}
	
	@Override
	public List<T> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
		List<T> data = new ArrayList<T>();
		int dataSize = 0;
		try {
			if(this.filters != null) {
				Iterator<String> iterator = this.filters.keySet().iterator();
				while(iterator.hasNext()) {
					String field = iterator.next();
					Object value = this.filters.get(field);
					if(value instanceof String) {
						filters.put(field, (String) value);
					} else if(value instanceof Date[]) {
						Date[] filDate = (Date[]) value;
						SimpleDateFormat fomatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
						Date fromDate;
						Date toDate;
						switch (filDate.length) {
						case 1:
							fromDate = filDate[0];
							if(fromDate != null) {
								filters.put(field, fomatter.format(fromDate) + "-");
							}
							break;
						case 2:
							fromDate = filDate[0];
							toDate = filDate[1];
							if(fromDate == null && toDate != null) {
								filters.put(field, "-" + fomatter.format(toDate));
							} else if(fromDate != null && toDate == null) {
								filters.put(field, fomatter.format(fromDate) + "-");
							} else if(fromDate != null && toDate != null) {
								filters.put(field, fomatter.format(fromDate) + "-" + fomatter.format(toDate));
							}
							break;
						default:
							break;
						}
					} else if(value instanceof Number) {
						filters.put(field, ((Number) value).toString());
					}
				}
			}
			LinkedHashMap<String, String> sorter = null;
			if(this.orders != null) {
				sorter = new LinkedHashMap<>();
				Iterator<String> iterator = this.orders.keySet().iterator();
				while(iterator.hasNext()) {
					String field = iterator.next();
					String value = this.orders.get(field);
					sorter.put(field, value);
				}
			}
			if(sortField != null) {
				if(sorter == null) {
					sorter = new LinkedHashMap<>();
				}
				switch (sortOrder) {
					case ASCENDING:
						sorter.put(sortField, "ASC");
						break;
					case DESCENDING:
						sorter.put(sortField, "DESC");
						break;
					case UNSORTED:
					default:
						sorter = null;
						break;
				}
			}
			data = this.daoService.findList(first, pageSize, filters, sorter);
			dataSize = this.daoService.count(filters);
		} catch (Exception e) {
			
		}
		this.setRowCount(dataSize);
		return data;
	}

}

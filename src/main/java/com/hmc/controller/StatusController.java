package com.hmc.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.ManagedBean;

import org.springframework.web.context.annotation.RequestScope;

import com.hmc.util.Status;

@ManagedBean
@RequestScope
public class StatusController {
	
	public List<Status> getStatus() {
		Status isActive = new Status(1, "Đang hoạt động");
		Status isDeactive = new Status(0, "Dừng hoạt động");
		List<Status> list = new ArrayList<>();
		list.add(isActive);
		list.add(isDeactive);
		return list;
	}
	
}

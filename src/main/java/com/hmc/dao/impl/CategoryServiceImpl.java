package com.hmc.dao.impl;

import java.io.Serializable;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hmc.dao.CategoryService;
import com.hmc.entity.Category;

@Service
public class CategoryServiceImpl extends GenericDAOImpl<Category, Serializable> implements CategoryService, Serializable {

}

package com.hmc.dao.impl;

import java.io.Serializable;

import org.springframework.stereotype.Service;

import com.hmc.dao.PublisherService;
import com.hmc.entity.Publisher;

@Service
public class PublisherServiceImpl extends GenericDAOImpl<Publisher, Serializable> implements PublisherService, Serializable {

}

package com.hmc.dao.impl;

import java.io.Serializable;

import org.springframework.stereotype.Service;

import com.hmc.dao.AuthorService;
import com.hmc.entity.Author;

@Service
public class AuthorServiceImpl extends GenericDAOImpl<Author, Serializable> implements AuthorService, Serializable {

}

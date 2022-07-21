package com.hmc.dao.impl;

import java.io.Serializable;

import org.springframework.stereotype.Service;

import com.hmc.dao.BookService;
import com.hmc.entity.Book;

@Service
public class BookServiceImpl extends GenericDAOImpl<Book, Serializable> implements BookService, Serializable {

}

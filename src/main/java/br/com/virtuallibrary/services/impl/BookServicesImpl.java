package br.com.virtuallibrary.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.virtuallibrary.commons.services.BaseServiceImpl;
import br.com.virtuallibrary.entity.Book;
import br.com.virtuallibrary.repositories.BookRepository;
import br.com.virtuallibrary.services.BookService;

@Service
public class BookServicesImpl extends BaseServiceImpl<Book, String, BookRepository> implements BookService {

	@Autowired
	public BookServicesImpl(BookRepository repository) {
		super(repository);
	}

}

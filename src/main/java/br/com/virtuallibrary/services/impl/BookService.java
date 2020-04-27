package br.com.virtuallibrary.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.virtuallibrary.commons.services.impl.SaveAndUpdateService;
import br.com.virtuallibrary.entity.Book;
import br.com.virtuallibrary.repositories.BookRepository;
import br.com.virtuallibrary.services.IBookService;

@Service
public class BookService extends SaveAndUpdateService<Book, String, BookRepository> implements IBookService {

	@Autowired
	public BookService(BookRepository repository) {
		super(repository);
	}

}

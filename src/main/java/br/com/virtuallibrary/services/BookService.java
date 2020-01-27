package br.com.virtuallibrary.services;

import br.com.virtuallibrary.commons.services.BaseService;
import br.com.virtuallibrary.entity.Book;
import br.com.virtuallibrary.repositories.BookRepository;

public interface BookService extends BaseService<Book, String, BookRepository> {

}

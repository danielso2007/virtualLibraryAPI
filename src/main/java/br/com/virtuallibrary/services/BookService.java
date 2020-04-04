package br.com.virtuallibrary.services;

import br.com.virtuallibrary.commons.services.IBaseService;
import br.com.virtuallibrary.entity.Book;
import br.com.virtuallibrary.repositories.BookRepository;

public interface BookService extends IBaseService<Book, String, BookRepository> {

}

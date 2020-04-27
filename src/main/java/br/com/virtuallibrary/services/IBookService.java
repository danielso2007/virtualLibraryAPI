package br.com.virtuallibrary.services;

import br.com.virtuallibrary.commons.services.ISaveAndUpdateService;
import br.com.virtuallibrary.entity.Book;
import br.com.virtuallibrary.repositories.BookRepository;

public interface IBookService extends ISaveAndUpdateService<Book, String, BookRepository> {

}

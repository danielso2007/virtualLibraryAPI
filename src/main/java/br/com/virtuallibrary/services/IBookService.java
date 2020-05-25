package br.com.virtuallibrary.services;

import com.commons.rest.api.services.ISaveAndUpdateService;

import br.com.virtuallibrary.entity.Book;
import br.com.virtuallibrary.repositories.BookRepository;

public interface IBookService extends ISaveAndUpdateService<Book, String, BookRepository> {

}

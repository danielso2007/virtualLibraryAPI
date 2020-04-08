package br.com.virtuallibrary.services;

import java.util.Map;

import org.springframework.data.domain.Page;

import br.com.virtuallibrary.commons.services.IBaseService;
import br.com.virtuallibrary.entity.Book;
import br.com.virtuallibrary.repositories.BookRepository;

public interface BookService extends IBaseService<Book, String, BookRepository> {

	Page<Book> findPaginated(int page, int size, Map<String, String> filters);

}

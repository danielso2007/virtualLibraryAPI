package br.com.virtuallibrary.repositories;

import java.util.List;

import org.springframework.stereotype.Repository;

import br.com.virtuallibrary.commons.repositories.IBaseRepository;
import br.com.virtuallibrary.entity.Book;

@Repository
public interface BookRepository extends IBaseRepository<Book, String> {

	List<Book> findByAuthorStartingWith(String regexp);
	List<Book> findByAuthorEndingWith(String regexp);

	List<Book> findByTitleStartingWith(String regexp);
	List<Book> findByTitleEndingWith(String regexp);
}

package br.com.virtuallibrary.repositories;

import org.springframework.stereotype.Repository;

import br.com.virtuallibrary.commons.repositories.IBaseRepository;
import br.com.virtuallibrary.entity.Book;

@Repository
public interface BookRepository extends IBaseRepository<Book, String> {

}

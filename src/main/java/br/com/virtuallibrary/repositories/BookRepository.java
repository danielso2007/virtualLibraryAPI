package br.com.virtuallibrary.repositories;

import org.springframework.stereotype.Repository;

import br.com.virtuallibrary.commons.repositories.BaseRepository;
import br.com.virtuallibrary.entity.Book;

@Repository
public interface BookRepository extends BaseRepository<Book, String> {

}

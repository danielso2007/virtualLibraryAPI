package br.com.virtuallibrary.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import br.com.virtuallibrary.commons.services.impl.BaseServiceImpl;
import br.com.virtuallibrary.entity.Book;
import br.com.virtuallibrary.repositories.BookRepository;
import br.com.virtuallibrary.services.BookService;

@Service
public class BookServicesImpl extends BaseServiceImpl<Book, String, BookRepository> implements BookService {

	@Autowired
	public BookServicesImpl(BookRepository repository) {
		super(repository);
	}

	@Override
	public Page<Book> findPaginated(String title, String author, int page, int size) {
		Query query = new Query();
		
		if (title != null) {
			query.addCriteria(new Criteria("title").regex(String.format(".*%s.*", title)));
		}
		if (author != null) {
			query.addCriteria(new Criteria("author").regex(String.format(".*%s.*", author)));
		}
		
		return findAll(query, page, size);
	}

}

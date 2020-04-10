package br.com.virtuallibrary.rest.controllers.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.web.bind.annotation.RestController;

import br.com.virtuallibrary.commons.controllers.impl.CompleteController;
import br.com.virtuallibrary.entity.Book;
import br.com.virtuallibrary.repositories.BookRepository;
import br.com.virtuallibrary.rest.controllers.IBookController;
import br.com.virtuallibrary.rest.hateoas.assembers.BookModelAssembler;
import br.com.virtuallibrary.rest.hateoas.model.BookModel;
import br.com.virtuallibrary.services.BookService;

@RestController
public class BookController
	extends CompleteController<Book, String, BookRepository, BookService, BookModel>
    implements IBookController<Book, String, BookRepository, BookService, BookModel> {
	@Autowired
	public BookController(
			BookService service,
			PagedResourcesAssembler<Book> pagedResourcesAssembler,
			BookModelAssembler modelAssembler) {
		super(service, pagedResourcesAssembler, modelAssembler);
	}
}
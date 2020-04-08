package br.com.virtuallibrary.rest.controllers.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.server.ExposesResourceFor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import br.com.virtuallibrary.commons.IConstants;
import br.com.virtuallibrary.commons.controllers.impl.BaseController;
import br.com.virtuallibrary.entity.Book;
import br.com.virtuallibrary.repositories.BookRepository;
import br.com.virtuallibrary.rest.controllers.IBookController;
import br.com.virtuallibrary.rest.hateoas.assembers.BookModelAssembler;
import br.com.virtuallibrary.rest.hateoas.model.BookModel;
import br.com.virtuallibrary.services.BookService;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RestControllerAdvice
@CrossOrigin(origins = "*")
@ExposesResourceFor(Book.class)
@RequestMapping(IConstants.BOOKS)
@Tag(name = "Book", description = "The Book API")
public class BookController extends BaseController<Book, String, BookRepository, BookService, BookModel>
			implements IBookController<Book, String, BookRepository, BookService, BookModel>{

	@Autowired
	public BookController(BookService service, PagedResourcesAssembler<Book> pagedResourcesAssembler, BookModelAssembler modelAssembler) {
		super(service, pagedResourcesAssembler, modelAssembler);
	}

}
package br.com.virtuallibrary.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.virtuallibrary.commons.Constants;
import br.com.virtuallibrary.commons.controllers.BaseController;
import br.com.virtuallibrary.entity.Book;
import br.com.virtuallibrary.repositories.BookRepository;
import br.com.virtuallibrary.services.BookService;

@RestController
@RequestMapping(Constants.ROOT_URL + Constants.V1 + "/books")
public class BookController extends BaseController<Book, String, BookRepository, BookService> {

	@Autowired
	public BookController(BookService service) {
		super(service);
	}

	@GetMapping
	public List<Book> findAll() {
		return getService().findAll();
	}

}
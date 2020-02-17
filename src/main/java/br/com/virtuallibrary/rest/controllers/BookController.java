package br.com.virtuallibrary.rest.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.ExposesResourceFor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.virtuallibrary.commons.Constants;
import br.com.virtuallibrary.commons.controllers.BaseController;
import br.com.virtuallibrary.entity.Book;
import br.com.virtuallibrary.repositories.BookRepository;
import br.com.virtuallibrary.rest.hateoas.assembers.BookModelAssembler;
import br.com.virtuallibrary.rest.hateoas.model.BookModel;
import br.com.virtuallibrary.services.BookService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@ExposesResourceFor(Book.class)
@RestController
@CrossOrigin(origins = "*")
@RequestMapping(Constants.BOOKS)
@Api(tags = "Book", value = "Os livros da biblioteca virtual", protocols = "HTTP")
public class BookController extends BaseController<Book, String, BookRepository, BookService, BookModel> {

	@Autowired
	private PagedResourcesAssembler<Book> pagedResourcesAssembler;
	
	@Autowired
	public BookController(BookService service, BookModelAssembler modelAssembler) {
		super(service, modelAssembler);
	}

	@ResponseStatus(HttpStatus.OK)
	@GetMapping(produces = { Constants.APPLICATION_JSON_UTF_8, Constants.APPLICATION_XML_UTF_8 })
	@ApiOperation(value = "Obter todos os registros", notes = "Obter todos os registros da base de dados.")
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Registros listados com sucesso"),
			@ApiResponse(code = 400, message = "Erro na obtenção dos dados"),
			@ApiResponse(code = 500, message = "Erro interno do servidor") })
	public ResponseEntity<CollectionModel<BookModel>> findAll(
			@RequestParam(value = "page", required = false, defaultValue = Constants.defaultPage) int page,
			@RequestParam(value = "size", required = false, defaultValue = Constants.defaultSize) int size) {
		
		PagedModel<BookModel> collModel = pagedResourcesAssembler.toModel(getService().findPaginated(page, size), getModelAssembler());
		
		return ResponseEntity.ok().body(collModel);
	}

}
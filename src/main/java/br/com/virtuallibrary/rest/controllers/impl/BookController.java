package br.com.virtuallibrary.rest.controllers.impl;

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
import org.springframework.web.bind.annotation.RestControllerAdvice;

import br.com.virtuallibrary.commons.IConstants;
import br.com.virtuallibrary.commons.controllers.impl.BaseController;
import br.com.virtuallibrary.entity.Book;
import br.com.virtuallibrary.repositories.BookRepository;
import br.com.virtuallibrary.rest.controllers.IBookController;
import br.com.virtuallibrary.rest.hateoas.assembers.BookModelAssembler;
import br.com.virtuallibrary.rest.hateoas.model.BookModel;
import br.com.virtuallibrary.services.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
	private PagedResourcesAssembler<Book> pagedResourcesAssembler;
	
	@Autowired
	public BookController(BookService service, BookModelAssembler modelAssembler) {
		super(service, modelAssembler);
	}

	@ResponseStatus(HttpStatus.OK)
	@GetMapping(produces = { IConstants.APPLICATION_JSON_UTF_8, IConstants.APPLICATION_XML_UTF_8 })
	@Operation(summary = "Obter lista de Book paginado",
	description = "Retorna a lista de Books paginado.<br/>"
			+ "Os filtros são por default contains e ignoreCase.<br/>"
			+ "Segue os filtros:<br/>"
			+ "Por título;<br/>"
			+ "Por autor;<br/>"
			+ "Os filtros podem ser combinados.",
	tags = { "Book" })
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Registros listados com sucesso"),
			@ApiResponse(responseCode = "400", description = "Erro na obtenção dos dados"),
			@ApiResponse(responseCode = "500", description = "Erro interno do servidor")})
	public ResponseEntity<CollectionModel<BookModel>> findAll(
			@Parameter(description="Número da página.") @RequestParam(value = "page", required = false, defaultValue = IConstants.defaultPage) int page,
			@Parameter(description="Quantidade de registros por página.") @RequestParam(value = "size", required = false, defaultValue = IConstants.defaultSize) int size,
			@Parameter(description="Filtro pelo título do livro.") @RequestParam(value = "title", required = false) String title,
			@Parameter(description="Filtro pelo autor do livro.") @RequestParam(value = "author", required = false) String author) {
		
		PagedModel<BookModel> collModel = null;
		
		if (title == null && author == null) {
			collModel = pagedResourcesAssembler.toModel(getService().findPaginated(page, size), getModelAssembler());
		} else{
			
			collModel = pagedResourcesAssembler.toModel(getService().findPaginated(title, author, page, size), getModelAssembler());
		}
		
		
		return ResponseEntity.ok().body(collModel);
	}

}
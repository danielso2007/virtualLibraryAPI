package br.com.virtuallibrary.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.virtuallibrary.commons.Constants;
import br.com.virtuallibrary.commons.controllers.BaseController;
import br.com.virtuallibrary.entity.Book;
import br.com.virtuallibrary.repositories.BookRepository;
import br.com.virtuallibrary.services.BookService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(Constants.ROOT_URL + Constants.V1 + "/books")
@Api(tags = "Book", value = "Os livros da biblioteca virtual", protocols = "HTTP")
public class BookController extends BaseController<Book, String, BookRepository, BookService> {

	@Autowired
	public BookController(BookService service) {
		super(service);
	}

	@ResponseStatus(HttpStatus.OK)
	@GetMapping(produces = { Constants.APPLICATION_JSON_UTF_8, Constants.APPLICATION_XML_UTF_8 })
	@ApiOperation(value = "Obter todos os registros", notes = "Obter todos os registros da base de dados.")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Registros listados com sucesso"),
			@ApiResponse(code = 400, message = "Erro na obtenção dos dados"),
			@ApiResponse(code = 500, message = "Erro interno do servidor") })
	public List<Book> findAll() {
		return getService().findAll();
	}

}
package br.com.virtuallibrary.rest.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
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
import br.com.virtuallibrary.entity.Rating;
import br.com.virtuallibrary.repositories.RatingRepository;
import br.com.virtuallibrary.rest.hateoas.assembers.RatingModelAssembler;
import br.com.virtuallibrary.rest.hateoas.model.RatingModel;
import br.com.virtuallibrary.services.RatingService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(Constants.RATINGS)
@Api(tags = "Rating", value = "Avaliação dos livros da biblioteca virtual", protocols = "HTTP")
public class RatingController extends BaseController<Rating, String, RatingRepository, RatingService, RatingModel> {

	@Autowired
	private PagedResourcesAssembler<Rating> pagedResourcesAssembler;
	
	@Autowired
	public RatingController(RatingService service, RatingModelAssembler modelAssembler) {
		super(service, modelAssembler);
	}

	@ResponseStatus(HttpStatus.OK)
	@GetMapping(produces = { Constants.APPLICATION_JSON_UTF_8, Constants.APPLICATION_XML_UTF_8 })
	@ApiOperation(value = "Obter todos os registros", notes = "Obter todos os registros da base de dados.")
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Registros listados com sucesso"),
			@ApiResponse(code = 400, message = "Erro na obtenção dos dados"),
			@ApiResponse(code = 500, message = "Erro interno do servidor") })
	public ResponseEntity<PagedModel<RatingModel>> findAll(
			@RequestParam(value = "bookId", required = false, defaultValue = Constants.BLANK) String bookId,
			@RequestParam(value = "page", required = false, defaultValue = Constants.defaultPage) int page, 
			@RequestParam(value = "size", required = false, defaultValue = Constants.defaultSize) int size) {

		PagedModel<RatingModel> collModel = null;
		
		if (bookId.isBlank()) {
			collModel = pagedResourcesAssembler.toModel(getService().findPaginated(page, size), getModelAssembler());
		} else {
			collModel = pagedResourcesAssembler.toModel(getService().findPaginated(bookId, page, size), getModelAssembler());
		}
		
		return ResponseEntity.ok().body(collModel);
	}

}
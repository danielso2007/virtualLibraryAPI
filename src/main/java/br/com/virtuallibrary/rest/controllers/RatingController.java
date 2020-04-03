package br.com.virtuallibrary.rest.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.web.PagedResourcesAssembler;
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

import br.com.virtuallibrary.commons.Constants;
import br.com.virtuallibrary.commons.controllers.BaseController;
import br.com.virtuallibrary.entity.Rating;
import br.com.virtuallibrary.repositories.RatingRepository;
import br.com.virtuallibrary.rest.hateoas.assembers.RatingModelAssembler;
import br.com.virtuallibrary.rest.hateoas.model.RatingModel;
import br.com.virtuallibrary.services.RatingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RestControllerAdvice
@CrossOrigin(origins = "*")
@ExposesResourceFor(Rating.class)
@RequestMapping(Constants.RATINGS)
@Tag(name = "Rating", description = "The Rating API")
public class RatingController extends BaseController<Rating, String, RatingRepository, RatingService, RatingModel> {

	@Autowired
	private PagedResourcesAssembler<Rating> pagedResourcesAssembler;
	
	@Autowired
	public RatingController(RatingService service, RatingModelAssembler modelAssembler) {
		super(service, modelAssembler);
	}

	@ResponseStatus(HttpStatus.OK)
	@GetMapping(produces = { Constants.APPLICATION_JSON_UTF_8, Constants.APPLICATION_XML_UTF_8 })
	@Operation(summary = "Obter lista de Rating paginado", description = "Retorna a lista de Rating paginado. Quando passado %bookId%, retorna todos os Ratings associado ao livro.", tags = { "Rating" })
	@ApiResponses(value = { 
			@ApiResponse(responseCode = "200", description = "Registros listados com sucesso"),
			@ApiResponse(responseCode = "400", description = "Erro na obtenção dos dados"),
			@ApiResponse(responseCode = "500", description = "Erro interno do servidor") })
	public ResponseEntity<PagedModel<RatingModel>> findAll(
			@Parameter(description="O código do livro, deafult é \"\"") @RequestParam(value = "bookId", required = false, defaultValue = Constants.BLANK) String bookId,
			@Parameter(description="Número da página, default é 0") @RequestParam(value = "page", required = false, defaultValue = Constants.defaultPage) int page, 
			@Parameter(description="Quantidade de registros por página, default é 5") @RequestParam(value = "size", required = false, defaultValue = Constants.defaultSize) int size) {

		PagedModel<RatingModel> collModel = null;
		
		if (bookId.isBlank()) {
			collModel = pagedResourcesAssembler.toModel(getService().findPaginated(page, size), getModelAssembler());
		} else {
			collModel = pagedResourcesAssembler.toModel(getService().findPaginated(bookId, page, size), getModelAssembler());
		}
		
		return ResponseEntity.ok().body(collModel);
	}

}
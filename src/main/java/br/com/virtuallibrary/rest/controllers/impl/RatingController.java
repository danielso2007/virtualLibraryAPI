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
import br.com.virtuallibrary.entity.Rating;
import br.com.virtuallibrary.repositories.RatingRepository;
import br.com.virtuallibrary.rest.controllers.IRatingController;
import br.com.virtuallibrary.rest.hateoas.assembers.RatingModelAssembler;
import br.com.virtuallibrary.rest.hateoas.model.RatingModel;
import br.com.virtuallibrary.services.RatingService;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RestControllerAdvice
@CrossOrigin(origins = "*")
@ExposesResourceFor(Rating.class)
@RequestMapping(IConstants.RATINGS)
@Tag(name = "Rating", description = "The Rating API")
public class RatingController extends BaseController<Rating, String, RatingRepository, RatingService, RatingModel>
			implements IRatingController<Rating, String, RatingRepository, RatingService, RatingModel>{

	@Autowired
	public RatingController(RatingService service, PagedResourcesAssembler<Rating> pagedResourcesAssembler, RatingModelAssembler modelAssembler) {
		super(service, pagedResourcesAssembler, modelAssembler);
	}

}
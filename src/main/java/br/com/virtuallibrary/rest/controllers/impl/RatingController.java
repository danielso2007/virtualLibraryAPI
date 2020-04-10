package br.com.virtuallibrary.rest.controllers.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.web.bind.annotation.RestController;

import br.com.virtuallibrary.commons.controllers.impl.CompleteController;
import br.com.virtuallibrary.entity.Rating;
import br.com.virtuallibrary.repositories.RatingRepository;
import br.com.virtuallibrary.rest.controllers.IRatingController;
import br.com.virtuallibrary.rest.hateoas.assembers.RatingModelAssembler;
import br.com.virtuallibrary.rest.hateoas.model.RatingModel;
import br.com.virtuallibrary.services.RatingService;

@RestController
public class RatingController
	extends CompleteController<Rating, String, RatingRepository, RatingService, RatingModel>
	implements IRatingController<Rating, String, RatingRepository, RatingService, RatingModel> {
	@Autowired
	public RatingController(RatingService service, PagedResourcesAssembler<Rating> pagedResourcesAssembler, RatingModelAssembler modelAssembler) {
		super(service, pagedResourcesAssembler, modelAssembler);
	}
}
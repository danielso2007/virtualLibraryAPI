package br.com.virtuallibrary.rest.controllers.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.web.bind.annotation.RestController;

import com.commons.rest.api.controllers.impl.SaveAndUpdateController;

import br.com.virtuallibrary.entity.Rating;
import br.com.virtuallibrary.repositories.RatingRepository;
import br.com.virtuallibrary.rest.controllers.IRatingController;
import br.com.virtuallibrary.rest.hateoas.assembers.RatingModelAssembler;
import br.com.virtuallibrary.rest.hateoas.model.RatingModel;
import br.com.virtuallibrary.services.IRatingService;

@RestController
public class RatingController
	extends SaveAndUpdateController<Rating, String, RatingRepository, IRatingService, RatingModel>
	implements IRatingController<Rating, String, RatingRepository, IRatingService, RatingModel> {
	@Autowired
	public RatingController(IRatingService service, PagedResourcesAssembler<Rating> pagedResourcesAssembler, RatingModelAssembler modelAssembler) {
		super(service, pagedResourcesAssembler, modelAssembler);
	}
}
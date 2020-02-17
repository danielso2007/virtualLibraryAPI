package br.com.virtuallibrary.rest.hateoas.assembers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import br.com.virtuallibrary.entity.Rating;
import br.com.virtuallibrary.rest.controllers.BookController;
import br.com.virtuallibrary.rest.controllers.RatingController;
import br.com.virtuallibrary.rest.hateoas.model.RatingModel;

@Component
public class RatingModelAssembler extends RepresentationModelAssemblerSupport<Rating, RatingModel> {

	public RatingModelAssembler() {
		super(RatingController.class, RatingModel.class);
	}

	@Override
	public RatingModel toModel(Rating entity) {
		RatingModel model = instantiateModel(entity);

		model.add(linkTo(methodOn(RatingController.class).find(entity.getId())).withSelfRel());
		model.add(linkTo(methodOn(RatingController.class).delete(entity.getId())).withRel("delete"));
		model.add(linkTo(methodOn(BookController.class).find(entity.getBookId())).withRel("book"));

		model.setId(entity.getId());
		model.setBookId(entity.getBookId());
		model.setStars(entity.getStars());
		model.setCreatedAt(entity.getCreatedAt());
		model.setCreator(entity.getCreator());
		model.setUpdatedAt(entity.getUpdatedAt());
		model.setUpdater(entity.getUpdater());

		return model;
	}

}

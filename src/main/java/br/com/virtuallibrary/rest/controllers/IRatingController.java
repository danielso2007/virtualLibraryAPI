package br.com.virtuallibrary.rest.controllers;

import java.io.Serializable;

import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.ExposesResourceFor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;

import br.com.virtuallibrary.commons.IConstants;
import br.com.virtuallibrary.commons.controllers.IBaseController;
import br.com.virtuallibrary.commons.entities.BaseEntity;
import br.com.virtuallibrary.commons.repositories.IBaseRepository;
import br.com.virtuallibrary.commons.services.IBaseService;
import br.com.virtuallibrary.entity.Rating;
import io.swagger.v3.oas.annotations.tags.Tag;

@CrossOrigin(origins = "*")
@ExposesResourceFor(Rating.class)
@RequestMapping(IConstants.RATINGS)
@Tag(name = "Rating", description = "The Rating API")
public interface IRatingController<
			E extends BaseEntity, 
			ID extends Serializable, 
			R extends IBaseRepository<E, ID>, 
			S extends IBaseService<E, ID, R>, 
			M extends RepresentationModel<M>>
		extends IBaseController<E, ID, R, S, M> {

}

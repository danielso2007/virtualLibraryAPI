package br.com.virtuallibrary.rest.controllers;

import java.io.Serializable;

import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.ExposesResourceFor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;

import br.com.virtuallibrary.commons.IConstants;
import br.com.virtuallibrary.commons.controllers.IDeleteController;
import br.com.virtuallibrary.commons.controllers.ILoadController;
import br.com.virtuallibrary.commons.controllers.ISaveAndUpdateController;
import br.com.virtuallibrary.commons.controllers.ISearchController;
import br.com.virtuallibrary.commons.entities.BaseEntity;
import br.com.virtuallibrary.commons.repositories.IBaseRepository;
import br.com.virtuallibrary.commons.services.ICompleteService;
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
			S extends ICompleteService<E, ID, R>, 
			M extends RepresentationModel<M>>
		extends ISearchController<E, ID, R, S, M>,
		        IDeleteController<E, ID, R, S, M>,
		        ILoadController<E, ID, R, S, M>,
		        ISaveAndUpdateController<E, ID, R, S, M> {

}

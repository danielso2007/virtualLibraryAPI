package br.com.virtuallibrary.rest.controllers;

import java.io.Serializable;

import org.springframework.hateoas.RepresentationModel;

import br.com.virtuallibrary.commons.controllers.IBaseController;
import br.com.virtuallibrary.commons.entities.BaseEntity;
import br.com.virtuallibrary.commons.repositories.IBaseRepository;
import br.com.virtuallibrary.commons.services.IBaseService;

public interface IRatingController<
			E extends BaseEntity, 
			ID extends Serializable, 
			R extends IBaseRepository<E, ID>, 
			S extends IBaseService<E, ID, R>, 
			M extends RepresentationModel<M>>
		extends IBaseController<E, ID, R, S, M> {

}

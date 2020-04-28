package br.com.virtuallibrary.commons.controllers.impl;

import java.io.Serializable;

import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

import br.com.virtuallibrary.commons.controllers.IDeleteController;
import br.com.virtuallibrary.commons.entities.BaseEntity;
import br.com.virtuallibrary.commons.repositories.IBaseRepository;
import br.com.virtuallibrary.commons.services.IDeleteService;

public class DeleteController<
	    E extends BaseEntity,
	    K extends Serializable,
	    R extends IBaseRepository<E, K>,
	    S extends IDeleteService<E, K, R>,
	    M extends RepresentationModel<M>>
    extends LoadController<E, K, R, S, M>
    implements IDeleteController<E, K, R, S, M> {

	public DeleteController(S service, PagedResourcesAssembler<E> pagedResourcesAssembler, RepresentationModelAssemblerSupport<E, M> modelAssembler) {
		super(service, pagedResourcesAssembler, modelAssembler);
	}

	@Override
	public ResponseEntity<Object> delete(@PathVariable K id) {
		return getService().findById(id).map(entity -> {
			getService().delete(id);
			return ResponseEntity.ok().build();
		}).orElse(ResponseEntity.notFound().build());
	}

}
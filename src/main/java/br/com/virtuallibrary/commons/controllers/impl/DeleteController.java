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
import br.com.virtuallibrary.commons.services.ICrudService;

public abstract class DeleteController<
	    E extends BaseEntity,
	    ID extends Serializable,
	    R extends IBaseRepository<E, ID>,
	    S extends ICrudService<E, ID, R>,
	    M extends RepresentationModel<M>>
    implements IDeleteController<E, ID, R, S, M> {

	private final S service;
	private final RepresentationModelAssemblerSupport<E, M> modelAssembler;
	private final PagedResourcesAssembler<E> pagedResourcesAssembler;

	public DeleteController(S service, PagedResourcesAssembler<E> pagedResourcesAssembler, RepresentationModelAssemblerSupport<E, M> modelAssembler) {
		this.service = service;
		this.modelAssembler = modelAssembler;
		this.pagedResourcesAssembler = pagedResourcesAssembler;
	}

	@Override
	public final S getService() {
		return service;
	}
	
	@Override
	public PagedResourcesAssembler<E> getPagedResourcesAssembler() {
		return pagedResourcesAssembler;
	}

	@Override
	public RepresentationModelAssemblerSupport<E, M> getModelAssembler() {
		return this.modelAssembler;
	}
	
	@Override
	public ResponseEntity<Object> delete(@PathVariable ID id) {
		return service.findById(id).map(entity -> {
			service.delete(id);
			return ResponseEntity.ok().build();
		}).orElse(ResponseEntity.notFound().build());
	}

}
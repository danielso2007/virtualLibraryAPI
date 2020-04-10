package br.com.virtuallibrary.commons.controllers.impl;

import java.io.Serializable;
import java.util.Map;

import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;

import br.com.virtuallibrary.commons.IConstants;
import br.com.virtuallibrary.commons.controllers.ISearchController;
import br.com.virtuallibrary.commons.entities.BaseEntity;
import br.com.virtuallibrary.commons.repositories.IBaseRepository;
import br.com.virtuallibrary.commons.services.ISearchService;

public abstract class SeachController<
	    E extends BaseEntity,
	    ID extends Serializable,
	    R extends IBaseRepository<E, ID>,
	    S extends ISearchService<E, ID, R>,
	    M extends RepresentationModel<M>>
    implements ISearchController<E, ID, R, S, M> {

	private final S service;
	private final RepresentationModelAssemblerSupport<E, M> modelAssembler;
	private final PagedResourcesAssembler<E> pagedResourcesAssembler;

	public SeachController(S service, PagedResourcesAssembler<E> pagedResourcesAssembler, RepresentationModelAssemblerSupport<E, M> modelAssembler) {
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
	public ResponseEntity<CollectionModel<M>> findAll(
			@RequestParam(value = "page", required = false, defaultValue = IConstants.defaultPage) int page,
			@RequestParam(value = "size", required = false, defaultValue = IConstants.defaultSize) int size,
			@RequestParam(required = false) Map<String,String> filters) {
		return ResponseEntity.ok().body(getPagedResourcesAssembler().toModel(getService().findPaginated(page, size, filters), getModelAssembler()));
	}
	
}
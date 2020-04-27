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

public class SeachController<
	    E extends BaseEntity,
	    ID extends Serializable,
	    R extends IBaseRepository<E, ID>,
	    S extends ISearchService<E, ID, R>,
	    M extends RepresentationModel<M>>
    extends BaseController<E, ID, R, S, M>
    implements ISearchController<E, ID, R, S, M> {

	public SeachController(S service, PagedResourcesAssembler<E> pagedResourcesAssembler, RepresentationModelAssemblerSupport<E, M> modelAssembler) {
		super(service, pagedResourcesAssembler, modelAssembler);
	}

	@Override
	public ResponseEntity<CollectionModel<M>> findAll(
			@RequestParam(value = "page", required = false, defaultValue = IConstants.defaultPage) int page,
			@RequestParam(value = "size", required = false, defaultValue = IConstants.defaultSize) int size,
			@RequestParam(required = false) Map<String,String> filters) {
		return ResponseEntity.ok().body(getPagedResourcesAssembler().toModel(getService().findPaginated(page, size, filters), getModelAssembler()));
	}
	
}
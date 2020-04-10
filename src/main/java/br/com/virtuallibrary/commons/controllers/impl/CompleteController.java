package br.com.virtuallibrary.commons.controllers.impl;

import java.io.Serializable;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import br.com.virtuallibrary.commons.IConstants;
import br.com.virtuallibrary.commons.controllers.IDeleteController;
import br.com.virtuallibrary.commons.controllers.ILoadController;
import br.com.virtuallibrary.commons.controllers.ISaveAndUpdateController;
import br.com.virtuallibrary.commons.controllers.ISearchController;
import br.com.virtuallibrary.commons.entities.BaseEntity;
import br.com.virtuallibrary.commons.repositories.IBaseRepository;
import br.com.virtuallibrary.commons.services.ICompleteService;
import lombok.extern.slf4j.Slf4j;

//FIXME: Repetição de código, verificar como usar multipla herança implementada. Code repetition, check how to use multiple inheritance implemented.
@Slf4j
public abstract class CompleteController<
	    E extends BaseEntity,
	    ID extends Serializable,
	    R extends IBaseRepository<E, ID>,
	    S extends ICompleteService<E, ID, R>,
	    M extends RepresentationModel<M>>
	implements ISearchController<E, ID, R, S, M>,
			   IDeleteController<E, ID, R, S, M>,
			   ILoadController<E, ID, R, S, M>,
			   ISaveAndUpdateController<E, ID, R, S, M> {

	private final S service;
	private final RepresentationModelAssemblerSupport<E, M> modelAssembler;
	private final PagedResourcesAssembler<E> pagedResourcesAssembler;

	public CompleteController(S service, PagedResourcesAssembler<E> pagedResourcesAssembler, RepresentationModelAssemblerSupport<E, M> modelAssembler) {
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
	
	@Override
	public ResponseEntity<M> find(@PathVariable ID id) {
		return service.findById(id) 
			.map(modelAssembler::toModel) 
			.map(ResponseEntity::ok) 
			.orElse(ResponseEntity.notFound().build());
	}

	@Override
	public ResponseEntity<M> create(@RequestBody @Valid E object) {
		return service.save(object)
				.map(modelAssembler::toModel) 
				.map(entity -> ResponseEntity.status(HttpStatus.CREATED).body(entity)) 
				.orElse(ResponseEntity.notFound().build());
	}

	@Override
	public ResponseEntity<Object> delete(@PathVariable ID id) {
		return service.findById(id).map(entity -> {
			service.delete(id);
			return ResponseEntity.ok().build();
		}).orElse(ResponseEntity.notFound().build());
	}

	@Override
	public ResponseEntity<M> update(@RequestBody @Valid E object, @PathVariable ID id) {
		return service.update(object, id)
				.map(modelAssembler::toModel) 
				.map(ResponseEntity::ok) 
				.orElse(ResponseEntity.notFound().build());
	}

	@Override
	public ResponseEntity<M> update(
			@RequestBody Map<String, String> updates,
			@PathVariable ID id) throws NoSuchFieldException,
	                                    SecurityException,
	                                    IllegalArgumentException,
	                                    IllegalAccessException {
		try {
			return service.update(updates, id)
			.map(modelAssembler::toModel) 
			.map(ResponseEntity::ok) 
			.orElse(ResponseEntity.notFound().build());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return ResponseEntity.notFound().build();
		}
	}

}
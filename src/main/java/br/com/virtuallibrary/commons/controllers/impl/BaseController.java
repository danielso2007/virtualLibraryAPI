package br.com.virtuallibrary.commons.controllers.impl;

import java.io.Serializable;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import br.com.virtuallibrary.commons.IConstants;
import br.com.virtuallibrary.commons.controllers.IBaseController;
import br.com.virtuallibrary.commons.entities.BaseEntity;
import br.com.virtuallibrary.commons.repositories.IBaseRepository;
import br.com.virtuallibrary.commons.services.IBaseService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BaseController<
	    E extends BaseEntity,
	    ID extends Serializable,
	    R extends IBaseRepository<E, ID>,
	    S extends IBaseService<E, ID, R>,
	    M extends RepresentationModel<M>>
    implements IBaseController<E, ID, R, S, M> {

	private final S service;
	private final RepresentationModelAssemblerSupport<E, M> modelAssembler;
	private final PagedResourcesAssembler<E> pagedResourcesAssembler;

	public BaseController(S service, PagedResourcesAssembler<E> pagedResourcesAssembler, RepresentationModelAssemblerSupport<E, M> modelAssembler) {
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
		
		PagedModel<M> collModel = getPagedResourcesAssembler().toModel(getService().findPaginated(page, size, filters), getModelAssembler());
		
		return ResponseEntity.ok().body(collModel);
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
			@PathVariable ID id) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
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
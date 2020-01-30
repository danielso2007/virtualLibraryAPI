package br.com.virtuallibrary.commons.controllers;

import java.io.Serializable;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import br.com.virtuallibrary.commons.entities.BaseEntity;
import br.com.virtuallibrary.commons.repositories.BaseRepository;
import br.com.virtuallibrary.commons.services.BaseService;

public class BaseController<E extends BaseEntity, ID extends Serializable, R extends BaseRepository<E, ID>, S extends BaseService<E, ID, R>> {

	private final S service;

	public BaseController(S service) {
		this.service = service;
	}

	public S getService() {
		return service;
	}

	@GetMapping("/{id}")
	public ResponseEntity<E> find(@PathVariable ID id) {
		return service.findById(id).map(entity -> ResponseEntity.ok().body(entity))
				.orElse(ResponseEntity.notFound().build());
	}

	@PostMapping
	public ResponseEntity<E> create(@RequestBody @Valid E object) {
		return service.save(object).map(entity -> ResponseEntity.ok().body(entity))
				.orElse(ResponseEntity.notFound().build());
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Object> delete(@PathVariable ID id) {
		return service.findById(id).map(entity -> {
			service.delete(id);
			return ResponseEntity.ok().build();
		}).orElse(ResponseEntity.notFound().build());
	}

	@PutMapping("/{id}")
	public ResponseEntity<E> update(@RequestBody @Valid E object, @PathVariable ID id) {
		return service.update(object, id).map(entity -> ResponseEntity.ok().body(entity))
				.orElse(ResponseEntity.notFound().build());

	}

	@PatchMapping("/{id}")
	public ResponseEntity<E> update(@RequestBody Map<String, String> updates, @PathVariable ID id) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		return service.update(updates, id).map(entity -> ResponseEntity.ok().body(entity))
				.orElse(ResponseEntity.notFound().build());
	}

}
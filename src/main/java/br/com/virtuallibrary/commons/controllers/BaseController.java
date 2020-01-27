package br.com.virtuallibrary.commons.controllers;

import java.io.Serializable;
import java.util.Map;

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
	public E find(@PathVariable ID id) {
		return service.findById(id);
	}

	@PostMapping
	public E createE(@RequestBody E object) {
		return service.create(object);

	}

	@DeleteMapping("/{id}")
	public void delete(@PathVariable ID id) {
		service.delete(id);

	}

	@PutMapping("/{id}")
	public E update(@RequestBody E object, @PathVariable ID id) {
		return service.update(object, id);

	}

	@PatchMapping("/{id}")
	public E update(@RequestBody Map<String, String> updates, @PathVariable ID id) {
		return service.update(updates, id);

	}

}
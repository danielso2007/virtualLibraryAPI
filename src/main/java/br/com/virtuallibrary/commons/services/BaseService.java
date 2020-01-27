package br.com.virtuallibrary.commons.services;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import br.com.virtuallibrary.commons.entities.BaseEntity;
import br.com.virtuallibrary.commons.repositories.BaseRepository;

public interface BaseService<E extends BaseEntity, ID extends Serializable, R extends BaseRepository<E, ID>> {

	List<E> findAll();

	E findById(ID id);

	E create(E object);

	void delete(ID id);

	E update(Map<String, String> updates, ID id);

	E update(E object, ID id);

}

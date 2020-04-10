package br.com.virtuallibrary.commons.services;

import java.io.Serializable;
import java.util.Map;
import java.util.Optional;

import javax.validation.ValidationException;

import br.com.virtuallibrary.commons.entities.BaseEntity;
import br.com.virtuallibrary.commons.repositories.IBaseRepository;

public interface ISaveAndUpdateService<E extends BaseEntity, ID extends Serializable, R extends IBaseRepository<E, ID>>
	extends IBaseService<E, ID, R> {

	Optional<E> save(E object);

	Optional<E> update(Map<String, String> updates, final ID id) throws ValidationException, SecurityException, IllegalArgumentException, IllegalAccessException;

	Optional<E> update(E object, final ID id);

}

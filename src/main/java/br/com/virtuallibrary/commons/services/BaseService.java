package br.com.virtuallibrary.commons.services;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.validation.ValidationException;

import org.springframework.data.domain.Page;
import org.springframework.security.core.userdetails.UserDetails;

import br.com.virtuallibrary.commons.entities.BaseEntity;
import br.com.virtuallibrary.commons.repositories.BaseRepository;

public interface BaseService<E extends BaseEntity, ID extends Serializable, R extends BaseRepository<E, ID>> {

	List<E> findAll();

	Page<E> findPaginated(int page, int size);
	
	Optional<E> findById(ID id);

	Optional<E> save(E object);

	void delete(ID id);

	Optional<E> update(Map<String, String> updates, ID id) throws ValidationException, SecurityException, IllegalArgumentException, IllegalAccessException;

	Optional<E> update(E object, ID id);

	UserDetails getUser();

}

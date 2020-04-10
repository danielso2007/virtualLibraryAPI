package br.com.virtuallibrary.commons.services;

import java.io.Serializable;
import java.util.Optional;

import br.com.virtuallibrary.commons.entities.BaseEntity;
import br.com.virtuallibrary.commons.repositories.IBaseRepository;

public interface ILoadService<E extends BaseEntity, ID extends Serializable, R extends IBaseRepository<E, ID>>
	extends IBaseService<E, ID, R> {

	Optional<E> findById(final ID id);

}

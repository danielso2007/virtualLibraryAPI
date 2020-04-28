package br.com.virtuallibrary.commons.services;

import java.io.Serializable;
import java.util.Optional;

import br.com.virtuallibrary.commons.entities.BaseEntity;
import br.com.virtuallibrary.commons.repositories.IBaseRepository;

public interface ILoadService<E extends BaseEntity, K extends Serializable, R extends IBaseRepository<E, K>>
	extends ISearchService<E, K, R> {

	Optional<E> findById(final K id);

}

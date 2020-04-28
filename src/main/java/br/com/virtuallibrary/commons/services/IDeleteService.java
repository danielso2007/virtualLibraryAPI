package br.com.virtuallibrary.commons.services;

import java.io.Serializable;

import br.com.virtuallibrary.commons.entities.BaseEntity;
import br.com.virtuallibrary.commons.repositories.IBaseRepository;

public interface IDeleteService<E extends BaseEntity, K extends Serializable, R extends IBaseRepository<E, K>>
	extends ILoadService<E, K, R> {

	void delete(final K id);

}

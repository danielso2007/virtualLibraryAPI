package br.com.virtuallibrary.commons.services;

import java.io.Serializable;

import br.com.virtuallibrary.commons.entities.BaseEntity;
import br.com.virtuallibrary.commons.repositories.IBaseRepository;

public interface IDeleteService<E extends BaseEntity, ID extends Serializable, R extends IBaseRepository<E, ID>>
	extends IBaseService<E, ID, R> {

	void delete(final ID id);

}

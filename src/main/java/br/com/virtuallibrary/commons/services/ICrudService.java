package br.com.virtuallibrary.commons.services;

import java.io.Serializable;

import br.com.virtuallibrary.commons.entities.BaseEntity;
import br.com.virtuallibrary.commons.repositories.IBaseRepository;

public interface ICrudService<E extends BaseEntity, ID extends Serializable, R extends IBaseRepository<E, ID>>
	extends IDeleteService<E, ID, R>,
			ILoadService<E, ID, R>,
			ISaveAndUpdateService<E, ID, R> {}

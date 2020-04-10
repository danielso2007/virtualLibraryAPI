package br.com.virtuallibrary.commons.services;

import java.io.Serializable;

import br.com.virtuallibrary.commons.entities.BaseEntity;
import br.com.virtuallibrary.commons.repositories.IBaseRepository;

public interface ICompleteService<E extends BaseEntity, ID extends Serializable, R extends IBaseRepository<E, ID>>
	extends ICrudService<E, ID, R>,
			ISearchService<E, ID, R> {}

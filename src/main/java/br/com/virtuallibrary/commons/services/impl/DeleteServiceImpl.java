package br.com.virtuallibrary.commons.services.impl;

import java.io.Serializable;

import org.springframework.transaction.annotation.Transactional;

import br.com.virtuallibrary.commons.entities.BaseEntity;
import br.com.virtuallibrary.commons.repositories.IBaseRepository;
import br.com.virtuallibrary.commons.services.IDeleteService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Transactional(readOnly = true)
public abstract class DeleteServiceImpl<E extends BaseEntity, ID extends Serializable, R extends IBaseRepository<E, ID>>
        extends BaseServiceImpl<E, ID, R>
		implements IDeleteService<E, ID, R> {

	public DeleteServiceImpl(R repository) {
		super(repository);
	}

	@Override
	public void delete(ID id) {
		log.debug(String.format("Deleting record %s: %s", getEntityClass().toString(), id));
		getRepository().deleteById(id);
	}

}

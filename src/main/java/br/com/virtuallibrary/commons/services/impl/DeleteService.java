package br.com.virtuallibrary.commons.services.impl;

import java.io.Serializable;

import org.springframework.transaction.annotation.Transactional;

import br.com.virtuallibrary.commons.entities.BaseEntity;
import br.com.virtuallibrary.commons.repositories.IBaseRepository;
import br.com.virtuallibrary.commons.services.IDeleteService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Transactional(readOnly = true)
public class DeleteService<E extends BaseEntity, K extends Serializable, R extends IBaseRepository<E, K>>
        extends LoadService<E, K, R>
		implements IDeleteService<E, K, R> {

	public DeleteService(R repository) {
		super(repository);
	}

	@Override
	public void delete(K id) {
		log.debug(String.format("Deleting record %s: %s", getEntityClass().toString(), id));
		getRepository().deleteById(id);
	}

}

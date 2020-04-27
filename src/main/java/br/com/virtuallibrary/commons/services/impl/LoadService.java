package br.com.virtuallibrary.commons.services.impl;

import java.io.Serializable;
import java.util.Optional;

import org.springframework.transaction.annotation.Transactional;

import br.com.virtuallibrary.commons.entities.BaseEntity;
import br.com.virtuallibrary.commons.repositories.IBaseRepository;
import br.com.virtuallibrary.commons.services.ILoadService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Transactional(readOnly = true)
public class LoadService<E extends BaseEntity, ID extends Serializable, R extends IBaseRepository<E, ID>>
		extends SearchService<E, ID, R>
		implements ILoadService<E, ID, R> {

	public LoadService(R repository) {
		super(repository);
	}
	
	@Override
	public Optional<E> findById(ID id) {
		if (id == null) { return Optional.empty(); }
		log.debug(String.format("Getting %s registration -> %s", getEntityClass().toString(), id));
		return getRepository().findById(id);
	}

}

package br.com.virtuallibrary.commons.services;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import br.com.virtuallibrary.commons.entities.BaseEntity;
import br.com.virtuallibrary.commons.repositories.BaseRepository;
import br.com.virtuallibrary.commons.utils.GenericsUtils;

public class BaseServiceImpl<E extends BaseEntity, ID extends Serializable, R extends BaseRepository<E, ID>>
		implements BaseService<E, ID, R> {

	private final R repository;
	private final Class<E> entityClass;

	public BaseServiceImpl(R repository) {
		this.repository = repository;
		this.entityClass = GenericsUtils.getGenericsInfo(this).getType(0);
	}

	public R getRepository() {
		return repository;
	}

	public Class<E> getEntityClass() {
		return entityClass;
	}

	@Override
	public List<E> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public E findById(ID id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public E create(E object) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void delete(ID id) {
		// TODO Auto-generated method stub

	}

	@Override
	public E update(Map<String, String> updates, ID id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public E update(E object, ID id) {
		// TODO Auto-generated method stub
		return null;
	}

}

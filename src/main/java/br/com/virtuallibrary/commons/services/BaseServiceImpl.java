package br.com.virtuallibrary.commons.services;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.validation.ValidationException;

import org.springframework.beans.BeanUtils;

import br.com.virtuallibrary.commons.entities.BaseEntity;
import br.com.virtuallibrary.commons.repositories.BaseRepository;
import br.com.virtuallibrary.commons.utils.GenericsUtils;

public class BaseServiceImpl<E extends BaseEntity, ID extends Serializable, R extends BaseRepository<E, ID>>
		implements BaseService<E, ID, R> {

	public static final String A_ENTIDADE_NAO_PODE_SER_NULA = "A entidade não pode ser nula.";
	private final R repository;
	private final Class<E> entityClass;

	public BaseServiceImpl(R repository) {
		this.repository = repository;
		this.entityClass = GenericsUtils.getGenericsInfo(this).getType(0);
	}

	protected R getRepository() {
		return repository;
	}

	protected Class<E> getEntityClass() {
		return entityClass;
	}

	@Override
	public List<E> findAll() {
		return repository.findAll();
	}

	@Override
	public Optional<E> findById(ID id) {
		return repository.findById(id);
	}

	@Override
	public Optional<E> save(E object) {
		Optional<E> opt = Optional.ofNullable(repository.save(object));
		return opt;
	}

	@Override
	public void delete(ID id) {
		repository.deleteById(id);
	}
	
	@Override
	public Optional<E> update(Map<String, String> updates, ID id) throws ValidationException, SecurityException, IllegalArgumentException, IllegalAccessException {
		Optional<E> opt = repository.findById(id);
		if (opt.isEmpty()) {
			return Optional.empty();
		}
		E entity = opt.get();
		for (String fieldUpdate : updates.keySet()) {
			Field declaredField;
			try {
				declaredField = entityClass.getDeclaredField(fieldUpdate);
			} catch (NoSuchFieldException e) {
				throw new ValidationException(String.format("O campo %s não existe.", fieldUpdate));
			}
			boolean accessible = declaredField.canAccess(entity);
            declaredField.setAccessible(true);
            declaredField.set(entity, updates.get(fieldUpdate));
            declaredField.setAccessible(accessible);
		}
		return Optional.of(repository.save(entity));
	}

	@Override
	public Optional<E> update(E object, ID id) {
		if (object ==  null) {
		   throw new IllegalArgumentException(A_ENTIDADE_NAO_PODE_SER_NULA);
		}
		Optional<E> opt = repository.findById(id);
		if (opt.isEmpty()) {
			return Optional.empty();
		}
		BeanUtils.copyProperties(opt.get(), object);
		
		return Optional.of(repository.save(object));
	}

}

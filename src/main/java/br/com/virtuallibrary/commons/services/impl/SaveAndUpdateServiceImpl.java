package br.com.virtuallibrary.commons.services.impl;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.Optional;

import javax.validation.ValidationException;

import org.springframework.beans.BeanUtils;
import org.springframework.transaction.annotation.Transactional;

import br.com.virtuallibrary.commons.entities.BaseEntity;
import br.com.virtuallibrary.commons.repositories.IBaseRepository;
import br.com.virtuallibrary.commons.services.ISaveAndUpdateService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Transactional(readOnly = true)
public abstract class SaveAndUpdateServiceImpl<E extends BaseEntity, ID extends Serializable, R extends IBaseRepository<E, ID>>
		extends BaseServiceImpl<E, ID, R>
		implements ISaveAndUpdateService<E, ID, R> {

	public static final String THE_FIELD_DOES_NOT_EXIST_FORMAT = "The %s field does not exist.";
	public static final String THE_ENTITY_CANNOT_BE_NULL = "The entity cannot be null.";
	
	public SaveAndUpdateServiceImpl(R repository) {
		super(repository);
	}
	
	@Override
	@Transactional
	public Optional<E> save(E entity) {
		checkAuditedEntity(entity);
		log.debug(String.format("Saving %s record.", getEntityClass().toString()));
		Optional<E> opt = Optional.ofNullable(getRepository().save(entity));
		return opt;
	}

	@Override
	@Transactional
	public Optional<E> update(Map<String, String> updates, ID id)
			throws ValidationException, SecurityException, IllegalArgumentException, IllegalAccessException {
		Optional<E> opt = getRepository().findById(id);
		if (opt.isEmpty()) {
			return Optional.empty();
		}
		log.debug(String.format("Updating %s record fields: %s", getEntityClass().toString(), id));
		E entity = opt.get();
		for (String fieldUpdate : updates.keySet()) {
			Field declaredField;
			try {
				declaredField = getEntityClass().getDeclaredField(fieldUpdate);
			} catch (NoSuchFieldException e) {
				throw new ValidationException(String.format(THE_FIELD_DOES_NOT_EXIST_FORMAT, fieldUpdate));
			}
			boolean accessible = declaredField.canAccess(entity);
			declaredField.setAccessible(true);
			declaredField.set(entity, getValueByFieldType(declaredField.getName(), updates.get(fieldUpdate)));
			declaredField.setAccessible(accessible);
		}
		checkAuditedEntity(entity);
		return Optional.of(getRepository().save(entity));
	}

	@Override
	@Transactional
	public Optional<E> update(E entity, ID id) {
		if (entity == null) {
			throw new IllegalArgumentException(THE_ENTITY_CANNOT_BE_NULL);
		}
		log.debug(String.format("Updating %s record: %s", getEntityClass().toString(), id));
		Optional<E> opt = getRepository().findById(id);
		if (opt.isEmpty()) {
			return Optional.empty();
		}
		E entityOriginal = opt.get();
		BeanUtils.copyProperties(entity, entityOriginal, getNullPropertyNames(entity));
		checkAuditedEntity(entityOriginal);
		return Optional.of(getRepository().save(entityOriginal));
	}

}

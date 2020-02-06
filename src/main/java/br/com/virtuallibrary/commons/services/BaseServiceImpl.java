package br.com.virtuallibrary.commons.services;

import java.beans.Transient;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import javax.validation.ValidationException;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import br.com.virtuallibrary.commons.Constants;
import br.com.virtuallibrary.commons.entities.BaseAudit;
import br.com.virtuallibrary.commons.entities.BaseEntity;
import br.com.virtuallibrary.commons.repositories.BaseRepository;
import br.com.virtuallibrary.commons.utils.GenericsUtils;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BaseServiceImpl<E extends BaseEntity, ID extends Serializable, R extends BaseRepository<E, ID>>
		implements BaseService<E, ID, R> {

	public static final String O_CAMPO_S_NAO_EXISTE_FORMAT = "O campo %s não existe.";
	public static final String A_ENTIDADE_NAO_PODE_SER_NULA = "A entidade não pode ser nula.";
	private final String ANONYMOUS = "Anonymous";
	private final R repository;
	private final Class<E> entityClass;

	@Override
	public UserDetails getUser() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth == null || ANONYMOUS.equals(auth.getPrincipal())) {
			return new User(ANONYMOUS, Constants.BLANK, false, false, false, false, new ArrayList<>());
		} else {
			return (UserDetails) auth.getPrincipal();
		}
	}

	@Override
	public Optional<UserDetails> getPessoaLogada() {
		UserDetails userDetails = getUser();
		if (userDetails.getUsername().equals(ANONYMOUS)) {
			return Optional.empty();
		} else {
			// TODO: Obter o usuário logado.
			return Optional.empty();
		}
	}

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
		if (id == null) {
			return Optional.empty();
		}
		log.debug(String.format("Obtendo registro (%s): %s", entityClass.toString(), id));
		return repository.findById(id);
	}

	@Override
	public Optional<E> save(E entity) {
		checkAuditedEntity(entity);
		log.debug(String.format("Salvando registro %s.", entityClass.toString()));
		Optional<E> opt = Optional.ofNullable(repository.save(entity));
		return opt;
	}

	@Override
	public void delete(ID id) {
		log.debug(String.format("Deletando registro (%s): %s", entityClass.toString(), id));
		repository.deleteById(id);
	}

	@Override
	public Optional<E> update(Map<String, String> updates, ID id)
			throws ValidationException, SecurityException, IllegalArgumentException, IllegalAccessException {
		Optional<E> opt = repository.findById(id);
		if (opt.isEmpty()) {
			return Optional.empty();
		}
		log.debug(String.format("Atualizando campos do registro (%s): %s", entityClass.toString(), id));
		E entity = opt.get();
		for (String fieldUpdate : updates.keySet()) {
			Field declaredField;
			try {
				declaredField = entityClass.getDeclaredField(fieldUpdate);
			} catch (NoSuchFieldException e) {
				throw new ValidationException(String.format(O_CAMPO_S_NAO_EXISTE_FORMAT, fieldUpdate));
			}
			boolean accessible = declaredField.canAccess(entity);
			declaredField.setAccessible(true);
			declaredField.set(entity, updates.get(fieldUpdate));
			declaredField.setAccessible(accessible);
		}
		checkAuditedEntity(entity);
		return Optional.of(repository.save(entity));
	}

	@Override
	public Optional<E> update(E entity, ID id) {
		if (entity == null) {
			throw new IllegalArgumentException(A_ENTIDADE_NAO_PODE_SER_NULA);
		}
		log.debug(String.format("Atualizando registro (%s): %s", entityClass.toString(), id));
		Optional<E> opt = repository.findById(id);
		if (opt.isEmpty()) {
			return Optional.empty();
		}
		E entityOriginal = opt.get();
		BeanUtils.copyProperties(entity, entityOriginal, getNullPropertyNames(entity));
		checkAuditedEntity(entityOriginal);
		return Optional.of(repository.save(entityOriginal));
	}

	/*
	 * Referência
	 * https://www.it-swarm.net/pt/java/como-ignorar-valores-nulos-usando-
	 * springframework-beanutils-copyproperties/1043455506/
	 */
	private String[] getNullPropertyNames(Object source) {
		final BeanWrapper src = new BeanWrapperImpl(source);
		java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();

		Set<String> emptyNames = new HashSet<>();

		for (java.beans.PropertyDescriptor pd : pds) {
			if (pd.getReadMethod().isAnnotationPresent(Transient.class)) {
				continue;
			}
			Object srcValue = src.getPropertyValue(pd.getName());
			if (srcValue == null)
				emptyNames.add(pd.getName());
		}
		String[] result = new String[emptyNames.size()];
		return emptyNames.toArray(result);
	}

	private void checkAuditedEntity(E entity) {
		if (entity instanceof BaseAudit) {
			BaseAudit auditedEntity = (BaseAudit) entity;

			// Garantir que não vieram informações do front-end.
			auditedEntity.setCreator(null);
			auditedEntity.setCreatedAt(null);
			auditedEntity.setUpdater(null);
			auditedEntity.setUpdatedAt(null);

			Date date = new Date();
			String login = ANONYMOUS;
			if (getPessoaLogada().isPresent()) {
				// TODO: Adicionar o login do usuário.
			}

			if (auditedEntity.getId() == null) {
				auditedEntity.setCreatedAt(date);
				auditedEntity.setCreator(login);
			} else {
				auditedEntity.setUpdatedAt(date);
				auditedEntity.setUpdater(login);
			}
		}
	}

}

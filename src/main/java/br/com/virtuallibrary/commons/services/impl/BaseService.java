package br.com.virtuallibrary.commons.services.impl;

import java.beans.Transient;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;

import br.com.virtuallibrary.commons.IConstants;
import br.com.virtuallibrary.commons.entities.BaseAudit;
import br.com.virtuallibrary.commons.entities.BaseEntity;
import br.com.virtuallibrary.commons.repositories.IBaseRepository;
import br.com.virtuallibrary.commons.services.IBaseService;
import br.com.virtuallibrary.commons.utils.GenericsUtils;

@Transactional(readOnly = true)
public abstract class BaseService<E extends BaseEntity, K extends Serializable, R extends IBaseRepository<E, K>>
		implements IBaseService<E, K, R> {

	public static final String THE_FIELD_DOES_NOT_EXIST_FORMAT = "The %s field does not exist.";
	public static final String THE_ENTITY_CANNOT_BE_NULL = "The entity cannot be null.";
	public static final String ANONYMOUS = "Anonymous";
	
	private final R repository;
	private final Class<E> entityClass;

	@Autowired
	private MongoTemplate mongoTemplate;

	public BaseService(R repository) {
		this.repository = repository;
		this.entityClass = GenericsUtils.getGenericsInfo(this).getType(0);
	}

	@Override
	public final R getRepository() {
		return repository;
	}

	@Override
	public final Class<E> getEntityClass() {
		return entityClass;
	}
	
	@Override
	public final UserDetails getUser() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth == null || ANONYMOUS.equals(auth.getPrincipal())) {
			return new User(ANONYMOUS, IConstants.BLANK, false, false, false, false, new ArrayList<>());
		} else {
			return (UserDetails) auth.getPrincipal();
		}
	}

	@Override
	public MongoTemplate getTemplate() {
		return mongoTemplate;
	}
	
	@Override
	public void checkAuditedEntity(E entity) {
		if (entity instanceof BaseAudit) {
			BaseAudit auditedEntity = (BaseAudit) entity;

			Date date = new Date();
			String login = ANONYMOUS;
			if (getUser() != null) {
				login = getUser().getUsername();
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
	
	@Override
	public Class<?> getFieldType(String field) {
		Class<?> _class = null;
		for (java.beans.PropertyDescriptor pd : getPropertyDescriptor()) {
			if (pd.getName().equalsIgnoreCase(field)) {
				_class = pd.getPropertyType();
			}
		}
		return _class;
	}
	
	@Override
	public Query addCriterias(Query query, List<Criteria> criterias) {
		if (criterias != null) {
			criterias.forEach(query::addCriteria);
		}
		return query;
	}

	/*
	 * Reference:
	 * https://www.it-swarm.net/pt/java/como-ignorar-valores-nulos-usando-
	 * springframework-beanutils-copyproperties/1043455506/
	 */
	protected String[] getNullPropertyNames(Object source) {
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
	
	protected java.beans.PropertyDescriptor[] getPropertyDescriptor() {
		return new BeanWrapperImpl(entityClass).getPropertyDescriptors();
	}
	
	protected Object getValueByFieldType(String field, String value) {
		Class<?> _class = getFieldType(field);
		if (_class.equals(Integer.class)) {
			return Integer.valueOf(value);
		} else if (_class.equals(int.class)) {
			return Integer.parseInt(value);
		} else {
			return value;
		}
	}

}

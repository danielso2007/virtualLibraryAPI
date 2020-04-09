package br.com.virtuallibrary.commons.services.impl;

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
import java.util.TreeMap;

import javax.validation.ValidationException;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.repository.support.PageableExecutionUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;

import com.mongodb.BasicDBList;

import br.com.virtuallibrary.commons.IConstants;
import br.com.virtuallibrary.commons.entities.BaseAudit;
import br.com.virtuallibrary.commons.entities.BaseEntity;
import br.com.virtuallibrary.commons.repositories.IBaseRepository;
import br.com.virtuallibrary.commons.services.IBaseService;
import br.com.virtuallibrary.commons.utils.GenericsUtils;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Transactional(readOnly = true)
public class BaseServiceImpl<E extends BaseEntity, ID extends Serializable, R extends IBaseRepository<E, ID>>
		implements IBaseService<E, ID, R> {

	private static final String DIRECTION_DESC = "desc";
	private static final String ORDERBY = "orderby";
	public static final String THE_FIELD_DOES_NOT_EXIST_FORMAT = "The %s field does not exist.";
	public static final String THE_ENTITY_CANNOT_BE_NULL = "The entity cannot be null.";
	private final String ANONYMOUS = "Anonymous";
	
	private final R repository;
	private final Class<E> entityClass;

	@Autowired
	private MongoTemplate mongoTemplate;

	@Override
	public UserDetails getUser() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth == null || ANONYMOUS.equals(auth.getPrincipal())) {
			return new User(ANONYMOUS, IConstants.BLANK, false, false, false, false, new ArrayList<>());
		} else {
			return (UserDetails) auth.getPrincipal();
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
	public MongoTemplate getTemplate() {
		return mongoTemplate;
	}
	
	@Override
	public List<E> findAll() {
		return getRepository().findAll();
	}
	
	@Override
	public List<E> findAll(Sort sort) {
		return getRepository().findAll(sort);
	}

	@Override
	public Page<E> findAll(Query query, final int page, final int size) {
		return findAll(query, page, size, null, null);
	}
	
	@Override
	public Page<E> findAll(Query query, int page, int size, List<Criteria> criterias) {
		return findAll(query, page, size, criterias, null);
	}
	
	@Override
	public Page<E> findAll(Query query, int page, int size, List<Criteria> criterias, Sort sort) {
		Pageable pageable = PageRequest.of(page, size);
		query = addCriterias(query, criterias);
		query.with(pageable);
		if (sort != null) {
			query.with(sort);
		}
		List<E> results = getTemplate().find(query, getEntityClass());
		return getPage(results, pageable, query, criterias);
	}
	
	@Override
	public Page<E> getPage(List<E> results, Pageable pageable, Query query, List<Criteria> criterias) {
		return PageableExecutionUtils.getPage(results, pageable, () -> getTemplate().count(addCriterias(new Query(), criterias), entityClass));
	}
	
	private Query addCriterias(Query query, List<Criteria> criterias) {
		if (criterias != null) {
			criterias.forEach(criteriaDefinition -> query.addCriteria(criteriaDefinition));
		}
		return query;
	}

	@Override
	public Page<E> findPaginated(int page, int size, Map<String, String> filters) {
		Query query = new Query();
		
		Map<String, String> filterValues = getFilterValues(filters);
		List<Criteria> criterias = new ArrayList<>();
		
		if (!filterValues.isEmpty()) {
			filterValues.forEach((field, value) -> {
				criterias.add(createCriteriaByFilter(query, field, value));
			});
		}

		return findAll(query, page, size, criterias);
	}
	
	@Override
	public Page<E> findPaginated(int page, int size) {
		Pageable pageable = PageRequest.of(page, size);
		return getRepository().findAll(pageable);
	}

	@Override
	public Optional<E> findById(ID id) {
		if (id == null) {
			return Optional.empty();
		}
		log.debug(String.format("Getting %s registration -> %s", getEntityClass().toString(), id));
		return getRepository().findById(id);
	}

	@Override
	public Optional<E> save(E entity) {
		checkAuditedEntity(entity);
		log.debug(String.format("Saving %s record.", getEntityClass().toString()));
		Optional<E> opt = Optional.ofNullable(getRepository().save(entity));
		return opt;
	}

	@Override
	public void delete(ID id) {
		log.debug(String.format("Deleting record %s: %s", getEntityClass().toString(), id));
		repository.deleteById(id);
	}

	@Override
	public Optional<E> update(Map<String, String> updates, ID id)
			throws ValidationException, SecurityException, IllegalArgumentException, IllegalAccessException {
		Optional<E> opt = getRepository().findById(id);
		if (opt.isEmpty()) {
			return Optional.empty();
		}
		log.debug(String.format("Updating %s record fields: %s", entityClass.toString(), id));
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
		return Optional.of(repository.save(entityOriginal));
	}

	/*
	 * Reference:
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
	public Sort getSort(Map<String, String> filters) {
		String[] orderBy = new String[1];
		filters.forEach((k,v) -> {
			if (ORDERBY.equalsIgnoreCase(k)) {
				orderBy[0] = v;
			}
		});
		if (orderBy[0] == null || orderBy[0].isEmpty()) {
			return null;
		}
		String[] fields = null;
		fields = orderBy[0].split(IConstants.COMMA);
		
		List<Order> orders = new ArrayList<>();
		
		for(int x = 0; x < fields.length; x++) {
			String[] field = fields[x].split(IConstants.COLON);
			if (field.length == 2) {
				Direction direction = null;
				if (DIRECTION_DESC.equalsIgnoreCase(field[1])) {
					direction = Direction.DESC;
				} else {
					direction = Direction.ASC;
				}
				orders.add(new Order(direction, field[0]));
			} else {
				orders.add(new Order(Sort.DEFAULT_DIRECTION, fields[x]));
			}
		}
		return Sort.by(orders);
	}
	
	@Override
	public Map<String, String> getFilterValues(Map<String, String> filters) {
		Map<String, String> filterValues = new TreeMap<>();
		final BeanWrapper src = new BeanWrapperImpl(entityClass);
		java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();
		for (java.beans.PropertyDescriptor pd : pds) {
			if (pd.getReadMethod().isAnnotationPresent(Transient.class)) {
				continue;
			}
			if (filters.containsKey(pd.getName())) {
				filterValues.put(pd.getName(), filters.get(pd.getName()));
			}
		}
		return filterValues;
	}

	@Override
	public Class<?> getFieldType(String field) {
		Class<?> $class = null;
		final BeanWrapper src = new BeanWrapperImpl(entityClass);
		java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();

		for (java.beans.PropertyDescriptor pd : pds) {
			if (pd.getName().equalsIgnoreCase(field)) {
				$class = pd.getPropertyType();
			}
		}
		return $class;
	}
	
	private Object getValueByFieldType(String field, String value) {
		Class<?> $class = getFieldType(field);
		if ($class.equals(Integer.class)) {
			return Integer.valueOf(value);
		} if ($class.equals(int.class)) {
			return Integer.parseInt(value);
		} else {
			return value;
		}
	}

	@Override
	public Criteria getCriteriaByFilter(Criteria criteria, final String filter, final Object value)
			throws IllegalArgumentException {
		return getCriteriaByFilter(criteria, filter, value, null);
	}

	@Override
	public Criteria getCriteriaByFilter(Criteria criteria, final String filter, final Object value, String regex) {
		if (regex != null && value instanceof String) {
			return criteria.regex(String.format(regex, value));
		} else if (filter.equalsIgnoreCase(IConstants.$GREATER_THAN)) {
			return criteria.gt(value);
		} else if (filter.equalsIgnoreCase(IConstants.$LESS_THAN)) {
			return criteria.lt(value);
		} else if (filter.equalsIgnoreCase(IConstants.$GREATER_THAN_OR_EQUAL)) {
			return criteria.gte(value);
		} else if (filter.equalsIgnoreCase(IConstants.$LESS_THAN_OR_EQUAL)) {
			return criteria.lte(value);
		} else if (filter.equalsIgnoreCase(IConstants.$EQ)) {
			return criteria.is(value);
		} else if (filter.equalsIgnoreCase(IConstants.$CONTAINS)) {
			return criteria.regex(String.format(IConstants.$REGEX_CONTAINS, value));
		} else {
			return criteria.is(value);
		}
	}

	@Override
	public Criteria createCriteriaByFilter(final Query query, final String field, final String value) {
		return createCriteriaByFilter(query, field, value, null);
	}

	@Override
	public Criteria createCriteriaByFilter(final Query query, final String field, final String value, String regex) {
		String[] filters = value.split(IConstants.COLON);
		Criteria criteria = new Criteria(field);
		if (filters.length == 2) {
			return getCriteriaByFilter(criteria, filters[0], getValueByFieldType(field, filters[1]), regex);
		} else if (filters.length == 4) {
			BasicDBList bsonList = new BasicDBList();
			bsonList.add(getCriteriaByFilter(criteria, filters[0], getValueByFieldType(field, filters[1]), regex).getCriteriaObject());
			bsonList.add(getCriteriaByFilter(criteria, filters[2], getValueByFieldType(field, filters[3]), regex).getCriteriaObject());
			return new Criteria(IConstants.$AND).is(bsonList);
		} else {
			return criteria.is(getValueByFieldType(field, value));
		}
	}

}

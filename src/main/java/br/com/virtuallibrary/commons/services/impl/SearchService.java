package br.com.virtuallibrary.commons.services.impl;

import java.beans.Transient;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.repository.support.PageableExecutionUtils;
import org.springframework.transaction.annotation.Transactional;

import com.mongodb.BasicDBList;

import br.com.virtuallibrary.commons.IConstants;
import br.com.virtuallibrary.commons.entities.BaseEntity;
import br.com.virtuallibrary.commons.repositories.IBaseRepository;
import br.com.virtuallibrary.commons.services.ISearchService;

@Transactional(readOnly = true)
public class SearchService<E extends BaseEntity, K extends Serializable, R extends IBaseRepository<E, K>>
		extends BaseService<E, K, R> implements ISearchService<E, K, R> {

	private static final String DIRECTION_DESC = "desc";
	private static final String ORDERBY = "orderby";

	public SearchService(R repository) {
		super(repository);
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
		return PageableExecutionUtils.getPage(results, pageable,
				() -> getTemplate().count(addCriterias(new Query(), criterias), getEntityClass()));
	}

	@Override
	public Page<E> findPaginated(int page, int size, Map<String, String> filters) {
		Query query = new Query();

		Map<String, String> filterValues = getFilterValues(filters);
		List<Criteria> criterias = new ArrayList<>();

		if (!filterValues.isEmpty()) {
			filterValues.forEach((field, value) -> criterias.add(createCriteriaByFilter(query, field, value)));
		}

		return findAll(query, page, size, criterias, getSort(filters));
	}

	@Override
	public Page<E> findPaginated(int page, int size) {
		Pageable pageable = PageRequest.of(page, size);
		return getRepository().findAll(pageable);
	}

	@Override
	public Sort getSort(Map<String, String> filters) {
		String[] orderBy = new String[1];
		filters.forEach((k, v) -> {
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

		for (int x = 0; x < fields.length; x++) {
			String[] field = fields[x].split(IConstants.COLON);
			if (field.length == 2) {
				if (fieldExistsInTheEntity(field[0])) {
					Direction direction = null;
					if (DIRECTION_DESC.equalsIgnoreCase(field[1])) {
						direction = Direction.DESC;
					} else {
						direction = Direction.ASC;
					}
					orders.add(new Order(direction, field[0]));
				}
			} else {
				if (fieldExistsInTheEntity(fields[x])) {
					orders.add(new Order(Sort.DEFAULT_DIRECTION, fields[x]));
				}
			}
		}
		return Sort.by(orders);
	}

	@Override
	public boolean fieldExistsInTheEntity(String field) {
		boolean exists = Boolean.FALSE;
		for (java.beans.PropertyDescriptor pd : getPropertyDescriptor()) {
			if (!pd.getReadMethod().isAnnotationPresent(Transient.class) && pd.getName().equalsIgnoreCase(field)) {
				exists = Boolean.TRUE;
				break;
			}
		}
		return exists;
	}

	@Override
	public Map<String, String> getFilterValues(Map<String, String> filters) {
		Map<String, String> filterValues = new TreeMap<>();
		for (java.beans.PropertyDescriptor pd : getPropertyDescriptor()) {
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
		Class<?> _class = null;
		for (java.beans.PropertyDescriptor pd : getPropertyDescriptor()) {
			if (pd.getName().equalsIgnoreCase(field)) {
				_class = pd.getPropertyType();
			}
		}
		return _class;
	}

	@Override
	public Criteria getCriteriaByFilter(Criteria criteria, final String filter, final Object value) {
		return getCriteriaByFilter(criteria, filter, value, null);
	}

	@Override
	public Criteria getCriteriaByFilter(Criteria criteria, final String filter, final Object value, String regex) {
		if (regex != null && value instanceof String) {
			return criteria.regex(String.format(regex, value));
		} else if (filter.equalsIgnoreCase(IConstants.GREATER_THAN)) {
			return criteria.gt(value);
		} else if (filter.equalsIgnoreCase(IConstants.LESS_THAN)) {
			return criteria.lt(value);
		} else if (filter.equalsIgnoreCase(IConstants.GREATER_THAN_OR_EQUAL)) {
			return criteria.gte(value);
		} else if (filter.equalsIgnoreCase(IConstants.LESS_THAN_OR_EQUAL)) {
			return criteria.lte(value);
		} else if (filter.equalsIgnoreCase(IConstants.EQ)) {
			return criteria.is(value);
		} else if (filter.equalsIgnoreCase(IConstants.CONTAINS)) {
			return criteria.regex(String.format(IConstants.REGEX_CONTAINS, value));
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
			bsonList.add(getCriteriaByFilter(criteria, filters[0], getValueByFieldType(field, filters[1]), regex)
					.getCriteriaObject());
			bsonList.add(getCriteriaByFilter(criteria, filters[2], getValueByFieldType(field, filters[3]), regex)
					.getCriteriaObject());
			return new Criteria(IConstants.AND).is(bsonList);
		} else {
			return criteria.is(getValueByFieldType(field, value));
		}
	}

}

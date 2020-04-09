package br.com.virtuallibrary.commons.services;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.validation.ValidationException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.security.core.userdetails.UserDetails;

import br.com.virtuallibrary.commons.entities.BaseEntity;
import br.com.virtuallibrary.commons.repositories.IBaseRepository;

public interface IBaseService<E extends BaseEntity, ID extends Serializable, R extends IBaseRepository<E, ID>> {

	List<E> findAll();
	
	List<E> findAll(Sort sort);

	Page<E> findPaginated(final int page, final int size);
	
	Page<E> findPaginated(int page, int size, Map<String, String> filters);
	
	Page<E> findAll(Query query, int page, int size);
	
	Page<E> findAll(Query query, int page, int size, List<Criteria> criterias);
	
	Page<E> findAll(Query query, int page, int size, List<Criteria> criterias, Sort sort);
	
	Page<E> getPage(List<E> results, Pageable pageable, Query query, List<Criteria> criterias);
	
	Optional<E> findById(final ID id);

	Optional<E> save(E object);

	void delete(final ID id);

	Optional<E> update(Map<String, String> updates, final ID id) throws ValidationException, SecurityException, IllegalArgumentException, IllegalAccessException;

	Optional<E> update(E object, final ID id);

	UserDetails getUser();

	MongoTemplate getTemplate();

	Map<String, String> getFilterValues(Map<String, String> filters);

	Criteria getCriteriaByFilter(Criteria criteria, String filter, Object value);

	Criteria getCriteriaByFilter(Criteria criteria, String filter, Object value, String regex);

	Criteria createCriteriaByFilter(Query query, String field, String value, String regex);

	Criteria createCriteriaByFilter(Query query, String field, String value);

	Sort getSort(Map<String, String> filters);

	Class<?> getFieldType(String field);

}

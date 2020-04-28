package br.com.virtuallibrary.commons.services;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import br.com.virtuallibrary.commons.entities.BaseEntity;
import br.com.virtuallibrary.commons.repositories.IBaseRepository;

public interface ISearchService<E extends BaseEntity, K extends Serializable, R extends IBaseRepository<E, K>>
	extends IBaseService<E, K, R> {

	List<E> findAll();
	
	List<E> findAll(Sort sort);

	Page<E> findPaginated(final int page, final int size);
	
	Page<E> findPaginated(int page, int size, Map<String, String> filters);
	
	Page<E> findAll(Query query, int page, int size);
	
	Page<E> findAll(Query query, int page, int size, List<Criteria> criterias);
	
	Page<E> findAll(Query query, int page, int size, List<Criteria> criterias, Sort sort);
	
	Page<E> getPage(List<E> results, Pageable pageable, Query query, List<Criteria> criterias);
	
	Map<String, String> getFilterValues(Map<String, String> filters);

	Criteria getCriteriaByFilter(Criteria criteria, String filter, Object value);

	Criteria getCriteriaByFilter(Criteria criteria, String filter, Object value, String regex);

	Criteria createCriteriaByFilter(Query query, String field, String value, String regex);

	Criteria createCriteriaByFilter(Query query, String field, String value);

	Sort getSort(Map<String, String> filters);

	Class<?> getFieldType(String field);

	/**
	 * Checks if the informed field exists in the current entity.
	 * @param field The field to be verified.
	 * @return <code>True</code> if the field exists in the entity. <code>False</code> if it does not exist.
	 */
	boolean fieldExistsInTheEntity(String field);

}

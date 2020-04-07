package br.com.virtuallibrary.commons.services;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.validation.ValidationException;

import org.springframework.data.domain.Page;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.security.core.userdetails.UserDetails;

import br.com.virtuallibrary.commons.entities.BaseEntity;
import br.com.virtuallibrary.commons.repositories.IBaseRepository;

public interface IBaseService<E extends BaseEntity, ID extends Serializable, R extends IBaseRepository<E, ID>> {

	List<E> findAll();

	/**
	 * and – operador lógico AND
	 * or – operador lógico OR
	 * not – operador lógico NOT
	 * gt = maior que
	 * lt = menor que
	 * gte = maior ou igual
	 * lte = menor ou igual
	 * ne = diferente de
	 * in = todos os documentos cujo atributo possui um dos valores especificados (no SQL operador IN)
	 * nin = todos os documentos cujo atributo não possui um dos valores especificados (no SQL operador NOT IN)
	 * @param page A página a ser pesquisada.
	 * @param size A quantidade de registro por página.
	 * @return O resultado paginado.
	 */
	Page<E> findPaginated(int page, int size);
	
	Optional<E> findById(ID id);

	Optional<E> save(E object);

	void delete(ID id);

	Optional<E> update(Map<String, String> updates, ID id) throws ValidationException, SecurityException, IllegalArgumentException, IllegalAccessException;

	Optional<E> update(E object, ID id);

	UserDetails getUser();

	MongoTemplate getTemplate();

	Criteria getCriteriaByFilter(Query query, String filter, Object value);

	Page<E> findAll(Query query, int page, int size);

}

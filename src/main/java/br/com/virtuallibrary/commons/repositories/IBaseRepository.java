package br.com.virtuallibrary.commons.repositories;

import java.io.Serializable;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.query.QueryByExampleExecutor;

import br.com.virtuallibrary.commons.entities.BaseEntity;

/**
 * O repositório base da aplicação.
 *
 * @param <E>  A entidade.
 * @param <ID> O tipo da identificação das entidades.
 */
@NoRepositoryBean
public interface IBaseRepository<E extends BaseEntity, ID extends Serializable> extends MongoRepository<E, ID>, QueryByExampleExecutor<E> {
}

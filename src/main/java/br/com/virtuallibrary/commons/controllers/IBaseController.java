package br.com.virtuallibrary.commons.controllers;

import java.io.Serializable;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.http.ResponseEntity;

import br.com.virtuallibrary.commons.entities.BaseEntity;
import br.com.virtuallibrary.commons.repositories.IBaseRepository;
import br.com.virtuallibrary.commons.services.IBaseService;

/**
 * Recurso básico com endpoints de CRUD.
 * @author Daniel Oliveira
 *
 * @param <E> Representa a entidade.
 * @param <ID> Representa o tipo identificador da entidade.
 * @param <R> Representa o repositório do entidade.
 * @param <S> Representa o serviço da entidade.
 */
public interface IBaseController<E extends BaseEntity, ID extends Serializable, R extends IBaseRepository<E, ID>, S extends IBaseService<E, ID, R>, M extends RepresentationModel<M>> {

	S getService();

	RepresentationModelAssemblerSupport<E, M> getModelAssembler();

	ResponseEntity<M> find(ID id);

	ResponseEntity<M> create(@Valid E object);

	ResponseEntity<Object> delete(ID id);

	ResponseEntity<M> update(@Valid E object, ID id);

	ResponseEntity<M> update(Map<String, String> updates, ID id) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException;

}

package br.com.virtuallibrary.commons.controllers;

import java.io.Serializable;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import br.com.virtuallibrary.commons.IConstants;
import br.com.virtuallibrary.commons.entities.BaseEntity;
import br.com.virtuallibrary.commons.repositories.IBaseRepository;
import br.com.virtuallibrary.commons.services.ISaveAndUpdateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

/**
 * Controle básico de save e update de registro.
 * @author Daniel Oliveira
 *
 * @param <E> Representa a entidade.
 * @param <K> Representa o tipo identificador da entidade.
 * @param <R> Representa o repositório do entidade.
 * @param <S> Representa o serviço da entidade.
 */
public interface ISaveAndUpdateController<
		E extends BaseEntity, 
		K extends Serializable, 
		R extends IBaseRepository<E, K>, 
		S extends ISaveAndUpdateService<E, K, R>, 
		M extends RepresentationModel<M>>
    extends IDeleteController <E, K, R, S, M> {

	@Override
	S getService();

	@Override
	PagedResourcesAssembler<E> getPagedResourcesAssembler();
	
	@Override
	RepresentationModelAssemblerSupport<E, M> getModelAssembler();

	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping(produces = { IConstants.APPLICATION_JSON_UTF_8, IConstants.APPLICATION_XML_UTF_8 })
	@Operation(summary = "Adiciona um novo registro.", description = "Será gravado no banco de dados um novo registro.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "Registro criado com sucesso"),
			@ApiResponse(responseCode = "404", description = "Não foi possível cadastrar o registro."),
			@ApiResponse(responseCode = "500", description = "Erro interno do servidor") })
	ResponseEntity<M> create(
			@Parameter(description="Registro a ser adicionado. Não pode ser nulo ou vazio.", required=true)
			@Valid E object);

	@ResponseStatus(HttpStatus.OK)
	@PutMapping(value = "/{id}", produces = { IConstants.APPLICATION_JSON_UTF_8, IConstants.APPLICATION_XML_UTF_8 })
	@Operation(summary = "Atualizar um registro", description = "Atualiza um registro existente.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Registro atualizado com sucesso"),
			@ApiResponse(responseCode = "404", description = "Registro não encontrado."),
			@ApiResponse(responseCode = "500", description = "Erro interno do servidor") })
	ResponseEntity<M> update(
			@Parameter(description="O registro a ser atualizado.", required=true) @Valid E object,
			@Parameter(description="Id do registro a ser atualizado. Não pode ser vazio.", required=true) K id);

	@ResponseStatus(HttpStatus.OK)
	@PatchMapping(value = "/{id}", produces = { IConstants.APPLICATION_JSON_UTF_8, IConstants.APPLICATION_XML_UTF_8 })
	@Operation(summary = "Atualizar campos do registro", description = "Atualiza os campo do registro passado por um Map.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Registro atualizado com sucesso"),
			@ApiResponse(responseCode = "404", description = "Registro não encontrado."),
			@ApiResponse(responseCode = "500", description = "Erro interno do servidor") })
	ResponseEntity<M> update(
			@Parameter(description="Os campos a serem atualizado no registro.", required=true) Map<String, String> updates,
			@Parameter(description="Id do registro a ser atualizado. Não pode ser vazio.", required=true) K id)
		throws NoSuchFieldException, IllegalAccessException;

}

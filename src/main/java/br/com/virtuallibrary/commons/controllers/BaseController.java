package br.com.virtuallibrary.commons.controllers;

import java.io.Serializable;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import br.com.virtuallibrary.commons.Constants;
import br.com.virtuallibrary.commons.entities.BaseEntity;
import br.com.virtuallibrary.commons.repositories.BaseRepository;
import br.com.virtuallibrary.commons.services.BaseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;

/**
 * Recurso básico com endpoints de CRUD.
 * @author Daniel Oliveira
 *
 * @param <E> Representa a entidade.
 * @param <ID> Representa o tipo identificador da entidade.
 * @param <R> Representa o repositório do entidade.
 * @param <S> Representa o serviço da entidade.
 */
@Slf4j
public class BaseController<E extends BaseEntity, ID extends Serializable, R extends BaseRepository<E, ID>, S extends BaseService<E, ID, R>, M extends RepresentationModel<M>> {

	private final S service;
	private final RepresentationModelAssemblerSupport<E, M> modelAssembler;

	public BaseController(S service, RepresentationModelAssemblerSupport<E, M> modelAssembler) {
		this.service = service;
		this.modelAssembler = modelAssembler;
	}

	public final S getService() {
		return service;
	}
	
	public RepresentationModelAssemblerSupport<E, M> getModelAssembler() {
		return this.modelAssembler;
	}

	@ResponseStatus(HttpStatus.OK)
	@GetMapping(value = "/{id}", produces = { Constants.APPLICATION_JSON_UTF_8, Constants.APPLICATION_XML_UTF_8 })
	@Operation(summary = "Obter um registro pelo id.", description = "Retorna um registro.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Registro carregado com sucesso."),
			@ApiResponse(responseCode = "404", description = "Registro não encontrado."),
			@ApiResponse(responseCode = "500", description = "Erro interno do servidor") })
	public ResponseEntity<M> find(
			@Parameter(description="O id do registro a ser obtido. Não pode ser vazio.", required=true) @PathVariable ID id) {
		return service.findById(id) 
		.map(modelAssembler::toModel) 
		.map(ResponseEntity::ok) 
		.orElse(ResponseEntity.notFound().build());
	}

	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping(produces = { Constants.APPLICATION_JSON_UTF_8, Constants.APPLICATION_XML_UTF_8 })
	@Operation(summary = "Adiciona um novo registro.", description = "Será gravado no banco de dados um novo registro.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "Registro criado com sucesso"),
			@ApiResponse(responseCode = "404", description = "Não foi possível cadastrar o registro."),
			@ApiResponse(responseCode = "500", description = "Erro interno do servidor") })
	public ResponseEntity<M> create(
			@Parameter(description="Registro a ser adicionado. Não pode ser nulo ou vazio.", required=true)
			@RequestBody @Valid E object) {
		return service.save(object)
				.map(modelAssembler::toModel) 
				.map(entity -> ResponseEntity.status(HttpStatus.CREATED).body(entity)) 
				.orElse(ResponseEntity.notFound().build());
	}

	@ResponseStatus(HttpStatus.OK)
	@DeleteMapping(value = "/{id}", produces = { Constants.APPLICATION_JSON_UTF_8, Constants.APPLICATION_XML_UTF_8 })
	@Operation(summary = "Deleta um registro.", description = "Remove o registro da base de dados.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Registro deletado com sucesso"),
			@ApiResponse(responseCode = "404", description = "Registro não encontrado."),
			@ApiResponse(responseCode = "500", description = "Erro interno do servidor") })
	public ResponseEntity<Object> delete(
			@Parameter(description="Id do registro a ser deletado. Não pode ser vazio.",
            required=true)
			@PathVariable ID id) {
		return service.findById(id).map(entity -> {
			service.delete(id);
			return ResponseEntity.ok().build();
		}).orElse(ResponseEntity.notFound().build());
	}

	@ResponseStatus(HttpStatus.OK)
	@PutMapping(value = "/{id}", produces = { Constants.APPLICATION_JSON_UTF_8, Constants.APPLICATION_XML_UTF_8 })
	@Operation(summary = "Atualizar um registro", description = "Atualiza um registro existente.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Registro atualizado com sucesso"),
			@ApiResponse(responseCode = "404", description = "Registro não encontrado."),
			@ApiResponse(responseCode = "500", description = "Erro interno do servidor") })
	public ResponseEntity<M> update(
			@Parameter(description="O registro a ser atualizado.", required=true)
			@RequestBody @Valid E object,
			@Parameter(description="Id do registro a ser atualizado. Não pode ser vazio.", required=true)
			@PathVariable ID id) {
		return service.update(object, id)
				.map(modelAssembler::toModel) 
				.map(ResponseEntity::ok) 
				.orElse(ResponseEntity.notFound().build());

	}

	@ResponseStatus(HttpStatus.OK)
	@PatchMapping(value = "/{id}", produces = { Constants.APPLICATION_JSON_UTF_8, Constants.APPLICATION_XML_UTF_8 })
	@Operation(summary = "Atualizar campos do registro", description = "Atualiza os campo do registro passado por um Map.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Registro atualizado com sucesso"),
			@ApiResponse(responseCode = "404", description = "Registro não encontrado."),
			@ApiResponse(responseCode = "500", description = "Erro interno do servidor") })
	public ResponseEntity<M> update(
			@Parameter(description="Os campos a serem atualizado no registro.", required=true)
			@RequestBody Map<String, String> updates,
			@Parameter(description="Id do registro a ser atualizado. Não pode ser vazio.", required=true)
			@PathVariable ID id) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		try {
			return service.update(updates, id)
			.map(modelAssembler::toModel) 
			.map(ResponseEntity::ok) 
			.orElse(ResponseEntity.notFound().build());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return ResponseEntity.notFound().build();
		}
	}

}
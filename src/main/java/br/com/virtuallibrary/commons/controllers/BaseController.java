package br.com.virtuallibrary.commons.controllers;

import java.io.Serializable;
import java.util.Map;

import javax.validation.Valid;

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
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * Recurso básico com endpoints de CRUD.
 * @author daniel
 *
 * @param <E> Representa a entidade.
 * @param <ID> Representa o tipo identificador da entidade.
 * @param <R> Representa o repositório do entidade.
 * @param <S> Representa o serviço da entidade.
 */
public class BaseController<E extends BaseEntity, ID extends Serializable, R extends BaseRepository<E, ID>, S extends BaseService<E, ID, R>> {

	private final S service;

	public BaseController(S service) {
		this.service = service;
	}

	public S getService() {
		return service;
	}

	@ResponseStatus(HttpStatus.OK)
	@GetMapping(value = "/{id}", produces = { Constants.APPLICATION_JSON_UTF_8, Constants.APPLICATION_XML_UTF_8 })
	@ApiOperation(value = "Obter registro pelo identificador", notes = "Será retornado um registro da base de dados.")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Registro carregado com sucesso."),
			@ApiResponse(code = 404, message = "Registro não encontrado."),
			@ApiResponse(code = 500, message = "Erro interno do servidor") })
	public ResponseEntity<E> find(@PathVariable ID id) {
		return service.findById(id).map(entity -> ResponseEntity.ok().body(entity))
				.orElse(ResponseEntity.notFound().build());
	}

	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping(produces = { Constants.APPLICATION_JSON_UTF_8, Constants.APPLICATION_XML_UTF_8 })
	@ApiOperation(value = "Salvar um novo registro", notes = "Cria um novo registro na base de dados.")
	@ApiResponses(value = { @ApiResponse(code = 201, message = "Registro criado com sucesso"),
			@ApiResponse(code = 404, message = "Não foi possível cadastrar o registro."),
			@ApiResponse(code = 500, message = "Erro interno do servidor") })
	public ResponseEntity<E> create(@RequestBody @Valid E object) {
		return service.save(object).map(entity -> ResponseEntity.status(HttpStatus.CREATED).body(entity))
				.orElse(ResponseEntity.notFound().build());
	}

	@ResponseStatus(HttpStatus.OK)
	@DeleteMapping(value = "/{id}", produces = { Constants.APPLICATION_JSON_UTF_8, Constants.APPLICATION_XML_UTF_8 })
	@ApiOperation(value = "Deletar um registro")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Registro deletado com sucesso"),
			@ApiResponse(code = 404, message = "Registro não encontrado."),
			@ApiResponse(code = 500, message = "Erro interno do servidor") })
	public ResponseEntity<Object> delete(@PathVariable ID id) {
		return service.findById(id).map(entity -> {
			service.delete(id);
			return ResponseEntity.ok().build();
		}).orElse(ResponseEntity.notFound().build());
	}

	@ResponseStatus(HttpStatus.OK)
	@PutMapping(value = "/{id}", produces = { Constants.APPLICATION_JSON_UTF_8, Constants.APPLICATION_XML_UTF_8 })
	@ApiOperation(value = "Atualizar um registro", notes = "Atualiza um registro na base de dados.")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Registro atualizado com sucesso"),
			@ApiResponse(code = 404, message = "Registro não encontrado."),
			@ApiResponse(code = 500, message = "Erro interno do servidor") })
	public ResponseEntity<E> update(@RequestBody @Valid E object, @PathVariable ID id) {
		return service.update(object, id).map(entity -> ResponseEntity.ok().body(entity))
				.orElse(ResponseEntity.notFound().build());

	}

	@ResponseStatus(HttpStatus.OK)
	@PatchMapping(value = "/{id}", produces = { Constants.APPLICATION_JSON_UTF_8, Constants.APPLICATION_XML_UTF_8 })
	@ApiOperation(value = "Atualizar campos específicos de um registro.", notes = "Atualiza um registro na base de dados.")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Registro atualizado com sucesso"),
			@ApiResponse(code = 404, message = "Registro não encontrado."),
			@ApiResponse(code = 500, message = "Erro interno do servidor") })
	public ResponseEntity<E> update(@RequestBody Map<String, String> updates, @PathVariable ID id) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		return service.update(updates, id).map(entity -> ResponseEntity.ok().body(entity))
				.orElse(ResponseEntity.notFound().build());
	}

}
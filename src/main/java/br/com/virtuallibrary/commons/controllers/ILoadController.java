package br.com.virtuallibrary.commons.controllers;

import java.io.Serializable;

import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import br.com.virtuallibrary.commons.IConstants;
import br.com.virtuallibrary.commons.entities.BaseEntity;
import br.com.virtuallibrary.commons.repositories.IBaseRepository;
import br.com.virtuallibrary.commons.services.ILoadService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

/**
 * Controle básico de load de registro.
 * @author Daniel Oliveira
 *
 * @param <E> Representa a entidade.
 * @param <ID> Representa o tipo identificador da entidade.
 * @param <R> Representa o repositório do entidade.
 * @param <S> Representa o serviço da entidade.
 */
public interface ILoadController<
		E extends BaseEntity, 
		ID extends Serializable, 
		R extends IBaseRepository<E, ID>, 
		S extends ILoadService<E, ID, R>, 
		M extends RepresentationModel<M>>
    extends ISearchController<E, ID, R, S, M> {

	@Override
	S getService();

	@Override
	PagedResourcesAssembler<E> getPagedResourcesAssembler();
	
	@Override
	RepresentationModelAssemblerSupport<E, M> getModelAssembler();

	@ResponseStatus(HttpStatus.OK)
	@GetMapping(value = "/{id}", produces = { IConstants.APPLICATION_JSON_UTF_8, IConstants.APPLICATION_XML_UTF_8 })
	@Operation(summary = "Obter um registro pelo id.", description = "Retorna um registro.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Registro carregado com sucesso."),
			@ApiResponse(responseCode = "404", description = "Registro não encontrado."),
			@ApiResponse(responseCode = "500", description = "Erro interno do servidor") })
	ResponseEntity<M> find(@Parameter(description="O id do registro a ser obtido. Não pode ser vazio.", required=true) ID id);

}

package br.com.virtuallibrary.commons.controllers;

import java.io.Serializable;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import br.com.virtuallibrary.commons.IConstants;
import br.com.virtuallibrary.commons.entities.BaseEntity;
import br.com.virtuallibrary.commons.repositories.IBaseRepository;
import br.com.virtuallibrary.commons.services.IBaseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

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

	PagedResourcesAssembler<E> getPagedResourcesAssembler();
	
	RepresentationModelAssemblerSupport<E, M> getModelAssembler();

	@ResponseStatus(HttpStatus.OK)
	@GetMapping(produces = { IConstants.APPLICATION_JSON_UTF_8, IConstants.APPLICATION_XML_UTF_8 })
	@Operation(summary = "Retorna a lista de registros paginado.", description =
		      "O filtro padrão é o igual ($eq), mas você pode utilizar (Exemplo: field=contains:valor):<br/>"
		    + "Contém - \"<b>contains</b>:valor\"<br/>"
		    + "Igual - \"<b>eq</b>:valor\"<br/>"
		    + "Maior que - \"<b>gt</b>:numerico\"<br/>"
		    + "Menor que - \"<b>lt</b>:numerico\"<br/>"
		    + "Maior ou igual - \"<b>gte</b>:numerico\"<br/>"
		    + "Menor ou igual - \"<b>lte</b>:numerico\"<br/>"
		    + "Também é possível combinar dois filtros - <b>gt</b>:numerico:<b>lt</b>:numerico<br/>"
		    + "Para ordenação, usar no parâmetro: <b>orderby=field1,field2,field3</b><br/>"
		    + "Outro exemplo: <b>orderby=field1:asc,field2:desc</b>")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Registros listados com sucesso"),
			@ApiResponse(responseCode = "400", description = "Erro na obtenção dos dados ou filtro"),
			@ApiResponse(responseCode = "500", description = "Erro interno do servidor")})
	ResponseEntity<CollectionModel<M>> findAll(
			@Parameter(description="Número da página.")  int page,
			@Parameter(description="Quantidade de registros por página.") int size,
			@Parameter(description="Filtros de pesquisa conforme campos da entidade.") Map<String, String> filters);
	
	@ResponseStatus(HttpStatus.OK)
	@GetMapping(value = "/{id}", produces = { IConstants.APPLICATION_JSON_UTF_8, IConstants.APPLICATION_XML_UTF_8 })
	@Operation(summary = "Obter um registro pelo id.", description = "Retorna um registro.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Registro carregado com sucesso."),
			@ApiResponse(responseCode = "404", description = "Registro não encontrado."),
			@ApiResponse(responseCode = "500", description = "Erro interno do servidor") })
	ResponseEntity<M> find(@Parameter(description="O id do registro a ser obtido. Não pode ser vazio.", required=true) ID id);

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
	@DeleteMapping(value = "/{id}", produces = { IConstants.APPLICATION_JSON_UTF_8, IConstants.APPLICATION_XML_UTF_8 })
	@Operation(summary = "Deleta um registro.", description = "Remove o registro da base de dados.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Registro deletado com sucesso"),
			@ApiResponse(responseCode = "404", description = "Registro não encontrado."),
			@ApiResponse(responseCode = "500", description = "Erro interno do servidor") })
	ResponseEntity<Object> delete(@Parameter(description="Id do registro a ser deletado. Não pode ser vazio.", required=true) ID id);

	@ResponseStatus(HttpStatus.OK)
	@PutMapping(value = "/{id}", produces = { IConstants.APPLICATION_JSON_UTF_8, IConstants.APPLICATION_XML_UTF_8 })
	@Operation(summary = "Atualizar um registro", description = "Atualiza um registro existente.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Registro atualizado com sucesso"),
			@ApiResponse(responseCode = "404", description = "Registro não encontrado."),
			@ApiResponse(responseCode = "500", description = "Erro interno do servidor") })
	ResponseEntity<M> update(
			@Parameter(description="O registro a ser atualizado.", required=true) @Valid E object,
			@Parameter(description="Id do registro a ser atualizado. Não pode ser vazio.", required=true) ID id);

	@ResponseStatus(HttpStatus.OK)
	@PatchMapping(value = "/{id}", produces = { IConstants.APPLICATION_JSON_UTF_8, IConstants.APPLICATION_XML_UTF_8 })
	@Operation(summary = "Atualizar campos do registro", description = "Atualiza os campo do registro passado por um Map.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Registro atualizado com sucesso"),
			@ApiResponse(responseCode = "404", description = "Registro não encontrado."),
			@ApiResponse(responseCode = "500", description = "Erro interno do servidor") })
	ResponseEntity<M> update(
			@Parameter(description="Os campos a serem atualizado no registro.", required=true) Map<String, String> updates,
			@Parameter(description="Id do registro a ser atualizado. Não pode ser vazio.", required=true) ID id)
		throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException;

}

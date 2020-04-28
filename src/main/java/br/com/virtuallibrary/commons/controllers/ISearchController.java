package br.com.virtuallibrary.commons.controllers;

import java.io.Serializable;
import java.util.Map;

import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import br.com.virtuallibrary.commons.IConstants;
import br.com.virtuallibrary.commons.entities.BaseEntity;
import br.com.virtuallibrary.commons.repositories.IBaseRepository;
import br.com.virtuallibrary.commons.services.ISearchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

/**
 * Controle básico de pesquisa.
 * @author Daniel Oliveira
 *
 * @param <E> Representa a entidade.
 * @param <K> Representa o tipo identificador da entidade.
 * @param <R> Representa o repositório do entidade.
 * @param <S> Representa o serviço da entidade.
 */
public interface ISearchController<
		E extends BaseEntity, 
		K extends Serializable, 
		R extends IBaseRepository<E, K>, 
		S extends ISearchService<E, K, R>, 
		M extends RepresentationModel<M>>
    extends IBaseController<E, K, R, S, M> {

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
	
}

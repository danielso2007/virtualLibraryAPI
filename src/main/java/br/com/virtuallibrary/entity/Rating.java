package br.com.virtuallibrary.entity;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonRootName;

import br.com.virtuallibrary.commons.entities.BaseAudit;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Document
@JsonRootName(value = "rating")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(value = Include.NON_NULL)
@Validated
@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true, of = { "bookId", "stars" })
@ToString(callSuper = true, of = { "bookId", "stars" })
@SuperBuilder
public class Rating extends BaseAudit {

	private static final long serialVersionUID = -526524160197083642L;

	@NotEmpty(message = "O código do livro não pode ser vazio.")
	@NotNull(message = "O código de livro não pode ser nulo.")
	@Schema(description = "O livro associado a essa classificação.", required = false, example = "5e49dcc31010b00b3383f8b6")
	private String bookId;
	@NotNull(message = "Starts não pode ser nulo.")
	@Min(value = 0, message = "O valor mínimo estrelas é 0.")
	@Max(value = 5, message = "O máximo de estrelas é 5.")
	@Schema(description = "O nível da classificação de um livro.", required = true, example = "5")
	private Integer stars;

}

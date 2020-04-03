package br.com.virtuallibrary.entity;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
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
@JsonRootName(value = "book")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(value = Include.NON_NULL)
@Validated
@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true, of = { "title", "author" })
@ToString(callSuper = true, of = { "title", "author" })
@SuperBuilder
public class Book extends BaseAudit {

	private static final long serialVersionUID = -2201621112079525990L;

	@NotEmpty(message = "O título não pode ser vazio.")
	@NotNull(message = "O título não pode ser nulo.")
	@Length(max = 300, message = "O título pode ter no máximo 300 caracteres.")
	@Schema(description = "O título do livro.", required = true, example = "Fernando Pessoa")
	private String title;
	@NotEmpty(message = "O nome do autor não pode ser vazio.")
	@NotNull(message = "O nome do autor não pode ser nulo.")
	@Length(max = 150, message = "O nome do autor só pode ter no máximo 150 caracteres.")
	@Schema(description = "O nome do autor.", required = true, example = "Ficções de interlúdio")
	private String author;

}

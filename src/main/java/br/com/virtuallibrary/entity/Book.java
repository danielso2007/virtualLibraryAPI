package br.com.virtuallibrary.entity;

import javax.validation.constraints.NotEmpty;

import org.hibernate.validator.constraints.Length;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import br.com.virtuallibrary.commons.entities.BaseAudit;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Document
@JsonIgnoreProperties(ignoreUnknown = true)
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
	@Length(max = 300, message = "O nome do autor só pode ter no máximo 300 caracteres.")
	@ApiModelProperty(notes = "O título do livro", example = "The XPTO")
	private String title;
	@NotEmpty(message = "O nome do autor não pode ser vazio.")
	@Length(max = 150, message = "O nome do autor só pode ter no máximo 300 caracteres.")
	@ApiModelProperty(notes = "O nome do autor", example = "Fulano da Silva")
	private String author;

}

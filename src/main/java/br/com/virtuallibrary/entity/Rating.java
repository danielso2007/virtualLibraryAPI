package br.com.virtuallibrary.entity;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

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

	@NotEmpty
	@ApiModelProperty(notes = "O identificado do livro", example = "5dc4c9734e9b1214ed7a9e3a")
	private String bookId;
	@Min(value = 0, message = "O valor mínimo estrelas é 0.")
	@Max(value = 5, message = "O máximo de estrelas é 5.")
	@ApiModelProperty(notes = "A avaliação do livro", example = "Valor inteiro")
	private int stars;

}

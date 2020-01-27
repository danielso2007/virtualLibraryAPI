package br.com.virtuallibrary.entity;

import org.springframework.data.mongodb.core.mapping.Document;

import br.com.virtuallibrary.commons.entities.BaseAudit;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Document
@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true, of = { "bookId", "stars" })
@ToString(callSuper = true, of = { "bookId", "stars" })
@SuperBuilder
public class Rating extends BaseAudit {

	private static final long serialVersionUID = -526524160197083642L;

	@ApiModelProperty(notes = "O identificado do livro", example = "5dc4c9734e9b1214ed7a9e3a")
	private String bookId;
	@ApiModelProperty(notes = "A avaliação do livro", example = "Valor inteiro")
	private int stars;

}

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
@EqualsAndHashCode(callSuper = true, of = { "title", "author" })
@ToString(callSuper = true, of = { "title", "author" })
@SuperBuilder
public class Book extends BaseAudit {

	private static final long serialVersionUID = -2201621112079525990L;

	@ApiModelProperty(notes = "O título do livro", example = "The XPTO")
	private String title;
	@ApiModelProperty(notes = "O nome do autor", example = "Fulano da Silva")
	private String author;

}

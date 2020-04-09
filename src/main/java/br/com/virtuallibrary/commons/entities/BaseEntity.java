package br.com.virtuallibrary.commons.entities;

import java.io.Serializable;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Document
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = false, of = { "id" })
@SuperBuilder
public abstract class BaseEntity implements Serializable {

	private static final long serialVersionUID = -4185303902863688416L;

	@Id
	@Schema(description = "O identificador do registro.", example = "5e49dcc31010b00b3383f8b6")
	private String id;

}

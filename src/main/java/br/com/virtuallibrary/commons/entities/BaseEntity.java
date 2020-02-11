package br.com.virtuallibrary.commons.entities;

import java.io.Serializable;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Document
@NoArgsConstructor
@Data
@EqualsAndHashCode(of = { "id" })
@SuperBuilder
public abstract class BaseEntity implements Serializable {

	private static final long serialVersionUID = -4185303902863688416L;

	@Id
	@ApiModelProperty(notes = "O identificado do registro", example = "5dc4c9734e9b1214ed7a9e3a")
	private String id;

}

package br.com.virtuallibrary.commons.entities;

import java.util.Date;

import org.springframework.data.mongodb.core.mapping.Document;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Document
@NoArgsConstructor
@AllArgsConstructor
@Data()
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public abstract class BaseAudit extends BaseEntity {

	private static final long serialVersionUID = -6536341488318207281L;

	@ApiModelProperty(notes = "A data de criação do registro. Não será permitido criar ou atualizar", example = "2019-11-08T12:00:00.212Z")
	private Date createdAt;
	@ApiModelProperty(notes = "O login do criado do registro. Não será permitido criar ou atualizar", example = "system")
	private String creator;
	@ApiModelProperty(notes = "A data de atualização do registro. Não será permitido criar ou atualizar", example = "2019-11-08T12:00:00.212Z")
	private Date updatedAt;
	@ApiModelProperty(notes = "O login do atualizador do registro. Não será permitido criar ou atualizar", example = "system")
	private String updater;

}

package br.com.virtuallibrary.commons.entities;

import java.util.Date;

import org.springframework.data.mongodb.core.mapping.Document;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Document
@NoArgsConstructor
@Data()
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public abstract class BaseAudit extends BaseEntity {

	private static final long serialVersionUID = -6536341488318207281L;

	@Schema(description = "A data de criação do registro.", example = "2020-02-16 21:22:27")
	private Date createdAt;
	@Schema(description = "O login de quem criou o registro.", example = "admin")
	private String creator;
	@Schema(description = "A data de atualização do registro.", example = "2020-02-16 21:22:27")
	private Date updatedAt;
	@Schema(description = "O login de quem atualizou o registro.", example = "admin")
	private String updater;

}

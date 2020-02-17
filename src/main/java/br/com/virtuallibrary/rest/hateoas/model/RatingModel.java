package br.com.virtuallibrary.rest.hateoas.model;

import java.util.Date;

import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Relation(collectionRelation = "ratings", itemRelation = "rating")
@JsonInclude(Include.NON_NULL)
public class RatingModel extends RepresentationModel<RatingModel> {

	private String id;
	private String bookId;
	private int stars;
	private Date createdAt;
	private String creator;
	private Date updatedAt;
	private String updater;
	
}

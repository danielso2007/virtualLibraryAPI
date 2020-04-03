package br.com.virtuallibrary.rest.hateoas.model;

import java.util.Date;

import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonRootName;

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
@Relation(collectionRelation = "books", itemRelation = "book")
@JsonRootName(value = "bookModel")
@JsonInclude(Include.NON_NULL)
public class BookModel extends RepresentationModel<BookModel> {

	private String id;
	private String title;
	private String author;
	private Date createdAt;
	private String creator;
	private Date updatedAt;
	private String updater;
	
}

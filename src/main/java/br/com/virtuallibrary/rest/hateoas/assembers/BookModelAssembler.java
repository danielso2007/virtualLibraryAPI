package br.com.virtuallibrary.rest.hateoas.assembers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import br.com.virtuallibrary.entity.Book;
import br.com.virtuallibrary.rest.controllers.BookController;
import br.com.virtuallibrary.rest.hateoas.model.BookModel;

@Component
public class BookModelAssembler extends RepresentationModelAssemblerSupport<Book, BookModel> {

	public BookModelAssembler() {
		super(BookController.class, BookModel.class);
	}

	@Override
	public BookModel toModel(Book entity) {
		BookModel model = instantiateModel(entity);

		model.add(linkTo(methodOn(BookController.class).find(entity.getId())).withSelfRel());
		model.add(linkTo(methodOn(BookController.class).delete(entity.getId())).withRel("delete"));

		model.setId(entity.getId());
		model.setAuthor(entity.getAuthor());
		model.setTitle(entity.getTitle());
		model.setCreatedAt(entity.getCreatedAt());
		model.setCreator(entity.getCreator());
		model.setUpdatedAt(entity.getUpdatedAt());
		model.setUpdater(entity.getUpdater());

		return model;
	}

}

package br.com.virtuallibrary.services;

import java.util.List;

import br.com.virtuallibrary.commons.services.IBaseService;
import br.com.virtuallibrary.entity.Rating;
import br.com.virtuallibrary.repositories.RatingRepository;

public interface RatingService extends IBaseService<Rating, String, RatingRepository> {

	List<Rating> findByBookId(String bookId);

}

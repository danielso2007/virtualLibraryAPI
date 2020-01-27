package br.com.virtuallibrary.services;

import java.util.List;

import br.com.virtuallibrary.commons.services.BaseService;
import br.com.virtuallibrary.entity.Rating;
import br.com.virtuallibrary.repositories.RatingRepository;

public interface RatingService extends BaseService<Rating, String, RatingRepository> {

	List<Rating> findByBookId(String bookId);

}

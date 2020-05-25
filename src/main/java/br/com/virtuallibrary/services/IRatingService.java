package br.com.virtuallibrary.services;

import java.util.List;

import com.commons.rest.api.services.ISaveAndUpdateService;

import br.com.virtuallibrary.entity.Rating;
import br.com.virtuallibrary.repositories.RatingRepository;

public interface IRatingService extends ISaveAndUpdateService<Rating, String, RatingRepository> {

	List<Rating> findByBookId(String bookId);

}

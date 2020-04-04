package br.com.virtuallibrary.services;

import java.util.List;

import org.springframework.data.domain.Page;

import br.com.virtuallibrary.commons.services.IBaseService;
import br.com.virtuallibrary.entity.Rating;
import br.com.virtuallibrary.repositories.RatingRepository;

public interface RatingService extends IBaseService<Rating, String, RatingRepository> {

	List<Rating> findByBookId(String bookId);
	Page<Rating> findPaginated(String bookI, int page, int size);
}

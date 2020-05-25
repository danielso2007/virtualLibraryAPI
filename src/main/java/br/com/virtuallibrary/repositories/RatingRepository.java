package br.com.virtuallibrary.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.commons.rest.api.repositories.IBaseRepository;

import br.com.virtuallibrary.entity.Rating;

@Repository
public interface RatingRepository extends IBaseRepository<Rating, String> {
	
	List<Rating> findByBookId(String bookId);
	Page<Rating> findByBookId(String bookId, Pageable pageable);
	List<Rating> findByStarsBetween(int starsGT, int starsLT);
	
}

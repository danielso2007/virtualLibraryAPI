package br.com.virtuallibrary.repositories;

import java.util.List;

import org.springframework.stereotype.Repository;

import br.com.virtuallibrary.commons.repositories.BaseRepository;
import br.com.virtuallibrary.entity.Rating;

@Repository
public interface RatingRepository extends BaseRepository<Rating, String> {
	
	public List<Rating> findByBookId(String bookId);
	
}

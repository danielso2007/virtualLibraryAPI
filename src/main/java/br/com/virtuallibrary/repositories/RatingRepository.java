package br.com.virtuallibrary.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import br.com.virtuallibrary.commons.repositories.IBaseRepository;
import br.com.virtuallibrary.entity.Rating;

@Repository
public interface RatingRepository extends IBaseRepository<Rating, String> {
	
	public List<Rating> findByBookId(String bookId);
	public Page<Rating> findByBookId(String bookId, Pageable pageable);
	
}

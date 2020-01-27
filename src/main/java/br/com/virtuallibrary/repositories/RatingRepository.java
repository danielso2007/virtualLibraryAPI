package br.com.virtuallibrary.repositories;

import org.springframework.stereotype.Repository;

import br.com.virtuallibrary.commons.repositories.BaseRepository;
import br.com.virtuallibrary.entity.Rating;

@Repository
public interface RatingRepository extends BaseRepository<Rating, String> {
}

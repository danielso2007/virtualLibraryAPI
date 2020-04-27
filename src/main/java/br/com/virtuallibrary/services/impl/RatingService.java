package br.com.virtuallibrary.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.virtuallibrary.commons.services.impl.SaveAndUpdateService;
import br.com.virtuallibrary.entity.Rating;
import br.com.virtuallibrary.repositories.RatingRepository;
import br.com.virtuallibrary.services.IRatingService;

@Service
public class RatingService extends SaveAndUpdateService<Rating, String, RatingRepository> implements IRatingService {

	@Autowired
	public RatingService(RatingRepository repository) {
		super(repository);
	}

	@Override
	public List<Rating> findByBookId(String bookId) {
		return getRepository().findByBookId(bookId);
	}
	
}

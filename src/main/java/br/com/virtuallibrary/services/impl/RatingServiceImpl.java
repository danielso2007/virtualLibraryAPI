package br.com.virtuallibrary.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.virtuallibrary.commons.services.BaseServiceImpl;
import br.com.virtuallibrary.entity.Rating;
import br.com.virtuallibrary.repositories.RatingRepository;
import br.com.virtuallibrary.services.RatingService;

@Service
public class RatingServiceImpl extends BaseServiceImpl<Rating, String, RatingRepository> implements RatingService {

	@Autowired
	public RatingServiceImpl(RatingRepository repository) {
		super(repository);
	}

	@Override
	public List<Rating> findByBookId(String bookId) {
		return getRepository().findByBookId(bookId);
	}
	
	@Override
	public Page<Rating> findPaginated(String bookId, int page, int size) {
		Pageable pageable = PageRequest.of(page, size);
		if (bookId != null) {
			return getRepository().findByBookId(bookId, pageable);
		}
		return getRepository().findAll(pageable);
	}

}

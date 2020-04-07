package br.com.virtuallibrary.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.repository.support.PageableExecutionUtils;
import org.springframework.stereotype.Service;

import com.mongodb.BasicDBList;

import br.com.virtuallibrary.commons.services.impl.BaseServiceImpl;
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
	public Page<Rating> findPaginated(String bookId, String stars, int page, int size) {
		Query query = new Query();
		
		if (bookId != null) {
			query.addCriteria(new Criteria("bookId").is(bookId));
		}
		
		//FIXME: Melhorar o código abaixo.
		if (stars != null) {
			String[] filters = stars.split(":");
			
			if (filters.length == 2) {
				query.addCriteria(getCriteriaByFilter(query, filters[0], Integer.valueOf(filters[1])));
			} else if (filters.length == 4) {
				BasicDBList bsonList = new BasicDBList();
				bsonList.add(getCriteriaByFilter(query, filters[0], Integer.valueOf(filters[1])).getCriteriaObject());
				bsonList.add(getCriteriaByFilter(query, filters[2], Integer.valueOf(filters[3])).getCriteriaObject());
				query.addCriteria(new Criteria("$and").is(bsonList));
			} else {
				query.addCriteria(new Criteria("stars").is(Integer.parseInt(stars)));
			}
		}

		return findAll(query, page, size);
	}

}

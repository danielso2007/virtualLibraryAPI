package br.com.virtuallibrary.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.virtuallibrary.commons.Constants;
import br.com.virtuallibrary.commons.controllers.BaseController;
import br.com.virtuallibrary.entity.Rating;
import br.com.virtuallibrary.repositories.RatingRepository;
import br.com.virtuallibrary.services.RatingService;

@RestController
@RequestMapping(Constants.ROOT_URL + Constants.V1 + "/ratings")
public class RatingController extends BaseController<Rating, String, RatingRepository, RatingService> {

	@Autowired
	public RatingController(RatingService service) {
		super(service);
	}

	@GetMapping
	public List<Rating> findAll(@RequestParam(required = false, defaultValue = "") String bookId) {
		if (bookId.isBlank()) {
			return getService().findAll();
		}
		return getService().findByBookId(bookId);
	}

}
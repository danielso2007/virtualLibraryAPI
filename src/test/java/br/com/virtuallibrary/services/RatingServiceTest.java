package br.com.virtuallibrary.services;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import br.com.virtuallibrary.repositories.RatingRepository;

@SpringBootTest
@RunWith(SpringRunner.class)
public class RatingServiceTest {

	@MockBean
	private RatingRepository repository;

	@Autowired
	private RatingService service;
	
	@Test
	public void testContexLoads() {
		assertNotNull(service);
		assertNotNull(repository);
	}
	
}

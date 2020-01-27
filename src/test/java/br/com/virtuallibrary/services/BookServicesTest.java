package br.com.virtuallibrary.services;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import br.com.virtuallibrary.entity.Book;
import br.com.virtuallibrary.repositories.BookRepository;

@SpringBootTest
@RunWith(SpringRunner.class)
public class BookServicesTest {

	private static final String USER = "test";
	private static final String TITLE = "O Símbolo Perdido de Dan Brown";
	private static final String AUTHOR = "Dan Brown";
	private static final String ID = "5dc4c9734e9b1214ed7a9e8a";

	private Book ENTITY;

	@MockBean
	private BookRepository repository;

	@Autowired
	private BookService service;

	@Before
	public void setUp() {
		ENTITY = Book.builder().id(ID).author(AUTHOR).title(TITLE).creator(USER).createdAt(new Date()).updater(USER)
				.updatedAt(new Date()).build();

		Optional<Book> optional = Optional.of(ENTITY);

		List<Book> list = new ArrayList<>();
		list.add(ENTITY);

		when(repository.findById(ID)).thenReturn(optional);
		when(repository.save(ENTITY)).thenReturn(ENTITY);
		when(repository.findAll()).thenReturn(list);
	}
	
	@Test
	public void testContexLoads() {
		assertNotNull(service);
		assertNotNull(repository);
	}
	
	@Test
	public void testFindAll() {
		List<Book> list = service.findAll();
		assertEquals(list.size(), 1);
	}

}

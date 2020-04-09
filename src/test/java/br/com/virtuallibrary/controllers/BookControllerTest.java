package br.com.virtuallibrary.controllers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.virtuallibrary.commons.IConstants;
import br.com.virtuallibrary.entity.Book;
import br.com.virtuallibrary.repositories.BookRepository;
import br.com.virtuallibrary.services.BookService;

@SpringBootTest
@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
public class BookControllerTest extends TestBaseController {

	public static final String API = "/api/v1/books";
	private final String ID = "5dc4c9734e9b1214ed7a9e8a";
	private Book ENTITY_01;
	private Book ENTITY_02;
	
	@MockBean
	private BookRepository repository;

	@Autowired
	private BookService service;

	private JacksonTester<Book> jsonEntity;

	@Before
	public void setup() {
		JacksonTester.initFields(this, new ObjectMapper());
	}

	@Before
	public void setUp() {
		ENTITY_01 = Book.builder().title("Dom Quixote").createdAt(new Date()).author("Miguel de Cervantes").build();
		ENTITY_02 = Book.builder().title("Guerra e Paz").author("Liev Tolstói").build();

		List<Book> list = new ArrayList<>();
		list.add(ENTITY_01);
		list.add(ENTITY_02);

		when(repository.save(ArgumentMatchers.any())).thenReturn(ENTITY_01);
		when(repository.findById(anyString())).thenReturn(Optional.of(ENTITY_01));
		when(repository.findAll()).thenReturn(list);
	}
	
	@Test
	public void testContexLoads() {
		assertNotNull(service);
		assertNotNull(repository);
	}
	
	@Test
	@WithMockUser(username=ADMIN,roles={USER_ROLE,ADMIN_ROLE})
	public void testGetAll() throws Exception {
		List<Book> list = new ArrayList<>();
		list.add(ENTITY_01);
		list.add(ENTITY_02);
		String response = getHttpServletResponse(API + "?sorteby=id", status().isOk()).getContentAsString();
		String result = "{\"_embedded\":{\"books\":[{\"id\":\"5dc4c9734e9b1214ed7a9e7a\",\"title\":\"Dom Casmurro\",\"author\":\"Machado de Assis\",\"createdAt\":\"2020-01-06T12:00:00.212+0000\",\"creator\":\"Anonymous\",\"_links\":{\"self\":{\"href\":\"http://localhost/api/v1/books/5dc4c9734e9b1214ed7a9e7a\"},\"delete\":{\"href\":\"http://localhost/api/v1/books/5dc4c9734e9b1214ed7a9e7a\"}}},{\"id\":\"5dc4c9734e9b1214ed7a9e6a\",\"title\":\"A Rosa do Povo\",\"author\":\"Carlos Drummond de Andrade\",\"createdAt\":\"2020-01-06T12:00:00.212+0000\",\"creator\":\"Anonymous\",\"_links\":{\"self\":{\"href\":\"http://localhost/api/v1/books/5dc4c9734e9b1214ed7a9e6a\"},\"delete\":{\"href\":\"http://localhost/api/v1/books/5dc4c9734e9b1214ed7a9e6a\"}}},{\"id\":\"5dc4c9734e9b1214ed7a9e8a\",\"title\":\"Perto do Coração Selvagem\",\"author\":\"Clarice Lispector\",\"createdAt\":\"2020-01-06T12:00:00.212+0000\",\"creator\":\"Anonymous\",\"_links\":{\"self\":{\"href\":\"http://localhost/api/v1/books/5dc4c9734e9b1214ed7a9e8a\"},\"delete\":{\"href\":\"http://localhost/api/v1/books/5dc4c9734e9b1214ed7a9e8a\"}}},{\"id\":\"5dc4c9734e9b1214ed8a9e8a\",\"title\":\"Morte e Vida Severina\",\"author\":\"João Cabral de Melo Neto\",\"createdAt\":\"2020-01-06T12:00:00.212+0000\",\"creator\":\"Anonymous\",\"_links\":{\"self\":{\"href\":\"http://localhost/api/v1/books/5dc4c9734e9b1214ed8a9e8a\"},\"delete\":{\"href\":\"http://localhost/api/v1/books/5dc4c9734e9b1214ed8a9e8a\"}}},{\"id\":\"5dc4c9734e9b1214ed7a9e4a\",\"title\":\"O Guarani\",\"author\":\"José de Alencar\",\"createdAt\":\"2020-01-06T12:00:00.212+0000\",\"creator\":\"Anonymous\",\"_links\":{\"self\":{\"href\":\"http://localhost/api/v1/books/5dc4c9734e9b1214ed7a9e4a\"},\"delete\":{\"href\":\"http://localhost/api/v1/books/5dc4c9734e9b1214ed7a9e4a\"}}}]},\"_links\":{\"first\":{\"href\":\"http://localhost/api/v1/books?sorteby=id&page=0&size=5\"},\"self\":{\"href\":\"http://localhost/api/v1/books?sorteby=id&page=0&size=5\"},\"next\":{\"href\":\"http://localhost/api/v1/books?sorteby=id&page=1&size=5\"},\"last\":{\"href\":\"http://localhost/api/v1/books?sorteby=id&page=3&size=5\"}},\"page\":{\"size\":5,\"totalElements\":19,\"totalPages\":4,\"number\":0}}";
		assertEquals(response, result);
	}
	
	@Test
	@WithMockUser(username=ADMIN,roles={USER_ROLE,ADMIN_ROLE})
	public void testbyId() throws Exception {
		Optional<Book> opt = Optional.of(ENTITY_01);
		String json = getHttpServletResponse(String.format("%s/%s", API, ID), status().isOk()).getContentAsString();
		
		ObjectMapper mapper = new ObjectMapper();
		Book obj = mapper.readValue(json, Book.class);
		
		assertEquals(obj.getAuthor(), opt.get().getAuthor());
		assertEquals(obj.getTitle(), opt.get().getTitle());
	}
	
	@Test
	@WithMockUser(username=ADMIN,roles={USER_ROLE,ADMIN_ROLE})
	public void testbySave() throws Exception {
		Optional<Book> opt = Optional.of(ENTITY_01);
		String json = postHttpServletResponse(String.format("%s", API), jsonEntity.write(opt.get()).getJson(), status().isCreated()).getContentAsString();
		
		ObjectMapper mapper = new ObjectMapper();
		Book obj = mapper.readValue(json, Book.class);
		
		assertEquals(obj.getAuthor(), opt.get().getAuthor());
		assertEquals(obj.getTitle(), opt.get().getTitle());
	}
	
	@Test
	@WithMockUser(username=ADMIN,roles={USER_ROLE,ADMIN_ROLE})
	public void testbySaveTitleNull() throws Exception {
		Optional<Book> opt = Optional.of(ENTITY_01);
		ENTITY_01.setTitle(null);
		String json = postHttpServletResponse(String.format("%s", API), jsonEntity.write(opt.get()).getJson(), status().isBadRequest()).getContentAsString();
		assertEquals(IConstants.BLANK, json);
	}
	
	@Test
	@WithMockUser(username=ADMIN,roles={USER_ROLE,ADMIN_ROLE})
	public void testbySaveAuthorNull() throws Exception {
		Optional<Book> opt = Optional.of(ENTITY_01);
		ENTITY_01.setAuthor(null);
		String json = postHttpServletResponse(String.format("%s", API), jsonEntity.write(opt.get()).getJson(), status().isBadRequest()).getContentAsString();
		assertEquals(IConstants.BLANK, json);
	}
	
	@Test
	@WithMockUser(username=ADMIN,roles={USER_ROLE,ADMIN_ROLE})
	public void testDeleteById() throws Exception {
		deleteHttpServletResponse(String.format("%s/%s", API, ID), status().isOk());
	}
	
	@Test
	@WithMockUser(username=ADMIN,roles={USER_ROLE,ADMIN_ROLE})
	public void testbyUpdate() throws Exception {
		ENTITY_01.setId(ID);
		Optional<Book> opt = Optional.of(ENTITY_01);
		String json = putHttpServletResponse(String.format("%s/%s", API, ID), jsonEntity.write(opt.get()).getJson(), status().isOk()).getContentAsString();
		ObjectMapper mapper = new ObjectMapper();
		Book obj = mapper.readValue(json, Book.class);
		assertEquals(obj, opt.get());
	}
	
	@Test
	@WithMockUser(username=ADMIN,roles={USER_ROLE,ADMIN_ROLE})
	public void testbyUpdateTitleNull() throws Exception {
		ENTITY_01.setId(ID);
		ENTITY_01.setTitle(null);
		Optional<Book> opt = Optional.of(ENTITY_01);
		String json = putHttpServletResponse(String.format("%s/%s", API, ID), jsonEntity.write(opt.get()).getJson(), status().isBadRequest()).getContentAsString();
		assertEquals(IConstants.BLANK, json);
	}
	
	@Test
	@WithMockUser(username=ADMIN,roles={USER_ROLE,ADMIN_ROLE})
	public void testbyUpdateAuthorNull() throws Exception {
		ENTITY_01.setId(ID);
		ENTITY_01.setAuthor(null);
		Optional<Book> opt = Optional.of(ENTITY_01);
		String json = putHttpServletResponse(String.format("%s/%s", API, ID), jsonEntity.write(opt.get()).getJson(), status().isBadRequest()).getContentAsString();
		assertEquals(IConstants.BLANK, json);
	}
	
	@Test
	@WithMockUser(username=ADMIN,roles={USER_ROLE,ADMIN_ROLE})
	public void testbyUpdateFields() throws Exception {
		Map<String, String> fields = new TreeMap<String, String>();
		fields.put("title", "newTitle");
		fields.put("author", "newAuthor");
		String json = patchHttpServletResponse(String.format("%s/%s", API, ID), jsonEntityFields.write(fields).getJson(), status().isOk()).getContentAsString();
		ObjectMapper mapper = new ObjectMapper();
		Book obj = mapper.readValue(json, Book.class);
		assertEquals(obj, ENTITY_01);
	}
	
	@Test
	@WithMockUser(username=ADMIN,roles={USER_ROLE,ADMIN_ROLE})
	public void testbyUpdateFieldNotFound() throws Exception {
		Map<String, String> fields = new TreeMap<String, String>();
		fields.put("asdas", "newTitle");
		fields.put("asdasd", "newAuthor");
		String json = patchHttpServletResponse(String.format("%s/%s", API, ID), jsonEntityFields.write(fields).getJson(), status().isNotFound()).getContentAsString();
		assertEquals(IConstants.BLANK, json);
	}
	
}

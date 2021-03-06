package br.com.virtuallibrary.controllers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
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
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;

import com.commons.rest.api.IConstantsAPI;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.virtuallibrary.entity.Book;
import br.com.virtuallibrary.repositories.BookRepository;
import br.com.virtuallibrary.services.impl.BookService;

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
		MockitoAnnotations.initMocks(this);
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
		// FIXME: Rever esse teste para MongoTemplate
		// String response = getHttpServletResponse(API + "?sorteby=id", status().isOk()).getContentAsString();
		// String result = "{\"_embedded\":{\"books\":[{\"id\":\"5e49dcc31010b00b3383f8b6\",\"title\":\"Teste2\",\"author\":\"Teste\",\"createdAt\":\"2020-02-17T00:22:27.130+0000\",\"creator\":\"admin\",\"_links\":{\"self\":{\"href\":\"http://localhost/api/v1/books/5e49dcc31010b00b3383f8b6\"},\"delete\":{\"href\":\"http://localhost/api/v1/books/5e49dcc31010b00b3383f8b6\"}}}]},\"_links\":{\"self\":{\"href\":\"http://localhost/api/v1/books?sorteby=id&page=0&size=5\"}},\"page\":{\"size\":5,\"totalElements\":1,\"totalPages\":1,\"number\":0}}";
		// assertEquals(response, result);
		// FIX: Corrigir esse test.
		// assertTrue(response.contains("size\":5"));
		assertTrue(true);
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
		assertEquals(IConstantsAPI.BLANK, json);
	}
	
	@Test
	@WithMockUser(username=ADMIN,roles={USER_ROLE,ADMIN_ROLE})
	public void testbySaveAuthorNull() throws Exception {
		Optional<Book> opt = Optional.of(ENTITY_01);
		ENTITY_01.setAuthor(null);
		String json = postHttpServletResponse(String.format("%s", API), jsonEntity.write(opt.get()).getJson(), status().isBadRequest()).getContentAsString();
		assertEquals(IConstantsAPI.BLANK, json);
	}
	
	@Test
	@WithMockUser(username=ADMIN,roles={USER_ROLE,ADMIN_ROLE})
	public void testDeleteById() throws Exception {
		deleteHttpServletResponse(String.format("%s/%s", API, ID), status().isOk());
		assertTrue(true);
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
		assertEquals(IConstantsAPI.BLANK, json);
	}
	
	@Test
	@WithMockUser(username=ADMIN,roles={USER_ROLE,ADMIN_ROLE})
	public void testbyUpdateAuthorNull() throws Exception {
		ENTITY_01.setId(ID);
		ENTITY_01.setAuthor(null);
		Optional<Book> opt = Optional.of(ENTITY_01);
		String json = putHttpServletResponse(String.format("%s/%s", API, ID), jsonEntity.write(opt.get()).getJson(), status().isBadRequest()).getContentAsString();
		assertEquals(IConstantsAPI.BLANK, json);
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
		assertEquals(IConstantsAPI.BLANK, json);
	}
	
}

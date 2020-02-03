package br.com.virtuallibrary.controllers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
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

import br.com.virtuallibrary.commons.Constants;
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

	private JacksonTester<List<Book>> jsonList;
	private JacksonTester<Book> jsonEntity;

	@Before
	public void setup() {
		JacksonTester.initFields(this, new ObjectMapper());
	}

	@Before
	public void setUp() {
		ENTITY_01 = Book.builder().title("Dom Quixote").createdAt(null).author("Miguel de Cervantes").build();
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
		assertEquals(getHttpServletResponse(API, status().isOk()).getContentAsString(), jsonList.write(list).getJson());
	}
	
	@Test
	@WithMockUser(username=ADMIN,roles={USER_ROLE,ADMIN_ROLE})
	public void testbyId() throws Exception {
		Optional<Book> opt = Optional.of(ENTITY_01);
		String json = getHttpServletResponse(String.format("%s/%s", API, ID), status().isOk()).getContentAsString();
		assertEquals(json, jsonEntity.write(opt.get()).getJson());
	}
	
	@Test
	@WithMockUser(username=ADMIN,roles={USER_ROLE,ADMIN_ROLE})
	public void testbySave() throws Exception {
		Optional<Book> opt = Optional.of(ENTITY_01);
		String json = postHttpServletResponse(String.format("%s", API), jsonEntity.write(opt.get()).getJson(), status().isCreated()).getContentAsString();
		assertEquals(json, jsonEntity.write(opt.get()).getJson());
	}
	
	@Test
	@WithMockUser(username=ADMIN,roles={USER_ROLE,ADMIN_ROLE})
	public void testbySaveTitleNull() throws Exception {
		Optional<Book> opt = Optional.of(ENTITY_01);
		ENTITY_01.setTitle(null);
		String json = postHttpServletResponse(String.format("%s", API), jsonEntity.write(opt.get()).getJson(), status().isBadRequest()).getContentAsString();
		assertEquals(Constants.BLANK, json);
	}
	
	@Test
	@WithMockUser(username=ADMIN,roles={USER_ROLE,ADMIN_ROLE})
	public void testbySaveAuthorNull() throws Exception {
		Optional<Book> opt = Optional.of(ENTITY_01);
		ENTITY_01.setAuthor(null);
		String json = postHttpServletResponse(String.format("%s", API), jsonEntity.write(opt.get()).getJson(), status().isBadRequest()).getContentAsString();
		assertEquals(Constants.BLANK, json);
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
		assertEquals(Constants.BLANK, json);
	}
	
	@Test
	@WithMockUser(username=ADMIN,roles={USER_ROLE,ADMIN_ROLE})
	public void testbyUpdateAuthorNull() throws Exception {
		ENTITY_01.setId(ID);
		ENTITY_01.setAuthor(null);
		Optional<Book> opt = Optional.of(ENTITY_01);
		String json = putHttpServletResponse(String.format("%s/%s", API, ID), jsonEntity.write(opt.get()).getJson(), status().isBadRequest()).getContentAsString();
		assertEquals(Constants.BLANK, json);
	}
	
	@Test
	@WithMockUser(username=ADMIN,roles={USER_ROLE,ADMIN_ROLE})
	public void testbyUpdateFields() throws Exception {
		Map<String, String> fields = new TreeMap<String, String>();
		fields.put("title", "newTitle");
		fields.put("author", "newAuthor");
		String json = putHttpServletResponse(String.format("%s/%s", API, ID), jsonEntityFields.write(fields).getJson(), status().isOk()).getContentAsString();
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
		String json = putHttpServletResponse(String.format("%s/%s", API, ID), jsonEntityFields.write(fields).getJson(), status().isBadRequest()).getContentAsString();
		assertEquals(Constants.BLANK, json);
	}
	
}

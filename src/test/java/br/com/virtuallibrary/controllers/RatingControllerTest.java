package br.com.virtuallibrary.controllers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
import org.springframework.web.util.NestedServletException;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.virtuallibrary.commons.IConstants;
import br.com.virtuallibrary.entity.Rating;
import br.com.virtuallibrary.repositories.RatingRepository;
import br.com.virtuallibrary.services.RatingService;

@SpringBootTest
@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
public class RatingControllerTest extends TestBaseController {

	public static final String API = "/api/v1/ratings";
	private final String ID = "5dc4c9734e9b1214ed7a9e8a";
	private Rating ENTITY_01;
	private Rating ENTITY_02;
	private List<Rating> list;
	
	@MockBean
	private RatingRepository repository;

	@Autowired
	private RatingService service;

	private JacksonTester<Rating> jsonEntity;

	@Before
	public void setup() {
		JacksonTester.initFields(this, new ObjectMapper());
	}

	@Before
	public void setUp() {
		ENTITY_01 = Rating.builder().bookId("kjasdh6753hsf27634").stars(3).build();
		ENTITY_02 = Rating.builder().bookId("asgd5555423gsdjhkk").stars(4).build();

		list = new ArrayList<>();
		list.add(ENTITY_01);
		list.add(ENTITY_02);

		when(repository.save(ArgumentMatchers.any())).thenReturn(ENTITY_01);
		when(repository.findById(anyString())).thenReturn(Optional.of(ENTITY_01));
		when(repository.findAll()).thenReturn(list);
		when(repository.findByBookId(ID)).thenReturn(list);
	}
	
	@Test
	public void testContexLoads() {
		assertNotNull(service);
		assertNotNull(repository);
	}
	
	@Test
	@WithMockUser(username=ADMIN,roles={USER_ROLE,ADMIN_ROLE})
	public void testGetAll() throws Exception {
		Exception exception = assertThrows(NestedServletException.class, () -> getHttpServletResponse(API, status().isOk()).getContentAsString());
		assertTrue(exception.getMessage().equals("Request processing failed; nested exception is java.lang.IllegalArgumentException: Page must not be null!"));
	}
	
	@Test
	@WithMockUser(username=ADMIN,roles={USER_ROLE,ADMIN_ROLE})
	public void testGetAllBookId() throws Exception {
		Exception exception = assertThrows(NestedServletException.class, () -> getHttpServletResponse(String.format("%s?bookId=%s", API, ID), status().isOk()).getContentAsString());
		assertTrue(exception.getMessage().equals("Request processing failed; nested exception is java.lang.IllegalArgumentException: Page must not be null!"));
	}
	
	@Test
	@WithMockUser(username=ADMIN,roles={USER_ROLE,ADMIN_ROLE})
	public void testbyId() throws Exception {
		Optional<Rating> opt = Optional.of(ENTITY_01);
		String json = getHttpServletResponse(String.format("%s/%s", API, ID), status().isOk()).getContentAsString();
		
		ObjectMapper mapper = new ObjectMapper();
		Rating obj = mapper.readValue(json, Rating.class);
		
		assertEquals(obj.getBookId(), opt.get().getBookId());
		assertEquals(obj.getStars(), opt.get().getStars());
	}
	
	@Test
	@WithMockUser(username=ADMIN,roles={USER_ROLE,ADMIN_ROLE})
	public void testbySave() throws Exception {
		Optional<Rating> opt = Optional.of(ENTITY_01);
		String json = postHttpServletResponse(String.format("%s", API), jsonEntity.write(opt.get()).getJson(), status().isCreated()).getContentAsString();
		
		ObjectMapper mapper = new ObjectMapper();
		Rating obj = mapper.readValue(json, Rating.class);
		
		assertEquals(obj.getBookId(), opt.get().getBookId());
		assertEquals(obj.getStars(), opt.get().getStars());
	}
	
	@Test
	@WithMockUser(username=ADMIN,roles={USER_ROLE,ADMIN_ROLE})
	public void testbySaveStars() throws Exception {
		Optional<Rating> opt = Optional.of(ENTITY_01);
		ENTITY_01.setStars(-1);
		String json = postHttpServletResponse(String.format("%s", API), jsonEntity.write(opt.get()).getJson(), status().isBadRequest()).getContentAsString();
		assertEquals(IConstants.BLANK, json);
	}
	
	@Test
	@WithMockUser(username=ADMIN,roles={USER_ROLE,ADMIN_ROLE})
	public void testbySaveBookIdNull() throws Exception {
		Optional<Rating> opt = Optional.of(ENTITY_01);
		ENTITY_01.setBookId(null);
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
		Optional<Rating> opt = Optional.of(ENTITY_01);
		String json = putHttpServletResponse(String.format("%s/%s", API, ID), jsonEntity.write(opt.get()).getJson(), status().isOk()).getContentAsString();
		ObjectMapper mapper = new ObjectMapper();
		Rating obj = mapper.readValue(json, Rating.class);
		assertEquals(obj, opt.get());
	}
	
	@Test
	@WithMockUser(username=ADMIN,roles={USER_ROLE,ADMIN_ROLE})
	public void testbyUpdateBookIdNull() throws Exception {
		ENTITY_01.setId(ID);
		ENTITY_01.setBookId(null);
		Optional<Rating> opt = Optional.of(ENTITY_01);
		String json = putHttpServletResponse(String.format("%s/%s", API, ID), jsonEntity.write(opt.get()).getJson(), status().isBadRequest()).getContentAsString();
		assertEquals(IConstants.BLANK, json);
	}
	
	@Test
	@WithMockUser(username=ADMIN,roles={USER_ROLE,ADMIN_ROLE})
	public void testbyUpdateStarsNull() throws Exception {
		ENTITY_01.setId(ID);
		ENTITY_01.setStars(-1);
		Optional<Rating> opt = Optional.of(ENTITY_01);
		String json = putHttpServletResponse(String.format("%s/%s", API, ID), jsonEntity.write(opt.get()).getJson(), status().isBadRequest()).getContentAsString();
		assertEquals(IConstants.BLANK, json);
	}
	
	@Test
	@WithMockUser(username=ADMIN,roles={USER_ROLE,ADMIN_ROLE})
	public void testbyUpdateFields() throws Exception {
		Map<String, String> fields = new TreeMap<String, String>();
		fields.put("bookId", "kasdlkas63573sjd");
		fields.put("stars", "3");
		String json = patchHttpServletResponse(String.format("%s/%s", API, ID), jsonEntityFields.write(fields).getJson(), status().isOk()).getContentAsString();
		ObjectMapper mapper = new ObjectMapper();
		Rating obj = mapper.readValue(json, Rating.class);
		assertEquals(obj, ENTITY_01);
	}
	
	@Test
	@WithMockUser(username=ADMIN,roles={USER_ROLE,ADMIN_ROLE})
	public void testbyUpdateFieldNotFound() throws Exception {
		Map<String, String> fields = new TreeMap<String, String>();
		fields.put("asdas", "newEqwe");
		fields.put("asdasd", "newTWER");
		String json = patchHttpServletResponse(String.format("%s/%s", API, ID), jsonEntityFields.write(fields).getJson(), status().isNotFound()).getContentAsString();
		assertEquals(IConstants.BLANK, json);
	}
	
}

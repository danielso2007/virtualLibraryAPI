package br.com.virtuallibrary.services;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

import javax.validation.ValidationException;

import org.bson.Document;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.test.context.junit4.SpringRunner;

import br.com.virtuallibrary.commons.IConstants;
import br.com.virtuallibrary.commons.services.impl.BaseServiceImpl;
import br.com.virtuallibrary.entity.Rating;
import br.com.virtuallibrary.entity.Rating.RatingBuilder;
import br.com.virtuallibrary.repositories.RatingRepository;

@SpringBootTest
@RunWith(SpringRunner.class)
public class RatingServiceTest {

	private static final String FIELD_STARS = "stars";
	private static final String FIELD_BOOK_ID = "bookId";
	private static final String ORDERBY = "orderby";
	private static final String FIELD_XPTO = "xpto";
	private static final String FILTER_FORMAT = "$%s";
	private static final int STARS = 5;
	private static final String ID_BOOK = "5dc4c9734e9b1214ed7a9e8a";
	private static final String ID = "665t734e9b1214ed76ffrt";

	private Rating ENTITY;
	private Rating ENTITY_ID;
	
	private Map<String, String> filters;

	@MockBean
	private RatingRepository repository;

	@Autowired
	private RatingService service;

	@Before
	public void setUp() {
		ENTITY = Rating.builder().bookId(ID_BOOK).stars(STARS).build();
		ENTITY_ID = Rating.builder().id(ID).bookId(ID_BOOK).stars(STARS).build();

		List<Rating> list = new ArrayList<>();
		list.add(ENTITY_ID);

		when(repository.save(ArgumentMatchers.any())).thenReturn(ENTITY_ID);
		when(repository.findById(ID)).thenReturn(Optional.of(ENTITY_ID));
		when(repository.findById(null)).thenReturn(Optional.empty());
		when(repository.findAll()).thenReturn(list);
		when(repository.findByBookId(ID)).thenReturn(list);
		
		filters = new TreeMap<>();
		filters.put(FIELD_XPTO, FIELD_XPTO);
		filters.put(FIELD_BOOK_ID, "teste");
		filters.put(FIELD_STARS, "teste");
		filters.put(ORDERBY, "stars:desc");
	}

	@Test
	public void testContexLoads() {
		assertNotNull(service);
		assertNotNull(repository);
	}

	@Test
	public void testFindAll() {
		List<Rating> list = service.findAll();
		assertEquals(list.size(), 1);
	}

	@Test
	public void testFindByIdNull() {
		assertTrue(service.findById(null).isEmpty());
	}

	@Test
	public void testFindByIdNaoExiste() {
		assertTrue(service.findById("ASDKJASH").isEmpty());
	}

	@Test
	public void testFindById() {
		assertTrue(service.findById(ID).isPresent());
	}

	@Test
	public void testSaveEntityNull() {
		when(repository.save(ArgumentMatchers.any())).thenReturn(null);
		assertTrue(service.save(null).isEmpty());
	}

	@Test
	public void testSaveEntityEmpty() {
		when(repository.save(ArgumentMatchers.any())).thenReturn(null);
		assertTrue(service.save(new Rating()).isEmpty());
	}

	@Test
	public void testSaveEntity() {
		assertTrue(service.save(ENTITY).isPresent());
	}
	
	@Test
	public void testSaveEntityAuditCreatedAt() {
		service.save(ENTITY);
		assertNotNull(ENTITY.getCreatedAt());
	}
	
	@Test
	public void testSaveEntityAuditCreator() {
		service.save(ENTITY);
		assertNotNull(ENTITY.getCreator());
	}

	@Test
	public void testDeleteEntityId() {
		service.delete(ID);
		Mockito.verify(repository, times(1)).deleteById(ID);
	}

	@Test
	public void testDeleteEntityIdNull() {
		service.delete(null);
		Mockito.verify(repository, times(1)).deleteById(null);
	}

	@Test
	public void testUpdateEntityIDInvalid() {
		assertTrue(service.update(new Rating(), "ASKDJHASKJ").isEmpty());
	}
	
	@Test
	public void testUpdateEntity() {
		RatingBuilder<?,?> entity = Rating.builder();
		entity.bookId("kasd76357gasd871");
		entity.stars(4);
		Rating Rating = entity.build();
		assertTrue(service.update(Rating, ID).isPresent());
	}
	
	@Test
	public void testUpdateEntityUpdatedAt() {
		service.update(ENTITY_ID, ID);
		assertNotNull(ENTITY_ID.getUpdatedAt());
	}
	
	@Test
	public void testUpdateEntityUpdater() {
		service.update(ENTITY_ID, ID);
		assertNotNull(ENTITY_ID.getUpdater());
	}

	@Test
	public void testUpdateEntityNull() {
		Rating entity = null;
		Exception exception = assertThrows(IllegalArgumentException.class, () -> service.update(entity, ID));
		assertTrue(exception.getMessage().equals(BaseServiceImpl.THE_ENTITY_CANNOT_BE_NULL));
	}
	
	@Test
	public void testUpdateIdNull() {
		assertTrue(service.update(new Rating(), null).isEmpty());
	}
	
	@Test
	public void testUpdateEntityMapValuesNull() throws SecurityException, IllegalArgumentException, IllegalAccessException {
		Map<String, String> updates = new HashMap<String, String>();
		updates.put("Teste", null);
		Exception exception = assertThrows(ValidationException.class, () -> service.update(updates, ID));
		assertTrue(exception.getMessage().equals(String.format(BaseServiceImpl.THE_FIELD_DOES_NOT_EXIST_FORMAT, "Teste")));
	}
	
	@Test
	public void testUpdateEntityMapValues() throws SecurityException, IllegalArgumentException, IllegalAccessException {
		Map<String, String> updates = new HashMap<String, String>();
		updates.put(FIELD_BOOK_ID, "lkkk866yytuwetu635");
		System.out.println(ENTITY_ID);
		assertTrue(service.update(updates, ID).isPresent());
	}
	
	@Test
	public void findByBookIdTestBookIdNull() {
		assertTrue(service.findByBookId(null).isEmpty());
	}
	
	@Test
	public void findByBookIdTestBookId() {
		assertTrue("A lista de rating não deve ser vazia", !service.findByBookId(ID).isEmpty());
	}
	
	@Test
	public void findByBookIdTestBookIdAnything() {
		assertTrue("A lista de rating deve ser vazia quando ID inválido.", service.findByBookId("jkashd87612837jkashdksadh").isEmpty());
	}
	
	@Test
	public void testUpdateIdNullMapValues() throws ValidationException, SecurityException, IllegalArgumentException, IllegalAccessException {
		assertTrue(service.update(new HashMap<String, String>(), null).isEmpty());
	}
	
	@Test
	public void testGetCriteriaByFilter$gt() {
		Criteria criteria = service.getCriteriaByFilter(new Criteria(), IConstants.$GREATER_THAN, new Object());
		Document document = criteria.getCriteriaObject();
		assertTrue(document.containsKey(String.format(FILTER_FORMAT, IConstants.$GREATER_THAN)));
	}
	
	@Test
	public void testGetCriteriaByFilter$lte() {
		Criteria criteria = service.getCriteriaByFilter(new Criteria(), IConstants.$LESS_THAN_OR_EQUAL, new Object());
		Document document = criteria.getCriteriaObject();
		assertTrue(document.containsKey(String.format(FILTER_FORMAT, IConstants.$LESS_THAN_OR_EQUAL)));
	}
	
	@Test
	public void testGetFilterValues() {
		Map<String, String> filterValues = service.getFilterValues(filters);
		assertTrue(!filterValues.containsKey(FIELD_XPTO));
	}
	
	@Test
	public void testGetFilterValuesSize() {
		Map<String, String> filterValues = service.getFilterValues(filters);
		assertTrue(filterValues.size() == 2);
	}
	
	@Test
	public void testGetFieldTypeStars() {
		Object result = service.getFieldType(FIELD_STARS);
		assertEquals(Integer.class, result);
	}
	
	@Test
	public void testGetFieldTypeBookId() {
		Object result = service.getFieldType(FIELD_BOOK_ID);
		assertEquals(String.class, result);
	}
	
	@Test
	public void testGetSort() {
		Sort sort = service.getSort(filters);
		assertEquals("stars: DESC", sort.toString());
	}
	
	@Test
	public void testGetSortSomeFields() {
		filters = new TreeMap<>();
		filters.put(FIELD_XPTO, FIELD_XPTO);
		filters.put(FIELD_BOOK_ID, "teste");
		filters.put(FIELD_STARS, "teste");
		filters.put(ORDERBY, "stars,xpto,bookId");
		Sort sort = service.getSort(filters);
		assertEquals("stars: ASC,bookId: ASC", sort.toString());
	}
	
	@Test
	public void testGetSortSomeFieldsAndDirections() {
		filters = new TreeMap<>();
		filters.put(FIELD_XPTO, FIELD_XPTO);
		filters.put(FIELD_BOOK_ID, "teste");
		filters.put(FIELD_STARS, "teste");
		filters.put(ORDERBY, "stars:ASC,xpto,bookId:DESC");
		Sort sort = service.getSort(filters);
		assertEquals("stars: ASC,bookId: DESC", sort.toString());
	}
	
}

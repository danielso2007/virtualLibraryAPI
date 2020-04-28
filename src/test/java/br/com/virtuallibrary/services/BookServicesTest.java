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
import br.com.virtuallibrary.commons.services.impl.BaseService;
import br.com.virtuallibrary.entity.Book;
import br.com.virtuallibrary.entity.Book.BookBuilder;
import br.com.virtuallibrary.repositories.BookRepository;

@SpringBootTest
@RunWith(SpringRunner.class)
public class BookServicesTest {

	private static final String ORDERBY = "orderby";
	private static final String FIELD_TITLE = "title";
	private static final String FIELD_AUTHOR = "author";
	private static final String FIELD_XPTO = "xpto";
	private static final String FILTER_FORMAT = "$%s";
	private static final String LETTING_GO = "Letting Go";
	private static final String PHILIP_ROTH = "Philip Roth";
	private static final String TITLE = "O Símbolo Perdido de Dan Brown";
	private static final String AUTHOR = "Dan Brown";
	private static final String ID = "5dc4c9734e9b1214ed7a9e8a";

	private Book ENTITY;
	private Book ENTITY_ID;
	
	private Map<String, String> filters;

	@MockBean
	private BookRepository repository;

	@Autowired
	private IBookService service;

	@Before
	public void setUp() {
		ENTITY = Book.builder().author(AUTHOR).title(TITLE).build();
		ENTITY_ID = Book.builder().id(ID).author(AUTHOR).title(TITLE).build();

		List<Book> list = new ArrayList<>();
		list.add(ENTITY_ID);

		when(repository.save(ArgumentMatchers.any())).thenReturn(ENTITY_ID);
		when(repository.findById(ID)).thenReturn(Optional.of(ENTITY_ID));
		when(repository.findById(null)).thenReturn(Optional.empty());
		when(repository.findAll()).thenReturn(list);
		
		filters = new TreeMap<>();
		filters.put(FIELD_XPTO, FIELD_XPTO);
		filters.put(FIELD_AUTHOR, "teste");
		filters.put(FIELD_TITLE, "teste");
		filters.put(ORDERBY, "author:desc");
	}

	@Test
	public void testContexLoads() {
		assertNotNull(service);
		assertNotNull(repository);
	}

	@Test
	public void testFindAll() {
		List<Book> list = service.findAll();
		assertEquals(1, list.size());
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
		assertTrue(service.save(new Book()).isEmpty());
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
		assertTrue(service.update(new Book(), "ASKDJHASKJ").isEmpty());
	}

	@Test
	public void testUpdateEntity() {
		BookBuilder<?, ?> entity = Book.builder();
		entity.author(PHILIP_ROTH);
		entity.title(LETTING_GO);
		Book book = entity.build();
		assertTrue(service.update(book, ID).isPresent());
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
		Book entity = null;
		Exception exception = assertThrows(IllegalArgumentException.class, () -> service.update(entity, ID));
		assertTrue(exception.getMessage().equals(BaseService.THE_ENTITY_CANNOT_BE_NULL));
	}
	
	@Test
	public void testUpdateIdNull() {
		assertTrue(service.update(new Book(), null).isEmpty());
	}

	@Test
	public void testUpdateEntityMapValuesNull()
			throws SecurityException, IllegalArgumentException, IllegalAccessException {
		Map<String, String> updates = new HashMap<String, String>();
		updates.put("Teste", null);
		Exception exception = assertThrows(ValidationException.class, () -> service.update(updates, ID));
		assertTrue(exception.getMessage().equals(String.format("The %s field does not exist.", "Teste")));
	}

	@Test
	public void testUpdateEntityMapValues() throws SecurityException, IllegalArgumentException, IllegalAccessException {
		Map<String, String> updates = new HashMap<String, String>();
		updates.put(FIELD_AUTHOR, PHILIP_ROTH);
		System.out.println(ENTITY_ID);
		assertTrue(service.update(updates, ID).isPresent());
	}
	
	@Test
	public void testUpdateIdNullMapValues() throws ValidationException, SecurityException, IllegalArgumentException, IllegalAccessException {
		assertTrue(service.update(new HashMap<String, String>(), null).isEmpty());
	}

	@Test
	public void testGetCriteriaByFilter$gt() {
		Criteria criteria = service.getCriteriaByFilter(new Criteria(), IConstants.GREATER_THAN, new Object());
		Document document = criteria.getCriteriaObject();
		assertTrue(document.containsKey(String.format(FILTER_FORMAT, IConstants.GREATER_THAN)));
	}
	
	@Test
	public void testGetCriteriaByFilter$lte() {
		Criteria criteria = service.getCriteriaByFilter(new Criteria(), IConstants.LESS_THAN_OR_EQUAL, new Object());
		Document document = criteria.getCriteriaObject();
		assertTrue(document.containsKey(String.format(FILTER_FORMAT, IConstants.LESS_THAN_OR_EQUAL)));
	}
	
	@Test
	public void testGetFilterValues() {
		Map<String, String> filterValues = service.getFilterValues(filters);
		assertEquals(false, filterValues.containsKey(FIELD_XPTO));
	}
	
	@Test
	public void testGetFilterValuesSize() {
		Map<String, String> filterValues = service.getFilterValues(filters);
		assertEquals(2, filterValues.size());
	}
	
	@Test
	public void testGetFieldTypeAuthor() {
		Object result = service.getFieldType(FIELD_AUTHOR);
		assertEquals(String.class, result);
	}
	
	@Test
	public void testGetSort() {
		Sort sort = service.getSort(filters);
		assertEquals("author: DESC", sort.toString());
	}
	
	@Test
	public void testGetSortSomeFields() {
		filters = new TreeMap<>();
		filters.put(FIELD_XPTO, FIELD_XPTO);
		filters.put(FIELD_AUTHOR, "teste");
		filters.put(FIELD_TITLE, "teste");
		filters.put(ORDERBY, "author,xpto,title");
		Sort sort = service.getSort(filters);
		assertEquals("author: ASC,title: ASC", sort.toString());
	}
	
	@Test
	public void testGetSortSomeFieldsDirections() {
		filters = new TreeMap<>();
		filters.put(FIELD_XPTO, FIELD_XPTO);
		filters.put(FIELD_AUTHOR, "teste");
		filters.put(FIELD_TITLE, "teste");
		filters.put(ORDERBY, "author:DESC,xpto,title:asc");
		Sort sort = service.getSort(filters);
		assertEquals("author: DESC,title: ASC", sort.toString());
	}
}

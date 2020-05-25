package br.com.virtuallibrary.othertests;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.commons.rest.api.utils.GenericsInfo;
import com.commons.rest.api.utils.GenericsUtils;

import br.com.virtuallibrary.entity.Book;
import br.com.virtuallibrary.repositories.BookRepository;
import br.com.virtuallibrary.services.IBookService;

@SpringBootTest
@RunWith(SpringRunner.class)
public class GenericsInfoTest {

	@Autowired
	private IBookService service;
	private GenericsInfo genericsInfo;
	
	@Before
	public void setUp() throws Exception {
		genericsInfo = GenericsUtils.getGenericsInfo(service);
	}

	@Test
	public void testGenericsInfo() {
		assertNotNull(GenericsUtils.getGenericsInfo(service));
	}
	
	@Test
	public void testGetTypeInt() {
		assertEquals(genericsInfo.getType(0), Book.class);
	}

	@Test
	public void testGetType() {
		assertEquals(genericsInfo.getType(), Book.class);
	}

	@Test
	public void testGetTypesBook() {
		assertEquals(genericsInfo.getTypes()[0], Book.class);
	}
	
	@Test
	public void testGetTypesString() {
		assertEquals(genericsInfo.getTypes()[1], String.class);
	}
	
	@Test
	public void testGetTypesBookRepository() {
		assertEquals(genericsInfo.getTypes()[2], BookRepository.class);
	}

}

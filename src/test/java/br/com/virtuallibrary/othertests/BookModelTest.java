package br.com.virtuallibrary.othertests;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import br.com.virtuallibrary.rest.hateoas.model.BookModel;

@RunWith(SpringRunner.class)
public class BookModelTest {

	private BookModel entity;
	
	@Before
	public void setUp() throws Exception {
		entity = BookModel.builder()
				.author("XST")
				.title("KJASD")
				.id("HSJD")
				.createdAt(new Date())
				.creator("user")
				.updatedAt(new Date())
				.updater("user")
				.build();
	}

	@Test
	public void testHashCode() {
		assertNotNull(entity.hashCode());
	}

	@Test
	public void testEqualsObjectNull() {
		assertTrue(!entity.equals(null));
	}
	
	@Test
	public void testEqualsObjectNew() {
		assertTrue(!entity.equals(new Object()));
	}
	
	@Test
	public void testEqualsObjectBookModel() {
		assertTrue(entity.equals(entity));
	}
	
	@SuppressWarnings("unlikely-arg-type")
	@Test
	public void testEqualsObjectNoBookModel() {
		assertTrue(!entity.equals(Integer.getInteger("12")));
	}

	@Test
	public void testEqualsNewBookModel() {
		assertTrue(!entity.equals(BookModel.builder().build()));
	}
	
	@Test
	public void testBuildToString() {
		assertNotNull(BookModel.builder().toString());
	}
	
	@Test
	public void testAllArgsConstructor() {
		assertNotNull(new BookModel("AKSJDH", "KLASDJKL", "asd", new Date(), "asd", new Date(), "asd"));
	}

}

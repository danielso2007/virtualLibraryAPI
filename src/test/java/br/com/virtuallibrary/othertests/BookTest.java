package br.com.virtuallibrary.othertests;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import br.com.virtuallibrary.entity.Book;

@RunWith(SpringRunner.class)
public class BookTest {

	private Book entity;
	
	@Before
	public void setUp() throws Exception {
		entity = Book.builder()
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
	public void testEqualsObjectBook() {
		assertTrue(entity.equals(entity));
	}
	
	@Test
	public void testEqualsObjectNoBook() {
		assertTrue(!entity.equals(Integer.getInteger("12")));
	}

	@Test
	public void testEqualsNewBook() {
		assertTrue(!entity.equals(Book.builder().build()));
	}
	
	@Test
	public void testBuildToString() {
		assertNotNull(Book.builder().toString());
	}
	
	@Test
	public void testAllArgsConstructor() {
		assertNotNull(new Book("AKSJDH", "KLASDJKL"));
	}
}

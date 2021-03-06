package br.com.virtuallibrary.othertests;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import br.com.virtuallibrary.entity.Rating;

@RunWith(SpringRunner.class)
public class RatingTest {

	private Rating entity;

	@Before
	public void setUp() throws Exception {
		entity = Rating.builder()
				.id("KJASHDKJSAHD")
				.bookId("ASLKDJSALKDJ")
				.stars(5)
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
		assertEquals(false, entity.equals(null));
	}

	@Test
	public void testEqualsObjectNew() {
		assertEquals(false, entity.equals(new Object()));
	}

	@Test
	public void testEqualsObjectBook() {
		assertTrue(entity.equals(entity));
	}
	
	@SuppressWarnings("unlikely-arg-type")
	@Test
	public void testEqualsObjectNoRating() {
		assertEquals(false, entity.equals(Integer.getInteger("12")));
	}

	@Test
	public void testEqualsNewBook() {
		assertEquals(false, entity.equals(Rating.builder().build()));
	}
	
	@Test
	public void testBuildToString() {
		assertNotNull(Rating.builder().toString());
	}
	
	@Test
	public void testAllArgsConstructor() {
		assertNotNull(new Rating("AKSJDHKASJDH", 5));
	}

}

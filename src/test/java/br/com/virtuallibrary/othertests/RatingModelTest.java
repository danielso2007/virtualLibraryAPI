package br.com.virtuallibrary.othertests;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;

import br.com.virtuallibrary.rest.hateoas.model.RatingModel;

public class RatingModelTest {

	private RatingModel entity;

	@Before
	public void setUp() throws Exception {
		entity = RatingModel.builder()
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
	
	@SuppressWarnings("unlikely-arg-type")
	@Test
	public void testEqualsObjectNoRatingModel() {
		assertTrue(!entity.equals(Integer.getInteger("12")));
	}

	@Test
	public void testEqualsNewBook() {
		assertTrue(!entity.equals(RatingModel.builder().build()));
	}
	
	@Test
	public void testBuildToString() {
		assertNotNull(RatingModel.builder().toString());
	}
	
	@Test
	public void testAllArgsConstructor() {
		assertNotNull(new RatingModel("asdkkl", "AKSJDHKASJDH", 5, new Date(), "asd", new Date(), "asd"));
	}

}

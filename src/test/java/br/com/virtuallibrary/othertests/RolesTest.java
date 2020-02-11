package br.com.virtuallibrary.othertests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import br.com.virtuallibrary.enums.Roles;

@RunWith(SpringRunner.class)
public class RolesTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testGetDescriptionADMIN() {
		assertEquals(Roles.ADMIN.getDescription(), "Administrador");
	}
	
	@Test
	public void testGetDescriptionUSER() {
		assertEquals(Roles.USER.getDescription(), "Usuário");
	}

}

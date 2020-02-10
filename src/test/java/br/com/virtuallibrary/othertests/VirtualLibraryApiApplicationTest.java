package br.com.virtuallibrary.othertests;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import br.com.virtuallibrary.VirtualLibraryApiApplication;

@RunWith(SpringRunner.class)
public class VirtualLibraryApiApplicationTest {

	@Test
	public void testMain() {
		VirtualLibraryApiApplication.main(new String[] {});		
	}

}

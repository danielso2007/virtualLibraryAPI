package br.com.virtuallibrary.suites;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import br.com.virtuallibrary.othertests.BookTest;
import br.com.virtuallibrary.othertests.GenericsInfoTest;
import br.com.virtuallibrary.othertests.RatingTest;
import br.com.virtuallibrary.othertests.RolesTest;
import br.com.virtuallibrary.othertests.VirtualLibraryApiApplicationTest;

@RunWith(Suite.class)
@Suite.SuiteClasses({ RolesTest.class, VirtualLibraryApiApplicationTest.class, GenericsInfoTest.class, BookTest.class,
		RatingTest.class })
public class OtherSuiteTest {

}

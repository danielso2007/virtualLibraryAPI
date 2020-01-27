package br.com.virtuallibrary.suites;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import br.com.virtuallibrary.services.BookServicesTest;
import br.com.virtuallibrary.services.RatingServiceTest;

@RunWith(Suite.class)
@Suite.SuiteClasses({ BookServicesTest.class, RatingServiceTest.class })
public class ServicesSuiteTest {
	
}

package br.com.virtuallibrary.suites;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import br.com.virtuallibrary.controllers.BookControllerTest;
import br.com.virtuallibrary.controllers.RatingControllerTest;
import br.com.virtuallibrary.controllers.commons.SeachControllerTest;

@RunWith(Suite.class)
@Suite.SuiteClasses({ BookControllerTest.class, RatingControllerTest.class, SeachControllerTest.class })
public class ControllersSuiteTest {

}

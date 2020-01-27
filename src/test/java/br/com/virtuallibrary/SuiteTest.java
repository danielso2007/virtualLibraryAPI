package br.com.virtuallibrary;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import br.com.virtuallibrary.suites.ControllersSuiteTest;
import br.com.virtuallibrary.suites.ServicesSuiteTest;

@SpringBootTest
@RunWith(Suite.class)
@Suite.SuiteClasses({
        ServicesSuiteTest.class,
        ControllersSuiteTest.class
})
@ActiveProfiles("test")
public class SuiteTest {

}

package com.accenture.taskmanager;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

//javadoc
/**
 * The TaskmanagerApplicationTests class is a test class for the Taskmanager application.
 * It uses the Spring Boot testing framework to load the application context and verify that it loads successfully.
 * The @ActiveProfiles annotation is used to specify the "test" profile for this test class.
 * 
 * @author Javier Roldán
 * @version 1.0
 */
@ActiveProfiles("test")
@SpringBootTest
class TaskmanagerApplicationTests {

	/**
	 * Test method to verify that the application context loads successfully.
	 * This method is executed as part of the test suite and will pass if the application context is loaded without any exceptions.
	 */
	@Test
	void contextLoads() {
	}

}

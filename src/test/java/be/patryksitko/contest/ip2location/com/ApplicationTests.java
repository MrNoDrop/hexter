package be.patryksitko.contest.ip2location.com;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import be.hexter.hexter.Application;

@SpringBootTest(classes = Application.class)
@ActiveProfiles("test")
class ApplicationTests {

	@Test
	void contextLoads() {
		// Test that the application context loads successfully
		System.out.println("✅ Application context loaded successfully");
	}

	@Test
	void applicationShouldStart() {
		// Verify the Spring Boot application can start without errors
		System.out.println("✅ Spring Boot application initialized");
	}

}

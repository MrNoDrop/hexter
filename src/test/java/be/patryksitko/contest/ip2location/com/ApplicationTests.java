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
	}

}

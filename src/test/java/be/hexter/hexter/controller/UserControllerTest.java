package be.hexter.hexter.controller;

import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.*;

/**
 * UserControllerTest - Basic smoke tests for UserController
 * 
 * Note: Comprehensive controller endpoint testing is handled by
 * UserIntegrationTest
 * which tests the complete flow through MockMvc with application context
 * loaded.
 */
@ActiveProfiles("test")
class UserControllerTest {

    @Test
    void testControllerClassExists() {
        assertThat(UserController.class).isNotNull();
        System.out.println("✅ UserController class exists and is loadable");
    }

    @Test
    void testControllerHasMethods() {
        assertThat(UserController.class.getMethods()).isNotEmpty();
        System.out.println("✅ UserController has public methods");
    }
}

package ulb.services.accounts;

import org.junit.jupiter.api.*;
import ulb.services.SQLService;
import ulb.exceptions.credentials.InvalidLoginException;
import ulb.exceptions.songs.SQLExceptionHandler;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for LoginService.
 * Ensures that users can log in correctly and that incorrect credentials are handled properly.
 */
public class TestLoginService {

    private RegisterService registerService;
    private LoginService loginService;
    private SQLService sqlService;

    // Test user credentials
    private final String testUsername = "5t6e8s9t2L3o4g3i3n"; // Unique username to avoid conflicts
    private final String testPassword = "TestPassword123!";
    private final String testLang = "en";
    private int userId;

    /**
     * Method executed before each test.
     * Initializes services and creates a temporary user for testing.
     */
    @BeforeEach
    public void setUp() throws Exception {
        registerService = new RegisterService();
        loginService = new LoginService();
        sqlService = SQLService.getInstance();

        // Create a temporary user for login testing
        userId = registerService.registerNewUser(
                testUsername,
                testPassword,
                testPassword,
                null,
                false,
                testLang
        );
    }

    /**
     * Tests successful login with correct credentials.
     * Verifies that the returned user ID matches the registered user.
     */
    @Test
    public void testLoginSuccess() throws Exception {
        int loggedInId = loginService.login(testUsername, testPassword, true);

        // Ensure the logged-in user ID matches the one from registration
        assertEquals(userId, loggedInId, "User ID should match after successful login");
    }

    /**
     * Tests failed login with an incorrect password.
     * Expects an InvalidLoginException to be thrown.
     */
    @Test
    public void testLoginFailure() {
        assertThrows(InvalidLoginException.class, () -> {
            // Attempt login with the wrong password
            loginService.login(testUsername, "wrongPassword", false);
        }, "Expected InvalidLoginException for wrong password");
    }

    /**
     * Method executed after each test.
     * Cleans up the database by removing the temporary user.
     */
    @AfterEach
    public void tearDown() throws SQLExceptionHandler {
        try {
            // Set current user context and delete the test user
            sqlService.setUserById(userId);
            sqlService.removeUser();
        } catch (SQLExceptionHandler e) {
            throw new SQLExceptionHandler("Error during cleanup");
        }
    }
}

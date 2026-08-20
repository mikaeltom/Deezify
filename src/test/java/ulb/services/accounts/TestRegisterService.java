package ulb.services.accounts;

import org.junit.jupiter.api.*;
import ulb.services.SQLService;
import ulb.exceptions.songs.SQLExceptionHandler;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for the RegisterService.
 * Verifies that a new user can be registered and properly cleaned up afterward.
 */
public class TestRegisterService {

    private RegisterService registerService;
    private SQLService sqlService;

    // Test data for user registration
    private final String testUsername = "5t6e8s9t2L3o4g3i3n"; // Unique username for testing
    private final String testPassword = "TestPassword123!";
    private final String testLang = "en";
    private int userId;

    /**
     * Setup method executed before each test.
     * Initializes the register and SQL services.
     */
    @BeforeEach
    public void setUp() {
        registerService = new RegisterService();
        sqlService = SQLService.getInstance();
    }

    /**
     * Tests registering a new user and verifies the user is properly created in the database.
     */
    @Test
    public void testRegisterAndCleanupUser() throws Exception {
        // Step 1: Register a new user
        userId = registerService.registerNewUser(
                testUsername,
                testPassword,
                testPassword,
                null,
                false,
                testLang
        );

        // Assert that the returned user ID is valid (positive number)
        assertTrue(userId > 0, "User ID should be valid and positive");

        // Step 2: Verify the user exists in the database
        Integer fetchedId = sqlService.getUserId(testUsername);
        assertEquals(userId, fetchedId, "Fetched ID should match the registered user ID");
    }

    /**
     * Cleanup method executed after each test.
     * Deletes the test user from the database to ensure a clean state.
     */
    @AfterEach
    public void tearDown() throws SQLExceptionHandler {
        try {
            // Step 3: Set the current user context and remove the user
            sqlService.setUserById(userId);
            sqlService.removeUser();
        } catch (SQLExceptionHandler e) {
            throw new SQLExceptionHandler("Error during cleanup");
        }
    }
}

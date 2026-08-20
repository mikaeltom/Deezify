package ulb.services.accounts;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ulb.exceptions.credentials.InvalidPasswordException;
import ulb.exceptions.credentials.InvalidUsernameException;
import ulb.exceptions.credentials.NotMatchingPasswordsException;
import ulb.models.User;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for the AccountSettingsService class.
 * This class contains unit tests for the methods in the AccountSettingsService class.
 */
public class TestAccountSettingsService {

    private TestableAccountSettingsService service;

    @BeforeEach
    public void setUp() {
        service = new TestableAccountSettingsService(); // Ensure the user is initialized here
    }

    /**
     * Test username existence check.
     */
    @Test
    public void testUpdateUserUsernameExists() {
        // Simulate an existing username
        assertThrows(InvalidUsernameException.class, () ->
                service.updateUser("existingUser", "secure123", "secure123", "img.jpg", false));
    }

    /**
     * Test update password mismatch.
     */
    @Test
    public void testUpdateUserPasswordMismatch() {
        // Simulate password mismatch
        assertThrows(NotMatchingPasswordsException.class, () ->
                service.updateUser("newUser", "abc123", "xyz123", "img.jpg", true));
    }

    /**
     * Test password too short.
     */
    @Test
    public void testUpdateUserPasswordTooShort() {
        // Simulate password too short
        assertThrows(InvalidPasswordException.class, () ->
                service.updateUser("newUser", "123", "123", "img.jpg", true));
    }

    /**
     * Test invalid username format.
     */
    @Test
    public void testUpdateUserInvalidUsernameFormat() {
        // Simulate an invalid username format
        assertThrows(InvalidUsernameException.class, () ->
                service.updateUser("u!", "secure123", "secure123", "img.jpg", true));
    }

    /**
     * Test update user staulogged option
     */
    @Test
    public void testUpdateUserChangeStayLoggedOnly() {
        assertFalse(service.getCurrentUser().isStayLogged());

        assertDoesNotThrow(() ->
                service.updateUser("currentUser", "", "", "old/path.jpg", true)
        );

        assertTrue(service.getCurrentUser().isStayLogged());
    }

    /**
     * Test update user null password with no changes.
     */
    @Test
    public void testUpdateUserNullPasswordAcceptedIfUnchanged() {
        assertDoesNotThrow(() ->
                service.updateUser("currentUser", null, null, null, false)
        );
    }

    /**
     * Test update user with all valid parameters.
     */
    @Test
    public void testRemoveUser() {
        // Remove the user and assert it's null
        service.removeUser();
        assertNull(service.getCurrentUser());
    }

    /**
     * Fake AccountSettingsService for testing purposes.
     */
    static class TestableAccountSettingsService extends AccountSettingsService {

        // Override the constructor to simulate the user being fetched correctly from the DB
        public TestableAccountSettingsService() {
            this.user = new User(1, "currentUser", "English", false, "old/path.jpg"); // Initialize the user
        }

        @Override
        protected void checkIfUsernameExists(String username) throws InvalidUsernameException {
            if ("existingUser".equals(username)) {
                throw new InvalidUsernameException("invalid_username_exists_exception");
            }
        }

        @Override
        protected void updateIsStayLogged(int userId, boolean stayLogged) {
            // Simulating updating the user's stay logged status
            user = new User(user.getId(), user.getUsername(), user.getLanguage(), stayLogged, user.getProfileImagePath());
        }

        @Override
        protected void checkUsernameFormat(String username) throws InvalidUsernameException {
            // Simulate format validation
            if (!username.matches("^[a-zA-Z0-9]{4,}$")) {
                throw new InvalidUsernameException("invalid_username_characters_exception");
            }
        }

        @Override
        protected void checkIfPasswordLength(String password) throws InvalidPasswordException {
            // Simulate password length check
            if (password.length() < 6) {
                throw new InvalidPasswordException("password_too_short_exception");
            }
        }

        @Override
        public void removeUser() {
            // Simulate user removal (nullify the user)
            this.user = null;
        }
    }
}

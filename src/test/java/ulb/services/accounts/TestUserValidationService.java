package ulb.services.accounts;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ulb.exceptions.BannedWordException;
import ulb.exceptions.credentials.InvalidPasswordException;
import ulb.exceptions.credentials.InvalidUsernameException;
import ulb.exceptions.credentials.NotMatchingPasswordsException;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Test class for UserValidationService.
 * This class contains unit tests for the methods in the UserValidationService class.
 */
public class TestUserValidationService {

    private TestableUserValidationService validationService;

    @BeforeEach
    public void setUp() {
        validationService = new TestableUserValidationService();
    }

    /**
     * Test for matching passwords.
     * This test checks if the method correctly identifies matching and non-matching passwords.
     */
    @Test
    public void testMatchingPasswordsValid() {
        assertDoesNotThrow(() -> validationService.testCheckMatchingPasswords("password", "password"));
    }

    /**
     * Test for non-matching passwords.
     * This test checks if the method throws an exception when passwords do not match.
     */
    @Test
    public void testMatchingPasswordsInvalid() {
        assertThrows(NotMatchingPasswordsException.class, () ->
                validationService.testCheckMatchingPasswords("pass1", "pass2"));
    }

    /**
     * Test for password length.
     * This test checks if the method correctly identifies valid and invalid password lengths.
     */
    @Test
    public void testPasswordLengthValid() {
        assertDoesNotThrow(() -> validationService.testCheckIfPasswordLength("longenough"));
    }

    /**
     * Test for invalid password length.
     * This test checks if the method throws an exception for passwords that are too short.
     */
    @Test
    public void testPasswordLengthInvalid() {
        assertThrows(InvalidPasswordException.class, () ->
                validationService.testCheckIfPasswordLength("123"));
    }

    /**
     * Test for username format.
     * This test checks if the method correctly identifies valid and invalid username formats.
     */
    @Test
    public void testUsernameFormatValid() {
        assertDoesNotThrow(() -> validationService.testCheckUsernameFormat("ValidUser123"));
    }

    /**
     * Test for invalid username format.
     * This test checks if the method throws an exception for usernames is too short.
     */
    @Test
    public void testUsernameFormatInvalidTooShort() {
        assertThrows(InvalidUsernameException.class, () ->
                validationService.testCheckUsernameFormat("ab1"));
    }

    /**
     * Test for invalid username format with special characters and banned words.
     */
    @Test
    public void testUsernameFormatContainsBannedWord() {
        assertThrows(BannedWordException.class, () ->
                validationService.testCheckUsernameFormat("fuck"));
    }

    /**
     * Testable subclass of UserValidationService for testing purposes.
     */
    static class TestableUserValidationService extends UserValidationService {
        public void testCheckMatchingPasswords(String p1, String p2) throws NotMatchingPasswordsException {
            checkMatchingPasswords(p1, p2);
        }

        public void testCheckIfPasswordLength(String p) throws InvalidPasswordException {
            checkIfPasswordLength(p);
        }

        public void testCheckUsernameFormat(String username) throws InvalidUsernameException, IOException, BannedWordException {
            checkUsernameFormat(username);
        }
    }
}

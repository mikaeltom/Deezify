package ulb.models;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test class for the User class.
 * This class contains unit tests for the methods in the User class.
 */
public class TestUser {

    /**
     * Test for the constructor and fields of the User class.
     */
    @Test
    void testConstructorWithIdUsernameLanguage() {
        User user = new User(1, "testUser", "English", false, "image");
        assertEquals(1, user.getId());
    }

    /**
     * Test for the constructor with username and language.
     */
    @Test
    void testConstructorWithUsernameLanguage() {
        User user = new User(0, "testUser", "French", false, "image");
        assertEquals(0, user.getId());
    }

    /**
     * Test for the constructor with id and username.
     */
    @Test
    void testConstructorWithIdUsername() {
        User user = new User(2, "anotherUser", "French", false, "image");
        assertEquals(2, user.getId());
    }
}

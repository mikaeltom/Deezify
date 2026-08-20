package ulb.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ulb.exceptions.BannedWordException;

import java.io.FileNotFoundException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for the BannedWordService class.
 * This class contains unit tests for the methods in the BannedWordService class.
 */
public class TestBannedWordService {

    private BannedWordService bannedWordService;

    @BeforeEach
    public void setUp() throws FileNotFoundException {
        bannedWordService = new BannedWordService();
    }

    /**
     * Test if the banned word is not found in the input string.
     */
    @Test
    public void testContainsBannedWordsWordAsSubstringDoesNotThrow() {
        String input = "shitty situation";
        assertDoesNotThrow(() -> bannedWordService.containsBannedWords(input));
    }

    /**
     * Test if the banned word is found in the input string.
     */
    @Test
    public void testContainsBannedWordsBannedWordWithPunctuationDoesNotThrow() {
        String input = "damn, this is wild";
        assertThrows(BannedWordException.class, () -> bannedWordService.containsBannedWords(input));
    }

    /**
     * Test if multiple banned words is found in the input string.
     */
    @Test
    public void testContainsBannedWordsMultipleWhitespaceHandledThrowsException() {
        String input = "fuck\tshit\nok";
        assertThrows(BannedWordException.class, () -> bannedWordService.containsBannedWords(input));
    }

    /**
     * Test if there is no banned word in the input string.
     */
    @Test
    public void testContainsBannedWordsWithoutBannedWordNoException() {
        String input = "this is a safe sentence";
        assertDoesNotThrow(() -> {
            bannedWordService.containsBannedWords(input);
        });
    }

    /**
     * Test if null input does not throw an exception.
     */
    @Test
    public void testContainsBannedWordsWithNullInputNoException() {
        assertDoesNotThrow(() -> {
            bannedWordService.containsBannedWords(null);
        });
    }

    /**
     * Test if empty input does not throw an exception.
     */
    @Test
    public void testContainsBannedWordsWithEmptyInputNoException() {
        assertDoesNotThrow(() -> {
            bannedWordService.containsBannedWords("   ");
        });
    }
}

package ulb.services;

import ulb.exceptions.BannedWordException;
import ulb.repositories.BannedWordRepository;

import java.io.FileNotFoundException;
import java.util.List;

/**
 * Manages the list of banned words that should not be used in tags.
 */
public class BannedWordService {
    private final List<String> bannedWords;

    public BannedWordService() throws FileNotFoundException {
        BannedWordRepository repository = new BannedWordRepository();
        this.bannedWords = repository.getInvalidWords();
    }

    /**
     * Checks if a tag name contains any banned words.
     *
     * @param words The word to check.
     */
    public void containsBannedWords(String words) throws BannedWordException {
        if (words == null || words.trim().isEmpty()) {
            return; // Null or empty input is handled somewhere else
        }
        for (String word : words.toLowerCase().split("\\s+")) {
            // Replaces any character that is not a-z by and empty string
            String sanitized = word.replaceAll("[^a-z]", "");
            if (bannedWords.contains(sanitized)) {
                throw new BannedWordException("banned_word_exception", sanitized);
            }
        }
    }
}

package ulb.repositories;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

/**
 * Repository for managing banned words.
 * <p>
 * This class is responsible for loading a list of banned words from a JSON file
 * and providing access to that list.
 * <p>
 * The JSON file should contain an array of invalid words under the key "invalidWords".
 */
public class BannedWordRepository {
    private final List<String> invalidWords;

    /**
     * Loads the list of banned words from a JSON file.
     */
    public BannedWordRepository() throws FileNotFoundException {
        File file = new File("maven-config/banned-tag.json");
        this.invalidWords = loadInvalidWordsFromFile(file);
    }

    /**
     * Loads the list of invalid words from a JSON file.
     *
     * @param filePath Path to the JSON file.
     * @return List of invalid words.
     */
    private List<String> loadInvalidWordsFromFile(File filePath) throws FileNotFoundException {
        List<String> result = new ArrayList<>();
        FileInputStream inputStream = new FileInputStream(filePath);
        JSONTokener tokener = new JSONTokener(inputStream);
        JSONObject root = new JSONObject(tokener);

        if (root.has("invalidWords")) {
            JSONArray wordsArray = root.getJSONArray("invalidWords");
            for (int i = 0; i < wordsArray.length(); i++) {
                result.add(wordsArray.getString(i));
            }
        }

        return result;
    }

    /**
     * Return the list of invalid words in String
     *
     * @return List of invalid words
     */
    public List<String> getInvalidWords() {
        return invalidWords;
    }
}

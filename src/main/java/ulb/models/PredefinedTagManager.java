package ulb.models;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

/**
 * Loads predefined tags from a JSON file and makes them accessible.
 */
public class PredefinedTagManager {
    private final List<String> predefinedTags;

    public PredefinedTagManager() throws FileNotFoundException {
        File file = new File("maven-config/predefined-tag.json");
        this.predefinedTags = loadTagsFromFile(file);
    }

    /**
     * Loads the predefined tags from a JSON file and returns them as a list of tag names.
     * <p>
     * Exceptions:
     * - FileNotFoundException: If the file does not exist.
     *
     * @param file The JSON file containing the predefined tags.
     * @return A list of tag names.
     */
    private List<String> loadTagsFromFile(File file) throws FileNotFoundException {
        List<String> result = new ArrayList<>();
        FileInputStream inputStream = new FileInputStream(file);
        JSONTokener tokener = new JSONTokener(inputStream);
        JSONObject root = new JSONObject(tokener);

        if (root.has("predefinedTags")) {
            JSONArray tagsArray = root.getJSONArray("predefinedTags");
            for (int i = 0; i < tagsArray.length(); i++) {
                result.add(tagsArray.getString(i));
            }
        }

        return result;
    }

    /**
     * Returns the list of predefined tags.
     *
     * @return a List of tag names.
     */
    public List<String> getPredefinedTags() {
        return predefinedTags;
    }
}

package ulb.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for the PredefinedTagManager class.
 * This class contains unit tests for the methods in the PredefinedTagManager class.
 */
public class TestPredefinedTagManager {

    private PredefinedTagManager tagManager;

    @BeforeEach
    public void setUp() throws FileNotFoundException {
        tagManager = new PredefinedTagManager();
    }

    /**
     * Test for the loadTagsFormFile method.
     */
    @Test
    public void testLoadTagsFromFile() {
        List<String> tags = tagManager.getPredefinedTags();

        assertNotNull(tags, "The list of predefined tags should not be null.");
        assertTrue(tags.size() > 0, "The predefined tags list should not be empty.");
    }

    /**
     * Test if predefined tags exist in the list.
     */
    @Test
    public void testPredefinedTagsExist() {
        List<String> tags = tagManager.getPredefinedTags();

        // Check if the predefined tags contain expected tags
        assertTrue(tags.contains("Alternative"), "The predefined tags should contain 'Alternative'.");
        assertTrue(tags.contains("Blues"), "The predefined tags should contain 'Blues'.");
        assertTrue(tags.contains("Pop"), "The predefined tags should contain 'Pop'.");
        assertTrue(tags.contains("Rock"), "The predefined tags should contain 'Rock'.");
        assertTrue(tags.contains("Jazz"), "The predefined tags should contain 'Jazz'.");
    }

    /**
     * Test if the predefined tags are loaded correctly when the file exists
     */
    @Test
    public void testPredefinedTagsFromValidFile() {
        File file = new File("maven-config/predefined-tag.json");

        // Check if the predefined tag file exists and is accessible
        assertTrue(file.exists(), "The predefined tag file should exist.");

        // Check the content of the predefined tags
        List<String> tags = tagManager.getPredefinedTags();
        assertNotNull(tags, "The predefined tags should be loaded correctly from the JSON file.");
        assertTrue(tags.size() > 0, "There should be predefined tags available in the list.");
    }

    /**
     * Test if no duplicate tags exist in the predefined tags list.
     */
    @Test
    public void testNoDuplicateTags() {
        List<String> tags = tagManager.getPredefinedTags();
        long uniqueCount = tags.stream().distinct().count();
        assertEquals(uniqueCount, tags.size(), "Tag list should not contain duplicates.");
    }
}

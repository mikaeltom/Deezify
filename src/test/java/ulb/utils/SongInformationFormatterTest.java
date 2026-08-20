package ulb.utils;

import javafx.util.Duration;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ulb.models.Song;
import ulb.models.Tag;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for the SongInformationFormatter class.
 * This class contains unit tests for the methods in the SongInformationFormatter class.
 */
class SongInformationFormatterTest {

    @BeforeAll
    static void setupLocale() {
        // Ensure tests use a predictable language for ResourceBundle (like "unknown", "no_tags")
        Locale.setDefault(Locale.ENGLISH);
    }

    /**
     * Test for the getFormattedString method.
     * This method should return a localized string for null or "Unknown" input.
     */
    @Test
    void testGetFormattedStringWithNull() {
        // Should return the localized "unknown" string when input is null
        String result = SongInformationFormatter.getFormattedString(null);
        assertEquals(I18n.getBundle().getString("unknown"), result);
    }

    /**
     * Test for the getFormattedString method.
     * This method should return a localized string for "Unknown" input.
     */
    @Test
    void testGetFormattedStringWithUnknownLiteral() {
        // Should return the localized "unknown" string when input equals "Unknown"
        String result = SongInformationFormatter.getFormattedString("Unknown");
        assertEquals(I18n.getBundle().getString("unknown"), result);
    }

    /**
     * Test for the getFormattedString method.
     * This method should return the original string if it's not null or "Unknown".
     */
    @Test
    void testGetFormattedStringWithValidInput() {
        // Should return the original string if it's not null or "Unknown"
        String result = SongInformationFormatter.getFormattedString("My Song");
        assertEquals("My Song", result);
    }
    /**
     * Test for the getFormattedTag method with null tags.
     */
    @Test
    void testGetFormattedTagsWithNullTags() {
        // Should return the localized "no_tags" string when song has no tags (null)
        Song song = new Song(0, "title", "artist", "album", Duration.seconds(180), "", new ArrayList<>(), "", "", "");
        song.setTags(null);
        String result = SongInformationFormatter.getFormattedTags(song);
        assertEquals(I18n.getBundle().getString("no_tags"), result);
    }

    /**
     * Test for the getFormattedTags method with no tags
     */
    @Test
    void testGetFormattedTagsWithEmptyTags() {
        // Should return the localized "no_tags" string when song tags are empty
        Song song = new Song(0, "title", "artist", "album", Duration.seconds(180), "", new ArrayList<>(), "", "", "");
        song.setTags(Collections.emptyList());
        String result = SongInformationFormatter.getFormattedTags(song);
        assertEquals(I18n.getBundle().getString("no_tags"), result);
    }

    /**
     * Test for the getFormattedTags method with tags
     */
    @Test
    void testGetFormattedTagsWithTags() {
        // Should return the tag list as a comma-separated string with no brackets
        Song song = new Song(0, "title", "artist", "album", Duration.seconds(180), "", new ArrayList<>(), "", "", "");
        song.setTags(Arrays.asList(
                new Tag("rock"),
                new Tag("pop"),
                new Tag("jazz")
        ));
        String result = SongInformationFormatter.getFormattedTags(song);
        assertEquals("rock, pop, jazz", result);
    }
}

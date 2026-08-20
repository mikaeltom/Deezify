package ulb.services.options;

import org.junit.jupiter.api.*;
import ulb.dtos.TagDTO;
import ulb.exceptions.BannedWordException;
import ulb.exceptions.songs.SQLExceptionHandler;
import ulb.exceptions.tag.InvalidTagError;
import ulb.models.*;
import ulb.models.Tag;
import ulb.services.BannedWordService;
import ulb.services.SQLService;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for the TagService class.
 * This class contains unit tests for the methods in the TagService class.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TestTagService {

    private SQLService sqlService;
    private TagService tagService;
    private Song song;
    private int songId;
    private int userId;
    private final List<Integer> addedTagIds = new ArrayList<>();

    @BeforeAll
    void setUp() throws SQLExceptionHandler, FileNotFoundException {
        sqlService = SQLService.getInstance();
        BannedWordService bannedWordService = new BannedWordService();
        PredefinedTagManager predefinedTagManager = new PredefinedTagManager();
        tagService = new TagService(sqlService, bannedWordService, predefinedTagManager);

        // Create user
        String username = "test383User230Tag123";
        sqlService.addNewUser(username, "en", "password", "path/to/image.jpg");
        userId = sqlService.getUserId(username);
        User user = sqlService.getUserByID(userId);
        sqlService.setUser(user);

        // Create song
        sqlService.addNewSong("path/to/song.mp3", "Test Song Tag", 180, "mp3", "Test Artist", "Test Album", null, null, null);
        songId = sqlService.getSongId("Test Song Tag");
        song = sqlService.getSong(songId);
    }

    @AfterAll
    void tearDown() throws SQLExceptionHandler {
        // Clean up tags
        for (Integer tagId : addedTagIds) {
            try {
                sqlService.removeTag(tagId);
            } catch (SQLExceptionHandler ignored) {
                // Tag may already have been deleted if unused
            }
        }

        // Clean up song and user
        sqlService.removeSong(songId);
        sqlService.setUserById(userId);
        sqlService.removeUser();
    }

    /**
     * Helper method to track added tags for cleanup.
     * @param tagName the name of the tag to track
     * @throws SQLExceptionHandler if there is an error with SQL operations
     */
    private void trackTag(String tagName) throws SQLExceptionHandler {
        int tagId = sqlService.getTagId(tagName);
        if (!addedTagIds.contains(tagId)) {
            addedTagIds.add(tagId);
        }
    }

    /**
     * Test the addition of a predefined tag.
     * @throws SQLExceptionHandler if there is an error with SQL operations
     */
    @Test
    void testInsertTag_valid() throws SQLExceptionHandler {
        String tagName = "testTag";
        assertDoesNotThrow(() -> tagService.insertTag(new Tag(tagName), false));
        trackTag(tagName);
    }

    /**
     * Test the addition of a predefined tag that already exists.
     * @throws SQLExceptionHandler if there is an error with SQL operations
     */
    @Test
    void testInsertTag_bannedWord() {
        assertThrows(BannedWordException.class, () ->
                tagService.insertTag(new Tag("fuck"), false)
        );
    }

    /**
     * Test the addition of a custom tag.
     * @throws SQLExceptionHandler if there is an error with SQL operations
     */
    @Test
    void testGetCustomTags() throws SQLExceptionHandler {
        List<? extends TagDTO> tags = tagService.getCustomTags();
        assertNotNull(tags);
    }

    /**
     * Test the addition of tags to a song.
     * @throws SQLExceptionHandler if there is an error with SQL operations
     */
    @Test
    void testAddTagToSongValid() throws SQLExceptionHandler, BannedWordException {
        String tagName = "RockTag";
        tagService.insertTag(new Tag(tagName), false);
        trackTag(tagName);
        assertDoesNotThrow(() -> tagService.addTagToSong(song, tagName));
        assertTrue(song.getTags().stream().anyMatch(t -> t.getName().equals(tagName)));
    }

    /**
     * Test the addition of a tag to a song that already has the tag.
     * @throws SQLExceptionHandler if there is an error with SQL operations
     */
    @Test
    void testAddTagToSongDuplicate() throws SQLExceptionHandler, BannedWordException, InvalidTagError {
        String tagName = "DuplicateTag";
        tagService.insertTag(new Tag(tagName), false);
        trackTag(tagName);
        tagService.addTagToSong(song, tagName);
        assertThrows(InvalidTagError.class, () -> tagService.addTagToSong(song, tagName));
    }

    /**
     * Test the addition of a tag to a song with a banned word.
     * @throws SQLExceptionHandler if there is an error with SQL operations
     */
    @Test
    void testRemoveTagFromSong() throws SQLExceptionHandler, InvalidTagError, BannedWordException {
        String tagName = "RemoveMeTag";
        tagService.insertTag(new Tag(tagName), false);
        trackTag(tagName);
        tagService.addTagToSong(song, tagName);
        assertDoesNotThrow(() -> tagService.removeTagFromSong(song, tagName));
        assertTrue(song.getTags().stream().noneMatch(t -> t.getName().equals(tagName)));
    }

    /**
     * Test the submission of a custom tag.
     * @throws SQLExceptionHandler if there is an error with SQL operations
     */
    @Test
    void testSubmitCustomTagValid() throws SQLExceptionHandler {
        String tagName = "UniqueCustomTag";
        assertDoesNotThrow(() -> tagService.submitCustomTag(song, tagName));
        trackTag(tagName);
        assertTrue(song.getTags().stream().anyMatch(t -> t.getName().equals(tagName)));
    }
}

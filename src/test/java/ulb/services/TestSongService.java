package ulb.services;

import org.junit.jupiter.api.*;
import ulb.exceptions.songs.SQLExceptionHandler;
import ulb.models.Song;
import ulb.models.User;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for the SongService class.
 * This class contains unit tests for the methods in the SongService class.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TestSongService {

    private SQLService sqlService;
    private SongService songService;
    private int songId;
    private int userId;
    private final String originalTitle = "Test Song Metadata";

    @BeforeAll
    void setup() throws SQLExceptionHandler {
        sqlService = SQLService.getInstance();
        songService = new SongService();

        // Create user and song for testing
        String username = "testUserMeta";
        sqlService.addNewUser(username, "en", "pass", null);
        userId = sqlService.getUserId(username);
        User user = sqlService.getUserByID(userId);
        sqlService.setUser(user);

        songService.addNewSong("path/song.mp3", originalTitle, 200, "mp3", "Original Artist", "Original Album", null, null, null);
        songId = sqlService.getSongId(originalTitle);
    }

    @AfterAll
    void cleanup() throws SQLExceptionHandler {
        sqlService.removeSong(songId);
        sqlService.setUserById(userId);
        sqlService.removeUser();
    }

    /**
     * Test the addition and deletion of metadata for a song.
     * @throws SQLExceptionHandler if there is an error with the SQL operations
     */
    @Test
    void testSetMetadata() throws SQLExceptionHandler {
        Song song = sqlService.getSong(songId);
        HashMap<String, String> metadata = new HashMap<>();
        metadata.put("Title", "Meta Title");
        metadata.put("Artist", "Meta Artist");
        metadata.put("Album", "Meta Album");
        metadata.put("Images", null); // optional

        assertDoesNotThrow(() -> songService.setMetadata(song, metadata));

        Song updated = sqlService.getSong(songId);
        assertEquals("Meta Title", updated.getTitle());
        assertEquals("Meta Artist", updated.getArtist());
        assertEquals("Meta Album", updated.getAlbum());

        // Reset for other tests
        songService.updateSongMetadata(song, originalTitle, "Original Artist", "Original Album");
    }

    /**
     * Test get orDefault method.
     */
    @Test
    void testGetOrDefault() {
        String defaultVal = "default";
        assertEquals("value", songService.getOrDefault("value", defaultVal));
        assertEquals("default", songService.getOrDefault("", defaultVal));
    }

    /**
     * Test the updateSongMetadata method.
     * @throws SQLExceptionHandler if there is an error with the SQL operations
     */
    @Test
    void testUpdateSongMetadataPartialUpdate() throws SQLExceptionHandler {
        Song song = sqlService.getSong(songId);

        songService.updateSongMetadata(song, "", "Updated Artist", "");

        Song updated = sqlService.getSong(songId);
        assertEquals("Updated Artist", updated.getArtist());
        assertEquals(originalTitle, updated.getTitle());
        assertEquals("Original Album", updated.getAlbum());

        // Reset
        songService.updateSongMetadata(song, originalTitle, "Original Artist", "Original Album");
    }

    /**
     * Test getSong method.
     */
    @Test
    void testGetSong() throws SQLExceptionHandler {
        Song song = songService.getSong(originalTitle);
        assertNotNull(song);
        assertEquals(originalTitle, song.getTitle());
    }

    /**
     * Test the adding of a new song.
     */
    @Test
    void testAddNewSong() throws SQLExceptionHandler {
        String newTitle = "Another Song Test";
        songService.addNewSong("song/2.mp3", newTitle, 300, "mp3", "Artist 2", "Album 2", null, null, null);
        int newId = sqlService.getSongId(newTitle);
        Song song = sqlService.getSong(newId);
        assertEquals(newTitle, song.getTitle());

        // Clean up
        sqlService.removeSong(newId);
    }
}

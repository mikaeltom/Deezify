package ulb.services.options;

import org.junit.jupiter.api.*;
import ulb.exceptions.songs.InvalidSongException;
import ulb.exceptions.songs.SQLExceptionHandler;
import ulb.models.Song;
import ulb.models.User;
import ulb.services.SQLService;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for the LyricsUploaderService class.
 * This class contains unit tests for the methods in the LyricsUploaderService class.
 */
public class TestLyricsUploaderService {

    private static SQLService sqlService;
    private static LyricsUploaderService lyricsService;
    private static Song testSong;
    private static int songId;
    private static int userId;

    @BeforeAll
    static void setUp() throws SQLExceptionHandler {
        sqlService = SQLService.getInstance();
        lyricsService = new LyricsUploaderService();

        // Create a fake user
        String username = "testUserLyrics123";
        String language = "en";
        String password = "password";
        String profileImagePath = "path/to/image.jpg";

        sqlService.addNewUser(username, language, password, profileImagePath);
        userId = sqlService.getUserId(username);
        User testUser = sqlService.getUserByID(userId);
        sqlService.setUser(testUser);

        // Create a fake song
        sqlService.addNewSong("FAKE/PATH/LYRICS_SONG", "LyricsSong123ABC", 200, "mp3", "Artist Lyrics", "Album Lyrics", null, null, null);
        songId = sqlService.getSongId("LyricsSong123ABC");
        testSong = sqlService.getSong(songId);
    }

    @AfterAll
    static void tearDown() throws SQLExceptionHandler {
        // Clean up: remove the song and user from the database
        sqlService.removeSong(songId);
        sqlService.setUserById(userId);
        sqlService.removeUser();
    }

    /**
     * Test the addition and deletion of lyrics for a song.
     * @throws IOException if there is an error creating the temporary file
     * @throws SQLExceptionHandler if there is an error with the SQL operations
     * @throws InvalidSongException if the song is invalid
     */
    @Test
    void testAddAndDeleteLyrics() throws IOException, SQLExceptionHandler, InvalidSongException {
        // Create a temporary lyrics file
        File tempLyrics = File.createTempFile("test-lyrics", ".txt");
        try (FileWriter writer = new FileWriter(tempLyrics)) {
            writer.write("These are some fake lyrics...");
        }

        // Add lyrics to the song
        lyricsService.addLyrics(tempLyrics, testSong);
        assertNotNull(testSong.getLyricsPath());
        assertFalse(testSong.getLyricsPath().isEmpty());

        // Delete the lyrics
        lyricsService.deleteLyrics(testSong);
        assertEquals("", testSong.getLyricsPath());

        // Clean up the temporary file
        tempLyrics.delete();
    }

    /**
     * Test the addition of lyrics when the song is invalid.
     */
    @Test
    void testDeleteLyricsWhenEmptyThrowsException() {
        // Force an empty lyrics path for the song
        testSong.setLyricsPath("");
        assertThrows(InvalidSongException.class, () -> lyricsService.deleteLyrics(testSong));
    }

    /**
     * Test the retrieval of the directory name.
     */
    @Test
    void testGetDirectoryName() {
        // Check that the directory name returned is as expected
        String expected = "src/main/resources/lyrics/LyricsUser" + sqlService.getUserId();
        assertEquals(expected, lyricsService.getDirectoryName());
    }
}

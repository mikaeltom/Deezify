package ulb.services.options;

import org.junit.jupiter.api.*;
import ulb.exceptions.songs.SQLExceptionHandler;
import ulb.models.Song;
import ulb.models.User;
import ulb.services.SQLService;
import ulb.services.collections.LibraryService;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for the MusicUploaderService class.
 * This class contains unit tests for the methods in the MusicUploaderService class.
 */
public class TestMusicUploaderService {

    private static SQLService sqlService;
    private static MusicUploaderService musicService;
    private static Song testSong;
    private static int songId;
    private static int userId;

    @BeforeAll
    static void setUp() throws SQLExceptionHandler {
        sqlService = SQLService.getInstance();
        LibraryService libraryService = new LibraryService();
        musicService = new MusicUploaderService(libraryService);

        // Create a fake user
        String username = "testUser32Mu34sic2331";
        String language = "en";
        String password = "password";
        String profileImagePath = "path/to/image.jpg";

        sqlService.addNewUser(username, language, password, profileImagePath);
        userId = sqlService.getUserId(username);
        User testUser = sqlService.getUserByID(userId);
        sqlService.setUser(testUser);

        // Create a fake song in the database manually (simulating that it's already known to the DB)
        sqlService.addNewSong("FAKE/PATH/MUSIC_SONG", "MusicSong789X34YZ", 240, "mp3", "Artist Music", "Album Music", null, null, null);
        songId = sqlService.getSongId("MusicSong789X34YZ");
        testSong = sqlService.getSong(songId);
    }


    /**
     * Test deleteSong method.
     * @throws IOException if there is an error creating the temporary file
     * @throws SQLExceptionHandler if there is an error with the SQL operations
     */
    @Test
    void testDeleteSong() throws IOException, SQLExceptionHandler {
        // Create a temporary file representing the song
        File tempMusic = File.createTempFile("test-music-delete", ".mp3");
        try (FileWriter writer = new FileWriter(tempMusic)) {
            writer.write("Fake audio content...");
        }

        // Set the path to the file in the song object (simulate stored song)
        testSong.setPath(tempMusic.getAbsolutePath());

        // Attempt to delete the song
        assertDoesNotThrow(() -> musicService.deleteSong(testSong));

        // Ensure the file is deleted
        assertFalse(tempMusic.exists());
        sqlService.setUserById(userId);
        sqlService.removeUser();
    }

    /**
     * Test getDirectoryName method.
     */
    @Test
    void testGetDirectoryName() {
        String expected = "src/main/resources/music/MusicUser" + sqlService.getUserId();
        assertEquals(expected, musicService.getDirectoryName());
    }
}

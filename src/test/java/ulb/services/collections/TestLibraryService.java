package ulb.services.collections;

import org.junit.jupiter.api.*;
import ulb.exceptions.songs.SQLExceptionHandler;
import ulb.models.Song;
import ulb.models.User;
import ulb.services.SQLService;
import java.io.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for the LibraryService class.
 * This class contains unit tests for the methods in the LibraryService class.
 */
public class TestLibraryService {

    private static SQLService sqlService;
    private static LibraryService libraryService;
    private static int userId;
    private static int addedSongId;
    private static File tempMusicFile;

    @BeforeAll
    static void setUp() throws SQLExceptionHandler, IOException {
        sqlService = SQLService.getInstance();
        libraryService = new LibraryService();

        // Create a test user
        String username = "libraryTestUser";
        String language = "en";
        String password = "password";
        String profileImagePath = "path/to/profile.jpg";

        sqlService.addNewUser(username, language, password, profileImagePath);
        userId = sqlService.getUserId(username);
        User testUser = sqlService.getUserByID(userId);
        sqlService.setUser(testUser);

        // Create a temporary fake music file
        File source = new File("src/test/resources/music/test.mp3");
        assertTrue(source.exists(), "Test MP3 file does not exist: " + source.getAbsolutePath());
        tempMusicFile = File.createTempFile("test-music", ".mp3");

        try (var in = new FileInputStream(source); var out = new FileOutputStream(tempMusicFile)) {
            in.transferTo(out);
        }
    }


    @AfterAll
    static void tearDown() throws SQLExceptionHandler {
        // Remove the song from the database if it was added
        if (addedSongId != 0) {
            sqlService.removeSong(addedSongId);
        }

        // Remove the test user
        sqlService.setUserById(userId);
        sqlService.removeUser();

        // Delete the temporary music file
        if (tempMusicFile.exists()) {
            tempMusicFile.delete();
        }
    }

    /**
     * Test for adding a song to the library.
     * Verifies that the song is added correctly and can be retrieved from the database.
     */
    @Test
    void testSetSongAddsSongToDatabase() throws SQLExceptionHandler, IOException {
        // Add the song using LibraryService
        libraryService.setSong(tempMusicFile);

        // Attempt to retrieve the song by its file name
        var musicInfo = ulb.models.LibraryModel.getMusicFile(tempMusicFile);
        String expectedTitle = musicInfo.musicName;
        addedSongId = sqlService.getSongId(expectedTitle);
        Song addedSong = sqlService.getSong(addedSongId);

        // Validate that the song was added correctly
        assertNotNull(addedSong);
        assertEquals(expectedTitle, addedSong.getTitle());
    }
}

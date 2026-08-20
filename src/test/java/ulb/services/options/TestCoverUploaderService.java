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
 * Test class for the CoverUploaderService class.
 * This class contains unit tests for the methods in the CoverUploaderService class.
 */
public class TestCoverUploaderService {

    private static SQLService sqlService;
    private static CoverUploaderService coverService;
    private static Song testSong;
    private static int songId;
    private static int userId;

    @BeforeAll
    static void setUp() throws SQLExceptionHandler {
        sqlService = SQLService.getInstance();
        coverService = new CoverUploaderService();

        String username = "1t2e3s4t5U5s6e6r";
        String language = "en";
        String password = "password";
        String profileImagePath = "path/to/image.jpg";

        sqlService.addNewUser(username, language, password, profileImagePath);
        userId = sqlService.getUserId(username);
        User testUser = sqlService.getUserByID(userId);
        sqlService.setUser(testUser);

        sqlService.addNewSong("FAKE/PATH/SONG12314243", "S92ong1k1kjh4dh5eg1h3djh8d", 180, "mp3", "Artist 1", "Album 1", null, null, null);
        songId = sqlService.getSongId("S92ong1k1kjh4dh5eg1h3djh8d");
        testSong = sqlService.getSong(songId);
    }

    @AfterAll
    static void tearDown() throws SQLExceptionHandler {
        // Remove the song and user from the database
        sqlService.removeSong(songId);
        sqlService.setUserById(userId);
        sqlService.removeUser();
    }

    /**
     * Test the addition and deletion of a cover image for a song.
     * @throws IOException if there is an error creating the temporary file
     * @throws SQLExceptionHandler if there is an error with the SQL operations
     * @throws InvalidSongException if the song is invalid
     */
    @Test
    void testAddAndDeleteCover() throws IOException, SQLExceptionHandler, InvalidSongException {
        // Create a temporary cover file
        File tempCover = File.createTempFile("test-cover", ".jpg");
        try (FileWriter writer = new FileWriter(tempCover)) {
            writer.write("Fake image content");
        }

        // Add cover to the song
        coverService.addCover(tempCover, testSong);
        assertNotNull(testSong.getImagePath());
        assertFalse(testSong.getImagePath().isEmpty());

        // Delete the cover
        coverService.deleteCover(testSong);

        // After deletion, imagePath should be the default placeholder (not null)
        assertEquals("src/main/resources/img/no-cover/no-cover1.jpg", testSong.getImagePath());

        // Clean up the temporary file
        tempCover.delete();
    }

    /**
     * Test the retrieval of the directory name for cover images.
     * This method checks if the directory name is correctly formatted based on the user ID.
     */
    @Test
    void testGetDirectoryName() {
        // Expected directory path based on the user ID
        String expected = "src/main/resources/img/ImgUser" + sqlService.getUserId();
        assertEquals(expected, coverService.getDirectoryName());
    }
}

package ulb.services;

import org.junit.jupiter.api.*;
import ulb.dtos.SongDTO;
import ulb.exceptions.songs.SQLExceptionHandler;
import ulb.models.Song;
import ulb.models.User;

import java.io.*;
import java.nio.file.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for the SearchBarService class.
 * This class contains unit tests for the methods in the SearchBarService class.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TestSearchBarService {

    private SQLService sqlService;
    private SearchBarService searchBarService;
    private int userId;
    private int songId;
    private final String username = "testSearchUser";
    private final String songTitle = "Searchable Song";
    private final String lyricsContent = "[00:00.00] Hello world this is a test\n[00:10.00] Another line of lyrics\n";

    @BeforeAll
    void setup() throws Exception {
        sqlService = SQLService.getInstance();
        searchBarService = new SearchBarService();

        // Create user and log in
        sqlService.addNewUser(username, "en", "pass", null);
        userId = sqlService.getUserId(username);
        User user = sqlService.getUserByID(userId);
        sqlService.setUser(user);

        // Create lyrics file
        String lyricsPath = "test_lyrics_test.lrc";
        Files.write(Paths.get(lyricsPath), lyricsContent.getBytes());

        // Add new song with that lyrics file
        sqlService.addNewSong("path/test.mp3", songTitle, 180, "mp3", "Search Artist", "Search Album", lyricsContent, null, null);
        songId = sqlService.getSongId(songTitle);
        Song song = sqlService.getSong(songId);
        song.setLyricsPath(lyricsPath); // simulate lyrics path
    }

    @AfterAll
    void cleanup() throws SQLExceptionHandler, IOException {
        sqlService.removeSong(songId);
        sqlService.removeUser();
        Files.deleteIfExists(Paths.get("test_lyrics_test.lrc"));
    }

    /**
     * Test the searchSongsAsDTO method with a valid song.
     * @throws Exception if there is an error during the test
     */
    @Test
    void testSearchSongsAsDTOWithMetadataMatch() throws Exception {
        List<SongDTO> results = searchBarService.searchSongsAsDTO("Searchable");
        assertFalse(results.isEmpty(), "Should find song via metadata");
        assertEquals(songTitle, results.get(0).getTitle());
    }
}

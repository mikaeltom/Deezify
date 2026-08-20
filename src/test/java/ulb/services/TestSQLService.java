package ulb.services;

import org.junit.jupiter.api.*;
import ulb.exceptions.songs.SQLExceptionHandler;
import ulb.models.Playlist;
import ulb.models.Song;
import ulb.models.Tag;
import ulb.models.User;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for SQL request methods.
 * This class contains unit tests for the methods in the SQLService class.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TestSQLService {
    private static final int userID = 1;
    private static final int songID = 1;
    private static final int playlistID = 1;
    private static final int tagID = 18;
    private static final SQLService SQL_SERVICE = SQLService.getInstance();

    @BeforeAll
    static void setUp() {
        try {
            SQL_SERVICE.clearDatabase(); // @BeforeAll requires to be static
            SQL_SERVICE.setUser(new User(userID, "Margot", "fr", false, "image"));
        } catch (SQLExceptionHandler e) {
            fail("Database setup failed: " + e.getMessage());
        }
    }

    /**
     * Test for adding a new user.
     */
    @Test
    @Order(1)
    void testAddNewUser() {
        assertDoesNotThrow(() -> SQL_SERVICE.addNewUser("Margot", "fr", "password", "image"), "Failed to add user.");
    }

    /**
     * Test for adding a new playlist.
     */
    @Test
    @Order(2)
    void testAddNewPlaylist() {
        assertDoesNotThrow(() -> SQL_SERVICE.addNewPlaylist("My Playlist"), "Failed to add playlist.");
    }

    /**
     * Test for adding a new song.
     */
    @Test
    @Order(3)
    void testAddNewSong() {
        assertDoesNotThrow(() -> SQL_SERVICE.addNewSong("C:/Music/ShapeOfYou.mp3", "Shape of You", 233,
                "mp3", "Ed Sheeran", "Divide", null, "C:/Images/ShapeOfYou.jpg", null), "Failed to add song.");
    }

    /**
     * Test for updating song details.
     */
    @Test
    @Order(4)
    void testUpdateSongDetails() {
        assertDoesNotThrow(
                () -> SQL_SERVICE.updateSongDetails(songID, "September", null, null, null, null),
                "Failed to update song.");
    }

    /**
     * Test for adding a new tag.
     */
    @Test
    @Order(5)
    void testAddNewTag() {
        assertDoesNotThrow(() -> SQL_SERVICE.addNewTag("Test", false), "Failed to add tag.");
    }

    /**
     * Test for adding tag to song.
     */
    @Test
    @Order(6)
    void testAddTagToSong() {
        assertDoesNotThrow(() -> SQL_SERVICE.addTagToSong(songID, tagID),
                "Failed to associate tag with song.");
    }

    /**
     * Test for adding tag to playlist.
     */
    @Test
    @Order(7)
    void testAddTagToPlaylist() {
        assertDoesNotThrow(() -> SQL_SERVICE.addTagToPlaylist(playlistID, tagID),
                "Failed to associate tag with playlist.");
    }

    /**
     * Test for getting all tags.
     */
    @Test
    @Order(8)
    void testGetPlaylistTags() {
        try {
            List<Tag> playlistTags = SQL_SERVICE.getPlaylistTags(playlistID);
            assertNotNull(playlistTags, "Tags list is null.");
            assertFalse(playlistTags.isEmpty(), "No tags found for playlist.");
        } catch (SQLExceptionHandler e) {
            fail("Exception thrown while fetching playlist tags: " + e.getMessage());
        }
    }

    /**
     * Test for getting all songs from user.
     */
    @Test
    @Order(9)
    void testGetAllSongsFromUser() {
        try {
            List<Song> userSongs = SQL_SERVICE.getAllSongsFromUser();
            assertNotNull(userSongs, "User songs list is null.");
            assertFalse(userSongs.isEmpty(), "No songs found for user.");
        } catch (SQLExceptionHandler e) {
            fail("Exception thrown while fetching all songs from user: " + e.getMessage());
        }
    }

    /**
     * Test for getting all playlists from user.
     */
    @Test
    @Order(10)
    void testGetPlaylist() {
        try {
            Playlist playlist = SQL_SERVICE.getPlaylist(playlistID);
            assertNotNull(playlist, "Failed to retrieve playlist.");
        } catch (SQLExceptionHandler e) {
            fail("Exception thrown while fetching playlist: " + e.getMessage());
        }
    }

    /**
     * Test for removing tag from song.
     */
    @Test
    @Order(11)
    void testRemoveTagFromSong() {
        assertDoesNotThrow(() -> SQL_SERVICE.removeTagFromSong(songID, tagID),
                "Failed to remove tag from song.");
    }

    /**
     * Test for removing tag from playlist.
     */
    @Test
    @Order(12)
    void testRemoveTagFromPlaylist() {
        assertDoesNotThrow(() -> SQL_SERVICE.removeTagFromPlaylist(playlistID, tagID),
                "Failed to remove tag from playlist.");
    }

    /**
     * Test for removingplaylist.
     */
    @Test
    @Order(13)
    void testRemovePlaylist() {
        assertDoesNotThrow(() -> SQL_SERVICE.removePlaylist(playlistID), "Failed to remove playlist.");
    }

    /**
     * Test for removing song.
     */
    @Test
    @Order(14)
    void testRemoveSong() {
        assertDoesNotThrow(() -> SQL_SERVICE.removeSong(songID), "Failed to remove song.");
    }

    /**
     * Test for removing tag.
     */
    @Test
    @Order(15)
    void testRemoveTag() {
        assertDoesNotThrow(() -> SQL_SERVICE.removeTag(tagID), "Failed to remove tag.");
        try {
            SQL_SERVICE.clearDatabase();
        } catch (SQLExceptionHandler e) {
            fail("Database setup failed: " + e.getMessage());
        }
    }
}
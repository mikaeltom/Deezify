package ulb.services.collections;

import org.junit.jupiter.api.*;
import ulb.exceptions.songs.SQLExceptionHandler;
import ulb.models.Playlist;
import ulb.models.Song;
import ulb.models.User;
import ulb.services.SQLService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for the PlaylistService class.
 * This class contains unit tests for the methods in the PlaylistService class.
 */
public class TestPlaylistService {

    private static SQLService sqlService;
    private static PlaylistService playlistService;
    private static int userId;
    private static int songId;
    private static int playlistId;
    private static final String testUsername = "testUserPlaylist123";
    private static final String testPlaylistName = "TestPlaylist123";
    private static final String testSongTitle = "TestSongForPlaylist";

    @BeforeAll
    static void setUp() throws SQLExceptionHandler {
        sqlService = SQLService.getInstance();
        playlistService = new PlaylistService();

        sqlService.addNewUser(testUsername, "en", "password", "path/to/image.jpg");
        userId = sqlService.getUserId(testUsername);
        User testUser = sqlService.getUserByID(userId);
        sqlService.setUser(testUser);

        sqlService.addNewSong("FAKE/PATH/SONG_PLAYLIST", testSongTitle, 200, "mp3", "Artist", "Album", null, null, null);
        songId = sqlService.getSongId(testSongTitle);

        sqlService.addNewPlaylist(testPlaylistName);
        playlistId = sqlService.getPlaylistId(testPlaylistName);
    }

    @AfterAll
    static void tearDown() throws SQLExceptionHandler {
        if (playlistId > 0) {
            sqlService.removePlaylist(playlistId);
        }
        if (songId > 0) {
            sqlService.removeSong(songId);
        }
        sqlService.setUserById(userId);
        sqlService.removeUser();
    }

    /**
     * Test the creation of a new playlist.
     */
    @Test
    void testAddSongToPlaylist() throws SQLExceptionHandler {
        playlistService.addSongToPlaylist(playlistId, songId);
        List<Song> songs = playlistService.getPlaylistSongs(playlistId);
        assertFalse(songs.isEmpty(), "Playlist should contain at least one song.");
        assertEquals(songId, songs.get(0).getId(), "Song ID should match the added song.");
    }

    /**
     * Test the removing of songs from a playlist.
     */
    @Test
    void testRemoveSongFromPlaylist() throws SQLExceptionHandler {
        playlistService.removeSongFromPlaylist(playlistId, songId);
        List<Song> songs = playlistService.getPlaylistSongs(playlistId);
        assertTrue(songs.isEmpty(), "Playlist should be empty after removing the song.");
    }

    /**
     * Test the retrieval of a playlist by its name.
     */
    @Test
    void testGetPlaylistFromName() throws SQLExceptionHandler {
        Playlist playlist = playlistService.getPlaylistFromName(testPlaylistName);
        assertNotNull(playlist, "Playlist should be retrieved successfully.");
        assertEquals(testPlaylistName, playlist.getName(), "Playlist name should match.");
    }
}

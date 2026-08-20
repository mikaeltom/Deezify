package ulb.services.options;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import ulb.exceptions.BannedWordException;
import ulb.exceptions.songs.SQLExceptionHandler;
import ulb.models.Playlist;
import ulb.services.BannedWordService;
import ulb.services.collections.PlaylistService;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Test class for PlaylistOptionsService.
 * This class contains unit tests for the methods in the PlaylistOptionsService class.
 */
public class TestPlaylistOptionsService {

    private PlaylistOptionsService playlistOptionsService;
    private FakePlaylistService fakePlaylistService;
    private FakeBannedWordService fakeBannedWordService;

    @BeforeEach
    public void setUp() throws FileNotFoundException {
        fakePlaylistService = new FakePlaylistService();
        fakeBannedWordService = new FakeBannedWordService();
        playlistOptionsService = new PlaylistOptionsService(fakePlaylistService, fakeBannedWordService);
    }

    /**
     * Test if the playlist is in the database.
     */
    @Test
    public void testIsInDatabasePlaylistExists() throws SQLExceptionHandler, IOException {
        fakePlaylistService.addPlaylist("Rock Classics");
        boolean exists = playlistOptionsService.isInDatabase("Rock Classics");
        assertTrue(exists);
    }

    /**
     * Test if the playlist is not in the database.
     * This test checks if the method correctly identifies that a playlist does not exist in the database.
     */
    @Test
    public void testIsInDatabasePlaylistDoesNotExist() throws SQLExceptionHandler, IOException {
        boolean exists = playlistOptionsService.isInDatabase("Pop Hits");
        assertFalse(exists);
    }

    /**
     * Test inserting a new playlist.
     */
    @Test
    public void testInsertPlaylistNewPlaylist() throws SQLExceptionHandler, IOException {
        playlistOptionsService.insertPlaylist("Jazz Vibes");
        assertTrue(fakePlaylistService.containsPlaylist("Jazz Vibes"));
    }

    /**
     * Test inserting an existing playlist.
     * This test checks if the method correctly identifies that a playlist already exists in the database.
     */
    @Test
    public void testInsertPlaylistExistingPlaylist() throws SQLExceptionHandler, IOException {
        fakePlaylistService.addPlaylist("Rock Classics");
        playlistOptionsService.insertPlaylist("Rock Classics");
        assertEquals(1, fakePlaylistService.getPlaylists().size());
    }

    /**
     * Test if the playlist name is valid.
     * This test checks if the method correctly identifies a valid playlist name.
     */
    @Test
    public void testValidatePlaylistNameNoBannedWords() {
        try {
            playlistOptionsService.validatePlaylistName("Good Playlist");
        } catch (BannedWordException e) {
            fail("BannedWordException should not be thrown");
        }
    }

    /**
     * Test if the playlist name contains a banned word.
     * This test checks if the method correctly identifies a playlist name with a banned word.
     */
    @Test
    public void testValidatePlaylistNameWithBannedWord() {
        fakeBannedWordService.addBannedWord("Fuck");
        try {
            playlistOptionsService.validatePlaylistName("Fuck");
            fail("BannedWordException should have been thrown");
        } catch (BannedWordException e) {
            assertEquals("A banned word is present in the input", e.getMessage());
        }
    }

    /**
     * Fake PlaylistService for testing.
     */
    class FakePlaylistService extends PlaylistService {
        private List<Playlist> playlists = new ArrayList<>();

        public void addPlaylist(String name) {
            // Creating Playlist with required constructor
            Playlist playlist = new Playlist(1, name,  new ArrayList<>(), 1);
            playlists.add(playlist);
        }

        public boolean containsPlaylist(String name) {
            return playlists.stream().anyMatch(p -> p.getName().equals(name));
        }

        public List<Playlist> getPlaylistsForUser() {
            return playlists;
        }

        public List<Playlist> getPlaylists() {
            return playlists; // Return the list of playlists
        }

        public void addNewPlaylist(String name) {
            // Creating Playlist with required constructor
            Playlist playlist = new Playlist(1, name, new ArrayList<>(), 1);
            playlists.add(playlist);
        }
    }

    // Simple Fake BannedWordService for testing
    class FakeBannedWordService extends BannedWordService {
        private List<String> bannedWords = new ArrayList<>();

        public FakeBannedWordService() throws FileNotFoundException {
        }

        public void addBannedWord(String word) {
            bannedWords.add(word);
        }

        @Override
        public void containsBannedWords(String name) throws BannedWordException {
            for (String bannedWord : bannedWords) {
                if (name.contains(bannedWord)) {
                    throw new BannedWordException("banned_word_exception", name);
                }
            }
        }
    }
}

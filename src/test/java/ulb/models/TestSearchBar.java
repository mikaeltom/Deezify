package ulb.models;

import javafx.util.Duration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ulb.exceptions.songs.InvalidSongException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for the SearchBar class.
 * This class contains unit tests for the methods in the SearchBar class.
 */
class TestSearchBar {
    private SearchBar searchBar;
    private Song validSong;
    private Song invalidSong;

    @BeforeEach
    void setUp() {
        searchBar = new SearchBar();
        validSong = new Song(1, "Title", "Artist", "Album", Duration.seconds(180), null, null, null, null, null);
        invalidSong = new Song(1, "", "", "", Duration.ZERO, null, null, null, null, null);
    }

    /**
     * Test for the search bar's initial state.
     */
    @Test
    void testGetSearchedSongInitiallyEmpty() {
        assertTrue(searchBar.getSearchedSong().isEmpty(), "Search list should be empty initially");
    }

    /**
     * Test for the search bar's state after adding a valid song.
     */
    @Test
    void testIsSongValidWithValidSong() {
        assertDoesNotThrow(() -> searchBar.isSongValid(validSong), "Valid song should not throw exception");
    }

    /**
     * Test for the search bar's state after adding an invalid song.
     */
    @Test
    void testIsSongValidWithInvalidSong() {
        InvalidSongException exception = assertThrows(InvalidSongException.class,
                () -> searchBar.isSongValid(invalidSong));
        assertNotNull(exception, "Exception should be thrown for an invalid song");
    }

    /**
     * Test for the search bar's state after clearing it.
     */
    @Test
    void testClearSearchedSong() {
        searchBar.getSearchedSong().add(validSong);
        searchBar.clearSearchedSong();
        assertTrue(searchBar.getSearchedSong().isEmpty(), "Search list should be empty after clearing");
    }

    /**
     * Test for searching for invalid songs.
     */
    @Test
    void testUpdateSearchedSongWithInvalidSongs() {
        List<Song> songs = List.of(invalidSong);
        InvalidSongException exception = assertThrows(InvalidSongException.class,
                () -> searchBar.updateSearchedSong(songs));
        assertNotNull(exception, "Exception should be thrown when adding invalid songs");
    }
}

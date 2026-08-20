package ulb.models;

import javafx.util.Duration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ulb.exceptions.playlist.InvalidMusicCollectionException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for the Playlist class.
 * This class contains unit tests for the methods in the Playlist class.
 */
class TestPlaylist {
    private Playlist playlist;
    private Song song1;
    private Song song2;
    private Song song3;

    @BeforeEach
    void setUp() {
        song1 = new Song(1, "Song 1", "Artist 1", "Album 1", Duration.seconds(180), null, null, null, null, null);
        song2 = new Song(2, "Song 2", "Artist 2", "Album 2", Duration.seconds(180), null, null, null, null, null);
        song3 = new Song(3, "Song 3", "Artist 3", "Album 3", Duration.seconds(180), null, null, null, null, null);

        List<Song> songs = new ArrayList<>();
        songs.add(song1);
        songs.add(song2);

        playlist = new Playlist(1, "My Playlist", songs, 0);
    }

    /**
     * Test set and get name of the playlist.
     */
    @Test
    void testSetAndGetName() {
        playlist.setName("Updated Name");
        assertNotEquals("My Playlist", playlist.getName());
        assertEquals("Updated Name", playlist.getName(), "Name should be updated.");
    }

    /**
     * Test creation of a playlist with songs.
     */
    @Test
    void testPlaylistCreationWithSongs() {
        assertEquals(2, playlist.getSongs().size(), "Playlist should contain 2 songs");
        assertEquals("Song 1", playlist.getSongs().get(0).getTitle(), "First song should be 'Song 1'");
        assertEquals("Song 2", playlist.getSongs().get(1).getTitle(), "Second song should be 'Song 2'");
    }

    /**
     * Test creation of a playlist without songs.
     */
    @Test
    void testPlaylistCreationWithoutSongs() {
        Playlist emptyPlaylist = new Playlist(2, "Empty Playlist");
        assertTrue(emptyPlaylist.getSongs().isEmpty(), "Playlist should be empty initially");
    }

    /**
     * Test adding a song to the playlist.
     */
    @Test
    void testAddSongToPlaylist() throws IOException {
        playlist.addSong(song3);
        assertEquals(3, playlist.getSongs().size(), "Playlist should contain 3 songs after adding a song");
        assertEquals("Song 3", playlist.getSongs().get(2).getTitle(), "New song added should be 'Song 3'");
    }

    /**
     * Test adding a duplicate song to the playlist.
     */
    @Test
    void testAddDuplicateSong() {
        assertThrows(IOException.class, () -> playlist.addSong(song1));
        List<Song> songs = playlist.getSongs();
        assertEquals(2, songs.size(), "Playlist should contain 2 songs after trying to add duplicate.");
    }

    /**
     * Test aremoving a song from the playlist.
     */
    @Test
    void testRemoveSongFromPlaylist() {
        playlist.removeSong(song1);
        assertEquals(1, playlist.getSongs().size(), "Playlist should contain 1 song after removing a song");
        assertFalse(playlist.getSongs().contains(song1), "Playlist should not contain 'Song 1' after removal");
    }

    /**
     * Test getting next song in the playlist.
     * @throws InvalidMusicCollectionException If an error occurs while getting the next song.
     */
    @Test
    void testNextSong() throws InvalidMusicCollectionException {
        Song nextSong = playlist.nextSong(song1);
        assertNotNull(nextSong, "Next song should not be null");
        assertEquals("Song 2", nextSong.getTitle(), "Next song should be 'Song 2'");
    }

    /**
     * Test getting next song at the end of the playlist.
     * @throws InvalidMusicCollectionException If an error occurs while getting the next song.
     */
    @Test
    void testNextSongAtEnd() throws InvalidMusicCollectionException {
        Song nextSong = playlist.nextSong(song2);
        assertEquals(song1, nextSong, "At the end of the playlist, the next song should loop to the first song");
    }

    /**
     * Test getting previous song.
     */
    @Test
    void testPreviousSong() throws InvalidMusicCollectionException {
        Song previousSong = playlist.previousSong(song2);
        assertNotNull(previousSong, "Previous song should not be null");
        assertEquals("Song 1", previousSong.getTitle(), "Previous song should be 'Song 1'");
    }

    /**
     * Test getting previous song with invalid song.
     */
    @Test
    void testPreviousSongWithInvalidSong() {
        Song invalidSong = new Song(888, "Invalid", "No Artist", "No Album", Duration.seconds(200), null, null, null,
                null, null);
        assertThrows(InvalidMusicCollectionException.class, () -> playlist.previousSong(invalidSong));
    }

    /**
     * Test getting previous song at the start of the playlist.
     * @throws InvalidMusicCollectionException If an error occurs while getting the previous song.
     */
    @Test
    void testPreviousSongAtStart() throws InvalidMusicCollectionException {
        Song previousSong = playlist.previousSong(song1);
        assertEquals(song2, previousSong,
                "At the start of the playlist, the previous song should loop to the last song");
    }

    /**
     * Test getting the increment of position in the playlist.
     */
    @Test
    void testIncrementPosition() {
        int oldPosition = playlist.getPosition();
        playlist.incrementPosition();
        assertEquals(oldPosition + 1, playlist.getPosition(), "Position should increment by 1");
    }

    /**
     * Test getting the removing a song and resetting the position.
     */
    @Test
    void testRemoveSongAndResetPosition() {
        playlist.removeSong(song1);
        assertEquals(0, playlist.getPosition(), "Position should be reset to 0 when playlist is empty");
    }
}

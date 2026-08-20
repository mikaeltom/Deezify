package ulb.models;

import javafx.util.Duration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ulb.exceptions.playlist.InvalidMusicCollectionException;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for the MusicQueue class.
 * This class contains unit tests for the methods in the MusicQueue class.
 */
class TestMusicQueue {

    private MusicQueue musicQueue = new MusicQueue();
    private Song song1;
    private Song song2;
    private Song song3;

    @BeforeEach
    void setUp() {
        song1 = new Song(1, "Song 1", "Artist 1", "Album", Duration.seconds(180), null, null, null, null, null);
        song2 = new Song(2, "Song 2", "Artist 2", "Album", Duration.seconds(200), null, null, null, null, null);
        song3 = new Song(3, "Song 3", "Artist 3", "Album", Duration.seconds(220), null, null, null, null, null);

        musicQueue.clearQueue();
    }

    /**
     * Test empty queue at the beginning.
     */
    @Test
    void testQueueIsEmptyInitially() {
        assertTrue(musicQueue.getSongs().isEmpty(), "Queue should be empty initially");
    }

    /**
     * Test adding songs to the queue.
     */
    @Test
    void testAddSongToQueue() throws IOException {
        musicQueue.addSong(song1);
        assertEquals(1, musicQueue.getSongs().size(), "Queue should contain 1 song after adding a song");
        assertEquals("Song 1", musicQueue.getSongs().get(0).getTitle(),
                "The only song in the queue should be 'Song 1'");
    }

    /**
     * Test skipping to a specific song not in the queue.
     */
    @Test
    void testSkipToSongNotInQueue() throws IOException {
        musicQueue.addSong(song1);
        musicQueue.addSong(song2);

        Song songNotInQueue = new Song(99, "Not In Queue", "Artist", "Album", Duration.seconds(300), null, null, null, null, null);
        musicQueue.skipTo(songNotInQueue);

        assertEquals(2, musicQueue.getSongs().size(), "No songs should be removed when skipping to a song not in the queue.");
    }

    /**
     * Test skipping first song in the queue.
     */
    @Test
    void testSkipToFirstSong() throws IOException {
        musicQueue.addSong(song1);
        musicQueue.addSong(song2);
        musicQueue.skipTo(song1);

        assertEquals(2, musicQueue.getSongs().size(), "No songs should be removed when skipping to the first song.");
        assertEquals(song1, musicQueue.getSongs().getFirst());
    }

    /**
     * Test adding duplicate songs to the queue.
     */
    @Test
    void testAddDuplicateSongs() throws IOException {
        musicQueue.addSong(song1);

        assertThrows(IOException.class, () -> musicQueue.addSong(song1));
        assertEquals(1, musicQueue.getSongs().size(), "Queue should not contain duplicate songs.");
        assertEquals(song1, musicQueue.nextSong(null));
    }

    /**
     * Test get first empty title in the queue.
     */
    @Test
    void testGetFirstTitleEmptyQueue() {
        assertNull(musicQueue.getFirstTitle(), "Should return null if the queue is empty.");
    }

    /**
     * Test get next song.
     */
    @Test
    void testNextSongInQueue() throws IOException {
        musicQueue.addSong(song1);
        musicQueue.addSong(song2);
        Song nextSong = musicQueue.nextSong(song1);
        assertNotNull(nextSong, "Next song should not be null");
        assertEquals("Song 1", nextSong.getTitle(), "Next song should be 'Song 1'");
    }

    /**
     * Test get previous song.
     */
    @Test
    void testPreviousSong() throws IOException, InvalidMusicCollectionException {
        musicQueue.addSong(song1);
        musicQueue.addSong(song2);
        Song previousSong = musicQueue.previousSong(song2);
        assertNotNull(previousSong, "Previous song should not be null");
        assertEquals("Song 2", previousSong.getTitle(), "Previous song should be 'Song 2'");
    }

    /**
     * Test get previous song when the queue is empty.
     */
    @Test
    void testPreviousSongWithEmptyQueue() {
        assertThrows(InvalidMusicCollectionException.class, () -> {
            musicQueue.previousSong(song1);
        });
    }

    /**
     * Test clearing the queue.
     */
    @Test
    void testClearQueue() throws IOException {
        musicQueue.addSong(song1);
        musicQueue.addSong(song2);
        musicQueue.clearQueue();
        assertTrue(musicQueue.getSongs().isEmpty(), "Queue should be empty after clearing");
    }

    /**
     * Test getting next song when all songs are played.
     */
    @Test
    void testNextSong() throws IOException {
        musicQueue.addSong(song1);
        musicQueue.addSong(song2);
        musicQueue.addSong(song3);
        Song nextSong = musicQueue.nextSong(song1);
        assertEquals("Song 1", nextSong.getTitle(), "Next song should be 'Song 1'");
        assertFalse(musicQueue.getSongs().contains(song1), "'Song 1' should be removed from the queue");
        nextSong = musicQueue.nextSong(song2);
        assertEquals("Song 2", nextSong.getTitle(), "Next song should be 'Song 2'");
        assertFalse(musicQueue.getSongs().contains(song2), "'Song 2' should be removed from the queue");
        nextSong = musicQueue.nextSong(song3);
        assertEquals("Song 3", nextSong.getTitle(), "Next song should be 'Song 3'");
        assertFalse(musicQueue.getSongs().contains(song3), "'Song 3' should be removed from the queue");
        assertTrue(musicQueue.getSongs().isEmpty(), "Queue should be empty after all songs are played");
    }

}

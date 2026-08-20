package ulb.services.collections;

import org.junit.jupiter.api.*;
import ulb.exceptions.songs.SQLExceptionHandler;
import ulb.models.Song;
import ulb.models.User;
import ulb.services.SQLService;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for the MusicQueueService class.
 * This class contains unit tests for the methods in the MusicQueueService class.
 */
public class TestMusicQueueService {

    private static SQLService sqlService;
    private static MusicQueueService queueService;
    private static Song testSong1;
    private static Song testSong2;
    private static int songId1;
    private static int songId2;
    private static int userId;

    @BeforeAll
    static void setUp() throws SQLExceptionHandler {
        sqlService = SQLService.getInstance();
        queueService = new MusicQueueService();

        // Fake user for test
        String username = "queueTestUser";
        String language = "en";
        String password = "password";
        String profileImagePath = "path/to/image.jpg";

        sqlService.addNewUser(username, language, password, profileImagePath);
        userId = sqlService.getUserId(username);
        User testUser = sqlService.getUserByID(userId);
        sqlService.setUser(testUser);

        // FakeTest
        sqlService.addNewSong("FAKE/PATH/SONG1", "TestSongQueue1", 200, "mp3", "Artist A", "Album A", null, null, null);
        songId1 = sqlService.getSongId("TestSongQueue1");
        testSong1 = sqlService.getSong(songId1);

        sqlService.addNewSong("FAKE/PATH/SONG2", "TestSongQueue2", 220, "mp3", "Artist B", "Album B", null, null, null);
        songId2 = sqlService.getSongId("TestSongQueue2");
        testSong2 = sqlService.getSong(songId2);
    }

    @AfterAll
    static void tearDown() throws SQLExceptionHandler {
        // Nettoyer les chansons et l'utilisateur
        sqlService.removeSong(songId1);
        sqlService.removeSong(songId2);
        sqlService.setUserById(userId);
        sqlService.removeUser();
    }

    /**
     * Clear the queue before each test.
     * This method ensures that the queue is empty before each test case runs.
     */
    @BeforeEach
    void clearQueueBeforeEach() {
        queueService.clearQueue();
    }

    /**
     * Test for adding a song to the queue.
     * Verifies that the song is added correctly and can be retrieved from the queue.
     */
    @Test
    void testAddAndGetQueue() throws Exception {
        queueService.addSong(testSong1);
        queueService.addSong(testSong2);

        ArrayList<Song> queue = queueService.getQueue();
        assertEquals(2, queue.size());
        assertEquals(testSong1.getTitle(), queue.get(0).getTitle());
        assertEquals(testSong2.getTitle(), queue.get(1).getTitle());
    }

    /**
     * Test for getting the next song in the queue.
     * Verifies that the correct song is returned as the next song.
     */
    @Test
    void testGetNextSongTitle() throws Exception {
        queueService.addSong(testSong1);
        assertEquals(testSong1.getTitle(), queueService.getNextSongTitle());
    }

    /**
     * Test for getting the next song in the queue.
     * Verifies that the correct song is returned as the next song.
     */
    @Test
    void testGetNextSong() throws Exception {
        queueService.addSong(testSong1);
        queueService.addSong(testSong2);

        Song next = queueService.getNext(testSong1);
        assertNotNull(next);
        assertEquals(testSong1.getTitle(), next.getTitle());
    }

    /**
     * Test for skipping to a specific song in the queue.
     * Verifies that the correct song is set as the next song.
     */
    @Test
    void testSkipTo() throws Exception {
        queueService.addSong(testSong1);
        queueService.addSong(testSong2);
        queueService.skipTo(testSong2);

        assertEquals(testSong2.getTitle(), queueService.getNextSongTitle());
    }

    /**
     * Test for clearing the queue.
     * Verifies that the queue is empty after clearing it.
     */
    @Test
    void testClearQueue() throws Exception {
        queueService.addSong(testSong1);
        queueService.clearQueue();
        assertTrue(queueService.getQueue().isEmpty());
    }

    /**
     * Test for setting a new queue.
     * Verifies that the new queue is set correctly and can be retrieved.
     */
    @Test
    void testSetNewQueue() {
        ArrayList<Song> newQueue = new ArrayList<>();
        newQueue.add(testSong2);
        queueService.setNewQueue(newQueue);

        ArrayList<Song> queue = queueService.getQueue();
        assertEquals(1, queue.size());
        assertEquals(testSong2.getTitle(), queue.get(0).getTitle());
    }
}

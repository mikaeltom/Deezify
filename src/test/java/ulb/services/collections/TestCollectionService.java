package ulb.services.collections;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ulb.exceptions.songs.SQLExceptionHandler;
import ulb.models.Playlist;
import ulb.models.Song;
import ulb.models.User;
import ulb.services.SQLService;
import ulb.models.LibraryModel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for CollectionService.
 * This class contains unit tests for the methods in the CollectionService class.
 */
class TestCollectionService {

    private LibraryService libraryService;
    private PlaylistService playlistService;
    private MusicQueueService queueService;
    private SQLService sqlService;
    private CollectionService collectionService;
    private Song song1;
    private Song song2;
    private int songId1;
    private int songId2;
    private int playlistId;
    private File tempFile1;
    private File tempFile2;
    private int userId;
    private User testUser;


    /**
     * Set up the test environment.
     * This method initializes the services and creates a test user and songs.
     */
    @BeforeEach
    void setUp() throws SQLExceptionHandler, IOException {
        libraryService = new LibraryService();
        playlistService = new PlaylistService();
        queueService = new MusicQueueService();
        sqlService = sqlService.getInstance();
        collectionService = new CollectionService(queueService, playlistService);

        String username = "collectionTestUser";
        String language = "en";
        String password = "password";
        String profileImagePath = "path/to/profile.jpg";

        sqlService.addNewUser(username, language, password, profileImagePath);
        userId = sqlService.getUserId(username);
        testUser = sqlService.getUserByID(userId);
        sqlService.setUser(testUser);

        File originalFile1 = new File("src/test/resources/music/test.mp3");
        File originalFile2 = new File("src/test/resources/music/test.wav");

        assertTrue(originalFile1.exists(), "Test MP3 file does not exist: " + originalFile1.getAbsolutePath());
        assertTrue(originalFile2.exists(), "Test WAV file does not exist: " + originalFile2.getAbsolutePath());

        tempFile1 = File.createTempFile("test-music-1", ".mp3");
        tempFile2 = File.createTempFile("test-music-2", ".wav");

        try (var in1 = new FileInputStream(originalFile1); var out1 = new FileOutputStream(tempFile1)) {
            in1.transferTo(out1);
        }
        try (var in2 = new FileInputStream(originalFile2); var out2 = new FileOutputStream(tempFile2)) {
            in2.transferTo(out2);
        }

        libraryService.setSong(tempFile1);
        libraryService.setSong(tempFile2);

        String title1 = LibraryModel.getMusicFile(tempFile1).musicName;
        String title2 = LibraryModel.getMusicFile(tempFile2).musicName;
        songId1 = sqlService.getSongId(title1);
        songId2 = sqlService.getSongId(title2);
        song1 = sqlService.getSong(songId1);
        song2 = sqlService.getSong(songId2);

        playlistService.addNewPlaylist("test_playlist");
        playlistId = sqlService.getPlaylistId("test_playlist");
        playlistService.addSongToPlaylist(playlistId, songId1);
        playlistService.addSongToPlaylist(playlistId, songId2);
    }


    /**
     * Clean up the test environment.
     * This method removes the test user, songs, and playlist after each test.
     */
    @AfterEach
    void tearDown() throws SQLExceptionHandler {
        playlistService.removeSongFromPlaylist(playlistId, songId1);
        playlistService.removeSongFromPlaylist(playlistId, songId2);
        playlistService.deletePlaylist("test_playlist");

        sqlService.removeSong(songId1);
        sqlService.removeSong(songId2);

        sqlService.setUserById(userId);
        sqlService.removeUser();

        if (tempFile1 != null && tempFile1.exists()) {
            tempFile1.delete();
        }
        if (tempFile2 != null && tempFile2.exists()) {
            tempFile2.delete();
        }
    }

    /**
     * Test song position change detection (true case).
     */
    @Test
    void testHasSongOrderChangedTrueWhenOrderDiffers() {
        List<Song> original = List.of(song1, song2);
        List<Song> modified = List.of(song2, song1);
        assertTrue(collectionService.hasSongOrderChanged(original, modified));
    }

    /**
     * Test song position change detection (false case).
     */
    @Test
    void testHasSongOrderChangedFalseWhenOrderSame() {
        List<Song> original = List.of(song1, song2);
        List<Song> sameOrder = List.of(song1, song2);
        assertFalse(collectionService.hasSongOrderChanged(original, sameOrder));
    }

    /**
     * Test playlist drag and drop functionality.
     */
    @Test
    void testHandlePlaylistDragAndDropReordersSongs() throws SQLExceptionHandler {
        Playlist playlist = playlistService.getPlaylistFromName("test_playlist");
        List<Song> newOrder = List.of(song2, song1);

        collectionService.handlePlaylistDragAndDrop(newOrder, playlist);

        List<Song> updated = playlistService.getPlaylistSongs(playlistId);
        assertEquals(song2.getId(), updated.get(0).getId());
        assertEquals(song1.getId(), updated.get(1).getId());
    }

    /**
     * Test updating song positions in a playlist.
     */
    @Test
    void testUpdateSongPositions() throws SQLExceptionHandler {
        List<Song> reversed = List.of(song2, song1);
        collectionService.updateSongPositions(reversed, playlistId);
        List<Song> updated = playlistService.getPlaylistSongs(playlistId);
        assertEquals(song2.getId(), updated.get(0).getId());
        assertEquals(song1.getId(), updated.get(1).getId());
    }

    /**
     * Test getting the next or previous song in a playlist.
     */
    @Test
    void testGetNextOrPreviousSongNextAndPreviousInPlaylist() throws SQLExceptionHandler {
        Playlist playlist = playlistService.getPlaylistFromName("test_playlist");

        // Force known order
        collectionService.updateSongPositions(List.of(song1, song2), playlist.getId());

        AbstractMap.SimpleEntry<String, Song> lastPlaylist = new AbstractMap.SimpleEntry<>(playlist.getName(), song1);
        Function<String, Playlist> getByTitle = (title) -> playlist;
        Supplier<ArrayList<Song>> queueSupplier = ArrayList::new;

        Song next = collectionService.getNextOrPreviousSong(false, song1, null, lastPlaylist, getByTitle, queueSupplier);
        assertEquals(song2.getId(), next.getId());

        Song previous = collectionService.getNextOrPreviousSong(true, song2, null, lastPlaylist, getByTitle, queueSupplier);
        assertEquals(song1.getId() + 1, previous.getId());
    }

    /**
     * Test getting the next or previous song when the playlist is empty.
     */
    @Test
    void testGetNextOrPreviousSongHandlesNullPlaylist() {
        Song result = collectionService.getNextOrPreviousSong(false, song1, null, null, t -> null, ArrayList::new);
        assertNull(result);
    }
}

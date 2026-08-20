package ulb.models;

import javafx.util.Duration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for the Lyrics class.
 * This class contains unit tests for the methods in the Lyrics class.
 */
class TestLyrics {

    private final String lyricsFilePath = "src/test/resources/lyrics/test-lyrics.lrc";
    private Song song;
    private Lyrics lyrics;

    @BeforeEach
    void setUp() {
        song = new Song(1, "Title", "Artist", "Album", Duration.seconds(180), null, null, null, null, lyricsFilePath);
        lyrics = new Lyrics(song);
    }

    /**
     * Test loading lyrics from a valid LRC file.
     */
    @Test
    void testLoadFromLrcFile() throws IOException {
        String content = "[00:01] Line 1\n[00:05] Line 2\n[00:10] Line 3\n";
        Path path = Paths.get(lyricsFilePath);
        Files.write(path, content.getBytes());
        lyrics.loadFromLrcFile();
        List<String> lyricsText = lyrics.getLyricsText();
        assertEquals(3, lyricsText.size(), "There should be 3 lines in the lyrics.");
        assertEquals(" Line 1", lyricsText.get(0), "First line should be 'Line 1'");
        assertEquals(" Line 2", lyricsText.get(1), "Second line should be 'Line 2'");
        assertEquals(" Line 3", lyricsText.get(2), "Third line should be 'Line 3'");
    }

    /**
     * Test loading lyrics from a file with invalid format.
     */
    @Test
    void testLoadFromLrcFileInvalidFilePath() {
        song = new Song(1, "Title", "Artist", "Album", Duration.seconds(180), null, null, null, null,
                "invalid-lyrics.lrc");
        lyrics = new Lyrics(song);
        assertThrows(IOException.class, () -> lyrics.loadFromLrcFile(),
                "IOException should be thrown when file doesn't exist.");
    }

    /**
     * Test loading lyrics from a file with null song object.
     */
    @Test
    void testLoadFromLrcFileNullSongObject() {
        Lyrics lyricsWithNullSong = new Lyrics(null);
        assertThrows(IOException.class, () -> lyricsWithNullSong.loadFromLrcFile(),
                "IOException should be thrown when song is null.");
    }

    /**
     * Test loading lyrics from a file with empty content.
     */
    @Test
    void testGetLyricsTextNoLyricsLoaded() {
        List<String> lyricsText = lyrics.getLyricsText();
        assertTrue(lyricsText.isEmpty(), "Lyrics should be empty initially.");
    }

    /**
     * Test loading lyrics from a file.
     */
    @Test
    void testGetLyricsTextAfterLoadingLyrics() throws IOException {
        String content = "[00:01] Line 1\n[00:05] Line 2\n[00:10] Line 3\n";
        Path path = Paths.get(lyricsFilePath);
        Files.write(path, content.getBytes());
        lyrics.loadFromLrcFile();
        List<String> lyricsText = lyrics.getLyricsText();
        assertEquals(3, lyricsText.size(), "There should be 3 lines in the lyrics.");
        assertEquals(" Line 1", lyricsText.get(0), "First line should be 'Line 1'");
        assertEquals(" Line 2", lyricsText.get(1), "Second line should be 'Line 2'");
        assertEquals(" Line 3", lyricsText.get(2), "Third line should be 'Line 3'");
    }

    /**
     * Test loading lyrics from a file with malformed timestamp lines.
     */
    @Test
    void testMalformedTimestampLines() throws IOException {
        String content = "No timestamp line\n[00:05] Valid line\n[bad:time] Invalid line";
        Path path = Paths.get(lyricsFilePath);
        Files.write(path, content.getBytes());

        lyrics.loadFromLrcFile();
        List<String> lyricsText = lyrics.getLyricsText();

        assertEquals(3, lyricsText.size(), "There should be 3 lines in the lyrics.");
        assertEquals("No timestamp line", lyricsText.get(0));
        assertEquals(" Valid line", lyricsText.get(1));
        assertEquals(" Invalid line", lyricsText.get(2));
        Files.delete(path);
    }

    /**
     * Test loading lyrics from a file with empty content.
     */
    @Test
    void testLyricsWithEmptyFile() throws IOException {
        String emptyContent = "";
        Path path = Paths.get(lyricsFilePath);
        Files.write(path, emptyContent.getBytes());
        lyrics.loadFromLrcFile();
        List<String> lyricsText = lyrics.getLyricsText();
        assertTrue(lyricsText.isEmpty(), "Lyrics should be empty if the file is empty.");
    }

}

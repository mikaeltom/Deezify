package ulb.services;

import javafx.util.Duration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ulb.models.LyricLine;
import ulb.models.Lyrics;
import ulb.models.Song;
import ulb.stub.MockTimeProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test class for the LyricsSynchronizer class.
 * This class contains unit tests for the methods in the LyricsSynchronizer class.
 */
public class TestLyricsSynchronizer {

    private LyricsSynchronizer lyricsSynchronizer;
    private MockTimeProvider timeProvider;
    private Consumer<String> lyricsLabel;
    private Lyrics lyrics;
    private Song song;

    @BeforeEach
    public void setUp() {
        // Prepare the song and lyrics
        song = new Song(1, "Test Song", "Artist", "Album", Duration.seconds(180), null, null, null, null, "path/to/lyrics.lrc");
        lyrics = new Lyrics(song);

        // Create a list of LyricLine
        List<LyricLine> lyricLines = new ArrayList<>();
        lyricLines.add(new LyricLine("[00:01.00] First line"));
        lyricLines.add(new LyricLine("[00:10.00] Second line"));
        lyricLines.add(new LyricLine("[00:20.00] Third line"));
        lyrics.getLines().addAll(lyricLines); // Add lines to lyrics list

        // Use MockTimeProvider to simulate time during tests
        timeProvider = new MockTimeProvider();

        // Create a mock for the Consumer to display the lyrics
        lyricsLabel = text -> {
        };

        // Create an instance of LyricsSynchronizer with the MockTimeProvider
        lyricsSynchronizer = new LyricsSynchronizer(lyrics, timeProvider, lyricsLabel);
    }

    /**
     * Test the Lyrics synchronization with the time.
     */
    @Test
    public void testUpdateLyricsAtTime() {
        // Simulate the passage of time
        timeProvider.incrementTime(1000L); // Simulated time: 1000ms

        // Force update of lyrics
        lyricsSynchronizer.updateLyrics();

        // Verify that the first line is displayed
        assertEquals(" First line", lyrics.getLines().get(0).getTextLine());

        // Advance the time by another 9000ms
        timeProvider.incrementTime(9000L); // Move to 10000ms

        // Force another update
        lyricsSynchronizer.updateLyrics();

        // Verify that the second line is displayed
        assertEquals(" Second line", lyrics.getLines().get(1).getTextLine());
    }
}

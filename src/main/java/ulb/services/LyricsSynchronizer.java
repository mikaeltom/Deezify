package ulb.services;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import ulb.models.LyricLine;
import ulb.models.Lyrics;
import ulb.utils.TimeProvider;

import java.util.List;
import java.util.function.Consumer;

/**
 * Synchronizes the display of lyrics with the current playback time of a song.
 * <p>
 * This class uses a timeline to periodically update the displayed lyrics based
 * on the current playback time provided by a TimeProvider. The lyrics are
 * updated in real-time with the help of a Consumer that accepts the text of
 * the current lyric line.
 * <p>
 * The LyricsSynchronizer is started and stopped using the `start` and `stop`
 * methods, respectively. The lyrics are updated every 100 milliseconds.
 * lyrics       The Lyrics object containing the lines of lyrics to be synchronized.
 * timeProvider The TimeProvider that supplies the current playback time.
 * lyricsLabel  A Consumer that accepts the text of the current lyric line to be displayed.
 */
public class LyricsSynchronizer {

    private final Lyrics lyrics;
    private final TimeProvider timeProvider;
    private final Consumer<String> lyricsLabel;
    private int currentLineIndex = -1;
    private Timeline timeline;

    public LyricsSynchronizer(Lyrics lyrics, TimeProvider timeProvider, Consumer<String> lyricsLabel) {
        this.lyrics = lyrics;
        this.timeProvider = timeProvider;
        this.lyricsLabel = lyricsLabel;
    }

    /**
     * Starts the LyricsSynchronizer. After calling this method, the
     * LyricsSynchronizer will periodically update the displayed lyrics
     * every 100 milliseconds, based on the current playback time supplied
     * by the TimeProvider.
     */
    public void start() {
        timeline = new Timeline(
                new KeyFrame(Duration.millis(100), event -> updateLyrics())
        );
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    /**
     * Stops the LyricsSynchronizer. After calling this method, the
     * LyricsSynchronizer will no longer update the displayed lyrics.
     */
    public void stop() {
        if (timeline != null) {
            timeline.stop();
        }
    }

    /**
     * Updates the displayed lyrics according to the current playback time.
     * The time is queried from the {@link TimeProvider} and then passed to
     * the {@link #updateLyricsAtTime(long)} method.
     */
    public void updateLyrics() {
        long currentTime = (long) timeProvider.getCurrentTimeMillis();
        updateLyricsAtTime(currentTime);
    }

    /**
     * Updates the displayed lyrics based on the provided playback time.
     * <p>
     * This method iterates through the list of lyric lines and finds the
     * most recent line that should be displayed at the given playback time.
     * If a new line is determined to be the current line, it updates the
     * current line index and passes the text of the new line to the
     * lyricsLabel consumer for display.
     *
     * @param currentTime The current playback time in milliseconds used to
     *                    determine which lyric line should be displayed.
     */
    private void updateLyricsAtTime(long currentTime) {
        List<LyricLine> lines = lyrics.getLines();
        int newIndex = -1;
        for (int i = 0; i < lines.size(); i++) {
            if (lines.get(i).getTimeLine() <= currentTime) {
                newIndex = i;
            } else {
                break;
            }
        }
        if (newIndex != currentLineIndex && newIndex >= 0) {
            currentLineIndex = newIndex;
            lyricsLabel.accept(lines.get(currentLineIndex).getTextLine());
        }
    }
}

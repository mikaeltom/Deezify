package ulb.controllers.playboxes;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.MediaPlayer;
import ulb.models.Lyrics;
import ulb.models.Song;
import ulb.services.LyricsSynchronizer;
import ulb.utils.I18n;
import ulb.utils.PopupType;
import ulb.views.playboxes.LyricsDisplayView;
import ulb.views.PopupView;

import java.io.IOException;

/**
 * Handles the display of lyrics for a song.
 * This class is a singleton, and it creates a new view every time the user
 * requests to display the lyrics of a song.
 * The view is loaded from a FXML file, and it is responsible for displaying
 * the lyrics in a VBox.
 * The view also displays a navigation system, where the user can navigate to
 * the previous or next song in the list of songs.
 * The list of songs is kept track of by the view.
 */
public class LyricsDisplayController implements LyricsDisplayView.LyricsDisplayListener {
    Song song;
    private String lyricsLabel;
    private Parent lyricsPane;
    private AnchorPane mainLayout;
    private boolean dynamicMode = false;
    private MediaPlayer mediaPlayer;
    private LyricsSynchronizer synchronizer;

    public void setMediaPlayer(MediaPlayer mediaPlayer) {
        this.mediaPlayer = mediaPlayer;
    }

    /**
     * Sets the main layout and song settings for the lyrics display view.
     * The main layout is the AnchorPane where the lyrics pane will be added.
     * The song is the Song object to be displayed in the lyrics view.
     * <p>
     * This method should be called before displaying the lyrics view.
     *
     * @param mainLayout the main layout to add the lyrics pane to.
     * @param song       the Song object to be displayed in the lyrics view.
     */
    public void setSettings(AnchorPane mainLayout, Song song) {
        this.mainLayout = mainLayout;
        this.song = song;
    }

    /**
     * Loads the base lyrics display view, which can be either static or dynamic.
     * The view is loaded from an FXML file, and it is responsible for displaying
     * the lyrics in a VBox. The view also displays a navigation system, where
     * the user can navigate to the previous or next song in the list of songs.
     * The list of songs is kept track of by the view.
     * <p>
     * This method removes any existing lyrics pane if it exists, and then loads
     * a new pane from the FXML file. It also sets the listener for the view,
     * sets the dynamic mode, and loads the settings for the view.
     * <p>
     * If an error occurs while loading the view, an error popup is displayed.
     *
     * @param isDynamicMode whether the view should display the lyrics in dynamic
     *                      mode (i.e. the lyrics are updated in real time as the
     *                      song is being played) or not.
     */
    private void loadLyricsBase(boolean isDynamicMode) {
        removeOldLyricsPaneIfExists();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ulb/views/LyricsDisplay.fxml"));
            lyricsPane = loader.load();
            Lyrics lyrics = new Lyrics(song);
            lyrics.loadFromLrcFile();

            LyricsDisplayView lyricsDisplayView = loader.getController();
            lyricsDisplayView.setListener(this);
            lyricsDisplayView.setDynamicMode(isDynamicMode);
            lyricsDisplayView.loadSettings(mainLayout, lyricsPane);
            displayLyrics(isDynamicMode, lyrics, lyricsDisplayView);

        } catch (IOException e) {
            new PopupView("error_loading_lyrics_title", I18n.get("lyrics_not_found"), PopupType.ERROR);
        }
    }

    /**
     * Displays the lyrics in either dynamic or static mode.
     * <p>
     * In dynamic mode, the lyrics are synchronized with the song's current playback time
     * and updated in real-time using a LyricsSynchronizer. In static mode, all lyrics are
     * displayed at once.
     *
     * @param isDynamicMode     whether the lyrics should be displayed in dynamic mode.
     * @param lyrics            the Lyrics object containing the lyrics to be displayed.
     * @param lyricsDisplayView the view responsible for displaying the lyrics.
     */
    private void displayLyrics(boolean isDynamicMode, Lyrics lyrics, LyricsDisplayView lyricsDisplayView) {
        if (isDynamicMode) {
            synchronizer = new LyricsSynchronizer(
                    lyrics,
                    () -> mediaPlayer.getCurrentTime().toMillis(),
                    lyric -> {
                        lyricsLabel = lyric;
                        lyricsDisplayView.showSingleLyric(lyricsLabel);
                    }
            );
            synchronizer.start();
        } else {
            lyricsDisplayView.showLyrics(lyrics.getLyricsText());
        }
    }

    public void loadLyrics() {
        loadLyricsBase(false);
    }

    public void loadDynamicLyrics() {
        loadLyricsBase(true);
    }


    /**
     * Forces the lyrics display to update its current lyric line.
     * This can be used to update the lyrics display when the user seeks to a different time
     * in the song.
     */
    public void updateCurrentLyric() {
        if (synchronizer != null) {
            synchronizer.updateLyrics();
        }
    }

    /**
     * Toggles the lyrics display mode between dynamic and static.
     * <p>
     * In dynamic mode, the lyrics are synchronized with the song's playback time
     * and updated in real-time. In static mode, all lyrics are displayed at once.
     * The method stops any active synchronization before switching modes.
     *
     * @param enableDynamic if true, the lyrics will be displayed in dynamic mode;
     *                      otherwise, they will be displayed in static mode.
     */
    public void toggleLyricsMode(boolean enableDynamic) {
        this.dynamicMode = enableDynamic;
        if (synchronizer != null) {
            synchronizer.stop();
        }

        if (dynamicMode) {
            loadDynamicLyrics();
        } else {
            loadLyrics();
        }
    }

    /**
     * Removes the lyrics pane from the main layout and stops any active
     * synchronization if the lyrics display is in dynamic mode.
     * <p>
     * This method is used to close the lyrics view when the user closes the
     * window or navigates away from the song.
     */
    public void removeLyrics() {
        mainLayout.getChildren().remove(lyricsPane);
        if (synchronizer != null) {
            synchronizer.stop();
        }
    }

    public boolean isDynamicMode() {
        return dynamicMode;
    }

    /**
     * Removes the existing lyrics pane from the main layout if it exists.
     * <p>
     * This method checks if the lyrics pane is not null and removes it from
     * the main layout if it is present. This is used to ensure that any old
     * lyrics pane is removed before loading a new one.
     */
    private void removeOldLyricsPaneIfExists() {
        if (lyricsPane != null) {
            mainLayout.getChildren().remove(lyricsPane);
        }
    }
}

package ulb.controllers.playboxes;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.MediaPlayer;
import ulb.models.Song;
import ulb.utils.I18n;
import ulb.utils.PopupType;
import ulb.views.playboxes.NowPlayingBoxView;
import ulb.views.PopupView;

/**
 * Controller responsible for handling the now playing box view.
 * The now playing box view is responsible for displaying information about the
 * song that is currently playing, such as the song title, artist name, and album
 * cover.
 * The controller manages the now playing box view's state, such as switching
 * between the expanded and collapsed views, and displaying the lyrics of the
 * currently playing song.
 * The controller also communicates with the main controller to display the
 * currently playing song in the playlist view.
 */
public class NowPlayingBoxController implements NowPlayingBoxView.NowPlayingBoxViewListener, NowPlayingUpdateHandler.NowPlayingBoxControllerAccess {
    private final LyricsDisplayController lyricsDisplayController = new LyricsDisplayController();
    private Song currentSong;
    private Parent nowPlayingBoxViewParent;
    private NowPlayingBoxView nowPlayingBoxView;
    private boolean isBigCoverDisplayed = false;
    private boolean isLyricsDisplayed = false;
    private NowPayingBoxControllerListener nowPayingBoxControllerListener;

    public void setNowPayingBoxControllerListener(NowPayingBoxControllerListener nowPayingBoxControllerListener) {
        this.nowPayingBoxControllerListener = nowPayingBoxControllerListener;
    }


    public void setCurrentSong(Song song) {
        this.currentSong = song;
    }

    /**
     * Sets the media player for the lyrics display controller.
     * This media player will be used to synchronize the lyrics
     * with the current playback time of the song.
     *
     * @param mediaPlayer The MediaPlayer instance to be set for the lyrics display.
     */
    public void setLyricsMediaPlayer(MediaPlayer mediaPlayer) {
        lyricsDisplayController.setMediaPlayer(mediaPlayer);
    }

    /**
     * Returns true if the now playing box is currently displayed in its
     * expanded (big cover) view, and false otherwise.
     */
    public boolean isBigCoverDisplayed() {
        return isBigCoverDisplayed;
    }

    /**
     * Toggles the isBigCoverDisplayed field.
     * This method is used to switch the now playing box view between its expanded
     * and collapsed states.
     */
    public void changeIsBigCoverDisplayed() {
        isBigCoverDisplayed = !isBigCoverDisplayed;
    }

    /**
     * Returns the parent view of the now playing box.
     */
    public Parent getPlayerViewParent() {
        return nowPlayingBoxViewParent;
    }

    /**
     * Shows the now playing box view.
     * This method loads the now playing box view from the NowPlayingBoxView.fxml file,
     * sets the listener for the view, and stores the loaded view in the
     * nowPlayingBoxViewParent field.
     * If there is an error while loading the view, an error popup is displayed.
     */
    public void show() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ulb/views/NowPlayingBoxView.fxml"), I18n.getBundle());
            nowPlayingBoxViewParent = loader.load();
            this.nowPlayingBoxView = loader.getController();
            this.nowPlayingBoxView.setListener(this);
        } catch (Exception e) {
            new PopupView("loading_error_title", I18n.get("error_loading_nowplaying"), PopupType.ERROR);
        }
    }

    /**
     * Updates the next song label in the now playing box.
     */
    public void setNextSongLabel(String nextSongTitle) {
        nowPlayingBoxView.updateNextSongLabel(nextSongTitle != null ? "Next Song: " + nextSongTitle : "");
    }

    /**
     * Updates the now playing box with the given song and next song label.
     * <p>
     * The song title, artist name, and album cover are updated in the now playing box.
     * The next song label is also updated.
     * <p>
     * If the song is null, the now playing box is reset.
     * If the song has an image path, the image is displayed.
     * If the image path is missing or the file does not exist, the image is reset.
     *
     * @param currentSong   the song to be displayed in the now playing box
     * @param nextSongLabel the label to be displayed in the next song label
     */
    @Override
    public void updateSongPlayingBox(Song currentSong, String nextSongLabel) {
        setCurrentSong(currentSong);
        nowPlayingBoxView.updateNowPlaying(currentSong.getTitle(), currentSong.getArtist(), currentSong.getImagePath(), nextSongLabel);
    }

    /**
     * Switches the now playing box view between its expanded and collapsed states.
     * If the view is currently collapsed, it is expanded and the currently playing
     * song is displayed in the playlist view. If the view is currently expanded, it
     * is collapsed.
     */
    @Override
    public void switchBigCoverView() {
        if (!isBigCoverDisplayed) {
            nowPayingBoxControllerListener.changeBigCoverView(currentSong);
        } else {
            nowPayingBoxControllerListener.bigCoverViewClose();
        }
    }


    /**
     * Handles the action when the lyrics button is clicked.
     * If the lyrics pane is already visible, it is removed.
     * Otherwise, the lyrics pane is loaded and added to the main layout.
     * The lyrics pane is positioned to the right of the main layout.
     * If the lyrics file doesn't exist, an error is displayed.
     */
    @Override
    public void toggleLyricsPane(AnchorPane mainLayout) {
        lyricsDisplayController.setSettings(mainLayout, currentSong);
        if (isLyricsDisplayed) {
            lyricsDisplayController.removeLyrics();
            isLyricsDisplayed = false;
        } else {
            lyricsDisplayController.toggleLyricsMode(lyricsDisplayController.isDynamicMode());
            isLyricsDisplayed = true;
        }
    }

    /**
     * Forces the lyrics display to update its current lyric line.
     * This can be used to update the lyrics display when the user seeks to a different time
     * in the song.
     */
    public void forceLyricsUpdate() {
        lyricsDisplayController.updateCurrentLyric();
    }

    public interface NowPayingBoxControllerListener {
        void changeBigCoverView(Song song);

        void bigCoverViewClose();
    }

}
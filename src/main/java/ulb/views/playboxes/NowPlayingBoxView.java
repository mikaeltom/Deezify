package ulb.views.playboxes;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.io.File;

import static ulb.utils.SongInformationFormatter.getFormattedString;

/**
 * The now playing box displays information about the song that is currently
 * playing.
 * This information includes the song title, artist name, and album cover.
 * The now playing box is a singleton because there can only be one song playing
 * at a time.
 */
public class NowPlayingBoxView {

    protected NowPlayingBoxViewListener listener;
    @FXML
    private Label songTitle;
    @FXML
    private Label artistName;
    @FXML
    private Label nextSongLabel;
    @FXML
    private ImageView albumCover;
    @FXML
    private Button songLyricsButton;

    public void setListener(NowPlayingBoxViewListener listener) {
        this.listener = listener;
    }

    /**
     * Updates the now playing box with the given song.
     * If the song is null, the now playing box is reset.
     * If the song has an image path, the image is displayed.
     * If the image path is missing or the file does not exist, the image is reset.
     */
    public void updateNowPlaying(String title, String artist, String imagePath, String nextSong) {
        songTitle.setText(title);
        artistName.setText(getFormattedString(artist));
        nextSongLabel.setText(nextSong);
        albumCover.setImage(new Image(new File(imagePath).toURI().toString()));
    }

    /**
     * Handles the action when the lyrics button is clicked.
     * If the lyrics pane is already visible, it is removed.
     * Otherwise, the lyrics pane is loaded and added to the main layout.
     * The lyrics pane is positioned to the right of the main layout.
     * If the lyrics file doesn't exist, an error is displayed.
     */
    public void onSongLyricsButtonClick() {
        Scene currentScene = songLyricsButton.getScene();
        AnchorPane mainLayout = (AnchorPane) currentScene.getRoot();
        listener.toggleLyricsPane(mainLayout);
    }

    /**
     * Handles the action when the album cover is clicked.
     * Switches the now playing box to its expanded view.
     */
    @FXML
    private void onAlbumCoverClick() {
        listener.switchBigCoverView();

    }

    /**
     * Updates the next song label in the now playing box.
     * @param label the new label to be displayed in the next song label
     */
    public void updateNextSongLabel(String label) {
        nextSongLabel.setText(label);
    }

    public interface NowPlayingBoxViewListener {
        void switchBigCoverView();

        void toggleLyricsPane(AnchorPane mainLayout);
    }
}

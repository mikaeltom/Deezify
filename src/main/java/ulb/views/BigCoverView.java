package ulb.views;

import javafx.beans.binding.Bindings;
import javafx.beans.value.ObservableNumberValue;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import ulb.dtos.SongDTO;

import java.nio.file.Files;
import java.nio.file.Paths;

import static ulb.utils.SongInformationFormatter.getFormattedString;
import static ulb.utils.SongInformationFormatter.getFormattedTags;

public class BigCoverView {

    @FXML
    public ImageView songImageView;

    @FXML
    private Label songTitleLabel;

    @FXML
    private Label artistNameLabel;

    @FXML
    private Label albumNameLabel;

    @FXML
    private Label tagsLabel;

    private SongDTO currentSong;
    private BigCoverListener listener;

    public void setBigCoverListener(BigCoverListener listener) {
        this.listener = listener;
    }

    /**
     * Set the song to display in the custom now playing box.
     * Delegates logic to the controller.
     *
     * @param song the song to be displayed in the custom now playing box
     */
    public void setSong(SongDTO song) {
        this.currentSong = song;
        updateView();
    }

    /**
     * Handles the action when the cover image is clicked in the BigCoverView.
     * Triggers the "handleClickCover" event via the listener.
     */
    @FXML
    public void handleImageClick() {
        listener.handleClickCover();
    }

    /**
     * Updates the BigCoverView with the information of the currently selected song.
     * It sets the song title, artist name, album name, tags, and cover image in the
     * view with the corresponding information from the song.
     */
    private void updateView() {
        songTitleLabel.setText(getFormattedString(currentSong.getTitle()));
        artistNameLabel.setText(getFormattedString(currentSong.getArtist()));
        albumNameLabel.setText(getFormattedString(currentSong.getAlbum()));
        tagsLabel.setText(getFormattedTags(currentSong));
        songImageView.setImage(getCoverImage());

        // NOT POSSIBLE TO DYNAMICALLY CHANGE THE SIZE OF THE IMAGE
        // SEE POST: https://bugs.openjdk.org/browse/JDK-8224069?jql=labels%20%3D%20ImageView

        // TO REPRODUCE UNCOMMENT THIS LINE AND COMMENT THE NEXT BIND CALL
        // songImageView.fitHeightProperty().bind(listener.getRestrainedHeight());


        // WITH THIS UNFORTUNATELY, IF BIG COVER IS DISPLAYED AND WINDOW RESIZED,
        // DOUBLE-CLICK ON THE NOW PLAYING BOX AGAIN TO RELOAD THE BIG COVER AND CHANGE ITS DIMENSIONS TO THE CORRECT ONES
        songImageView.fitHeightProperty().bind(
                Bindings.min((listener.getRestrainedHeight().getValue().intValue() - 110), (ObservableNumberValue) listener.getRestrainedHeight())
        );


    }

    /**
     * Retrieves the cover image for the current song.
     * If the song has a valid image path, the image is loaded from that path.
     * Otherwise, a default "no cover" image is used.
     *
     * @return the Image object representing the song's cover image
     */
    private Image getCoverImage() {
        String imagePath = currentSong.getImagePath();
        boolean isValid = imagePath != null
                && !imagePath.isEmpty()
                && Files.exists(Paths.get(imagePath))
                && !imagePath.equals("src/main/resources/img/");

        String finalPath = isValid ? imagePath : "src/main/resources/img/no-cover/no-cover1.jpg";
        return new Image("file:" + finalPath);
    }

    public interface BigCoverListener {
        void handleClickCover();

        ObservableValue<? extends Number> getRestrainedHeight();

    }
}

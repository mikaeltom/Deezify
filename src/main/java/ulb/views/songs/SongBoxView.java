package ulb.views.songs;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import ulb.dtos.SongDTO;

import java.nio.file.Files;
import java.nio.file.Paths;

import static ulb.utils.SongInformationFormatter.getFormattedString;
import static ulb.utils.SongInformationFormatter.getFormattedTags;

public class SongBoxView extends HBox {
    private SongDTO song;
    private SongBoxView.SongBoxViewListener songBoxViewListener;
    @FXML
    private ImageView imageView;
    @FXML
    private HBox box;
    @FXML
    private VBox textContainer;

    public void setSongBoxViewListener(SongBoxView.SongBoxViewListener songBoxViewListener) {
        this.songBoxViewListener = songBoxViewListener;
    }

    /**
     * Initializes the SongBoxView with the provided song.
     * Sets the song data for the view, associates the song with the user data of the box,
     * and creates the text container and image view for displaying the song's information.
     *
     * @param song the SongDTO object representing the song to be displayed
     */
    public void initialize(SongDTO song) {
        this.song = song;
        box.setUserData(song);
        createTextContainer();
        createImageView();
    }

    /**
     * Creates an {@link ImageView} for the given song. The image will be sized to
     * 50x50 and set to be
     * preserved ratio. If the song has an image path, the image will be loaded from
     * that path. If the
     * image path is null or empty, no image will be loaded.
     */
    private void createImageView() {
        String imagePath = song.getImagePath();
        if (imagePath == null || imagePath.isEmpty() || !Files.exists(Paths.get(imagePath))
                || imagePath.equals("src/main/resources/img")) {
            imagePath = "src/main/resources/img/no-cover/no-cover1.jpg";
        }

        Image image = songBoxViewListener.manageCachedImage("file:" + imagePath);

        imageView.setImage(image);
    }

    /**
     * Create a vertical box containing the song's title, artist, and album name.
     * The labels are styled with CSS classes "song-title", "song-artist", and
     * "song-album" respectively. The tags are also displayed in a label styled
     * with CSS class "song-tags". The box is created with spacing of 10 px
     * between the labels.
     */
    private void createTextContainer() {
        textContainer.getChildren().setAll(
                createStyledLabel(getFormattedString(song.getTitle()), "song-title"),
                createStyledLabel(getFormattedString(song.getArtist()), "song-artist"),
                createStyledLabel(getFormattedString(song.getAlbum()), "song-album"),
                createStyledLabel(getFormattedTags(song), "song-tags"));
    }

    /**
     * Create a {@link Label} with the given text and style class.
     *
     * @param text       The text to display in the label.
     * @param styleClass The CSS style class to apply to the label.
     * @return The created label.
     */
    private Label createStyledLabel(String text, String styleClass) {
        Label label = new Label(text);
        label.getStyleClass().add(styleClass);
        return label;
    }

    /**
     * Handles the action when an option is selected in the song box.
     * Triggers the triple dot button click event via the listener.
     * When the user clicks on the song box with the primary mouse button, the
     * OnClickedSongBox method of the song box view listener is called.
     * When the user clicks on the song box with the secondary mouse button, the
     * OnClickTripleDot method of the song box view listener is called.
     */
    @FXML
    public void handleClick(MouseEvent event) {
        if (event.getButton() == MouseButton.PRIMARY) {
            songBoxViewListener.onClickedSongBox();
        } else if (event.getButton() == MouseButton.SECONDARY) {
            songBoxViewListener.onClickTripleDot();
        }
    }

    public interface SongBoxViewListener {
        void onClickedSongBox();

        void onClickTripleDot();

        Image manageCachedImage(String imagePath);

    }
}

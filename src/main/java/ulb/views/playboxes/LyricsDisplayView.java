package ulb.views.playboxes;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.util.List;
import java.util.Objects;

/**
 * A view for displaying the lyrics of a song. The view is a VBox where the
 * lyrics are displayed in a list of Labels. The view also has a navigation
 * system, where the user can navigate to the previous or next song in the
 * list of songs. The list of songs is kept track of by the view.
 */
public class LyricsDisplayView {
    @FXML
    private VBox lyricsContainer;

    @FXML
    private ImageView toggleIconImage;

    private LyricsDisplayListener listener;
    private boolean isDynamic = false;

    public void setListener(LyricsDisplayListener newListener) {
        this.listener = newListener;
    }


    /**
     * Loads the lyrics pane into the main layout with the correct
     * positioning. The lyrics pane is anchored to the top right of the
     * main layout with a gap of 10 pixels. If the lyrics pane is already
     * in the main layout, it is not added again.
     *
     * @param mainLayout the main layout to add the lyrics pane to.
     * @param lyricsPane the lyrics pane to add to the main layout.
     */
    public void loadSettings(AnchorPane mainLayout, Parent lyricsPane) {
        if (!mainLayout.getChildren().contains(lyricsPane)) {
            AnchorPane.setTopAnchor(lyricsPane, 15.0);
            AnchorPane.setBottomAnchor(lyricsPane, 82.0);

            mainLayout.widthProperty().addListener((obs, oldWidth, newWidth) -> {
                double newLeft = newWidth.doubleValue() - 200;
                AnchorPane.setLeftAnchor(lyricsPane, newLeft);
            });

            AnchorPane.setRightAnchor(lyricsPane, 10.0);
            mainLayout.getChildren().add(lyricsPane);
        }
    }

    /**
     * Loads lyrics into the lyrics container.
     * <p>
     * This method clears any existing content in the lyrics container and
     * populates it with the provided list of lyrics. Each lyric is displayed
     * as a styled label with specific text properties, such as wrapping, color,
     * font weight, and font size.
     *
     * @param lyricsText the list of lyrics to be displayed, where each string
     *                   represents a line of lyrics.
     */
    public void showLyrics(List<String> lyricsText) {
        if (lyricsContainer != null) {
            lyricsContainer.getChildren().clear();

            for (String line : lyricsText) {
                Label lyricLine = new Label(line);
                lyricLine.setWrapText(true);
                lyricLine.setStyle("-fx-text-fill: #1DB954; -fx-font-weight: bold; -fx-font-size: 14px;");

                lyricsContainer.getChildren().add(lyricLine);
            }
        }
    }

    /**
     * Displays a single lyric line in the lyrics container with increased font size and centered horizontally.
     *
     * @param lyricText the single lyric line to be displayed.
     */
    public void showSingleLyric(String lyricText) {
        if (lyricsContainer != null) {
            lyricsContainer.getChildren().clear();

            String spacedLyricText = "\n\n" + lyricText + "\n\n";
            Label lyricLine = new Label(spacedLyricText);
            lyricLine.setWrapText(true);
            lyricLine.setStyle("-fx-text-fill: #1DB954; -fx-font-weight: bold; -fx-font-size: 24px;");
            lyricLine.setMaxWidth(Double.MAX_VALUE);
            lyricLine.setAlignment(javafx.geometry.Pos.CENTER);
            lyricLine.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);
            lyricsContainer.getChildren().add(lyricLine);
        }
    }

    /**
     * Handles the action when the toggle mode button is clicked, toggling between
     * dynamic and static modes for the lyrics display. The lyrics display is updated
     * accordingly by calling the listener's toggleLyricsMode method.
     */
    @FXML
    private void handleToggleMode() {
        if (listener != null) {
            isDynamic = !isDynamic;
            updateToggleIcon();
            listener.toggleLyricsMode(isDynamic);
        }
    }

    /**
     * Sets the dynamic mode for the lyrics display view and updates the
     * toggle mode button text accordingly.
     *
     * @param dynamicMode whether the lyrics display should be in dynamic mode or not.
     */
    public void setDynamicMode(boolean dynamicMode) {
        this.isDynamic = dynamicMode;
        updateToggleIcon();
    }

    /**
     * Updates the icon of the toggle mode button based on the current mode (dynamic or static).
     * The icon is changed to either a karaoke icon or a full lyrics icon.
     */
    private void updateToggleIcon() {
        String imagePath = isDynamic ? "/images/classical-lyrics.png" : "/images/micro.png";
        toggleIconImage.setImage(new javafx.scene.image.Image(Objects.requireNonNull(getClass().getResourceAsStream(imagePath))));
    }


    public interface LyricsDisplayListener {
        void toggleLyricsMode(boolean isDynamic);
    }
}

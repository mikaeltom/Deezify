package ulb.views.searches;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import ulb.dtos.SongDTO;

import java.net.URL;
import java.util.Objects;

/**
 * A single cell in the search bar. Contains a button to play the song,
 * another button to show a dropdown menu with options to add the song to a
 * playlist
 * or to edit the song's information and a heart button to add or remove
 * the song from favorites.
 */
public class SearchBarCellView {
    private static final String STYLESHEET_PATH = "/style/style.css";
    private final Label musicName = new Label();
    private final SongDTO song;
    private final Button playMusicButton = new Button();
    private final Button tripleDotButton = new Button();
    private final Button heartButton = new Button();
    private final ImageView heartImageView = new ImageView();
    private SearchBarCellViewListener listener;

    public SearchBarCellView(SongDTO song) {
        this.song = song;
    }

    /**
     * Initializes the heart button image based on whether the song is in favorites
     * or not.
     * If the song is in favorites, the heart button displays a filled heart image.
     * If the song is not in favorites, the heart button displays an outline heart
     * image.
     */
    public void initializeHeartButton() {
        if (listener.isInFavorites()) {
            heartImageView.setImage(listener.manageCachedImage(
                    Objects.requireNonNull(getClass().getResource("/images/heart-filled.png")).toString()));
        } else {
            heartImageView.setImage(listener.manageCachedImage(
                    Objects.requireNonNull(getClass().getResource("/images/heart-outline.png")).toString()));
        }
    }

    public void setListener(SearchBarCellViewListener listener) {
        this.listener = listener;
    }

    public void setMusicName(String musicName) {
        this.musicName.setText(musicName);
    }

    /**
     * Opens a popup window with options for the selected song.
     * The popup contains a text field for adding tags, as well as a list of
     * predefined tags.
     * If the user has selected a song, the popup will be centered on the user's
     * mouse position.
     */
    public void openSongOption() {
        listener.openSongOption(this);
    }

    /**
     * Adds the song to the playlist when the heart button is clicked
     * and removes it when it is clicked again, all while changing the
     * button's image.
     */
    public void handleHeartClick() {
        ImageView imageView = (ImageView) heartButton.getGraphic();
        String currentImageUrl = imageView.getImage().getUrl();
        URL newImageUrl;

        if (currentImageUrl.contains("heart-filled.png")) {
            newImageUrl = getClass().getResource("/images/heart-outline.png");
            listener.removeFromFavorites();
        } else {
            newImageUrl = getClass().getResource("/images/heart-filled.png");
            listener.addToFavorites();
        }

        imageView.setImage(listener.manageCachedImage(Objects.requireNonNull(newImageUrl).toString()));
    }

    /**
     * Handles the event when the play button on a search bar cell is clicked.
     * Plays the selected song and resets the progress bar.
     *
     * @param event The {@link MouseEvent} that triggered the play button.
     */
    @FXML
    private void onButtonPlayMusic(MouseEvent event) {
        listener.playMusic();
    }

    /**
     * Sets the text of the music name label to the specified label.
     *
     * @param label The text to be set on the music name label.
     */
    public void setLabel(String label) {
        musicName.setText(label);
    }

    /**
     * Constructs a composite node representing a search bar cell.
     * The node is structured as an HBox containing two sub-HBoxes
     * with predefined styles and dimensions. The left sub-HBox
     * includes a label and a playlist button, while the right
     * sub-HBox includes a triple dot button and a heart button.
     * Both sub-HBoxes have a uniform background and border style.
     *
     * @return A Node representing the fully constructed search bar cell.
     */
    public Node createNode() {
        HBox root = new HBox();
        root.setPrefHeight(40);
        root.setPrefWidth(220);
        root.getStyleClass().add("search-bar-cell");
        root.getStylesheets().add(STYLESHEET_PATH);

        HBox leftBox = new HBox();
        leftBox.setPrefHeight(38);
        leftBox.setPrefWidth(219);
        leftBox.setStyle("-fx-background-color: #1A1D20; -fx-background-radius: 10; -fx-border-radius: 10;");
        createLabel(leftBox);
        createPlaylistButton(leftBox);

        HBox rightBox = new HBox();
        rightBox.setPrefHeight(38);
        rightBox.setPrefWidth(47);
        rightBox.setStyle("-fx-background-color: #1A1D20; -fx-background-radius: 10; -fx-border-radius: 10;");
        createTripleDotButton(rightBox);
        createHeartButton(rightBox);

        root.getChildren().addAll(leftBox, rightBox);
        return root;
    }

    /**
     * Creates a label displaying the song title and adds it to the specified HBox.
     * The label is given the CSS class "search-bar-song-label".
     * The label is also given a fixed width and height of 146 and 38 pixels, respectively.
     * The label is also given a font of size 14px, with font family "Arial".
     * The label is given a 5px left margin.
     * The label is then added to the specified HBox.
     *
     * @param hbox The HBox to add the label to.
     */
    private void createLabel(HBox hbox) {
        musicName.setText(song.getTitle());
        musicName.setPrefHeight(38);
        musicName.setPrefWidth(146);
        musicName.getStyleClass().add("search-bar-song-label");
        musicName.getStylesheets().add(STYLESHEET_PATH);
        musicName.setFont(new Font("Arial", 14));
        HBox.setMargin(musicName, new Insets(0, 0, 0, 5));
        hbox.getChildren().add(musicName);
    }

    /**
     * Creates a button for playing music and adds it to the specified HBox.
     * The button is given a fixed size of 30x30 pixels.
     * The button is given the CSS class "button-icon".
     * The button is given a style with a background color of #1A1D20 and a
     * border color of #1A1D20, with a background radius and border radius of 10
     * pixels.
     * The button is given an image of a play icon, which is retrieved from the
     * cache if possible, or loaded from the file system otherwise.
     * The button is given a 20x20 pixel image size, and is given a preserve ratio
     * of true.
     * The button is given a 5px left margin, a 0px top margin, a 0px right margin,
     * and a 20px bottom margin.
     * The button is then added to the specified HBox.
     *
     * @param hbox The HBox to add the button to.
     */
    private void createPlaylistButton(HBox hbox) {
        playMusicButton.setPrefSize(30, 30);
        playMusicButton.setOnMouseClicked(this::onButtonPlayMusic);
        playMusicButton.setStyle(
                "-fx-background-color: #1A1D20; -fx-border-color: #1A1D20; -fx-background-radius: 10; -fx-border-radius: 10;");
        playMusicButton.getStyleClass().add("button-icon");
        playMusicButton.getStylesheets().add(STYLESHEET_PATH);

        ImageView playIcon = new ImageView(listener.manageCachedImage(
                Objects.requireNonNull(getClass().getResource("/images/play.png")).toExternalForm()));
        playIcon.setFitHeight(20);
        playIcon.setFitWidth(20);
        playIcon.setPreserveRatio(true);
        playMusicButton.setGraphic(playIcon);
        HBox.setMargin(playMusicButton, new Insets(5, 0, 0, 20));
        hbox.getChildren().add(playMusicButton);
    }

    /**
     * Creates a button for the triple dot option and adds it to the specified HBox.
     * The button is given a fixed size of 23x15 pixels.
     * The button is given the CSS class "button-icon".
     * The button is given a style with a background color and border color of #1A1D20,
     * with a background radius and border radius of 10 pixels.
     * The button is given an image of a triple dot icon, which is retrieved from the
     * cache if possible, or loaded from the file system otherwise.
     * The button is given a 20x23 pixel image size, and is given a preserve ratio
     * of true.
     * The button is given a 5px left margin, a 4px top margin, a 0px right margin,
     * and a 5px bottom margin.
     * The button is then added to the specified HBox.
     *
     * @param hbox The HBox to add the button to.
     */
    private void createTripleDotButton(HBox hbox) {
        tripleDotButton.setPrefSize(23, 15);
        tripleDotButton.setOnMouseClicked(event -> openSongOption());
        tripleDotButton.setStyle(
                "-fx-border-color: #1A1D20; -fx-background-color: #1A1D20; -fx-background-radius: 10; -fx-border-radius: 10;");
        tripleDotButton.getStyleClass().add("button-icon");
        tripleDotButton.getStylesheets().add(STYLESHEET_PATH);

        ImageView icon = new ImageView(listener.manageCachedImage(
                Objects.requireNonNull(getClass().getResource("/images/tripledot.png")).toExternalForm()));
        icon.setFitHeight(20);
        icon.setFitWidth(23);
        icon.setPreserveRatio(true);
        tripleDotButton.setGraphic(icon);
        HBox.setMargin(tripleDotButton, new Insets(5, 4, 0, 5));
        hbox.getChildren().add(tripleDotButton);
    }

    /**
     * Creates a heart button and adds it to the specified HBox.
     * The button is initialized with a default size of 23x20 pixels and
     * is styled with a background and border color of #1A1D20, with a
     * background and border radius of 10 pixels. The button's icon is set
     * to display either a filled or outline heart image based on the
     * song's favorite status. Clicking the button will toggle the song's
     * favorite status and update the icon accordingly. The button is
     * given a 5px left margin, 4px top margin, and is added to the specified HBox.
     *
     * @param hbox The HBox to which the heart button is added.
     */
    private void createHeartButton(HBox hbox) {
        heartButton.setPrefSize(23, 20);
        heartButton.setOnMouseClicked(e -> handleHeartClick());
        heartButton.setStyle(
                "-fx-background-color: #1A1D20; -fx-border-color: #1A1D20; -fx-background-radius: 10; -fx-border-radius: 10;");
        heartButton.getStyleClass().add("button-icon");
        heartButton.getStylesheets().add(STYLESHEET_PATH);
        initializeHeartButton();
        heartImageView.setFitHeight(20);
        heartImageView.setFitWidth(20);
        heartImageView.setPreserveRatio(true);
        heartButton.setGraphic(heartImageView);
        HBox.setMargin(heartButton, new Insets(5, 4, 0, 5));
        hbox.getChildren().add(heartButton);
    }

    public interface SearchBarCellViewListener {
        void playMusic();

        void openSongOption(SearchBarCellView view);

        boolean isInFavorites();

        void addToFavorites();

        void removeFromFavorites();

        Image manageCachedImage(String imageUrl);
    }
}

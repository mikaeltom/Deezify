package ulb.views.options;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.FlowPane;

import java.util.List;

/**
 * A view for displaying the options for a playlist.
 * The view is a part of the main window, and it is responsible for displaying
 * the playlist options.
 * The playlist options include adding songs to a playlist, creating a new
 * playlist,
 * renaming a playlist, and deleting a playlist.
 * The view also allows the user to switch between different playlists.
 * The view communicates with the main controller to handle the different
 * operations.
 */
public class PlaylistOptionsView {
    @FXML
    private TextField playlistTextField;
    @FXML
    private FlowPane customPlaylistFlowPane;

    private PlaylistViewListener listener;

    public void setListener(PlaylistViewListener listener) {
        this.listener = listener;
    }

    /**
     * Handle the submission of a custom playlist name.
     * <p>
     * This method is invoked when the user submits a custom playlist name.
     * It creates a new playlist with the given name and adds the current song
     * to it in the database and in the class.
     * If the playlist already exists in the database, it shows an error message.
     * Finally, it clears the text field.
     */
    @FXML
    private void handleCustomPlaylistSubmit() {
        String playlistName = playlistTextField.getText().trim();
        if (!playlistName.isEmpty()) {
            listener.addToPlaylist(playlistName);
            playlistTextField.clear();
        }
        loadCustomPlaylist();
    }

    /**
     * Loads custom playlists and displays them in the UI.
     * <p>
     * This method retrieves all playlists from the controller and clears the
     * existing playlist buttons in the flow pane. For each playlist, it creates
     * a new custom playlist button and adds it to the UI.
     * </p>
     * <p>
     * Exceptions:
     * - SQLExceptionHandler: If a database access error occurs while fetching
     * playlists.
     * </p>
     * <p>
     * throws SQLExceptionHandler If a database access error occurs.
     */
    public void loadCustomPlaylist() {
        customPlaylistFlowPane.getChildren().clear();
        listener.getPlaylists().forEach(this::createCustomPlaylistButton);
    }

    /**
     * Creates a custom playlist button and adds it to the UI.
     * <p>
     * This method initializes a toggle button for the given playlist name,
     * applies a custom style, and checks if the current song is part of the
     * specified playlist. It sets the toggle button state accordingly and
     * adds an action handler to manage playlist selection.
     *
     * @param playlistName The name of the playlist for which the button is created.
     *                     throws SQLExceptionHandler If a database access error
     *                     occurs.
     */
    private void createCustomPlaylistButton(String playlistName) {
        if ("Library".equals(playlistName) || "Favorites".equals(playlistName)) {
            return;
        }
        ToggleButton playlistButton = new ToggleButton(playlistName);
        playlistButton.getStyleClass().add("custom-playlist-button");
        if (listener.isInDatabase(playlistName)) {
            boolean isInPlaylist = listener.isInPlaylist(playlistName);
            playlistButton.setSelected(isInPlaylist);
        }
        playlistButton.setOnAction(event -> listener.handlePlaylistSelection(playlistButton.isSelected(),
                playlistButton.getText()));
        customPlaylistFlowPane.getChildren().add(playlistButton);
    }

    /**
     * Closes the current playlist options view and opens the song options view.
     * <p>
     * The song options view is initialized with the current song's metadata,
     * allowing the user to edit and save changes. The current stage is updated
     * with the new scene containing the song options layout.
     * <p>
     * Exceptions:
     * - IOException: If the SongOption.fxml file cannot be loaded.
     */
    @FXML
    public void handleClose() {
        listener.close();
    }

    public interface PlaylistViewListener {
        void close();

        boolean isInDatabase(String playlistName);

        List<String> getPlaylists();

        void addToPlaylist(String playlistName);

        void handlePlaylistSelection(boolean selected, String text);

        boolean isInPlaylist(String playlistName);
    }
}

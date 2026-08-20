package ulb.views.songs;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import ulb.dtos.SongDTO;
import ulb.views.searches.SearchBarCellView;

import java.util.ResourceBundle;

/**
 * A class that manages the UI components and functionality for tagging songs
 * in the search bar triple dot menu. It interacts with the TagController to
 * manage tags for the current song and updates the UI components accordingly.
 */
public class SongOptionView {
    protected SongOptionViewListener listener;
    @FXML
    ResourceBundle resources;
    @FXML
    private Button saveButton;
    @FXML
    private TextField titleField, artistField, albumField;

    public void setListener(SongOptionViewListener listener) {
        this.listener = listener;
    }


    /**
     * Handles the action when the lyric button is clicked, loading the Lyrics view.
     */
    @FXML
    private void handleLyricsButtonClick() {
        listener.importLyrics();
    }

    /**
     * Handles the cover button click to import a cover image.
     */
    @FXML
    private void handleCoverButtonClick() {
        listener.importCover();
    }

    /**
     * Handles the action when the tag button is clicked, calling the addTags method on the listener with the current song's metadata.
     * titleField The title of the song to be tagged.
     * artistField The artist of the song to be tagged.
     * albumField The album of the song to be tagged.
     */
    @FXML
    private void handleTagButtonClick() {
        listener.addTags();
    }

    /**
     * Handles the action when the playlist button is clicked, calling the addPlaylist method on the listener with the current song's metadata.
     * titleField The title of the song to be added to the playlist.
     * artistField The artist of the song to be added to the playlist.
     * albumField The album of the song to be added to the playlist.
     */
    @FXML
    private void handlePlaylistButtonClick() {
        listener.addPlaylist();
    }

    /**
     * Handles the action when the queue button is clicked, calling the importQueue method on the listener.
     */
    @FXML
    private void handleQueueButtonClick() {
        listener.importQueue();
    }

    /**
     * Handles the action when the Favorites button is clicked.
     * Adds the current song to the Favorites.
     */
    @FXML
    private void handleFavoritesButtonClick() {
        listener.importFavorites();
    }

    /**
     * Handles the action when the delete song button is clicked.
     */
    @FXML
    private void handleDeleteSongButtonClick() {
        listener.deleteSong();
    }

    /**
     * Initialize the song options dialog.
     * <p>
     * This method initializes the song options dialog by setting the
     * title, artist, and album fields to the corresponding values
     * of the given song. It also sets up the save button to save
     * any changes made to the metadata of the song.
     *
     * @param song              The song to set the metadata fields of.
     * @param searchBarCellView The cell view of the search result.
     */
    public void initializeDialog(SongDTO song, SearchBarCellView searchBarCellView) {
        titleField.setText(song.getTitle());
        artistField.setText(toDisplayValue(song.getArtist()));
        albumField.setText(toDisplayValue(song.getAlbum()));
        setUpSaveMetadataButton(searchBarCellView);
    }

    /**
     * Sets up the save button to update the song's metadata and close the dialog.
     * <p>
     * This method attaches an action event to the save button, which updates the
     * song's metadata with the values from the title, artist, and album text fields
     * when clicked. If a {@link SearchBarCellView} is provided, it updates the
     * label
     * with the new title. After updating, the dialog is closed.
     *
     * @param searchBarCellView The cell view to update the label if not null.
     */
    public void setUpSaveMetadataButton(SearchBarCellView searchBarCellView) {
        saveButton.setOnAction(event -> listener.updateSongMetadata(
                titleField != null ? titleField.getText() : "",
                toLogicalValue(artistField.getText()),
                toLogicalValue(albumField.getText()),
                searchBarCellView
        ));
    }

    /**
     * Translates "Unknown" to the appropriate language for display.
     */
    private String toDisplayValue(String originalValue) {
        if (originalValue == null || originalValue.equalsIgnoreCase("Unknown")) {
            return resources.getString("unknown");
        }
        return originalValue;
    }

    /**
     * Converts the display value back to the "Unknown" value.
     */
    private String toLogicalValue(String displayValue) {
        if (displayValue == null) return "Unknown";
        String localizedUnknown = resources.getString("unknown");
        if (displayValue.equalsIgnoreCase(localizedUnknown)) {
            return "Unknown";
        }
        return displayValue;
    }

    public interface SongOptionViewListener {
        void deleteSong();

        void updateSongMetadata(String title, String artist, String album, SearchBarCellView searchBarCellView);

        void addPlaylist();

        void addTags();

        void importCover();

        void importLyrics();

        void importQueue();

        void importFavorites();
    }
}
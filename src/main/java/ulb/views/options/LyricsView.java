package ulb.views.options;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

/**
 * A window that displays the lyrics of the current song and allows the user
 * to import or delete the lyrics.
 */
public class LyricsView {

    private LyricsViewListener lyricsViewListener;
    @FXML
    private Button importLyricsButton;

    public void setListener(LyricsViewListener listener) {
        this.lyricsViewListener = listener;
    }


    /**
     * Handles the action when the import lyrics button is clicked.
     * Opens a file chooser dialog to select a lyrics file.
     * If the file is selected, it is copied to the "lyrics" directory
     * and the path to the copied file is saved in the database.
     * If the file copy or the database operation fails, the method
     * prints an error message and does not perform any further action.
     * If the file is not selected, the method does not perform any action.
     */
    @FXML
    public void handleLyricsImportButtonClick() {
        lyricsViewListener.importLyrics((Stage) importLyricsButton.getScene().getWindow());
    }

    /**
     * Handles the action when the delete lyrics button is clicked.
     * Deletes the lyrics for the current song.
     * If the song is not found in the database, or if the deletion of the lyrics
     * fails, the method throws a SQLExceptionHandler or InvalidSongException
     * respectively.
     */
    @FXML
    public void handleDeleteLyricsButtonClick() {
        lyricsViewListener.deleteLyrics();
    }

    /**
     * Handles the action when the close button is clicked in the lyrics window.
     * Loads the SongOption view and sets the current song in the SongOption controller.
     * Updates the current stage with the SongOption scene.
     */
    @FXML
    public void handleCloseLyricsButtonClick() {
        lyricsViewListener.closeImportLyricsWindow();
    }

    public interface LyricsViewListener {
        void closeImportLyricsWindow();

        void importLyrics(Stage stage);

        void deleteLyrics();

    }
}
package ulb.controllers.options;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ulb.exceptions.songs.InvalidSongException;
import ulb.exceptions.songs.SQLExceptionHandler;
import ulb.views.options.LyricsView;
import ulb.models.Lyrics;
import ulb.models.Song;
import ulb.utils.I18n;
import ulb.utils.PopupType;
import ulb.views.PopupView;
import java.io.IOException;

/**
 * Handles the lyrics of a song.
 * This class is not a singleton, because a user can have multiple songs open at
 * the same time.
 * It provides methods to get the text of the lyrics, load the lyrics from a lrc
 * file, and save the lyrics to a lrc file.
 * The lyrics file is stored in the same directory as the song.
 * If the song does not have a lyrics file, an empty list of lyrics is returned.
 */
public class LyricsController implements LyricsView.LyricsViewListener {
    private final LyricsUploader lyricsUploader = new LyricsUploader();
    private Song currentSong;
    private OptionListener lyricsListener;

    public void setListener(OptionListener listener) {
        this.lyricsListener = listener;
    }

    /**
     * Initializes the LyricsController with the specified song.
     * <p>
     * This method sets the current song for the controller and creates a new
     * Lyrics object associated with the song. It then loads the lyrics from
     * the .lrc file specified in the song's metadata. If the lyrics file
     * cannot be read, an IOException is thrown.
     *
     * @param song The song whose lyrics will be managed by this controller.
     * @throws IOException If an error occurs while reading the lyrics file.
     */
    public void initialize(Song song) throws IOException {
        Lyrics lyrics;
        this.currentSong = song;
        lyrics = new Lyrics(song);
        lyrics.loadFromLrcFile();
    }

    /**
     * Set the current song for the lyrics window.
     *
     * @param song The song whose lyrics will be displayed or updated.
     **/
    public void setCurrentSong(Song song) {
        this.currentSong = song;
    }

    /**
     * Imports lyrics for the current song by opening a file chooser dialog.
     * <p>
     * This method allows the user to select a lyrics file from their filesystem.
     * The selected file is then added to the current song's information
     * and stored in the database. If the operation fails due to an I/O
     * issue or a SQL exception, the relevant exception is thrown.
     *
     * @param stage The stage on which the file chooser dialog will be displayed.
     *              throws SQLExceptionHandler If an error occurs while updating the
     *              database.
     */
    public void importLyrics(Stage stage) {
        try {
            if (!lyricsUploader.add(stage, this.currentSong)) {
                return;
            }
            new PopupView("lyrics_import_title", "lyrics_import_success", PopupType.SUCCESS);
        } catch (IOException | SQLExceptionHandler e) {
            new PopupView("error_import_lyrics", e.getMessage(), PopupType.ERROR);
        }
    }

    /**
     * Deletes the lyrics associated with the current song.
     * <p>
     * This method removes the lyrics path from the song and updates the database
     * to reflect the removal of the lyrics. If the song's lyrics path is empty,
     * an InvalidSongException is thrown.
     * throws SQLExceptionHandler If an error occurs while updating the database.
     * throws IOException If an error occurs while deleting the file.
     */
    public void deleteLyrics() {
        try {
            lyricsUploader.delete(this.currentSong);
            new PopupView("lyrics_import_title", "lyrics_import_delete", PopupType.SUCCESS);
        } catch (IOException | SQLExceptionHandler | InvalidSongException e) {
            new PopupView("error_deleting_lyrics", e.getMessage(), PopupType.ERROR);
        }
    }

    /**
     * Closes the import lyrics window and opens the song options window.
     * <p>
     * This method is called when the user clicks the "Close" button in the
     * import lyrics window. It closes the import lyrics window and opens
     * the song options window with the current song's metadata pre-filled
     * and ready to be edited. The user can then edit the song's metadata
     * and save the changes.
     * <p>
     * throws IOException If the SongOption.fxml file cannot be loaded.
     */
    public void closeImportLyricsWindow() {
        lyricsListener.returnToSongOptionView(this.currentSong);
    }

    /**
     * Displays the lyrics view for a given song.
     * <p>
     * This method loads the Lyrics.fxml layout and initializes the lyrics view.
     * It sets the current song in the lyrics view and displays the lyrics view
     * in the provided stage.
     * <p>
     * Exceptions:
     * - IOException: If there is an error loading the Lyrics.fxml file.
     *
     * @param song  The Song object to be displayed in the lyrics view.
     * @param stage The Stage where the lyrics view will be displayed.
     */
    public void show(Song song, Stage stage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/ulb/views/Lyrics.fxml"), I18n.getBundle());
            Parent root = fxmlLoader.load();
            ulb.views.options.LyricsView lyricsController = fxmlLoader.getController();
            lyricsController.setListener(this);
            setCurrentSong(song);
            Scene scene = new Scene(root);
            stage.setScene(scene);
        } catch (IOException e) {
            new PopupView("loading_error_title", I18n.get("error_loading_lyrics"), PopupType.ERROR);
        }
    }
}

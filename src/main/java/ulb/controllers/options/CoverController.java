package ulb.controllers.options;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ulb.exceptions.songs.InvalidSongException;
import ulb.exceptions.songs.SQLExceptionHandler;
import ulb.models.Song;
import ulb.utils.I18n;
import ulb.utils.PopupType;
import ulb.views.PopupView;
import ulb.views.options.CoverView;
import java.io.IOException;

/**
 * Controller responsible for handling song cover operations.
 * This controller manages the logic for importing, deleting,
 * and closing the cover image without directly interacting with the FXML UI.
 */
public class CoverController implements CoverView.CoverViewListener {
    private final CoverUploader coverUploader = new CoverUploader();
    private Song currentSong;
    private OptionListener coverListener;

    public void setListener(OptionListener listener) {
        this.coverListener = listener;
    }

    public void setSong(Song song) {
        this.currentSong = song;
    }

    /**
     * Imports a cover image for the currently managed song.
     *
     * @param stage The stage where the file chooser dialog should appear.
     */
    public void importCover(Stage stage) {
        try {
            if (currentSong == null)
                return;
            if (!coverUploader.add(stage, currentSong)) {
                return;
            }
            String newImagePath = coverUploader.getSelectedImagePath();
            if (newImagePath != null && !newImagePath.equals("src/main/resources/img") && !newImagePath.isEmpty()) {
                currentSong.setImagePath(newImagePath);
                new PopupView("cover_import_title", "cover_import_success", PopupType.SUCCESS);
            }
            coverListener.saveCoverChange(currentSong, currentSong.getImagePath());
        } catch (SQLExceptionHandler | IOException e) {
            new PopupView("error_invalid_song", e.getMessage(), PopupType.ERROR);
        }
    }

    /**
     * Deletes the cover image for the currently managed song.
     */
    @Override
    public void deleteCover() {
        try {
            if (currentSong == null)
                return;
            coverUploader.delete(currentSong);
            currentSong.setImagePath(null);
            coverListener.saveCoverChange(currentSong, currentSong.getImagePath());
            new PopupView("cover_import_title", "cover_import_delete", PopupType.SUCCESS);
        } catch (InvalidSongException | SQLExceptionHandler | IOException e) {
            new PopupView("error_invalid_song", e.getMessage(), PopupType.ERROR);
        }
    }

    /**
     * Handles the closing of the cover window.
     * This method could include logic to return to the main view.
     */
    @Override
    public void closeCoverWindow() {
        coverListener.returnToSongOptionView(this.currentSong);
    }

    /**
     * Displays the CoverView for editing cover images.
     * <p>
     * This method loads the Cover.fxml layout and initializes the cover view.
     * It sets the current stage with the CoverView scene and establishes a
     * listener for handling cover-related actions.
     * <p>
     * Exceptions:
     * - IOException: If there is an error loading the Cover.fxml file.
     *
     * @param stage The stage to set the CoverView scene on.
     */
    public void show(Stage stage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/ulb/views/Cover.fxml"), I18n.getBundle());
            Parent root = fxmlLoader.load();
            CoverView coverViewController = fxmlLoader.getController();
            coverViewController.setListener(this);
            Scene scene = new Scene(root);
            stage.setScene(scene);
        } catch (IOException e) {
            new PopupView("loading_error_title", I18n.get("error_loading_cover_view"), PopupType.ERROR);
        }
    }
}

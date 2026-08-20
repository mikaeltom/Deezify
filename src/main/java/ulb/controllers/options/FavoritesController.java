package ulb.controllers.options;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ulb.models.Song;
import ulb.utils.I18n;
import ulb.utils.PopupType;
import ulb.views.PopupView;
import ulb.views.options.FavoritesView;
import java.io.IOException;

/**
 * Controller responsible for handling favorite song operations.
 * This controller manages the logic for creating,deleting,
 */
public class FavoritesController implements FavoritesView.FavoritesViewListener {
    private Song currentSong;
    private OptionListener optionListener;

    public void setListener(OptionListener listener) {
        this.optionListener = listener;
    }

    /**
     * Sets the song currently being edited.
     *
     * @param song The song to be managed.
     */
    public void setSong(Song song) {
        this.currentSong = song;
    }

    /**
     * Removes a song from the favorites.
     * Decreases the size of the favorites by 1, and updates all songs with a higher
     * position than the deleted song by -1.
     */
    public void removeSongFromFavorites() {
        optionListener.deleteFromFavorites(currentSong);
    }

    /**
     * Handles the closing of the cover window.
     * This method could include logic to return to the main view.
     */
    @Override
    public void closeFavoritesWindow() {
        optionListener.returnToSongOptionView(this.currentSong);
    }

    /**
     * Adds a song to the favorites. Favorites always
     * exist in the database if the app launched correctly.
     * Checks if the favorites don't already contain the song before
     * adding.
     */
    public void addSongToFavorites() {
        optionListener.addToFavorites(currentSong);
    }

    /**
     * Displays the favorites view for a given song.
     * <p>
     * This method loads the FavoritesView.fxml layout and initializes the
     * favorites view controller. It sets the current song and displays the
     * favorites view in the provided stage.
     *
     * @param song  The Song object to be displayed in the favorites view.
     * @param stage The Stage where the favorites view will be displayed.
     */
    public void show(Song song, Stage stage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/ulb/views/FavoritesView.fxml"),
                    I18n.getBundle());
            Parent root = fxmlLoader.load();
            FavoritesView favoritesView = fxmlLoader.getController();
            favoritesView.setListener(this);
            setSong(song);
            Scene scene = new Scene(root);
            stage.setScene(scene);
        } catch (IOException e) {
            new PopupView("loading_error_title", I18n.get("error_loading_favorites"), PopupType.ERROR);
        }
    }
}

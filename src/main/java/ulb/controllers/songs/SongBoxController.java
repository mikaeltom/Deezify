package ulb.controllers.songs;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import ulb.models.Song;
import ulb.utils.I18n;
import ulb.utils.ImageCache;
import ulb.utils.PopupType;
import ulb.views.PopupView;
import ulb.views.songs.SongBoxView;

import java.io.IOException;

/**
 * The SongBoxController is responsible for managing the interactions between
 * the SongBoxView and the main application logic.
 * It implements the SongBoxViewListener interface to handle user actions
 * such as clicking on the song box or the triple dot button.
 * This controller is also responsible for loading the SongBoxView via FXML
 * and managing the song information displayed within the view.
 */

public class SongBoxController implements SongBoxView.SongBoxViewListener {
    private final Song song;
    private final ImageCache imageCache;
    private final SongOptionControllerAccess songOptionControllerAccess;
    private final PlayerViewControllerAccess playerViewControllerAccess;
    private final CollectionControllerAccess collectionControllerAccess;

    public SongBoxController(Song song,
                             SongOptionControllerAccess songOptionControllerAccess,
                             PlayerViewControllerAccess playerViewControllerAccess,
                             CollectionControllerAccess collectionControllerAccess) {
        this.song = song;
        this.imageCache = ImageCache.getInstance();
        this.songOptionControllerAccess = songOptionControllerAccess;
        this.playerViewControllerAccess = playerViewControllerAccess;
        this.collectionControllerAccess = collectionControllerAccess;
    }

    /**
     * Loads the SongBoxView via FXML and initializes it with the given song.
     *
     * @return the root node of the loaded view, or null if an error occurs.
     */
    public Node load() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/ulb/views/SongBox.fxml"), I18n.getBundle());
            Parent root = fxmlLoader.load();
            SongBoxView songBoxView = fxmlLoader.getController();
            songBoxView.setSongBoxViewListener(this);
            songBoxView.initialize(song);
            return root;
        } catch (IOException e) {
            new PopupView("loading_error_title", I18n.get("error_loading_import_boxes"), PopupType.ERROR);
        }
        return null;
    }

    /**
     * Handles the action when the song box is clicked.
     * This method plays the selected song and displays the now playing box.
     */
    @Override
    public void onClickedSongBox() {
        playerViewControllerAccess.loadSong(song);
        collectionControllerAccess.onSongBoxClick(song);
    }

    /**
     * Handles the action when the triple dot button in the song box is clicked.
     * This method calls the corresponding method in the main controller listener
     * to show the song options.
     */
    @Override
    public void onClickTripleDot() {
        songOptionControllerAccess.show(song);
    }

    /**
     * Retrieves a cached image for the given image path.
     * If the image is not already cached, it will be loaded and cached.
     * The image is retrieved via the main controller listener.
     */
    @Override
    public Image manageCachedImage(String imagePath) {
        return imageCache.manageCachedImage(imagePath);
    }

    public interface SongOptionControllerAccess {
        void show(Song song);
    }

    public interface PlayerViewControllerAccess {
        void loadSong(Song song);
    }

    public interface CollectionControllerAccess {
        void onSongBoxClick(Song song);
    }
}

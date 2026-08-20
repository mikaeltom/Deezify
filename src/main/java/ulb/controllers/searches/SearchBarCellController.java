package ulb.controllers.searches;

import javafx.scene.Node;
import javafx.scene.image.Image;
import ulb.controllers.MainControllerAccess;
import ulb.models.Song;
import ulb.utils.ImageCache;
import ulb.views.searches.SearchBarCellView;

/**
 * A class responsible for managing the search bar cell UI component.
 * <p>
 * The class uses an FXML loader to create a UI component representing a song
 * in the search bar. The song's title is set on the corresponding label in the
 * UI. If an error occurs during loading, it logs the error and returns null.
 * <p>
 * This class also implements the SearchBarCellViewListener interface, which
 * means it provides implementations for the openSongOption and playMusic
 * methods, which are called by the SearchBarCellView when the user clicks on
 * the triple dot button or the play button in the search bar cell.
 */
public class SearchBarCellController implements SearchBarCellView.SearchBarCellViewListener {
    private final MainControllerAccess mainControllerAccess;
    private final CollectionControllerAccess collectionControllerAccess;
    private final SongOptionControllerAccess songOptionControllerAccess;
    private final PlayerViewControllerAccess playerViewControllerAccess;
    private final ImageCache imageCache;
    private Song currentSong;

    public SearchBarCellController(MainControllerAccess mainControllerAccess, CollectionControllerAccess collectionControllerAccess, SongOptionControllerAccess songOptionControllerAccess, PlayerViewControllerAccess playerViewControllerAccess) {
        this.mainControllerAccess = mainControllerAccess;
        this.collectionControllerAccess = collectionControllerAccess;
        this.songOptionControllerAccess = songOptionControllerAccess;
        this.playerViewControllerAccess = playerViewControllerAccess;
        this.imageCache = ImageCache.getInstance();
    }

    private void setCurrentSong(Song song) {
        this.currentSong = song;
    }

    /**
     * Loads a search bar cell UI component for a given song.
     * <p>
     * This method uses an FXML loader to create a UI component representing a song
     * in the search bar. The song's title is set on the corresponding label in the
     * UI. If an error occurs during loading, it logs the error and returns null.
     *
     * @param song The Song object for which the search bar cell is to be created.
     * @return A Node representing the search bar cell UI component, or null if an
     * error occurs.
     */
    public Node loadSearchBarCell(Song song) {
        setCurrentSong(song);
        SearchBarCellView view = new SearchBarCellView(song);
        view.setListener(this);
        view.setMusicName(song.getTitle());
        view.initializeHeartButton();
        return view.createNode();
    }

    /**
     * Updates the collections view by retrieving all music collections
     * from the database and updating the CollectionsView with the retrieved data.
     * Then, it updates the current collection of songs in the library.
     */
    private void updateCollections() {
        collectionControllerAccess.refreshCollections();
        mainControllerAccess.updateCurrentCollection();
    }

    @Override
    public void openSongOption(SearchBarCellView view) {
        songOptionControllerAccess.show(currentSong, view);
    }

    @Override
    public boolean isInFavorites() {
        return collectionControllerAccess.isFavorites(currentSong);
    }

    /**
     * Adds the given song to the favourite collection.
     * <p>
     * This method delegates the task of adding a song to the favorites collection
     * to the {@link MainControllerAccess} instance passed to the constructor.
     */
    @Override
    public void addToFavorites() {
        songOptionControllerAccess.addToFavorites(currentSong);
        updateCollections();
    }

    /**
     * Removes the current song from the favorites collection.
     * <p>
     * This method delegates the task of removing a song from the favorites collection
     * to the {@link SongOptionControllerAccess} instance passed to the constructor.
     * After removal, it updates the collections to reflect the change.
     */

    @Override
    public void removeFromFavorites() {
        songOptionControllerAccess.deleteFromFavorites(currentSong);
        updateCollections();
    }

    @Override
    public void playMusic() {
        playerViewControllerAccess.loadAndPlay(currentSong);
    }

    @Override
    public Image manageCachedImage(String imageUrl) {
        return imageCache.manageCachedImage(imageUrl);
    }


    public interface CollectionControllerAccess {
        void refreshCollections();

        boolean isFavorites(Song song);
    }

    public interface SongOptionControllerAccess {
        void addToFavorites(Song song);

        void deleteFromFavorites(Song song);

        void show(Song song, SearchBarCellView view);
    }

    public interface PlayerViewControllerAccess {
        void loadAndPlay(Song song);
    }
}

package ulb.views.options;

/**
 * A class that manages the favorites view.
 * The favorites view is a popup window that is displayed when the user clicks on the
 * favorites button in the song options dialog.
 * It contains a button that closes the popup window and a list of buttons representing
 * the songs in the user's favorites.
 * When the user clicks on one of the buttons, the corresponding song is played.
 */
public class FavoritesView {
    private FavoritesViewListener favoritesViewListener;

    public void handleFavoritesImportButtonClick() {

        favoritesViewListener.addSongToFavorites();
    }

    /**
     * Handles the action when the delete favorites button is clicked.
     * Triggers the deletion of the current song from the user's favorites.
     */
    public void handleDeleteFavoritesButtonClick() {
        favoritesViewListener.removeSongFromFavorites();
    }

    /**
     * Handles the action when the close favorites button is clicked.
     * Closes the window containing the favorites view.
     */
    public void handleCloseFavoritesButtonClick() {
        favoritesViewListener.closeFavoritesWindow();
    }

    /**
     * Sets the listener for this view.
     * The listener is informed when the user interacts with the view.
     */
    public void setListener(FavoritesViewListener favoritesViewListener) {
        this.favoritesViewListener = favoritesViewListener;
    }

    public interface FavoritesViewListener {
        void closeFavoritesWindow();

        void addSongToFavorites();

        void removeSongFromFavorites();

    }
}

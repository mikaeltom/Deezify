package ulb.controllers.songs;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ulb.controllers.MainControllerAccess;
import ulb.controllers.playboxes.NowPlayingUpdateHandler;
import ulb.controllers.options.*;
import ulb.controllers.searches.SearchBarCellController;
import ulb.exceptions.songs.InvalidSongException;
import ulb.exceptions.songs.SQLExceptionHandler;
import ulb.models.Playlist;
import ulb.models.Song;
import ulb.services.BannedWordService;
import ulb.services.SongService;
import ulb.utils.I18n;
import ulb.utils.PopupType;
import ulb.views.PopupView;
import ulb.views.searches.SearchBarCellView;
import ulb.views.songs.SongOptionView;

import java.io.IOException;
import java.util.HashMap;

/**
 * The SongOptionController class is responsible for managing the song options
 * such as adding tag, cover, lyrics, queue, and favorites.
 */
public class SongOptionController implements SongOptionView.SongOptionViewListener, OptionListener, SearchBarCellController.SongOptionControllerAccess, SongBoxController.SongOptionControllerAccess {
    public final MusicUploader musicUploader;
    private final SongService songService;
    private final LyricsController lyricsController = new LyricsController();
    private final CoverController coverController = new CoverController();
    private final QueueController queueController = new QueueController();
    private final FavoritesController favoritesController = new FavoritesController();
    private final TagController tagController;
    private final PlaylistController playlistController;
    private final NowPlayingUpdateHandler nowPlayingUpdateHandler;
    private Stage stage;
    private Song song;
    private SearchBarCellView searchBarCellView;
    private CollectionControllerAccess collectionControllerAccess;
    private MainControllerAccess mainControllerAccess;

    public SongOptionController(MusicUploader musicUploader, SongService songService, TagController tagController, NowPlayingUpdateHandler nowPlayingUpdateHandler, BannedWordService bannedWordManager) {
        this.musicUploader = musicUploader;
        this.songService = songService;
        this.tagController = tagController;
        this.nowPlayingUpdateHandler = nowPlayingUpdateHandler;
        this.playlistController = new PlaylistController(bannedWordManager);
    }

    public void setMainControllerAccess(MainControllerAccess access) {
        this.mainControllerAccess = access;
    }

    public void setCollectionControllerAccess(CollectionControllerAccess collectionControllerAccess) {
        this.collectionControllerAccess = collectionControllerAccess;
    }

    /**
     * Displays a popup window with options for the selected song.
     *
     * @param song              The Song object whose options are being displayed.
     * @param searchBarCellView The SearchBarCellView associated with the song.
     */
    public void show(Song song, SearchBarCellView searchBarCellView) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ulb/views/SongOption.fxml"), I18n.getBundle());
            Parent popupRoot = loader.load();
            SongOptionView viewController = loader.getController();
            this.song = song;
            this.searchBarCellView = searchBarCellView;

            viewController.setListener(this);
            viewController.initializeDialog(song, searchBarCellView);
            Stage popupStage = new Stage();
            popupStage.initModality(Modality.APPLICATION_MODAL);
            setupOptionWindow(popupStage, popupRoot);

            stage = popupStage;
        } catch (IOException e) {
            new PopupView("loading_error_title", I18n.get("error_loading_songoption"), PopupType.ERROR);
        }
    }

    /**
     * Setups the options popup window.
     *
     * @param popupStage The popup window to be set up.
     * @param popupRoot  The root of the popup window.
     */
    private void setupOptionWindow(Stage popupStage, Parent popupRoot) {
        popupStage.setScene(new Scene(popupRoot));
        popupStage.setResizable(false);
        popupStage.centerOnScreen();
        popupStage.show();
    }

    /**
     * Updates the cover image of a song in the database and locally.
     *
     * @param song      The song whose cover image is to be updated.
     * @param imagePath The new cover image path, or null or empty if no change is desired.
     */
    private void updateCoverInMetadata(Song song, String imagePath) {
        try {
            song.setImagePath(imagePath);
            HashMap<String, String> metadata = new HashMap<>();
            metadata.put("Images", (imagePath == null || imagePath.isEmpty() ? song.getImagePath() : imagePath));
            songService.setMetadata(song, metadata);
        } catch (SQLExceptionHandler e) {
            new PopupView("error_loading_metadata", e.getMessage(), PopupType.ERROR);
        }
    }

    /**
     * Updates the cover image path for a song in the database and locally.
     * This method is called when the user saves changes to the cover image.
     *
     * @param song      The song whose cover image is to be updated.
     * @param imagePath The new cover image path, or null or empty if no change is desired.
     */
    @Override
    public void saveCoverChange(Song song, String imagePath) {
        if (imagePath != null && !imagePath.isEmpty() && !imagePath.equals("./img/")) {
            song.setImagePath(imagePath);
            updateCoverInMetadata(song, imagePath);
            updateCurrentCollection();
            nowPlayingUpdateHandler.updateNowPlayingBox(song);
        }
    }

    /**
     * Updates the metadata for the current song with the specified title, artist, and album.
     *
     * @param titleField        The new title for the song.
     * @param artistField       The new artist for the song.
     * @param albumField        The new album for the song.
     * @param searchBarCellView The search bar cell view to update the title if applicable.
     */
    @Override
    public void updateSongMetadata(String titleField, String artistField, String albumField,
                                   SearchBarCellView searchBarCellView) {
        try {
            if (!song.getTitle().equals(titleField) || !song.getArtist().equals(artistField)
                    || !song.getAlbum().equals(albumField)) {
                songService.updateSongMetadata(song, titleField, artistField, albumField);
                updateCurrentCollection();
                this.updateSearchBarCellTitle(titleField, searchBarCellView);
                this.nowPlayingUpdateHandler.updateNowPlayingBox(song);
                new PopupView("song_metadata_title", "song_metadata_success", PopupType.SUCCESS);
            }
            stage.close();
        } catch (SQLExceptionHandler e) {
            new PopupView("error_update_metadata", e.getMessage(), PopupType.ERROR);
        }
    }

    /**
     * Updates the title of a given search bar cell view.
     *
     * @param title             The new title for the search bar cell view.
     * @param searchBarCellView The search bar cell view to update the title of.
     */
    private void updateSearchBarCellTitle(String title, SearchBarCellView searchBarCellView) {
        if (searchBarCellView != null) {
            searchBarCellView.setLabel(title);
        }
    }

    /**
     * Adds the specified song to the queue.
     *
     * @param song The song to be added to the queue.
     */
    @Override
    public void addToQueue(Song song) {
        collectionControllerAccess.addToQueue(song);
        updateCurrentCollection();
    }

    private void deleteSongFromQueue(Song song) {
        collectionControllerAccess.deleteSongFromQueue(song);
    }

    /**
     * Removes the specified song from the queue.
     *
     * @param song The song to be removed from the queue.
     */
    @Override
    public void deleteFromQueue(Song song) {
        deleteSongFromQueue(song);
        updateCurrentCollection();
    }

    private Playlist getPlaylistFromName(String title) {
        return collectionControllerAccess.getPlaylistByTitle(title);
    }


    /**
     * Adds the specified song to the Favorites collection.
     *
     * @param currentSong The song to be added to the Favorites collection.
     */
    @Override
    public void addToFavorites(Song currentSong) {
        try {
            Playlist playlist = getPlaylistFromName("Favorites");
            playlistController.addSongToPlaylist(currentSong, playlist);
            updateCurrentCollection();
            loadCollections();
            new PopupView("favorites_import_title", "favorites_import_success", PopupType.SUCCESS);
        } catch (SQLExceptionHandler | IOException e) {
            new PopupView("error_song_to_favorites_title", I18n.get("error_adding_song_to_favorites"), PopupType.ERROR);
        }
    }

    /**
     * Removes the specified song from the Favorites collection.
     *
     * @param song The song to be removed from the Favorites collection.
     *             If an I/O error occurs.
     */
    @Override
    public void deleteFromFavorites(Song song) {
        try {
            Playlist playlist = getPlaylistFromName("Favorites");
            playlistController.removeSongFromPlaylist(song, playlist);
            updateCurrentCollection();
            new PopupView("favorites_import_title", "favorites_delete", PopupType.SUCCESS);
        } catch (SQLExceptionHandler e) {
            new PopupView("error_remove_song_favorites_title", I18n.get("error_remove_song_favorites"), PopupType.ERROR);
        }
    }

    /**
     * Deletes the current song from the library, queue, and favorites.
     */
    @Override
    public void deleteSong() {
        try {
            musicUploader.delete(song);
            deleteSongFromQueue(song);
            updateCurrentCollection();
            stage.close();
            new PopupView("delete_song_title", "delete_song_success", PopupType.SUCCESS);
        } catch (SQLExceptionHandler | IOException | InvalidSongException e) {
            new PopupView("error_deleting_song_title", e.getMessage(), PopupType.ERROR);
        }
    }

    /**
     * Opens the playlist view and sets the current song along with its metadata.
     */
    @Override
    public void addPlaylist() {
        playlistController.setListener(this);
        playlistController.show(song, stage);
    }

    /**
     * Opens the tag view and sets the current song along with its metadata.
     */
    @Override
    public void addTags() {
        tagController.setListener(this);
        tagController.show(song, stage);
    }

    /**
     * Opens the cover import view and sets the current song.
     */
    @Override
    public void importCover() {
        coverController.setListener(this);
        coverController.setSong(song);
        coverController.show(stage);
    }

    /**
     * Opens the lyrics import view and sets the current song.
     */
    @Override
    public void importLyrics() {
        lyricsController.setListener(this);
        lyricsController.show(song, stage);
    }

    /**
     * Opens the queue view and sets the current song.
     */
    @Override
    public void importQueue() {
        queueController.setListener(this);
        queueController.show(song, stage);
    }

    /**
     * Opens the favorites view and sets the current song.
     */
    @Override
    public void importFavorites() {
        favoritesController.setListener(this);
        favoritesController.show(song, stage);
    }

    /**
     * Hides the current song option view and displays the song option view for the given song.
     *
     * @param newSong The song whose song option view should be displayed.
     */
    @Override
    public void returnToSongOptionView(Song newSong) {
        stage.hide();
        this.show(newSong, searchBarCellView);
    }

    /**
     * Method updates the currently selected collection.
     */
    @Override
    public void updateCurrentCollection() {
        mainControllerAccess.updateCurrentCollection();
    }

    /**
     * Load the collections.
     */
    @Override
    public void loadCollections() {
        collectionControllerAccess.refreshCollections();
    }

    @Override
    public void show(Song song){
        show(song, searchBarCellView);
    }

    public interface CollectionControllerAccess {
        Playlist getPlaylistByTitle(String playlistTitle);

        void addToQueue(Song song);

        void refreshCollections();

        void deleteSongFromQueue(Song song);
    }
}

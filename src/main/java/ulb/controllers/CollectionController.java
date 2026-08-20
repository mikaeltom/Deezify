package ulb.controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import ulb.controllers.playboxes.NowPlayingUpdateHandler;
import ulb.controllers.searches.SearchBarCellController;
import ulb.controllers.songs.SongListController;
import ulb.controllers.songs.SongOptionController;
import ulb.controllers.songs.SongBoxController.CollectionControllerAccess;
import ulb.controllers.songs.SongBoxController.PlayerViewControllerAccess;
import ulb.controllers.songs.SongBoxController.SongOptionControllerAccess;
import ulb.dtos.MusicCollectionDTO;
import ulb.dtos.PlaylistDTO;
import ulb.dtos.QueueDTO;
import ulb.exceptions.songs.SQLExceptionHandler;
import ulb.models.Playlist;
import ulb.models.Song;
import ulb.services.collections.CollectionService;
import ulb.services.collections.MusicQueueService;
import ulb.services.collections.PlaylistService;
import ulb.utils.I18n;
import ulb.utils.PopupType;
import ulb.views.CollectionsView;
import ulb.views.PopupView;

import java.io.IOException;
import java.util.*;

/**
 * Manages the user's collections (Library, Favorites, Queue, and Playlists).
 * Provides methods to retrieve the collections, add new collections, and
 * update collections.
 * It also provides methods to retrieve the currently selected collection
 * and to set the currently selected collection.
 */
public class CollectionController
        implements CollectionsView.CollectionsViewListener, SongListController.SongListListener,
        SongOptionController.CollectionControllerAccess, SearchBarCellController.CollectionControllerAccess,
        CollectionControllerAccess, PlayerController.CollectionControllerAccess,
        NowPlayingUpdateHandler.CollectionControllerAccess {
    private final MusicQueueService musicQueueService;
    private final PlaylistService playlistService;
    private final CollectionService collectionService;
    private final SongListController songListController;
    private CollectionsView collectionsView;
    private Parent collectionsViewParent;
    private AbstractMap.SimpleEntry<List<Song>, String> currentSongList;
    private MainControllerAccess mainControllerAccess;
    private AbstractMap.SimpleEntry<String, Song> lastPlaylist;

    public CollectionController(MusicQueueService musicQueueService,
            PlaylistService playlistService,
            SongOptionControllerAccess songOptionControllerAccess,
            PlayerViewControllerAccess playerViewControllerAccess) {
        this.musicQueueService = musicQueueService;
        this.playlistService = playlistService;
        this.currentSongList = new AbstractMap.SimpleEntry<>(getPlaylistByTitle("Library").getSongs(),
                "Library");
        this.songListController = new SongListController(currentSongList.getKey(),
                songOptionControllerAccess, playerViewControllerAccess, this);
        this.songListController.setSongListListener(this);
        this.collectionService = new CollectionService(musicQueueService, playlistService);
    }

    void setMainControllerAccess(MainControllerAccess mainControllerAccess) {
        this.mainControllerAccess = mainControllerAccess;
    }

    public AbstractMap.SimpleEntry<List<Song>, String> getCurrentSongList() {
        return currentSongList;
    }

    /**
     * Updates the currently selected collection.
     * If the current collection is the Queue, it will update the Queue.
     * If the current collection is the Favorites, it will update the Favorites.
     * If the current collection is a Playlist, it will update the Playlist.
     */
    public void updateCurrentCollection() {
        if (currentSongList.getValue().equals("Queue")) {
            updateToQueue();
        } else if (currentSongList.getValue().equals("Favorites")) {
            updateToFavorites();
        } else {
            updateToPlaylist(currentSongList.getValue());
        }
    }

    public String getSelectedCollection() {
        return collectionsView.getCurrentLabel();
    }

    public ArrayList<Song> getQueue() {
        return musicQueueService.getQueue();
    }

    /**
     * Retrieves the user's favorites.
     *
     * @return A list of Song objects representing the user's favorites, or
     *         null if the operation fails.
     */
    public ArrayList<Song> getFavorites() {
        try {
            return (ArrayList<Song>) playlistService.getPlaylistFromName("Favorites").getSongs();
        } catch (SQLExceptionHandler e) {
            new PopupView("error_getting_favorites", e.getMessage(), PopupType.ERROR);
            return new ArrayList<>();
        }
    }

    /**
     * Updates the current song list to the Queue collection.
     * Sets the currentSongList to the list of songs in the queue and updates
     * the song list controller with the queue's songs.
     */
    public void updateToQueue() {
        this.currentSongList = new AbstractMap.SimpleEntry<>(getQueue(), "Queue");
        songListController.updateSongsList(currentSongList.getKey());
    }

    /**
     * Updates the current song list to the specified playlist.
     * Sets the currentSongList to the list of songs in the given playlist and
     * updates
     * the song list controller with the playlist's songs.
     *
     * @param playlistTitle The title of the playlist to update to.
     */
    public void updateToPlaylist(String playlistTitle) {
        this.currentSongList = new AbstractMap.SimpleEntry<>(
                getPlaylistByTitle(playlistTitle).getSongs(), playlistTitle);
        songListController.updateSongsList(currentSongList.getKey());
    }

    /**
     * Updates the current song list to the Library collection.
     * Sets the currentSongList to the list of songs in the library and updates
     * the song list controller with the library's songs.
     */
    public void updateToLibrary() {
        this.currentSongList = new AbstractMap.SimpleEntry<>(getPlaylistByTitle("Library").getSongs(),
                "Library");
        songListController.updateSongsList(currentSongList.getKey());
    }

    /**
     * Updates the current song list to the Favorites collection.
     * Sets the currentSongList to the list of songs in the favorites
     * and updates the song list controller with the favorites' songs.
     */
    public void updateToFavorites() {
        this.currentSongList = new AbstractMap.SimpleEntry<>(getFavorites(), "Favorites");
        songListController.updateSongsList(currentSongList.getKey());
    }

    /**
     * Adds a song to the music queue.
     *
     * @param song The song to add to the queue.
     *             throws IOException If the song is not valid.
     */
    @Override
    public void addToQueue(Song song) {
        try {
            musicQueueService.addSong(song);
            new PopupView("queue_import_title", "queue_import_success", PopupType.SUCCESS);
        } catch (IOException e) {
            new PopupView("error_adding_song_title", I18n.get("error_adding_queue"), PopupType.ERROR);
        }
    }

    /**
     * Retrieves the title of the next song in the music queue.
     *
     * @return The title of the next song in the queue, or null if the queue is
     *         empty.
     */
    @Override
    public String getNextSongTitleQueue() {
        return musicQueueService.getNextSongTitle();
    }

    /**
     * Deletes a playlist from the library.
     * <p>
     * Retrieves the ID of the playlist by name, removes it from the database,
     * and updates the library.
     * If an error occurs, it throws an IOException and displays an error popup.
     *
     * @param name The name of the playlist to delete.
     */
    @Override
    public void deletePlaylist(String name) {
        try {
            playlistService.deletePlaylist(name);
            updateToLibrary();
            collectionsView.refresh();
            this.collectionsView.setMusicCollection(getAllCollections());
        } catch (SQLExceptionHandler | IOException e) {
            new PopupView("error_deleting_playlist_title", I18n.get("error_delete_playlist_message"), PopupType.ERROR);
        }
    }

    /**
     * Clears the music queue by removing all songs from it.
     * Also updates the library by retrieving the new collections,
     * and refreshes the collections view.
     */
    @Override
    public void clearQueue() {
        musicQueueService.clearQueue();
        updateToLibrary();
        collectionsView.refresh();
    }

    public void deleteSongFromQueue(Song song) {
        musicQueueService.deleteSong(song);
    }

    /**
     * Skips to the provided song in the music queue.
     * <p>
     * Sets the provided song as the next song in the music queue,
     * updates the current song list to the queue,
     * and retrieves the next song in the queue (which is the provided song).
     * </p>
     *
     * @param song The song to skip to in the queue.
     */
    public void skipToInQueue(Song song) {
        musicQueueService.skipTo(song);
        updateToQueue();
        musicQueueService.getNext(song);
    }

    /**
     * Retrieves a playlist by title.
     * <p>
     * Returns a Playlist object containing the playlist's ID, name, songs, and
     * tags if the playlist exists in the database. If the playlist does not
     * exist, it displays an error popup.
     * </p>
     *
     * @param playlistTitle The title of the playlist to retrieve.
     * @return A Playlist object containing the playlist's ID, name, songs, and
     *         tags, or null if an error occurs.
     */
    @Override
    public Playlist getPlaylistByTitle(String playlistTitle) {
        try {
            return playlistService.getPlaylistFromName(playlistTitle);
        } catch (SQLExceptionHandler e) {
            new PopupView("error_getting_playlist_title", e.getMessage(), PopupType.ERROR);
            return null;
        }
    }

    /**
     * Retrieves all collections from the database.
     * <p>
     * Returns a list containing the music queue, all playlists for the user
     * with ID 1, and the user's Favorites collection.
     * </p>
     *
     * @return A list of all collections.
     * @throws SQLExceptionHandler If an error occurs while retrieving the
     *                             collections from the database.
     * @throws IOException         If an error occurs while retrieving the
     *                             collections from the database.
     */
    public List<MusicCollectionDTO> getAllCollections() throws SQLExceptionHandler, IOException {
        List<MusicCollectionDTO> collections = new ArrayList<>();
        collections.add(musicQueueService.getMusicQueue());
        collections.addAll(playlistService.getPlaylistsForUser());
        return collections;
    }

    /**
     * Handles the selection of a collection in the sidebar.
     * <p>
     * If the selected collection is the Queue, it updates the current song list
     * to the music queue.
     * If the selected collection is the Favorites, it updates the current song
     * list to the Favorites.
     * If the selected collection is a Playlist, it updates the current song list
     * to the Playlist.
     * If the Playlist does not exist, it displays an error popup.
     * </p>
     *
     * @param label The name of the collection that was selected.
     */
    public void handleClickedCollection(String label) {
        try {
            if (label.equals("Queue")) {
                updateToQueue();
            } else {
                Playlist playlist = playlistService.getPlaylistFromName(label);
                if (playlist != null) {
                    this.currentSongList = new AbstractMap.SimpleEntry<>(
                            getPlaylistByTitle(label).getSongs(), label);
                    songListController.updateSongsList(currentSongList.getKey());
                } else {
                    new PopupView("error_while_loading_playlist", I18n.get("playlist_not_found"), PopupType.ERROR);
                }
            }
        } catch (SQLExceptionHandler e) {
            new PopupView("error_while_loading_collection_title", e.getMessage(), PopupType.ERROR);
        }
    }

    /**
     * Returns a string to be used as the label for a collection in the sidebar.
     * If the collection is a playlist, the name of the playlist is returned.
     * If the collection is a queue, the string "Queue" is returned.
     * If the collection is a library, the string "Library" is returned.
     * Otherwise, the string "Nothing" is returned.
     *
     * @param collection The collection to get the label for.
     * @return The label to be used for the collection in the sidebar.
     */
    public String getCollectionLabel(MusicCollectionDTO collection) {
        if (collection instanceof PlaylistDTO) {
            return collection.getName();
        } else if (collection instanceof QueueDTO) {
            return "Queue";
        }
        return "Nothing";
    }

    /**
     * Displays the CollectionsView by loading the FXML file and initializing
     * the view controller. It sets the current instance as the listener for
     * the CollectionsView and populates it with all music collections.
     *
     * @throws SQLExceptionHandler If an error occurs while retrieving collections
     *                             from the database.
     */
    public void show() throws SQLExceptionHandler {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ulb/views/CollectionsView.fxml"),
                    I18n.getBundle());
            collectionsViewParent = loader.load();
            this.collectionsView = loader.getController();
            this.collectionsView.setCollectionsViewListener(this);
            this.collectionsView.setMusicCollection(getAllCollections());
            songListController.show();
        } catch (IOException e) {
            new PopupView("loading_error_title", I18n.get("error_while_loading_collection"), PopupType.ERROR);
        }
    }

    public Parent getCollectionsViewParent() {
        return collectionsViewParent;
    }

    /**
     * Refreshes the collections view by retrieving all music collections
     * from the database and updating the CollectionsView with the retrieved data.
     * <p>
     * If an error occurs during the retrieval process, it catches the exception
     * and displays an error popup with the exception message.
     * </p>
     */
    @Override
    public void refreshCollections() {
        try {
            collectionsView.setMusicCollection(getAllCollections());
        } catch (SQLExceptionHandler | IOException e) {
            new PopupView("loading_error_title", I18n.get("error_while_loading_collection"), PopupType.ERROR);
        }
    }

    public void setLastPlaylist(String playlistTitle, Song currentSong) {
        this.lastPlaylist = new AbstractMap.SimpleEntry<>(playlistTitle, currentSong);
    }

    public Parent getSongListViewChildren() {
        return songListController.getSongListViewParent();
    }

    /**
     * Updates the current song list after a drag and drop operation.
     * <p>
     * If the current song list is the Queue, it updates the Queue by calling
     * {@link #queuesDragAndDrop(List)}. Otherwise, it updates the current
     * playlist by calling {@link #playlistsDragAndDrop(List)}.
     *
     * @param newSongList The new list of songs.
     */
    @Override
    public void updateFromDragAndDrop(List<Song> newSongList) {
        if (newSongList == null || newSongList.isEmpty())
            return;
        if (currentSongList.getValue().equals("Queue")) {
            queuesDragAndDrop(newSongList);
        } else {
            playlistsDragAndDrop(newSongList);
        }
    }

    /**
     * Updates the current playlist after a drag and drop operation.
     *
     * <p>
     * It checks if the order of songs has changed between the current playlist
     * songs
     * and the new song list. If the order has changed, it updates the song
     * positions
     * in the database and updates the current song list.
     * </p>
     *
     * @param newSongList The new list of songs.
     */
    private void playlistsDragAndDrop(List<Song> newSongList) {
        try {
            collectionService.handlePlaylistDragAndDrop(newSongList, getPlaylistFromCurrentSongList());
            this.currentSongList = new AbstractMap.SimpleEntry<>(
                    getPlaylistByTitle(currentSongList.getValue()).getSongs(),
                    currentSongList.getValue());
        } catch (SQLExceptionHandler e) {
            new PopupView("error_updating_playlist_title", e.getMessage(), PopupType.ERROR);
        }
    }

    /**
     * Retrieves the Playlist object corresponding to the current song list
     * by looking up its name in the database.
     *
     * @return The Playlist object corresponding to the current song list, or
     *         null if the operation fails.
     */
    private Playlist getPlaylistFromCurrentSongList() {
        try {
            return playlistService.getPlaylistFromName(currentSongList.getValue());
        } catch (SQLExceptionHandler e) {
            new PopupView("error_getting_playlist_title", e.getMessage(), PopupType.ERROR);
            return null;
        }
    }

    /**
     * Updates the current song list to the new song list after a drag and drop
     * operation in the Queue.
     * Sets the currentSongList to the list of songs in the Queue and updates
     * the song list controller with the Queue's songs.
     *
     * @param newSongList The new list of songs.
     */
    private void queuesDragAndDrop(List<Song> newSongList) {
        musicQueueService.setNewQueue((ArrayList<Song>) newSongList);
        this.currentSongList = new AbstractMap.SimpleEntry<>(getQueue(), currentSongList.getValue());
    }

    /**
     * Checks if the given song is in the user's favorites collection.
     *
     * @param song The song to check.
     * @return True if the song is in the favorites, false otherwise.
     */
    public boolean isFavorites(Song song) {
        return getFavorites().contains(song);
    }

    /**
     * Handles the selection of a song box in the current collection.
     * <p>
     * If the selected collection is not the Queue, it sets the last used playlist
     * to the current collection and the current song.
     * If the selected collection is the Queue, it updates the current song list.
     * </p>
     *
     * @param song The song that was selected.
     */
    @Override
    public void onSongBoxClick(Song song) {
        if (!getSelectedCollection().equals("Queue")) {
            setLastPlaylist(getSelectedCollection(), song);
        }

        if (getSelectedCollection().equals("Queue")
                && getCurrentSongList().getValue().equals("Queue")) {
            skipToInQueue(song);
        }
    }

    /**
     * Retrieves the next or previous song in the current collection.
     *
     * <p>
     * If the current collection is not the Queue, it sets the last used playlist
     * to the current collection and the current song.
     * If the current collection is the Queue, it updates the current song list.
     * </p>
     *
     * @param isPrevious  If true, it returns the previous song in the collection.
     *                    If false, it returns the next song in the collection.
     * @param currentSong The current song.
     * @return The next or previous song in the collection, or null if the
     *         collection is empty.
     */
    @Override
    public Song playerChooseNextSong(boolean isPrevious, Song currentSong) {
        if (!getCurrentSongList().getValue().equals("Queue")) {
            setLastPlaylist(getCurrentSongList().getValue(), currentSong);
        }

        if (getSelectedCollection().equals("Queue")) {
            mainControllerAccess.updateCurrentCollection();
        }
        return collectionService.getNextOrPreviousSong(
                isPrevious,
                currentSong,
                currentSongList,
                lastPlaylist,
                this::getPlaylistByTitle,
                this::getQueue);
    }
}

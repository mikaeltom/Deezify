package ulb.controllers.options;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ulb.exceptions.BannedWordException;
import ulb.exceptions.songs.SQLExceptionHandler;
import ulb.models.Playlist;
import ulb.models.Song;
import ulb.services.BannedWordService;
import ulb.services.options.PlaylistOptionsService;
import ulb.services.collections.PlaylistService;
import ulb.utils.I18n;
import ulb.utils.PopupType;
import ulb.views.PopupView;
import ulb.views.options.PlaylistOptionsView;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Handles creating, deleting, and updating playlists.
 * This class is a singleton, meaning only one instance of this class can exist.
 * It provides methods to create a new playlist, delete a playlist, and update a
 * playlist.
 */
public class PlaylistController implements PlaylistOptionsView.PlaylistViewListener {
    private OptionListener listener;
    private Song currentSong;
    private final PlaylistOptionsService playlistOptionsService;

    public PlaylistController(BannedWordService bannedWordManager) {
        playlistOptionsService = new PlaylistOptionsService(new PlaylistService(), bannedWordManager);
    }

    public void setListener(OptionListener listener) {
        this.listener = listener;
    }

    public void setCurrentSong(Song song) {
        this.currentSong = song;
    }

    private Playlist getPlaylist(String playlistName){
        try {
            return playlistOptionsService.getPlaylist(playlistName);
        } catch (SQLExceptionHandler e) {
            new PopupView("error_retrieving_playlist", e.getMessage(), PopupType.ERROR);
            return null;
        }
    }

    public void addSongToPlaylist(Song song, Playlist playlist) throws SQLExceptionHandler, IOException {
        playlistOptionsService.addSongToPlaylist(song, playlist);
    }

    public void removeSongFromPlaylist(Song song, Playlist playlist) throws SQLExceptionHandler {
        playlistOptionsService.removeSongFromPlaylist(song, playlist);
    }

    /**
     * Checks if a playlist already exists in the database.
     * This method performs a case-insensitive search.
     *
     * @param playlistName The name of the playlist to search for.
     * @return true if the playlist exists in the database, false otherwise.
     * throws SQLExceptionHandler if a database access error occurs.
     */
    @Override
    public boolean isInDatabase(String playlistName) {
        try {
            return playlistOptionsService.isInDatabase(playlistName);
        } catch (SQLExceptionHandler | IOException e) {
            new PopupView("error_checking_playlist", e.getMessage(), PopupType.ERROR);
            return false;
        }
    }

    /**
     * Retrieves all playlists for a user (only ID and Name).
     *
     * @return A list of Playlist objects containing only ID and Name.
     * throws SQLExceptionHandler if a database access error occurs.
     */
    @Override
    public List<String> getPlaylists() {
        try {
            return playlistOptionsService.getPlaylists();
        } catch (SQLExceptionHandler | IOException e) {
            new PopupView("error_retrieving_playlist", e.getMessage(), PopupType.ERROR);
            return new ArrayList<>();
        }
    }

    /**
     * Checks if a song is in a specified playlist.
     *
     * @param playlistName The name of the playlist to search in.
     * @return true if the song is in the playlist, false otherwise.
     */
    @Override
    public boolean isInPlaylist(String playlistName) {
        try {
            return playlistOptionsService.isInPlaylist(playlistName, currentSong);
        } catch (SQLExceptionHandler e) {
            new PopupView("error_checking_playlist", e.getMessage(), PopupType.ERROR);
        }
        return false;
    }

    /**
     * Displays the playlist view with the given song and album information.
     * Sets the current song and initializes the playlist view controller with the
     * given song and album information.
     *
     * @param song   The Song object to be displayed in the playlist view.
     * @param stage  The Stage object to be used for displaying the playlist view.
     */
    public void show(Song song, Stage stage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ulb/views/PlaylistView.fxml"), I18n.getBundle());
            Parent playlistRoot = loader.load();
            PlaylistOptionsView view = loader.getController();
            view.setListener(this);
            setCurrentSong(song);
            view.loadCustomPlaylist();
            stage.setScene(new Scene(playlistRoot));
        } catch (IOException e) {
            new PopupView("loading_error_title", I18n.get("error_loading_playlist_view"), PopupType.ERROR);
        }
    }

    /**
     * Adds a song to a specified playlist. If the playlist does not exist in the
     * database, it will be created.
     * Checks if the playlist is valid and does not already contain the song before
     * adding. If the playlist is new, it sets its position to 1.
     * Also updates the current collection when the operation is successful.
     *
     * @param playlistName The name of the playlist to add the song to.
     */
    @Override
    public void addToPlaylist(String playlistName) {
        try {
            playlistOptionsService.validatePlaylistName(playlistName);
            if (playlistName != null) {
                if (isInDatabase(playlistName)) {
                    addSongToPlaylist(currentSong, Objects.requireNonNull(getPlaylist(playlistName)));
                } else {
                    playlistOptionsService.insertPlaylist(playlistName);
                    addSongToPlaylist(currentSong, Objects.requireNonNull(getPlaylist(playlistName)));
                    listener.loadCollections();
                }
                listener.updateCurrentCollection();
            }
        } catch (SQLExceptionHandler | BannedWordException | IOException e) {
            new PopupView("error_adding_song_playlist", e.getMessage(), PopupType.ERROR);
        }
    }

    /**
     * Removes the specified song from the given playlist if it exists in the
     * database.
     * <p>
     * This method first checks if the playlist exists in the database. If it does,
     * the song is removed from the playlist using the appropriate method.
     * After removing the song, it updates the current collection to reflect
     * the changes.
     * </p>
     *
     * @param playlistName The name of the playlist from which to remove the song.
     * @throws SQLExceptionHandler If a database access error occurs during the
     *                             operation.
     */
    private void removeSong(String playlistName) throws SQLExceptionHandler {
        if (isInDatabase(playlistName)) {
            playlistOptionsService.removeSongFromPlaylist(currentSong, Objects.requireNonNull(getPlaylist(playlistName)));
            listener.updateCurrentCollection();
        }
    }

    /**
     * Handles the selection of a playlist button.
     * <p>
     * If the button is selected, it adds the current song to the playlist.
     * If the button is deselected, it removes the current song from the playlist.
     * </p>
     *
     * @param playlistButton Whether the button is selected.
     * @param playlistName   The name of the playlist.
     */
    @Override
    public void handlePlaylistSelection(boolean playlistButton, String playlistName) {
        try {
            if (playlistButton) {
                addToPlaylist(playlistName);
            } else {
                removeSong(playlistName);
            }
        } catch (SQLExceptionHandler e) {
            new PopupView("error_handling_playlist_click", e.getMessage(), PopupType.ERROR);
        }
    }

    /**
     * Closes the current playlist view and returns to the song options view.
     * <p>
     * This method invokes the listener to return to the song option view with
     * the current song. It then clears the current song to ensure that no
     * song is set in the context of this controller.
     * </p>
     */
    @Override
    public void close() {
        listener.returnToSongOptionView(currentSong);
        setCurrentSong(null);
    }
}

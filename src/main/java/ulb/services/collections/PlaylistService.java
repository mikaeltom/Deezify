package ulb.services.collections;

import ulb.exceptions.songs.SQLExceptionHandler;
import ulb.models.Playlist;
import ulb.models.Song;
import ulb.services.SQLService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Service class for managing playlists.
 * Provides methods to retrieve, create, update, and delete playlists for a
 * user.
 * It interacts with the SQLService to perform database operations related to
 * playlists.
 * This class also handles operations such as adding or removing songs from
 * playlists.
 */
public class PlaylistService {
    private final SQLService sqlService = SQLService.getInstance();

    /**
     * Retrieves all playlists for a user, including their songs.
     * This method retrieves all playlists for a user from the database,
     * and then adds all songs for each playlist to the local Playlist
     * objects. It then returns the list of Playlist objects.
     * The Favorites playlist with ID 1 is skipped.
     *
     * @return A list of Playlist objects containing the playlists and their songs.
     * @throws SQLExceptionHandler If an error occurs while retrieving playlists or
     *                             songs from the database.
     */
    public List<Playlist> getPlaylistsForUser() throws SQLExceptionHandler, IOException {
        List<Playlist> playlists = new ArrayList<>();
        for (Playlist playlist : sqlService.getAllPlaylistsFromUser()) {
            for (Song song : sqlService.getPlaylistSongs(playlist.getId())) {
                playlist.addSong(song);
            }
            playlists.add(playlist);
        }
        return playlists;
    }

    /**
     * Retrieves a playlist by title.
     * <p>
     * Returns a Playlist object containing the playlist's ID, name, songs, and
     * tags if the playlist exists in the database. If the playlist does not
     * exist, it displays an error popup.
     * </p>
     *
     * @param title The title of the playlist to retrieve.
     * @return A Playlist object containing the playlist's ID, name, songs, and
     * tags, or null if an error occurs.
     * @throws SQLExceptionHandler If a database access error occurs.
     */
    public Playlist getPlaylistFromName(String title) throws SQLExceptionHandler {
        int playlistID = sqlService.getPlaylistId(title);
        return sqlService.getPlaylist(playlistID);
    }

    /**
     * Deletes a playlist from the database.
     * <p>
     * Retrieves the ID of the playlist by name, removes it from the database,
     * and updates the library.
     * If an error occurs, it throws an IOException and displays an error popup.
     * </p>
     *
     * @param name The name of the playlist to delete.
     */
    public void deletePlaylist(String name) throws SQLExceptionHandler {
        int playlistID = sqlService.getPlaylistId(name);
        sqlService.removePlaylist(playlistID);
    }

    public List<Song> getPlaylistSongs(int playlistId) throws SQLExceptionHandler {
        return sqlService.getPlaylistSongs(playlistId);
    }

    public void updateSongPositionInPlaylist(int playlistID, int songId, int newPosition) throws SQLExceptionHandler {
        sqlService.updateSongPositionInPlaylist(playlistID, songId, newPosition);
    }

    public int getSongPosition(int playlistId, int songId) throws SQLExceptionHandler {
        return sqlService.getSongPosition(playlistId, songId);
    }

    public void addNewPlaylist(String name) throws SQLExceptionHandler {
        sqlService.addNewPlaylist(name);
    }

    public void addSongToPlaylist(int playlistId, int songId) throws SQLExceptionHandler {
        sqlService.addSongToPlaylist(playlistId, songId);
    }

    public void removeSongFromPlaylist(int playlistId, int songId) throws SQLExceptionHandler {
        sqlService.removeSongFromPlaylist(playlistId, songId);
    }
}

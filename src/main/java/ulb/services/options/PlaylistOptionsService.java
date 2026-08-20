package ulb.services.options;

import ulb.exceptions.BannedWordException;
import ulb.exceptions.songs.SQLExceptionHandler;
import ulb.models.Playlist;
import ulb.models.Song;
import ulb.services.BannedWordService;
import ulb.services.collections.PlaylistService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles playlist options such as creating, deleting, and updating playlists.
 * This class provides methods to manage playlists and their associated songs.
 * It was created to make PlaylistController more readable and to separate the
 * concerns of the controller and the service.
 */
public class PlaylistOptionsService {
    private final PlaylistService playlistService;
    private final BannedWordService bannedWordService;

    public PlaylistOptionsService(PlaylistService playlistService, BannedWordService bannedWordService) {
        this.playlistService = playlistService;
        this.bannedWordService = bannedWordService;
    }

    /**
     * Checks if a playlist with the given name already exists in the database.
     * @param playlistName the name of the playlist to check
     * @return true if the playlist exists, false otherwise
     * @throws SQLExceptionHandler if an error occurs while retrieving the playlists
     * @throws IOException if an error occurs while updating the database
     */
    public boolean isInDatabase(String playlistName) throws SQLExceptionHandler, IOException {
        List<String> playlists = getPlaylists();
        for (String playlist : playlists) {
            if (playlist.equalsIgnoreCase(playlistName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Inserts a new playlist into the database.
     * @param name the name of the playlist to insert
     * @throws SQLExceptionHandler if an error occurs while inserting the playlist
     * @throws IOException if an error occurs while updating the database
     */
    public void insertPlaylist(String name) throws SQLExceptionHandler, IOException {
        if (!isInDatabase(name)) {
            playlistService.addNewPlaylist(name);
        }
    }

    /**
     * Retrieves all playlists for the current user.
     * @return a list of playlist names
     * @throws SQLExceptionHandler if an error occurs while retrieving the playlists
     * @throws IOException if an error occurs while updating the database
     */
    public List<String> getPlaylists() throws SQLExceptionHandler, IOException {
        ArrayList<String> playlists = new ArrayList<>();
        playlistService.getPlaylistsForUser().forEach(playlist -> playlists.add(playlist.getName()));
        return playlists;
    }

    /**
     * Checks if a song is already in a playlist.
     * @param playlistName the name of the playlist to check
     * @param currentSong the song to check
     * @return true if the song is in the playlist, false otherwise
     * @throws SQLExceptionHandler if an error occurs while retrieving the playlist
     */
    public boolean isInPlaylist(String playlistName, Song currentSong) throws SQLExceptionHandler {
        Playlist playlist = getPlaylist(playlistName);
        return playlist.getSongs().stream()
                .anyMatch(song -> song.getId() == currentSong.getId());
    }

    /**
     * Retrieves a playlist by its name.
     * @param playlistName the name of the playlist to retrieve
     * @return the playlist object
     * @throws SQLExceptionHandler if an error occurs while retrieving the playlist
     */
    public Playlist getPlaylist(String playlistName) throws SQLExceptionHandler {
        return playlistService.getPlaylistFromName(playlistName);
    }

    /**
     * Adds a song to a playlist.
     * @param song the song to add
     * @param playlist the playlist to add the song to
     * @throws SQLExceptionHandler if an error occurs while adding the song
     * @throws IOException if an error occurs while updating the database
     */
    public void addSongToPlaylist(Song song, Playlist playlist) throws SQLExceptionHandler, IOException {
        playlistService.addSongToPlaylist(playlist.getId(), song.getId());
        playlist.addSong(song);
    }

    /**
     * Removes a song from a playlist.
     * @param song the song to remove
     * @param playlist the playlist to remove the song from
     * @throws SQLExceptionHandler if an error occurs while removing the song
     */
    public void removeSongFromPlaylist(Song song, Playlist playlist) throws SQLExceptionHandler {
        playlistService.removeSongFromPlaylist(playlist.getId(), song.getId());
        playlist.removeSong(song);
    }

    /**
     * Validates the playlist name against banned words.
     * @param name the name of the playlist to validate
     * @throws BannedWordException if the name contains banned words
     */
    public void validatePlaylistName(String name) throws BannedWordException {
        bannedWordService.containsBannedWords(name);
    }
}

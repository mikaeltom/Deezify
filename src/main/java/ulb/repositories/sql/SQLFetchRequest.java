package ulb.repositories.sql;

import javafx.util.Duration;
import ulb.exceptions.songs.SQLExceptionHandler;
import ulb.models.Playlist;
import ulb.models.Song;
import ulb.models.Tag;
import ulb.models.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;


/**
 * SQLFetchRequest class handles SQL queries to fetch data from the database.
 * It provides methods to retrieve songs, playlists, tags, and user information.
 */
public class SQLFetchRequest {
    private final SQLHandler sqlHandler;

    public SQLFetchRequest(SQLHandler sqlHandler) {
        this.sqlHandler = sqlHandler;
    }

    /**
     * Maps a row from the ResultSet to a SongDTO object.
     *
     * @param rs The ResultSet containing song data.
     * @return A SongDTO object populated with data from the current row of the
     * ResultSet.
     * @throws SQLExceptionHandler If an error occurs during data retrieval.
     * @throws SQLException        If an SQL error occurs.
     */
    private Song mapResultSetToSong(ResultSet rs) throws SQLExceptionHandler, SQLException {
        return new Song(
                rs.getInt("SongID"),
                rs.getString("Name"),
                rs.getString("Artist"),
                rs.getString("Album"),
                new Duration(rs.getInt("TimeLength")),
                rs.getString("SongPath"),
                this.getSongTags(rs.getInt("SongID")),
                rs.getString("ImagePath"),
                rs.getString("VideoPath"),
                rs.getString("Lyrics"));
    }

    /**
     * Retrieves song details.
     *
     * @param userID The user ID.
     * @param songID The song ID.
     * @return A Song object.
     * @throws SQLExceptionHandler If an error occurs during data retrieval.
     */
    public Song getSong(int userID, int songID) throws SQLExceptionHandler {
        return sqlHandler.fetchSingleRow("get/get_song.sql", this::mapResultSetToSong, songID, userID);
    }

    /**
     * Retrieves all custom tags (non-predefined) associated with a user.
     *
     * @param userID The user ID.
     * @return A list of custom tags associated with the user.
     * @throws SQLExceptionHandler If an error occurs during data retrieval.
     */
    public List<Tag> getCustomTags(int userID) throws SQLExceptionHandler {
        return sqlHandler.fetchMultipleRows("get/get_customTags.sql", rs -> new Tag(rs.getString("Name")), userID);
    }

    /**
     * Retrieves tags associated with a song.
     *
     * @param songID The song ID.
     * @return A list of tags.
     */
    public List<Tag> getSongTags(int songID) throws SQLExceptionHandler {
        return sqlHandler.fetchMultipleRows("get/get_tags_from_song.sql", rs -> new Tag(rs.getString("Name")), songID);
    }

    /**
     * Retrieves tags associated with a playlist.
     *
     * @param playlistID The playlist ID.
     * @return A list of tags.
     */
    public List<Tag> getPlaylistTags(int playlistID) throws SQLExceptionHandler {
        return sqlHandler.fetchMultipleRows("get/get_tags_from_playlist.sql", rs -> new Tag(rs.getString("Name")),
                playlistID);
    }

    /**
     * Retrieves all songs from a specific user.
     *
     * @param userID The user ID.
     * @return A list of Song objects.
     */
    public List<Song> getAllSongsFromUser(int userID) throws SQLExceptionHandler {
        return sqlHandler.fetchMultipleRows("get/get_all_songs_from_user.sql", this::mapResultSetToSong,
                userID);
    }

    /**
     * Retrieves playlist details.
     *
     * @param userID     The user ID.
     * @param playlistID The playlist ID.
     * @return A Playlist object.
     */
    public Playlist getPlaylist(int userID, int playlistID) throws SQLExceptionHandler {
        return sqlHandler.fetchSingleRow("get/get_playlist.sql", rs -> new Playlist(
                rs.getInt("PlaylistID"),
                rs.getString("Name"),
                getPlaylistSongs(playlistID), 0), playlistID, userID);
    }

    /**
     * Retrieves all songs in a playlist.
     *
     * @param playlistID The playlist ID.
     * @return A list of Song objects.
     */
    public List<Song> getPlaylistSongs(int playlistID) throws SQLExceptionHandler {
        return sqlHandler.fetchMultipleRows("get/get_all_songs_from_playlist.sql",
                this::mapResultSetToSong,
                playlistID);
    }

    /**
     * Retrieves all playlists for a user (only ID and Name).
     *
     * @param userID The user ID.
     * @return A list of Playlist objects containing only ID and Name.
     */
    public List<Playlist> getAllPlaylistsFromUser(int userID) throws SQLExceptionHandler {
        return sqlHandler.fetchMultipleRows("get/get_all_playlists_from_user.sql", rs -> new Playlist(
                rs.getInt("PlaylistID"),
                rs.getString("Name")), userID);
    }

    /**
     * Searches for songs by name, album, or artist.
     *
     * @param query The search query.
     * @return A list of matching Song objects.
     * @throws SQLExceptionHandler If an error occurs during data retrieval.
     */
    public List<Song> searchSongs(String query) throws SQLExceptionHandler {
        String wildcardQuery = "%" + query + "%";
        return sqlHandler.fetchMultipleRows("search.sql", this::mapResultSetToSong,
                wildcardQuery, wildcardQuery, wildcardQuery);
    }

    /**
     * Retrieves the ID of a tag by its name.
     */
    public int getTagId(String name, int userID) throws SQLExceptionHandler {
        return sqlHandler.fetchSingleRow("get/get_tag_id.sql", rs -> rs.getInt("TagID"), name, userID);
    }

    /**
     * Retrieves the ID of a song by its name.
     */
    public int getSongId(String name, int userID) throws SQLExceptionHandler {
        return sqlHandler.fetchSingleRow("get/get_song_id.sql", rs -> rs.getInt("SongID"), name, userID);
    }

    /**
     * Retrieves the ID of a playlist by its name.
     */
    public int getPlaylistId(String name, int userID) throws SQLExceptionHandler {
        return sqlHandler.fetchSingleRow("get/get_playlist_id.sql", rs -> rs.getInt("PlaylistID"), name, userID);
    }

    /**
     * Retrieves the position of a specific song within a playlist.
     *
     * @param playlistID The ID of the playlist containing the song.
     * @param songID     The ID of the song whose position is to be retrieved.
     * @return The position of the song within the playlist.
     * @throws SQLExceptionHandler If an error occurs during the database access.
     */
    public int getSongPosition(int playlistID, int songID) throws SQLExceptionHandler {
        return sqlHandler.fetchSingleRow("get/get_song_position_in_playlist.sql", rs -> rs.getInt("Position"),
                playlistID, songID);
    }

    /**
     * Give all the user that have the stay logged option enabled
     */
    public List<User> getStayLoggedUsers() throws SQLExceptionHandler {
        return sqlHandler.fetchMultipleRows("get/get_stay_logged_users.sql", rs -> new User(
                rs.getInt("UserID"),
                rs.getString("Username"),
                rs.getString("Langue"),
                rs.getInt("StayLogged") == 1,
                rs.getString("ProfileImagePath")));
    }

    /**
     * Retrieves the ID of a user by their username.
     *
     * @param username The username to search for.
     * @return The corresponding user ID.
     * @throws SQLExceptionHandler If an error occurs during the database access.
     */
    public Integer getUserId(String username) throws SQLExceptionHandler {
        return sqlHandler.fetchSingleRow("get/get_user_id_from_name.sql", rs -> rs.getInt("UserID"), username);
    }

    /**
     * Retrieves the ID of a user by their username and password.
     *
     * @param username The username to search for.
     * @param password The password to search for.
     * @return The corresponding user ID.
     * @throws SQLExceptionHandler If an error occurs during the database access.
     */
    public Integer getUserIdFromLogin(String username, String password) throws SQLExceptionHandler {
        return sqlHandler.fetchSingleRow("get/get_user_id_from_login.sql", rs -> rs.getInt("UserID"), username,
                password);
    }

    /**
     * Retrieves a user by their ID.
     *
     * @param userId The ID of the user to retrieve.
     * @return A User object containing the user's details.
     * @throws SQLExceptionHandler If an error occurs during the database access.
     */
    public User getUserById(int userId) throws SQLExceptionHandler {
        return sqlHandler.fetchSingleRow("get/get_user.sql",
                rs -> new User(rs.getInt("UserID"),
                        rs.getString("Username"),
                        rs.getString("Langue"),
                        rs.getInt("StayLogged") == 1,
                        rs.getString("ProfileImagePath")),
                userId);
    }

    /**
     * Retrieves the number of occurrences of a tag in all songs and playlists of
     * all users.
     *
     * @param tagID The ID of the tag for which to count the occurrences.
     * @return The number of times the tag is used.
     * @throws SQLExceptionHandler If an error occurs during the database access.
     */
    public int getTagUses(int tagID) throws SQLExceptionHandler {
        return sqlHandler.fetchSingleRow("get/get_tag_uses.sql", rs -> rs.getInt("total_occurrences"), tagID);
    }
}
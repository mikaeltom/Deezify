package ulb.services;

import ulb.exceptions.credentials.InvalidUsernameException;
import ulb.exceptions.songs.SQLExceptionHandler;
import ulb.models.Playlist;
import ulb.models.Song;
import ulb.models.Tag;
import ulb.models.User;
import ulb.repositories.sql.SQLFetchRequest;
import ulb.repositories.sql.SQLHandler;
import ulb.repositories.sql.SQLUpdateRequest;

import java.util.List;

/**
 * Provides methods to interact with the SQL database.
 * <p>
 * This class provides methods to add new users, songs, playlists, and tags to
 * the
 * database, as well as to retrieve songs, playlists, and tags. It also provides
 * methods to update the metadata of songs and to add songs to playlists.
 * <p>
 * All database operations are handled by the {@link SQLHandler} class, which
 * provides a layer of abstraction between the service layer and the database.
 * The service layer only interacts with the database through the
 * {@link SQLHandler} class.
 * <p>
 * Exceptions that may be thrown include {@link SQLExceptionHandler}, which is
 * thrown if an error occurs while accessing the database.
 */
public class SQLService {
    private static SQLService instance;
    private final SQLFetchRequest fetch;
    private final SQLUpdateRequest update;
    private User user;

    private SQLService() {
        SQLHandler sqlHandler = new SQLHandler();
        this.fetch = new SQLFetchRequest(sqlHandler);
        this.update = new SQLUpdateRequest(sqlHandler);
    }

    /**
     * Retrieves the single instance of the {@link SQLService} class.
     * <p>
     * This method is thread-safe and ensures that only one instance of the
     * {@link SQLService} class is created. If the instance has not been created
     * yet, it is created when this method is first called.
     * <p>
     * The instance is stored in a static field and is never changed after it
     * has been created. This ensures that the same instance is always returned
     * when this method is called.
     * <p>
     * This method is useful when you want to ensure that only one instance of
     * the {@link SQLService} class is created, and you want to be able to access
     * this instance from any part of your code.
     *
     * @return The single instance of the {@link SQLService} class.
     */
    public static synchronized SQLService getInstance() {
        if (instance == null) {
            instance = new SQLService();
        }
        return instance;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setUserById(int id) throws SQLExceptionHandler {
        this.user = this.getUserByID(id);
    }

    public int getUserId() {
        return this.user.getId();
    }

    /**
     * Inserts a new user in the database.
     * <p>
     * The user is inserted with the given username and language preference.
     * <p>
     * Exceptions:
     * - SQLExceptionHandler: If an error occurs while accessing the database.
     *
     * @param username The desired username for the new user.
     * @param language The language preference of the new user.
     */
    public void addNewUser(String username, String language, String password, String profileImagePath)
            throws SQLExceptionHandler {
        update.addNewUser(username, language, password, profileImagePath);
    }

    /**
     * Inserts a new song in the database.
     * <p>
     * The song is inserted with the given metadata.
     * <p>
     * Exceptions:
     * - SQLExceptionHandler: If an error occurs while accessing the database.
     *
     * @param songPath   The path to the song file.
     * @param name       The name of the song.
     * @param timeLength The length of the song in seconds.
     * @param audioType  The type of audio file.
     * @param artist     The artist of the song.
     * @param album      The album of the song.
     * @param lyrics     The lyrics of the song.
     * @param imagePath  The path to the cover image of the song.
     * @param videoPath  The path to the video of the song.
     */
    public void addNewSong(String songPath, String name, int timeLength,
            String audioType, String artist, String album,
            String lyrics, String imagePath, String videoPath) throws SQLExceptionHandler {
        update.addNewSong(this.user.getId(), songPath, name, timeLength, audioType, artist, album, lyrics, imagePath,
                videoPath);
    }

    /**
     * Inserts a new playlist into the database for a specified user.
     */
    public void addNewPlaylist(String name) throws SQLExceptionHandler {
        update.addNewPlaylist(this.user.getId(), name);
    }

    /**
     * Adds a song to a specified playlist.
     */
    public void addSongToPlaylist(int playlistID, int songID) throws SQLExceptionHandler {
        update.addSongToPlaylist(this.user.getId(), playlistID, songID);
    }

    /**
     * Inserts a new tag into the database for a specified user.
     */
    public void addNewTag(String tagName, boolean isPredefined) throws SQLExceptionHandler {
        update.addNewTag(this.user.getId(), tagName, isPredefined);
    }

    /**
     * Adds a tag to a specified song.
     */
    public void addTagToSong(int songID, int tagID) throws SQLExceptionHandler {
        update.addTagToSong(this.user.getId(), songID, tagID);
    }

    /**
     * Adds a tag to a specified playlist.
     */
    public void addTagToPlaylist(int playlistID, int tagID) throws SQLExceptionHandler {
        update.addTagToPlaylist(this.user.getId(), playlistID, tagID);
    }

    /**
     * Adds lyrics to a song in the database.
     */
    public void addLyricsToSong(int songID, String lyricsPath) throws SQLExceptionHandler {
        update.addLyricsToSong(this.user.getId(), songID, lyricsPath);
    }

    /**
     * Adds an image to a song in the database.
     */
    public void addImageToSong(int songID, String imagePath) throws SQLExceptionHandler {
        update.addImageToSong(this.user.getId(), songID, imagePath);
    }

    /**
     * Removes a song from the database for a specified user.
     */
    public void removeSong(int songID) throws SQLExceptionHandler {
        update.removeSong(this.user.getId(), songID);
    }

    /**
     * Removes a playlist from the database for a specified user.
     */
    public void removePlaylist(int playlistID) throws SQLExceptionHandler {
        update.removePlaylist(this.user.getId(), playlistID);
    }

    /**
     * Removes a song from a specified playlist.
     */
    public void removeSongFromPlaylist(int playlistID, int songID) throws SQLExceptionHandler {
        update.removeSongFromPlaylist(this.user.getId(), playlistID, songID);
    }

    /**
     * Removes a tag from the database for a specified user.
     */
    public void removeTag(int tagID) throws SQLExceptionHandler {
        update.removeTag(this.user.getId(), tagID);
    }

    /**
     * Removes a tag from a specified song.
     */
    public void removeTagFromSong(int songID, int tagID) throws SQLExceptionHandler {
        update.removeTagFromSong(this.user.getId(), songID, tagID);
    }

    /**
     * Removes a tag from a specified playlist.
     */
    public void removeTagFromPlaylist(int playlistID, int tagID) throws SQLExceptionHandler {
        update.removeTagFromPlaylist(this.user.getId(), playlistID, tagID);
    }

    /**
     * Updates the details of a song in the database.
     */
    public void updateSongDetails(int songID, String newName, String newImagePath,
            String newVideoPath, String newArtist, String newAlbum) throws SQLExceptionHandler {
        update.updateSongDetails(this.user.getId(), songID, newName, newImagePath, newVideoPath, newArtist, newAlbum);
    }

    /**
     * Retrieves a song from the database for a specified user.
     */
    public Song getSong(int songID) throws SQLExceptionHandler {
        return fetch.getSong(this.user.getId(), songID);
    }

    /**
     * Retrieves all custom tags (non-predefined) associated with a user.
     */
    public List<Tag> getCustomTags() throws SQLExceptionHandler {
        return fetch.getCustomTags(this.user.getId());
    }

    /**
     * Retrieves tags associated with a specific playlist.
     */
    public List<Tag> getPlaylistTags(int playlistID) throws SQLExceptionHandler {
        return fetch.getPlaylistTags(playlistID);
    }

    /**
     * Retrieves all songs from a specific user.
     */
    public List<Song> getAllSongsFromUser() throws SQLExceptionHandler {
        return fetch.getAllSongsFromUser(this.user.getId());
    }

    /**
     * Retrieves playlist details.
     */
    public Playlist getPlaylist(int playlistID) throws SQLExceptionHandler {
        return fetch.getPlaylist(this.user.getId(), playlistID);
    }

    /**
     * Retrieves all songs from a specified playlist.
     */

    public List<Song> getPlaylistSongs(int playlistID) throws SQLExceptionHandler {
        return fetch.getPlaylistSongs(playlistID);
    }

    /**
     * Retrieves all playlists for a user (only ID and Name).
     */
    public List<Playlist> getAllPlaylistsFromUser() throws SQLExceptionHandler {
        return fetch.getAllPlaylistsFromUser(this.user.getId());
    }

    /**
     * Searches for songs matching the given query in the database. If no songs
     * match the query,
     * an empty list is returned. Otherwise, the list of songs is returned.
     */
    public List<Song> searchSongs(String query) throws SQLExceptionHandler {
        String wildcardQuery = "%" + query + "%";
        return fetch.searchSongs(wildcardQuery);
    }

    /**
     * Retrieves the ID of a tag by its name.
     */
    public int getTagId(String name) throws SQLExceptionHandler {
        return fetch.getTagId(name, this.user.getId());
    }

    /**
     * Retrieves the ID of a song by its name.
     */
    public int getSongId(String name) throws SQLExceptionHandler {
        return fetch.getSongId(name, this.user.getId());
    }

    /**
     * Retrieves the ID of a playlist by its name.
     */
    public int getPlaylistId(String name) throws SQLExceptionHandler {
        return fetch.getPlaylistId(name, this.user.getId());
    }

    /**
     * Retrieves the position of a song within a specified playlist.
     */
    public int getSongPosition(int playlistID, int songID) throws SQLExceptionHandler {
        return fetch.getSongPosition(playlistID, songID);
    }

    public User getUserByID(int userId) throws SQLExceptionHandler {
        return fetch.getUserById(userId);
    }

    public int getTagUses(int tagID) throws SQLExceptionHandler {
        return fetch.getTagUses(tagID);
    }

    /**
     * Clears all data from the database.
     */
    public void clearDatabase() throws SQLExceptionHandler {
        update.clearDatabase();
    }

    /**
     * Updates the position of a song in a playlist.
     */
    public void updateSongPositionInPlaylist(int playlistID, int songID, int newPosition) throws SQLExceptionHandler {
        update.updateSongPositionInPlaylist(this.user.getId(), playlistID, songID, newPosition);
    }

    /**
     * Retrieves the users that activated the stay logged option.
     *
     * @return A list of users that have the stay logged option enabled.
     * @throws SQLExceptionHandler if an error occurs while retrieving the users.
     */
    public List<User> getAllStayLoggedUsers() throws SQLExceptionHandler {
        return fetch.getStayLoggedUsers();
    }

    /**
     * Retrieves the ID of a user by their username.
     *
     * @param username The username of the user.
     * @return The ID of the user.
     * @throws SQLExceptionHandler if an error occurs while retrieving the user ID.
     */
    public Integer getUserId(String username) throws SQLExceptionHandler {
        return fetch.getUserId(username);
    }

    /**
     * Retrieves the ID of a user by their username and password.
     *
     * @param username The username of the user.
     * @param password The password of the user.
     * @return The ID of the user.
     * @throws SQLExceptionHandler if an error occurs while retrieving the user ID.
     */
    public Integer getUserIdFromLogin(String username, String password) throws SQLExceptionHandler {
        return fetch.getUserIdFromLogin(username, password);
    }

    /**
     * Updates the stay logged status of a user in the database.
     *
     * @param userId     The ID of the user.
     * @param stayLogged The stay logged status to set.
     * @throws SQLExceptionHandler if an error occurs while updating the status.
     */
    public void updateStayLogged(int userId, boolean stayLogged) throws SQLExceptionHandler {
        update.updateUserStayLogged(userId, stayLogged);
    }

    /**
     * Removes a user from the database.
     *
     * @throws SQLExceptionHandler if an error occurs while removing the user.
     */
    public void removeUser() throws SQLExceptionHandler {
        update.removeUser(this.user.getId());
    }

    /**
     * Updates the profile image path of the user in the database.
     *
     * @param profileImagePath The new profile image path.
     * @throws SQLExceptionHandler if an error occurs while updating the path.
     */
    public void updateUserProfileImagePath(String profileImagePath) throws SQLExceptionHandler {
        update.updateUserProfileImagePath(user.getId(), profileImagePath);
        user.setProfileImagePath(profileImagePath);
    }

    /**
     * Updates the username of the user in the database.
     *
     * @param username The new username.
     * @throws InvalidUsernameException If the provided username is invalid.
     * @throws SQLExceptionHandler      If an error occurs while updating the
     *                                  username.
     */
    public void updateUserUsername(String username) throws InvalidUsernameException, SQLExceptionHandler {
        update.updateUserUsername(user.getId(), username);
        user.setUsername(username);
    }

    /**
     * Updates the password of the user in the database.
     *
     * @param password The new password.
     * @throws SQLExceptionHandler If an error occurs while updating the password.
     */
    public void updateUserPassword(String password) throws SQLExceptionHandler {
        update.updateUserPassword(user.getId(), password);
    }

    /**
     * Retrieves the language of the user.
     *
     * @return The language of the user as String : 'en', 'fr', 'nl'.
     */
    public String getLanguage() {
        return user.getLanguage();
    }

    /**
     * Updates the language of the user in the database and also the user instance.
     *
     * @param language The new language to be set.
     * @throws SQLExceptionHandler If an error occurs while updating the language.
     */
    public void setLanguage(String language) throws SQLExceptionHandler {
        if (this.user != null) {
            this.updateLanguage(language);
        }
    }

    /**
     * Updates only the language of the user in the database.
     *
     * @param language The new language to be set.
     * @throws SQLExceptionHandler If an error occurs while updating the language.
     */
    private void updateLanguage(String language) throws SQLExceptionHandler {
        update.updateLanguage(this.user.getId(), language);
    }
}

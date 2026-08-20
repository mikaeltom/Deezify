package ulb.repositories.sql;

import ulb.exceptions.songs.SQLExceptionHandler;

public class SQLUpdateRequest {

    /**
     * Methods for inserting, updating and deleting data into/from the database.
     * - `userID` refers to the owner of the data (User, Playlist, Song, etc.).
     * - `name` is the main identifier for Playlists, Songs, and Tags.
     * - Other parameters provide metadata (e.g., file paths, durations, artist
     * info).
     * throws SQLExceptionHandler : thrown if an error occurred
     */

    private final SQLHandler sqlHandler;

    public SQLUpdateRequest(SQLHandler sqlHandler) {
        this.sqlHandler = sqlHandler;
    }

    /**
     * Inserts a new user in the database.
     *
     * @param username The desired username for the new user.
     * @param language The language preference of the new user.
     * @throws SQLExceptionHandler : thrown if an error occurred
     */
    public void addNewUser(String username, String language, String password, String profileImagePath)
            throws SQLExceptionHandler {
        sqlHandler.executeUpdate("insert/insert_user.sql", username, language, password, profileImagePath);
    }

    /**
     * Inserts a new song into the database.
     *
     * @param userID     The ID of the user who owns the song.
     * @param songPath   The path to the song file.
     * @param name       The name of the song.
     * @param timeLength The length of the song in seconds.
     * @param audioType  The type of audio file.
     * @param artist     The artist of the song.
     * @param album      The album of the song.
     * @param lyrics     The lyrics of the song.
     * @param imagePath  The path to the cover image of the song.
     * @param videoPath  The path to the video of the song.
     * @throws SQLExceptionHandler : thrown if an error occurred
     */
    public void addNewSong(int userID, String songPath, String name, int timeLength,
                           String audioType, String artist, String album,
                           String lyrics, String imagePath, String videoPath) throws SQLExceptionHandler {
        sqlHandler.executeUpdate("insert/insert_song.sql", userID, songPath, name, timeLength, audioType, artist, album,
                lyrics,
                imagePath,
                videoPath,
                userID,
                name,
                artist,
                album);
    }

    /**
     * Inserts a new playlist into the database for a specified user.
     *
     * @param userID The ID of the user who owns the playlist.
     * @param name   The name of the new playlist.
     * @throws SQLExceptionHandler If an error occurs during the database update.
     */
    public void addNewPlaylist(int userID, String name) throws SQLExceptionHandler {
        sqlHandler.executeUpdate("insert/insert_playlist.sql", userID, name);
    }

    /**
     * Adds a song to a playlist.
     *
     * @param userID     The ID of the user who owns the playlist.
     * @param playlistID The ID of the playlist to add the song to.
     * @param songID     The ID of the song to add to the playlist.
     * @throws SQLExceptionHandler If an error occurs during the database update.
     */
    public void addSongToPlaylist(int userID, int playlistID, int songID) throws SQLExceptionHandler {
        sqlHandler.executeUpdate("insert/insert_song_to_playlist.sql", userID, playlistID, songID);
    }

    /**
     * Adds a new tag to the database for a specified user.
     *
     * @param userID        The ID of the user who owns the tag.
     * @param tagName       The name of the new tag.
     * @param isPredefined Whether the tag is predefined or not.
     * @throws SQLExceptionHandler If an error occurs during the database update.
     */
    public void addNewTag(int userID, String tagName, boolean isPredefined) throws SQLExceptionHandler {
        sqlHandler.executeUpdate("insert/insert_tag.sql", userID, tagName, isPredefined);
    }

    /**
     * Adds a tag to a song in the database.
     *
     * @param userID The ID of the user who owns the song and tag.
     * @param songID The ID of the song to add the tag to.
     * @param tagID  The ID of the tag to add to the song.
     * @throws SQLExceptionHandler If an error occurs during the database update.
     */
    public void addTagToSong(int userID, int songID, int tagID) throws SQLExceptionHandler {
        sqlHandler.executeUpdate("insert/insert_tag_to_song.sql", songID, tagID, userID);
    }

    /**
     * Adds a tag to a playlist in the database.
     *
     * @param userID     The ID of the user who owns the playlist and tag.
     * @param playlistID The ID of the playlist to add the tag to.
     * @param tagID      The ID of the tag to add to the playlist.
     * @throws SQLExceptionHandler If an error occurs during the database update.
     */
    public void addTagToPlaylist(int userID, int playlistID, int tagID) throws SQLExceptionHandler {
        sqlHandler.executeUpdate("insert/insert_tag_to_playlist.sql", playlistID, tagID, userID);
    }

    /**
     * Adds lyrics to a song in the database.
     *
     * @param userID     The ID of the user who owns the song.
     * @param songID     The ID of the song to which the lyrics will be added.
     * @param lyricsPath The path to the lyrics file.
     * @throws SQLExceptionHandler If an error occurs during the database update.
     */
    public void addLyricsToSong(int userID, int songID, String lyricsPath) throws SQLExceptionHandler {
        sqlHandler.executeUpdate("insert/insert_lyrics.sql", lyricsPath, userID, songID);
    }

    /**
     * Adds an image to a song in the database.
     *
     * @param userID    The ID of the user who owns the song.
     * @param songID    The ID of the song to which the image will be added.
     * @param imagePath The path to the image file.
     * @throws SQLExceptionHandler If an error occurs during the database update.
     */
    public void addImageToSong(int userID, int songID, String imagePath) throws SQLExceptionHandler {
        sqlHandler.executeUpdate("insert/insert_imagePath.sql", imagePath, userID, songID);
    }

    /**
     * Removes a song from the database for a specified user.
     *
     * @param userID The ID of the user who owns the song.
     * @param songID The ID of the song to be removed.
     * @throws SQLExceptionHandler If an error occurs during the database update.
     */
    public void removeSong(int userID, int songID) throws SQLExceptionHandler {
        sqlHandler.executeUpdate("delete/delete_song.sql", songID, userID);
    }

    /**
     * Removes a playlist from the database for a specified user.
     *
     * @param userID     The ID of the user who owns the playlist.
     * @param playlistID The ID of the playlist to be removed.
     * @throws SQLExceptionHandler If an error occurs during the database update.
     */
    public void removePlaylist(int userID, int playlistID) throws SQLExceptionHandler {
        sqlHandler.executeUpdate("delete/delete_playlist.sql", playlistID, userID);
    }

    /**
     * Removes a song from a playlist in the database.
     *
     * @param userID     The ID of the user who owns the playlist and song.
     * @param playlistID The ID of the playlist from which the song will be removed.
     * @param songID     The ID of the song to be removed.
     * @throws SQLExceptionHandler If an error occurs during the database update.
     */
    public void removeSongFromPlaylist(int userID, int playlistID, int songID) throws SQLExceptionHandler {
        sqlHandler.executeUpdate("delete/delete_song_from_playlist.sql", playlistID, songID, userID);
    }

    /**
     * Removes a tag from the database for a specified user.
     *
     * @param userID The ID of the user who owns the tag.
     * @param tagID  The ID of the tag to be removed.
     * @throws SQLExceptionHandler If an error occurs during the database update.
     */
    public void removeTag(int userID, int tagID) throws SQLExceptionHandler {
        sqlHandler.executeUpdate("delete/delete_tag.sql", tagID, userID);
    }

    /**
     * Removes a tag from a song in the database.
     *
     * @param userID The ID of the user who owns the song and tag.
     * @param songID The ID of the song from which the tag will be removed.
     * @param tagID  The ID of the tag to be removed from the song.
     * @throws SQLExceptionHandler If an error occurs during the database update.
     */
    public void removeTagFromSong(int userID, int songID, int tagID) throws SQLExceptionHandler {
        sqlHandler.executeUpdate("delete/delete_tag_from_song.sql", songID, tagID, userID);
    }

    /**
     * Removes a tag from a playlist in the database.
     *
     * @param userID     The ID of the user who owns the playlist and tag.
     * @param playlistID The ID of the playlist from which the tag will be removed.
     * @param tagID      The ID of the tag to be removed from the playlist.
     * @throws SQLExceptionHandler If an error occurs during the database update.
     */
    public void removeTagFromPlaylist(int userID, int playlistID, int tagID) throws SQLExceptionHandler {
        sqlHandler.executeUpdate("delete/delete_tag_from_playlist.sql", playlistID, tagID, userID);
    }

    /**
     * Updates the details of a song in the database.
     *
     * @param userID       The ID of the user who owns the song.
     * @param songID       The ID of the song to be updated.
     * @param newName      The new name of the song, or null if no change is
     *                     desired.
     * @param newImagePath The new path to the song's image, or null if no change is
     *                     desired.
     * @param newVideoPath The new path to the song's video, or null if no change is
     *                     desired.
     * @param newArtist    The new artist of the song, or null if no change is
     *                     desired.
     * @param newAlbum     The new album of the song, or null if no change is
     *                     desired.
     * @throws SQLExceptionHandler If an error occurs during the database update.
     */
    public void updateSongDetails(int userID, int songID, String newName, String newImagePath,
                                  String newVideoPath, String newArtist, String newAlbum) throws SQLExceptionHandler {

        sqlHandler.executeUpdate("update/update_song.sql", newName, newImagePath, newVideoPath, newArtist, newAlbum,
                songID,
                userID);
    }

    public void updateSongPositionInPlaylist(int userID, int playlistID, int songID, int newPosition)
            throws SQLExceptionHandler {
        sqlHandler.executeUpdate("update/update_songposition_playlist.sql", newPosition, playlistID, songID, userID);
    }

    /**
     * Updates the 'StayLogged' value of a user.
     *
     * @param userID     The ID of the user to update.
     * @param stayLogged The new value of StayLogged (true/false).
     * @throws SQLExceptionHandler If an error occurs during the update.
     */
    public void updateUserStayLogged(int userID, boolean stayLogged) throws SQLExceptionHandler {
        sqlHandler.executeUpdate("update/update_user_stay_logged.sql", stayLogged, userID);
    }

    /**
     * Updates the username of a user in the database.
     *
     * @param userID   The ID of the user to update.
     * @param username The new username for the user.
     * @throws SQLExceptionHandler If an error occurs during the update.
     */
    public void updateUserUsername(int userID, String username) throws SQLExceptionHandler {
        sqlHandler.executeUpdate("update/update_user_username.sql", username, userID);
    }

    /**
     * Updates the password of a user in the database.
     *
     * @param userID   The ID of the user to update.
     * @param password The new password for the user.
     * @throws SQLExceptionHandler If an error occurs during the update.
     */
    public void updateUserPassword(int userID, String password) throws SQLExceptionHandler {
        sqlHandler.executeUpdate("update/update_user_password.sql", password, userID);
    }

    /**
     * Removes a user from the database.
     *
     * @param userID The ID of the user to be removed.
     * @throws SQLExceptionHandler If an error occurs during the database update.
     */
    public void removeUser(int userID) throws SQLExceptionHandler {
        sqlHandler.executeUpdate("delete/delete_user.sql", userID);
    }

    /**
     * Clears the database by removing all entries in all tables.
     *
     * @throws SQLExceptionHandler If an error occurs during the database update.
     */
    public void clearDatabase() throws SQLExceptionHandler {
        sqlHandler.executeClearQuery("clear_database.sql");
    }

    /**
     * Updates the profile image path of a user in the database.
     *
     * @param userID              The ID of the user whose profile image path will
     *                            be
     *                            updated.
     * @param newProfileImagePath The new path to the user's profile image.
     * @throws SQLExceptionHandler If an error occurs during the database update.
     */
    public void updateUserProfileImagePath(int userID, String newProfileImagePath) throws SQLExceptionHandler {
        sqlHandler.executeUpdate("update/update_user_profile_image.sql", newProfileImagePath, userID);
    }

    /**
     * Updates the language of a user in the database. 'en', 'fr', 'nl' are the only
     * accepted.
     *
     * @param id       The ID of the user whose language will be updated.
     * @param language The new language for the user.
     * @throws SQLExceptionHandler If an error occurs during the database update.
     */
    public void updateLanguage(int id, String language) throws SQLExceptionHandler {
        sqlHandler.executeUpdate("update/update_user_language.sql", language, id);
    }
}

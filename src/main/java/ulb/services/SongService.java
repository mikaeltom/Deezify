package ulb.services;

import ulb.exceptions.songs.SQLExceptionHandler;
import ulb.models.Song;

import java.util.HashMap;

/**
 * Handles database operations related to songs, such as retrieving all songs
 * from
 * user #1 and updating song metadata.
 */
public class SongService {
    private final SQLService sqlService = SQLService.getInstance();

    /**
     * Updates the metadata of a given song in the database and locally.
     *
     * @param song     The song whose metadata is to be updated.
     * @param metadata A HashMap containing the new metadata values for the song.
     *                 Keys include "Title", "Artist", and "Album".
     * @throws SQLExceptionHandler If an error occurs while updating the database.
     */
    public void setMetadata(Song song, HashMap<String, String> metadata) throws SQLExceptionHandler {
        sqlService.updateSongDetails(song.getId(), metadata.get("Title"), metadata.get("Images"), null,
                metadata.get("Artist"),
                metadata.get("Album"));
        song.setMetadata(metadata);
    }

    /**
     * Returns the given field if it is not empty, otherwise returns the default
     * value.
     *
     * @param field        The field to check.
     * @param defaultValue The default value to return if the field is empty.
     * @return The field if it is not empty, or the default value if it is.
     */
    protected String getOrDefault(String field, String defaultValue) {
        return field.isEmpty() ? defaultValue : field;
    }

    /**
     * Updates the metadata of the given song in the database and locally.
     * <p>
     * The title, artist, and album fields are updated with the given values if
     * they are not empty. If the given values are empty, the current values are
     * used. The tags are updated with the given tags, or the current tags if
     * none are provided.
     * <p>
     * If an error occurs while updating the database, a
     * {@link SQLExceptionHandler} is thrown.
     *
     * @param song        The song whose metadata is to be updated.
     * @param titleField  The new title of the song, or empty if no change is
     *                    desired.
     * @param artistField The new artist of the song, or empty if no change is
     *                    desired.
     * @param albumField  The new album of the song, or empty if no change is
     *                    desired.
     * @throws SQLExceptionHandler If an error occurs while updating the database.
     */
    public void updateSongMetadata(Song song, String titleField, String artistField,
                                   String albumField) throws SQLExceptionHandler {
        song.setArtist(artistField);
        song.setAlbum(albumField);
        song.setTitle(titleField);
        HashMap<String, String> metadata = new HashMap<>();
        metadata.put("Title", getOrDefault(titleField, song.getTitle()));
        metadata.put("Artist", getOrDefault(artistField, song.getArtist()));
        metadata.put("Album", getOrDefault(albumField, song.getAlbum()));
        metadata.put("Tags", song.tagsToString());
        setMetadata(song, metadata);
    }

    public Song getSong(String title) throws SQLExceptionHandler {
        int id = sqlService.getSongId(title);
        return sqlService.getSong(id);
    }

    public void addNewSong(String songPath, String name, int timeLength, String audioType, String artist, String album,
                           String lyrics, String imagePath, String videoPath) throws SQLExceptionHandler {
        sqlService.addNewSong(songPath, name, timeLength, audioType, artist, album, lyrics, imagePath, videoPath);
    }
}

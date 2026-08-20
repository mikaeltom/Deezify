package ulb.controllers.options;

import ulb.controllers.uploaders.Uploader;
import ulb.exceptions.songs.InvalidSongException;
import ulb.exceptions.songs.SQLExceptionHandler;
import ulb.models.Song;
import ulb.services.options.LyricsUploaderService;

import java.io.File;
import java.io.IOException;

/**
 * Handles the upload of a lyrics file to the "lyrics" directory and updates the song in the database with the path to the lyrics file.
 * <p>
 * Exceptions:
 * - IOException: If an error occurs while saving the file.
 * - SQLExceptionHandler: If an error occurs while updating the database.
 */
public class LyricsUploader extends Uploader<Song> {
    private final LyricsUploaderService lyricsUploaderService = new LyricsUploaderService();

    /**
     * Uploads a lyrics file to the "lyrics" directory and updates the song in the database with the path to the lyrics file.
     * <p>
     * Exceptions:
     * - IOException: If an error occurs while saving the file.
     * - SQLExceptionHandler: If an error occurs while updating the database.
     *
     * @param file the file to upload
     * @param song the song to which the lyrics will be added
     * @throws IOException         if an error occurs while saving the file
     * @throws SQLExceptionHandler if an error occurs while updating the database
     */
    @Override
    protected void uploadFile(File file, Song song) throws IOException, SQLExceptionHandler {
        lyricsUploaderService.addLyrics(file, song);
    }

    /**
     * Deletes the lyrics associated with the given song.
     * <p>
     * Exceptions:
     * - SQLExceptionHandler: If an error occurs while updating the database.
     * - InvalidSongException: If the song is not found in the database.
     *
     * @param song the song whose lyrics are to be deleted
     * @throws SQLExceptionHandler  if an error occurs while updating the database
     * @throws InvalidSongException if the song is not found in the database
     */
    @Override
    protected void deleteItem(Song song) throws SQLExceptionHandler, InvalidSongException {
        lyricsUploaderService.deleteLyrics(song);
    }

    /**
     * Retrieves the title of the file chooser dialog for selecting a lyrics file.
     * <p>
     * This method overrides the default title in the superclass.
     *
     * @return the title of the file chooser dialog
     */
    @Override
    protected String getTitle() {
        return "Select a lyrics file";
    }

    /**
     * Retrieves the description of the file chooser dialog for selecting a lyrics file.
     * <p>
     * This method overrides the default description in the superclass.
     *
     * @return the description of the file chooser dialog
     */
    @Override
    protected String getDescription() {
        return "Lyrics files";
    }

    /**
     * Retrieves the file extensions that are accepted by the file chooser dialog.
     * <p>
     * This method overrides the default extensions in the superclass.
     *
     * @return the file extensions that are accepted by the file chooser dialog
     */
    @Override
    protected String[] getExtensions() {
        return new String[]{"*.lrc"};
    }
}

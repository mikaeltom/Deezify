package ulb.controllers.options;

import javafx.stage.FileChooser;
import ulb.controllers.uploaders.Uploader;
import ulb.exceptions.songs.SQLExceptionHandler;
import ulb.models.Song;
import ulb.services.collections.LibraryService;
import ulb.services.options.MusicUploaderService;

import java.io.File;
import java.io.IOException;

/**
 * Handles the upload of a music file to the library.
 * This class is a subclass of the abstract class Uploader and is responsible for
 * adding a new song to the library or deleting an existing song from the library.
 * The music file is copied to the "music" directory and the song is added to the
 * database with the path to the music file.
 */
public class MusicUploader extends Uploader<Song> {
    private final MusicUploaderService musicUploaderService;

    public MusicUploader(LibraryService libraryService) {
        this.musicUploaderService = new MusicUploaderService(libraryService);
    }

    /**
     * Adds a new song to the library.
     * The song is added to the database and the music file is copied to the
     * "music" directory.
     *
     * @param file the music file to be added
     * @param song the song to be added
     * @throws IOException         if an error occurs while copying the file
     * @throws SQLExceptionHandler if an error occurs while adding the song to the database
     */
    @Override
    protected void uploadFile(File file, Song song) throws IOException, SQLExceptionHandler {
        musicUploaderService.addSong(file);
    }

    /**
     * Deletes the specified song from the library.
     * <p>
     * This method removes the song from the database and deletes the associated
     * music file. If the song is not found in the database, or if the deletion
     * of the music file fails, a {@link SQLExceptionHandler} or {@link IOException}
     * is thrown respectively.
     *
     * @param song The song to be deleted.
     * @throws SQLExceptionHandler If an error occurs while removing the song from the database.
     * @throws IOException         If an error occurs while deleting the music file.
     */
    @Override
    protected void deleteItem(Song song) throws SQLExceptionHandler, IOException {
        musicUploaderService.deleteSong(song);
    }

    /**
     * Gets the title of the {@link FileChooser} dialog.
     *
     * @return The title of the dialog.
     */
    @Override
    protected String getTitle() {
        return "Select a music file";
    }

    /**
     * Returns the description of the {@link FileChooser} dialog.
     *
     * @return The description of the dialog.
     */
    @Override
    protected String getDescription() {
        return "Music files";
    }

    /**
     * Returns the file extensions to be accepted by the {@link FileChooser} dialog.
     *
     * @return An array of file extensions.
     */
    @Override
    protected String[] getExtensions() {
        return new String[]{"*.mp3", "*.wav", "*.flac"};
    }
}
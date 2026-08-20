package ulb.services.options;

import ulb.exceptions.songs.SQLExceptionHandler;
import ulb.models.Song;
import ulb.services.UploaderService;
import ulb.services.collections.LibraryService;

import java.io.File;
import java.io.IOException;

/**
 * Handles the upload of a music file to the "music" directory.
 * Provides a method to open a dialog to select a music file and save it to the
 * "music" directory.
 */
public class MusicUploaderService extends UploaderService {
    private final LibraryService libraryService;

    public MusicUploaderService(LibraryService libraryService) {
        this.libraryService = libraryService;
    }

    /**
     * Retrieves the name of the directory where music files are stored.
     *
     * @return the directory name for music files
     */
    @Override
    protected String getDirectoryName() {
        return "src/main/resources/music/MusicUser" + sqlService.getUserId();
    }

    /**
     * Adds a song to the library.
     * Opens a file chooser dialog for the user to select a song to add.
     * If the song is successfully added to the library, it saves the song to the
     * "music" directory.
     * If the library is empty, it clears the library and adds the selected song.
     * If a song with the same name already exists in the library, the method throws
     * an IOException.
     *
     * @param selectedFile the selected song to add
     * @throws IOException         if the selected song is already in the library,
     *                             or if the saving of the song fails
     * @throws SQLExceptionHandler if the song is not found in the database
     */
    public void addSong(File selectedFile) throws IOException, SQLExceptionHandler {
        File file = saveFile(selectedFile);
        libraryService.setSong(file);
    }

    /**
     * Deletes a song from the library.
     * Removes the song from the database and deletes the associated music file.
     * If the song is not found in the database, or if the deletion of the music
     * file fails, the method throws a SQLExceptionHandler or IOException
     * respectively.
     *
     * @param song the song to be deleted
     * @throws IOException         if the deletion of the music file fails
     * @throws SQLExceptionHandler if the song is not found in the database
     */
    public void deleteSong(Song song) throws IOException, SQLExceptionHandler {
        sqlService.removeSong(song.getId());
        uploaderRepository.deleteFile(song.getPath());
    }
}

package ulb.services.options;

import ulb.exceptions.songs.InvalidSongException;
import ulb.exceptions.songs.SQLExceptionHandler;
import ulb.models.Song;
import ulb.services.UploaderService;
import ulb.utils.I18n;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

/**
 * The LyricsUploaderService class is responsible for handling the uploading of
 * lyrics files.
 * It provides methods to save lyrics files to the specified directory and
 * update the
 * database with the path to the saved file. This class extends UploaderService
 * and
 * overrides methods to specify the directory name for lyrics files.
 */
public class LyricsUploaderService extends UploaderService {

    /**
     * Retrieves the name of the directory where lyrics files are stored.
     * <p>
     * This method returns the name of the directory where lyrics files are stored.
     * The directory name is "lyrics".
     *
     * @return the directory name for lyrics files
     */
    @Override
    protected String getDirectoryName() {
        return "src/main/resources/lyrics/LyricsUser" + sqlService.getUserId();
    }

    /**
     * Adds the lyrics of a song to the database and updates the song's lyrics path.
     * <p>
     * Opens a file chooser dialog to select a lyrics file.
     * Saves the selected file to the "lyrics" directory.
     * Updates the database with the path to the saved file.
     * Sets the selected song's lyrics path to the path of the saved file.
     * <p>
     * Exceptions:
     * - IOException: If an error occurs while saving the file.
     * - SQLExceptionHandler: If an error occurs while updating the database.
     *
     * @param selectedFile The file containing the lyrics of the song.
     * @param song         The song whose lyrics will be updated.
     * @throws IOException         if an error occurs while saving the file
     * @throws SQLExceptionHandler if an error occurs while updating the database
     */
    public void addLyrics(File selectedFile, Song song) throws IOException, SQLExceptionHandler {
        String path = saveFile(selectedFile).getPath();
        sqlService.addLyricsToSong(song.getId(), path);
        song.setLyricsPath(path);
    }

    /**
     * Deletes the lyrics associated with the given song.
     * <p>
     * This method removes the lyrics path from the song and updates the database
     * to reflect the removal of the lyrics. If the song's lyrics path is empty,
     * an InvalidSongException is thrown.
     *
     * @param song The song whose lyrics are to be deleted.
     * @throws SQLExceptionHandler  If an error occurs while updating the database.
     * @throws InvalidSongException If the lyrics path does not exist.
     */
    public void deleteLyrics(Song song) throws SQLExceptionHandler, InvalidSongException {
        if (Objects.equals(song.getLyricsPath(), "")) {
            throw new InvalidSongException(InvalidSongException.ErrorType.LYRICS_PATH, I18n.get("invalid_song_message_lyrics"));
        }
        sqlService.addLyricsToSong(song.getId(), "");
        song.setLyricsPath("");
    }
}

package ulb.services.options;

import ulb.exceptions.songs.InvalidSongException;
import ulb.exceptions.songs.SQLExceptionHandler;
import ulb.models.Song;
import ulb.services.UploaderService;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

/**
 * Handles the upload of a cover image for a song.
 * The cover image is saved to the "img" directory and the path is updated in
 * the database.
 * The cover image is associated with the given song.
 */
public class CoverUploaderService extends UploaderService {
    private String selectedPath;

    /**
     * Gets the directory name where cover images are stored.
     * This method returns the name of the directory where cover images are stored.
     * The directory name is "src/main/resources/img".
     *
     * @return the directory name for cover images
     */
    @Override
    protected String getDirectoryName() {
        return "src/main/resources/img/ImgUser" + sqlService.getUserId();
    }

    /**
     * Gets the path of the selected image.
     * This path is used by {@link #addCover(File, Song)} and
     * {@link #deleteCover(Song)}.
     * If no image has been selected, this method returns an empty string.
     *
     * @return the path of the selected image
     */
    public String getSelectedImagePath() {
        return selectedPath;
    }

    /**
     * Adds a cover image for the given song.
     * The cover image is saved to the "img" directory and the path is updated in
     * the database.
     * If the image path is empty, the method does nothing.
     *
     * @param selectedFile the selected cover image file
     * @param song         the song to add the cover image to
     * @throws IOException         if an error occurs while saving the file
     * @throws SQLExceptionHandler if an error occurs while updating the database
     */
    public void addCover(File selectedFile, Song song) throws IOException, SQLExceptionHandler {
        selectedPath = saveFile(selectedFile).getPath();
        sqlService.addImageToSong(song.getId(), selectedPath);
        song.setImagePath(selectedPath);
    }

    /**
     * Deletes the cover image associated with the given song.
     * <p>
     * This method removes the cover image path from the song and updates the
     * database
     * to reflect the removal. If the song's image path is empty, an
     * InvalidSongException
     * is thrown.
     *
     * @param song The song whose cover image is to be deleted.
     * @throws SQLExceptionHandler  If an error occurs while updating the database.
     * @throws InvalidSongException If the image path does not exist.
     */
    public void deleteCover(Song song) throws SQLExceptionHandler, InvalidSongException, IOException {
        if (Objects.equals(song.getImagePath(), "")) {
                throw new InvalidSongException(InvalidSongException.ErrorType.COVER_PATH, "invalid_song_message_cover");
        }
        if (!song.getImagePath().contains("no-cover")) {
            uploaderRepository.deleteFile(song.getImagePath());
        }
        sqlService.addImageToSong(song.getId(), "");
        song.setImagePath(null);
    }
}

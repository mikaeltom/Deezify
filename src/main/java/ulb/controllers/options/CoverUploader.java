package ulb.controllers.options;

import ulb.controllers.uploaders.Uploader;
import ulb.exceptions.songs.InvalidSongException;
import ulb.exceptions.songs.SQLExceptionHandler;
import ulb.models.Song;
import ulb.services.options.CoverUploaderService;

import java.io.File;
import java.io.IOException;

/**
 * Controller responsible for handling cover image uploads.
 * This controller manages the logic for importing and deleting cover images
 * without directly interacting with the FXML UI.
 */
public class CoverUploader extends Uploader<Song> {
    private final CoverUploaderService coverUploaderService = new CoverUploaderService();

    /**
     * Uploads a cover image file and associates it with the given song.
     * <p>
     * This method delegates the task of adding the cover image to the
     * CoverUploaderService, which saves the file and updates the song's cover path.
     * <p>
     * Exceptions:
     * - IOException: If an error occurs during file upload.
     * - SQLExceptionHandler: If an error occurs while updating the database.
     *
     * @param file The cover image file to be uploaded.
     * @param song The song to associate with the uploaded cover image.
     */
    @Override
    protected void uploadFile(File file, Song song) throws IOException, SQLExceptionHandler {
        coverUploaderService.addCover(file, song);
    }

    /**
     * Deletes the cover image associated with the given song.
     * <p>
     * This method delegates the task of deleting the cover image to the
     * CoverUploaderService, which removes the cover image from the database
     * and the file system.
     * <p>
     * Exceptions:
     * - SQLExceptionHandler: If an error occurs while updating the database.
     * - InvalidSongException: If the song is invalid or the cover cannot be deleted.
     *
     * @param song The song whose cover image is to be deleted.
     */
    @Override
    protected void deleteItem(Song song) throws SQLExceptionHandler, InvalidSongException, IOException {
        coverUploaderService.deleteCover(song);
    }

    /**
     * Retrieves the title of the window that is displayed when the user is asked to upload a cover image.
     * <p>
     * This method is used by the base class to display the window title.
     * <p>
     * The window title is currently "Select a cover image".
     *
     * @return the title of the window
     */
    @Override
    protected String getTitle() {
        return "Select a cover image";
    }

    /**
     * Retrieves a description of the type of item that is being uploaded.
     * <p>
     * This method is used by the base class to display a description of the
     * item that is being uploaded.
     * <p>
     * The description is currently "Image files".
     *
     * @return the description of the item
     */
    @Override
    protected String getDescription() {
        return "Image files";
    }

    /**
     * Returns an array of file extensions for cover image files.
     * <p>
     * This method specifies the types of image files that are supported for cover uploads.
     * Supported extensions include .jpg, .png, and .jpeg.
     *
     * @return an array of supported image file extensions
     */
    @Override
    protected String[] getExtensions() {
        return new String[]{"*.jpg", "*.png", "*.jpeg"};
    }

    /**
     * Retrieves the path of the selected cover image.
     * <p>
     * This method returns the image path that was selected during
     * the cover upload process. It delegates the retrieval of the path
     * to the CoverUploaderService.
     *
     * @return the path of the selected image as a String.
     */
    public String getSelectedImagePath() {
        return coverUploaderService.getSelectedImagePath();
    }

}

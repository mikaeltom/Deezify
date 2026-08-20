package ulb.controllers.uploaders;

import javafx.stage.Stage;
import ulb.exceptions.songs.InvalidSongException;
import ulb.exceptions.songs.SQLExceptionHandler;
import ulb.views.UploaderView;

import java.io.File;
import java.io.IOException;

/**
 * A generic abstract class that provides utility methods for uploading files to the correct
 * location, based on the type of object.
 * <p>
 * The class provides a method to show a file chooser dialog to select a file with
 * the specified title and extensions. It also provides abstract methods to upload and delete
 * the selected file, allowing subclasses to define specific behavior based on the type of item (T).
 *
 * @param <T> the type of item being handled by this uploader (e.g., Song, User, etc.)
 */
public abstract class Uploader<T> {
    protected final UploaderView uploaderView = new UploaderView();

    /**
     * Shows a file chooser dialog to select a file with the specified title and extensions.
     * The dialog is displayed on the given stage.
     *
     * @param stage       the stage on which the file chooser dialog will be displayed
     * @param title       the title of the file chooser dialog
     * @param description a description of the types of files that can be selected, which will be
     *                    displayed in the file chooser dialog
     * @param extensions  the extensions that can be selected in the file chooser dialog
     * @return the selected file, or null if no file was selected
     */
    protected final File selectFile(Stage stage, String title, String description, String... extensions) {
        return uploaderView.showChooseFile(stage, title, description, extensions);
    }

    /**
     * Uploads the specified file to the correct location, based on the type of uploader this is.
     * <p>
     * Subclasses must implement this method to define how the file is associated with the object T
     * and how the file path is updated (e.g. in the database).
     *
     * @param file the file to be uploaded
     * @param item the object to be updated (e.g. a Song, User, etc.)
     * @throws IOException         if an error occurs while saving the file
     * @throws SQLExceptionHandler if an error occurs while updating the database
     */
    protected abstract void uploadFile(File file, T item) throws IOException, SQLExceptionHandler;

    /**
     * Deletes the item associated with the given object.
     * <p>
     * This method is called when the user requests to delete an item's file (e.g. its music, cover, or profile picture).
     * It is responsible for deleting the file from the file system and updating the database to reflect the deletion.
     *
     * @param item the object whose associated file is to be deleted
     * @throws SQLExceptionHandler  if an error occurs while updating the database
     * @throws InvalidSongException if the object does not have an associated item or path
     * @throws IOException          if an error occurs while deleting the item from the file system
     */
    protected abstract void deleteItem(T item) throws SQLExceptionHandler, InvalidSongException, IOException;

    /**
     * Shows a file chooser dialog to select a file to upload, and then uploads
     * the selected file. If the file is successfully uploaded, the database is
     * updated with the new file's path.
     * <p>
     * This method does not specify an object to update. Subclasses must implement the logic
     * for handling null or creating a new item.
     *
     * @param stage the stage on which the file chooser dialog will be displayed
     * @throws IOException         if an error occurs while saving the file
     * @throws SQLExceptionHandler if an error occurs while updating the database
     */
    public boolean add(Stage stage) throws SQLExceptionHandler, IOException {
        return add(stage, null);
    }

    /**
     * Shows a file chooser dialog to select a file to upload, and then uploads
     * the selected file. If the file is successfully uploaded, the database is
     * updated with the new file's path.
     * <p>
     * This method takes a parameter of type T, which specifies the object to be updated.
     * Subclasses must define how the file is associated with the object.
     *
     * @param stage the stage on which the file chooser dialog will be displayed
     * @param item  the object to be updated
     * @throws IOException         if an error occurs while saving the file
     * @throws SQLExceptionHandler if an error occurs while updating the database
     */
    public boolean add(Stage stage, T item) throws SQLExceptionHandler, IOException {
        File selectedFile = selectFile(stage, getTitle(), getDescription(), getExtensions());
        if (selectedFile == null) return false;
        uploadFile(selectedFile, item);
        return true;
    }

    /**
     * Deletes the item associated with the given object.
     * <p>
     * This method simply calls the abstract method {@link #deleteItem(Object)},
     * which is implemented by subclasses to delete the file associated with the object.
     *
     * @param item the object whose file is to be deleted
     * @throws SQLExceptionHandler  if an error occurs while updating the database
     * @throws InvalidSongException if the object does not have an item associated with it
     * @throws IOException          if an error occurs while deleting the item from the file system
     */
    public void delete(T item) throws SQLExceptionHandler, InvalidSongException, IOException {
        deleteItem(item);
    }

    /**
     * Gets the title of the uploader.
     * <p>
     * This title is used in the file chooser dialog to specify the title of the
     * dialog.
     *
     * @return The title of the uploader.
     */
    protected abstract String getTitle();

    /**
     * Provides a description of the uploader.
     * <p>
     * This description is used in the file chooser dialog to specify
     * the types of files that can be selected.
     *
     * @return A description of the uploader.
     */
    protected abstract String getDescription();

    /**
     * Provides the file extensions that this uploader can handle.
     * <p>
     * This method returns an array of strings representing the file extensions
     * that are supported by this uploader. These extensions are used in the file
     * chooser dialog to filter the files that can be selected for upload.
     *
     * @return an array of strings representing the supported file extensions
     */
    protected abstract String[] getExtensions();
}

package ulb.controllers.uploaders;

import javafx.stage.Stage;
import ulb.exceptions.songs.SQLExceptionHandler;
import ulb.models.User;
import ulb.services.accounts.ProfileImageUploaderService;

import java.io.File;
import java.io.IOException;

/**
 * Controller responsible for handling profile picture uploads.
 * This controller manages the logic for importing and deleting profile pictures
 * without directly interacting with the FXML UI.
 */
public class ProfileImageUploader extends Uploader<User> {
    private final ProfileImageUploaderService profilePictureUploaderService = new ProfileImageUploaderService();
    File selectedFile;

    /**
     * Uploads a profile picture and associates it with the given user.
     *
     * @param file The profile picture file to be uploaded.
     * @param user The user to associate the profile picture with.
     */
    public void uploadFile(File file, User user) throws IOException, SQLExceptionHandler {
        profilePictureUploaderService.addProfileImage(file);
    }

    /**
     * Deletes the profile picture associated with the given user.
     */
    public void deleteItem(User user) throws SQLExceptionHandler, IOException {
        String path = user.getProfileImagePath();
        profilePictureUploaderService.deleteProfileImage(path);
    }

    /**
     * Returns the title of the file chooser dialog for profile picture uploads.
     *
     * @return the title string.
     */
    @Override
    protected String getTitle() {
        return "Select a profile picture";
    }

    /**
     * Returns the description of accepted files.
     *
     * @return the description string.
     */
    @Override
    protected String getDescription() {
        return "Image files";
    }

    /**
     * Returns accepted image file extensions.
     *
     * @return the array of supported image extensions.
     */
    @Override
    protected String[] getExtensions() {
        return new String[]{"*.jpg", "*.png", "*.jpeg"};
    }


    /**
     * Displays a file chooser dialog to select a profile picture.
     *
     * @param stage The stage on which the file chooser dialog will be displayed.
     * @return the selected file path as a String, or null if no file is selected.
     */
    public String getSelectedProfileImagePath(Stage stage) {
        selectedFile = selectFile(stage, getTitle(), getDescription(), getExtensions());
        if (selectedFile == null) return null;
        return selectedFile.getPath();
    }

    /**
     * Saves the selected profile picture.
     *
     * @throws SQLExceptionHandler if there is an error during the SQL operation.
     * @throws IOException         if there is an error during file upload.
     */
    public void savePicture() throws SQLExceptionHandler, IOException {
        this.uploadFile(selectedFile, null);
    }
}

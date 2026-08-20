package ulb.services.accounts;

import ulb.exceptions.songs.SQLExceptionHandler;
import ulb.services.UploaderService;

import java.io.File;
import java.io.IOException;

/**
 * Handles the upload and removal of profile images for users.
 * Profile images are saved to the "img" directory and the path is updated in the database.
 */
public class ProfileImageUploaderService extends UploaderService {
    private String selectedPath;

    /**
     * Gets the directory name where profile images are stored.
     *
     * @return the directory name for profile images.
     */
    @Override
    protected String getDirectoryName() {
        return "src/main/resources/profile";
    }

    /**
     * Adds a profile image for the given user.
     * The image is saved in the "img" directory and its path is stored in the database.
     *
     * @param selectedFile The profile image file selected.
     * @throws IOException         If an error occurs while saving the file.
     * @throws SQLExceptionHandler If an error occurs while updating the database.
     */
    public void addProfileImage(File selectedFile) throws IOException, SQLExceptionHandler {
        selectedPath = String.valueOf(saveFile(selectedFile));
        if (!selectedPath.equals(getDirectoryName() + "/") && !selectedPath.isEmpty() && !selectedPath.equals("null")) {
            sqlService.updateUserProfileImagePath(selectedPath);
        }
    }

    /**
     * Deletes the profile image associated with the given user.
     * The path is cleared from the database and the in-memory object.
     *
     * @throws SQLExceptionHandler If an error occurs while updating the database.
     */
    public void deleteProfileImage(String path) throws SQLExceptionHandler, IOException {
        sqlService.updateUserProfileImagePath("");
        uploaderRepository.deleteFile(path);
        selectedPath = null;
    }
}

package ulb.repositories;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

/**
 * Handles operations related to uploading data to the database.
 * This class is responsible for updating the cover image path and lyrics path
 * for a song in the database.
 * It also handles the removal of a song from the database.
 */
public class UploaderRepository {
    /**
     * Saves a file to a specified directory.
     *
     * @param selectedFile  The file to be saved.
     * @param directoryName The name of the directory where the file will be saved.
     * @return The name of the saved file.
     * @throws IOException If an error occurs while saving the file.
     */
    public File saveFile(File selectedFile, String directoryName) throws IOException {
        if (selectedFile == null) {
            return null;
        }

        File directory = new File(directoryName);
        Files.createDirectories(directory.toPath());

        File targetFile = new File(directory, selectedFile.getName());
        Files.copy(selectedFile.toPath(), targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING);


        return targetFile;
    }

    /**
     * Deletes a file from the filesystem.
     *
     * @param path the path to the file to be deleted
     * @throws IOException if an error occurs while deleting the file
     */
    public void deleteFile(String path) throws IOException {
        File file = new File(path);
        Files.deleteIfExists(file.toPath());
    }
}

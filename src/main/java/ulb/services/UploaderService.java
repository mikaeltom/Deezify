package ulb.services;

import ulb.repositories.UploaderRepository;

import java.io.File;
import java.io.IOException;

/**
 * The UploaderService class provides a base implementation for handling file
 * uploads.
 * It defines methods for retrieving the directory name where files will be
 * stored
 * and interacting with the UploaderRepository for file operations.
 * <p>
 * Subclasses should implement the getDirectoryName method to specify the target
 * directory for file uploads.
 */
public abstract class UploaderService {
    protected final SQLService sqlService = SQLService.getInstance();
    protected final UploaderRepository uploaderRepository = new UploaderRepository();

    /**
     * Retrieves the name of the directory where files will be stored.
     *
     * @return the directory name as a String
     */
    protected abstract String getDirectoryName();

    /**
     * Saves the selected file to the designated directory.
     * <p>
     * This method delegates the actual file saving operation to the
     * UploaderRepository. It retrieves the directory name where the
     * file will be stored from the abstract method getDirectoryName().
     *
     * @param selectedFile the file to be saved
     * @return the name of the saved file
     * @throws IOException if an I/O error occurs during the file saving process
     */
    protected File saveFile(File selectedFile) throws IOException {
        return uploaderRepository.saveFile(selectedFile, getDirectoryName());
    }
}

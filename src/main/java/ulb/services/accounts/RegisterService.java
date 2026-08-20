package ulb.services.accounts;

import ulb.exceptions.BannedWordException;
import ulb.exceptions.credentials.InvalidPasswordException;
import ulb.exceptions.credentials.InvalidRegisterException;
import ulb.exceptions.credentials.InvalidUsernameException;
import ulb.exceptions.credentials.NotMatchingPasswordsException;
import ulb.exceptions.songs.SQLExceptionHandler;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Handles user registration operations.
 * This class is responsible for registering new users and managing their
 * credentials.
 */
public class RegisterService extends UserValidationService {

    /**
     * Handles the whole register process for a new user with the provided username,
     * password, and profile image path.
     */
    public int registerNewUser(String username, String password, String confirmationPassword, String profileImagePath,
                               boolean keepLogged, String currentLanguage)
            throws NotMatchingPasswordsException, InvalidRegisterException, SQLExceptionHandler,
            InvalidPasswordException, InvalidUsernameException, IOException, BannedWordException {
        checkMatchingPasswords(password, confirmationPassword);
        checkIfPasswordLength(password);
        checkUsernameFormat(username);
        int userId = this.register(username, password, profileImagePath, currentLanguage);

        createUserDirectories(userId);

        updateIsStayLogged(userId, keepLogged);
        return userId;
    }

    /**
     * Registers a new user with the provided username and password.
     *
     * @param username The username of the new user.
     * @param password The password of the new user.
     * @return The ID of the newly registered user.
     * @throws InvalidRegisterException If the registration fails due to invalid
     *                                  input or existing username.
     */
    private int register(String username, String password, String profileImagePath, String currentLanguage)
            throws InvalidRegisterException {
        try {
            checkIfUsernameExists(username);
            sqlService.addNewUser(username, currentLanguage, password, profileImagePath);
            return sqlService.getUserId(username);
        } catch (SQLExceptionHandler | InvalidUsernameException e) {
            throw new InvalidRegisterException("invalid_register_exception", e);
        }
    }

    /**
     * Creates the folders for music, lyrics, and images for a user inside
     * src/main/resources.
     *
     * @param userId ID of the user
     * @throws IOException if folder creation fails
     */
    private void createUserDirectories(int userId) throws IOException {
        createUserFolder("music", "MusicUser" + userId);
        createUserFolder("lyrics", "LyricsUser" + userId);
        createUserFolder("img", "ImgUser" + userId);
    }

    /**
     * Helper method to create a folder inside
     * src/main/resources/<type>/<folderName>
     *
     * @param folderType the base folder (e.g. "music", "lyrics", "img")
     * @param folderName the name of the user-specific folder to create
     * @throws IOException if folder creation fails
     */
    private void createUserFolder(String folderType, String folderName) throws IOException {
        Path path = Paths.get("src", "main", "resources", folderType, folderName);
        if (Files.notExists(path)) {
            Files.createDirectories(path);
        }
    }
}

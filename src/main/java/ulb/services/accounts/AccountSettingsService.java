package ulb.services.accounts;

import ulb.exceptions.BannedWordException;
import ulb.exceptions.credentials.InvalidPasswordException;
import ulb.exceptions.credentials.InvalidUsernameException;
import ulb.exceptions.credentials.NotMatchingPasswordsException;
import ulb.exceptions.songs.SQLExceptionHandler;
import ulb.models.User;
import ulb.utils.I18n;
import ulb.utils.PopupType;
import ulb.views.PopupView;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * The AccountSettingsService class is responsible for managing the account
 * settings of the user.
 * It provides methods to update user credentials, profile image, and stay
 * logged-in status.
 */
public class AccountSettingsService extends UserValidationService {
    User user = sqlService.getUser();

    public User getCurrentUser() {
        return user;
    }

    /**
     * Removes the user from the database.
     *
     * @throws SQLExceptionHandler if there is an error with the SQL query
     */
    public void removeUser() throws SQLExceptionHandler, IOException {
        sqlService.removeUser();
        removeDirectories();
    }

    /**
     * Updates the profile image path of the user in the database.
     *
     * @param profileImagePath The new profile image path.
     * @throws SQLExceptionHandler if there is an error with the SQL query
     */
    private void updateUserProfileImagePath(String profileImagePath) throws SQLExceptionHandler {
        if (!(profileImagePath == null) && !profileImagePath.isEmpty() &&
                !profileImagePath.equals(user.getProfileImagePath())) {
            sqlService.updateUserProfileImagePath(profileImagePath);
        }
    }

    /**
     * Updates the stay logged-in status of the user in the database.
     *
     * @param stayLoggedIn The new stay logged-in status.
     * @throws SQLExceptionHandler if there is an error with the SQL query
     */
    private void updateUserStayLogged(boolean stayLoggedIn) throws SQLExceptionHandler {
        if (stayLoggedIn != user.isStayLogged()) {
            updateIsStayLogged(user.getId(), stayLoggedIn);
        }
    }

    /**
     * Updates the user credentials (username and password) in the database.
     *
     * @param username The new username.
     * @param password The new password.
     * @throws SQLExceptionHandler if there is an error with the SQL query
     * @throws InvalidUsernameException if the username is invalid
     */
    private void updateUserCredentials(String username, String password)
            throws SQLExceptionHandler, InvalidUsernameException {
        if (username != null && !username.isEmpty() && !username.equals(user.getUsername())) {
            sqlService.updateUserUsername(username);
        }
        if (password != null && !password.isEmpty()) {
            sqlService.updateUserPassword(password);
        }
    }

    /**
     * Updates the user information in the database. It also checks the validity of
     * the new username and password.
     *
     * @param username         The new username.
     * @param password         The new password.
     * @param confirmPassword  The confirmation of the new password.
     * @param profileImagePath The new profile image path.
     * @param stayLoggedIn     The new stay logged-in status.
     * @throws NotMatchingPasswordsException if the passwords do not match
     * @throws InvalidUsernameException if the username is invalid
     * @throws SQLExceptionHandler if there is an error with the SQL query
     */
    public void updateUser(String username, String password, String confirmPassword, String profileImagePath,
                           boolean stayLoggedIn) throws NotMatchingPasswordsException, InvalidUsernameException, SQLExceptionHandler,
            InvalidPasswordException, IOException, BannedWordException {
        if (!username.equals(user.getUsername())) {
            checkIfUsernameExists(username);
            checkUsernameFormat(username);
        }
        if (password != null && !password.isEmpty()) {
            checkMatchingPasswords(password, confirmPassword);
            checkIfPasswordLength(password);
        }
        this.updateUserProfileImagePath(profileImagePath);
        this.updateUserStayLogged(stayLoggedIn);
        this.updateUserCredentials(username, password);
    }

    /**
     * Retrieves the current user's profile image path.
     *
     * @return The profile image path of the current user.
     */
    public boolean isStayLogged() {
        return user.isStayLogged();
    }

    /**
     * Deletes the folders for music, lyrics, and images associated with the user.
     *
     * @throws IOException if folder deletion fails
     */
    private void removeDirectories() throws IOException {
        deleteUserFolder("music", "MusicUser" + user.getId());
        deleteUserFolder("lyrics", "LyricsUser" + user.getId());
        deleteUserFolder("img", "ImgUser" + user.getId());
    }

    /**
     * Helper method to delete a folder inside
     * src/main/resources/<type>/<folderName>
     *
     * @param folderType the base folder (e.g. "music", "lyrics", "img")
     * @param folderName the name of the user-specific folder to delete
     * @throws IOException if folder deletion fails
     */
    private void deleteUserFolder(String folderType, String folderName) throws IOException {
        Path path = Paths.get("src", "main", "resources", folderType, folderName);
        if (Files.exists(path)) {
            Files.walk(path)
                    .sorted((a, b) -> b.compareTo(a))
                    .forEach(p -> {
                        try {
                            Files.delete(p);
                        } catch (IOException e) {
                            new PopupView("error_deleting_user_title", I18n.get("error_deleting_user"), PopupType.ERROR);
                        }
                    });
        }
    }
}

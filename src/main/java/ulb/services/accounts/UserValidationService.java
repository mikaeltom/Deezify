package ulb.services.accounts;

import ulb.exceptions.BannedWordException;
import ulb.exceptions.credentials.InvalidPasswordException;
import ulb.exceptions.credentials.InvalidUsernameException;
import ulb.exceptions.credentials.NotMatchingPasswordsException;
import ulb.exceptions.songs.SQLExceptionHandler;
import ulb.services.BannedWordService;
import ulb.services.SQLService;

import java.io.IOException;


/**
 * The UserValidationService class is responsible for validating user credentials during registration and account settings updates.
 * It provides methods to check if the provided passwords match and if the username already exists in the database.
 */
public abstract class UserValidationService {
    protected final SQLService sqlService = SQLService.getInstance();

    /**
     * Checks if the provided passwords match.
     *
     * @param password        The password entered by the user.
     * @param confirmPassword The confirmation password entered by the user.
     * @throws NotMatchingPasswordsException If the passwords do not match.
     */
    protected void checkMatchingPasswords(String password, String confirmPassword) throws NotMatchingPasswordsException {
        if (!password.equals(confirmPassword)) {
            throw new NotMatchingPasswordsException("not_matching_passwords_exception");
        }
    }


    /**
     * Checks if the provided username already exists in the database.
     *
     * @param username The username entered by the user.
     * @throws InvalidUsernameException If the username already exists.
     * @throws SQLExceptionHandler      If there is an error while accessing the database.
     */
    protected void checkIfUsernameExists(String username) throws InvalidUsernameException, SQLExceptionHandler {
        Integer userId = sqlService.getUserId(username);
        if (userId != null) {
            throw new InvalidUsernameException("invalid_username_exists_exception");
        }
    }

    /**
     * Updates the stay logged status of the user in the database.
     *
     * @param userId     The ID of the user.
     * @param stayLogged The stay logged status to set.
     */
    protected void updateIsStayLogged(int userId, boolean stayLogged) throws SQLExceptionHandler {
        sqlService.updateStayLogged(userId, stayLogged);
    }

    /**
     * Checks if the provided password is long enough.
     *
     * @param password The password entered by the user.
     * @throws InvalidPasswordException If the password is too short.
     */
    protected void checkIfPasswordLength(String password) throws InvalidPasswordException {
        if (password.length() < 6) {
            throw new InvalidPasswordException("password_too_short_exception");
        }
        if (password.length() > 20) {
            throw new InvalidPasswordException("password_too_long_exception");
        }
    }

    /**
     * Checks if the provided username format is valid.
     *
     * @param username The username entered by the user.
     * @throws InvalidUsernameException If the username format is invalid.
     */
    protected void checkUsernameFormat(String username) throws InvalidUsernameException, IOException, BannedWordException {
        BannedWordService bannedWordService = new BannedWordService();
        bannedWordService.containsBannedWords(username);
        if (!username.matches("^[a-zA-Z0-9]{4,20}$")) {
            throw new InvalidUsernameException("invalid_username_characters_exception");
        }
    }
}

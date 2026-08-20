package ulb.services.accounts;

import ulb.exceptions.credentials.InvalidLoginException;
import ulb.exceptions.songs.SQLExceptionHandler;

/**
 * Handles user login operations.
 * This class is responsible for authenticating users and managing their login status.
 */
public class LoginService extends UserValidationService {

    /**
     * Authenticates the user by checking username and password in the database.
     *
     * @param username The username provided.
     * @param password The password provided.
     * @return The UserID if credentials are correct.
     * @throws InvalidLoginException if the credentials are incorrect.
     */
    public int login(String username, String password, boolean stayLogged) throws InvalidLoginException, SQLExceptionHandler {
        Integer userId = sqlService.getUserIdFromLogin(username, password);
        if (userId == null) {
            throw new InvalidLoginException("invalid_login_exception");
        }
        updateIsStayLogged(userId, stayLogged);
        return userId;
    }
}

package ulb.exceptions.credentials;

import ulb.utils.I18n;

/**
 * This exception is thrown when the username provided by the user is invalid.
 * It extends the Exception class and provides a constructor to set the error message.
 */
public class InvalidUsernameException extends Exception {
    public InvalidUsernameException(String message) {
        super(I18n.get(message));
    }
}

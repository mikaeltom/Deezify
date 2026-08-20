package ulb.exceptions.credentials;

import ulb.utils.I18n;

/**
 * This exception is thrown when the password provided by the user is invalid.
 * It extends the Exception class and provides a constructor to set the error message.
 */
public class InvalidPasswordException extends Exception {
    public InvalidPasswordException(String message) {
        super(I18n.get(message));
    }
}

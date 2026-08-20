package ulb.exceptions.credentials;

import ulb.utils.I18n;

/**
 * This exception is thrown when the login credentials provided by the user are invalid.
 * It extends the Exception class and provides a constructor to set the error message.
 */
public class InvalidRegisterException extends Exception {
    public InvalidRegisterException(String message) {
        I18n.get(message);
    }

    public InvalidRegisterException(String message, Throwable cause) {
        super(I18n.get(message, cause.getMessage(), cause));
    }
}
package ulb.exceptions.credentials;

import ulb.utils.I18n;

/**
 * This exception is thrown when the login credentials provided by the user are invalid.
 * It extends the Exception class and provides a constructor to set the error message.
 */
public class InvalidLoginException extends Exception {
    public InvalidLoginException(String key) {
        super(I18n.get(key));
    }
}


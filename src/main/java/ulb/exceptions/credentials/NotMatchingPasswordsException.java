package ulb.exceptions.credentials;

import ulb.utils.I18n;

/**
 * This exception is thrown when the passwords provided by the user do not match.
 * It extends the Exception class and provides a constructor to set the error message.
 */
public class NotMatchingPasswordsException extends Exception {
    public NotMatchingPasswordsException(String message) {
        super(I18n.get(message));
    }
}

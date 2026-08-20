package ulb.exceptions.tag;

import ulb.utils.I18n;

/**
 * This exception is thrown when there is an error related to tags.
 * It extends the Exception class and provides a constructor to set the error message.
 */
public class InvalidTagError extends Exception {
    public InvalidTagError(String message) {
        super(I18n.get(message));
    }
}

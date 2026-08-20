package ulb.exceptions;

import ulb.utils.I18n;

/**
 * This exception is thrown when the given name is not invalid (tags, playlists, username,...).
 * It extends the Exception class and provides a constructor to set the error message.
 */
public class BannedWordException extends Exception {
    public BannedWordException(String messageKey, Object... params) {
        super(I18n.get(messageKey, params));
    }
}
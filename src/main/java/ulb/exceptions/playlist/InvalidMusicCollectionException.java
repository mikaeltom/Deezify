package ulb.exceptions.playlist;

import ulb.utils.I18n;

/**
 * This exception is thrown when the music collection is invalid.
 * It extends the Exception class and provides constructors to set the error message.
 */
public class InvalidMusicCollectionException extends Exception {

    public InvalidMusicCollectionException(String additionalMessage, ErrorType errorType) {
        super(I18n.get(getKey(errorType)) + " : " + additionalMessage);
    }

    private static String getKey(ErrorType type) {
        return switch (type) {
            case PLAYLIST -> "playlist";
            case QUEUE -> "queue";
            case FAVORITES -> "favorites";
        };
    }

    public enum ErrorType {
        PLAYLIST, QUEUE, FAVORITES
    }
}

package ulb.exceptions.songs;

import ulb.utils.I18n;

/**
 * This exception is thrown when the song provided by the user is invalid.
 * It extends the Exception class and provides a constructor to set the error message.
 */
public class InvalidSongException extends Exception {
    
    public InvalidSongException(ErrorType errorType) {
        super(I18n.get(getKey(errorType)));
    }

    public InvalidSongException(ErrorType errorType, String additionalMessage) {
        super(I18n.get(getKey(errorType)) + " : " + additionalMessage);
    }

    private static String getKey(ErrorType type) {
        return switch (type) {
            case TITLE -> "invalid_song_title";
            case ARTIST -> "invalid_song_artist";
            case ALBUM -> "invalid_song_album";
            case DURATION -> "invalid_song_duration";
            case PATH -> "invalid_song_path";
            case TAGS -> "invalid_song_tags";
            case LYRICS_PATH -> "invalid_song_lyrics_path";
            case COVER_PATH -> "invalid_song_cover_path";
        };
    }

    public enum ErrorType {
        TITLE, ARTIST, ALBUM, DURATION, PATH, TAGS, LYRICS_PATH, COVER_PATH
    }
}

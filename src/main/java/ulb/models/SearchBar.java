package ulb.models;

import ulb.exceptions.songs.InvalidSongException;
import ulb.exceptions.songs.SQLExceptionHandler;
import ulb.services.SongService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Represents the search bar and associated song list. Handles searching for
 * songs, retrieving
 * the results and updating the search bar's song list.
 */
public class SearchBar {
    private final List<Song> searchedSong;
    private final SongService songService = new SongService();

    public SearchBar() {
        this.searchedSong = new ArrayList<>();
    }

    /**
     * Returns the list of searched songs.
     *
     * @return List of searched songs.
     */
    public List<Song> getSearchedSong() {
        return searchedSong;
    }

    /**
     * Validates a song based on multiple criteria.
     *
     * @param song The song to validate.
     * @throws InvalidSongException if any validation fails.
     */
    public void isSongValid(Song song) throws InvalidSongException {
        Map<InvalidSongException.ErrorType, Boolean> validations = Map.of(
                InvalidSongException.ErrorType.DURATION,
                song.getDuration() == null || song.getDuration().toSeconds() <= 0,
                InvalidSongException.ErrorType.TITLE, isNullOrEmpty(song.getTitle()),
                InvalidSongException.ErrorType.ARTIST, isNullOrEmpty(song.getArtist()),
                InvalidSongException.ErrorType.ALBUM, isNullOrEmpty(song.getAlbum()),
                InvalidSongException.ErrorType.PATH, isNullOrEmpty(song.getPath()),
                InvalidSongException.ErrorType.TAGS, song.getTags() == null);

        for (var entry : validations.entrySet()) {
            if (entry.getValue()) {
                throw new InvalidSongException(entry.getKey());
            }
        }
    }

    /**
     * Clears the list of searched songs.
     */
    public void clearSearchedSong() {
        searchedSong.clear();
    }

    /**
     * Updates the searched songs list with new valid songs.
     *
     * @param newSearchedSong List of new songs to add.
     * @throws InvalidSongException if any song is invalid.
     */
    public void updateSearchedSong(List<Song> newSearchedSong) throws InvalidSongException, SQLExceptionHandler {
        if (newSearchedSong == null || newSearchedSong.isEmpty()) {
            throw new InvalidSongException(InvalidSongException.ErrorType.TITLE,
                    "invalid_song_message_list");
        }

        for (Song song : newSearchedSong) {
            isSongValid(song);
            Song realSong = songService.getSong(song.getTitle());
            if (!searchedSong.contains(realSong)) {
                searchedSong.add(realSong);
            }
        }
    }

    /**
     * Utility method to check if a string is null or empty.
     *
     * @param str The string to check.
     * @return true if null or empty, false otherwise.
     */
    private boolean isNullOrEmpty(String str) {
        return str == null || str.isEmpty();
    }
}

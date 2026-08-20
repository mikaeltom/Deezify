package ulb.services;

import ulb.dtos.SongDTO;
import ulb.exceptions.songs.InvalidSongException;
import ulb.exceptions.songs.SQLExceptionHandler;
import ulb.models.Lyrics;
import ulb.models.SearchBar;
import ulb.models.Song;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service class responsible for searching songs in the database
 * and their lyrics.
 */
public class SearchBarService {
    private final SQLService sqlService;
    private final HashMap<String, List<String>> lyrics;
    private final SearchBar searchBar;

    public SearchBarService() {
        this.sqlService = SQLService.getInstance();
        this.lyrics = new HashMap<>();
        this.searchBar = new SearchBar();
    }

    /**
     * Loads all lyrics from the available songs
     *
     * @throws SQLExceptionHandler If there's an error accessing the database
     * @throws IOException         If there's an error reading the lyrics files
     */
    private void getAllLyrics() throws SQLExceptionHandler, IOException {
        List<Song> songs = sqlService.getAllSongsFromUser();
        for (Song song : songs) {
            if (!song.getLyricsPath().isEmpty() && !lyrics.containsKey(song.getLyricsPath())) {
                Lyrics lyric = new Lyrics(song);
                lyric.loadFromLrcFile();
                lyrics.put(song.getTitle(), lyric.getLyricsText());
            }
        }
    }

    /**
     * Searches for songs whose lyrics contain the specified query
     *
     * @param query The search query
     * @return List of songs matching the search criteria
     * @throws SQLExceptionHandler If there's an error accessing the database
     * @throws IOException         If there's an error reading the lyrics files
     */
    private List<Song> lyricsSearch(String query) throws SQLExceptionHandler, IOException {
        List<Song> songs = new ArrayList<>();
        getAllLyrics();
        for (String title : lyrics.keySet()) {
            List<String> lyricLines = lyrics.get(title);
            for (String line : lyricLines) {
                if (line.toLowerCase().contains(query.toLowerCase())) {
                    Song song = sqlService.getSong(sqlService.getSongId(title));
                    songs.add(song);
                    break;
                }
            }
        }
        return songs;
    }

    /**
     * Searches for songs based on a query, including both metadata and lyrics search
     * when appropriate.
     *
     * @param query The search query
     * @return List of songs matching the search criteria
     * @throws SQLExceptionHandler If there's an error accessing the database
     * @throws IOException         If there's an error reading the lyrics files
     */
    private List<Song> searchSongs(String query) throws SQLExceptionHandler, IOException {
        // First search in song metadata (database search)
        List<Song> metadataResults = sqlService.searchSongs(query);
        List<Song> results = new ArrayList<>(metadataResults);

        // If query has more than 2 words, also search in lyrics content
        // This is an optimization to avoid expensive lyrics search for simple queries
        if (query.trim().split("\\s+").length > 2) {
            List<Song> lyricMatches = lyricsSearch(query);

            // Add all songs from lyrics search that aren't already in the result list
            List<Integer> existingSongIds = metadataResults.stream()
                    .map(Song::getId)
                    .collect(Collectors.toList());

            for (Song song : lyricMatches) {
                if (!existingSongIds.contains(song.getId())) {
                    results.add(song);
                }
            }
        }

        return results;
    }

    /**
     * Searches for songs and converts them to SongDTO for display in the view.
     *
     * @param query The search query
     * @return List of SongDTO objects matching the search criteria
     * @throws SQLExceptionHandler  If there's an error accessing the database
     * @throws InvalidSongException If there's an error with song data
     * @throws IOException          If there's an error reading the lyrics files
     */
    public List<SongDTO> searchSongsAsDTO(String query) throws SQLExceptionHandler, InvalidSongException, IOException {
        searchBar.clearSearchedSong();

        List<Song> songs = searchSongs(query);

        if (songs.isEmpty()) {
            return new ArrayList<>();
        }

        searchBar.updateSearchedSong(songs);

        return new ArrayList<>(searchBar.getSearchedSong());
    }
}
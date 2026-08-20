package ulb.services.collections;

import ulb.exceptions.songs.SQLExceptionHandler;
import ulb.models.LibraryModel;
import ulb.models.LibraryModel.MusicFileInfo;
import ulb.services.SongService;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Handles the library of songs.
 * Provides methods to add a song to the library and to retrieve all songs.
 */
public class LibraryService {

    private final SongService songService = new SongService();

    /**
     * Sets all songs in the library.
     * Exceptions:
     * - SQLExceptionHandler: If an error occurs while adding the song to the
     * database.
     * - IOException: If an error occurs while reading the song file.
     */
    public void setAllSongs() throws SQLExceptionHandler, IOException {
        List<File> musicFiles = LibraryModel.getMusicFiles();
        for (File musicFile : musicFiles) {
            setSong(musicFile);
        }
    }

    /**
     * Sets a song in the library.
     * This method adds a new song to the library. It first gets the information
     * about the song from the file, then adds the song to the database.
     * The song is added to user #1, and the cover image is set to a random one.
     * <p>
     * Exceptions:
     * - SQLExceptionHandler: If an error occurs while adding the song to the
     * database.
     * - IOException: If an error occurs while reading the song file.
     *
     * @param file The song file to add.
     */
    public void setSong(File file) throws SQLExceptionHandler, IOException {
        MusicFileInfo musicFile = LibraryModel.getMusicFile(file);
        String randomCoverPath = getRandomCoverPath();
        songService.addNewSong(musicFile.path, musicFile.musicName, musicFile.durationInSeconds,
                musicFile.type, musicFile.artist, "Unknown", "", randomCoverPath, "");

    }

    /**
     * Returns a random cover path for a song.
     */
    private String getRandomCoverPath() {
        int randomNumber = (int) (Math.random() * 8) + 1; // Generate a random number between 1 and 8
        return "src/main/resources/img/no-cover/no-cover" + randomNumber + ".jpg";
    }
}

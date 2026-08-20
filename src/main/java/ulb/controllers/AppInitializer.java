package ulb.controllers;

import ulb.exceptions.songs.SQLExceptionHandler;
import ulb.services.collections.LibraryService;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * AppInitializer is responsible for initializing the application components.
 * It sets up the SQL service, library service, and tag controller, and handles
 * any necessary setup tasks on application start.
 */
public class AppInitializer {
    private final LibraryService libraryService;

    public AppInitializer(LibraryService libraryService) {
        this.libraryService = libraryService;
    }

    public void setAllSongs() throws SQLExceptionHandler, IOException {
        libraryService.setAllSongs();
    }

    /**
     * Sets up the application on first launch.
     * <p>
     * This adds the first user, adds predefined tags, and sets up the library
     * with all songs.
     *
     * @throws SQLExceptionHandler   if a database access error occurs
     * @throws FileNotFoundException if the tag file is not found
     * @throws IOException           if an IO error occurs
     */
    public void setFirstLaunch() throws SQLExceptionHandler, IOException {
        setAllSongs();
    }
}

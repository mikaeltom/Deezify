package ulb.models;

import ulb.dtos.MusicCollectionDTO;
import ulb.exceptions.playlist.InvalidMusicCollectionException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * An abstract class representing a collection of music.
 * Provides methods to manage songs and retrieve the collection's name.
 */
public abstract class MusicCollection implements MusicCollectionDTO {

    protected ArrayList<Song> songs;
    protected String name;

    protected MusicCollection() {
        this.songs = new ArrayList<>();
    }


    /**
     * Retrieves the name of the collection.
     *
     * @return The name of the collection.
     */
    @Override
    public String getName() {
        return name;
    }


    /**
     * Adds a song to the collection.
     * If the song is already in the collection, or if the song is null, an
     * IOException is thrown.
     *
     * @param song The song to add to the collection.
     * @throws IOException If the song is already in the collection, or if the
     *                     song is null.
     */
    public void addSong(Song song) throws IOException {
        if (song == null) {
            throw new IOException("Song is null");
        }
        if (songs.contains(song)) {
            throw new IOException("Song is already in the collection");
        }
        songs.add(song);
    }


    /**
     * Removes a song from the collection.
     * <p>
     * If the specified song is null or not present in the collection, the method
     * does nothing.
     *
     * @param song The song to be removed from the collection.
     */
    public void removeSong(Song song) {
        if (song == null) {
            return;
        }
        if (!songs.contains(song)) {
            return;
        }
        songs.remove(song);
    }


    /**
     * Retrieves all songs in the collection.
     *
     * @return A list of all songs in the collection.
     */
    public List<Song> getSongs() {
        return new ArrayList<>(songs);
    }

    public abstract Song nextSong(Song song) throws InvalidMusicCollectionException;

    public abstract Song previousSong(Song song) throws InvalidMusicCollectionException;

}

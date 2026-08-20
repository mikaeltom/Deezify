package ulb.models;

import ulb.dtos.PlaylistDTO;
import ulb.exceptions.playlist.InvalidMusicCollectionException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a playlist of songs. This class extends the MusicCollection class
 * because it too contains a list of songs.
 */
public class Playlist extends MusicCollection implements PlaylistDTO {
    private final int id;
    private String name;
    private int position;

    public Playlist(int id, String name, List<Song> songs, int position) {
        this.id = id;
        this.name = name;
        this.songs = songs != null ? new ArrayList<>(songs) : new ArrayList<>();
        this.position = position;
    }

    /**
     * Constructor without songs (empty playlist)
     *
     * @param id   same as the main constructor
     * @param name same as the main constructor
     */
    public Playlist(int id, String name) {
        this.id = id;
        this.name = name;
    }

    /**
     * Retrieves the unique identifier of the playlist.
     *
     * @return The identifier of the playlist.
     */
    public int getId() {
        return id;
    }


    /**
     * Retrieves the playlist name.
     *
     * @return Playlist name.
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * Updates the playlist name.
     *
     * @param name New name for the playlist.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Retrieves the next song in the playlist.
     *
     * @param song Current song.
     */
    @Override
    public void addSong(Song song) throws IOException {
        super.addSong(song);
        incrementPosition();
    }

    /**
     * Removes a song from the playlist.
     * <p>
     * If the playlist becomes empty as a result of this operation, the position
     * is reset to 0.
     *
     * @param song The song to remove from the playlist.
     */
    @Override
    public void removeSong(Song song) {
        super.removeSong(song);
        if (songs.isEmpty()) {
            position = 0;
        }
    }

    /**
     * Retrieves the next song in the playlist.
     *
     * @param song Current song.
     * @return Next song, or first song if at the end.
     * If the provided song is not in the playlist, it logs an error message
     * and returns null.
     */
    @Override
    public Song nextSong(Song song) throws InvalidMusicCollectionException {
        if (song == null || !songs.contains(song)) {
            throw new InvalidMusicCollectionException("not_in_playlist_exception",
                    InvalidMusicCollectionException.ErrorType.PLAYLIST);
        }
        int index = songs.indexOf(song);
        return (index == songs.size() - 1) ? songs.getFirst() : songs.get(index + 1);
    }

    /**
     * Retrieves the previous song in the playlist.
     *
     * @param song Current song.
     * @return Previous song, or last song if at the start.
     */
    @Override
    public Song previousSong(Song song) throws InvalidMusicCollectionException {
        if (song == null || !songs.contains(song)) {
            throw new InvalidMusicCollectionException("not_in_playlist_exception",
                    InvalidMusicCollectionException.ErrorType.PLAYLIST);
        }
        int index = songs.indexOf(song);
        return (index > 0) ? songs.get(index - 1) : songs.getLast();
    }

    /**
     * Increments the position of the playlist by one.
     *
     * <p>
     * This method increases the current position of the playlist by one unit.
     * It does not perform any boundary checks or validations, so it is assumed
     * that the position is managed correctly elsewhere in the code.
     * </p>
     */
    public void incrementPosition() {
        int oldPos = this.position;
        this.position = oldPos + 1;
    }

    /**
     * Retrieves the current position in the playlist.
     *
     * @return The current position in the playlist.
     */
    public int getPosition() {
        return position;
    }


}

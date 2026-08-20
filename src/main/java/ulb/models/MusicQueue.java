package ulb.models;

import ulb.dtos.QueueDTO;
import ulb.exceptions.playlist.InvalidMusicCollectionException;
import ulb.utils.I18n;

import java.util.ArrayList;

/**
 * A queue of songs. The queue is a first in first out queue.
 * The first song that was added is the first song to be played.
 * The songs are stored in a list, where the first element is the first song
 * to be played and the last element is the last song to be played.
 * The queue is a circular list, so when the end of the list is reached, the
 * first element is the next song to be played.
 */
public class MusicQueue extends MusicCollection implements QueueDTO {
    private static final String name = "Music Queue";

    public ArrayList<Song> getQueue() {
        return new ArrayList<>(super.songs);
    }

    /**
     * Sets the songs of the queue.
     * This method is used to set a new queue of songs, replacing any existing
     * songs in the queue.
     *
     * @param newQueue The new queue of songs to be set.
     */
    public void setQueue(ArrayList<Song> newQueue) {
        songs = newQueue;
    }

    /**
     * Retrieves the next song in the queue.
     *
     * @param song The song whose next song to retrieve.
     * @return The next song in the queue, or null if the song is not in the queue.
     */
    @Override
    public Song nextSong(Song song) {
        if (!songs.isEmpty()) {
            Song s = songs.getFirst();
            songs.removeFirst();
            return s;
        }
        return null;
    }

    /**
     * Skips to a specific song in the queue
     *
     * @param song song to skip to.
     */
    public void skipTo(Song song) {
        int index = songs.indexOf(song);
        if (index > 0) {
            for (int i = 0; i < index; i++) {
                songs.removeFirst();
            }
        }
    }

    /**
     * Retrieves the title of the first song in the music queue.
     *
     * @return The title of the first song in the queue, or null if the queue is empty.
     */
    public String getFirstTitle() {
        return !songs.isEmpty() ? songs.getFirst().getTitle() : null;
    }

    /**
     * Retrieves the previous song in the queue.
     *
     * @param song The song whose previous song to retrieve.
     * @return The previous song in the queue, or null if the queue is empty.
     * If the song is not in the queue, it logs an error message and returns null.
     * @throws InvalidMusicCollectionException If the queue is empty.
     */
    @Override
    public Song previousSong(Song song) throws InvalidMusicCollectionException {
        if (song == null || songs.isEmpty()) {
            throw new InvalidMusicCollectionException("queue_is_empty_exception",
                    InvalidMusicCollectionException.ErrorType.QUEUE);
        }
        return song;
    }

    /**
     * Clears the music queue by removing all songs from the queue.
     */
    public void clearQueue() {
        songs.clear();
    }

    /**
     * Removes a song from the queue.
     * If the specified song is null or not present in the queue, the method
     * does nothing.
     *
     * @param song The song to be removed from the queue.
     */
    public void deleteSong(Song song) throws InvalidMusicCollectionException {
        if (song != null && songs.contains(song)){
            songs.remove(song);
        } else {
           throw new InvalidMusicCollectionException(I18n.get("song_not_in_queue_exception"),
                    InvalidMusicCollectionException.ErrorType.QUEUE);
        }
    }

    /**
     * Retrieves the name of the queue.
     *
     * @return The name of the queue.
     */
    @Override
    public String getName() {
        return name;
    }
}

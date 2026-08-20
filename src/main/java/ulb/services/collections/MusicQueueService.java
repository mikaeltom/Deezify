package ulb.services.collections;

import ulb.exceptions.playlist.InvalidMusicCollectionException;
import ulb.models.MusicQueue;
import ulb.models.Song;
import ulb.utils.PopupType;
import ulb.views.PopupView;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Service class for managing the music queue operations.
 * Provides methods to add, retrieve, skip, delete, and clear songs in the queue,
 * as well as setting a new queue and retrieving the music queue object.
 */
public class MusicQueueService {
    private final MusicQueue queue = new MusicQueue();

    /**
     * Retrieves the list of songs in the music queue.
     *
     * @return The list of songs in the music queue.
     */
    public ArrayList<Song> getQueue() {
        return queue.getQueue();
    }

    /**
     * Retrieves the music queue object.
     *
     * @return The music queue object.
     */
    public MusicQueue getMusicQueue() {
        return queue;
    }

    /**
     * Adds a song to the music queue.
     *
     * @param song The song to add to the queue.
     * @throws IOException If the song is not valid.
     */
    public void addSong(Song song) throws IOException {
        queue.addSong(song);
    }

    /**
     * Retrieves the title of the next song in the music queue.
     *
     * <p>
     * This method returns the title of the first song in the queue.
     * If the queue is empty, it returns null.
     *
     * @return The title of the next song in the queue, or null if the queue is
     * empty.
     */
    public String getNextSongTitle() {
        return queue.getFirstTitle();
    }

    /**
     * Retrieves the next song in the music queue.
     *
     * <p>
     * This method returns the song that follows the given song in the queue.
     * If the queue is empty or the provided song is not in the queue, it
     * returns null.
     * </p>
     *
     * @param song The current song whose successor is to be retrieved.
     * @return The next song in the queue, or null if the queue is empty
     * or the song is not in the queue.
     */
    public Song getNext(Song song) {
        return queue.nextSong(song);
    }

    /**
     * Skips to the given song in the music queue.
     * <p>
     * This method calls the skip to method on the music queue.
     * </p>
     *
     * @param song The song to skip to in the queue.
     */
    public void skipTo(Song song) {
        queue.skipTo(song);
    }

    /**
     * Clears the music queue.
     * <p>
     * This method calls the clear queue method on the music queue.
     * </p>
     */
    public void clearQueue() {
        queue.clearQueue();
    }

    /**
     * Deletes the given song from the music queue.
     * <p>
     * This method calls the delete song method on the music queue.
     * </p>
     *
     * @param song The song to delete from the queue.
     */
    public void deleteSong(Song song) {
        try {
            queue.deleteSong(song);
            new PopupView("queue_import_title", "queue_delete", PopupType.SUCCESS);
        } catch (InvalidMusicCollectionException e) {
            new PopupView("error_remove_queue", e.getMessage(), PopupType.ERROR);
        }
    }

    /**
     * Sets the music queue to the provided list of songs.
     *
     * @param newQueue The new list of songs to set in the music queue.
     */
    public void setNewQueue(ArrayList<Song> newQueue) {
        queue.setQueue(newQueue);
    }
}

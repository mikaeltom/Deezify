package ulb.controllers.playboxes;

import ulb.models.Song;

/**
 * The NowPlayingUpdateHandler class is responsible for updating the now playing box
 * with the currently playing song and the next song in the queue.
 * It provides methods to set the collection controller access and update the now playing box.
 */
public class NowPlayingUpdateHandler {
    private NowPlayingBoxControllerAccess nowPlayingBoxControllerAccess;
    private CollectionControllerAccess collectionControllerAccess;

    /**
     * Sets the collection controller access for the now playing update handler.
     *
     * @param collectionControllerAccess The collection controller access to be set.
     */
    public void setCollectionControllerAccess(CollectionControllerAccess collectionControllerAccess) {
        this.collectionControllerAccess = collectionControllerAccess;
    }

    /**
     * Updates the now playing box with the currently playing song and the next song in the queue.
     *
     * @param song The currently playing song to be displayed in the now playing box.
     */
    public void updateNowPlayingBox(Song song) {
        nowPlayingBoxControllerAccess.updateSongPlayingBox(song, collectionControllerAccess.getNextSongTitleQueue());
    }

    /**
     * Sets the now playing box controller access for the now playing update handler.
     *
     * @param nowPlayingBoxController The now playing box controller to be set.
     */
    public void setNowPlayingBoxControllerAccess(NowPlayingBoxController nowPlayingBoxController) {
        this.nowPlayingBoxControllerAccess = nowPlayingBoxController;
    }

    /**
     * Interface for accessing the collection controller.
     * Provides a method to get the title of the next song in the queue.
     */
    public interface CollectionControllerAccess {
        String getNextSongTitleQueue();
    }

    /**
     * Interface for accessing the now playing box controller.
     * Provides a method to update the now playing box with the currently playing song and the next song title.
     */
    public interface NowPlayingBoxControllerAccess {
        void updateSongPlayingBox(Song song, String nextSongTitle);
    }
}

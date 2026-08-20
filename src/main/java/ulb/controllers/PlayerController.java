package ulb.controllers;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import ulb.controllers.playboxes.NowPlayingBoxController;
import ulb.controllers.playboxes.NowPlayingUpdateHandler;
import ulb.controllers.searches.SearchBarCellController;
import ulb.controllers.songs.SongBoxController;
import ulb.models.Song;
import ulb.utils.I18n;
import ulb.utils.PopupType;
import ulb.views.PlayerView;
import ulb.views.PopupView;
import java.io.File;
import java.nio.file.Paths;


/**
 * Handles the playback of songs in the music library.
 * This class is a singleton, i.e. there can only be one instance of this class.
 * It provides methods to play a song, pause playback, and seek to a specific
 * time in the currently playing song.
 * It also provides methods to get the currently playing song, and the
 * MediaPlayer object for external use.
 * The MediaPlayer object is always in the PLAYING state when a song is loaded,
 * even if the user has not started playback yet.
 * The music library is not managed by this class; see MusicLibrary for more
 * information.
 */
public class PlayerController implements PlayerView.PlayerListener,
        SearchBarCellController.PlayerViewControllerAccess,
        SongBoxController.PlayerViewControllerAccess, NowPlayingBoxController.NowPayingBoxControllerListener {
    private final BooleanProperty songFinished = new SimpleBooleanProperty(false);
    private final NowPlayingBoxController nowPlayingBoxController = new NowPlayingBoxController();
    private MediaPlayer mediaPlayer;
    private Song currentSong;
    private Double currentVolume = 50.0;
    private PlayerView playerView;
    private Parent playerViewParent;
    private CollectionControllerAccess collectionControllerAccess;
    private BigCoverManagerAccess bigCoverManagerAccess;

    public PlayerController(NowPlayingUpdateHandler nowPlayingUpdateHandler) {
        nowPlayingUpdateHandler.setNowPlayingBoxControllerAccess(this.nowPlayingBoxController);
        nowPlayingBoxController.setNowPayingBoxControllerListener(this);
        nowPlayingBoxController.show();
    }

    public void setCollectionControllerAccess(CollectionControllerAccess collectionControllerAccess) {
        this.collectionControllerAccess = collectionControllerAccess;
    }

    public void setBigCoverManagerAccess(BigCoverManagerAccess bigCoverManagerAccess) {
        this.bigCoverManagerAccess = bigCoverManagerAccess;
    }

    public void updateNowPlayingBox() {
        nowPlayingBoxController.show();
    }

    public void changeBigCoverView() {
        nowPlayingBoxController.changeIsBigCoverDisplayed();
    }

    /**
     * Loads the PlayerView view and displays it on the screen.
     * It also sets the PlayerListener to this class.
     * <p>
     * Exceptions:
     * - IOException: If there is an error loading the PlayerView.fxml file.
     */
    public void show() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/ulb/views/PlayerView.fxml"));
            playerViewParent = loader.load();
            this.playerView = loader.getController();
            this.playerView.setListener(this);
        } catch (Exception e) {
            new PopupView("loading_error_title", I18n.get("error_loading_player"), PopupType.ERROR);
        }
    }

    /**
     * Returns the parent node of the player view.
     */
    public Parent getPlayerViewParent() {
        return playerViewParent;
    }

    /**
     * Updates the progress bar of the player view with the current time and total time of the media player.
     * If the media player is null or the total duration is null, the method does nothing.
     * The progress is calculated as follows: (currentTime.toSeconds() / totalTime.toSeconds()) * 100.
     * The  method of the {@link NowPlayingBoxController} is also called with the media player.
     */
    public void notifyUpdateProgressBar() {
        if (mediaPlayer != null && mediaPlayer.getTotalDuration() != null) {
            Duration currentTime = mediaPlayer.getCurrentTime();
            Duration totalTime = mediaPlayer.getTotalDuration();
            double progress = (currentTime.toSeconds() / totalTime.toSeconds()) * 100;
            playerView.updateProgressBar(currentTime, totalTime, progress);
            nowPlayingBoxController.setLyricsMediaPlayer(mediaPlayer);
        }
    }

    /**
     * Returns the parent node of the now playing box view.
     */
    public Parent getNowPlayingBoxViewParent() {
        return nowPlayingBoxController.getPlayerViewParent();
    }

    /**
     * Sets up the time tracking for the media player.
     * This method is necessary to trigger the update of the progress bar in the player view
     * when the media player is ready and when the current time of the media player changes.
     * The method adds a listener to the current time property of the media player and
     * sets the on ready event handler of the media player.
     * If the media player is null, the method does nothing.
     */
    public void setupTimeTracking() {
        if (mediaPlayer != null) {
            mediaPlayer.setOnReady(this::notifyUpdateProgressBar);
            mediaPlayer.currentTimeProperty().addListener(event -> notifyUpdateProgressBar());
        }
    }

    /**
     * Sets the song that is currently playing.
     * This method is useful when some other component needs to change the song that
     * is currently playing.
     *
     * @param currentSong The song to set as the currently playing song.
     */
    private void setCurrentSong(Song currentSong) {
        this.currentSong = currentSong;
    }

    /**
     * Load a song into the media player.
     *
     * @param song The song to play. If null or the path is null, the song is not
     *             loaded. If the file does not exist, the song is not loaded.
     */
    public void loadSong(Song song) {
        if (song == null || song.getPath() == null) {
            new PopupView("error_loading_song_title", I18n.get("error_invalid_song"), PopupType.ERROR);
            return;
        }
        File file = new File(Paths.get(song.getPath()).toString());
        if (!file.exists()) {
            new PopupView("error_loading_song_title", I18n.get("error_loading_song") + song.getPath(), PopupType.ERROR);
            return;
        }
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
        mediaPlayer = new MediaPlayer(new Media(file.toURI().toString()));
        nowPlayingBoxController.setLyricsMediaPlayer(mediaPlayer);
        setCurrentSong(song);
        setVolume(currentVolume);
        playPause();
        setupTimeTracking();
        nowPlayingBoxController.updateSongPlayingBox(song, collectionControllerAccess.getNextSongTitleQueue());
    }

    /**
     * Play the currently loaded song.
     * If a song is not currently loaded, or if the song is already playing, this
     * method does nothing.
     * If the song is paused, this method will resume playback.
     */
    private void play() {
        if (mediaPlayer != null && !mediaPlayer.getStatus().equals(MediaPlayer.Status.PLAYING)) {
            songFinished.set(false);
            mediaPlayer.play();
            nowPlayingBoxController.setLyricsMediaPlayer(mediaPlayer);
        }

    }

    /**
     * Pause the currently playing song.
     * If a song is not currently playing, this method does nothing.
     * If the song is already paused, this method does nothing.
     */
    public void pause() {
        if (mediaPlayer != null) {
            mediaPlayer.pause();
            nowPlayingBoxController.setLyricsMediaPlayer(mediaPlayer);
        }
    }

    /**
     * Returns true if the currently loaded song is playing, false otherwise.
     */
    private boolean isPlaying() {
        return mediaPlayer != null && mediaPlayer.getStatus().equals(MediaPlayer.Status.PLAYING);
    }

    /**
     * Toggles the playback state of the current song.
     *
     * <p>
     * If the currently loaded song is playing, this method pauses it.
     * If the song is paused or stopped, this method starts playing it.
     * This method does nothing if no song is loaded.
     * </p>
     */
    @Override
    public void playPause() {
        if (currentSong != null) {
            if (isPlaying()) {
                pause();
            } else {
                play();
            }
            playerView.updatePlayPauseButton(isPlaying());
        }
    }

    /**
     * Sets the volume of the media player.
     *
     * <p>
     * This method sets the volume of the media player to the given volume. The
     * volume
     * should be a double between 0 and 100, inclusive. 0 is mute and 100 is full
     * volume.
     * </p>
     *
     * @param volume The volume to set the media player to, between 0 and 100,
     *               inclusive.
     */
    @Override
    public void setVolume(double volume) {
        currentVolume = volume;
        if (mediaPlayer != null) {
            mediaPlayer.setVolume(volume / 100);
        }
    }

    /**
     * Retrieves the next song in the music library based on the current collection.
     *
     * <p>
     * This method retrieves the next song in the music library based on the current
     * collection.
     * If the current collection is a queue, it attempts to retrieve the last song
     * that was
     * added to the queue. If the current collection is not a queue, it retrieves
     * the next
     * song in the current collection.
     * </p>
     *
     * @param choice If true, the previous song is retrieved. Otherwise, the
     *               next song is retrieved.
     * @return The next song in the music library, or null if the current collection
     * is empty.
     */
    private Song chooseNextSong(Choice choice) {
        boolean isPrevious = (choice == Choice.PREVIOUS);
        return collectionControllerAccess.playerChooseNextSong(isPrevious, currentSong);
    }

    /**
     * Changes the currently playing song in the music library.
     *
     * @param choice A String specifying the action to perform, either "previous"
     *               or "next", to navigate through the music library.
     *               throws UserError if the music library is empty and no song is available to
     *               play.
     */
    @Override
    public void changeMusic(Choice choice) {
        Song song = chooseNextSong(choice);
        loadSong(song);
        playPause();
        if (nowPlayingBoxController.isBigCoverDisplayed()) {
            bigCoverManagerAccess.showBigCover(song);
        }

    }

    /**
     * Seeks the media player to a specific percentage of the total duration.
     * <p>
     * This method takes a double between 0 and 100, inclusive, which specifies
     * the percentage of the total duration to seek to. For example, 50 would
     * seek to the middle of the song, and 75 would seek to 75% of the way
     * through the song.
     * </p>
     * <p>
     * If the media player is null, or if the total duration is null, this
     * method does nothing.
     * </p>
     *
     * @param percentage The percentage of the total duration to seek to, as a
     *                   double between 0 and 100, inclusive.
     */
    @Override
    public void seekToPercentage(double percentage) {
        if (mediaPlayer != null && mediaPlayer.getTotalDuration() != null) {
            Duration currentDuration = mediaPlayer.getTotalDuration().multiply(percentage / 100.0);
            mediaPlayer.seek(currentDuration);
            nowPlayingBoxController.forceLyricsUpdate();
        }
    }

    public void updateNextSongLabel(String label) {
        nowPlayingBoxController.setNextSongLabel(label);
    }

    @Override
    public void loadAndPlay(Song song) {
        loadSong(song);
        play();
    }

    @Override
    public void changeBigCoverView(Song song) {
        changeBigCoverView();
        bigCoverManagerAccess.showBigCover(song);
    }

    @Override
    public void bigCoverViewClose() {
        bigCoverManagerAccess.closeBigCover();
    }

    public enum Choice {
        NEXT, PREVIOUS
    }

    public interface CollectionControllerAccess {
        String getNextSongTitleQueue();

        Song playerChooseNextSong(boolean isPrevious, Song currentSong);
    }

    public interface BigCoverManagerAccess {
        void showBigCover(Song song);

        void closeBigCover();
    }
}

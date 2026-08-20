package ulb.views;

import javafx.fxml.FXML;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.util.Duration;
import ulb.controllers.PlayerController.Choice;

import java.util.Objects;

/**
 * View for the player panel. Contains buttons for playing, pausing, next and
 * previous song, a volume slider and a progress bar.
 * Also contains the current time and total time of the current song.
 */

public class PlayerView {
    private final Image playImage = new Image(Objects.requireNonNull(getClass().getResource("/images/play-not-center.png")).toExternalForm());
    private final Image pauseImage = new Image(Objects.requireNonNull(getClass().getResource("/images/pause-icon.png")).toExternalForm());
    protected PlayerListener listener;
    private boolean isProgressDrag = false;
    @FXML
    private Slider volumeSlider;
    @FXML
    private Slider progressBar;
    @FXML
    private Text currentSongTime;
    @FXML
    private Text totalSongTime;
    @FXML
    private ImageView nextButtonIcon;

    /**
     * Initializes the player view by setting up the volume slider
     * and initializing event handlers for user interactions.
     * This method prepares the view for user input, allowing
     * volume adjustments and progress bar interactions.
     */
    public void initialize() {
        setupVolumeSlider();
        initializeEventHandlers();
    }

    public void setListener(PlayerListener listener) {
        this.listener = listener;
    }

    /**
     * Sets the initial value of the volume slider to 50 and
     * sets up a listener to inform the PlayerListener when the
     * volume value changes.
     * obs The observable value of the volume slider.
     * oldValue The old value of the volume slider.
     * newValue The new value of the volume slider.
     */
    private void setupVolumeSlider() {
        volumeSlider.setValue(50);
        volumeSlider.valueProperty().addListener((obs, oldValue, newValue) ->
                listener.setVolume(newValue.doubleValue()));
    }

    /**
     * Sets up the progress bar to respond to mouse presses and releases,
     * seeking to the current percentage value of the progress bar when the
     * mouse is released.
     */
    private void initializeEventHandlers() {
        progressBar.setOnMousePressed(event -> isProgressDrag = true);
        progressBar.setOnMouseReleased(event -> {
            listener.seekToPercentage(progressBar.getValue());
            isProgressDrag = false;
        });
    }

    /**
     * Toggles between play and pause states for the current track when invoked.
     */
    @FXML
    private void setUpPauseButton() {
        listener.playPause();
    }


    /**
     * Sets up the previous button to change to the previous track when clicked.
     */
    @FXML
    private void setUpPreviousButton() {
        listener.changeMusic(Choice.PREVIOUS);
    }

    /**
     * Sets up the next button to change to the next track when clicked.
     */
    @FXML
    private void setUpNextButton() {
        listener.changeMusic(Choice.NEXT);
    }


    /**
     * Formats the given duration into a string representation.
     *
     * <p>
     * This method takes a Duration object and converts it into a string
     * formatted as "minutes:seconds". The minutes and seconds are extracted
     * from the duration and formatted into a two-digit second format.
     * </p>
     *
     * @param duration The duration to format must not be null.
     * @return A string representation of the duration in the "minutes:seconds" format.
     */
    private String formatTime(Duration duration) {
        int minutes = (int) duration.toMinutes();
        int seconds = (int) duration.toSeconds() % 60;
        return String.format("%d:%02d", minutes, seconds);
    }


    /**
     * Updates the progress bar and song time displays with the current playback progress.
     *
     * @param currentTime The current playback time of the song.
     * @param totalTime   The total duration of the song.
     * @param progress    The progress percentage of the song playback.
     */
    public void updateProgressBar(Duration currentTime, Duration totalTime, Double progress) {
        if (!isProgressDrag) {
            progressBar.setValue(progress);
            currentSongTime.setText(formatTime(currentTime));
            totalSongTime.setText(formatTime(totalTime));
        }
    }

    /**
     * Updates the play/pause button icon depending on the given state.
     *
     * @param isPlaying True if the player is currently playing, false otherwise.
     */
    public void updatePlayPauseButton(Boolean isPlaying) {
        nextButtonIcon.setImage(isPlaying ? playImage : pauseImage);
    }

    public interface PlayerListener {
        void seekToPercentage(double percentage);

        void setVolume(double volume);

        void playPause();

        void changeMusic(Choice choice);
    }
}
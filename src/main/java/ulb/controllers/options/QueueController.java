package ulb.controllers.options;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ulb.models.Song;
import ulb.utils.I18n;
import ulb.utils.PopupType;
import ulb.views.PopupView;
import ulb.views.options.QueueView;

import java.io.IOException;

/**
 * Controller responsible for handling song queue operations.
 * This controller manages the logic for creating,deleting,
 */
public class QueueController implements QueueView.QueueViewListener {
    private Song currentSong;
    private OptionListener optionListener;

    public void setListener(OptionListener listener) {
        this.optionListener = listener;
    }

    /**
     * Sets the song currently being edited.
     *
     * @param song The song to be managed.
     */
    public void setSong(Song song) {
        this.currentSong = song;
    }

    /**
     * Deletes the cover image for the currently managed song.
     */
    @Override
    public void deleteFromQueue() {
        if (currentSong == null)
            return;
        optionListener.deleteFromQueue(currentSong);
        currentSong.setImagePath(null);
    }

    /**
     * Handles the closing of the cover window.
     * This method could include logic to return to the main view.
     */
    @Override
    public void closeQueueWindow() {
        optionListener.returnToSongOptionView(this.currentSong);
    }

    /**
     * Adds the currently managed song to the queue.
     * This method delegates the operation to the option listener
     * and displays a success message.
     */
    @Override
    public void addToQueue(){
        optionListener.addToQueue(currentSong);
    }

    /**
     * Displays the QueueView for editing song queue.
     * <p>
     * This method loads the QueueView.fxml layout and initializes the
     * queue view controller. It sets the current song and displays the
     * queue view in the provided stage.
     *
     * @param song  The Song object to be displayed in the queue view.
     * @param stage The Stage where the queue view will be displayed.
     */
    public void show(Song song, Stage stage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/ulb/views/QueueView.fxml"),
                    I18n.getBundle());
            Parent root = fxmlLoader.load();
            QueueView queueView = fxmlLoader.getController();
            queueView.setListener(this);
            setSong(song);
            Scene scene = new Scene(root);
            stage.setScene(scene);
        } catch (IOException e) {
            new PopupView("loading_error_title", "error_loading_queue_view", PopupType.ERROR);
        }
    }
}

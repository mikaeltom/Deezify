package ulb.views.options;

/**
 * A window that displays the songs that are currently in the queue.
 * The window allows the user to close the queue window and remove the songs
 * from the queue.
 */
public class QueueView {
    private QueueViewListener queueViewListener;

    /**
     * Handles the action when the import queue button is clicked.
     * Triggers the addition of the current song to the queue.
     */
    public void handleQueueImportButtonClick() {
        queueViewListener.addToQueue();
    }

    /**
     * Handles the action when the delete queue button is clicked.
     * Triggers the removal of the current song from the queue.
     */
    public void handleDeleteQueueButtonClick() {
        queueViewListener.deleteFromQueue();
    }

    /**
     * Handles the action when the close queue button is clicked.
     * Closes the queue window.
     */
    public void handleCloseQueueButtonClick() {
        queueViewListener.closeQueueWindow();
    }

    /**
     * Sets the listener for the queue view.
     * The listener is informed when the user interacts with the queue view.
     */
    public void setListener(QueueViewListener queueViewListener) {
        this.queueViewListener = queueViewListener;
    }

    public interface QueueViewListener {
        void closeQueueWindow();

        void addToQueue();

        void deleteFromQueue();

    }
}

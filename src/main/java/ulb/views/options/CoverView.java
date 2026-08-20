package ulb.views.options;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;


/**
 * FXML Controller for the cover image editor.
 * This class handles user interactions and delegates logic to CoverController.
 */
public class CoverView {
    private CoverViewListener coverViewListener;

    @FXML
    private Button importCoverButton;


    /**
     * Sets the listener for this cover view.
     * The listener is informed when the user interacts with the cover view.
     */
    public void setListener(CoverViewListener coverViewListener) {
        this.coverViewListener = coverViewListener;
    }

    /**
     * Handles the import cover button click event.
     * Calls CoverController to perform the logic.
     */
    @FXML
    public void handleCoverImportButtonClick() {
        coverViewListener.importCover((Stage) importCoverButton.getScene().getWindow());
    }

    /**
     * Handles the delete cover button click event.
     * Calls CoverController to delete the cover.
     */
    @FXML
    public void handleDeleteCoverButtonClick() {
        coverViewListener.deleteCover();
    }

    /**
     * Handles the close cover button click event.
     * Calls CoverController to handle the closing logic.
     */
    @FXML
    public void handleCloseCoverButtonClick() {
        coverViewListener.closeCoverWindow();
    }

    public interface CoverViewListener {
        void closeCoverWindow();

        void importCover(Stage stage);

        void deleteCover();
    }
}

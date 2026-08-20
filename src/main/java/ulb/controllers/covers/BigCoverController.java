package ulb.controllers.covers;

import javafx.beans.value.ObservableValue;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.VBox;
import ulb.models.Song;
import ulb.utils.I18n;
import ulb.utils.PopupType;
import ulb.views.BigCoverView;
import ulb.views.PopupView;

import java.io.IOException;

/**
 * The BigCoverController class is responsible for managing the
 * BigCoverView, which displays a large cover image of the currently
 * playing song. It handles user interactions with the cover image.
 */
public class BigCoverController implements BigCoverView.BigCoverListener {
    private Parent bigCoverViewParent;
    private VBox parent;
    private Song currentSong;
    private BigCoverManagerAccess bigCoverManagerAccess;

    public void setCurrentSong(Song currentSong) {
        this.currentSong = currentSong;
    }

    public void setBigCoverManagerAccess(BigCoverManagerAccess bigCoverManagerAccess) {
        this.bigCoverManagerAccess = bigCoverManagerAccess;
    }

    /**
     * Loads the BigCoverView view and displays it on the screen.
     * It also sets the song to display in the custom now playing box.
     * <p>
     * Exceptions:
     * - IOException: If there is an error loading the BigCover.fxml file.
     */
    public void show(VBox parent) {
        try {
            BigCoverView bigCoverView;
            this.parent = parent;
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ulb/views/BigCover.fxml"), I18n.getBundle());
            bigCoverViewParent = loader.load();
            bigCoverView = loader.getController();
            bigCoverView.setBigCoverListener(this);
            bigCoverView.setSong(currentSong);
        } catch (IOException e) {
            new PopupView("loading_error_title", I18n.get("error_loading_bigcover"), PopupType.ERROR);
        }
    }

    public ObservableValue<? extends Number> getRestrainedHeight() {
        return parent.heightProperty();
    }

    @Override
    public void handleClickCover() {
        bigCoverManagerAccess.closeBigCover();
    }

    public Parent getBigCoverViewParent() {
        return this.bigCoverViewParent;
    }

    public interface BigCoverManagerAccess {
        void closeBigCover();
    }
}

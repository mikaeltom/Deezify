package ulb.controllers.covers;

import javafx.scene.layout.VBox;
import ulb.controllers.CollectionController;
import ulb.controllers.PlayerController;
import ulb.models.Song;
import ulb.views.MainView;

/**
 * This class is responsible for managing the display of the big cover view
 * in the application. It handles the interaction between the big cover
 * controller, player view controller, collection controller, and main view.
 */
public class BigCoverManager
        implements BigCoverController.BigCoverManagerAccess, PlayerController.BigCoverManagerAccess {
    private final BigCoverController bigCoverController;
    private final PlayerController playerViewController;
    private final CollectionController collectionController;
    private MainView mainView;

    public BigCoverManager(BigCoverController bigCoverController,
            PlayerController playerViewController,
            CollectionController collectionController) {
        this.bigCoverController = bigCoverController;
        this.playerViewController = playerViewController;
        this.collectionController = collectionController;
    }

    /**
     * Displays the big cover view for the given song.
     */
    @Override
    public void showBigCover(Song song) {
        VBox libraryContainer = mainView.getLibraryContainer();
        libraryContainer.getChildren().clear();
        bigCoverController.setCurrentSong(song);
        bigCoverController.show(libraryContainer);
        libraryContainer.getChildren().add(bigCoverController.getBigCoverViewParent());
    }

    /**
     * Closes the big cover view and returns to the main library view.
     */
    @Override
    public void closeBigCover() {
        playerViewController.changeBigCoverView();
        VBox libraryContainer = mainView.getLibraryContainer();
        libraryContainer.getChildren().clear();
        collectionController.updateCurrentCollection();
        libraryContainer.getChildren().add(collectionController.getSongListViewChildren());
    }

    public void setMainView(MainView mainView) {
        this.mainView = mainView;
    }
}

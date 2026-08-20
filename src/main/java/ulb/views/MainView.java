package ulb.views;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;


/**
 * The HomeView class is the main view of the application. It contains the
 * navigation buttons, the search bar, the song library, and the player view.
 * It is responsible for loading all the views and initializing the
 * corresponding controllers.
 */
public class MainView {
    @FXML
    public HBox searchBar;
    @FXML
    private StackPane playerAnchor;
    @FXML
    private StackPane collectionsAnchor;
    @FXML
    private Button addMusicButton;
    @FXML
    private VBox libraryContainer;
    @FXML
    private VBox nowPlayingContainer;

    @FXML
    private Button settingsButton;

    @FXML
    private Button accountSettingsButton;

    private MainViewListener mainViewListener;

    public void setListener(MainViewListener listener) {
        this.mainViewListener = listener;
    }

    /**
     * Initializes the MainView by setting up the button actions.
     * This method is automatically called by JavaFX after the FXML file has been loaded.
     * It sets up the action for the add music button, the account settings button,
     * and the settings button, and notifies the listener of the events.
     */
    public void initialize() {
        initializeAddMusicButton();
        initializeAccountSettingsButton();
        initializeSettingsButton();
    }

    /**
     * Sets up the action handler for the "Add Music" button.
     * When the button is clicked, it triggers the addMusicButtonClicked
     * method on the MainViewListener to handle the action.
     */
    private void initializeAddMusicButton() {
        addMusicButton.setOnAction(
                event -> mainViewListener.addMusicButtonClicked());
    }

    /**
     * Sets up the action handler for the "Account Settings" button.
     * When the button is clicked, it triggers the onAccountSettingsClicked
     * method on the MainViewListener to handle the action.
     */
    private void initializeAccountSettingsButton() {
        accountSettingsButton.setOnAction(
                event -> mainViewListener.onAccountSettingsClicked()
        );
    }

    /**
     * Sets up the action handler for the "Settings" button.
     * When the button is clicked, it triggers the onSettingClicked
     * method on the MainViewListener to handle the action.
     */
    private void initializeSettingsButton() {
        settingsButton.setOnAction(
                event -> mainViewListener.onSettingClicked());
    }

    public StackPane getCollectionsAnchor() {
        return collectionsAnchor;
    }

    public VBox getLibraryContainer() {
        return libraryContainer;
    }

    public StackPane getPlayerAnchor() {
        return playerAnchor;
    }

    public VBox getNowPlayingContainer() {
        return nowPlayingContainer;
    }

    public HBox getSearchBar() {
        return searchBar;
    }

    public interface MainViewListener {
        void addMusicButtonClicked();

        void onSettingClicked();

        void onAccountSettingsClicked();
    }
}

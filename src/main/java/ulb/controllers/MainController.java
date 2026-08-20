package ulb.controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import ulb.controllers.accounts.AccountSettingsController;
import ulb.controllers.accounts.ChooseLanguageController;
import ulb.controllers.covers.BigCoverController;
import ulb.controllers.covers.BigCoverManager;
import ulb.controllers.playboxes.NowPlayingUpdateHandler;
import ulb.controllers.options.MusicUploader;
import ulb.controllers.options.TagController;
import ulb.controllers.searches.SearchBarCellController;
import ulb.controllers.searches.SearchBarController;
import ulb.controllers.songs.SongOptionController;
import ulb.exceptions.songs.SQLExceptionHandler;
import ulb.config.ConfigManager;
import ulb.models.PredefinedTagManager;
import ulb.services.*;
import ulb.services.collections.MusicQueueService;
import ulb.services.collections.LibraryService;
import ulb.services.collections.PlaylistService;
import ulb.utils.I18n;
import ulb.utils.PopupType;
import ulb.views.MainView;
import ulb.views.PopupView;
import java.io.IOException;

/**
 * The main controller of the application. Manages all the other controllers and
 * services.
 */
public class MainController implements MainControllerAccess, MainView.MainViewListener {
    private final LibraryService libraryService = new LibraryService();
    private final MusicUploader musicUploader = new MusicUploader(libraryService);
    private final MusicQueueService musicQueueService = new MusicQueueService();
    private final PlaylistService playlistService = new PlaylistService();
    private BigCoverController bigCoverController;
    private final TagController tagController;
    private final NowPlayingUpdateHandler nowPlayingUpdateHandler = new NowPlayingUpdateHandler();
    private SongOptionController songOptionController;
    private CollectionController collectionController;
    private PlayerController playerViewController;
    private BannedWordService bannedWordManager;
    private PredefinedTagManager predefinedTagManager;
    private SongService songService;
    private SearchBarController searchBarController;
    private BigCoverManager bigCoverManager;
    private Stage stage;
    private MainListener listener;

    public MainController(Stage stage) throws SQLExceptionHandler, IOException {
        setUpBannedWordManager();
        setUpPredefinedTagManager();
        this.tagController = new TagController(this.bannedWordManager, this.predefinedTagManager);

        ConfigManager configManager = new ConfigManager();
        if (configManager.isFirstLaunch()) {
            initialize();
            configManager.setFirstLaunch(false);
        }
        launch(stage);
    }

    /**
     * Establishes communication channels between controllers at the same level.
     * Sets collection controller access for the player view controller, song option
     * controller,
     * and now playing update handler, allowing them to interact with the collection
     * controller.
     **/

    public void setSameLevelCommunication() {
        this.playerViewController.setCollectionControllerAccess(this.collectionController);
        this.songOptionController.setCollectionControllerAccess(this.collectionController);
        this.nowPlayingUpdateHandler.setCollectionControllerAccess(this.collectionController);
    }

    /**
     * Sets up the main view components and services.
     * 
     * @param stage the primary stage
     */
    public void launch(Stage stage) {
        this.stage = stage;
        setupPlayerViewController();
        this.songService = new SongService();
        setupSongOptionController();
        setupCollectionController();
        setSameLevelCommunication();
        setupSearchBarController();
        setupBigCoverController();
        bigCoverManager = new BigCoverManager(bigCoverController, playerViewController,
                collectionController);
        this.playerViewController.setBigCoverManagerAccess(bigCoverManager);
        this.bigCoverController.setBigCoverManagerAccess(bigCoverManager);
    }

    /**
     * Initializes the application components on the first launch.
     */
    public void initialize() throws SQLExceptionHandler, IOException {
        AppInitializer appInitializer = new AppInitializer(libraryService);
        appInitializer.setFirstLaunch();
    }

    public void setListener(MainListener listener) {
        this.listener = listener;
    }

    /**
     * Initializes the predefined tag manager.
     */
    public void setUpPredefinedTagManager() {
        try {
            this.predefinedTagManager = new PredefinedTagManager();
        } catch (Exception e) {
            new PopupView(
                    "Predefined Tags Error",
                    "Could not load predefined tags. Please check 'maven-config/predefined-tag.json'.\n"
                            + e.getMessage(),
                    PopupType.ERROR);
            this.predefinedTagManager = null;
        }
    }

    /**
     * Initializes the banned word manager.
     */
    public void setUpBannedWordManager() {
        try {
            this.bannedWordManager = new BannedWordService();
        } catch (Exception e) {
            new PopupView(
                    "Banned Tags Error",
                    "Could not load banned tags list. Please check 'maven-config/banned-tag.json'.\n" + e.getMessage(),
                    PopupType.ERROR);
            this.bannedWordManager = null;
        }
    }

    /**
     * Initializes the collection controller.
     */
    public void setupCollectionController() {
        this.collectionController = new CollectionController(musicQueueService,
                playlistService,
                songOptionController, playerViewController);
        this.collectionController.setMainControllerAccess(this);
    }

    private void setupBigCoverController() {
        bigCoverController = new BigCoverController();
    }

    /**
     * Initializes the song option controller.
     */
    public void setupSongOptionController() {
        this.songOptionController = new SongOptionController(musicUploader, songService, tagController,
                nowPlayingUpdateHandler,
                bannedWordManager);

        this.songOptionController.setMainControllerAccess(this);
    }

    /**
     * Initializes the player view controller.
     */
    private void setupPlayerViewController() {
        this.playerViewController = new PlayerController(this.nowPlayingUpdateHandler);
        this.playerViewController.show();
    }

    /**
     * Initializes the search bar controller.
     */
    private void setupSearchBarController() {
        this.searchBarController = new SearchBarController();
        searchBarController.setListener(song -> new SearchBarCellController(this,
                collectionController, songOptionController, playerViewController).loadSearchBarCell(song));
        try {
            this.searchBarController.show();
        } catch (IOException e) {
            new PopupView("loading_error_title", I18n.get("error_loading_searchbar"), PopupType.ERROR);
        }

    }

    /**
     * Shows the main view which contains the collections, song list, player, and
     * search bar.
     */
    public void show() {
        MainView mainView;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ulb/views/MainView.fxml"), I18n.getBundle());
            loader.load();
            mainView = loader.getController();
            mainView.initialize();
            mainView.setListener(this);
            bigCoverManager.setMainView(mainView);
            collectionController.show();
            Parent root = loader.getRoot();
            this.stage.setScene(new Scene(root));
            this.stage.setMinHeight(600);
            this.stage.setMinWidth(800);
            this.stage.show();
            StackPane collectionPane = mainView.getCollectionsAnchor();
            VBox libraryContainer = mainView.getLibraryContainer();
            StackPane playerPane = mainView.getPlayerAnchor();
            VBox nowPlayingContainer = mainView.getNowPlayingContainer();
            collectionPane.getChildren().add(collectionController.getCollectionsViewParent());
            libraryContainer.getChildren().clear();
            libraryContainer.getChildren().add(collectionController.getSongListViewChildren());
            playerPane.getChildren().add(playerViewController.getPlayerViewParent());
            nowPlayingContainer.getChildren().add(playerViewController.getNowPlayingBoxViewParent());
            HBox searchBarContainer = mainView.getSearchBar();
            searchBarContainer.getChildren().add(searchBarController.getSearchBarView());
        } catch (IOException | SQLExceptionHandler e) {
            new PopupView("loading_error_title", I18n.get("error_loading_main"), PopupType.ERROR);
        }
    }

    /**
     * Called when the user clicks on the "Add Music" button in the main window.
     */
    @Override
    public void addMusicButtonClicked() {
        try {
            musicUploader.add(stage); // add the music file
            updateCurrentCollection(); // refresh all the library
        } catch (SQLExceptionHandler | IOException e) {
            new PopupView("error_loading_searchbar_title", e.getMessage(), PopupType.ERROR);
        }
    }

    /**
     * Called when the user clicks on the "Settings" button in the main window.
     */
    @Override
    public void onSettingClicked() {
        ChooseLanguageController chooseLanguageController = new ChooseLanguageController();
        chooseLanguageController.setMainControllerAccess(this);
        chooseLanguageController.show();
    }

    /**
     * Called when the user clicks on the "Account Settings" button in the main
     * window.
     */
    @Override
    public void onAccountSettingsClicked() {
        AccountSettingsController accountSettingsController = new AccountSettingsController(this);
        accountSettingsController.show();
    }

    /**
     * Updates the current collection of songs in the library. If the current it's
     * in Queue, it refreshes the label.
     */
    @Override
    public void updateCurrentCollection() {
        collectionController.updateCurrentCollection();
        playerViewController.updateNextSongLabel(collectionController.getNextSongTitleQueue());
    }

    /**
     * Logs out the user by calling the listener's onAskUserController method.
     */
    @Override
    public void logout() {
        listener.onAskUserController();
    }

    /**
     * Changes the language of the application by updating the ResourceBundle
     * and reloading the view.
     */
    @Override
    public void changeLanguage() throws SQLExceptionHandler {
        try {
            collectionController.show();
            searchBarController.show();
            playerViewController.updateNowPlayingBox();
            show();
        } catch (IOException e) {
            new PopupView("error_reloading_view_title", I18n.get("error_reloading_view"), PopupType.ERROR);
        }
    }

    public interface MainListener {
        void onAskUserController();
    }
}

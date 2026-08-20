package ulb.views.searches;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import ulb.dtos.SongDTO;

import java.util.List;

/**
 * This class handles the UI for the search bar and its associated search
 * results list.
 * It is responsible for setting up the search bar and its event handlers.
 * It communicates with the SearchBarController to retrieve the search results.
 */
public class SearchBarView {
    private final int MAX_HEIGHT = 400;

    @FXML
    public VBox searchBar;

    @FXML
    private TextField searchText;
    @FXML
    private VBox listResult;
    @FXML
    private ScrollPane scrollPane;

    private SearchBarViewListener listener;


    public void setListener(SearchBarViewListener listener) {
        this.listener = listener;
    }

    /**
     * Initializes the SearchBarView.
     * <p>
     * This method is called after the FXML file has been loaded and the objects
     * have been created.
     * It sets up the event handlers for the search text field and the scroll pane.
     */
    @FXML
    public void initialize() {
        scrollPane.prefViewportHeightProperty().bind(listResult.heightProperty());
        searchText.textProperty().addListener((event, Event, newValue) -> {
            handleSearch(newValue);
            searchBar.setPrefHeight(30);
            listResult.setPrefHeight(MAX_HEIGHT);
            scrollPane.setPrefHeight(MAX_HEIGHT);
        });
        searchText.setOnKeyPressed(this::handleKeyPressed);
    }

    /**
     * Handles a key press event on the search text field.
     * <p>
     * If the pressed key is the escape key, the search text field is cleared.
     *
     * @param event The key press event
     */
    private void handleKeyPressed(KeyEvent event) {
        if (event.getCode() == KeyCode.ESCAPE) {
            searchText.clear();
        }
    }

    /**
     * Handles a search query.
     * <p>
     * If the query is empty or there are no results, the search bar is hidden.
     * Otherwise, the search bar is shown and the search results are displayed.
     * If an exception occurs, an ErrorView is shown.
     *
     * @param query The search query
     */
    private void handleSearch(String query) {
        listResult.getChildren().clear();
        List<SongDTO> results = listener.search(query);
        if (query.isEmpty() || results.isEmpty()) {
            setSearchBarVisibility(false);
            return;
        }
        setSearchBarVisibility(true);
        updateSearchResults(results);
    }

    /**
     * Updates the search results displayed in the search bar.
     * <p>
     * This method takes a list of songs, creates UI components for each song,
     * and adds them to the list of search results. The height of the search
     * bar is adjusted based on the number of results, up to a maximum height.
     *
     * @param songs The list of Song objects to display as search results.
     */
    private void updateSearchResults(List<SongDTO> songs) {
        for (SongDTO song : songs) {
            Node musicItem = listener.createCell(song);
            if (musicItem != null) {
                listResult.getChildren().add(musicItem);
            }
        }
        int CELL_HEIGHT = 40;
        int newHeight = Math.min(songs.size() * CELL_HEIGHT, MAX_HEIGHT);
        updateUIHeights(newHeight);
    }


    /**
     * Sets the visibility of the search bar.
     * <p>
     * This method adjusts the preferred height of the search bar and the visibility
     * of the scroll pane based on the given visibility flag.
     *
     * @param visible If true, the search bar is displayed with the default height;
     *                if false, the search bar is hidden.
     */
    private void setSearchBarVisibility(boolean visible) {
        int DEFAULT_HEIGHT = 30;
        searchBar.setPrefHeight(visible ? DEFAULT_HEIGHT : 0);
        scrollPane.setVisible(visible);
    }

    /**
     * Sets the preferred height of the search bar UI components.
     * <p>
     * This method sets the preferred height of the search bar, the list of search
     * results, and the scroll pane to the given height.
     *
     * @param height The new preferred height for the search bar UI components.
     */
    private void updateUIHeights(int height) {
        searchBar.setPrefHeight(height);
        listResult.setPrefHeight(height);
        scrollPane.setPrefHeight(height);
    }

    public interface SearchBarViewListener {
        List<SongDTO> search(String query);

        Node createCell(SongDTO song);
    }
}
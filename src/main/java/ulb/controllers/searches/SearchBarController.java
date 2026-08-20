package ulb.controllers.searches;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.VBox;
import ulb.dtos.SongDTO;
import ulb.exceptions.songs.InvalidSongException;
import ulb.exceptions.songs.SQLExceptionHandler;
import ulb.models.Song;
import ulb.services.SearchBarService;
import ulb.utils.I18n;
import ulb.utils.PopupType;
import ulb.views.PopupView;
import ulb.views.searches.SearchBarView;

import java.io.IOException;
import java.util.List;

/**
 * Controls the search bar and associated song list. Handles the interaction between
 * the view and the search service.
 */
public class SearchBarController implements SearchBarView.SearchBarViewListener {
    private final SearchBarService searchService;
    private VBox tempResults;
    SearchBarListener listener;

    public SearchBarController() {
        searchService = new SearchBarService();
    }

    public void setListener(SearchBarListener listener) {
        this.listener = listener;
    }

    public void show() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ulb/views/SearchBar.fxml"), I18n.getBundle());
        SearchBarView sbv = new SearchBarView();
        loader.setControllerFactory(event -> sbv);
        this.tempResults = loader.load();
        SearchBarView controller = loader.getController();
        controller.setListener(this);
    }

    /**
     * Searches for songs based on a query.
     * <p>
     * This method delegates the search to the search service, which searches for
     * songs in the database and their lyrics. If an error occurs during the search,
     * an error popup is shown and an empty list is returned.
     *
     * @param query The search query
     * @return List of songs matching the search criteria, or an empty list if an error occurs
     */
    @Override
    public List<SongDTO> search(String query) {
        try {
            return searchService.searchSongsAsDTO(query);
        } catch (SQLExceptionHandler | InvalidSongException | IOException e) {
            new PopupView("error_retrieving_song", e.getMessage(), PopupType.ERROR);
        }
        return List.of();
    }

    /**
     * Creates a UI component representing a song in the search bar.
     * <p>
     * This method takes a SongDTO object as input and returns a Node
     * which is a UI component representing the song in the search bar.
     * If the listener is null, or if the listener's createCell method
     * returns null, this method returns null.
     *
     * @param song The SongDTO object to create a UI component for.
     * @return A Node representing the song in the search bar, or null if the
     * listener is null or if the listener's createCell method returns null.
     */
    @Override
    public Node createCell(SongDTO song) {
        if (song instanceof Song concreteSong) {
            return listener.createCell(concreteSong);
        }
        return null;
    }

    public VBox getSearchBarView() {
        return tempResults;
    }
}
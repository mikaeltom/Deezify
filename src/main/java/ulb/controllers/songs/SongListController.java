package ulb.controllers.songs;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.input.*;
import javafx.scene.layout.HBox;
import ulb.controllers.songs.SongBoxController.CollectionControllerAccess;
import ulb.controllers.songs.SongBoxController.PlayerViewControllerAccess;
import ulb.controllers.songs.SongBoxController.SongOptionControllerAccess;
import ulb.dtos.SongDTO;
import ulb.models.Song;
import ulb.utils.I18n;
import ulb.utils.PopupType;
import ulb.views.PopupView;
import ulb.views.songs.SongListView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Controller responsible for managing the song list view.
 * It initializes the view, sets up listeners, and handles interactions
 * between the view and other components.
 * The controller listens for events from the SongListView and communicates
 * with the MainControllerAccess.
 */
public class SongListController implements SongListView.SongListViewListener {
    private final SongOptionControllerAccess songOptionControllerAccess;
    private final PlayerViewControllerAccess playerViewControllerAccess;
    private final CollectionControllerAccess collectionControllerAccess;
    private SongListView songListView;
    private Parent songListViewParent;
    private List<Song> currentSongList;
    private SongListListener songListListener;


    public SongListController(List<Song> songList,
            SongOptionControllerAccess songOptionControllerAccess,
            PlayerViewControllerAccess playerViewControllerAccess,
            CollectionControllerAccess collectionControllerAccess) {
        this.currentSongList = songList;
        this.songOptionControllerAccess = songOptionControllerAccess;
        this.playerViewControllerAccess = playerViewControllerAccess;
        this.collectionControllerAccess = collectionControllerAccess;
    }

    /**
     * Sets the listener for the song list view.
     * The listener is informed when the user interacts with the song list view.
     */
    public void setSongListListener(SongListListener songListListener) {
        this.songListListener = songListListener;
    }

    /**
     * Loads the SongListView once and sets up its listener.
     * If the load fails, it shows an error popup.
     */
    public void show() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ulb/views/SongListView.fxml"),
                    I18n.getBundle());
            songListViewParent = loader.load();
            this.songListView = loader.getController();
            this.songListView.setSongListViewListener(this);
            this.songListView.clearSongsList();
            setupScrollPane();
        } catch (IOException e) {
            new PopupView("loading_error_title", I18n.get("error_loading_songlist"), PopupType.ERROR);
        }
    }

    /**
     * Initializes the song list view's scroll pane.
     * <p>
     * This method is used to initialize the song list view's scroll pane when the
     * view is first loaded. It loads each song box in the current song list and
     * adds it to the view's scroll pane.
     */
    public void setupScrollPane() {
        currentSongList.forEach(song -> songListView.addSongBox(new SongBoxController
                (song, songOptionControllerAccess,
                        playerViewControllerAccess, collectionControllerAccess).load()));
        songListView.setScrollPane();
    }

    /**
     * Updates the song list view with a new list of songs.
     *
     * <p>
     * This method updates the current song list to the provided list of songs,
     * clears the existing song boxes from the view, and reloads the song boxes
     * in the updated order. It handles any exceptions by displaying an error
     * popup if the song list view fails to load.
     *
     * @param songList The new list of Song objects to display in the song list
     *                 view.
     */
    public void updateSongsList(List<Song> songList) {
        this.currentSongList = songList;
        this.songListView.clearSongsList();
        setupScrollPane();
    }

    /**
     * Gets the parent node of the song list view.
     */
    public Parent getSongListViewParent() {
        return songListViewParent;
    }

    /**
     * Reconstructs an ordered list of Song objects based on the given list of
     * SongDTOs,
     * using the current list of Song objects. Matches on multiple fields for
     * robustness.
     *
     * @param songDTOs     List of SongDTOs representing the desired order.
     * @param currentSongs List of current Song objects to match against.
     * @return A new ordered list of Song objects.
     */
    private List<Song> mapDTOsToSongs(List<SongDTO> songDTOs, List<Song> currentSongs) {
        List<Song> reordered = new ArrayList<>();

        for (SongDTO dto : songDTOs) {
            for (Song song : currentSongs) {
                if (song.getId() == dto.getId()
                        && song.getTitle().equals(dto.getTitle())
                        && song.getArtist().equals(dto.getArtist())
                        && song.getAlbum().equals(dto.getAlbum())) {

                    reordered.add(song);
                    break;
                }
            }
        }
        return reordered;
    }

    /**
     * Handles the drag and drop operation for the song list.
     *
     * <p>
     * This method is triggered when a drag and drop event occurs on the song list.
     * It maps the provided list of SongDTOs to the current list of Song objects,
     * updates the order of songs accordingly, and notifies the listener of the new
     * order if available. If an exception occurs during the update, an error popup
     * is displayed.
     * </p>
     *
     * @param songListDTO The list of SongDTOs representing the desired order after
     *                    drag and drop.
     */
    @Override
    public void handleDragAndDrop(ArrayList<SongDTO> songListDTO) {
        List<Song> newOrderedSongs = mapDTOsToSongs(songListDTO, currentSongList);

        if (songListListener != null) {
            songListListener.updateFromDragAndDrop(newOrderedSongs);
        }
        updateSongsList(newOrderedSongs);
    }

    /**
     * Handles the start of a drag operation on a song in the song list.
     *
     * <p>
     * This method is triggered when the user starts dragging a song in the song
     * list.
     * It sets up the dragboard with the content of the song's unique identifier,
     * which is stored in the node's user data. It also enables the MOVE transfer
     * mode to indicate that the drag operation should result in the song being
     * moved to a new position in the song list.
     * </p>
     *
     * @param node  The node representing the song that is being dragged.
     * @param event The mouse event that triggered the drag operation.
     */
    @Override
    public void startDrag(Node node, MouseEvent event) {
        Dragboard dragboard = node.startDragAndDrop(TransferMode.MOVE);
        ClipboardContent content = new ClipboardContent();
        content.putString(node.getUserData().toString()); // Add the unique identifier (e.g., song title or ID)
        dragboard.setContent(content);
        event.consume();
    }

    /**
     * Handles the drag over event during a drag-and-drop operation.
     *
     * <p>
     * This method is triggered when a drag gesture is detected over the song list
     * view.
     * It checks if the source of the gesture is not the song list view itself and
     * if the
     * dragboard contains a string. If these conditions are met, it accepts the MOVE
     * transfer
     * mode, allowing the dragged item to be moved within the list. The event is
     * then consumed
     * to prevent further propagation.
     * </p>
     *
     * @param event The DragEvent object containing the details of the drag over
     *              event.
     */
    @Override
    public void acceptDragOver(DragEvent event) {
        if (event.getGestureSource() != songListView && event.getDragboard().hasString()) {
            event.acceptTransferModes(TransferMode.MOVE); // Accept the MOVE transfer mode
        }
        event.consume();
    }

    /**
     * Handles the drop event during a drag-and-drop operation.
     *
     * <p>
     * This method is triggered when the user drops a dragged item onto the song
     * list view.
     * It first checks if the dragged item is a string, and if so, attempts to find
     * the
     * corresponding HBox in the song list view's children. If the HBox is found, it
     * is
     * reordered to the position specified by the y-coordinate of the drop event.
     * Finally, the event is marked as completed if the reordering was successful,
     * and
     * consumed to prevent further propagation.
     * </p>
     *
     * @param event The DragEvent object containing the details of the drop event.
     */
    @Override
    public void handleDrop(DragEvent event) {
        Dragboard dragboard = event.getDragboard();
        if (dragboard.hasString()) {
            String draggedNodeId = dragboard.getString();
            HBox draggedBox = songListView.findDraggedBox(draggedNodeId);
            if (draggedBox != null) {
                songListView.reorderBox(draggedBox, event.getY());
                songListView.updateBoxOrder();
                event.setDropCompleted(true);
            }
        }
        event.consume();
    }

    public interface SongListListener {
        void updateFromDragAndDrop(List<Song> songList);
    }
}

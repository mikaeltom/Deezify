package ulb.views.songs;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import ulb.dtos.SongDTO;

import java.util.ArrayList;

/**
 * A view for displaying a list of songs. The view is a scroll pane that
 * contains a VBox where each song is represented by an HBox containing the
 * song's title, artist name and album cover.
 * The view also has drag and drop event handlers set up to allow the user to
 * drag songs from the song list view to the playlist view to add songs to the
 * current playlist.
 */
public class SongListView {
    @FXML
    private ScrollPane songScrollPane;
    @FXML
    private VBox playlistBox;
    private SongListViewListener listener;

    public void setSongListViewListener(SongListViewListener listener) {
        this.listener = listener;
    }


    /**
     * Find the HBox that contains the song with the given title.
     * This method is used to find the song box that was dragged
     * and dropped at a new position.
     *
     * @param title The title of the song to find.
     * @return The HBox that contains the song with the given title
     * or null if no such song box is found.
     */
    public HBox findDraggedBox(String title) {
        for (Node node : playlistBox.getChildren()) {
            if (node instanceof HBox hbox) {
                SongDTO song = (SongDTO) hbox.getUserData();
                if (song.toString().equals(title)) {
                    return hbox;
                }
            }
        }
        return null;
    }

    /**
     * Reorders the song boxes in the playlist box after a drag and drop.
     *
     * <p>
     * This method is used after a drag and drop operation to reorder the
     * song boxes in the playlist box. The dragged box is removed from the
     * playlist box and then reinserted at the position where the user dropped
     * it. The position of the drop is determined by the vertical position of
     * the mouse when the drop occurred.
     *
     * @param draggedBox The HBox that was dragged and dropped.
     * @param dropY      The vertical position of the mouse when the drop occurred.
     */
    public void reorderBox(HBox draggedBox, double dropY) {
        playlistBox.getChildren().remove(draggedBox);
        int dropIndex = findDropBoxIndex(dropY);
        addDraggedBoxAtPosition(draggedBox, dropIndex);
    }

    /**
     * Finds the index of the box in the playlist box that the user dropped
     * the dragged box onto. The index is determined by the vertical position
     * of the mouse when the drop occurred. The box that the user dropped onto
     * is the one whose vertical position is closest to the vertical position
     * of the mouse.
     *
     * @param dropY The vertical position of the mouse when the drop occurred.
     * @return The index of the box in the playlist box that the user dropped
     * the dragged box onto, or -1 if the user dropped the box below
     * all the boxes in the playlist box.
     */
    private int findDropBoxIndex(double dropY) {
        for (int i = 0; i < playlistBox.getChildren().size(); i++) {
            Node node = playlistBox.getChildren().get(i);
            if (dropY < node.getLayoutY() + node.getBoundsInParent().getHeight() / 2) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Adds the dragged box at the specified index in the playlist box.
     *
     * @param draggedBox The HBox that was dragged and dropped.
     * @param dropIndex  The index at which to add the dragged box, or -1 to add at the end.
     */
    private void addDraggedBoxAtPosition(HBox draggedBox, int dropIndex) {
        playlistBox.getChildren().add(dropIndex == -1 ? playlistBox.getChildren().size() : dropIndex, draggedBox);
    }

    /**
     * Updates the order of songs in the current collection based on the order of
     * song boxes in the playlist box.
     *
     * <p>
     * This method is used after the user has reordered the song boxes in the
     * playlist box. The order of the song boxes is read and used to update the
     * order of songs in the current collection.
     */
    public void updateBoxOrder() {
        ArrayList<SongDTO> newOrder = new ArrayList<>();

        for (Node node : playlistBox.getChildren()) {
            if (node instanceof HBox hbox) {
                SongDTO song = (SongDTO) hbox.getUserData();
                newOrder.add(song);
            }
        }
        listener.handleDragAndDrop(newOrder);
    }

    /**
     * Sets up drag and drop event handlers on the playlist box and all its children.
     */
    public void setupDragAndDrop() {
        playlistBox.getChildren().forEach(node -> node.setOnDragDetected(event -> listener.startDrag(node, event)));
        playlistBox.setOnDragOver(event -> listener.acceptDragOver(event));
        playlistBox.setOnDragDropped(event -> listener.handleDrop(event));
    }

    /**
     * Clears the song boxes in the playlist box.
     */
    public void clearSongsList() {
        playlistBox.getChildren().clear();
    }

    /**
     * Adds a song box to the playlist box.
     *
     * @param songBox a song box to add
     */
    public void addSongBox(Node songBox) {
        playlistBox.getChildren().add(songBox);

    }

    /**
     * Sets up the song scroll pane by setting its content and drag and drop handlers.
     */
    public void setScrollPane() {
        setupDragAndDrop();
        songScrollPane.setContent(playlistBox);
    }

    public interface SongListViewListener {
        void startDrag(Node node, MouseEvent event);

        void acceptDragOver(DragEvent event);

        void handleDrop(DragEvent event);

        void handleDragAndDrop(ArrayList<SongDTO> songListDTO);
    }
}

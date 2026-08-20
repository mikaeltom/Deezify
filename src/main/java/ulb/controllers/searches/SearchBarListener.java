package ulb.controllers.searches;

import javafx.scene.Node;
import ulb.models.Song;

/**
 * Interface for creating a cell in the search bar.
 * <p>
 * This interface defines a method for creating a cell in the search bar
 * for a given song. It is used to decouple the creation of the cell from
 * the actual implementation, allowing for different types of cells to be
 * created based on the song's properties.
 */
public interface SearchBarListener {
    Node createCell(Song song);
}

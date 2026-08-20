package ulb.views;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.OverrunStyle;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import ulb.dtos.MusicCollectionDTO;
import ulb.dtos.PlaylistDTO;
import ulb.dtos.QueueDTO;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * The CollectionsView class is responsible for displaying the user's
 * collections
 * (Library, Favorites, Queue, and Playlists) in the sidebar.
 */
public class CollectionsView {
    @FXML
    private VBox CollectionsVBox;
    private Label currentSelectedLabel = null;
    private HBox currentSelectedContainer = null;
    private String currentLabel = "Library";
    private List<MusicCollectionDTO> musicCollections;

    private CollectionsViewListener listener;

    public void setCollectionsViewListener(CollectionsViewListener listener) {
        this.listener = listener;
    }

    /**
     * Sets the list of collections to be displayed in the view.
     * The collections are loaded into the view after the list is set.
     *
     * @param collection the list of collections to be displayed
     */
    public void setMusicCollection(List<MusicCollectionDTO> collection) {
        this.musicCollections = collection;
        loadCollections();
    }

    /**
     * Returns the label of the currently selected collection.
     *
     * @return the label of the currently selected collection
     */
    public String getCurrentLabel() {
        return currentSelectedLabel.getText();
    }


    /**
     * Loads the user's collections (Library, Favorites, Queue, and Playlists)
     * into the VBox for display.
     */
    private void loadCollections() {
        CollectionsVBox.getChildren().clear();
        reorder();
        for (MusicCollectionDTO collection : musicCollections) {
            String label = listener.getCollectionLabel(collection);
            if (collection instanceof PlaylistDTO &&
                    !label.equals("Library") &&
                    !label.equals("Favorites") &&
                    !label.equals("Queue")) {
                CollectionsVBox.getChildren().add(createAndSelectIfCurrent(collection));
            }
        }
    }

    /**
     * Reorders the collections in the VBox based on their labels to start with Favorites, then library then Queue.
     */
    private void reorder() {
        String[] labels = {"Favorites", "Library", "Queue"};
        for (String label : labels) {
            musicCollections.stream()
                    .filter(collection -> listener.getCollectionLabel(collection).equals(label))
                    .findFirst()
                    .ifPresent(collection -> CollectionsVBox.getChildren().add(createAndSelectIfCurrent(collection)));
        }
        musicCollections = Collections.unmodifiableList(musicCollections);
    }


    /**
     * Creates a HBox that represents a collection of songs.
     *
     * @param collection The collection to create the HBox for.
     * @return The HBox representing the collection.
     */
    private HBox createAndSelectIfCurrent(MusicCollectionDTO collection) {
        HBox collectionItem = createCollectionItem(collection);
        if (listener.getCollectionLabel(collection).equals(currentLabel)) {
            Label label = (Label) ((HBox) collectionItem.getChildren().getFirst()).getChildren().getFirst();
            currentSelectedContainer = collectionItem;
            markAsSelected(label);
        }
        return collectionItem;
    }

    /**
     * Refreshes the collections displayed in the VBox.
     */
    public void refresh() {
        loadCollections();
    }

    /**
     * Creates a HBox that represents a collection of songs.
     *
     * @param collection The collection to create the HBox for.
     * @return The HBox representing the collection.
     */
    private HBox createCollectionItem(MusicCollectionDTO collection) {
        HBox box = createHBox();
        HBox textButtonContainer = createTextButtonContainer(collection);
        box.getChildren().add(textButtonContainer);
        addClickEvent(box, (Label) textButtonContainer.getChildren().getFirst());
        return box;
    }

    /**
     * Creates a HBox that contains a Label with the collection's name
     *
     * @param collection - The collection to create the HBox for.
     * @return The HBox instance representing the collection.
     */
    private HBox createTextButtonContainer(MusicCollectionDTO collection) {
        Label nameLabel = createCollectionLabel(collection);
        HBox.setHgrow(nameLabel, Priority.ALWAYS);

        HBox container = createBaseContainer(nameLabel);

        if (shouldShowDeleteButton(collection)) {
            Button clearButton = createStyledDeleteButton(collection);
            container.getChildren().add(clearButton);
        }

        HBox.setHgrow(container, Priority.ALWAYS);
        return container;
    }

    /**
     * Creates a base HBox that contains the collection's name label.
     *
     * @param nameLabel The Label representing the collection's name.
     * @return The HBox instance.
     */
    private HBox createBaseContainer(Label nameLabel) {
        HBox container = new HBox(nameLabel);
        container.setSpacing(10);
        container.setAlignment(Pos.CENTER_LEFT);
        container.getStyleClass().add("playlist-container");
        return container;
    }

    /**
     * Determines whether to show the delete button for a given collection.
     *
     * @param collection The collection to check.
     * @return true if the delete button should be shown, false otherwise.
     */
    private boolean shouldShowDeleteButton(MusicCollectionDTO collection) {
        String name = collection.getName();
        return !name.equals("Library") && !name.equals("Favorites") && !name.equals("Queue");
    }

    /**
     * Creates a styled delete button for the specified collection.
     *
     * @param collection The collection to create the delete button for.
     * @return The Button instance.
     */
    private Button createStyledDeleteButton(MusicCollectionDTO collection) {
        Button button = createDeleteButton(collection);
        button.getStyleClass().add("clear-button");
        return button;
    }

    /**
     * Creates a HBox that is used as the container for a collection item.
     *
     * @return The HBox instance
     */
    private HBox createHBox() {
        HBox box = new HBox();
        box.setPrefWidth(131.0);
        box.setMaxWidth(Double.MAX_VALUE);
        box.setSpacing(5);
        return box;
    }

    /**
     * Creates a Label with text or image that represents a collection of songs in the sidebar.
     *
     * @param collection The collection to create the Label for.
     * @return The Label instance.
     */
    private Label createCollectionLabel(MusicCollectionDTO collection) {
        Label nameLabel = createBaseLabel();
        String collectionLabel = listener.getCollectionLabel(collection);
        nameLabel.setText(collectionLabel);

        ImageView icon = getCollectionIcon(collectionLabel);

        if (icon != null) {
            applyGraphicOnlyStyle(nameLabel, icon);
        } else {
            nameLabel.setAlignment(Pos.CENTER_LEFT);
        }

        return nameLabel;
    }

    /**
     * Creates a base Label with a preferred width of 131px, wrap text enabled
     *
     * @return The base Label instance.
     */
    private Label createBaseLabel() {
        Label label = new Label();
        label.setPrefWidth(131.0);
        label.setWrapText(true);
        label.setTextOverrun(OverrunStyle.CLIP);
        label.setMinHeight(Label.USE_PREF_SIZE);
        label.getStyleClass().add("playlist-item");
        return label;
    }

    /**
     * Returns the icon associated with the specified collection label.
     *
     * @param collectionLabel The label of the collection.
     * @return The ImageView representing the icon for the collection.
     */
    private ImageView getCollectionIcon(String collectionLabel) {
        return switch (collectionLabel) {
            case "Library" -> createIcon("/images/library-icon.png");
            case "Favorites" -> createIcon("/images/heart-filled.png");
            case "Queue" -> createIcon("/images/queue-icon.png");
            default -> null;
        };
    }

    /**
     * Applies a graphic-only style to the specified Label.
     *
     * @param label The Label to apply the style to.
     * @param icon  The ImageView to set as the graphic.
     */
    private void applyGraphicOnlyStyle(Label label, ImageView icon) {
        label.setGraphic(icon);
        label.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        label.setAlignment(Pos.CENTER);
        label.setStyle("-fx-text-fill: transparent; -fx-alignment: center;");
    }

    /**
     * Creates an ImageView with the specified image path.
     *
     * @param imagePath The path to the image file.
     * @return The ImageView instance.
     */
    private ImageView createIcon(String imagePath) {
        Image image = new Image(Objects.requireNonNull(getClass().getResource(imagePath)).toExternalForm());
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(27);
        imageView.setFitHeight(27);
        return imageView;
    }


    /**
     * Adds an image to the clear queue button.
     *
     * @param clearQueueButton The button to add the image to.
     */
    private void addImageOnClear(Button clearQueueButton) {
        Image image = new Image(Objects.requireNonNull(getClass().getResource("/images/bin-logo.png")).toExternalForm());
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(12.8);
        imageView.setFitHeight(12.8);
        clearQueueButton.setGraphic(imageView);
    }

    /**
     * Marks the provided label as the selected collection label.
     *
     * @param nameLabel The label to be marked as selected.
     */
    private void markAsSelected(Label nameLabel) {
        clearPreviousSelection();
        applyNewSelection(nameLabel);
        if (currentSelectedContainer != null) {
            currentSelectedContainer.getStyleClass().add("playlist-selected");
        }
    }

    /**
     * Clears the visual selection state of the previously selected label.
     */
    private void clearPreviousSelection() {
        if (currentSelectedLabel != null) {
            currentSelectedLabel.getStyleClass().remove("playlist-selected");
            removeSelectionFromParent(currentSelectedLabel);
        }
    }

    /**
     * Removes the "playlist-selected" style class from the parent of the specified label.
     *
     * @param label label
     */
    private void removeSelectionFromParent(Label label) {
        Node parent = label.getParent();
        if (parent instanceof HBox) {
            parent.getStyleClass().remove("playlist-selected");
        }
    }

    /**
     * Updates the visual selection state to reflect the newly selected label.
     *
     * @param nameLabel name label
     */
    private void applyNewSelection(Label nameLabel) {
        nameLabel.getStyleClass().add("playlist-selected");
        Node newParent = nameLabel.getParent();
        if (newParent instanceof HBox) {
            newParent.getStyleClass().add("playlist-selected");
            currentSelectedContainer = (HBox) newParent.getParent();
        }
        currentSelectedLabel = nameLabel;
        currentLabel = nameLabel.getText();
    }

    /**
     * Adds a mouse click event to the specified HBox.
     *
     * @param box       The HBox to which the click event is added.
     * @param nameLabel The label representing the collection, which will be marked
     *                  as selected upon clicking the HBox.
     */
    private void addClickEvent(HBox box, Label nameLabel) {
        box.setOnMouseClicked(event -> {
            if (currentSelectedLabel == nameLabel)
                return;
            if (currentSelectedLabel != null) {
                currentSelectedLabel.getStyleClass().remove("playlist-selected");
            }
            if (currentSelectedContainer != null) {
                currentSelectedContainer.getStyleClass().remove("playlist-selected");
            }
            nameLabel.getStyleClass().add("playlist-selected");
            box.getStyleClass().add("playlist-selected");
            currentSelectedLabel = nameLabel;
            currentSelectedContainer = box;
            handleCollectionClick(nameLabel.getText());
        });
    }

    /**
     * Handles the click event when a collection is selected.
     *
     * @param collection The selected collection.
     */
    private void handleCollectionClick(String collection) {
        listener.handleClickedCollection(collection);
    }

    /**
     * Creates a VBox that contains a Button labeled "Clear" which when clicked
     * will clear the queue/delete the playlist and update the library view.
     *
     * @param collection The collection to clear/delete when the button is clicked.
     * @return The VBox containing the Button.
     */
    private Button createDeleteButton(MusicCollectionDTO collection) {
        Button clearButton = new Button("");
        addImageOnClear(clearButton);
        clearButton.setMaxSize(20, 20);
        clearButton.getStyleClass().add("clear-item");
        if (collection instanceof PlaylistDTO) {
            clearButton.setOnAction(e -> listener.deletePlaylist(collection.getName()));
        } else if (collection instanceof QueueDTO) {
            clearButton.setOnAction(e -> listener.clearQueue());
        }
        return clearButton;
    }

    public interface CollectionsViewListener {
        void clearQueue();

        void deletePlaylist(String name);

        void handleClickedCollection(String label);

        String getCollectionLabel(MusicCollectionDTO collection);

    }
}

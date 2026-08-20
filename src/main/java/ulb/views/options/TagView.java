package ulb.views.options;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.FlowPane;
import ulb.dtos.SongDTO;
import ulb.dtos.TagDTO;

import java.util.List;

/**
 * The TagView class is responsible for displaying the UI for editing tags of a
 * song.
 * It provides methods to initialize the view and set the current song.
 * It communicates with the TagController to retrieve the tags for the current
 * song and add new tags.
 * The view is a popup window that appears when the user clicks on the "Tags"
 * button in the song options dialog.
 */
public class TagView {
    private SongDTO currentSong;
    @FXML
    private TextField tagTextField;

    @FXML
    private FlowPane predefinedTagFlowPane, customTagFlowPane;

    private TagViewListener listener;

    public void setListener(TagViewListener tagViewListener) {
        this.listener = tagViewListener;
    }

    /**
     * Initializes the view by setting the current song.
     */
    @FXML
    public void initialize(SongDTO song) {
        setCurrentSong(song);
    }

    /**
     * Sets the current song and updates the display of tags.
     */
    public void setCurrentSong(SongDTO song) {
        this.currentSong = song;
        setupPredefinedTagButtons();
        loadCustomTags();
    }

    /**
     * Handles the submission of a custom tag for the current song.
     * <p>
     * This method retrieves the tag name from the text field, validates it,
     * and attempts to add it to the current song. If the tag is valid, it
     * is inserted into the database and associated with the song. If the
     * tag is invalid or causes a database error, an error message is displayed.
     * The input field is cleared and custom tags are reloaded regardless of
     * success.
     * </p>
     * <p>
     */
    @FXML
    private void handleCustomTagSubmit() {
        String tagName = tagTextField.getText().trim();
        if (tagName.isEmpty()) {
            return;
        }
        listener.customTagSubmit(tagName);
        tagTextField.clear();
        loadCustomTags();
    }

    /**
     * Attache l'événement `handleTagSelection` aux boutons des tags prédéfinis.
     */
    private void setupPredefinedTagButtons() {
        for (javafx.scene.Node node : predefinedTagFlowPane.getChildren()) {
            if (node instanceof ToggleButton toggleButton) {
                String tagName = toggleButton.getText();
                if (currentSong != null) {
                    toggleButton.setSelected(currentSong.getTagsNames().contains(tagName));
                }
                toggleButton.setOnAction(event -> handleTagSelection(toggleButton));
            }
        }
    }

    /**
     * Load custom tags for a user and display them in the UI.
     * <p>
     * This method retrieves the custom tags for the given user ID,
     * clears the old custom tag buttons, and adds new buttons for each
     * custom tag.
     * </p>
     * <p>
     * Exceptions:
     * - SQLExceptionHandler: If a database access error occurs.
     */
    @FXML
    public void loadCustomTags() {
        List<? extends TagDTO> customTags = listener.getCustomTags();
        customTagFlowPane.getChildren().clear();
        for (TagDTO tag : customTags) {
            createCustomTagButton(tag.getName());
        }
    }

    /**
     * Handles the selection of a tag button.
     */
    private void handleTagSelection(ToggleButton tagButton) {
        if (tagButton.isSelected()) {
            addTag(tagButton.getText());
        } else {
            removeTag(tagButton.getText());
        }
    }

    /**
     * Adds a tag to the song
     */
    private void addTag(String tagText) {
        listener.addSongTag(tagText);

    }

    /**
     * Removes a tag from the song
     */
    private void removeTag(String tagText) {
        listener.removeTag(tagText);
    }

    /**
     * Dynamically creates a button for a custom tag.
     *
     * @param tagName The name of the custom tag.
     */
    private void createCustomTagButton(String tagName) {
        ToggleButton tagButton = new ToggleButton(tagName);
        tagButton.getStyleClass().add("tag-button");
        tagButton.setSelected(currentSong.getTagsNames().contains(tagName));
        tagButton.setOnAction(event -> handleTagSelection(tagButton));
        customTagFlowPane.getChildren().add(tagButton);
    }

    /**
     * Closes the tag view and opens the song options view.
     * <p>
     * The song options view is opened with the current song's metadata pre-filled
     * and ready to be edited. The user can then edit the song's metadata and
     * save the changes.
     */
    @FXML
    public void handleClose() {
        listener.close();
    }

    public interface TagViewListener {
        void close();

        List<? extends TagDTO> getCustomTags();

        void removeTag(String tagText);

        void addSongTag(String tagText);

        void customTagSubmit(String tagName);
    }
}

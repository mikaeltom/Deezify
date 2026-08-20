package ulb.controllers.options;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ulb.dtos.TagDTO;
import ulb.exceptions.BannedWordException;
import ulb.exceptions.songs.SQLExceptionHandler;
import ulb.exceptions.tag.InvalidTagError;
import ulb.models.PredefinedTagManager;
import ulb.models.Song;
import ulb.services.BannedWordService;
import ulb.services.SQLService;
import ulb.services.options.TagService;
import ulb.utils.I18n;
import ulb.utils.PopupType;
import ulb.views.PopupView;
import ulb.views.options.TagView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Controller that handles the UI for adding and removing tags from songs.
 */
public class TagController implements TagView.TagViewListener {
    private final TagService tagService;
    private OptionListener listener;
    private Song currentSong;

    private TagView tagView;

    public TagController(BannedWordService bannedWordService,
                         PredefinedTagManager predefinedTagManager) {
        this.tagService = new TagService(
                SQLService.getInstance(),
                bannedWordService,
                predefinedTagManager
        );
    }

    public void setListener(OptionListener listener) {
        this.listener = listener;
    }

    private void setCurrentSong(Song song) {
        this.currentSong = song;
    }

    /**
     * Retrieves all custom tags (non-predefined) associated with a user.
     *
     * @return A list of custom tags associated with the user.
     */
    public List<? extends TagDTO> getCustomTags() {
        try {
            return tagService.getCustomTags();
        } catch (SQLExceptionHandler e) {
            new PopupView("error_retrieving_custom_tag", e.getMessage(), PopupType.ERROR);
            return new ArrayList<>();
        }
    }

    /**
     * Adds a tag to the current song.
     *
     * @param tagText The name of the tag to be added to the song.
     */
    public void addSongTag(String tagText) {
        try {
            tagService.addTagToSong(currentSong, tagText);
            listener.updateCurrentCollection();
        } catch (SQLExceptionHandler | InvalidTagError | BannedWordException e) {
            new PopupView("error_adding_tag", e.getMessage(), PopupType.ERROR);
        }
    }


    /**
     * Removes a tag from the current song.
     *
     * @param tagText The name of the tag to be removed from the song.
     */
    public void removeTag(String tagText) {
        try {
            tagService.removeTagFromSong(currentSong, tagText);
            listener.updateCurrentCollection();
            tagView.loadCustomTags();
        } catch (InvalidTagError | SQLExceptionHandler e) {
            new PopupView("error_removing_tag", e.getMessage(), PopupType.ERROR);
        }
    }

    /**
     * Displays the TagView with the provided song and metadata.
     *
     * @param song   The Song object to be displayed and tagged.
     * @param stage  The Stage object on which the TagView is to be displayed.
     */
    public void show(Song song,Stage stage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ulb/views/TagView.fxml"), I18n.getBundle());
            Parent tagRoot = loader.load();
            tagView = loader.getController();
            tagView.setListener(this);
            setCurrentSong(song);
            tagView.initialize(song);
            stage.setScene(new Scene(tagRoot));
        } catch (IOException e) {
            new PopupView("loading_error_title", I18n.get("error_loading_tag_view"), PopupType.ERROR);
        }
    }

    /**
     * Closes the tag view and opens the song options view.
     */
    public void close() {
        listener.returnToSongOptionView(currentSong);
        this.currentSong = null;
    }

    /**
     * Submits a custom tag for the current song.
     *
     * @param tagText The text of the custom tag to be submitted.
     */
    public void customTagSubmit(String tagText) {
        try {
            tagService.submitCustomTag(currentSong, tagText);
            listener.updateCurrentCollection();
        } catch (SQLExceptionHandler | InvalidTagError | BannedWordException e) {
            new PopupView("error_submitting_tag", e.getMessage(), PopupType.ERROR);
        }
    }
}
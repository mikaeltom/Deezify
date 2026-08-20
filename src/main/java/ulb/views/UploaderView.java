package ulb.views;

import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

/**
 * A class that provides utility methods for creating a FileChooser dialog for selecting songs.
 */
public class UploaderView {

    /**
     * Creates a FileChooser.ExtensionFilter from the given description and extensions.
     *
     * @param description A short description of the file type that this filter corresponds to.
     * @param extensions  The extensions that this filter will accept. All extensions must start with a period.
     * @return A FileChooser.ExtensionFilter
     */
    private FileChooser.ExtensionFilter getExtensionFilter(String description, String... extensions) {
        return new FileChooser.ExtensionFilter(description, extensions);
    }

    /**
     * Displays a file chooser dialog to select a file.
     *
     * @param stage       The stage on which the file chooser dialog will be displayed.
     * @param title       The title of the file chooser dialog.
     * @param description A short description of the file type that this filter corresponds to.
     * @param extensions  The extensions that this filter will accept.
     * @return The selected file, or null if no file is selected.
     */
    public File showChooseFile(Stage stage, String title, String description, String... extensions) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(title);
        fileChooser.getExtensionFilters().add(getExtensionFilter(description, extensions));
        return fileChooser.showOpenDialog(stage);
    }
}

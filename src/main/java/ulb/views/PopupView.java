package ulb.views;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import ulb.utils.I18n;
import ulb.utils.PopupType;

import java.util.Objects;
import java.util.ResourceBundle;


/**
 * A class for displaying error popups.
 * <p>After creating an instance of this class, an error popup will be displayed
 * with the given title and message.
 */
public class PopupView {

    public PopupView(String titleKey, String messageKey, PopupType type) {
        ResourceBundle bundle = I18n.getBundle();
        if (type == PopupType.SUCCESS) {
            String title = bundle.getString(titleKey);
            String message = bundle.getString(messageKey);
            show(title, message, type);
        } else {
            show(bundle.getString(titleKey), messageKey, type);
        }
    }

    /**
     * Displays an error popup with the given title and message.
     *
     * @param title   The title of the error popup.
     * @param message The message to display in the error popup.
     */
    private void show(String title, String message, PopupType type) {
        Alert alert = new Alert(Alert.AlertType.NONE);
        if (type == PopupType.ERROR) {
            alert = new Alert(Alert.AlertType.ERROR);
        }

        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.getButtonTypes().setAll(ButtonType.OK);
        setStyle(alert);
        alert.showAndWait();
    }

    /**
     * Applies the CSS style to the given alert dialog.
     *
     * @param alert The alert dialog to apply the style to.
     */
    private void setStyle(Alert alert) {
        DialogPane dialogPane = alert.getDialogPane();

        dialogPane.getStyleClass().add("dialog-pane");

        String cssFile = Objects.requireNonNull(getClass().getResource("/style/style.css")).toExternalForm();
        dialogPane.getStylesheets().add(cssFile);
    }
}

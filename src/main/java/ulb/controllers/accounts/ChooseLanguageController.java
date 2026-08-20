package ulb.controllers.accounts;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ulb.controllers.MainControllerAccess;
import ulb.exceptions.songs.SQLExceptionHandler;
import ulb.utils.I18n;
import ulb.utils.PopupType;
import ulb.views.accounts.ChooseLanguageView;
import ulb.views.PopupView;

import java.io.IOException;

/**
 * Controller for the ChooseLanguageView.
 * This class is responsible for handling the logic of the ChooseLanguageView.
 */
public class ChooseLanguageController implements ChooseLanguageView.ChooseLanguageViewListener {
    private Stage stage;

    private MainControllerAccess mainControllerAccess;

    public void show() {
        try {
            ChooseLanguageView view;
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ulb/views/ChooseLanguageView.fxml"),
                    I18n.getBundle());
            AnchorPane root = loader.load();
            view = loader.getController();
            view.setListener(this);
            Scene scene = new Scene(root);
            stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        } catch (IOException e) {
            new PopupView("loading_error_title", I18n.get("error_loading_choose_language"), PopupType.ERROR);
        }
    }

    /**
     * Updates the language of the application.
     * This method is called when the user selects a new language.
     * It updates the language in the LanguageService and notifies the
     * MainControllerAccess.
     *
     * @param language The new language to be set.
     */
    private void updateLanguage(String language) throws SQLExceptionHandler {
        I18n.updateLanguage(language);
    }

    /**
     * Changes the language to French.
     */
    public void changeToFrench() {
        handleUpdate("fr");
    }

    /**
     * Changes the language to English.
     */
    public void changeToEnglish() {
        handleUpdate("en");
    }

    /**
     * Changes the language to Dutch.
     */
    public void changeToDutch() {
        handleUpdate("nl");
    }

    /**
     * Handles the update of the language.
     * This method is called when the user selects a new language.
     * It updates the language in the LanguageService and notifies the
     * MainControllerAccess and close the window.
     *
     * @param language The new language to be set.
     */
    private void handleUpdate(String language) {
        try {
            updateLanguage(language);
            mainControllerAccess.changeLanguage();
        } catch (SQLExceptionHandler e) {
            new PopupView("error_failed_update_language", e.getMessage(), PopupType.ERROR);
        }
        stage.hide();
    }

    public void setMainControllerAccess(MainControllerAccess mainControllerAccess) {
        this.mainControllerAccess = mainControllerAccess;
    }
}

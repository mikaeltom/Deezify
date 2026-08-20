package ulb.controllers;

import javafx.stage.Stage;
import ulb.exceptions.songs.SQLExceptionHandler;
import ulb.services.SQLService;
import ulb.utils.I18n;
import ulb.utils.PopupType;
import ulb.views.PopupView;

import java.io.IOException;

/**
 * The AppController class is responsible for managing the main application flow.
 * It handles the transition between the user controller and the main controller.
 */
public class AppController implements MainController.MainListener, UserController.UserControllerListener {

    private final Stage primaryStage;
    private final SQLService sqlService = SQLService.getInstance();
    private UserController userController;

    public AppController(Stage stage) {
        this.primaryStage = stage;
    }

    /**
     * Displays the user controller to the user.
     * This method creates a new user controller and shows it.
     * It is the entry point of the application.
     */
    public void show() {
        userController = new UserController(primaryStage);
        setAndShow();
    }

    /**
     * This method is used to set the user controller and show it.
     * It sets the listener for the user controller and then shows it.
     */
    private void setAndShow() {
        userController.setListener(this);
        userController.show();
    }

    /**
     * Asks the main controller to handle the user login or registration success.
     * This method is called when the user has successfully logged in or registered.
     * It sets the user and language in the SQLService and then shows the main controller.
     * If the user has changed the language, it sets the language in the SQLService.
     * If any error occurs, a popup view is shown with the error message.
     *
     * @param userID   The userId of the user.
     * @param language The language selected by the user.
     */
    @Override
    public void onAskMainController(int userID, String language) {
        try {
            sqlService.setUserById(userID);
            if (userController.isChangedLanguage()) {
                sqlService.setLanguage(language);
            }
            MainController mainController = new MainController(primaryStage);
            mainController.setListener(this);
            mainController.show();
        } catch (SQLExceptionHandler | IOException e) {
            new PopupView("loading_error_title", I18n.get("error_loading_main"), PopupType.ERROR);
        }
    }

    /**
     * Asks the user controller to handle the user request.
     * This method is called when the user wants to go back to the user choice view.
     * It sets the user to null in the SQLService and then shows the user controller.
     * If any error occurs, a popup view is shown with the error message.
     */
    @Override
    public void onAskUserController() {
        sqlService.setUser(null);
        show();
    }
}

package ulb.controllers;

import javafx.stage.Stage;
import ulb.controllers.accounts.LoginController;
import ulb.controllers.accounts.RegisterController;
import ulb.controllers.accounts.UserChoiceController;
import ulb.exceptions.songs.SQLExceptionHandler;
import ulb.utils.I18n;
import ulb.utils.PopupType;
import ulb.views.PopupView;

/**
 * Controller for managing user authentication interactions in the application.
 * This class handles the user choice view, login, and registration controllers.
 */
public class UserController implements
        UserChoiceController.UserChoiceListener,
        LoginController.LoginListener,
        RegisterController.RegisterListener {

    private final Stage stage;
    private UserControllerListener listener;
    private boolean changedLanguage = false;

    public UserController(Stage primaryStage) {
        this.stage = primaryStage;
    }

    /**
     * Tell if the user has changed the language.
     * 
     * @return true if the user has changed the language, false otherwise.
     */
    public boolean isChangedLanguage() {
        return this.changedLanguage;
    }

    public void setListener(UserControllerListener listener) {
        this.listener = listener;
    }

    public void show() {
        showUserChoice();
    }

    /**
     * Displays the user choice view.
     * This view allows the user to choose between login and registration.
     */
    private void showUserChoice() {
        UserChoiceController userChoiceController = new UserChoiceController(stage);
        userChoiceController.setListener(this);
        userChoiceController.show();
    }

    /**
     * Displays the login view.
     * This view allows the user to log in to their account.
     */
    private void showLogin() {
        LoginController loginController = new LoginController(stage);
        loginController.setListener(this);
        loginController.show();
    }

    /**
     * Displays the registration view.
     * This view allows the user to create a new account.
     */
    private void showRegister() {
        RegisterController registerController = new RegisterController(stage);
        registerController.setListener(this);
        registerController.show();
    }

    private String getCurrentLanguage() {
        return I18n.getLanguage();
    }

    /**
     * Handles the back button click event.
     * This method is called when the user wants to go back to the user choice view.
     */
    @Override
    public void onLoginSelected() {
        showLogin();
    }

    /**
     * Handles the register button click event.
     * This method is called when the user wants to create a new account.
     */
    @Override
    public void onRegisterSelected() {
        showRegister();
    }

    /**
     * Handles the profile icon click event.
     * This method is called when the user selects an existing profile to log in.
     * Goes to the application main controller.
     * @param userID The userId of the profile.
     */
    @Override
    public void onLoginSuccess(int userID) {
        askMainController(userID);
    }

    /**
     * Handles the register success event.
     * This method is called when the user successfully registers a new account.
     * Goes to the application main controller.
     * @param userID The userId of the newly registered user.
     */
    @Override
    public void onRegisterSuccess(int userID) {
        askMainController(userID);
    }

    /**
     * Asks the main controller to handle the user login or registration success.
     * @param userID The userId of the logged-in or registered user.
     */
    private void askMainController(int userID) {
        if (listener != null) {
            listener.onAskMainController(userID, getCurrentLanguage());
        }
    }

    /**
     * Handles the back button click event.
     * This method is called when the user wants to go back to the user choice view.
     */
    @Override
    public void backToUserChoice() {
        showUserChoice();
    }

    /**
     * Handles the language selection event.
     * This method is called when the user selects a new language from the language selection view.
     * It changes the language of the application and shows the user choice view again.
     *
     * @param selectedCode The ISO 639 language code of the selected language.
     */
    @Override
    public void languageSelected(String selectedCode) {
        changedLanguage = true;

        try {
            I18n.setLanguage(selectedCode);
        } catch (SQLExceptionHandler e) {
            new PopupView("error_changing_language", I18n.get("error_cannot_change_language"), PopupType.ERROR);
            return;
        }

        showUserChoice();
    }

    public interface UserControllerListener {
        void onAskMainController(int userID, String language);
    }
}

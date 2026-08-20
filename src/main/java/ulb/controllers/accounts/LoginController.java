package ulb.controllers.accounts;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ulb.exceptions.credentials.InvalidLoginException;
import ulb.exceptions.songs.SQLExceptionHandler;
import ulb.services.accounts.LoginService;
import ulb.utils.I18n;
import ulb.utils.PopupType;
import ulb.views.PopupView;
import ulb.views.accounts.LoginView;
import ulb.views.accounts.LoginView.LoginViewListener;

import java.io.IOException;

/**
 * Controller for the login view.
 * This class handles the logic for the login view, including user
 * authentication and navigation.
 */
public class LoginController implements LoginViewListener {
    private final LoginService loginService = new LoginService();
    private final Stage stage;

    private LoginListener listener;
    private boolean keepLogged = false;

    public LoginController(Stage stage) {
        this.stage = stage;
    }

    public void setListener(LoginListener listener) {
        this.listener = listener;
    }

    /**
     * Displays the login view.
     * Loads the FXML file and sets up the scene and stage.
     */
    public void show() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ulb/views/Login.fxml"), I18n.getBundle());
            Parent root = loader.load();
            LoginView loginView = loader.getController();
            loginView.setListener(this);
            stage.setScene(new Scene(root));
            stage.setMinWidth(800);
            stage.setMinHeight(600);
            stage.show();
        } catch (IOException e) {
            new PopupView("loading_error_title", I18n.get("error_login_view"), PopupType.ERROR);
        }
    }

    /**
     * Handle the back button click event.
     */
    @Override
    public void back() {
        listener.backToUserChoice();
    }

    /**
     * Handle the login button click event.
     * Validates the user credentials and attempts to log in.
     * If successful, updates the stay logged status.
     *
     * @param username The username entered by the user.
     * @param password The password entered by the user.
     */
    @Override
    public void saveLogInformation(String username, String password) {
        try {
            int userId = loginService.login(username, password, keepLogged);
            listener.onLoginSuccess(userId);
        } catch (InvalidLoginException | SQLExceptionHandler e) {
            new PopupView("error_login", e.getMessage(), PopupType.ERROR);
        }
    }

    /**
     * Handle the register button click event.
     * Navigates to the registration view.
     */
    @Override
    public void handleRegisterClick() {
        listener.onRegisterSelected();
    }

    /**
     * Handle the checkbox click event.
     * Updates the keepLogged status based on the checkbox state.
     *
     * @param isChecked The state of the checkbox.
     */
    @Override
    public void checkBoxClicked(boolean isChecked) {
        this.keepLogged = isChecked;
    }

    public interface LoginListener {
        void onLoginSuccess(int userId);

        void onRegisterSelected();

        void backToUserChoice();
    }
}

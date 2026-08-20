package ulb.views.accounts;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;


/**
 * LoginView class represents the login view of the application.
 * It contains the UI elements and their corresponding actions.
 * This class is responsible for handling user interactions
 * and notifying the listener about those interactions.
 */
public class LoginView {

    @FXML
    private Button backButton;

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private CheckBox keepLoggedCheckbox;

    @FXML
    private Button loginButton;

    @FXML
    private Label goToRegisterLabel;

    private LoginViewListener listener;

    /**
     * Initializes the LoginView by setting up the button actions.
     */
    public void initialize() {
        setUpCheckboxStyle();
        setUpBackButtonAction();
        setUpLoginButtonAction();
        setUpGoToRegisterLabelAction();
        setUpKeepLoggedCheckboxAction();
    }

    private void setUpCheckboxStyle() {
        keepLoggedCheckbox.setStyle("-fx-background-color: #131318; -fx-border-color: #1DB954;");
    }

    private void setUpBackButtonAction() {
        backButton.setOnAction(e -> listener.back());
    }

    /**
     * Sets up the action for the login button.
     * When clicked, it retrieves the username and password from the text fields
     * and notifies the listener to handle the login action.
     */

    private void setUpLoginButtonAction() {
        loginButton.setOnAction(e -> {
            String username = usernameField.getText();
            String password = passwordField.getText();
            listener.saveLogInformation(username, password);
        });
    }

    private void setUpGoToRegisterLabelAction() {
        goToRegisterLabel.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> listener.handleRegisterClick());
    }

    private void setUpKeepLoggedCheckboxAction() {
        keepLoggedCheckbox.setOnAction(e -> listener.checkBoxClicked(keepLoggedCheckbox.isSelected()));
    }

    public void setListener(LoginViewListener listener) {
        this.listener = listener;
    }

    public interface LoginViewListener {
        void back();

        void saveLogInformation(String Username, String Password);

        void handleRegisterClick();

        void checkBoxClicked(boolean isChecked);
    }
}

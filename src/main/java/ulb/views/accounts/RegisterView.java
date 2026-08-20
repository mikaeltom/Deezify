package ulb.views.accounts;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;


/**
 * RegisterView class represents the registration view of the application.
 * It contains the UI elements and their corresponding actions.
 * This class is responsible for handling user interactions
 * and notifying the listener about those interactions.
 */
public class RegisterView {
    @FXML
    private Button backButton;

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private PasswordField confirmPasswordField;

    @FXML
    private CheckBox keepLoggedCheckbox;

    @FXML
    private Button registerButton;

    @FXML
    private Label goToRegisterLabel;

    @FXML
    private Button profileImageButton;

    private RegisterViewListener listener;

    /**
     * Initializes the RegisterView by setting up the button actions.
     * This method is automatically called by JavaFX when the FXML is loaded.
     */
    public void initialize() {
        setUpCheckboxStyle();
        setUpBackButtonAction();
        setUpLoginButtonAction();
        setUpGoToLoginLabelAction();
        setUpKeepLoggedCheckboxAction();
        setupProfileImageButtonAction();
    }

    public void setListener(RegisterViewListener listener) {
        this.listener = listener;
    }

    private void setUpCheckboxStyle() {
        keepLoggedCheckbox.setStyle("-fx-background-color: #131318; -fx-border-color: #1DB954;");
    }

    private void setUpBackButtonAction() {
        backButton.setOnAction(e -> listener.back());
    }

    /**
     * Sets up the action for the register button.
     * When clicked, it retrieves the username, password, and confirmation password
     * from the text fields and notifies the listener to handle the registration process.
     */
    private void setUpLoginButtonAction() {
        registerButton.setOnAction(e -> {
            String username = usernameField.getText();
            String password = passwordField.getText();
            String passwordConfirmation = confirmPasswordField.getText();
            listener.handleRegisterClick(username, password, passwordConfirmation);
        });
    }

    private void setUpGoToLoginLabelAction() {
        goToRegisterLabel.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> listener.handleLoginClick());
    }

    private void setUpKeepLoggedCheckboxAction() {
        keepLoggedCheckbox.setOnAction(e -> listener.checkBoxClicked(keepLoggedCheckbox.isSelected()));
    }

    private void setupProfileImageButtonAction() {
        profileImageButton.setOnAction(e -> listener.handleUploadImage());
    }

    public interface RegisterViewListener {
        void handleUploadImage();

        void back();

        void handleRegisterClick(String username, String password, String confirmationPassword);

        void handleLoginClick();

        void checkBoxClicked(boolean isChecked);

    }
}
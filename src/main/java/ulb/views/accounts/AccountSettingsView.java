package ulb.views.accounts;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;
import ulb.dtos.UserDTO;


/**
 * The AccountSettingsView class is responsible for displaying the account settings
 * view to the user. It contains buttons for deleting the account and logging out.
 */
public class AccountSettingsView {

    @FXML
    public Button saveButton;

    @FXML
    public Button deleteProfileImageButton;

    @FXML
    private TextField usernameField;

    @FXML
    private TextField passwordField, confirmPasswordField;

    @FXML
    private CheckBox keepLoggedCheckbox;

    @FXML
    private Button profileImageButton;

    @FXML
    private Button deleteButton;

    @FXML
    private Button logoutButton;

    @FXML
    private ImageView profileImageView;


    private AccountSettingsViewListener listener;

    /**
     * Initializes the AccountSettingsView by setting up the button actions.
     */
    public void initialize() {
        setupSaveButton();
        setupProfileImageButton();
        setupKeepLoggedCheckBox();
        setupProfileImageButton();
        setupDeleteImageButton();
        setupDeleteButton();
        setupLogoutButton();
    }

    /**
     * Sets the user information in the account settings view.
     *
     * @param userDTO The user data transfer object containing user information.
     */
    public void setUser(UserDTO userDTO) {
        usernameField.setText(userDTO.getUsername());
        keepLoggedCheckbox.setSelected(userDTO.isStayLogged());
        String imagePath = userDTO.getProfileImagePath();
        setProfileImageView(imagePath);
    }

    /**
     * Sets the profile image in the account settings view.
     * If the provided path is valid, it loads the image from the file system.
     * Otherwise, it sets a default image.
     * The image is resized to fit the image view size.
     * A circle clip is applied to the image.
     *
     * @param imagePath The path to the profile image.
     */
    public void setProfileImageView(String imagePath) {
        Image image;
        if (imagePath != null && !imagePath.isEmpty()) {
            image = new Image("file:" + imagePath);

        } else {
            image = new Image("file:src/main/resources/images/account-settings.png");
        }
        profileImageView.setFitWidth(48);
        profileImageView.setFitHeight(48);
        profileImageView.setImage(image);
        Circle clip = new Circle(24, 24, 24);
        profileImageView.setClip(clip);
    }


    public void changeStayLogged(boolean keepLogged) {
        keepLoggedCheckbox.setSelected(keepLogged);
    }

    /**
     * Sets up the action for the delete button.
     * When clicked, it notifies the listener to handle the delete account action.
     */
    private void setupDeleteButton() {
        deleteButton.setOnAction(event -> {
            if (listener != null) {
                listener.handleDeleteAccount();
            }
        });
    }

    /**
     * Sets up the action for the logout button.
     * When clicked, it notifies the listener to handle the logout action.
     */
    private void setupLogoutButton() {
        logoutButton.setOnAction(event -> {
            if (listener != null) {
                listener.handleLogout();
            }
        });
    }

    /**
     * Sets up the action for the save button.
     * When clicked, it retrieves the username and password from the text fields
     * and notifies the listener to handle the save action.
     */
    private void setupSaveButton() {
        saveButton.setOnAction(event -> {
            String username = usernameField.getText();
            String password = passwordField.getText();
            String confirmPassword = confirmPasswordField.getText();
            if (listener != null) {
                listener.handleSave(username, password, confirmPassword);
            }
        });
    }

    /**
     * Sets up the action for the profile image button.
     * When clicked, it notifies the listener to handle the profile image action.
     */
    private void setupProfileImageButton() {
        profileImageButton.setOnAction(event -> {
            if (listener != null) {
                listener.handleProfileImage();
            }
        });
    }

    /**
     * Sets up the action for the keep logged checkbox.
     * When clicked, it notifies the listener to handle the stay logged action.
     * The listener is notified with the selected state of the checkbox.
     */
    private void setupKeepLoggedCheckBox() {
        keepLoggedCheckbox.setOnAction(event -> {
            if (listener != null) {
                listener.handleStayLoggedIn(keepLoggedCheckbox.isSelected());
            }
        });
    }

    /**
     * Sets up the action for the delete profile image button.
     * When clicked, it notifies the listener to handle the delete profile image action.
     */
    private void setupDeleteImageButton() {
        deleteProfileImageButton.setOnAction(event -> {
            if (listener != null) {
                listener.handleDeleteImage();
            }
        });
    }

    public void setListener(AccountSettingsViewListener listener) {
        this.listener = listener;
    }

    public interface AccountSettingsViewListener {
        void handleDeleteAccount();

        void handleLogout();

        void handleSave(String username, String password, String confirmPassword);

        void handleStayLoggedIn(boolean stayLoggedIn);

        void handleProfileImage();

        void handleDeleteImage();
    }
}

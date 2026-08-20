package ulb.controllers.accounts;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ulb.controllers.uploaders.ProfileImageUploader;
import ulb.exceptions.BannedWordException;
import ulb.exceptions.credentials.InvalidPasswordException;
import ulb.exceptions.credentials.InvalidRegisterException;
import ulb.exceptions.credentials.InvalidUsernameException;
import ulb.exceptions.credentials.NotMatchingPasswordsException;
import ulb.exceptions.songs.SQLExceptionHandler;
import ulb.services.accounts.RegisterService;
import ulb.utils.I18n;
import ulb.utils.PopupType;
import ulb.views.PopupView;
import ulb.views.accounts.RegisterView;

import java.io.IOException;

/**
 * Controller for the registration view.
 * This class handles the logic for the registration view, including user
 * registration and navigation.
 */
public class RegisterController implements RegisterView.RegisterViewListener {
    private final RegisterService registerService = new RegisterService();
    private final ProfileImageUploader profileImageUploader = new ProfileImageUploader();
    private final Stage stage;
    private RegisterListener listener;
    private boolean keepLogged = false;
    private String profileImagePath;
    private String currentLanguage;

    public RegisterController(Stage stage) {
        this.stage = stage;
    }

    public void setListener(RegisterListener listener) {
        this.listener = listener;
    }

    public void show() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ulb/views/RegisterView.fxml"),
                    I18n.getBundle());
            this.currentLanguage = I18n.getLanguage();
            loader.load();
            RegisterView registerView = loader.getController();
            registerView.setListener(this);
            stage.setScene(new Scene(loader.getRoot()));
            stage.setMinWidth(800);
            stage.setMinHeight(600);
            stage.show();

        } catch (IOException e) {
            new PopupView("loading_error_title", I18n.get("error_login_view"), PopupType.ERROR);
        }
    }

    /**
     * Clears the selected image path.
     */
    private void clearSelectedImagePath() {
        profileImagePath = null;
    }

    /**
     * Handles the upload of an image.
     * This method is called when the user selects an image to upload.
     */
    @Override
    public void handleUploadImage() {
        profileImagePath = profileImageUploader.getSelectedProfileImagePath(stage);
    }

    /**
     * Handles the click event of the back button.
     * This method is called when the user clicks the back button.
     */
    @Override
    public void back() {
        clearSelectedImagePath();
        listener.backToUserChoice();
    }

    /**
     * Handles the click event of the register button.
     * This method is called when the user clicks the register button.
     * @param username             The username entered by the user.
     * @param password             The password entered by the user.
     * @param confirmationPassword The confirmation password entered by the user.
     */
    @Override
    public void handleRegisterClick(String username, String password, String confirmationPassword) {
        try {
            int userId = registerService.registerNewUser(username, password, confirmationPassword, profileImagePath,
                    keepLogged, currentLanguage);
            listener.onRegisterSuccess(userId);
            profileImageUploader.savePicture();
        } catch (IOException | SQLExceptionHandler | InvalidRegisterException | NotMatchingPasswordsException
                | InvalidUsernameException | InvalidPasswordException | BannedWordException e) {
            new PopupView("error_register", e.getMessage(), PopupType.ERROR);
        }
    }

    /**
     * Handles the click event of the login button.
     * This method is called when the user clicks the login button and navigates to
     * the login view.
     */
    @Override
    public void handleLoginClick() {
        clearSelectedImagePath();
        listener.onLoginSelected();
    }

    /**
     * Handles the click event of the checkbox.
     * This method is called when the user clicks the checkbox to keep logged in.
     * @param isChecked The state of the checkbox.
     */
    @Override
    public void checkBoxClicked(boolean isChecked) {
        this.keepLogged = isChecked;
    }

    public interface RegisterListener {
        void onRegisterSuccess(int userId);

        void backToUserChoice();

        void onLoginSelected();
    }
}

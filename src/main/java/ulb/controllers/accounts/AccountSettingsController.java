package ulb.controllers.accounts;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ulb.controllers.MainControllerAccess;
import ulb.controllers.uploaders.ProfileImageUploader;
import ulb.exceptions.BannedWordException;
import ulb.exceptions.credentials.InvalidPasswordException;
import ulb.exceptions.credentials.InvalidUsernameException;
import ulb.exceptions.credentials.NotMatchingPasswordsException;
import ulb.exceptions.songs.SQLExceptionHandler;
import ulb.models.User;
import ulb.services.accounts.AccountSettingsService;
import ulb.utils.I18n;
import ulb.utils.PopupType;
import ulb.views.accounts.AccountSettingsView;
import ulb.views.PopupView;

import java.io.IOException;

/**
 * The AccountSettingsController class is responsible for handling the account
 * settings view. It allows the user to delete their account or log out of the
 * application.
 */
public class AccountSettingsController implements AccountSettingsView.AccountSettingsViewListener {
    private final MainControllerAccess mainControllerAccess;
    private final AccountSettingsService accountSettingsService = new AccountSettingsService();
    private final ProfileImageUploader profileImageUploader = new ProfileImageUploader();
    private final User user = accountSettingsService.getCurrentUser();
    private boolean stayLoggedIn = accountSettingsService.isStayLogged();
    private String profileImagePath;
    private AccountSettingsView view;
    private Stage stage;

    public AccountSettingsController(MainControllerAccess mainControllerAccess) {
        this.mainControllerAccess = mainControllerAccess;
    }

    /**
     * Displays the account settings view as a new window.
     */
    public void show() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ulb/views/AccountSettings.fxml"),
                    I18n.getBundle());
            Parent popupRoot = loader.load();
            this.view = loader.getController();
            this.view.setListener(this);
            this.view.setUser(user);
            this.stage = new Stage();
            this.stage.initModality(Modality.APPLICATION_MODAL);
            setupAccountSettingsWindow(stage, popupRoot);
        } catch (IOException e) {
            new PopupView("loading_error_title", I18n.get("loading_error_account_settings"), PopupType.ERROR);
        }
    }

    /**
     * Sets up the account settings window.
     *
     * @param popupStage The popup window to be set up.
     * @param popupRoot  The root of the popup window.
     */
    private void setupAccountSettingsWindow(Stage popupStage, Parent popupRoot) {
        popupStage.setScene(new Scene(popupRoot));
        popupStage.setResizable(false);
        popupStage.centerOnScreen();
        popupStage.show();
    }

    /**
     * Handles the delete account button click event.
     * It removes the user from the database and notifies the listener.
     */
    @Override
    public void handleDeleteAccount() {
        try {
            this.accountSettingsService.removeUser();
            this.mainControllerAccess.logout();
            this.stage.hide();
        } catch (SQLExceptionHandler | IOException e) {
            new PopupView("error_deleting_account_title", I18n.get("error_deleting_account"), PopupType.ERROR);
        }
    }

    /**
     * Handles the logout button click event.
     * It logs out the user and notifies the listener.
     */
    @Override
    public void handleLogout() {
        this.mainControllerAccess.logout();
        this.stage.hide();
    }

    /**
     * Handles the save button click event.
     * It updates the user information in the database.
     */
    @Override
    public void handleSave(String username, String password, String confirmPassword) {
        try {
            this.accountSettingsService.updateUser(username, password, confirmPassword, profileImagePath, stayLoggedIn);
            this.profileImageUploader.savePicture();
            if (profileImagePath != null) {
                new PopupView("account_settings_title", "account_settings_success", PopupType.SUCCESS);
            }
            this.stage.hide();
        } catch (SQLExceptionHandler | NotMatchingPasswordsException | InvalidUsernameException | IOException
                | InvalidPasswordException | BannedWordException e) {
            new PopupView("error_editing_settings", e.getMessage(), PopupType.ERROR);
        }
    }

    /**
     * Handles the stay logged in checkbox click event.
     * It updates the stay logged in status of the user.
     */
    @Override
    public void handleStayLoggedIn(boolean stayLoggedIn) {
        this.stayLoggedIn = stayLoggedIn;
        this.view.changeStayLogged(stayLoggedIn);
    }

    /**
     * Handles the profile image button click event.
     * It opens a file chooser to select a new profile image.
     */
    @Override
    public void handleProfileImage() {
        profileImagePath = profileImageUploader.getSelectedProfileImagePath(stage);
        if (profileImagePath != null) {
            this.view.setProfileImageView(profileImagePath);
        }
    }

    /**
     * Handles the delete image button click event.
     * It deletes the profile image of the user.
     */
    @Override
    public void handleDeleteImage() {
        try {
            profileImageUploader.deleteItem(user);
            this.view.setProfileImageView(null);
            new PopupView("delete_profile_picture_title", "delete_profile_picture_success",
                    PopupType.SUCCESS);
            this.stage.hide();
        } catch (SQLExceptionHandler | IOException e) {
            new PopupView("error_deleting_image_title", I18n.get("error_deleting_image"), PopupType.ERROR);
        }
    }
}

package ulb.controllers.accounts;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ulb.exceptions.songs.SQLExceptionHandler;
import ulb.models.User;
import ulb.services.SQLService;
import ulb.utils.I18n;
import ulb.utils.PopupType;
import ulb.views.PopupView;
import ulb.views.accounts.UserChoiceView;
import ulb.views.accounts.UserChoiceView.UserChoiceViewListener;

import java.io.IOException;
import java.util.List;

/**
 * Controller for the user choice view.
 * This class handles the logic for the user choice view, including displaying
 * user profiles and handling user selection.
 */
public class UserChoiceController implements UserChoiceViewListener {
    private final SQLService sqlService = SQLService.getInstance();
    private final Stage stage;
    private UserChoiceListener listener;

    public UserChoiceController(Stage stage) {
        this.stage = stage;
    }

    public void setListener(UserChoiceListener listener) {
        this.listener = listener;
    }

    public void show() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ulb/views/UserChoice.fxml"), I18n.getBundle());
            loader.load();
            UserChoiceView userChoiceView = loader.getController();
            userChoiceView.setListener(this);
            List<User> stayLoggedUsers = this.sqlService.getAllStayLoggedUsers();
            userChoiceView.setStayLoggedUsers(stayLoggedUsers);
            userChoiceView.createUserCircle();
            Parent root = loader.getRoot();
            Scene scene = new Scene(root);
            stage.setMinWidth(800);
            stage.setMinHeight(600);
            stage.setScene(scene);
            stage.show();
        } catch (IOException | SQLExceptionHandler e) {
            new PopupView("loading_error_title", I18n.get("error_loading_userchoice"), PopupType.ERROR);
        }
    }

    /**
     * Handle the profile icon click event. When we click on a profile icon,
     * it means that we click on an already existing profile that enabled 'stay
     * logged'.
     *
     * @param username The name of the profile that is being clicked on.
     */
    @Override
    public void profileClicked(String username) {
        try {
            int userId = sqlService.getUserId(username);
            listener.onLoginSuccess(userId);
        } catch (SQLExceptionHandler e) {
            new PopupView("error_selecting", e.getMessage(), PopupType.ERROR);
        }
    }

    /**
     * Handle the click event of the "Add New Profile" button. It goes to the login
     * view.
     */
    @Override
    public void addNewProfileClicked() {
        listener.onLoginSelected();
    }

    /**
     * Handle the click event of the "Change Language" button. It goes to the
     * language selection view.
     */
    @Override
    public void onLanguageClicked(String selectedCode) {
        listener.languageSelected(selectedCode);
    }

    public interface UserChoiceListener {
        void onLoginSelected();

        void onLoginSuccess(int userID);

        void languageSelected(String selectedCode);
    }
}

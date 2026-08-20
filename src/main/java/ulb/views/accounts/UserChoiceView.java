package ulb.views.accounts;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import ulb.dtos.UserDTO;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;


/**
 * UserChoiceView class represents the user choice view of the application.
 * It contains the UI elements and their corresponding actions.
 * This class is responsible for handling user interactions
 * and notifying the listener about those interactions.
 */
public class UserChoiceView {

    private final Map<String, String> languageCodeMap = new HashMap<>();
    private UserChoiceViewListener listener;
    @FXML
    private HBox circleContainer;
    @FXML
    private ComboBox<String> languageComboBox;
    @FXML
    private ResourceBundle resources;
    private List<UserDTO> stayLoggedUsers;

    public void initialize() {
        setupLangagueSelection();
    }

    /**
     * Sets up the language selection combo box.
     * This method populates the combo box with available languages
     * and sets the action listener for language selection.
     */
    public void setupLangagueSelection() {
        languageCodeMap.put("Français", "fr");
        languageCodeMap.put("English", "en");
        languageCodeMap.put("Nederlands", "nl");
        setSelectedLanguage(resources.getLocale().getLanguage());
        languageComboBox.setOnAction(event -> {
            String selectedLabel = languageComboBox.getSelectionModel().getSelectedItem();
            String selectedCode = languageCodeMap.get(selectedLabel);
            listener.onLanguageClicked(selectedCode);
        });
    }

    /**
     * Sets the selected language in the language combo box.
     * This method iterates through the languageCodeMap and selects the corresponding label
     * based on the provided language code.
     *
     * @param languageCode The language code to be set.
     */
    private void setSelectedLanguage(String languageCode) {
        for (Map.Entry<String, String> entry : languageCodeMap.entrySet()) {
            if (entry.getValue().equals(languageCode)) {
                languageComboBox.getSelectionModel().select(entry.getKey());
                break;
            }
        }
    }

    public void setStayLoggedUsers(List<? extends UserDTO> stayLoggedUsers) {
        this.stayLoggedUsers = new ArrayList<>(stayLoggedUsers);
    }

    /**
     * Initializes the user choice view.
     * This method sets up the layout and generates user avatars.
     */
    public void createUserCircle() {
        circleContainer.setAlignment(Pos.CENTER);
        generateUserAvatars();
        addNewUserButton();
    }

    public void setListener(UserChoiceViewListener listener) {
        this.listener = listener;
    }

    /**
     * Generates user avatars based on the provided list of users.
     * Each user is represented by a colored circle or an image, depending on their profile settings.
     */
    private void generateUserAvatars() {
        Random random = new Random(1);

        for (UserDTO userDTO : stayLoggedUsers) {
            Color avatarColor = getRandomColor(random);
            VBox userBox = createUserBox(userDTO, avatarColor);
            circleContainer.getChildren().add(userBox);
        }
    }

    /**
     * Generates a random color for the user avatar.
     * The color is generated using HSB (Hue, Saturation, Brightness) values.
     *
     * @param random Random object to generate random values.
     * @return A random color for the user avatar.
     */
    private Color getRandomColor(Random random) {
        return Color.hsb(random.nextInt(360), 0.5, 0.85);
    }


    /**
     * Creates a user box containing the user's avatar and name.
     * The avatar is represented by a colored circle or an image, depending on the user's profile settings.
     *
     * @param user  The user object containing user information.
     * @param color The color for the avatar circle.
     * @return A VBox containing the user's avatar and name.
     */
    private VBox createUserBox(UserDTO user, Color color) {
        String username = user.getUsername();
        String imagePath = user.getProfileImagePath();

        StackPane avatarPane = createAvatarPane(username, imagePath, color);
        avatarPane.setPrefSize(100, 100);

        Label nameLabel = createNameLabel(username);

        VBox box = new VBox(avatarPane, nameLabel);
        box.setAlignment(Pos.CENTER);
        addHoverEffect(box);
        box.setOnMouseClicked(e -> listener.profileClicked(username));

        return box;
    }

    /**
     * Creates a StackPane containing the user's avatar.
     * The avatar can be either an image or a colored circle, depending on the user's profile settings.
     *
     * @param username  The username of the user.
     * @param imagePath The path to the user's profile image.
     * @param color     The color for the avatar circle.
     * @return A StackPane containing the user's avatar.
     */
    private StackPane createAvatarPane(String username, String imagePath, Color color) {
        if (isValidImagePath(imagePath)) {
            return createImageAvatar(imagePath);
        } else {
            return createInitialAvatar(username, color);
        }
    }

    /**
     * Checks if the provided image path is valid.
     *
     * @param imagePath the path to the image
     * @return True if path is valid, otherwise False
     */
    private boolean isValidImagePath(String imagePath) {
        return imagePath != null &&
                !imagePath.isEmpty() &&
                !imagePath.equals("src/main/resources/profile/") &&
                Files.exists(Paths.get(imagePath));
    }

    /**
     * Creates a StackPane containing the user's image avatar.
     *
     * @param imagePath The path to the user's profile image.
     * @return A StackPane containing the user's image avatar.
     */
    private StackPane createImageAvatar(String imagePath) {
        ImageView imageView = new ImageView(new Image("file:" + imagePath));
        imageView.setFitWidth(100);
        imageView.setFitHeight(100);
        imageView.setClip(new Circle(50, 50, 50));
        return new StackPane(imageView);
    }

    /**
     * Creates a StackPane containing the user's initial avatar.
     *
     * @param username The username of the user.
     * @param color    The color for the avatar circle.
     * @return A StackPane containing the user's initial avatar.
     */
    private StackPane createInitialAvatar(String username, Color color) {
        Circle avatar = createAvatarCircle(color);
        Label initial = createInitialLabel(username);
        return new StackPane(avatar, initial);
    }

    /**
     * Creates a label for the user's name.
     *
     * @param username The username of the user.
     * @return A Label object representing the user's name.
     */
    private Label createNameLabel(String username) {
        Label nameLabel = new Label(username);
        nameLabel.getStyleClass().add("user-name-label");
        return nameLabel;
    }

    /**
     * Creates a circle for the user avatar.
     *
     * @param color The color of the circle.
     * @return A Circle object representing the user avatar.
     */
    private Circle createAvatarCircle(Color color) {
        Circle circle = new Circle(50);
        circle.getStyleClass().add("user-avatar-circle");
        circle.setFill(color);
        return circle;
    }

    /**
     * Creates a label for the user's initial.
     *
     * @param username The username of the user.
     * @return A Label object representing the user's initial.
     */
    private Label createInitialLabel(String username) {
        Label label = new Label(username.substring(0, 1).toUpperCase());
        label.getStyleClass().add("user-avatar-initial");
        return label;
    }

    private void addHoverEffect(VBox box) {
        box.getStyleClass().add("user-hover-effect");
    }


    /**
     * Adds a new user button to the user choice view.
     * This button allows the user to create a new profile.
     */
    private void addNewUserButton() {
        StackPane avatarPane = createAddUserAvatarPane();
        VBox addBox = createAddUserBox(avatarPane);

        addBox.setOnMouseClicked(e -> listener.addNewProfileClicked());
        circleContainer.getChildren().add(addBox);
    }

    /**
     * Creates a StackPane containing the add user avatar.
     * The avatar is represented by a colored circle with a "+" sign.
     *
     * @return A StackPane containing the add user avatar.
     */
    private StackPane createAddUserAvatarPane() {
        Circle addCircle = createAvatarCircle(Color.LIGHTGRAY);
        addCircle.getStyleClass().add("user-avatar-circle");

        Text plusText = new Text("+");
        plusText.getStyleClass().add("user-add-button-text");

        StackPane avatarPane = new StackPane(addCircle, plusText);
        avatarPane.setPrefSize(100, 100);

        return avatarPane;
    }

    /**
     * Creates a VBox containing the add user avatar and label.
     *
     * @param avatarPane The StackPane containing the add user avatar.
     * @return A VBox containing the add user avatar and label.
     */
    private VBox createAddUserBox(StackPane avatarPane) {
        Label label = new Label(" ");
        label.getStyleClass().add("user-name-label");

        VBox addBox = new VBox(avatarPane, label);
        addHoverEffect(addBox);
        addBox.setAlignment(Pos.CENTER);

        return addBox;
    }


    public interface UserChoiceViewListener {
        void profileClicked(String username);

        void addNewProfileClicked();

        void onLanguageClicked(String selectedCode);
    }
}

package ulb;

import javafx.application.Application;
import javafx.stage.Stage;
import ulb.controllers.AppController;

/**
 * Main class for the application.
 * This class initializes the JavaFX application and sets up the primary stage.
 */
public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        AppController appController = new AppController(primaryStage);
        appController.show();
    }
}

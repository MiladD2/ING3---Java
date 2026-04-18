package view;

import controller.MainController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class JavaFXApp extends Application {

    private MainController mainController;

    @Override
    public void start(Stage primaryStage) throws Exception {
        mainController = new MainController();
        ViewController.setMainController(mainController);

        Parent root = FXMLLoader.load(getClass().getResource("LoginView.fxml"));
        Scene scene = new Scene(root, 1100, 750);
        
        primaryStage.setTitle("JAVAZIK");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    @Override
    public void stop() {
        if (mainController != null) {
            mainController.sauvegarderDonnees();
        }
    }

    public static void main(String[] args) {
        Application.launch(JavaFXApp.class, args);
    }
}

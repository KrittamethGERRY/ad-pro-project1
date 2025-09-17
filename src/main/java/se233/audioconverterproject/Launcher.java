package se233.audioconverterproject;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Launcher extends Application {
    public static Stage primaryStage;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        primaryStage = stage;
        stage.setTitle("Audio File Converter");
        FXMLLoader fxmlLoader = new FXMLLoader(Launcher.class.getResource("main-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        scene.setRoot(fxmlLoader.getRoot());
        scene.getStylesheets().add(Launcher.class.getResource("Styles/index.css").toExternalForm());
        stage.setScene(scene);
        stage.show();
    }

}
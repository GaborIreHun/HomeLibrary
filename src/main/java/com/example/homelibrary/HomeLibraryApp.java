package com.example.homelibrary;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * This class represent the main method of the artifact
 * The class extends the Application superclass
 */
public class HomeLibraryApp extends Application {

    /**
     * @inheritDoc
     * @param stage
     * @throws IOException
     */
    @Override
    public void start(Stage stage) throws IOException {
        // Initializing FXMLLoader with the appropriate fxml file(main page)
        FXMLLoader fxmlLoader = new FXMLLoader(HomeLibraryApp.class.getResource("mainWindow.fxml"));
        // Loading setup to a new Scene object
        Scene scene = new Scene(fxmlLoader.load(), 640, 400);
        // Creating title
        stage.setTitle("Home Library");
        // Setting scene with set properties
        stage.setScene(scene);
        // Initializing/showing the window
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
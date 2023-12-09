package dev.kenuki.snakegamejavafx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("snake-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 900, 600);
        final SnakeController snakeController = fxmlLoader.getController();
        scene.setOnKeyPressed(snakeController::keyHandle);
        stage.setTitle("Snake Game");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
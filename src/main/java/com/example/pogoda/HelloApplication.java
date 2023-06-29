package com.example.pogoda;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 450, 800);
        stage.setTitle("Weather App!");
        stage.setResizable(false);
        stage.getIcons().add(new javafx.scene.image.Image("https://upload.wikimedia.org/wikipedia/commons/thumb/b/bf/Circle-icons-weather.svg/2048px-Circle-icons-weather.svg.png"));
        scene.setFill(new RadialGradient(0, 0, 0.5, 0.5, 1, true, null, new Stop(0, Color.web("#ff99ff")), new Stop(1, Color.web("#32ff12"))));
        stage.setScene(scene);
        stage.show();
    }
}
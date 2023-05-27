package com.example.pogoda;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import javafx.application.Application;
import javafx.application.HostServices;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.net.URL;
import java.util.Scanner;

public class HelloController extends Application {
    public TextField cityNameTextField;
    public ImageView weatherIcon;
    public Label googleMaps;
    public Label google;
    public Label wikipedia;
    public Label meow_fact;
    @FXML
    private Label welcomeText;
    @FXML
    public void initialize()  throws IOException  {
        meow_fact.setText(getMeow());
        meow_fact.setWrapText(true);
    }

    public void onShowWeatherButton() throws IOException {
        if (!(cityNameTextField != null && cityNameTextField.getText() != null && cityNameTextField.getText().length() > 0)) {
            welcomeText.setText("Please enter a city name");
        } else {
            welcomeText.setText("Showing forecast for " + cityNameTextField.getText());
            URL url = new URL("https://api.weatherapi.com/v1/current.json?key=ENTER_YOUR_KEY_HERE&q=" + cityNameTextField.getText() + "&aqi=no");
            HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Content-Type", "application/json; utf-8");

            int status = con.getResponseCode();

            if (!(status == 200)) {
                welcomeText.setText("Error: " + status);
            } else {
                StringBuilder inLine = new StringBuilder();
                Scanner scanner = new Scanner(url.openStream());

                while (scanner.hasNext()) {
                    inLine.append(scanner.nextLine());
                }
                System.out.println(inLine);


                JsonObject convertObj = new Gson().fromJson(inLine.toString(), JsonObject.class);


                String city_name = convertObj.get("location").getAsJsonObject().get("name").getAsString();
                welcomeText.setText("Weather in: " + city_name +
                        "\n: Temperature: " + convertObj.get("current").getAsJsonObject().get("temp_c").getAsString() +
                        "C\n Wind: " + convertObj.get("current").getAsJsonObject().get("wind_kph").getAsString() +
                        "kph\nPressure: " + convertObj.get("current").getAsJsonObject().get("pressure_mb").getAsString() +
                        "mb\nPrecipitation: " + convertObj.get("current").getAsJsonObject().get("precip_mm").getAsString() +
                        "mm\nHumidity: " + convertObj.get("current").getAsJsonObject().get("humidity").getAsString() +
                        "%\nCondition: " + convertObj.get("current").getAsJsonObject().get("condition").getAsJsonObject().get("text").getAsString());

                welcomeText.setStyle("-fx-text-fill: blue;");
                welcomeText.setStyle("-fx-font-size: 16px;");
                welcomeText.setWrapText(true);
                Image image = new Image("https:" + convertObj.get("current").getAsJsonObject().get("condition").getAsJsonObject().get("icon").getAsString());
                weatherIcon.setImage(image);


                HostServices hostServices = getHostServices();


            }
        }
    }

    public String getMeow() throws IOException {
        URL url = new URL("https://meowfacts.herokuapp.com/");
        HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("Content-Type", "application/json; utf-8");

        int status = con.getResponseCode();
        if (status!=200) {
            System.out.println("Error");
            return "I can't get the meow facts right now";
        }
        else {
            Scanner sc = new Scanner(url.openStream());
            StringBuilder inline = new StringBuilder();
            while (sc.hasNext()) {
                inline.append(sc.nextLine());
            }
            sc.close();

            JsonObject jsonObject = new Gson().fromJson(inline.toString(), JsonObject.class);
            return jsonObject.get("data").getAsString();
        }

    }

    @Override
    public void start(Stage stage) {

    }
}

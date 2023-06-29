package com.example.pogoda;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import javafx.application.Application;
import javafx.application.HostServices;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

import static org.assertj.core.api.Assertions.assertThat;


@SuppressWarnings({"ALL", "MismatchedQueryAndUpdateOfCollection"})
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
    public void initialize() {
        meow_fact.maxWidth(200);
        meow_fact.prefWidth(100);
        meow_fact.setWrapText(true);
        meow_fact.setText(getMeow());
        meow_fact.setMinHeight(Region.USE_PREF_SIZE);
    }

    public void onShowWeatherButton() {
        try {

            if (!(cityNameTextField != null && cityNameTextField.getText() != null && cityNameTextField.getText().length() > 0)) {
                welcomeText.setText("Please enter a city name");
            } else {
                welcomeText.setText("Showing forecast for " + cityNameTextField.getText());
                String url_s = "https://api.weatherapi.com/v1/current.json?key=6e7f3ab892f14124a9b200410232806&q=" + cityNameTextField.getText() + "&aqi=no&lang=pl";
                String newur_sl = url_s.replaceAll(" ", "%20");

                URL url = new URL(newur_sl);
                HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
                con.setRequestMethod("GET");
                con.setRequestProperty("Content-Type", "application/json; utf-8");

                int status = con.getResponseCode();

                if (!(status == 200)) {
                    welcomeText.setText("Error: " + status);
                    System.out.println("Error: " + status + "\n" + con.getResponseMessage());
                } else {
                    StringBuilder inLine = new StringBuilder();
                    Scanner scanner = new Scanner(url.openStream());

                    while (scanner.hasNext()) {
                        inLine.append(scanner.nextLine());
                    }
                    System.out.println(inLine);


                    JsonObject convertObj = new Gson().fromJson(inLine.toString(), JsonObject.class);

                    String city_name = convertObj.get("location").getAsJsonObject().get("name").getAsString();

                    double temp = convertObj.get("current").getAsJsonObject().get("temp_c").getAsDouble();
                    assertThat(temp).isBetween(-100.0, 100.0);
                    double wind = convertObj.get("current").getAsJsonObject().get("wind_kph").getAsDouble();
                    assertThat(wind).isBetween(0.0, 500.0);
                    double pressure = convertObj.get("current").getAsJsonObject().get("pressure_mb").getAsDouble();
                    assertThat(pressure).isBetween(0.0, 2000.0);
                    double precipitation = convertObj.get("current").getAsJsonObject().get("precip_mm").getAsDouble();
                    assertThat(precipitation).isBetween(0.0, 30000.0);
                    double humidity = convertObj.get("current").getAsJsonObject().get("humidity").getAsDouble();
                    assertThat(humidity).isBetween(0.0, 100.0);
                    String condition = convertObj.get("current").getAsJsonObject().get("condition").getAsJsonObject().get("text").getAsString();
                    assertThat(condition).isNotNull();

                    welcomeText.setText("Weather in: " + city_name +
                            ":\nTemperature: " + convertObj.get("current").getAsJsonObject().get("temp_c").getAsString() +
                            "C\nWind: " + convertObj.get("current").getAsJsonObject().get("wind_kph").getAsString() +
                            "kph\nPressure: " + convertObj.get("current").getAsJsonObject().get("pressure_mb").getAsString() +
                            "mb\nPrecipitation: " + convertObj.get("current").getAsJsonObject().get("precip_mm").getAsString() +
                            "mm\nHumidity: " + convertObj.get("current").getAsJsonObject().get("humidity").getAsString() +
                            "%\nCondition: " + convertObj.get("current").getAsJsonObject().get("condition").getAsJsonObject().get("text").getAsString());
                    if (convertObj.get("current").getAsJsonObject().get("condition").getAsJsonObject().get("text").getAsString().equals("Sunny")) {
                        welcomeText.setTextFill(javafx.scene.paint.Color.web("#ff0000"));
                    } else {
                        welcomeText.setTextFill(javafx.scene.paint.Color.web("#000000"));
                    }
                    if (convertObj.get("current").getAsJsonObject().get("temp_c").getAsInt() < 0) {
                        welcomeText.setTextFill(javafx.scene.paint.Color.web("#0000ff"));
                    }
                    //if error
                    if (convertObj.get("error") != null) {
                        welcomeText.setText("Error: " + convertObj.get("error").getAsJsonObject().get("message").getAsString());
                    }

                    welcomeText.setStyle("-fx-font-size: 14px;");
                    welcomeText.setMinHeight(Region.USE_PREF_SIZE);

                    welcomeText.setWrapText(true);
                    Image image = new Image("https:" + convertObj.get("current").getAsJsonObject().get("condition").getAsJsonObject().get("icon").getAsString());
                    weatherIcon.setImage(image);


                    HostServices hostServices = getHostServices();
                    googleMaps.setOnMouseClicked(event -> hostServices.showDocument("https://www.google.com/maps/place/" + cityNameTextField.getText()));
                    googleMaps.setText("Open " + cityNameTextField.getText() + " in Google Maps");
                    google.setOnMouseClicked(event -> hostServices.showDocument("https://www.google.com/search?q=" + cityNameTextField.getText()));
                    google.setText("Search " + cityNameTextField.getText() + " in Google");
                    wikipedia.setOnMouseClicked(event -> hostServices.showDocument("https://en.wikipedia.org/wiki/" + cityNameTextField.getText()));
                    wikipedia.setText("Open " + cityNameTextField.getText() + " in Wikipedia");
                    googleMaps.setVisible(true);
                    google.setVisible(true);
                    wikipedia.setVisible(true);

                }
            }
        } catch (Exception e) {
            welcomeText.setText("Error: Network error: " + e.getMessage());
        }
    }


    public String getMeow() {
        try {

            URL url = new URL("https://meowfacts.herokuapp.com/");
            HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Content-Type", "application/json; utf-8");

            int status = con.getResponseCode();
            if (status != 200) {
                System.out.println("Error");
                return "I can't get the meow facts right now";
            } else {
                Scanner sc = new Scanner(url.openStream());
                StringBuilder inline = new StringBuilder();
                while (sc.hasNext()) {
                    inline.append(sc.nextLine());
                }
                sc.close();

                JsonObject jsonObject = new Gson().fromJson(inline.toString(), JsonObject.class);
                return jsonObject.get("data").getAsString();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "I can't get the meow facts right now";
        }

    }


    @Override
    public void start(Stage stage) {

    }

    public void cityfromlist(ActionEvent actionEvent) {
        Button selectedItem = (Button) actionEvent.getSource();
        System.out.println(selectedItem.getText());
        cityNameTextField.setText(selectedItem.getText());
    }

    public void loadlocation(ActionEvent ignoredActionEvent) throws IOException {
        FileReader fileReader = new FileReader("location.txt");
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        String line = bufferedReader.readLine();
        if (line != null && line.length() > 0) {
            cityNameTextField.setText(line);
            welcomeText.setText("Location loaded");

        } else {
            welcomeText.setText("There is no location saved");
        }
    }

    public void savelocation(ActionEvent ignoredActionEvent) throws IOException {
        PrintWriter writer = new PrintWriter("location.txt");
        writer.close();
        //get a location from the textfield and save it to the file on the disk
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(new FileOutputStream("location.txt", true), StandardCharsets.UTF_8);
        BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);
        bufferedWriter.write("");
        bufferedWriter.flush();
        if (cityNameTextField.getText().length() > 0) {
            welcomeText.setText("Location saved");
            bufferedWriter.write(cityNameTextField.getText());
        } else {
            welcomeText.setText("Please enter a city name first");
        }
        bufferedWriter.close();

    }

    public void getbyIP(ActionEvent ignoredActionEvent) {
        //get the location by IP
        //I use ipinfo.io to get the IP address
        try {
            URL url = new URL("https://ipinfo.io/ip");
            HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Content-Type", "application/json; utf-8");

            int status = con.getResponseCode();
            if (status != 200) {
                System.out.println("Error");
            } else {
                Scanner sc = new Scanner(url.openStream());
                StringBuilder inline = new StringBuilder();
                while (sc.hasNext()) {
                    inline.append(sc.nextLine());
                }
                sc.close();
                String ip = inline.toString();
                System.out.println(ip);
                URL url2 = new URL("https://ipinfo.io/" + ip + "/json");
                HttpsURLConnection con2 = (HttpsURLConnection) url2.openConnection();
                con2.setRequestMethod("GET");
                con2.setRequestProperty("Content-Type", "application/json; utf-8");

                int status2 = con2.getResponseCode();
                if (status2 != 200) {
                    System.out.println("Error ");
                } else {
                    Scanner sc2 = new Scanner(url2.openStream());
                    StringBuilder inline2 = new StringBuilder();
                    while (sc2.hasNext()) {
                        inline2.append(sc2.nextLine());
                    }
                    sc2.close();
                    JsonObject jsonObject = new Gson().fromJson(inline2.toString(), JsonObject.class);
                    cityNameTextField.setText(jsonObject.get("city").getAsString());
                    welcomeText.setText("Location loaded");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings({"CommentedOutCode", "unchecked"})
    public void statistic(ActionEvent ignoredActionEvent) throws ClassNotFoundException {
        //mysql database
        //column 1 - ID(int), column 2 - hour(int), column 3 - temperature(double). I want to add it to the arraylist of ObjectWeatherHistory
//        Host: sql7.freesqldatabase.com
//        Database name: sql7629360
//        Database user: sql7629360
//        Database password: KKKTblqyIl
//        Port number: 3306
        //Table: weatherhistory_warsaw

        ArrayList<ObjectWeatherHistory> list_db = new ArrayList<>();
        Class.forName("com.mysql.cj.jdbc.Driver");
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://sql7.freesqldatabase.com:3306/sql7629360", "sql7629360", "KKKTblqyIl");
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM weatherhistory_warsaw");
            while (resultSet.next()) {
                list_db.add(new ObjectWeatherHistory(resultSet.getDouble(3), 20.0, 1000.0, 10.0, resultSet.getInt(2)));
            }
            connection.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            welcomeText.setText("DB connection error");
        }


        //sample data
        ArrayList<ObjectWeatherHistory> list = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < 24; i++) {

            list.add(new ObjectWeatherHistory(random.nextDouble(20), 20.0, 1000.0 + i, 10.0, i));
        }
        //create the chart
        NumberAxis xAxis = new NumberAxis();
        NumberAxis yAxis = new NumberAxis();
        xAxis.setTickUnit(1);
        xAxis.setLowerBound(1);
        xAxis.setUpperBound(24);
        xAxis.setAutoRanging(false);
        xAxis.setLabel("Hour");
        yAxis.setLabel("Temperature");
        LineChart<Number, Number> lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setTitle("Weather history for Warsaw");
        XYChart.Series series = new XYChart.Series();
        series.setName("Temperature");
//        for (int i = 0; i < list.size(); i++) {
//            series.getData().add(new XYChart.Data(list.get(i).getHour(), list.get(i).getTemperture()));
//        }
        for (ObjectWeatherHistory objectWeatherHistory : list_db) {
            series.getData().add(new XYChart.Data<>(objectWeatherHistory.getHour(), objectWeatherHistory.getTemperture()));
        }
        Scene scene = new Scene(lineChart, 800, 600);
        lineChart.getData().add(series);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.show();
    }
}

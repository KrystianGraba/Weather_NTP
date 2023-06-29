module com.example.pogoda {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;

    requires java.sql;

    opens com.example.pogoda to javafx.fxml;
    exports com.example.pogoda;
}
module com.example.pogoda {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;

    requires com.dlsc.formsfx;

    opens com.example.pogoda to javafx.fxml;
    exports com.example.pogoda;
}
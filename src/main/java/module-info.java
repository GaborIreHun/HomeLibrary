module com.example.homelibrary {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.net.http;
    requires com.google.gson;
    requires com.fasterxml.jackson.databind;
    //requires opencsv;
    requires java.desktop;
    requires com.opencsv;
    requires junit;


    opens com.example.homelibrary to javafx.fxml;
    exports com.example.homelibrary;
}
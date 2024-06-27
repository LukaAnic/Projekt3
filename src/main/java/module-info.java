module at.wifi.ca.projekt3 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires password4j;


    opens at.wifi.ca.projekt3 to javafx.fxml;
    exports at.wifi.ca.projekt3;
    exports at.wifi.ca.projekt3.model;
    opens at.wifi.ca.projekt3.model to javafx.fxml;
}
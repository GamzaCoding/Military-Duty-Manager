module org.example.dutymanager {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.apache.poi.poi;
    requires org.apache.poi.ooxml;
    requires java.desktop;
    requires javafx.graphics;

    opens org.example.dutymanager to javafx.fxml;
    opens org.example.dutymanager.controller to javafx.fxml;

    exports org.example.dutymanager;
    exports org.example.dutymanager.controller;
}
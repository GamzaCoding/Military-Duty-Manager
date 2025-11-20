module org.example.dutymanager {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.apache.poi.poi;
    requires org.apache.poi.ooxml;
    requires java.desktop;

    opens org.example.dutymanager to javafx.fxml;
    exports org.example.dutymanager;
}
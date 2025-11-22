package org.example.dutymanager;

import java.util.Objects;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class StartApplication extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(StartApplication.class.getResource("/org/example/dutymanager/start-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 500);

        scene.getStylesheets().add(
                Objects.requireNonNull(
                        StartApplication.class.getResource("/css/start.css")
                ).toExternalForm()
        );

        stage.setTitle("당직 관리 프로그램");
        stage.setScene(scene);
        stage.show();
    }
}

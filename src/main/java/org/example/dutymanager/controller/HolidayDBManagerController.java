package org.example.dutymanager.controller;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Objects;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.dutymanager.service.MainService;

public class HolidayDBManagerController {
    @FXML private TextField yearField;
    @FXML private TextField monthField;
    @FXML private TextField dayField;
    @FXML private TextField descriptionField;
    @FXML private Label successText;
    private final MainService mainService = new MainService();

    @FXML
    protected void onDBOpenButtonClick() {
        try {
            File holidaysDB = mainService.getHolidaysDB();
            openExcelFile(holidaysDB);
        } catch (IOException e) {
            System.out.println("공휴일 DB 파일을 여는 과정에서 에러가 발생했습니다. : " + e.getMessage());
        }
    }

    @FXML
    private void onAddHolidayButtonClick() {
        int year = Integer.parseInt(yearField.getText());
        int month = Integer.parseInt(monthField.getText());
        int day = Integer.parseInt(dayField.getText());
        LocalDate targetDate = LocalDate.of(year, month, day);

        mainService.changeWeekdayToHoliday(targetDate, descriptionField.getText());
        successText.setText(String.format("휴일DB에 %d-%d-%d를 추가 완료!", year, month, day));
    }

    @FXML
    private void onRemoveHolidayButtonClick() {
        int year = Integer.parseInt(yearField.getText());
        int month = Integer.parseInt(monthField.getText());
        int day = Integer.parseInt(dayField.getText());
        LocalDate targetDate = LocalDate.of(year, month, day);

        mainService.changeHolidayToWeekday(targetDate);
        successText.setText(String.format("휴일DB에서 %d-%d-%d를 삭제 완료!", year, month, day));
    }

    @FXML
    private void onGoHomeClick() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/dutymanager/start-view.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) successText.getScene().getWindow();
        Scene newScene = new Scene(root, 800, 500);

        newScene.getStylesheets().add(
                Objects.requireNonNull(
                        getClass().getResource("/css/start.css")
                ).toExternalForm()
        );
        stage.setScene(newScene);
    }

    private void openExcelFile(File file) throws IOException {
        if (!Desktop.isDesktopSupported()) {
            Runtime.getRuntime().exec(new String[]{"open", file.getAbsolutePath()});
            return;
        }
        Desktop.getDesktop().open(file);
    }
}

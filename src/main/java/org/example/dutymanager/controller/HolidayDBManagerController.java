package org.example.dutymanager.controller;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.Objects;
import java.util.function.Consumer;
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
    @FXML private Label failureText;
    private final MainService mainService = new MainService();

    @FXML
    private void onDBOpenButtonClick() {
        initText();
        try {
            File holidaysDB = mainService.getHolidaysDB();
            openExcelFile(holidaysDB);
            successText.setText("DB를 열기 성공!");
        } catch (IOException | IllegalArgumentException e) {
            failureText.setText("DB 열기 중 ERROR 발생! DB를 새로 생성하거나, DB의 위치를 확인해주세요");
        }
    }

    @FXML
    private void onAddHolidayButtonClick() {
        handleHolidayAction(
                localDate -> mainService.changeWeekdayToHoliday(localDate, descriptionField.getText()),
                "휴일DB에 %d-%d-%d를 추가 완료!");
    }

    @FXML
    private void onRemoveHolidayButtonClick() {
        handleHolidayAction(mainService::changeHolidayToWeekday, "휴일DB에서 %d-%d-%d를 삭제 완료!");
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

    private void handleHolidayAction(Consumer<LocalDate> action, String successFormat) {
        initText();

        String yearText = yearField.getText();
        String monthText = monthField.getText();
        String dayText = dayField.getText();

        if (isTextBlank(yearText, monthText, dayText)) {
            failureText.setText("년/월/일을 모두 입력해 주세요.");
            return;
        }

        try {
            validateFormat(yearText, monthText, dayText);
            int year = Integer.parseInt(yearText);
            int month = Integer.parseInt(monthText);
            int day = Integer.parseInt(dayText);
            validateDate(year, month, day);
            LocalDate targetDate = LocalDate.of(year, month, day);

            action.accept(targetDate);

            successText.setText(String.format(successFormat, year, month, day));
        } catch (IllegalStateException e) {
            failureText.setText("DB 열기 중 ERROR 발생! DB를 새로 생성하거나, DB의 위치를 확인해주세요");
        }

        catch (NumberFormatException | DateTimeException e) {
            failureText.setText("유효한 날짜(숫자)를 입력해 주세요.");
        }
    }

    private boolean isTextBlank(String yearText, String monthText, String dayText) {
        return yearText.isBlank() || monthText.isBlank() || dayText.isBlank();
    }

    private void initText() {
        successText.setText("");
        failureText.setText("");
    }

    private void validateDate(int year, int month, int day) {
        if (year <= 0 || month <= 0 || day <= 0) {
            throw new NumberFormatException();
        }

        if (month >= 13 || day >= 32) {
            throw new NumberFormatException();
        }
    }

    private void validateFormat(String yearText, String monthText, String dayText) {
        if (yearText.matches("\\d{4}") &&
                monthText.matches("\\d{1,2}") &&
                dayText.matches("\\d{1,2}")) {
            return;
        }
        throw new NumberFormatException();
    }
}

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
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.example.dutymanager.service.MainService;

public class DutyCreateController {

    @FXML private TextField startYearField;
    @FXML private TextField startMonthField;
    @FXML private TextField startDayField;
    @FXML private TextField endYearField;
    @FXML private TextField endMonthField;
    @FXML private TextField endDayField;
    @FXML private Label successText;
    @FXML private Label failureText;
    @FXML private Label selectedFilePathLabel;

    private File dutyOrderFile;
    private final MainService mainService = new MainService();

    @FXML
    private void onCalculateDutyResultClick() {
        initText();
        try {
            String startYearText = startYearField.getText();
            String startMonthText = startMonthField.getText();
            String startDayText = startDayField.getText();
            String endYearText = endYearField.getText();
            String endMonthText = endMonthField.getText();
            String endDayText = endDayField.getText();

            if (isTextBlank(startYearText, startMonthText, startDayText) ||
                    isTextBlank(endYearText, endMonthText, endDayText)) {
                failureText.setText("년/월/일을 모두 입력해 주세요.");
                return;
            }
            validateFormat(startYearText, startMonthText, startDayText);
            validateFormat(endYearText, endMonthText, endDayText);

            int startYear = Integer.parseInt(startYearText);
            int startMonth = Integer.parseInt(startMonthText);
            int startDay = Integer.parseInt(startDayText);
            int endYear = Integer.parseInt(endYearText);
            int endMonth = Integer.parseInt(endMonthText);
            int endDay = Integer.parseInt(endDayText);
            validateDate(startYear, startMonth, startDay);
            validateDate(endYear, endMonth, endDay);

            LocalDate startDate = LocalDate.of(startYear, startMonth, startDay);
            LocalDate endDate = LocalDate.of(endYear, endMonth, endDay);

            File result = mainService.calculateResult(startDate, endDate, dutyOrderFile);
            successText.setText("당직 결과 계산 완료!");
            openExcelFile(result);

        } catch (IllegalStateException | IOException e) {
            failureText.setText("당직순서 파일에 이상이 있습니다, 파일을 확인해주세요.");
        }
        catch (NumberFormatException e) {
            failureText.setText("날짜 입력 또는 당직순서 파일 선택이 잘못되었습니다.");
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

    private void validateDate(int year, int month, int day) {
        if (year <= 0 || month <= 0 || day <= 0) {
            throw new NumberFormatException();
        }

        if (month >= 13 || day >= 32) {
            throw new NumberFormatException();
        }
    }

    @FXML
    private void onGoHomeClick() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/dutymanager/start-view.fxml"));
        Parent root = loader.load();

        Stage stage = (Stage) startYearField.getScene().getWindow();

        Scene newScene = new Scene(root, 800, 500);

        newScene.getStylesheets().add(
                Objects.requireNonNull(
                        getClass().getResource("/css/start.css")
                ).toExternalForm()
        );
        stage.setScene(newScene);
    }

    @FXML
    private void onSelectExcelFileClick() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("엑셀 파일 선택");
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home") + "/Desktop"));
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Excel Files", "*.xlsx", "*.xls")
        );
        File selectedFile = fileChooser.showOpenDialog(selectedFilePathLabel.getScene().getWindow());

        if (selectedFile != null) {
            selectedFilePathLabel.setText(selectedFile.getAbsolutePath());
            System.out.println("파일 선택됨: " + selectedFile.getAbsolutePath());
            dutyOrderFile = selectedFile;
        } else {
            selectedFilePathLabel.setText("선택된 파일 없음");
        }
    }

    private void openExcelFile(File file) throws IOException {
        if (!Desktop.isDesktopSupported()) {
            Runtime.getRuntime().exec(new String[]{"open", file.getAbsolutePath()}); // macOS fallback
            return;
        }
        Desktop.getDesktop().open(file);
    }

    private void initText() {
        successText.setText("");
        failureText.setText("");
    }

    private boolean isTextBlank(String yearText, String monthText, String dayText) {
        return yearText.isBlank() || monthText.isBlank() || dayText.isBlank();
    }
}

package org.example.dutymanager.controller;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
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
    @FXML private Label resultMessage;
    @FXML private Label selectedFilePathLabel;

    private File dutyOrderFile;
    private final MainService mainService = new MainService();

    @FXML
    private void onCalculateDutyResultClick() {
        try {
            int startYear = Integer.parseInt(startYearField.getText());
            int startMonth = Integer.parseInt(startMonthField.getText());
            int startDay = Integer.parseInt(startDayField.getText());
            int endYear = Integer.parseInt(endYearField.getText());
            int endMonth = Integer.parseInt(endMonthField.getText());
            int endDay = Integer.parseInt(endDayField.getText());

            LocalDate startDate = LocalDate.of(startYear, startMonth, startDay);
            LocalDate endDate = LocalDate.of(endYear, endMonth, endDay);

            File result = mainService.calculateResult(startDate, endDate, dutyOrderFile);
            resultMessage.setText("당직 결과 계산 완료!");
            openExcelFile(result);

        } catch (Exception e) {
            resultMessage.setText("날짜 입력 또는 당직순서 파일 선택이 잘못되었습니다.");
            resultMessage.setStyle("-fx-text-fill: red;");
        }
    }

    @FXML
    private void onGoHomeClick() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/dutymanager/start-view.fxml"));
        Parent root = loader.load();

        Stage stage = (Stage) startYearField.getScene().getWindow();

        Scene newScene = new Scene(root, 800, 500);
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
}

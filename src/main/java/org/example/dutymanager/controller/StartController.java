package org.example.dutymanager.controller;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.example.dutymanager.StartApplication;
import org.example.dutymanager.service.MainService;

public class StartController {

    @FXML private Label successText;
    private final MainService mainService = new MainService();

    @FXML
    private void onStartButtonClick() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    StartApplication.class.getResource("/org/example/dutymanager/view/dutyCreate-view.fxml"));
            Scene newScene = new Scene(loader.load());

            Stage stage = (Stage) successText.getScene().getWindow();
            stage.setScene(newScene);
            stage.show();

        } catch (IOException e) {
            System.out.println("DutyCreateController로 넘어가는 과정에서 오류가 발생했습니다. : " + e.getMessage());
        }
    }

    @FXML
    private void onTutorialButtonClick() {
        mainService.startTutorial();
        openExcelFile(mainService.getTutorialFile());
        successText.setText("당직자 순서(양식).xlsx 생성 완료");
    }

    @FXML
    private void onHolidayDBManagerButtonClick() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    StartApplication.class.getResource("view/HolidayDBManager-view.fxml"));
            Scene newScene = new Scene(loader.load());

            Stage stage = (Stage) successText.getScene().getWindow();
            stage.setScene(newScene);
            stage.show();

        } catch (IOException e) {
            System.out.println("HolidayDBManagerController로 넘어가는 과정에서 오류가 발생했습니다. : " + e.getMessage());
        }
    }

    @FXML
    private void onHolidayDBCreateButtonClick() {
        if (mainService.alreadyHolidayDBExist()) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("공휴일 DB 생성");
            alert.setHeaderText("기존 공휴일 DB가 이미 존재합니다.");
            alert.setContentText("새로 생성하면 기존 데이터는 삭제됩니다.\n정말 새로 만드시겠습니까?");

            ButtonType yesButton = new ButtonType("예", ButtonBar.ButtonData.OK_DONE);
            ButtonType noButton = new ButtonType("아니오", ButtonBar.ButtonData.CANCEL_CLOSE);

            alert.getButtonTypes().setAll(yesButton, noButton);
            alert.showAndWait().ifPresent(response -> {
                if (response == yesButton) {
                    mainService.createHolidayDB();
                    successText.setText("기존 DB를 덮어쓰고 재생성했습니다.");
                } else {
                    successText.setText("DB 생성이 취소되었습니다.");
                }
            });
        } else {
            mainService.createHolidayDB();
            successText.setText("holidays.xlsx 생성 성공");
        }
    }

    private void openExcelFile(File file) {
        try {
            if (!Desktop.isDesktopSupported()) {
                Runtime.getRuntime().exec(new String[]{"open", file.getAbsolutePath()});
                return;
            }
            Desktop.getDesktop().open(file);
        } catch (IOException e) {
            System.out.println("엘셀 파일을 오픈하는 과정에서 에러가 발생했습니다. : " + e.getMessage());
        }
    }
}
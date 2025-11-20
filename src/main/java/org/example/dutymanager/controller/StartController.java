package org.example.dutymanager.controller;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.example.dutymanager.service.MainService;

public class StartController {

    @FXML
    private Label tutorialSuccessText;
    private final MainService mainService = new MainService();

    @FXML
    protected void onStartButtonClick() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/org/example/dutymanager/view/dutyCreate-view.fxml"));
            Scene newScene = new Scene(loader.load());

            Stage stage = (Stage) tutorialSuccessText.getScene().getWindow();
            stage.setScene(newScene);
            stage.show();

        } catch (IOException e) {
            System.out.println("DutyCreateController로 넘어가는 과정에서 오류가 발생했습니다. : " + e.getMessage());
        }
    }

    @FXML
    protected void onHolidayDBCreateButtonClick() {
        // holidayDB 생성 로직, 기존에 DB가 있다면 경고 메시지를 띄우자!(기존 DB가 이미 존재합니다. 새로 생성하시겠습니까?)
        // 이렇게 메시지를 띄우고 기존 DB 파일의 이름을 변경하자.
        mainService.createHolidayDB();
        tutorialSuccessText.setText("당직결과 폴더 안에 holidays.xlsx 파일 생성 성공");
    }

    @FXML
    protected void onTutorialButtonClick() {
        mainService.startTutorial();
        openExcelFile(mainService.getTutorialFile());
        tutorialSuccessText.setText("당직결과 폴더 안데 당직자 순서(양식).xlsx 파일 생성 완료");
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

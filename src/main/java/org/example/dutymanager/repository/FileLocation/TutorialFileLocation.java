package org.example.dutymanager.repository.FileLocation;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.format.DateTimeFormatter;

public class TutorialFileLocation {
    private static final DateTimeFormatter FILE_FORMATTER = DateTimeFormatter.ofPattern("yyyy년_MM월_dd일_HH시_mm분");
    private static final String TUTORIAL_FILE_PATH = "/Desktop/당직결과/";
    private static final String TUTORIAL_HEADER = "당직자 순서(양식)_";
    private static final String FILE_EXTENSION = ".xlsx";

    public TutorialFileLocation() {
    }

    public File getFile() {
        String home = System.getProperty("user.home");
        File directory = new File( home + TUTORIAL_FILE_PATH);
        accessFile(directory);
        String fileName = TUTORIAL_HEADER + FILE_FORMATTER.format(java.time.LocalDateTime.now()) + FILE_EXTENSION;
        return new File(directory, fileName);
    }

    private void accessFile(File directory) {
        try {
            Files.createDirectories(directory.toPath());
        } catch (IOException e) {
            System.out.println("holidays.xlsx 파일에 접근하는 과정에서 오류가 발생했습니다 : " + e.getMessage());
        }
    }
}

package org.example.dutymanager.repository.FileLocation;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class ResultFileLocation {
    private static final DateTimeFormatter FOLDER_FORMATTER = DateTimeFormatter.ofPattern("yyyy년/MM월");
    private static final DateTimeFormatter FILE_FORMATTER = DateTimeFormatter.ofPattern("yyyy년_MM월_dd일_HH시_mm분");
    private static final String BASIC_FILE_PATH = "/Desktop/당직 관리 프로그램/";
    private static final String FILE_EXTENSION = ".xlsx";
    private static final String DUTY_TABLE_HEADER = "당직표_";

    public ResultFileLocation() {
    }

    public File getFile() {
        LocalDate now = LocalDate.now();
        String home = System.getProperty("user.home");
        String subFolder = home + BASIC_FILE_PATH + FOLDER_FORMATTER.format(now);
        File directory = new File(subFolder);
        accessFile(directory);
        String fileName = DUTY_TABLE_HEADER + FILE_FORMATTER.format(java.time.LocalDateTime.now()) + FILE_EXTENSION;
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

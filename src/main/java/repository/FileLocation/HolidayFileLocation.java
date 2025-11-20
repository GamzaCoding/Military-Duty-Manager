package repository.FileLocation;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class HolidayFileLocation {
    private static final String FILE_NAME = "holidays";
    private static final String FILE_EXTENSION = ".xlsx";
    private static final String HOLIDAY_FILE_PATH = "/Desktop/당직결과/holidays";

    public File getFile() {
        String home = System.getProperty("user.home");
        String subFolder = home + HOLIDAY_FILE_PATH;
        File directory = new File(subFolder);
        accessFile(directory);
        String fileName = FILE_NAME + FILE_EXTENSION;
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

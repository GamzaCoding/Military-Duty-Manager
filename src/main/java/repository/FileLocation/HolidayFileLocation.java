package repository.FileLocation;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class HolidayFileLocation {
    private static final String FILE_NAME = "holidays";
    private static final String FILE_EXTENSION = ".xlsx";

    public File getFile() throws IOException {
        String subFolder = "/Users/seok/Desktop/당직결과/" + FILE_NAME;
        File directory = new File(subFolder);
        Files.createDirectories(directory.toPath());
        String fileName = FILE_NAME + FILE_EXTENSION;
        return new File(directory, fileName);
    }
}

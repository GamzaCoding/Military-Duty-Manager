package repository.FileLocation;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class HolidayFileLocation {
    private static final String SUB_FOLDER_NAME = "holiday";
    private static final String FILE_NAME = "holidays";
    private static final String PATH = "/";
    private static final String FILE_EXTENSION = ".xlsx";

    public File getFile() throws IOException {
        File directory = new File(SUB_FOLDER_NAME + PATH);
        Files.createDirectories(directory.toPath());

        String fileName = FILE_NAME + FILE_EXTENSION;

        return new File(directory, fileName);
    }
}

package repository.FileLocation;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.format.DateTimeFormatter;

public class TutorialFileLocation {
    private static final DateTimeFormatter FILE_FORMATTER = DateTimeFormatter.ofPattern("yyyy년_MM월_dd일_HH시_mm분");
    private static final String PATH = "/";
    private static final String FILE_NAME_HEADER = "당직자 순서(양식)_";
    private static final String FILE_EXTENSION = ".xlsx";

    public TutorialFileLocation() {
    }

    public File getFile() throws IOException {
        File directory = new File("/Users/seok/Desktop/당직결과" + PATH);
        Files.createDirectories(directory.toPath());
        String fileName = FILE_NAME_HEADER + FILE_FORMATTER.format(java.time.LocalDateTime.now()) + FILE_EXTENSION;

        return new File(directory, fileName);
    }
}

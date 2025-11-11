package repository.writer;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.format.DateTimeFormatter;

public class TutorialFileLocation {
    private static final DateTimeFormatter FILE_FORMATTER = DateTimeFormatter.ofPattern("yyyy년_MM월_dd일_HH시_mm분");

    public TutorialFileLocation() {
    }

    public File getLocation() throws IOException {
        String subFolder = "tutorial/";
        File directory = new File(subFolder);
        Files.createDirectories(directory.toPath());

        String fileName = "당직자 순서(양식)_" + FILE_FORMATTER.format(java.time.LocalDateTime.now()) + ".xlsx";

        return new File(directory, fileName);
    }
}

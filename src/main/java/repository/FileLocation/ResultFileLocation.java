package repository.FileLocation;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class ResultFileLocation {
    private static final DateTimeFormatter FOLDER_FORMATTER = DateTimeFormatter.ofPattern("yyyy년/MM월");
    private static final DateTimeFormatter FILE_FORMATTER = DateTimeFormatter.ofPattern("yyyy년_MM월_dd일_HH시_mm분");

    public ResultFileLocation() {
    }

    public File getLocation() throws IOException {
        LocalDate now = LocalDate.now();
        String subFolder = "output/" + FOLDER_FORMATTER.format(now);
        File directory = new File(subFolder);
        Files.createDirectories(directory.toPath());

        String fileName = "당직표_" + FILE_FORMATTER.format(java.time.LocalDateTime.now()) + ".xlsx";

        return new File(directory, fileName);
    }
}

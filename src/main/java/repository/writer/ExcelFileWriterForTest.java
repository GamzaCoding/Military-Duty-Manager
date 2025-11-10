package repository.writer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import service.model.person.Person;

public class ExcelFileWriterForTest implements ExcelFileWriter {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private final List<Person> persons;

    private ExcelFileWriterForTest(List<Person> persons) {
        this.persons = persons;
    }

    public static ExcelFileWriterForTest of(List<Person> persons) {
        return new ExcelFileWriterForTest(persons);
    }

    @Override
    public void write(File outputFile) {
        handleIOExceptionDuringWrite(outputFile, file -> {
            writePersonsToExcel(file, persons);
        });
    }

    private void writePersonsToExcel(File file, List<Person> persons) throws IOException {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("당직자 목록");

            Row header = sheet.createRow(0);
            header.createCell(0).setCellValue("순번");
            header.createCell(1).setCellValue("계급");
            header.createCell(2).setCellValue("이름");
            header.createCell(3).setCellValue("전입일");
            header.createCell(4).setCellValue("전출일");

            int rowIndex = 1;
            for (Person person : persons) {
                Row row = sheet.createRow(rowIndex++);
                row.createCell(0).setCellValue(person.position());
                row.createCell(1).setCellValue(person.rank());
                row.createCell(2).setCellValue(person.name());
                row.createCell(3).setCellValue(formatDate(person.moveInDate()));
                row.createCell(4).setCellValue(formatDate(person.moveOutDate()));
            }

            for (int i = 0; i < 5; i++) {
                sheet.autoSizeColumn(i);
            }

            try (FileOutputStream fos = new FileOutputStream(file)) {
                workbook.write(fos);
            }
        }
    }

    private String formatDate(java.time.LocalDate date) {
        return date == null ? "" : DATE_FORMATTER.format(date);
    }

    private <T> void handleIOExceptionDuringWrite(T input, IOFunctionForWrite<T> ioFunction) {
        try {
            ioFunction.accept(input);
        } catch (IOException e) {
            throw new IllegalStateException("엑셀 파일에 값을 입력하는 도중 오류가 발생했습니다: " + e.getMessage());
        }
    }
}

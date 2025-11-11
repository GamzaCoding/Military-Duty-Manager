package repository.writer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import service.model.person.Person;
import service.model.person.Persons;

public class ExcelFileWriterFor implements ExcelFileWriter {
    private final Persons persons;

    private ExcelFileWriterFor(Persons persons) {
        this.persons = persons;
    }

    public static ExcelFileWriterFor of(Persons persons) {
        return new ExcelFileWriterFor(persons);
    }

    @Override
    public void write(File outputFile) {
        handleIOExceptionDuringWrite(outputFile, file -> {
            writePersonsToExcel(file, persons);
        });
    }

    private void writePersonsToExcel(File file, Persons persons) throws IOException {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("당직자 순서(평일)");

            Row header = sheet.createRow(0);
            header.createCell(0).setCellValue("순번");
            header.createCell(1).setCellValue("계급");
            header.createCell(2).setCellValue("이름");
            header.createCell(3).setCellValue("전입일(예정일 포함)");
            header.createCell(4).setCellValue("전출일(예정일 포함)");

            int rowIndex = 1;
            for (Person person : persons.getPersons()) {
                Row row = sheet.createRow(rowIndex++);
                row.createCell(0).setCellValue(person.position());
                row.createCell(1).setCellValue(person.rank());
                row.createCell(2).setCellValue(person.name());
                row.createCell(3).setCellValue(person.getMoveInDateByMilitaryFormat());
                row.createCell(4).setCellValue(person.getMoveOutDateByMilitaryFormat());
            }

            for (int i = 0; i < 5; i++) {
                sheet.autoSizeColumn(i);
            }

            try (FileOutputStream fos = new FileOutputStream(file)) {
                workbook.write(fos);
            }
        }
    }

    private <T> void handleIOExceptionDuringWrite(T input, IOFunctionForWrite<T> ioFunction) {
        try {
            ioFunction.accept(input);
        } catch (IOException e) {
            throw new IllegalStateException("엑셀 파일에 값을 입력하는 도중 오류가 발생했습니다: " + e.getMessage());
        }
    }
}

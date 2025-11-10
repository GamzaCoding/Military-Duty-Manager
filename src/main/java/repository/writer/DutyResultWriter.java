package repository.writer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import service.model.Duty;

public class DutyResultWriter implements ExcelFileWriter {
    private final List<Duty> duties;

    private DutyResultWriter(List<Duty> dutys) {
        this.duties = dutys;
    }

    public static DutyResultWriter of(List<Duty> dutys) {
        return new DutyResultWriter(dutys);
    }

    @Override
    public void write(File outputFile) {
        handleIOExceptionDuringWrite(outputFile, this::writeDutiesToExcel);
    }

    private void writeDutiesToExcel(File file) throws IOException {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("당직표 결과");

            Row row0 = sheet.createRow(0);
            for (int i = 0; i < 7; i++) {
                row0.createCell(i).setCellValue(duties.get(i).getDay().toString());
            }
            Row row1 = sheet.createRow(1);
            for (int i = 0; i < 7; i++) {
                row1.createCell(i).setCellValue(duties.get(i).getPerson().getRankAndName());
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

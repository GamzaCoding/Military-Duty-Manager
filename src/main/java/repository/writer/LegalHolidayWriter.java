package repository.writer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import service.model.day.Day;

public class LegalHolidayWriter {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    public static final int CELL_COUNT = 3;

    // 공휴일 엑셀 파일에 휴일 데이터를 추가
    public void write(File outFile, Day day) {
        try (Workbook workbook = getWorkbook(outFile)) {
            Sheet sheet = workbook.getSheet(day.getYear() + "년");

            int nextRowIndex = sheet.getLastRowNum() + 1;
            Row row = sheet.createRow(nextRowIndex);
            row.createCell(0).setCellValue(day.getLocalDate().format(FORMATTER));
            row.createCell(1).setCellValue(day.getWeekTypeName().weekName());
            row.createCell(2).setCellValue(day.getDescription());

            CellStyle basicStyle = createBasicStyle(workbook);
            applyStyleToRowCells(row, basicStyle);

            try (FileOutputStream fos = new FileOutputStream(outFile)) {
                workbook.write(fos);
            }
        } catch (IOException e) {
            throw new IllegalStateException("공휴일 데이터를 추가하는 중 오류 발생: " + e.getMessage(), e);
        }
    }

    private void applyStyleToRowCells(Row row, CellStyle style) {
        for (int i = 0; i < CELL_COUNT; i++) {
            row.getCell(i).setCellStyle(style);
        }
    }

    private CellStyle createBasicStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        Font font = workbook.createFont();
        font.setFontHeightInPoints((short) 15);
        font.setFontName("굴림");
        style.setFont(font);

        return style;
    }

    private Workbook getWorkbook(File file) throws IOException {
        try (FileInputStream fis = new FileInputStream(file)) {
            return new XSSFWorkbook(fis);
        }
    }


    // 공휴일 엑셀 파일에서 휴일 데이터를 삭제
    public void remove(File outFile, Day day) {

    }

    // 공휴일 엑셀 파일에서 휴일 데이터를 수정(날짜 변경), 휴일 목록을 불러올 수 있어야 하겠지?
    public void Modify(File outFile, Day day) {

    }
}

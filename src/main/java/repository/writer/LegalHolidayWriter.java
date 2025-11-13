package repository.writer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.function.BiConsumer;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
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
    private static final int FIRST_DATA_ROW_INDEX = 1;
    public static final int CELL_COUNT = 3;
    public static final int DAY_DATA_INDEX = 0;

    // 공휴일 엑셀 파일에 휴일 데이터를 추가
    public void write(File outFile, Day day) {
        handleExcelOperation(outFile, day, (sheet, workbook) -> addDayToFile(day, sheet, workbook));
    }

    // 공휴일 엑셀 파일에서 휴일 데이터를 삭제
    public void remove(File outFile, Day day) {
        handleExcelOperation(outFile, day, (sheet, workbook) -> removeDayFromFile(day, sheet));
    }

    private void handleExcelOperation(File outFile, Day day, BiConsumer<Sheet, Workbook> operation) {
        try (Workbook workbook = getWorkbook(outFile)) {
            Sheet sheet = workbook.getSheet(day.getYear() + "년");

            operation.accept(sheet, workbook);

            try (FileOutputStream fos = new FileOutputStream(outFile)) {
                workbook.write(fos);
            }
        } catch (IOException e) {
            throw new IllegalStateException("공휴일 데이터 처리 중 오류 발생: " + e.getMessage(), e);
        }
    }

    private void addDayToFile(Day day, Sheet sheet, Workbook workbook) {
        int nextRowIndex = sheet.getLastRowNum() + 1;
        Row row = sheet.createRow(nextRowIndex);
        row.createCell(0).setCellValue(day.getLocalDate().format(FORMATTER));
        row.createCell(1).setCellValue(day.getWeekTypeName().weekName());
        row.createCell(2).setCellValue(day.getDescription());

        CellStyle basicStyle = createBasicStyle(workbook);
        applyStyleToRowCells(row, basicStyle);
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

    private void removeDayFromFile(Day day, Sheet sheet) {
        LocalDate targetDate = day.getLocalDate();

        for (int i = FIRST_DATA_ROW_INDEX; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            if (row == null) {
                continue;
            }
            Cell cell = row.getCell(DAY_DATA_INDEX);
            if (cell != null && targetDate.equals(parseCellDate(cell))) {
                sheet.removeRow(row);
                shiftRowsUp(sheet, i);
                break;
            }
        }
    }

    private LocalDate parseCellDate(Cell cell) {
        if (cell == null) {
            return null;
        }

        // 셀이 날짜 포맷일 경우 (엑셀 내부는 숫자 기반)
        if (isLocalDateFormatOnNumericType(cell)) {
            return cell.getLocalDateTimeCellValue().toLocalDate();
        }

        // 셀이 문자열인 경우
        if (cell.getCellType() == CellType.STRING) {
            String value = cell.getStringCellValue().trim();
            if (value.isEmpty()) {
                return null;
            }
            return LocalDate.parse(value, FORMATTER);
        }

        // 포맷 불명확할 경우 안전하게 숫자 기반 처리
        if (cell.getCellType() == CellType.NUMERIC) {
            return cell.getLocalDateTimeCellValue().toLocalDate();
        }

        throw new IllegalStateException("셀 타입이 날짜나 문자열이 아닙니다: " + cell.getCellType());
    }

    private static boolean isLocalDateFormatOnNumericType(Cell cell) {
        return cell.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(cell);
    }

    private void shiftRowsUp(Sheet sheet, int deletedRowIndex) {
        int lastRowNum = sheet.getLastRowNum();
        if (deletedRowIndex >= 0 && deletedRowIndex < lastRowNum) {
            sheet.shiftRows(deletedRowIndex + 1, lastRowNum, -1);
        }
    }

    private Workbook getWorkbook(File file) throws IOException {
        try (FileInputStream fis = new FileInputStream(file)) {
            return new XSSFWorkbook(fis);
        }
    }
}

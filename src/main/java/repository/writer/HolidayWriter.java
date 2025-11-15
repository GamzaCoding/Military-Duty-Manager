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

public class HolidayWriter {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    public static final String FONT_TYPE = "굴림";
    private static final int FIRST_DATA_ROW_INDEX = 1;
    public static final int CELL_COUNT = 3;
    public static final int DAY_DATA_INDEX = 0;
    public static final int LOCAL_DATE_INDEX = 0;
    public static final int WEEK_TYPE_INDEX = 1;
    public static final int DAY_DESCRIPTION_INDEX = 2;
    public static final int FONT_SIZE = 15;

    public void add(File outFile, Day day) {
        handleExcelOperation(outFile, day, (sheet, workbook) -> addDayToFile(day, sheet, workbook));
    }

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
        int bottomRowIndex = sheet.getLastRowNum() + 1;
        Row row = sheet.createRow(bottomRowIndex);
        row.createCell(LOCAL_DATE_INDEX).setCellValue(day.getLocalDate().format(FORMATTER));
        row.createCell(WEEK_TYPE_INDEX).setCellValue(day.getWeekTypeName().weekName());
        row.createCell(DAY_DESCRIPTION_INDEX).setCellValue(day.getDescription());

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
        font.setFontHeightInPoints((short) FONT_SIZE);
        font.setFontName(FONT_TYPE);
        style.setFont(font);

        return style;
    }

    private void removeDayFromFile(Day day, Sheet sheet) {
        LocalDate targetDate = day.getLocalDate();
        for (int i = FIRST_DATA_ROW_INDEX; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            if (isBlankRow(row)) {
                continue;
            }
            Cell cell = row.getCell(DAY_DATA_INDEX);
            if (cell.getCellType() == CellType.BLANK) {
                continue;
            }
            if (targetDate.equals(parseCellDate(cell))) {
                sheet.removeRow(row);
                shiftRowsUp(sheet, i);
                break;
            }
        }
    }

    private static boolean isBlankRow(Row row) {
        return row == null;
    }

    private LocalDate parseCellDate(Cell cell) {
        if (isLocalDateFormatOnNumericType(cell)) {
            return cell.getLocalDateTimeCellValue().toLocalDate();
        }
        if (cell.getCellType() == CellType.STRING) {
            String value = cell.getStringCellValue().trim();
            return LocalDate.parse(value, FORMATTER);
        }
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

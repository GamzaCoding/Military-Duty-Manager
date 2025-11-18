package repository.writer;

import static repository.writer.sampleData.Sample.*;

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
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import repository.writer.util.CellStyler;
import service.model.day.Day;

public class HolidayWriter implements ExcelFileWriter {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final int FIRST_DATA_ROW_INDEX = 1;
    public static final int DAY_DATA_INDEX = 0;
    public static final int LOCAL_DATE_INDEX = 0;
    public static final int WEEK_TYPE_INDEX = 1;
    public static final int DAY_DESCRIPTION_INDEX = 2;
    public static final double CORRECTION_VALUE = 1.3;
    public static final int ROW_HEIGHT = 24;

    @Override
    public void write(File outFile) {
        handleIOExceptionDuringWrite(outFile, this::writeTutorialHolidayForm);
    }

    public void add(File outFile, Day day) {
        handleExcelOperation(outFile, day, (sheet, workbook) -> addDayToFile(day, sheet, workbook));
    }

    public void remove(File outFile, Day day) {
        handleExcelOperation(outFile, day, (sheet, workbook) -> removeDayFromFile(day, sheet));
    }

    private void writeTutorialHolidayForm(File file) throws IOException {
        try (Workbook workbook = new XSSFWorkbook()) {
            CellStyler cellStyler = new CellStyler(workbook);
            writeHeader(workbook, cellStyler.holidayHeaderStyle());
            writeBasicBody(workbook);
            writeResult(file, workbook);
        }
    }

    private void writeResult(File file, Workbook workbook) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(file)) {
            workbook.write(fos);
        }
    }

    private void writeHeader(Workbook workbook, CellStyle headerStyle) {
        Sheet sheet = workbook.createSheet(SAMPLE_SHEET_NAME);
        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < HOLIDAY_CATEGORY.size(); i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(HOLIDAY_CATEGORY.get(i));
            cell.setCellStyle(headerStyle);
            applyBasicColumWidth(sheet, i);
        }
        applyBasicRowHeight(sheet);
    }

    private void writeBasicBody(Workbook workbook) {
        Sheet sheet = workbook.getSheet(SAMPLE_SHEET_NAME);
        addDayToFile(SAMPLE_HOLIDAY, sheet, workbook);
        for (int i = 0; i < HOLIDAY_CATEGORY.size(); i++) {
            applyBasicColumWidth(sheet, i);
        }
        applyBasicRowHeight(sheet);
    }

    private void applyBasicColumWidth(Sheet sheet, int columIndex) {
        sheet.autoSizeColumn(columIndex);
        int currentWidth = sheet.getColumnWidth(columIndex);
        sheet.setColumnWidth(columIndex, (int) (currentWidth * CORRECTION_VALUE));
    }

    private void applyBasicRowHeight(Sheet sheet) {
        for (Row row : sheet) {
            row.setHeightInPoints(ROW_HEIGHT);
        }
    }

    private void addDayToFile(Day day, Sheet sheet, Workbook workbook) {
        int bottomRowIndex = sheet.getLastRowNum() + 1;
        Row row = sheet.createRow(bottomRowIndex);
        row.createCell(LOCAL_DATE_INDEX).setCellValue(day.getLocalDate().format(FORMATTER));
        row.createCell(WEEK_TYPE_INDEX).setCellValue(day.getDayOfWeekName());
        row.createCell(DAY_DESCRIPTION_INDEX).setCellValue(day.getDescription());
        CellStyle cellStyle = new CellStyler(workbook).holidayBodyStyle();
        for (Cell cell : row) {
            cell.setCellStyle(cellStyle);
        }
    }

    private void removeDayFromFile(Day day, Sheet sheet) {
        LocalDate targetDate = day.getLocalDate();
        for (int i = FIRST_DATA_ROW_INDEX; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            if (isBlankRow(row)) {
                continue;
            }
            Cell cell = row.getCell(DAY_DATA_INDEX);
            if (isCellBlank(cell)) {
                continue;
            }
            if (targetDate.equals(parseCellDate(cell))) {
                sheet.removeRow(row);
                shiftRowsUp(sheet, i);
                break;
            }
        }
    }

    private boolean isCellBlank(Cell cell) {
        return cell.getCellType() == CellType.BLANK;
    }

    private boolean isBlankRow(Row row) {
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

    private boolean isLocalDateFormatOnNumericType(Cell cell) {
        return cell.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(cell);
    }

    private void shiftRowsUp(Sheet sheet, int deletedRowIndex) {
        int lastRowNum = sheet.getLastRowNum();
        if (deletedRowIndex >= 0 && deletedRowIndex < lastRowNum) {
            sheet.shiftRows(deletedRowIndex + 1, lastRowNum, -1);
        }
    }

    private void handleExcelOperation(File outFile, Day day, BiConsumer<Sheet, Workbook> operation) {
        try (Workbook workbook = getWorkbook(outFile)) {
            Sheet sheet = workbook.getSheet(day.getYear() + "년");

            operation.accept(sheet, workbook);

            writeResult(outFile, workbook);
        } catch (IOException e) {
            throw new IllegalStateException("공휴일 데이터 처리 중 오류 발생: " + e.getMessage(), e);
        }
    }

    private Workbook getWorkbook(File file) throws IOException {
        try (FileInputStream fis = new FileInputStream(file)) {
            return new XSSFWorkbook(fis);
        }
    }

    private <T> void handleIOExceptionDuringWrite(T input, IOFunctionForWrite<T> ioFunction) {
        try {
            ioFunction.accept(input);
        } catch (IOException e) {
            throw new IllegalStateException("엑셀 파일을 작성하는 도중 오류가 발생했습니다: " + e.getMessage());
        }
    }
}

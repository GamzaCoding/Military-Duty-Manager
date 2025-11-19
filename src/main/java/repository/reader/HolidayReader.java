package repository.reader;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import service.model.day.Day;
import service.model.day.DayType;
import service.model.day.Days;

public class HolidayReader {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final int FIRST_DATA_ROW_INDEX = 1;
    private static final int LOCAL_DATE_INDEX = 0;

    public Days readHolidays(File excelFile, String sheetName) {
        return handleIOExceptionDuringRead(excelFile, file -> readHolidaysFromExcel(excelFile, sheetName));
    }

    private Days readHolidaysFromExcel(File excelFile, String sheetName) throws IOException {
        List<Day> holidays = new ArrayList<>();
        try (FileInputStream fis = new FileInputStream(excelFile);
             Workbook workbook = new XSSFWorkbook(fis)) {
            readHolidays(sheetName, workbook, holidays);
        }
        return Days.of(holidays);
    }

    private void readHolidays(String sheetName, Workbook workbook, List<Day> holidays) {
        for (Sheet sheet : workbook) {
            if (sheet.getSheetName().contains(sheetName + "년")) {
                readSheetData(sheet, holidays);
            }
        }
    }

    private void readSheetData(Sheet sheet, List<Day> holidays) {
        for (int rowIndex = FIRST_DATA_ROW_INDEX; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
            Row row = sheet.getRow(rowIndex);
            if (isNullRow(row)) {
                continue;
            }
            LocalDate date = parseDateFromRow(row);
            if (isNullDate(date)) {
                continue;
            }
            holidays.add(Day.of(date, DayType.HOLIDAY));
        }
    }

    private boolean isNullDate(LocalDate date) {
        return date == null;
    }

    private boolean isNullRow(Row row) {
        return row == null;
    }

    private LocalDate parseDateFromRow(Row row) {
        Cell cell = row.getCell(LOCAL_DATE_INDEX);
        if (isBlankCell(cell)) {
            return null;
        }
        LocalDate date = getLocalDate(cell);
        if (isBlankLocalDate(date)) {
            return null;
        }
        return date;
    }

    private boolean isBlankCell(Cell cell) {
        if (cell == null) {
            return true;
        }
        if (cell.getCellType() == CellType.BLANK) {
            return true;
        }
        return cell.getCellType() == CellType.STRING &&
                cell.getStringCellValue().trim().isEmpty();
    }

    private LocalDate getLocalDate(Cell cell) {
        if (isLocalDateFormatOnNumericType(cell)) {
            return cell.getLocalDateTimeCellValue().toLocalDate();
        }
        return LocalDate.parse(getStringValue(cell), DATE_FORMATTER);
    }

    private String getStringValue(Cell cell) {
        return cell.getStringCellValue().trim();
    }

    private boolean isLocalDateFormatOnNumericType(Cell cell) {
        return cell.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(cell);
    }

    private boolean isBlankLocalDate(LocalDate localDate) {
        return isNullDate(localDate);
    }

    private <T, R> R handleIOExceptionDuringRead(T input, IOFunctionForRead<T, R> ioFunction) {
        try {
            return ioFunction.apply(input);
        } catch (IOException e) {
            throw new IllegalStateException("엑셀 파일을 읽는 도중 오류가 발생했습니다: " + e.getMessage(), e);
        }
    }
}

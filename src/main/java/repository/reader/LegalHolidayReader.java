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
import service.model.day.WeekType;

public class LegalHolidayReader {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final int FIRST_DATA_ROW_INDEX = 1;
    private static final int LOCAL_DATE_INDEX = 0;

    public Days readLegalHolidays(File excelFile, String sheetName) {
        return handleIOExceptionDuringRead(excelFile, file -> readHolidaysFromExcel(excelFile, sheetName));
    }

    private Days readHolidaysFromExcel(File excelFile, String sheetName) throws IOException {
        List<Day> holidays = new ArrayList<>();

        try (FileInputStream fis = new FileInputStream(excelFile);
             Workbook workbook = new XSSFWorkbook(fis)) {

            for (Sheet sheet : workbook) {
                if (sheet.getSheetName().contains(sheetName + "년")) {
                    readSheetData(sheet, holidays);
                }
            }
        }
        return Days.of(holidays);
    }

    // List<Day> holidays 이부분을 리팩터링 해야 할거 같음
    private void readSheetData(Sheet sheet, List<Day> holidays) {
        for (int rowIndex = FIRST_DATA_ROW_INDEX; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
            Row row = sheet.getRow(rowIndex);

            if (isBlankRow(row)) {
                continue;
            }
            if (isBlankCell(row.getCell(LOCAL_DATE_INDEX))) {
                continue;
            }
            LocalDate localDate = getLocalDate(row.getCell(LOCAL_DATE_INDEX));
            if (isBlankLocalDate(localDate)) {
                continue;
            }
            WeekType weekType = WeekType.from(localDate.getDayOfWeek());
            holidays.add(Day.of(localDate, weekType, DayType.HOLIDAY));
        }
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

    private boolean isBlankRow(Row row) {
        return row == null;
    }

    private boolean isBlankLocalDate(LocalDate localDate) {
        return localDate == null;
    }

    private <T, R> R handleIOExceptionDuringRead(T input, IOFunctionForRead<T, R> ioFunction) {
        try {
            return ioFunction.apply(input);
        } catch (IOException e) {
            throw new IllegalStateException("엑셀 파일을 읽는 도중 오류가 발생했습니다: " + e.getMessage(), e);
        }
    }
}

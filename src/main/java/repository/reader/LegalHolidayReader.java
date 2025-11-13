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
             Workbook workbook = new XSSFWorkbook(fis)) { // 메모리 누수 문제로 try()로 감싸줌

            for (Sheet sheet : workbook) {
                if (sheet.getSheetName().contains(sheetName)) {
                    readSheetData(sheet, holidays);
                }
            }
        }
        return Days.of(holidays);
    }

    private void readSheetData(Sheet sheet, List<Day> holidays) {
        for (int i = FIRST_DATA_ROW_INDEX; i <= sheet.getPhysicalNumberOfRows(); i++) {
            Row row = sheet.getRow(i);

            if (row == null) {
                continue; // 빈 행은 건너뛰기
            }

            LocalDate localDate = getLocalDate(row.getCell(LOCAL_DATE_INDEX));
            if (localDate == null) { // 엑셀 행에 아무 데이터 없는데 그냥 셀이 존재할 경우 걸러내는 조건
                continue;
            }
            WeekType weekType = WeekType.from(localDate.getDayOfWeek());

            holidays.add(Day.of(localDate, weekType, DayType.HOLIDAY));
        }
    }

    private LocalDate getLocalDate(Cell cell) {
        if (cell == null) {
            return null;
        }

        if (cell.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(cell)) {
            return cell.getLocalDateTimeCellValue().toLocalDate();
        } else {
            String value = getStringValue(cell);
            if (value.isBlank()) {
                return null;
            }
            return LocalDate.parse(value, DATE_FORMATTER);
        }
    }

    private String getStringValue(Cell cell) {
        if (cell == null) {
            return "";
        }
        if (cell.getCellType() == CellType.STRING) {
            return cell.getStringCellValue().trim();
        }
        return "";
    }

    private <T, R> R handleIOExceptionDuringRead(T input, IOFunctionForRead<T, R> ioFunction) {
        try {
            return ioFunction.apply(input);
        } catch (IOException e) {
            throw new IllegalStateException("엑셀 파일을 읽는 도중 오류가 발생했습니다: " + e.getMessage(), e);
        }
    }
}

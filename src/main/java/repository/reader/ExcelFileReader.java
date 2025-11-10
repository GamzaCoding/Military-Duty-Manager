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
import service.model.person.Person;
import service.model.person.Persons;

public class ExcelFileReader {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final int FIRST_DATA_ROW_INDEX = 1;
    private static final int POSITION_INDEX = 0;
    private static final int RANK_INDEX = 1;
    private static final int NAME_INDEX = 2;
    private static final int MOVE_IN_INDEX = 3;
    private static final int MOVE_OUT_INDEX = 4;
    private static final String WEEKDAY_SHEET_NAME = "당직자 순서(평일)";
    private static final String HOLIDAY_SHEET_NAME = "당직자 순서(휴일)";

    public Persons readWeekdayPersons(File excelFile) {
        return handleIOExceptionDuringRead(excelFile, file -> readPersonsFromExcel(excelFile, WEEKDAY_SHEET_NAME));
    }

    public Persons readHolidayPersons(File excelFile) {
        return handleIOExceptionDuringRead(excelFile, file -> readPersonsFromExcel(excelFile, HOLIDAY_SHEET_NAME));
    }

    private Persons readPersonsFromExcel(File excelFile, String sheetName) throws IOException {
        List<Person> people = new ArrayList<>();

        try (FileInputStream fis = new FileInputStream(excelFile);
             Workbook workbook = new XSSFWorkbook(fis)) { // 메모리 누수 문제로 try()로 감싸줌

            for (Sheet sheet : workbook) {
                if (sheet.getSheetName().contains(sheetName)) {
                    readSheetData(sheet, people);
                }
            }
        }
        return Persons.of(people);
    }

    private void readSheetData(Sheet sheet, List<Person> people) {
        for (int i = FIRST_DATA_ROW_INDEX; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);

            if (row == null) {
                continue; // 빈 행은 건너뛰기
            }

            Integer position = getNumber(row.getCell(POSITION_INDEX));
            String rank = getStringValue(row.getCell(RANK_INDEX));
            String name = getStringValue(row.getCell(NAME_INDEX));
            LocalDate moveInDate = parseDate(row.getCell(MOVE_IN_INDEX));
            LocalDate moveOutDate = parseDate(row.getCell(MOVE_OUT_INDEX));

            people.add(Person.from(position, rank, name, moveInDate, moveOutDate));
        }
    }

    private LocalDate parseDate(Cell cell) {
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

    private Integer getNumber(Cell cell) {
        if (cell == null) {
            return 0;
        }
        if (cell.getCellType() == CellType.NUMERIC) {
            return (int) cell.getNumericCellValue();
        }
        return 0;
    }

    private <T, R> R handleIOExceptionDuringRead(T input, IOFunctionForRead<T, R> ioFunction) {
        try {
            return ioFunction.apply(input);
        } catch (IOException e) {
            throw new IllegalStateException("엑셀 파일을 읽는 도중 오류가 발생했습니다: " + e.getMessage(), e);
        }
    }
}

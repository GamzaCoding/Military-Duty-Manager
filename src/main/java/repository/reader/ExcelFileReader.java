package repository.reader;

import static repository.sampleData.Sample.*;

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
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import service.model.day.Day;
import service.model.day.DayType;
import service.model.duty.Duties;
import service.model.duty.Duty;
import service.model.person.Person;
import service.model.person.Persons;

public class ExcelFileReader {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final int FIRST_DATA_ROW_INDEX = 1;
    private static final int ORDER_INDEX = 0;
    private static final int RANK_INDEX = 1;
    private static final int NAME_INDEX = 2;
    private static final int MOVE_IN_INDEX = 3;
    private static final int MOVE_OUT_INDEX = 4;
    private static final int WEEK_SIZE = 7;

    public Duties readReulstDuties(File inputFile) {
        return handleIOExceptionDuringRead(inputFile, file -> readDutiesFromExcel(inputFile));
    }

    public Persons readWeekdayPersons(File inputFile) {
        return handleIOExceptionDuringRead(inputFile,
                file -> readPersonsFromExcel(inputFile, WEEKDAY_DUTY_ORDER_SHEET));
    }

    public Persons readHolidayPersons(File inputFile) {
        return handleIOExceptionDuringRead(inputFile,
                file -> readPersonsFromExcel(inputFile, HOLIDAY_DUTY_ORDER_SHEET));
    }

    private Duties readDutiesFromExcel(File inputFile) throws IOException {
        List<Duty> duties = new ArrayList<>();
        try (FileInputStream fis = new FileInputStream(inputFile)) {
            Workbook workbook = new XSSFWorkbook(fis);
            readDuties(workbook, duties);
        }
        return Duties.of(duties);
    }

    private void readDuties(Workbook workbook, List<Duty> duties) {
        for (Sheet sheet : workbook) {
            if (sheet.getSheetName().contains(RESULT_SHEET)) {
                readRowFromSheet(sheet, duties);
            }
        }
    }

    private void readRowFromSheet(Sheet sheet, List<Duty> duties) {
        for (int localDateRowIndex = 0; localDateRowIndex <= sheet.getPhysicalNumberOfRows();
             localDateRowIndex = localDateRowIndex + 2) {
            Row DateRow = sheet.getRow(localDateRowIndex);
            if (DateRow == null) {
                continue;
            }
            addDutyFormRow(sheet, duties, DateRow, localDateRowIndex);
        }
    }

    private void addDutyFormRow(Sheet sheet, List<Duty> duties, Row DateRow, int localDateRowIndex) {
        for (int cellIndex = 0; cellIndex < WEEK_SIZE; cellIndex++) {
            Duty duty = getDuty(sheet, DateRow, cellIndex, localDateRowIndex);
            if (duty == null) {
                continue;
            }
            duties.add(duty);
        }
    }

    private Duty getDuty(Sheet sheet, Row DateRow, int cellIndex, int localDateRowIndex) {
        Cell dateCell = DateRow.getCell(cellIndex);
        if (dateCell == null) {
            return null;
        }
        Day day = getDay(dateCell);
        Row personRow = sheet.getRow(getPersonRowIndex(localDateRowIndex));
        Person person = getPerson(personRow, cellIndex);
        if (person == null) {
            return null;
        }
        return Duty.of(day, person);
    }

    private int getPersonRowIndex(int localDateRowIndex) {
        return localDateRowIndex + 1;
    }

    private Persons readPersonsFromExcel(File excelFile, String sheetName) throws IOException {
        List<Person> people = new ArrayList<>();

        try (FileInputStream fis = new FileInputStream(excelFile);
             Workbook workbook = new XSSFWorkbook(fis)) {
            readPersons(sheetName, workbook, people);
        }
        return Persons.of(people);
    }

    private void readPersons(String sheetName, Workbook workbook, List<Person> people) {
        for (Sheet sheet : workbook) {
            if (sheet.getSheetName().contains(sheetName)) {
                readSheetData(sheet, people);
            }
        }
    }

    private void readSheetData(Sheet sheet, List<Person> people) {
        for (int rowIndex = FIRST_DATA_ROW_INDEX; rowIndex <= sheet.getPhysicalNumberOfRows(); rowIndex++) {
            Row row = sheet.getRow(rowIndex);
            if (isUnValidRow(row)) {
                continue;
            }
            Person person = parsePersonFromRow(row);
            people.add(person);
        }
    }

    private boolean isUnValidRow(Row row) {
        return row == null || getNumber(row.getCell(ORDER_INDEX)) == 0;
    }

    private Person parsePersonFromRow(Row row) {
        Integer order = getNumber(row.getCell(ORDER_INDEX));
        String rank = getStringValue(row.getCell(RANK_INDEX));
        String name = getStringValue(row.getCell(NAME_INDEX));
        LocalDate moveInDate = parseDate(row.getCell(MOVE_IN_INDEX));
        LocalDate moveOutDate = parseDate(row.getCell(MOVE_OUT_INDEX));
        return Person.from(order, rank, name, moveInDate, moveOutDate);
    }

    private Person getPerson(Row personRow, int cellIndex) {
        if (personRow == null) {
            return null;
        }
        Cell personCell = personRow.getCell(cellIndex);
        String[] split = personCell.getStringCellValue().split(" ");
        return Person.from(null, split[0], split[1], null, null);
    }

    private Day getDay(Cell dateCell) {
        String dutyDayString = dateCell.toString().trim();
        String[] split1 = dutyDayString.split("\\.");
        int year = Integer.parseInt("20" + split1[0].replace("'", ""));
        int month = Integer.parseInt(split1[1].trim());
        int day = Integer.parseInt(split1[2].trim());

        Day sampleDay;
        LocalDate localDate = LocalDate.of(year, month, day);
        if (dateCell.getCellStyle().getFillForegroundColor() == IndexedColors.ROSE.getIndex()) {
            sampleDay = Day.of(localDate, DayType.HOLIDAY);
        } else {
            sampleDay = Day.of(localDate, DayType.WEEKDAY);
        }
        return sampleDay;
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

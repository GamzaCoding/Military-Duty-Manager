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
    private static final int POSITION_INDEX = 0;
    private static final int RANK_INDEX = 1;
    private static final int NAME_INDEX = 2;
    private static final int MOVE_IN_INDEX = 3;
    private static final int MOVE_OUT_INDEX = 4;
    private static final String WEEKDAY_SHEET_NAME = "당직자 순서(평일)";
    private static final String HOLIDAY_SHEET_NAME = "당직자 순서(휴일)";
    private static final String RESULT_SHEET_NAME = "당직표 결과";

    public Persons readWeekdayPersons(File inputFile) {
        return handleIOExceptionDuringRead(inputFile, file -> readPersonsFromExcel(inputFile, WEEKDAY_SHEET_NAME));
    }

    public Persons readHolidayPersons(File inputFile) {
        return handleIOExceptionDuringRead(inputFile, file -> readPersonsFromExcel(inputFile, HOLIDAY_SHEET_NAME));
    }

    public Duties readReulstDuties(File inputFile) {
        return handleIOExceptionDuringRead(inputFile, file -> readDutiesFromExcel(inputFile));
    }

    private Duties readDutiesFromExcel(File inputFile) throws IOException {
        List<Duty> duties = new ArrayList<>();
        try (FileInputStream fis = new FileInputStream(inputFile)) {
            Workbook workbook = new XSSFWorkbook(fis);
            for (Sheet sheet : workbook) {
                if (sheet.getSheetName().contains(ExcelFileReader.RESULT_SHEET_NAME)) {
                    readDutiesData(sheet, duties);
                }
            }
        }
        return Duties.of(duties);
    }

    private Persons readPersonsFromExcel(File excelFile, String sheetName) throws IOException {
        List<Person> people = new ArrayList<>();

        try (FileInputStream fis = new FileInputStream(excelFile);
             Workbook workbook = new XSSFWorkbook(fis)) {

            for (Sheet sheet : workbook) {
                if (sheet.getSheetName().contains(sheetName)) {
                    readSheetData(sheet, people);
                }
            }
        }
        return Persons.of(people);
    }

    private void readDutiesData(Sheet sheet, List<Duty> duties) {
        for (int i = 0; i <= sheet.getPhysicalNumberOfRows(); i = i + 2) {
            Row DateRow = sheet.getRow(i);
            if (DateRow == null) {
                continue;
            }
            for (int cellIndex = 0; cellIndex < 7; cellIndex++) {
                Duty duty = getDuty(sheet, DateRow, cellIndex, i);
                if (duty == null) {
                    continue;
                }
                duties.add(duty);
            }
        }
    }

    private static Duty getDuty(Sheet sheet, Row DateRow, int cellIndex, int i) {
        Cell dateCell = DateRow.getCell(cellIndex);
        if (dateCell == null) {
            return null;
        }
        Day day = getDay(dateCell);
        Row personRow = sheet.getRow(i + 1);
        Person person = getPerson(personRow, cellIndex);
        if (person == null) {
            return null;
        }
        return Duty.of(day, person);
    }

    private static Person getPerson(Row personRow, int cellIndex) {
        if (personRow == null) {
            return null;
        }
        Cell personCell = personRow.getCell(cellIndex);
        String[] split = personCell.getStringCellValue().split(" ");
        return Person.from(null, split[0], split[1], null, null);
    }

    private static Day getDay(Cell dateCell) {
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

    private void readSheetData(Sheet sheet, List<Person> people) {
        for (int i = FIRST_DATA_ROW_INDEX; i <= sheet.getPhysicalNumberOfRows(); i++) {
            Row row = sheet.getRow(i);

            if (row == null) {
                continue; // 빈 행은 건너뛰기
            }

            Integer position = getNumber(row.getCell(POSITION_INDEX));
            if (position == 0) { // 엑셀 행에 아무 데이터 없는데 그냥 셀이 존재할 경우 걸러내는 조건
                continue;
            }
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

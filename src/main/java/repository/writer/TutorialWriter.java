package repository.writer;

import static repository.writer.sampleData.Sample.*;
import static repository.writer.util.CellSizeSetter.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import repository.writer.util.CellStyler;
import service.model.person.Person;
import service.model.person.Persons;

public class TutorialWriter implements ExcelFileWriter {
    public static final int ORDER_INDEX = 0;
    public static final int RANK_INDEX = 1;
    public static final int NAME_INDEX = 2;
    public static final int MOVE_IN_DATE_INDEX = 3;
    public static final int MOVE_OUT_DATE_INDEX = 4;
    public static final int HEADER_ROW_INDEX = 0;
    private final Persons weekPersons;
    private final Persons holidayPersons;

    private TutorialWriter(Persons weekPersons, Persons holidayPersons) {
        this.weekPersons = weekPersons;
        this.holidayPersons = holidayPersons;
    }

    public static TutorialWriter of(Persons weekPersons, Persons holidayPersons) {
        return new TutorialWriter(weekPersons, holidayPersons);
    }

    @Override
    public void write(File outputFile) {
        handleIOExceptionDuringWrite(outputFile, file -> writePersonsToExcel(file, weekPersons, holidayPersons));
    }

    private void writePersonsToExcel(File file, Persons weekPersons, Persons holidayPersons) throws IOException {
        try (Workbook workbook = new XSSFWorkbook()) {

            CellStyler cellStyler = new CellStyler(workbook);
            writePersonSheet(workbook, WEEKDAY_DUTY_ORDER_SHEET, weekPersons, cellStyler.weekdayPersonHeaderStyle());
            writePersonSheet(workbook, HOLIDAY_DUTY_ORDER_SHEET, holidayPersons, cellStyler.holidayPersonHeaderStyle());

            try (FileOutputStream fos = new FileOutputStream(file)) {
                workbook.write(fos);
            }
        }
    }
    private void writePersonSheet(Workbook workbook, String sheetName, Persons persons, CellStyle headerStyle) {
        Sheet sheet = workbook.createSheet(sheetName);
        CellStyler cellStyler = new CellStyler(workbook);

        writeHeader(headerStyle, sheet);
        writeBody(sheet, persons, cellStyler.personBodyStyle(), cellStyler.DateStyle());

        for (int categoryIndex = 0; categoryIndex < DUTY_ORDER_CATEGORY.size(); categoryIndex++) {
            applyBasicColumWidth(sheet, categoryIndex);
        }
        applyBasicRowHeight(sheet);
    }

    private void writeHeader(CellStyle headerStyle, Sheet sheet) {
        Row headerRow = sheet.createRow(HEADER_ROW_INDEX);
        for (int categoryIndex = 0; categoryIndex < DUTY_ORDER_CATEGORY.size(); categoryIndex++) {
            Cell cell = headerRow.createCell(categoryIndex);
            cell.setCellValue(DUTY_ORDER_CATEGORY.get(categoryIndex));
            cell.setCellStyle(headerStyle);
        }
    }

    private void writeBody(Sheet sheet, Persons persons, CellStyle bodyStyle, CellStyle dateStyle) {
        int personDataRowIndex = 1;
        for (Person person : persons.getPersons()) {
            Row row = sheet.createRow(personDataRowIndex++);
            createNumericCell(row, ORDER_INDEX, person.order(), bodyStyle);
            createStringCell(row, RANK_INDEX, person.rank(), bodyStyle);
            createStringCell(row, NAME_INDEX, person.name(), bodyStyle);
            createDateCell(row, MOVE_IN_DATE_INDEX, person.moveInDate(), dateStyle);
            createDateCell(row, MOVE_OUT_DATE_INDEX, person.moveOutDate(), dateStyle);
        }
    }

    private void createNumericCell(Row row, int cellIndex, int value, CellStyle style) {
        Cell cell = row.createCell(cellIndex, CellType.NUMERIC);
        cell.setCellValue(value);
        cell.setCellStyle(style);
    }

    private void createStringCell(Row row, int cellIndex, String value, CellStyle style) {
        validateNull(value);
        Cell cell = row.createCell(cellIndex);
        cell.setCellValue(value);
        cell.setCellStyle(style);
    }

    private void createDateCell(Row row, int cellIndex, LocalDate date, CellStyle style) {
        Cell cell = row.createCell(cellIndex);
        Optional.ofNullable(date)
                .map(d -> Date.from(d.atStartOfDay(ZoneId.systemDefault()).toInstant()))
                .ifPresentOrElse(cell::setCellValue, cell::setBlank);
        cell.setCellStyle(style);
    }

    private void validateNull(String value) {
        if (value == null) {
            throw new IllegalStateException("계급 혹은 이름값이 없습니다.");
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

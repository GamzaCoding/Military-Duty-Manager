package repository.writer.subWriter;

import static repository.sampleData.Sample.DUTY_ORDER_CATEGORY;
import static repository.writer.util.CellSizeSetter.*;

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
import repository.writer.util.CellStyler;
import service.model.day.DayType;
import service.model.person.Person;
import service.model.person.Persons;

public class DutyOrderWriter {
    private static final int ORDER_INDEX = 0;
    private static final int RANK_INDEX = 1;
    private static final int NAME_INDEX = 2;
    private static final int MOVE_IN_DATE_INDEX = 3;
    private static final int MOVE_OUT_DATE_INDEX = 4;
    private static final int HEADER_ROW_INDEX = 0;

    private final Workbook workbook;

    public DutyOrderWriter(Workbook workbook) {
        this.workbook = workbook;
    }

    public void createDutyOrderSheet(String sheetName, Persons persons, DayType dayType) {
        Sheet sheet = workbook.createSheet(sheetName);
        CellStyler cellStyler = new CellStyler(workbook);

        writeHeader(sheet, cellStyler.getHeaderStyleByDayType(dayType));
        writeBody(sheet, persons, cellStyler.personBodyStyle(), cellStyler.dateStyle());
        adjustLayout(sheet);
    }

    private void adjustLayout(Sheet sheet) {
        for (int categoriIndex = 0; categoriIndex < DUTY_ORDER_CATEGORY.size(); categoriIndex++) {
            applyBasicColumWidth(sheet, categoriIndex);
        }
        applyBasicRowHeight(sheet);
    }

    private void writeHeader(Sheet sheet, CellStyle headerStyle) {
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
}

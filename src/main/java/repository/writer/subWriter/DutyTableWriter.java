package repository.writer.subWriter;

import static repository.writer.util.CellSizeSetter.*;

import java.util.ArrayList;
import java.util.List;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import repository.writer.util.CellStyler;
import service.model.day.Day;
import service.model.duty.Duties;
import service.model.duty.Duty;
import service.model.person.Person;

public class DutyTableWriter {
    private static final int WEEK_SIZE = 7;

    private final Workbook workbook;

    public DutyTableWriter(Workbook workbook) {
        this.workbook = workbook;
    }

    public void writeDutyTable(String sheetName, Duties duties) {
        Sheet sheet = workbook.createSheet(sheetName);
        CellStyler cellStyler = new CellStyler(workbook);
        List<SevenDuties> weeklyGroups = groupByWeek(duties);

        writeResult(weeklyGroups, sheet, cellStyler.holidayStyleInDutyTable(), cellStyler.personBodyStyle());
        adjustSheetLayout(sheet);
    }

    private void writeResult(List<SevenDuties> weeklyGroups, Sheet sheet, CellStyle holidayStyle, CellStyle personStyle) {
        int tableRowIndex = 0;
        for (SevenDuties sevenDuties : weeklyGroups) {
            Row dateRow = sheet.createRow(tableRowIndex++);
            writeDateRow(dateRow, sevenDuties, holidayStyle);

            Row personRow = sheet.createRow(tableRowIndex++);
            writePersonRow(personRow, sevenDuties, personStyle);
        }
    }

    private void writeDateRow(Row row, SevenDuties sevenDuties, CellStyle holidayStyle) {
        for (int dutyIndex = 0; dutyIndex < sevenDuties.size(); dutyIndex++) {
            Duty duty = sevenDuties.get(dutyIndex);
            Day day = duty.getDay();
            writeDayCell(row, dutyIndex, day, holidayStyle);
        }
    }
    private void writeDayCell(Row row, int cellIndex, Day day, CellStyle holidayStyle) {
        Cell cell = row.createCell(cellIndex);
        cell.setCellValue(day.toString());
        applyHolidayStyleIfNeeded(cell, day, holidayStyle);
    }

    private void applyHolidayStyleIfNeeded(Cell cell, Day day, CellStyle holidayStyle) {
        if (day.isHoliday()) {
            cell.setCellStyle(holidayStyle);
        }
    }

    private void writePersonRow(Row row, SevenDuties sevenDuties, CellStyle style) {
        for (int dutyIndex = 0; dutyIndex < sevenDuties.size(); dutyIndex++) {
            Person person = sevenDuties.get(dutyIndex).getPerson();
            Cell cell = row.createCell(dutyIndex);
            cell.setCellValue(person.getRankAndName());
            cell.setCellStyle(style);
        }
    }

    private List<SevenDuties> groupByWeek(Duties duties) {
        List<SevenDuties> groups = new ArrayList<>();
        for (int sevenDutyStartIndex = 0; sevenDutyStartIndex < duties.size(); sevenDutyStartIndex += WEEK_SIZE) {
            int sevenDutyEndIndex = Math.min(sevenDutyStartIndex + WEEK_SIZE, duties.size());
            groups.add(new SevenDuties(duties.subList(sevenDutyStartIndex, sevenDutyEndIndex)));
        }
        return groups;
    }

    private void adjustSheetLayout(Sheet sheet) {
        for (int weekIndex = 0; weekIndex < WEEK_SIZE; weekIndex++) {
            applyBasicColumWidth(sheet, weekIndex);
        }
        applyDutyTableHeight(sheet);
    }

    private record SevenDuties(List<Duty> duties) {
        public Duty get(int index) {
            return duties.get(index);
        }
        public int size() {
            return duties.size();
        }
    }
}

package org.example.dutymanager.repository.writer.subWriter;

import static org.example.dutymanager.repository.writer.util.CellSizeSetter.*;

import java.util.ArrayList;
import java.util.List;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.example.dutymanager.repository.writer.util.CellStyler;
import org.example.dutymanager.service.model.day.Day;
import org.example.dutymanager.service.model.duty.Duties;
import org.example.dutymanager.service.model.duty.Duty;
import org.example.dutymanager.service.model.person.Person;

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

        writeResult(weeklyGroups, sheet, cellStyler);
        adjustSheetLayout(sheet);
    }

    private void writeResult(List<SevenDuties> weeklyGroups, Sheet sheet, CellStyler cellStyler) {
        int tableRowIndex = 0;
        for (SevenDuties sevenDuties : weeklyGroups) {
            Row dateRow = sheet.createRow(tableRowIndex++);
            writeDateRow(dateRow, sevenDuties, cellStyler);

            Row personRow = sheet.createRow(tableRowIndex++);
            writePersonRow(personRow, sevenDuties, cellStyler);
        }
    }

    private void writeDateRow(Row row, SevenDuties sevenDuties, CellStyler cellStyler) {
        for (int dutyIndex = 0; dutyIndex < sevenDuties.size(); dutyIndex++) {
            Duty duty = sevenDuties.get(dutyIndex);
            Day day = duty.getDay();
            writeDayCell(row, dutyIndex, day, cellStyler);
        }
    }
    private void writeDayCell(Row row, int cellIndex, Day day, CellStyler cellStyler) {
        Cell cell = row.createCell(cellIndex);
        cell.setCellValue(day.toString());
        cell.setCellStyle(cellStyler.weekdayStyleInDutyTable());
        applyHolidayStyleIfNeeded(cell, day, cellStyler);
    }

    private void applyHolidayStyleIfNeeded(Cell cell, Day day, CellStyler cellStyler) {
        if (day.isHoliday()) {
            cell.setCellStyle(cellStyler.holidayStyleInDutyTable());
        }
    }

    private void writePersonRow(Row row, SevenDuties sevenDuties, CellStyler cellStyler) {
        for (int dutyIndex = 0; dutyIndex < sevenDuties.size(); dutyIndex++) {
            Person person = sevenDuties.get(dutyIndex).getPerson();
            Cell cell = row.createCell(dutyIndex);
            cell.setCellValue(person.getRankAndName());
            cell.setCellStyle(cellStyler.personBodyStyle());
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

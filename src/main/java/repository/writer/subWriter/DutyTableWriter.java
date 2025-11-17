package repository.writer.subWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import service.model.day.Day;
import service.model.duty.Duties;
import service.model.duty.Duty;
import service.model.person.Person;

public class DutyTableWriter {
    public static final int WEEK_SIZE = 7;

    private final Workbook workbook;

    public DutyTableWriter(Workbook workbook) {
        this.workbook = workbook;
    }

    public void writeDutyTable(String sheetName, Duties duties) {
        Sheet sheet = workbook.createSheet(sheetName);

        CellStyle weekdayStyle = createWeekdayStyle(workbook);
        CellStyle holidayStyle = createHolidayStyle(workbook);
        CellStyle personStyle = createPersonStyle(workbook);

        // duties를 날짜에 맞게 정렬해야한다.

        List<SevenDuties> weeklyGroups = groupByWeek(duties);

        int rowIndex = 0;
        for (SevenDuties sevenDuties : weeklyGroups) {
            Row dateRow = sheet.createRow(rowIndex++);
            writeDateRow(dateRow, sevenDuties, weekdayStyle, holidayStyle);

            Row personRow = sheet.createRow(rowIndex++);
            writePersonRow(personRow, sevenDuties, personStyle);
        }
        adjustSheetLayout(sheet);
    }

    // DutyTableWriter 테스트를 위한 메서드
    public void saveWorkbook(File outputFile) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(outputFile)) {
            workbook.write(fos);
        }
    }

    private void writeDateRow(Row row, SevenDuties sevenDuties, CellStyle weekdayStyle, CellStyle holidayStyle) {
        for (int i = 0; i < sevenDuties.size(); i++) {
            Duty duty = sevenDuties.get(i);
            Day day = duty.getDay();
            Cell cell = row.createCell(i);
            cell.setCellValue(day.toString());

            if (day.isHoliday()) {
                cell.setCellStyle(holidayStyle);
            } else {
                cell.setCellStyle(weekdayStyle);
            }
        }
    }

    private void writePersonRow(Row row, SevenDuties sevenDuties, CellStyle style) {
        for (int i = 0; i < sevenDuties.size(); i++) {
            Person person = sevenDuties.get(i).getPerson();
            Cell cell = row.createCell(i);
            cell.setCellValue(person.getRankAndName());
            cell.setCellStyle(style);
        }
    }

    private CellStyle createWeekdayStyle(Workbook workbook) {
        CellStyle style = baseCenterStyle(workbook);
        style.setFillForegroundColor(IndexedColors.WHITE.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        return style;
    }

    private CellStyle createHolidayStyle(Workbook workbook) {
        CellStyle style = baseCenterStyle(workbook);
        style.setFillForegroundColor(IndexedColors.ROSE.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        return style;
    }

    private CellStyle createPersonStyle(Workbook workbook) {
        return baseCenterStyle(workbook);
    }

    private List<SevenDuties> groupByWeek(Duties duties) {
        List<SevenDuties> groups = new ArrayList<>();
        for (int i = 0; i < duties.size(); i += 7) {
            int end = Math.min(i + 7, duties.size());
            groups.add(new SevenDuties(duties.subList(i, end)));
        }
        return groups;
    }

    private CellStyle baseCenterStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        setThinBorder(style);

        Font font = workbook.createFont();
        font.setFontName("굴림");
        font.setFontHeightInPoints((short) 15);
        style.setFont(font);
        return style;
    }

    private void setThinBorder(CellStyle style) {
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
    }

    private void adjustSheetLayout(Sheet sheet) {
        for (int i = 0; i < WEEK_SIZE; i++) {
            sheet.autoSizeColumn(i);
            int width = sheet.getColumnWidth(i);
            sheet.setColumnWidth(i, (int) (width * 1.3));
        }
        for (Row row : sheet) {
            row.setHeightInPoints(40);
        }
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

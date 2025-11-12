package repository.writer.subWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
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
import service.model.day.DayType;
import service.model.person.Person;
import service.model.person.Persons;

public class DutyOrderWriter {
    public static final int HEADER_LENGTH = 5;
    private final Workbook workbook;

    public DutyOrderWriter(Workbook workbook) {
        this.workbook = workbook;
    }

    public void createDutyOrderSheet(String sheetName, Persons persons, DayType dayType) {
        Sheet sheet = workbook.createSheet(sheetName);
        CellStyle headerStyle = createHeaderStyleByDayType(dayType);
        CellStyle bodyStyle = createBodyStyle(workbook);
        createHeader(sheet, headerStyle);
        createBody(sheet, persons, bodyStyle);
        adjustLayout(sheet);
    }

    // DutyOrderWriter 테스트를 위한 메서드
    public void saveWorkbook(File outputFile) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(outputFile)) {
            workbook.write(fos);
        }
    }

    private void adjustLayout(Sheet sheet) {
        for (int i = 0; i < HEADER_LENGTH; i++) {
            sheet.autoSizeColumn(i);
            int width = sheet.getColumnWidth(i);
            sheet.setColumnWidth(i, (int) (width * 1.3));
        }
        for (Row row : sheet) row.setHeightInPoints(24);
    }

    private void createHeader(Sheet sheet, CellStyle headerStyle) {
        Row headerRow = sheet.createRow(0);
        List<String> headerTitles = List.of("당직순번", "계급", "이름", "전입일자(예정일 포함)", "전출일자(에정일 포함)");
        // 이거 stream으로 변경할 수  없나?
        for (int i = 0; i < headerTitles.size(); i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headerTitles.get(i));
            cell.setCellStyle(headerStyle);
        }
    }

    private CellStyle createHeaderStyleByDayType(DayType dayType) {
        if (dayType == DayType.HOLIDAY) {
            return createHeaderStyle(workbook, IndexedColors.LIGHT_CORNFLOWER_BLUE);
        }
        return createHeaderStyle(workbook, IndexedColors.LIGHT_GREEN);
    }

    private CellStyle createHeaderStyle(Workbook workbook, IndexedColors color) {
        CellStyle style = baseCenterStyle(workbook);
        style.setFillForegroundColor(color.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        Font font = workbook.createFont();
        font.setBold(true);
        font.setFontName("굴림");
        font.setFontHeightInPoints((short) 15);
        style.setFont(font);
        return style;
    }

    private CellStyle createBodyStyle(Workbook workbook) {
        CellStyle cellStyle = baseCenterStyle(workbook);
        Font font = workbook.createFont();
        font.setFontHeightInPoints((short) 15);
        font.setFontName("굴림");
        cellStyle.setFont(font);

        return cellStyle;
    }
    private CellStyle baseCenterStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        return style;
    }

    private void createBody(Sheet sheet, Persons persons, CellStyle bodyStyle) {
        int rowIndex = 1;
        for (Person person : persons.getPersons()) {
            Row row = sheet.createRow(rowIndex++);
            row.createCell(0).setCellValue(person.position());
            row.createCell(1).setCellValue(person.rank());
            row.createCell(2).setCellValue(person.name());
            row.createCell(3).setCellValue(person.getMoveInDateByMilitaryFormat());
            row.createCell(4).setCellValue(person.getMoveOutDateByMilitaryFormat());
            for (Cell cell : row) cell.setCellStyle(bodyStyle);
        }
    }
}

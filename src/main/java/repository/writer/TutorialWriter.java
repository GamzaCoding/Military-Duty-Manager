package repository.writer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import service.model.person.Person;
import service.model.person.Persons;

public class TutorialWriter implements ExcelFileWriter {

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
            CellStyle headerStyleOfWeekday = createHeaderStyleOfWeekday(workbook);
            CellStyle headerStyleOfHoliday = createHeaderStyleOfHoliday(workbook);
            CellStyle bodyStyle = createBodyStyle(workbook);
            CellStyle dateStyle = createDateStyle(workbook);

            writePersonSheet(workbook, "당직자 순서(평일)", weekPersons, headerStyleOfWeekday, bodyStyle, dateStyle);
            writePersonSheet(workbook, "당직자 순서(휴일)", holidayPersons, headerStyleOfHoliday, bodyStyle, dateStyle);

            try (FileOutputStream fos = new FileOutputStream(file)) {
                workbook.write(fos);
            }
        }
    }

    // 시트 생성 및 데이터 작성
    private void writePersonSheet(Workbook workbook, String sheetName, Persons persons,
                                  CellStyle headerStyle, CellStyle bodyStyle, CellStyle dateStyle) {
        Sheet sheet = workbook.createSheet(sheetName);

        // 헤더 작성
        Row headerRow = sheet.createRow(0);
        List<String> headers = List.of("순번", "계급", "이름", "전입일(예정일 포함)", "전출일(예정일 포함)") ;

        for (int i = 0; i < headers.size(); i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers.get(i));
            cell.setCellStyle(headerStyle);
        }

        // 당직자 데이터 작성
        int rowIndex = 1;
        for (Person person : persons.getPersons()) {
            Row row = sheet.createRow(rowIndex++);
            createNumericCell(row, 0, person.position(), bodyStyle);
            createStringCell(row, 1, person.rank(), bodyStyle);
            createStringCell(row, 2, person.name(), bodyStyle);
            createDateCell(row, 3, person.moveInDate(), dateStyle);
            createDateCell(row, 4, person.moveOutDate(), dateStyle);
        }

        // 셀 크기 및 행 높이 조정
        for (int i = 0; i < headers.size(); i++) {
            sheet.autoSizeColumn(i);
            int currentWidth = sheet.getColumnWidth(i);
            sheet.setColumnWidth(i, (int) (currentWidth * 1.3));
        }
        for (Row row : sheet) {
            row.setHeightInPoints(24);
        }
    }

    // 셀 생성 + 값 + 스타일 적용
    private void createNumericCell(Row row, int index, int value, CellStyle style) {
        Cell cell = row.createCell(index, CellType.NUMERIC);
        cell.setCellValue(value);
        cell.setCellStyle(style);
    }

    private void createStringCell(Row row, int index, String value, CellStyle style) {
        Cell cell = row.createCell(index);
        cell.setCellValue(value != null ? value : "");
        cell.setCellStyle(style);
    }

    private void createDateCell(Row row, int index, LocalDate date, CellStyle style) {
        Cell cell = row.createCell(index);
        if (date != null) {
            Date javaDate = Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant());
            cell.setCellValue(javaDate);
        } else {
            cell.setBlank();
        }
        cell.setCellStyle(style);
    }

    // 헤더 스타일 — 하늘색 배경 + 중앙정렬 + Bold
    private CellStyle createHeaderStyleOfWeekday(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setFillForegroundColor(IndexedColors.LIGHT_CORNFLOWER_BLUE.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        setBorder(style);

        Font font = workbook.createFont();
        font.setBold(true);
        font.setFontHeightInPoints((short) 16);
        font.setFontName("굴림");
        style.setFont(font);

        return style;
    }

    private CellStyle createHeaderStyleOfHoliday(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        setBorder(style);

        Font font = workbook.createFont();
        font.setBold(true);
        font.setFontHeightInPoints((short) 16);
        font.setFontName("굴림");
        style.setFont(font);

        return style;
    }

    // 본문 스타일 — 중앙정렬 + 얇은 테두리 + 기본 글꼴
    private CellStyle createBodyStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        setBorder(style);

        Font font = workbook.createFont();
        font.setFontHeightInPoints((short) 15);
        font.setFontName("굴림");
        style.setFont(font);

        return style;
    }

    private CellStyle createDateStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        setBorder(style);

        DataFormat format = workbook.createDataFormat();
        style.setDataFormat(format.getFormat("yy-MM-dd"));

        Font font = workbook.createFont();
        font.setFontHeightInPoints((short) 15);
        font.setFontName("굴림");
        style.setFont(font);

        return style;
    }

    // 공통 테두리 설정
    private void setBorder(CellStyle style) {
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
    }

    private <T> void handleIOExceptionDuringWrite(T input, IOFunctionForWrite<T> ioFunction) {
        try {
            ioFunction.accept(input);
        } catch (IOException e) {
            throw new IllegalStateException("엑셀 파일을 작성하는 도중 오류가 발생했습니다: " + e.getMessage());
        }
    }
}

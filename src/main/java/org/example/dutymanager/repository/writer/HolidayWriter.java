package org.example.dutymanager.repository.writer;

import static org.example.dutymanager.repository.sampleData.Sample.*;
import static org.example.dutymanager.repository.writer.util.CellSizeSetter.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.function.BiConsumer;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.example.dutymanager.repository.sampleData.SampleHolidays;
import org.example.dutymanager.repository.writer.util.CellStyler;
import org.example.dutymanager.service.model.day.Day;


public class HolidayWriter implements ExcelFileWriter {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final int FIRST_DATA_ROW_INDEX = 1;
    private static final int DAY_DATA_INDEX = 0;
    private static final int LOCAL_DATE_INDEX = 0;
    private static final int WEEK_TYPE_INDEX = 1;
    private static final int DAY_DESCRIPTION_INDEX = 2;

    @Override
    public void write(File outFile) {
        handleIOExceptionDuringWrite(outFile, this::writeTutorialHolidayForm);
    }

    // 추가시 dm에 이미 있으면 이미 공휴일로 지정된 날짜입니다. 라는 메시지 띄우기
    public void add(File outFile, Day day) {
        handleExcelOperation(outFile, day, (sheet, workbook) -> addDayToFile(day, sheet, workbook));
    }

    public void remove(File outFile, Day day) {
        handleExcelOperation(outFile, day, (sheet, workbook) -> removeDayFromFile(day, sheet));
    }

    private void writeTutorialHolidayForm(File file) throws IOException {
        try (Workbook workbook = new XSSFWorkbook()) {
            CellStyler cellStyler = new CellStyler(workbook);
            SampleHolidays sampleHolidays = new SampleHolidays();
            createSheets(workbook, sampleHolidays);
            writeHeader(workbook, cellStyler.holidayHeaderStyle());
            writeBasicBody(workbook, sampleHolidays);
            applyWidthAnsHeight(workbook);
            writeResult(file, workbook);
        }
    }

    private void applyWidthAnsHeight(Workbook workbook) {
        int numberOfSheets = workbook.getNumberOfSheets();
        for (int i = 0; i < numberOfSheets; i++) {
            Sheet sheet = workbook.getSheetAt(i);
            for (int categoryIndex = 0; categoryIndex < HOLIDAY_CATEGORY.size(); categoryIndex++) {
                applyBasicColumWidth(sheet, categoryIndex);
            }
            applyBasicRowHeight(sheet);
        }
    }

    private void createSheets(Workbook workbook, SampleHolidays sampleHolidays) {
        for (String sampleYear : sampleHolidays.getSampleYears()) {
            workbook.createSheet(sampleYear + "년");
        }

    }

    private void writeResult(File file, Workbook workbook) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(file)) {
            workbook.write(fos);
        }
    }

    private void writeHeader(Workbook workbook, CellStyle headerStyle) {
        int numberOfSheets = workbook.getNumberOfSheets();
        for (int sheetIndex = 0; sheetIndex < numberOfSheets; sheetIndex++) {
            Sheet sheet = workbook.getSheetAt(sheetIndex);

            Row headerRow = sheet.createRow(0);
            for (int categoryIndex = 0; categoryIndex < HOLIDAY_CATEGORY.size(); categoryIndex++) {
                Cell cell = headerRow.createCell(categoryIndex);
                cell.setCellValue(HOLIDAY_CATEGORY.get(categoryIndex));
                cell.setCellStyle(headerStyle);
            }
        }
    }

    private void writeBasicBody(Workbook workbook, SampleHolidays sampleHolidays) {
        for (Day sampleHoliday : sampleHolidays.getSampleHolidays()) {
            Sheet sheet = workbook.getSheet(sampleHoliday.getYear() + "년");
            addDayToFile(sampleHoliday, sheet, workbook);
        }
    }

    private void addDayToFile(Day day, Sheet sheet, Workbook workbook) {
        int bottomRowIndex = sheet.getLastRowNum() + 1;
        Row row = sheet.createRow(bottomRowIndex);
        row.createCell(LOCAL_DATE_INDEX).setCellValue(day.getLocalDate().format(FORMATTER));
        row.createCell(WEEK_TYPE_INDEX).setCellValue(day.getDayOfWeekName());
        row.createCell(DAY_DESCRIPTION_INDEX).setCellValue(day.getDescription());
        CellStyle cellStyle = new CellStyler(workbook).holidaysBodyStyle();
        for (Cell cell : row) {
            cell.setCellStyle(cellStyle);
        }
    }

    private void removeDayFromFile(Day day, Sheet sheet) {
        LocalDate targetDate = day.getLocalDate();
        for (int dataRowIndex = FIRST_DATA_ROW_INDEX; dataRowIndex <= sheet.getLastRowNum(); dataRowIndex++) {
            if (isTargetRow(sheet, dataRowIndex, targetDate)) {
                deleteRowAndShift(sheet, dataRowIndex);
            }
        }
    }

    private boolean isTargetRow(Sheet sheet, int dataRowIndex, LocalDate targetDate) {
        Row row = sheet.getRow(dataRowIndex);
        if (isBlankRow(row)) {
            return false;
        }
        Cell cell = row.getCell(DAY_DATA_INDEX);
        if (isCellBlank(cell)) {
            return false;
        }
        LocalDate dateInCell = parseCellDate(cell);
        return targetDate.equals(dateInCell);
    }

    private void deleteRowAndShift(Sheet sheet, int rowIndex) {
        Row row = sheet.getRow(rowIndex);
        sheet.removeRow(row);
        shiftRowsUp(sheet, rowIndex);
    }

    private boolean isCellBlank(Cell cell) {
        return cell.getCellType() == CellType.BLANK;
    }

    private boolean isBlankRow(Row row) {
        return row == null;
    }

    private LocalDate parseCellDate(Cell cell) {
        if (isLocalDateFormatOnNumericType(cell)) {
            return cell.getLocalDateTimeCellValue().toLocalDate();
        }
        if (cell.getCellType() == CellType.STRING) {
            String value = cell.getStringCellValue().trim();
            return LocalDate.parse(value, FORMATTER);
        }
        if (cell.getCellType() == CellType.NUMERIC) {
            return cell.getLocalDateTimeCellValue().toLocalDate();
        }
        throw new IllegalStateException("셀 타입이 날짜나 문자열이 아닙니다: " + cell.getCellType());
    }

    private boolean isLocalDateFormatOnNumericType(Cell cell) {
        return cell.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(cell);
    }

    private void shiftRowsUp(Sheet sheet, int deletedRowIndex) {
        int lastRowNum = sheet.getLastRowNum();
        if (deletedRowIndex >= 0 && deletedRowIndex < lastRowNum) {
            sheet.shiftRows(deletedRowIndex + 1, lastRowNum, -1);
        }
    }

    private void handleExcelOperation(File outFile, Day day, BiConsumer<Sheet, Workbook> operation) {
        try (Workbook workbook = getWorkbook(outFile)) {
            if (isSheetExist(workbook, day.getYear() + "년")) {
                Sheet sheet = workbook.getSheet(day.getYear() + "년");
                operation.accept(sheet, workbook);
                writeResult(outFile, workbook);
            } else {
                Sheet sheet = workbook.createSheet(day.getYear() + "년");
                CellStyler cellStyler = new CellStyler(workbook);
                writeHeader(workbook, cellStyler.holidayHeaderStyle());
                operation.accept(sheet, workbook);
                applyWidthAnsHeight(workbook);
                writeResult(outFile, workbook);
            }
        } catch (IOException e) {
            throw new IllegalStateException("공휴일 데이터 처리 중 오류 발생: " + e.getMessage(), e);
        }
    }

    private boolean isSheetExist(Workbook workbook, String sheetName) {
        return workbook.getSheet(sheetName) != null;
    }

    private Workbook getWorkbook(File file) throws IOException {
        try (FileInputStream fis = new FileInputStream(file)) {
            return new XSSFWorkbook(fis);
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

package org.example.dutymanager.repository.writer;

import static org.example.dutymanager.repository.sampleData.Sample.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.example.dutymanager.repository.writer.subWriter.DutyOrderWriter;
import org.example.dutymanager.service.model.day.DayType;
import org.example.dutymanager.service.model.person.Persons;


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

    private void writePersonsToExcel(File outputFile, Persons weekPersons, Persons holidayPersons) throws IOException {
        try (Workbook workbook = new XSSFWorkbook()) {
            DutyOrderWriter dutyOrderWriter = new DutyOrderWriter(workbook);
            dutyOrderWriter.createDutyOrderSheet(WEEKDAY_DUTY_ORDER_SHEET, weekPersons, DayType.WEEKDAY);
            dutyOrderWriter.createDutyOrderSheet(HOLIDAY_DUTY_ORDER_SHEET, holidayPersons, DayType.HOLIDAY);

            saveWorkbook(workbook, outputFile);
        }
    }

    private static void saveWorkbook(Workbook workbook, File outputFile) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(outputFile)) {
            workbook.write(fos);
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

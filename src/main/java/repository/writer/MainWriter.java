package repository.writer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import repository.writer.subWriter.DutyOrderWriter;
import repository.writer.subWriter.DutyTableWriter;
import service.model.day.DayType;
import service.model.duty.Duties;
import service.model.person.Persons;

public class MainWriter implements ExcelFileWriter{
    private final Persons weekPersons;
    private final Persons holidayPersons;
    private final Duties duties;

    public MainWriter(Persons weekPersons, Persons holidayPersons, Duties duties) {
        this.weekPersons = weekPersons;
        this.holidayPersons = holidayPersons;
        this.duties = duties;
    }

    @Override
    public void write(File outputFile) {
        validate(outputFile);
        handleIOException(outputFile, this::writeExcelFile);

    }

    private void writeExcelFile(File outputFile) throws IOException {
        try (Workbook workbook = new XSSFWorkbook()) {
            DutyOrderWriter dutyOrderWriter = new DutyOrderWriter(workbook);
            DutyTableWriter dutyTableWriter = new DutyTableWriter(workbook);

            dutyTableWriter.writeDutyTable("당직표 결과", duties);
            dutyOrderWriter.createDutyOrderSheet("당직자 순서(평일)",weekPersons, DayType.WEEKDAY);
            dutyOrderWriter.createDutyOrderSheet("당직자 순서(휴일)",holidayPersons, DayType.HOLIDAY);

            saveWorkbook(workbook, outputFile);
        }
    }

    private void saveWorkbook(Workbook workbook, File outputFile) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(outputFile)) {
            workbook.write(fos);
        }
    }

    private void validate(File outputFile) {
        if (outputFile == null) {
            throw new IllegalArgumentException("엑셀 결과 파일을 저장할 경로가 null입니다.");
        }
    }

    private <T> void handleIOException(T input, IOFunctionForWrite<T> ioFunction) {
        try {
            ioFunction.accept(input);
        } catch (IOException e) {
            throw new IllegalStateException("엑셀 파일 생성 중 오류가 발생했습니다: " + e.getMessage(), e);
        }
    }
}

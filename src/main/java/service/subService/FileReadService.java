package service.subService;

import java.io.File;
import repository.reader.ExcelFileReader;
import service.model.duty.Duties;
import service.model.person.Persons;

public class FileReadService {
    private final ExcelFileReader excelFileReader;

    public FileReadService() {
        this.excelFileReader = new ExcelFileReader();
    }

    public Persons readWeekPersons(File orderFile) {
        return excelFileReader.readWeekdayPersons(orderFile);
    }

    public Persons readHoliPersons(File orderFile) {
        return excelFileReader.readHolidayPersons(orderFile);
    }

    public Duties readDuties(File resultFile) {
        return excelFileReader.readReulstDuties(resultFile);
    }
}

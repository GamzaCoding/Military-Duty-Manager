package org.example.dutymanager.service.subService;

import java.io.File;
import org.example.dutymanager.repository.reader.ExcelFileReader;
import org.example.dutymanager.service.model.duty.Duties;
import org.example.dutymanager.service.model.person.Persons;

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

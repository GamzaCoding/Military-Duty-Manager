package org.example.dutymanager.service.subService;

import java.io.File;
import org.example.dutymanager.repository.FileLocation.ResultFileLocation;
import org.example.dutymanager.repository.writer.DutyResultWriter;
import org.example.dutymanager.service.model.duty.Duties;
import org.example.dutymanager.service.model.person.Persons;

public class FileWriteService {
    private final DutyResultWriter dutyResultWriter;
    private final ResultFileLocation resultFileLocation = new ResultFileLocation();

    public FileWriteService(Persons weekPersons, Persons holidayPersons, Duties duties) {
        this.dutyResultWriter = new DutyResultWriter(weekPersons, holidayPersons, duties);
    }

    public void write() {
        dutyResultWriter.write(resultFileLocation.getFile());
    }

    public File getResultFile() {
        return resultFileLocation.getFile();
    }
}

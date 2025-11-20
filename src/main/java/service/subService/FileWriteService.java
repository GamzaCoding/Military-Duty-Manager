package service.subService;

import repository.FileLocation.ResultFileLocation;
import repository.writer.DutyResultWriter;
import service.model.duty.Duties;
import service.model.person.Persons;

public class FileWriteService {
    private final DutyResultWriter dutyResultWriter;

    public FileWriteService(Persons weekPersons, Persons holidayPersons, Duties duties) {
        this.dutyResultWriter = new DutyResultWriter(weekPersons, holidayPersons, duties);
    }

    public void write() {
        dutyResultWriter.write(new ResultFileLocation().getFile());
    }
}

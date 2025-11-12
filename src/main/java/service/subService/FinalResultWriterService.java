package service.subService;

import java.io.File;
import java.time.LocalDate;
import repository.writer.MainWriter;
import service.model.duty.Duties;
import service.model.person.Persons;

public class FinalResultWriterService {
    private final File ouputFile;

    public FinalResultWriterService(File ouputFile) {
        this.ouputFile = ouputFile;
    }

    public void writeFinalResult(LocalDate startDate, LocalDate endDate, Persons weekPersons, Persons holidayPersons) {
        Duties duties = DutyOrderWriteService.makeResultDuty(startDate, endDate, weekPersons, holidayPersons);
        MainWriter finalWriter = new MainWriter(weekPersons, holidayPersons, duties);
        finalWriter.write(ouputFile);
    }
}

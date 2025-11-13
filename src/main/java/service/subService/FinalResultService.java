package service.subService;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import repository.writer.MainWriter;
import service.model.day.Days;
import service.model.duty.Duties;
import service.model.person.Persons;

public class FinalResultService {
    private final File ouputFile;

    public FinalResultService(File ouputFile) {
        this.ouputFile = ouputFile;
    }

    public void writeFinalResult(LocalDate startDate, LocalDate endDate, Persons weekPersons, Persons holidayPersons)
            throws IOException {

        DutyDayService dutyDayService = new DutyDayService();
        Days dutyDays = dutyDayService.makeDutyDays(startDate, endDate);

        Duties duties = DutyOrderService.makeResultDuty(dutyDays, weekPersons, holidayPersons);
        MainWriter finalWriter = new MainWriter(weekPersons, holidayPersons, duties);
        finalWriter.write(ouputFile);
    }
}

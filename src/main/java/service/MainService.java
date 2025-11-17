package service;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import service.model.day.Days;
import service.model.duty.Duties;
import service.model.duty.Duty;
import service.model.person.Person;
import service.model.person.Persons;
import service.subService.DutyChangeService;
import service.subService.DutyDayService;
import service.subService.DutyOrderService;
import service.subService.FileReadService;
import service.subService.FileWriteService;
import service.subService.HolidayService;
import service.subService.TutorialService;

public class MainService {
    private final DutyOrderService dutyOrderService;
    private final DutyDayService dutyDayService;
    private final DutyChangeService dutyChangeService;
    private final HolidayService holidayService;
    private final TutorialService tutorialService;
    private final FileReadService fileReadService;
    private Duties tempDuties;

    public MainService() {
        this.dutyOrderService = new DutyOrderService();
        this.dutyDayService = new DutyDayService();
        this.dutyChangeService = new DutyChangeService();
        this.holidayService = new HolidayService();
        this.tutorialService = new TutorialService();
        this.fileReadService = new FileReadService();
    }

    public void startTutorial() throws IOException {
        tutorialService.generateTutorialFiles();
    }

    public void calculateResult(LocalDate startDate, LocalDate endDate, File inputFile) throws IOException {
        Days dutyDays = dutyDayService.makeDutyDays(startDate, endDate);
        Persons weekdayPersons = fileReadService.readWeekPersons(inputFile);
        Persons holidayPersons = fileReadService.readHoliPersons(inputFile);

        Duties duties = dutyOrderService.makeResultDuty(dutyDays, weekdayPersons, holidayPersons);
        FileWriteService fileWriteService = new FileWriteService(weekdayPersons, holidayPersons, duties);
        fileWriteService.write();
    }

    public void readDutyFile(File inputFile) {
        tempDuties = fileReadService.readDuties(inputFile);
    }

    public void changDutyEachOther(Duty dutyTo, Duty dutyFrom) {
        if (tempDuties != null) {
            tempDuties = dutyChangeService.changeDutyBothSides(tempDuties, dutyTo, dutyFrom);
        }
    }

    public void changeDutyOneSide(Duty duty, Person person) {
        if (tempDuties != null) {
            tempDuties = dutyChangeService.changeDutyOneSide(tempDuties, duty, person);
        }
    }
}

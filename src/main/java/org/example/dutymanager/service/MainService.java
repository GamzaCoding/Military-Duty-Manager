package org.example.dutymanager.service;

import java.io.File;
import java.time.LocalDate;
import org.example.dutymanager.service.model.day.Day;
import org.example.dutymanager.service.model.day.Days;
import org.example.dutymanager.service.model.duty.Duties;
import org.example.dutymanager.service.model.duty.Duty;
import org.example.dutymanager.service.model.person.Person;
import org.example.dutymanager.service.model.person.Persons;
import org.example.dutymanager.service.subService.DutyChangeService;
import org.example.dutymanager.service.subService.DutyDayService;
import org.example.dutymanager.service.subService.DutyOrderService;
import org.example.dutymanager.service.subService.FileReadService;
import org.example.dutymanager.service.subService.FileWriteService;
import org.example.dutymanager.service.subService.HolidayService;
import org.example.dutymanager.service.subService.TutorialService;


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

    public void startTutorial() {
        tutorialService.generateTutorialFiles();
    }

    public void createHolidayDB() {
        if (holidayService.alreadyDBExist()) {
            holidayService.changeDBName();
        }
        holidayService.createHolidayDB();
    }

    public File calculateResult(LocalDate startDate, LocalDate endDate, File inputFile) {
        Days dutyDays = dutyDayService.makeDutyDays(startDate, endDate);
        Persons weekdayPersons = fileReadService.readWeekPersons(inputFile);
        Persons holidayPersons = fileReadService.readHoliPersons(inputFile);

        Duties duties = dutyOrderService.makeResultDuty(dutyDays, weekdayPersons, holidayPersons);
        FileWriteService fileWriteService = new FileWriteService(weekdayPersons, holidayPersons, duties);
        fileWriteService.write();
        return fileWriteService.getResultFile();
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

    public void changeWeekdayToHoliday(Day day, String description) {
        holidayService.makeDayAsHoliday(day, description);
    }

    public void changeHolidayToWeekday(Day day) {
        holidayService.makeDayAsWeekday(day);
    }

    public File getTutorialFile() {
        return tutorialService.getFile();
    }
}

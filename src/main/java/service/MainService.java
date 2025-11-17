package service;

import java.io.IOException;
import java.time.LocalDate;
import service.subService.DutyChangeService;
import service.subService.DutyDayService;
import service.subService.DutyOrderService;
import service.subService.HolidayService;
import service.subService.TutorialService;

public class MainService {
    private final DutyOrderService dutyOrderService;
    private final DutyDayService dutyDayService;
    private final DutyChangeService dutyChangeService;
    private final HolidayService holidayService;
    private final TutorialService tutorialService;

    public MainService() {
        this.dutyOrderService = new DutyOrderService();
        this.dutyDayService = new DutyDayService();
        this.dutyChangeService = new DutyChangeService();
        this.holidayService = new HolidayService();
        this.tutorialService = new TutorialService();
    }

    public void startTutorial() throws IOException {
        tutorialService.generateTutorialFiles();
    }

    public void calculateResult(LocalDate startDate, LocalDate endDate) {

    }


}

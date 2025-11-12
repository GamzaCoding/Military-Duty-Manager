package service.subService;

import java.io.IOException;
import org.junit.jupiter.api.Test;

class TutorialServiceTest {

    @Test
    void 당직순서_양식_생성_테스트() throws IOException {
        TutorialService tutorialService = new TutorialService();
        tutorialService.createWeekdayHoliDayDutyOrderFile();
    }
}
package service.subService;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import repository.reader.ExcelFileReader;
import repository.writer.location.ResultFileLocation;
import service.model.person.Persons;

class TutorialServiceTest {

    @Test
    void 당직순서_양식_생성_테스트() throws IOException {
        TutorialService tutorialService = new TutorialService();
        tutorialService.createWeekdayHoliDayDutyOrderFile();
    }

    @Test
    void 당직순서_양식_읽어와서_당직결과_생성_테스트() throws IOException {
        // given
        ExcelFileReader excelFileReader = new ExcelFileReader();
        String filePath = "/Users/seok/openMission/openMission-Military-Duty-Management/tutorial/당직자 순서(양식)_2025년_11월_11일_17시_02분.xlsx";
        File file = new File(filePath);

        Persons holidayPersons = excelFileReader.readHolidayPersons(file);
        Persons weekdayPersons = excelFileReader.readWeekdayPersons(file);

        LocalDate startDate = LocalDate.of(2025, 11, 11); // 외부 입력 값
        LocalDate endDate = LocalDate.of(2025, 12, 30); // 외부 입력 값

        ResultFileLocation resultFileLocation = new ResultFileLocation();
        File location = resultFileLocation.getLocation();

        // when
        DutyResultWriteService dutyResultWriteService = new DutyResultWriteService(location);
        dutyResultWriteService.writeDutyResult(startDate, endDate, weekdayPersons, holidayPersons);

        // then
    }
}
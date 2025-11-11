package service.subService;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import repository.reader.ExcelFileReader;
import repository.writer.ResultFileLocation;
import service.model.person.Persons;

class DutyResultWriteServiceTest {

    @Test
    void 최종_당직_작성_테스트() throws IOException {
        // given
        ExcelFileReader excelFileReader = new ExcelFileReader();
        String filePath = "/Users/seok/openMission/당직자 순서(입력 데이터).xlsx";
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
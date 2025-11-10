package service.subService;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import repository.reader.ExcelFileReader;
import repository.writer.DutyResultWriter;
import repository.writer.ResultFileLocation;
import service.model.duty.Duties;
import service.model.person.Persons;

class DutyMakeServiceTest {

    @Test
    void 당직_객체_생성_서비스_테스트() throws IOException {
        // given

        ExcelFileReader excelFileReader = new ExcelFileReader();
        String filePath = "/Users/seok/openMission/당직자 순서(입력 데이터).xlsx";
        File file = new File(filePath);
        Persons persons = excelFileReader.readHolidayPersons(file);

        DutyMakeService dutyMakeService = new DutyMakeService(persons);
        LocalDate startDate = LocalDate.of(2025,11,1);
        LocalDate endDate = LocalDate.of(2025,11,30);

        // when
        Duties duties = dutyMakeService.makeDuties(startDate, endDate);
        System.out.println(duties.toString());

        DutyResultWriter dutyResultWriter = DutyResultWriter.of(duties.getDuties());
        ResultFileLocation resultFileLocation = new ResultFileLocation();
        File outputFile = resultFileLocation.getLocation();
        dutyResultWriter.write(outputFile);
        // then
    }

}
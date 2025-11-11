package controller;

import java.io.File;
import java.io.IOException;
import repository.reader.ExcelFileReader;
import repository.writer.ExcelFileWriterForTest;
import repository.writer.ResultFileLocation;
import service.model.person.Persons;

public class TestController {

    public static void main(String[] args) throws IOException {
        Persons persons = readeProcess();
        writeProcess(persons);

    }

    private static void writeProcess(Persons persons) throws IOException {
        ExcelFileWriterForTest excelFileWriter = ExcelFileWriterForTest.of(persons);
        ResultFileLocation resultFileLocation = new ResultFileLocation();
        excelFileWriter.write(resultFileLocation.getLocation());
    }

    private static Persons readeProcess() {
        ExcelFileReader excelFileReader = new ExcelFileReader();
        String filePath = "/Users/seok/openMission/당직자 순서(입력 데이터).xlsx";
        File file = new File(filePath);
        Persons persons = excelFileReader.readWeekdayPersons(file);
        persons.getPersons().forEach(System.out::println);
        return persons;
    }
}

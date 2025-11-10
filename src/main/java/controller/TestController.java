package controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import repository.reader.ExcelFileReader;
import repository.writer.ExcelFileWriter;
import repository.writer.ResultFileLocation;
import service.model.Person;

public class TestController {

    public static void main(String[] args) throws IOException {

        List<Person> people = readeProcess();
        writeProcess(people);

    }

    private static void writeProcess(List<Person> people) throws IOException {
        ExcelFileWriter excelFileWriter = new ExcelFileWriter();
        ResultFileLocation resultFileLocation = new ResultFileLocation();
        excelFileWriter.writePersons(resultFileLocation.getLocation(), people);
    }

    private static List<Person> readeProcess() {
        ExcelFileReader excelFileReader = new ExcelFileReader();
        String filePath = "/Users/seok/openMission/당직자 순서(입력 데이터).xlsx";
        File file = new File(filePath);
        List<Person> soldiers = excelFileReader.readPersons(file);
        soldiers.forEach(System.out::println);
        return soldiers;
    }
}

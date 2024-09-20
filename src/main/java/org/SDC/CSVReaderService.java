package org.SDC;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;

public class CSVReaderService {


    private static final int HEADER_LINES = 10;  // Počet řádků v hlavičce
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmm");

    private DataProcessor dataProcessor;

    public CSVReaderService(DataProcessor dataProcessor) {
        this.dataProcessor = dataProcessor;
    }

    public void readAndProcessCSV(String filePath, String from, String to, int day) throws IOException {

        UserChoicesHandler userChoicesHandler = new UserChoicesHandler();
        ApplicationRunner applicationRunner = new ApplicationRunner();

        // Parsování zadaných parametrů FROM a TO
        YearMonth fromDate = YearMonth.parse(from, DateTimeFormatter.ofPattern("yyyyMM"));
        YearMonth toDate = YearMonth.parse(to, DateTimeFormatter.ofPattern("yyyyMM"));
        YearMonth theLastMonthYear = toDate;


        try (Reader reader = new FileReader(filePath);
             CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT)) {

            int currentLine = 0;

            for (CSVRecord csvRecord : csvParser) {
                currentLine++;

                // Přeskočení hlavičky
                if (currentLine <= HEADER_LINES) {
                    // Vypisování hlavičky
                    System.out.println(csvRecord.get(0) + ": " + csvRecord.get(1));
                    continue;
                }

                // Zpracování dat
                String timestampStr = csvRecord.get(0); // První sloupec (A)
                String valueStr = csvRecord.get(1);     // Druhý sloupec (B)
                    if (valueStr == null || valueStr.isEmpty()) { // Prázdné políčko definuj jako 0 (chybějící data)
                        valueStr = "0";
                    }

                LocalDateTime timestamp = LocalDateTime.parse(timestampStr, formatter);
                double value = Double.parseDouble(valueStr); // Převod na číslo

                // Filtrace na základě FROM/TO
                YearMonth recordYearMonth = YearMonth.from(timestamp);
                if (!recordYearMonth.isBefore(fromDate) && !recordYearMonth.isAfter(toDate)) {
                    // Předání dat ke zpracování
                    dataProcessor.processData(timestamp, value, day, theLastMonthYear);
                }

            }
        }
        dataProcessor.logTotalResults();

        if (userChoicesHandler.askForRepeatOrExit()) {
            applicationRunner.run();
        } else {
            System.out.println("Program ukončen.");
        }
    }
}

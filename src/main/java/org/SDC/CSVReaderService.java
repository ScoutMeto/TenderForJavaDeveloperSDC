package org.SDC;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;

/**
 * This class is responsible for reading and processing CSV files containing solar data.
 * It parses the input data based on user-specified date ranges and sends the data
 * to the `DataProcessor` for further calculations.
 *
 * Author: Matej Pella
 */
public class CSVReaderService {

    private static final int HEADER_LINES = 10;  // Počet řádků v hlavičce
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmm");

    private DataProcessor dataProcessor;

    /**
     * Constructor to initialize CSVReaderService with a DataProcessor instance.
     *
     * @param dataProcessor an instance of DataProcessor that handles data calculations.
     */
    public CSVReaderService(DataProcessor dataProcessor) {
        this.dataProcessor = dataProcessor;
    }

    /**
     * Reads and processes a CSV file by filtering the data based on date range and optional day selection.
     * After processing the CSV, it logs the results and asks if the user wants to repeat the process or exit the program.
     *
     * @param filePath the path to the CSV file.
     * @param from     the start of the date range (format yyyyMM).
     * @param to       the end of the date range (format yyyyMM).
     * @param day      an optional day of the week to filter data, where 1 = Monday, ..., 7 = Sunday.
     * @throws IOException if the file cannot be read or processed.
     */
    public void readAndProcessCSV(String filePath, String from, String to, int day) throws IOException {

        UserChoicesHandler userChoicesHandler = new UserChoicesHandler();
        ApplicationRunner applicationRunner = new ApplicationRunner();

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

                String timestampStr = csvRecord.get(0); // First column.
                String valueStr = csvRecord.get(1);     // Second column.
                if (valueStr == null || valueStr.isEmpty()) { // If empty = 0.
                    valueStr = "0";
                }

                LocalDateTime timestamp = LocalDateTime.parse(timestampStr, formatter);
                double value = Double.parseDouble(valueStr);

                // From/To filtration.
                YearMonth recordYearMonth = YearMonth.from(timestamp);
                if (!recordYearMonth.isBefore(fromDate) && !recordYearMonth.isAfter(toDate)) {
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

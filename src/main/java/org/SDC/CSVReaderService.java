package org.SDC;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CSVReaderService {

    private static final int HEADER_LINES = 10;  // Počet řádků v hlavičce
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmm");

    private DataProcessor dataProcessor;

    public CSVReaderService(DataProcessor dataProcessor) {
        this.dataProcessor = dataProcessor;
    }

    public void readAndProcessCSV(String filePath, String from, String to, int day) throws IOException {

        // Kontrola, zda jsou FROM a TO zadané
        if (from == null || from.isEmpty()) {
            throw new IllegalArgumentException("Chyba: Povinný parametr FROM nebyl zadán.");
        }
        if (to == null || to.isEmpty()) {
            throw new IllegalArgumentException("Chyba: Povinný parametr TO nebyl zadán.");
        }

        // Kontrola správného formátu (yyyymm nebo yyyymmdd) pomocí regex výrazu
        if (!from.matches("^\\d{6}$") && !from.matches("^\\d{8}$")) {
            throw new IllegalArgumentException("Chyba: Parametr FROM není ve správném formátu (yyyyMM nebo yyyyMMdd).");
        }
        if (!to.matches("^\\d{6}$") && !to.matches("^\\d{8}$")) {
            throw new IllegalArgumentException("Chyba: Parametr TO není ve správném formátu (yyyyMM nebo yyyyMMdd).");
        }

        LocalDateTime fromDate = LocalDateTime.parse(from + "T0000", formatter);  // Datum FROM
        LocalDateTime toDate = LocalDateTime.parse(to + "T2359", formatter);      // Datum TO

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
                    } else if (!valueStr.matches("^[0-9.]+$")) {
                        throw new IllegalArgumentException("Hodnota náležící k datu " + timestampStr + " obsahuje nepovolené znaky: " + valueStr);
                    }

                LocalDateTime timestamp = LocalDateTime.parse(timestampStr, formatter);
                double value = Double.parseDouble(valueStr); // Převod na číslo

                // Filtrace na základě FROM/TO
                if (!timestamp.isBefore(fromDate) && !timestamp.isAfter(toDate)) {
                    // Předání dat ke zpracování
                    dataProcessor.processData(timestamp, value, day);
                }
            }
        }
    }
}

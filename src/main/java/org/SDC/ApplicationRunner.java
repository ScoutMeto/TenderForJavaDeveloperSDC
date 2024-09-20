package org.SDC;

import java.io.IOException;

public class ApplicationRunner {

    public void run() {
        // Vytvoření objektů
        DataProcessor dataProcessor = new DataProcessor();
        CSVReaderService csvReaderService = new CSVReaderService(dataProcessor);
        UserChoicesHandler userChoicesHandler = new UserChoicesHandler();

            String fromDate = userChoicesHandler.askForDate("Zadejte počáteční datum ve formátu yyyyMM (6 znaků): ");  // Získání hodnoty FROM od uživatele
            String toDate = userChoicesHandler.askForDate("Zadejte koncové datum ve formátu yyyyMM (6 znaků): ");      // Získání hodnoty TO od uživatele
            Integer chosenDay = userChoicesHandler.askForDay();     // Získání volitelného dne


        try {
            // Čtení a zpracování CSV souboru
            csvReaderService.readAndProcessCSV("src/main/resources/dataexport.csv", fromDate, toDate, chosenDay == null ? 0 : chosenDay);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

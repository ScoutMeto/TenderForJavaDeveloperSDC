package org.SDC;

import java.io.IOException;


public class Main {
    public static void main(String[] args) {
        // Vytvoření objektů
        DataProcessor dataProcessor = new DataProcessor();
        CSVReaderService csvReaderService = new CSVReaderService(dataProcessor);
        UserChoicesHandler userChoicesHandler = new UserChoicesHandler();

        // Výzva k zadání vstupů
        String fromDate = userChoicesHandler.askForFromDate();  // Získání hodnoty FROM od uživatele
        String toDate = userChoicesHandler.askForToDate();      // Získání hodnoty TO od uživatele
        Integer chosenDay = userChoicesHandler.askForDay();     // Získání volitelného dne

        MyLogger.logInfo("Zadané hodnoty OD, DO, NEPOVINNĚ VOLITELNÝ DEN: " + fromDate + "/" + toDate + "/" + chosenDay);

        try {
            // Čtení a zpracování CSV souboru
            csvReaderService.readAndProcessCSV("src/main/resources/dataexport.csv", fromDate, toDate, chosenDay == null ? 0 : chosenDay);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

/*
Vychytávky?
-využití API
-
 */
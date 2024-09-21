package org.SDC;

import java.io.IOException;

/**
 * Main class responsible for running the application.
 * <p>
 * This class handles the initialization of essential components, including
 * the `DataProcessor`, `CSVReaderService`, and `UserChoicesHandler` classes.
 * It collects user inputs for the date range and optional day selection,
 * then triggers the reading and processing of CSV data.
 * <p>
 * The results are either printed to the console or saved depending on the user's choice.
 *
 * @author Matej Pella
 */
public class ApplicationRunner {

    /**
     * Executes the main application logic.
     * <p>
     * This method initializes necessary services, prompts the user for the
     * 'from' and 'to' dates in the format YYYYMM, and an optional day.
     * It then processes the CSV file based on the input and outputs the results.
     *
     * @throws IOException if the CSV file cannot be read or processed.
     */
    public void run() {
        DataProcessor dataProcessor = new DataProcessor();
        CSVReaderService csvReaderService = new CSVReaderService(dataProcessor);
        UserChoicesHandler userChoicesHandler = new UserChoicesHandler();

        String fromDate = userChoicesHandler.askForDate("Zadejte počáteční datum ve formátu yyyyMM (6 znaků): ");
        String toDate = userChoicesHandler.askForDate("Zadejte koncové datum ve formátu yyyyMM (6 znaků): ");
        Integer chosenDay = userChoicesHandler.askForDay();

        try {
            csvReaderService.readAndProcessCSV("src/main/resources/dataexport.csv", fromDate, toDate, chosenDay == null ? 0 : chosenDay);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

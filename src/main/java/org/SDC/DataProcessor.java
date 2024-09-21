package org.SDC;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.*;
import java.time.format.TextStyle;
import java.util.Locale;

/**
 * Class responsible for processing solar radiation data.
 *
 * This class handles calculations of total values over a season (defined by a date range),
 * monthly summaries, and daily performance for a specific day of the week if selected.
 * It also manages logging of results via LogManager.
 *
 * The class accumulates and stores data while processing the CSV file row by row.
 *
 * @see CSVReaderService
 * @author Matej Pella
 */
public class DataProcessor {

    private static final Logger logger = LogManager.getLogger(DataProcessor.class);

    private long allSeasonValue = 0;
    private long oneMonthValue = 0;
    private long daySum = 0;
    private long allValuesOfChosenDayInOneMonth = 0;
    private long allValuesOfChosenDayInSeason = 0;
    private int numberOfChosenDaysInMonth = 0;
    private Month currentMonth = null;
    private LocalDateTime actualInformationAboutDateTime = null;

    /**
     * Processes a single row of solar radiation data.
     *
     * This method manages the monthly and daily data logging, updates the current monthly and seasonal totals,
     * and computes detailed results for a specific day of the week if provided by the user.
     * It delegates the handling of month and day changes to specialized methods.
     *
     * @param timestamp   The date and time of the data point.
     * @param value       The solar radiation value for the given timestamp.
     * @param chosenDay   Optional: Day of the week (1 = Monday, ..., 7 = Sunday) for additional calculations.
     * @param theLastMonthYear The last year and month to determine the end of the period.
     */
    public void processData(LocalDateTime timestamp, double value, Integer chosenDay, YearMonth theLastMonthYear) {
        int toYear = theLastMonthYear.getYear();
        Month toMonth = theLastMonthYear.getMonth();
        Month month = timestamp.getMonth();
        DayOfWeek chosenDayOfWeek = (chosenDay != null && chosenDay > 0) ? DayOfWeek.of(chosenDay) : null;
        int dayOfMonth = timestamp.getDayOfMonth();

        // Zpracování měsíční změny
        handleMonthChange(timestamp, month, chosenDayOfWeek, toYear, toMonth, chosenDay);

        // Zpracování denní změny
        handleDayChange(timestamp, dayOfMonth, chosenDayOfWeek, toYear, toMonth, chosenDay);

        // Přidání hodnoty do měsíčního a sezónního součtu
        allSeasonValue += value;
        oneMonthValue += value;

        // Pokud uživatel zvolil konkrétní den v týdnu, provede detailní analýzu
        if (chosenDayOfWeek != null && timestamp.getDayOfWeek().getValue() == chosenDay) {
            daySum += value;
            allValuesOfChosenDayInOneMonth += value;
            allValuesOfChosenDayInSeason += value;
            numberOfChosenDaysInMonth++;
        }

        actualInformationAboutDateTime = timestamp;
    }

    /**
     * Handles logic related to switching between months during data processing.
     *
     * This method logs monthly results, resets monthly data when the month changes,
     * and ensures that daily results are logged for the selected day of the week
     * before the month transitions.
     *
     * @param timestamp        The current data point's timestamp.
     * @param month            The current month being processed.
     * @param chosenDayOfWeek   The day of the week chosen by the user for specific analysis (1 = Monday, ..., 7 = Sunday).
     * @param toYear           The year of the last month to determine the end of the period.
     * @param toMonth          The month to determine the end of the period.
     * @param chosenDay        The specific day of the week chosen by the user for detailed analysis.
     */
    private void handleMonthChange(LocalDateTime timestamp, Month month, DayOfWeek chosenDayOfWeek, int toYear, Month toMonth, Integer chosenDay) {
        if (actualInformationAboutDateTime != null && actualInformationAboutDateTime.getMonth() != month) {
            if (chosenDayOfWeek != null && actualInformationAboutDateTime.getDayOfWeek().getValue() == chosenDay && !actualInformationAboutDateTime.toLocalDate().equals(timestamp.toLocalDate())) {
                if (!(actualInformationAboutDateTime.getYear() == toYear && actualInformationAboutDateTime.getMonth() == toMonth)) {
                    logDailyResults();
                }
            }
            logMonthlyResults(chosenDayOfWeek);
            resetMonthlyData();
        }

        if (currentMonth == null || !currentMonth.equals(month)) {
            currentMonth = month;
        }
    }

    /**
     * Handles logic related to switching between days during data processing.
     *
     * This method logs daily results for the selected day of the week, resets daily totals when the day changes,
     * and checks whether the current day is the last day of the month before logging.
     *
     * @param timestamp        The current data point's timestamp.
     * @param dayOfMonth       The current day of the month being processed.
     * @param chosenDayOfWeek  The day of the week chosen by the user for specific analysis (1 = Monday, ..., 7 = Sunday).
     * @param toYear           The year of the last month to determine the end of the period.
     * @param toMonth          The month to determine the end of the period.
     * @param chosenDay        The specific day of the week chosen by the user for detailed analysis.
     */
    private void handleDayChange(LocalDateTime timestamp, int dayOfMonth, DayOfWeek chosenDayOfWeek, int toYear, Month toMonth, Integer chosenDay) {
        if (actualInformationAboutDateTime != null && actualInformationAboutDateTime.getDayOfMonth() != dayOfMonth) {
            boolean isLastDayOfMonth = actualInformationAboutDateTime.getMonth().length(actualInformationAboutDateTime.toLocalDate().isLeapYear()) == actualInformationAboutDateTime.getDayOfMonth();
            if (actualInformationAboutDateTime.getDayOfWeek().getValue() == chosenDay && !isLastDayOfMonth) {
                if (!(actualInformationAboutDateTime.getYear() == toYear && actualInformationAboutDateTime.getMonth() == toMonth)) {
                    logDailyResults();
                }
            }
            daySum = 0;
        }
    }
    /*
    public void processData(LocalDateTime timestamp, double value, Integer chosenDay, YearMonth theLastMonthYear) {

        int toYear = theLastMonthYear.getYear();
        Month toMonth = theLastMonthYear.getMonth();

        Month month = timestamp.getMonth();
        DayOfWeek chosenDayOfWeek = (chosenDay != null && chosenDay > 0) ? DayOfWeek.of(chosenDay) : null;
        int dayOfMonth = timestamp.getDayOfMonth();

        // Save the last day in month before next month.
        if (actualInformationAboutDateTime != null && actualInformationAboutDateTime.getMonth() != month) {
            if (chosenDayOfWeek != null && actualInformationAboutDateTime.getDayOfWeek().getValue() == chosenDay && !actualInformationAboutDateTime.toLocalDate().equals(timestamp.toLocalDate())) {
                if (!(actualInformationAboutDateTime.getYear() == toYear &&
                        actualInformationAboutDateTime.getMonth() == toMonth)) {
                    logDailyResults();
                }
            }
            logMonthlyResults(chosenDayOfWeek);
            resetMonthlyData();
        }

        if (currentMonth == null || !currentMonth.equals(month)) {
            currentMonth = month;
        }

        // If new day, log data from the day before.
        if (actualInformationAboutDateTime != null && actualInformationAboutDateTime.getDayOfMonth() != dayOfMonth) {
            // Check if the last day was the last month day (logged).
            boolean isLastDayOfMonth = actualInformationAboutDateTime.getMonth().length(actualInformationAboutDateTime.toLocalDate().isLeapYear()) == actualInformationAboutDateTime.getDayOfMonth();

            if (actualInformationAboutDateTime.getDayOfWeek().getValue() == chosenDay && !isLastDayOfMonth) {
                // Days from the last month will not log.
                if (!(actualInformationAboutDateTime.getYear() == toYear &&
                        actualInformationAboutDateTime.getMonth() == toMonth)) {
                    logDailyResults();
                }
            }
            daySum = 0;
        }

        allSeasonValue += value;

        oneMonthValue += value;

        // If a user chose the concrete day, do detailed analyze.
        if (chosenDayOfWeek != null && timestamp.getDayOfWeek().getValue() == chosenDay) {
            daySum += value;
            allValuesOfChosenDayInOneMonth += value;
            allValuesOfChosenDayInSeason += value;
            numberOfChosenDaysInMonth++;
        }

        actualInformationAboutDateTime = timestamp;
    }
    */

    /**
     * Logs the cumulative results for a specific day.
     *
     * Outputs the daily solar radiation total to the logger for the last processed day.
     */
    private void logDailyResults() {
        String dayName = actualInformationAboutDateTime.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.forLanguageTag("cs"));
        logger.info("Souhrnný výkon za " + dayName + " " + actualInformationAboutDateTime.toLocalDate() + ": " + daySum + " W/m²");
    }

    /**
     * Logs the cumulative results for a specific month.
     *
     * Outputs the monthly solar radiation total and, if applicable,
     * averages the data for the chosen day of the week.
     *
     * @param chosenDayOfWeek The day of the week to log results for.
     */
    private void logMonthlyResults(DayOfWeek chosenDayOfWeek) {
        String monthName = currentMonth.getDisplayName(TextStyle.FULL, Locale.forLanguageTag("cs"));
        logger.info("Měsíc: " + monthName + ", rok: " + actualInformationAboutDateTime.getYear() + ", Celkový výkon: " + oneMonthValue + " W/m²");
        if (chosenDayOfWeek != null) {
            String dayName = chosenDayOfWeek.getDisplayName(TextStyle.FULL, Locale.forLanguageTag("cs"));
            long averageDayValue = allValuesOfChosenDayInOneMonth / numberOfChosenDaysInMonth;
            logger.info("Průměrný výkon za zvolený den(" + dayName + ") za měsíc (" + monthName + ") roku " + actualInformationAboutDateTime.getYear() + ": " + averageDayValue + " W/m²");
        }
    }

    /**
     * Resets monthly-specific data accumulators.
     *
     * Called when moving to a new month in the dataset.
     */
    private void resetMonthlyData() {
        oneMonthValue = 0;
        allValuesOfChosenDayInOneMonth = 0;
        numberOfChosenDaysInMonth = 0;
    }

    /**
     * Logs the total results for the entire period (season).
     *
     * Outputs the total accumulated solar radiation for the period and, if applicable,
     * additional details about the performance of specific days throughout the period.
     */
    public void logTotalResults() {
        logger.info("Celkový výkon za období: " + allSeasonValue + " W/m²");
        if (allValuesOfChosenDayInSeason == 0) {
            logger.info("Detailnějnější data ohledně výkonů v konkrétních dnech v průběhu celého období můžete získat, pokud si den zvolíte při úvodním zadání parametrů.");
        }
        logger.info("Celkový výkon za celé zvolené období pro zvolené dny: " + allValuesOfChosenDayInSeason + " W/m²");
    }
}


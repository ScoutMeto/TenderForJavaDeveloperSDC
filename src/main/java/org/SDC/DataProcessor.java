package org.SDC;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.Locale;

public class DataProcessor {

    private static final Logger logger = LogManager.getLogger(DataProcessor.class);

    private long allSeasonValue = 0;  // Celkový součet za období
    private long oneMonthValue = 0; // Součet za měsíc
    private long daySum = 0;  // Součet za konkrétní den (např. pondělí)
    private long allValuesOfChosenDayInOneMonth = 0; // Součet za konkrétní den v jednom měsíci
    private long allValuesOfChosenDayInSeason = 0; // Součet za konkrétní den za celé období (from/to)
    private int numberOfChosenDaysInMonth = 0;  // Počet zvolených dní v měsíci
    private Month currentMonth = null;  // Aktuálně zpracovávaný měsíc
    private LocalDateTime actualInformationAboutDateTime = null; // Poslední zpracovaný den

    /*
    private final int chosenDay;

    public DataProcessor(int chosenDay) {
        this.chosenDay = chosenDay;
    }

     */

    // Zpracování dat
    public void processData(LocalDateTime timestamp, double value, int chosenDay, YearMonth theLastMonthYear) {
        Month month = timestamp.getMonth();
        DayOfWeek dayOfWeek = timestamp.getDayOfWeek();
        DayOfWeek chosenDayOfWeek = DayOfWeek.of(chosenDay);

        int dayOfMonth = timestamp.getDayOfMonth();

        // Uložíme poslední zpracovaný den, než změníme měsíc
        if (actualInformationAboutDateTime != null && actualInformationAboutDateTime.getMonth() != month) {
            // Než vypíšeme souhrn za měsíc, zkontrolujeme, jestli poslední den měsíce už nebyl zalogován
            if (actualInformationAboutDateTime.getDayOfWeek().getValue() == chosenDay && !actualInformationAboutDateTime.toLocalDate().equals(timestamp.toLocalDate())) {
                logDailyResults();  // Souhrnný výkon za poslední zvolený den v měsíci
            }
            logMonthlyResults(chosenDayOfWeek);  // Souhrn za celý měsíc
            resetMonthlyData();
        }

        // Aktualizace aktuálního měsíce (pouze pokud došlo ke změně měsíce)
        if (currentMonth == null || currentMonth.equals(month)) {
            currentMonth = month;
        }


        // Pokud jsme na novém dni, logujeme součet za předchozí den
        if (actualInformationAboutDateTime != null && actualInformationAboutDateTime.getDayOfMonth() != dayOfMonth) {
            // Zkontrolujeme, zda poslední den nebyl posledním dnem v měsíci, který už byl zalogován
            boolean isLastDayOfMonth = actualInformationAboutDateTime.getMonth().length(actualInformationAboutDateTime.toLocalDate().isLeapYear()) == actualInformationAboutDateTime.getDayOfMonth();

            if (actualInformationAboutDateTime.getDayOfWeek().getValue() == chosenDay && !isLastDayOfMonth) {
            logDailyResults();
            }
            daySum = 0;  // Reset denního součtu
        }

        // Přičtení hodnoty do celkového součtu
        allSeasonValue += value;

        // Přičtení hodnoty do součtu za měsíc
        oneMonthValue += value;

        // Pokud je zvolený den
        if (timestamp.getDayOfWeek().getValue() == chosenDay) {
            daySum += value;  // Součet za zvolený den
            allValuesOfChosenDayInOneMonth += value;  // Součet za den v aktuálním měsíci
            allValuesOfChosenDayInSeason += value;  // Součet za den za celé období
            numberOfChosenDaysInMonth++;
        }

        // Uložíme poslední zpracovaný den
        actualInformationAboutDateTime = timestamp;
    }





    // Výpis denních výsledků
    private void logDailyResults() {
        String dayName = actualInformationAboutDateTime.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.forLanguageTag("cs"));  // Název dne v češtině
        logger.info("Souhrnný výkon za " + dayName +" " + actualInformationAboutDateTime.toLocalDate() + ": " + daySum + " W/m²");
    }

    // Výpis měsíčních výsledků
    private void logMonthlyResults(DayOfWeek chosenDayOfWeek) {
        String monthName = currentMonth.getDisplayName(TextStyle.FULL, Locale.forLanguageTag("cs")); // Název měsíce v češtině
        String dayName = chosenDayOfWeek.getDisplayName(TextStyle.FULL, Locale.forLanguageTag("cs"));  // Název dne v češtině
        int year = actualInformationAboutDateTime.getYear();  // Získání aktuálního roku jako int
        logger.info("Měsíc: " + monthName + ", Celkový výkon: " + oneMonthValue + " W/m²");
        if (numberOfChosenDaysInMonth > 0) {
            long averageDayValue = allValuesOfChosenDayInOneMonth / numberOfChosenDaysInMonth;
            logger.info("Průměrný výkon za zvolený den(" + dayName + ") za měsíc (" + monthName + ") roku " + actualInformationAboutDateTime.getYear() + ": " + averageDayValue + " W/m²");
        }
    }

    // Resetování dat pro nový měsíc
    private void resetMonthlyData() {
        oneMonthValue = 0;
        allValuesOfChosenDayInOneMonth = 0;
        numberOfChosenDaysInMonth = 0;
    }

    // Výpis celkového výsledku za celé období
    public void logTotalResults() {
        logger.info("Celkový výkon za období: " + allSeasonValue + " W/m²");
        logger.info("Celkový výkon za celé zvolené období pro zvolené dny: " + allValuesOfChosenDayInSeason + " W/m²");
    }
}


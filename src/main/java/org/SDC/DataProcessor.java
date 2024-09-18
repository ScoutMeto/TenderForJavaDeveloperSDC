package org.SDC;


import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.HashMap;
import java.util.Map;

public class DataProcessor {

    private long allSeasonValue = 0;  // Celkový součet za období
    private long oneMonthValue = 0; // Součet za měsíc
    private long daySum = 0;  // Součet za konkrétní den (např. pondělí)
    private long allValuesOfChosenDayInOneMonth = 0; // Součet za konkrétní den v jednom měsíci
    private long allValuesOfChosenDayInSeason = 0; // Součet za konkrétní den za celé období (from/to)
    private int numberOfChosenDaysInMonth = 0;  // Počet zvolených dní v měsíci
    private Month currentMonth = null;  // Aktuálně zpracovávaný měsíc
    private LocalDateTime lastProcessedDay = null; // Poslední zpracovaný den

    // Mapování dnů a měsíců do češtiny
    private static final Map<DayOfWeek, String> dayOfWeekMap = new HashMap<>();
    private static final Map<Month, String> monthMap = new HashMap<>();

    static {
        dayOfWeekMap.put(DayOfWeek.MONDAY, "Pondělí");
        dayOfWeekMap.put(DayOfWeek.TUESDAY, "Úterý");
        dayOfWeekMap.put(DayOfWeek.WEDNESDAY, "Středa");
        dayOfWeekMap.put(DayOfWeek.THURSDAY, "Čtvrtek");
        dayOfWeekMap.put(DayOfWeek.FRIDAY, "Pátek");
        dayOfWeekMap.put(DayOfWeek.SATURDAY, "Sobota");
        dayOfWeekMap.put(DayOfWeek.SUNDAY, "Neděle");

        monthMap.put(Month.JANUARY, "Leden");
        monthMap.put(Month.FEBRUARY, "Únor");
        monthMap.put(Month.MARCH, "Březen");
        monthMap.put(Month.APRIL, "Duben");
        monthMap.put(Month.MAY, "Květen");
        monthMap.put(Month.JUNE, "Červen");
        monthMap.put(Month.JULY, "Červenec");
        monthMap.put(Month.AUGUST, "Srpen");
        monthMap.put(Month.SEPTEMBER, "Září");
        monthMap.put(Month.OCTOBER, "Říjen");
        monthMap.put(Month.NOVEMBER, "Listopad");
        monthMap.put(Month.DECEMBER, "Prosinec");
    }

    // Zpracování dat
    public void processData(LocalDateTime timestamp, double value, int chosenDay) {
        Month month = timestamp.getMonth();
        DayOfWeek dayOfWeek = timestamp.getDayOfWeek();
        int dayOfMonth = timestamp.getDayOfMonth();

        // Uložíme poslední zpracovaný den, než změníme měsíc
        if (lastProcessedDay != null && lastProcessedDay.getMonth() != month) {
            // Než vypíšeme souhrn za měsíc, zpracujeme zvolený den posledního dne měsíce
            if (lastProcessedDay.getDayOfWeek().getValue() == chosenDay) {
                logDailyResults();  // Souhrnný výkon za poslední zvolený den v měsíci
            }
            logMonthlyResults();  // Souhrn za celý měsíc
            resetMonthlyData();
        }

        // Aktualizace aktuálního měsíce (pouze pokud došlo ke změně měsíce)
        if (currentMonth == null || !currentMonth.equals(month)) {
            currentMonth = month;
        }


        // Pokud jsme na novém dni, logujeme součet za předchozí den
        if (lastProcessedDay != null && lastProcessedDay.getDayOfMonth() != dayOfMonth) {
            if (lastProcessedDay.getDayOfWeek().getValue() == chosenDay) {
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
        lastProcessedDay = timestamp;
    }

    // Výpis denních výsledků
    private void logDailyResults() {
        String dayName = dayOfWeekMap.get(lastProcessedDay.getDayOfWeek());  // Název dne v češtině
        MyLogger.logInfo("Souhrnný výkon za " + dayName +" " + lastProcessedDay.toLocalDate() + ": " + daySum + " W/m²");
    }

    // Výpis měsíčních výsledků
    private void logMonthlyResults() {
        String monthName = monthMap.get(currentMonth);  // Název měsíce v češtině
        String dayName = dayOfWeekMap.get(lastProcessedDay.getDayOfWeek());  // Název dne v češtině
        MyLogger.logInfo("Měsíc: " + monthName + ", Celkový výkon: " + oneMonthValue + " W/m²");
        if (numberOfChosenDaysInMonth > 0) {
            long averageDayValue = allValuesOfChosenDayInOneMonth / numberOfChosenDaysInMonth;
            MyLogger.logInfo("Průměrný výkon za zvolený den(" + dayName + ") v průběhu měsíce" + "(" + monthName + "): " + averageDayValue + " W/m²");
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
        MyLogger.logInfo("Celkový výkon za období: " + allSeasonValue + " W/m²");
        MyLogger.logInfo("Celkový výkon za celé zvolené období pro zvolené dny: " + allValuesOfChosenDayInSeason + " W/m²");
    }
}


/*
import java.time.LocalDateTime;
import java.time.Month;

// Na základě volby uživatele dojde k filtraci dat, výpočtu a následnému výpisu
public class DataProcessor {

    private long allSeasonValue = 0;  // Celkový součet za období
    private long oneMonthValue = 0; // Součet za měsíc
    private long daySum = 0;  // Součet za konkrétní den (např. pondělí)
    private long totalDaySum = 0;  // Celkový součet pro zvolený den
    private long allValuesOfChosenDayInOneMonth = 0; // Součet za konkrétní den v jednom měsíci
    private long allValuesOfChosenDayInSeason = 0; //Součet za konkrétní den za celé období (from/to)
    private int numberOfChosenDaysInMonth = 0;  // Počet zvolených dní v měsíci
    private Month currentMonth = null;  // Aktuálně zpracovávaný měsíc

    public void processData(LocalDateTime timestamp, double value, int chosenDay) {
        Month month = timestamp.getMonth();

        // Když se změní měsíc, logujeme výsledky za předchozí měsíc
        if (currentMonth != null && !currentMonth.equals(month)) {
            logMonthlyResults();
            resetMonthlyData();
        }

        // Aktualizace aktuálního měsíce
        currentMonth = month;

        // Přičtení hodnoty do celkového součtu
        allSeasonValue += value;

        // Přičtení hodnoty do součtu za měsíc
        oneMonthValue += value;

        // Pokud je zvolený konkrétní den
        if (timestamp.getDayOfWeek().getValue() == chosenDay) {
            daySum += value;  // Součet za zvolený den
            totalDaySum += value;  // Celkový součet za zvolený den v celém období
            allValuesOfChosenDayInOneMonth += value;  // Součet za den v aktuálním měsíci
            allValuesOfChosenDayInSeason += value; // Součet za den za celé období (from/to)
            numberOfChosenDaysInMonth++;
        }

        // Zobrazení výkonu za daný záznam
       MyLogger.logInfo("Datum: " + timestamp + ", Hodnota: " + value);
    }

    // Výpis měsíčních výsledků
    private void logMonthlyResults() {
        MyLogger.logInfo("Měsíc: " + currentMonth + ", Celkový výkon: " + oneMonthValue);
        if (numberOfChosenDaysInMonth > 0) {
            long averageDayValue = allValuesOfChosenDayInOneMonth / numberOfChosenDaysInMonth;
            MyLogger.logInfo("Průměrný výkon za zvolený den v měsíci (" + currentMonth + "): " + averageDayValue);
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
        MyLogger.logInfo("Celkový výkon za období: " + allSeasonValue);
        MyLogger.logInfo("Celkový výkon za zvolený den: " + totalDaySum);
        MyLogger.logInfo("Celkový výkon za celé zvolené období pro zvolené dny " + allValuesOfChosenDayInSeason);
    }
}

 */
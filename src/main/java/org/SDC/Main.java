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

            // Výpis celkových výsledků
            dataProcessor.logTotalResults();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

/*
Aktuálně:
            -duplicitní výpis při přelomu měsíce, kdy hlídaný den vychází na 31. Byl opraven výpis dne,
            aby se vypsal před souhrnem a nezasáhl do nového měsíce, ale zároveň nebyl kód zcela upraven
            a vypisuje se duplicitně (zůstala i stará část kódu). Uprav:
            ...
            2024-09-17 15:59:12 INFO  org.SDC.MyLogger - Měsíc: Prosinec, Celkový výkon: 41177 W/m²
            2024-09-17 15:59:12 INFO  org.SDC.MyLogger - Průměrný výkon za zvolený den(Pondělí) v průběhu měsíce(Prosinec): 51 W/m²
            2024-09-17 15:59:12 INFO  org.SDC.MyLogger - Měsíc: Prosinec, Celkový výkon: 0 W/m²
            2024-09-17 15:59:12 INFO  org.SDC.MyLogger - Souhrnný výkon za Pondělí 2012-12-31: 1779 W/m²
            ...
            -zároveň musím zařídit, abych nemusel zadávat yyyymmdd, ale dle zadání stačí yyyymm

1) volba uživatele (pokud zvolí i den, je to komplexnější)
2) předávka dat přes konstruktory instancí metod
3) co vypisuju přes logger:
    -{vlastní proměnná, allSeasonValue} celkový výkon za období - součet hodnot (long) = informace zůstává až do konce a vypíše se na závěr
    -{vlastní proměnná, oneMonthValue} celkový výkon za měsíc - součet hodnot (long) = restart po konci měsíce a výpise na konci onoho měsíce (metoda kontroluje, kdy měsíc končí)
    ...při volbě konkrétního dne navíc (metoda kontroluje čísla 0= žádný den/null; 1 je pondělí, 2 úterý...)
    -{vlastní proměnná, allValuesOfChosenDayInOneMonth} celký výkon za daný den(data sbírána po dobu měsíce) - součet hodnot (long) = restart po konci měsíce a výpise (nejdříve přičíst hodnotu k poslední proměnné - allValuesOfChosenDay
        {vlastní proměnná, numberOfChosenDayInOneMonth} počet dnů (např. pondělků) v měsíci = slouží pro výpočet průměru
        {vlastní proměnná, allValuesOfChosenDayInSeason} součet všech výkonů za zvolený den, ale za celé období from/to (všechny zvolené pondělky, ne jen ty v měsíci) (long) = informace zůstává do konce a výpis na závěr

 */
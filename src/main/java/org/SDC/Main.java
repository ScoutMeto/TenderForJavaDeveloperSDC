package org.SDC;

/**
 * The Main class serves as the entry point for the application.
 * It initializes and starts the program by invoking the ApplicationRunner class.
 *
 * Author: Matej Pella
 */
public class Main {

    /**
     * The main method is the starting point of the Java application.
     * It creates an instance of ApplicationRunner and calls the `run` method to begin the program's execution.
     *
     * @param args command-line arguments (not used in this application).
     */
    public static void main(String[] args) {
        new ApplicationRunner().run();
    }
}

/*
Navrhovaná úprava kódu:
    Export dat do souboru .csv
    -soubor "finalDataSet" vytvořit ve složce "resources" automaticky při zahájení dotazu
    -na základě přijatých parametrů vytvořit hlavičku dokumentu (záleží, zda uživatel zvolí pouze měsíce, nebo přidá také den)
    -průběžně, spolu s výpisem, exportovat a zapisovat data do souboru (přístup zvolen kvůli paměti)
    -po ukončení výpisu se uživatele zeptat, zda chce soubor s daty zachovat, nebo má být vymazán
 */
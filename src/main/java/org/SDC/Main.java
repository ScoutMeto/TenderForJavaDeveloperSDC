package org.SDC;

/**
 * The Main class serves as the entry point for the application.
 * It initializes and starts the program by invoking the ApplicationRunner class.
 * <p>
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
    -ziskem dat se přidá automaticky generovaná přípona tvořena uživateli zadaným rozsahem from/to
    -na základě přijatých parametrů vytvořit hlavičku dokumentu (záleží, zda uživatel zvolí pouze měsíce, nebo přidá také den)
    -průběžně, spolu s výpisem, exportovat a zapisovat data do souboru (přístup zvolen kvůli paměti)
    -po ukončení výpisu se uživatele zeptat, zda chce soubor s daty zachovat, nebo má být vymazán
    -po uložení nebo vymazání následuje dotaz ohledně zadání nových dat, nebo ukončení programu

    Předělat aplikaci na Springboot aplikaci
    -přidat možnost spouštět z command line s parametry -console nebo -restApi
    -pokud by aplikace byla spuštěna z konzole s parametrem -console chovala by se jako klasická konzolová aplikace,
        tedy jako nyní
    -okud by byla spuštěna jako -restApi, tak by fungovala jako Api. Kdy pomoci knihovny thymeleaf bychom backend
        propojily s jednoduchým frontendem:
        (Html stránka s formulářem obsahující inputy pro zadávání parametrů (cesta k csv souboru) datum od a datum do
        jako povinné parametry a nepovinný den
     -ýsledky by byly vypisované na stránce, napřiklad do tabulky, dali by se přidat grafy
     -nechybělo by tlačitko pro export výsledků do excelu nebo csv atd.
 */
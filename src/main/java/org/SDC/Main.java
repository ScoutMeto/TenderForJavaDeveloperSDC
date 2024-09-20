package org.SDC;

import java.io.IOException;


public class Main {
    public static void main(String[] args) {
        new ApplicationRunner().run();
    }
}

/*
Úpravy:
-nad metodami a pro trídy dokumentační komentáře /** ...
-komentáře v kódu anglicky
-uklidit, zarovnat vše, stejné mezery a odsazení
-rozdělit třídu DataProcessor nějak... nevím jak...
 */


/*
Vychytávky?
-export dat do csv
    -vyřeš export podle myšlenky:
        Chci, aby CSVReaderService při výpisu na konci pomocí metody dataProcessor.logTotalResults(); při vypsání
        automaticky připravil soubor dat. Uživatel se při dotazu, zda chce data exportovat,
        pouze rozhodne, zda soubor uložíme, nebo zatratíme.
    -pokud je zadaná jakákoliv jiná volba, než uložení dat (tedy konec programu/znovuspuštění programu/volba neukládat data), je soubor zatracen)
 */
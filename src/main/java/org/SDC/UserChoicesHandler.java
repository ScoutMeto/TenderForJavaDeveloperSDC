package org.SDC;


import java.util.Scanner;

public class UserChoicesHandler {


    private Scanner scanner;

    public UserChoicesHandler() {
        this.scanner = new Scanner(System.in);
    }

    public String askForFromDate() {
        String fromDate;
        do {
            System.out.print("Zadejte počáteční datum ve formátu yyyyMM (6 znaků): ");
            fromDate = scanner.nextLine();

            if (fromDate.equalsIgnoreCase("exit")) {
                System.out.println("Program byl ukončen.");
                System.exit(0);
            }

            if (!isValidDate(fromDate)) {
                System.out.println("Neplatné datum. Datum musí mít 6 znaků a být ve formátu yyyyMM.");
            }

        } while (!isValidDate(fromDate));

        return fromDate;
    }

    public String askForToDate() {
        String fromDate;
        do {
            System.out.print("Zadejte koncové datum ve formátu yyyyMM (6 znaků): ");
            fromDate = scanner.nextLine();

            if (fromDate.equalsIgnoreCase("exit")) {
                System.out.println("Program byl ukončen.");
                System.exit(0);
            }

            if (!isValidDate(fromDate)) {
                System.out.println("Neplatné datum. Datum musí mít 6 znaků a být ve formátu yyyyMM.");
            }

        } while (!isValidDate(fromDate));

        return fromDate;
    }


    // Dotaz na den v týdnu
    public Integer askForDay() {
        String dayInput;
        Integer day = null;
        do {
            System.out.print("Zadejte číslo dne v týdnu (1 = Pondělí, 7 = Neděle), nebo stiskněte Enter pro přeskočení: ");
            dayInput = scanner.nextLine();

            if (dayInput.equalsIgnoreCase("exit")) {
                System.out.println("Program ukončen.");
                System.exit(0);
            }

            try {
                if (dayInput.isEmpty()) {
                    return null;  // Stisk Enter bez zadání dne = null
                }
                day = Integer.parseInt(dayInput);
                if (day < 1 || day > 7) {
                    System.out.println("Neplatný den. Zadejte číslo mezi 1 a 7. \"exit\" pro ukončení.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Neplatný vstup. Zadejte číslo. \"exit\" pro ukončení.");
            }

        } while (day == null);

        return day;
    }

    // Metoda na validaci data ve formátu yyyyMM
    private boolean isValidDate(String date) {
        if (date.length() != 6) {
            return false;
        }
        try {
            Integer.parseInt(date);  // Pokus o převedení na číslo
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}

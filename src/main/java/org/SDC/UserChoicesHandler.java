package org.SDC;

import java.util.Scanner;

/**
 * This class is responsible for handling user input, including receiving date ranges, day selection,
 * and whether to repeat the process or exit the program.
 *
 * Author: Matej Pella
 */
public class UserChoicesHandler {

    private Scanner scanner;

    /**
     * Constructor initializes the Scanner for reading user input from the console.
     */
    public UserChoicesHandler() {
        this.scanner = new Scanner(System.in);
    }

    /**
     * Prompts the user to enter a date in the format yyyyMM and validates the input.
     * If "exit" is entered, the program terminates.
     *
     * @param message the message to display when asking for input.
     * @return a valid date string in the format yyyyMM.
     */
    public String askForDate(String message) {
        String dateInput;
        do {
            System.out.print(message);
            dateInput = scanner.nextLine();
            if (dateInput.equalsIgnoreCase("exit")) {
                System.out.println("Program byl ukončen.");
                System.exit(0);
            }
            if (!isValidDate(dateInput)) {
                System.out.println("Neplatné datum. Datum musí mít 6 znaků a být ve formátu yyyyMM. Znovu, nebo \"exit\" pro ukončení.");
            }
        } while (!isValidDate(dateInput));
        return dateInput;
    }

    /**
     * Asks the user to input a day of the week (1 = Monday, ..., 7 = Sunday) or allows them to skip by pressing Enter.
     * If "exit" is entered, the program terminates.
     *
     * @return an Integer representing the selected day of the week or null if skipped.
     */
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
                    return null;
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

    /**
     * Validates the date input to ensure it is in the correct format (yyyyMM).
     *
     * @param date the input date string.
     * @return true if the date is valid, false otherwise.
     */
    private boolean isValidDate(String date) {
        if (date.length() != 6) {
            return false;
        }
        try {
            Integer.parseInt(date);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Asks the user whether to repeat the program with new data or exit the program.
     *
     * @return true if the user wants to repeat, false if they want to exit.
     */
    public boolean askForRepeatOrExit() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("Chcete zadat nová data (z) nebo ukončit program (u)?");
            String userInput = scanner.nextLine().trim().toLowerCase();
            if (userInput.equals("z")) {
                return true;
            } else if (userInput.equals("u")) {
                return false;
            } else {
                System.out.println("Neplatná volba, zkuste to znovu.");
            }
        }
    }
}

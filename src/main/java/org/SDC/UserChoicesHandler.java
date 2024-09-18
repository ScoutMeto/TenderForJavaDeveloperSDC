package org.SDC;



import java.util.Scanner;

public class UserChoicesHandler {

    private Scanner scanner;

    public UserChoicesHandler() {
        this.scanner = new Scanner(System.in);
    }

    public String askForFromDate() {
        System.out.print("Zadejte počáteční datum ve formátu YYYYMM (FROM): ");
        return scanner.nextLine();
    }

    public String askForToDate() {
        System.out.print("Zadejte koncové datum ve formátu YYYYMM (TO): ");
        return scanner.nextLine();
    }

    public Integer askForDay() {
        System.out.print("Zadejte volitelný den v týdnu (1 = Pondělí, ..., 7 = Neděle) nebo stiskněte Enter pro přeskočení: ");
        String input = scanner.nextLine();
        if (input.isEmpty()) {
            return null;  // Stisk Enter bez zadání dne = null
        } else {
            return Integer.parseInt(input);  // Převede zadané číslo na integer
        }
    }
}

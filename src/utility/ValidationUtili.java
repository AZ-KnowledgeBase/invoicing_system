package utility;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class ValidationUtili {

    // Converts a date user input string in YYYY-MM-DD format into a LocalDate, or returns null.
    public static LocalDate parseDate(Scanner scanner) {
        while (true) {
            String input = scanner.nextLine().trim();
            try {
                return LocalDate.parse(input, DateTimeFormatter.ISO_LOCAL_DATE);
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format. Please re-enter (YYYY-MM-DD): ");
            }
        }
    }

    // Add your other validation methods here (email, contact, etc.)
}
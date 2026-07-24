/**
 * 14. Convert a string to a date.
 *
 * Test Data: "2017-01-25" -> LocalDate 2017-01-25
 */
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class Q14StringToDate {

    static LocalDate parse(String text) {
        return LocalDate.parse(text, DateTimeFormatter.ISO_LOCAL_DATE);
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Input a date string (yyyy-mm-dd): ");
        String input = scanner.nextLine();

        System.out.println("Input string: " + input);
        System.out.println("Parsed date: " + parse(input));
    }
}

/**
 * 14. Convert a string to a date.
 *
 * Test Data: "2017-01-25" -> LocalDate 2017-01-25
 */
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Q14StringToDate {

    static LocalDate parse(String text) {
        return LocalDate.parse(text, DateTimeFormatter.ISO_LOCAL_DATE);
    }

    public static void main(String[] args) {
        String input = "2017-01-25";
        LocalDate date = parse(input);
        System.out.println("Input string: " + input);
        System.out.println("Parsed date: " + date);
    }
}

/**
 * 17. Print a given date-time in several common format patterns.
 */
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class Q17DateTimeFormatPatterns {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Input a date-time (yyyy-mm-ddTHH:mm:ss): ");
        LocalDateTime input = LocalDateTime.parse(scanner.nextLine());
        ZonedDateTime zoned = input.atZone(ZoneId.systemDefault());

        print(zoned, "yyyy-MM-dd");
        print(zoned, "HH:mm:ss");
        print(zoned, "yyyy-MM-dd HH:mm:ss");
        print(zoned, "E MMM yyyy HH:mm:ss.SSSZ");
        print(zoned, "HH:mm:ss,SSSZ");
    }

    private static void print(ZonedDateTime dateTime, String pattern) {
        System.out.println(pattern + " -> " + dateTime.format(DateTimeFormatter.ofPattern(pattern)));
    }
}

/**
 * 17. Print several common date/time format patterns.
 */
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class Q17DateTimeFormatPatterns {

    public static void main(String[] args) {
        ZonedDateTime now = ZonedDateTime.now();

        print(now, "yyyy-MM-dd");
        print(now, "HH:mm:ss");
        print(now, "yyyy-MM-dd HH:mm:ss");
        print(now, "E MMM yyyy HH:mm:ss.SSSZ");
        print(now, "HH:mm:ss,SSSZ");
    }

    private static void print(ZonedDateTime dateTime, String pattern) {
        System.out.println(pattern + " -> " + dateTime.format(DateTimeFormatter.ofPattern(pattern)));
    }
}

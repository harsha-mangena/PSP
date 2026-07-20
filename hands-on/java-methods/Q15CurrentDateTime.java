/**
 * 15. Display the current date and time.
 *
 * Output is inherently time-dependent, so there is no fixed value to match.
 */
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Q15CurrentDateTime {

    static String currentDateTime() {
        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern("EEEE MMMM d, yyyy H:mm:ss");
        return LocalDateTime.now().format(formatter);
    }

    public static void main(String[] args) {
        System.out.println("Current date and time: " + currentDateTime());
    }
}

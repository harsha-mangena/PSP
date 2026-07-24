/**
 * 16. Compute the difference between two dates (hours, minutes, milli,
 * seconds and nano).
 */
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Scanner;

public class Q16DateDifferenceHMSNano {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Input the start date-time (yyyy-mm-ddTHH:mm:ss): ");
        LocalDateTime start = LocalDateTime.parse(scanner.nextLine());
        System.out.print("Input the end date-time (yyyy-mm-ddTHH:mm:ss): ");
        LocalDateTime end = LocalDateTime.parse(scanner.nextLine());

        Duration duration = Duration.between(start, end);

        long hours = duration.toHours();
        long minutes = duration.toMinutes() % 60;
        long seconds = duration.getSeconds() % 60;
        long millis = duration.toMillis();
        long nanos = duration.toNanos();

        System.out.println("Difference between " + start + " and " + end + ":");
        System.out.println("Hours: " + hours);
        System.out.println("Minutes: " + minutes + " (remainder)");
        System.out.println("Seconds: " + seconds + " (remainder)");
        System.out.println("Total milliseconds: " + millis);
        System.out.println("Total nanoseconds: " + nanos);
    }
}

/**
 * 16. Compute the difference between two dates (hours, minutes, milli,
 * seconds and nano).
 */
import java.time.Duration;
import java.time.LocalDateTime;

public class Q16DateDifferenceHMSNano {

    public static void main(String[] args) {
        LocalDateTime start = LocalDateTime.of(2024, 1, 1, 8, 30, 15);
        LocalDateTime end = LocalDateTime.of(2024, 1, 2, 14, 45, 50);

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

/**
 * 5. Get the current time in New York.
 */
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class Q05CurrentTimeInNewYork {

    public static void main(String[] args) {
        ZonedDateTime nyTime = ZonedDateTime.now(ZoneId.of("America/New_York"));
        System.out.println("Current time in New York: "
                + nyTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss z")));
    }
}

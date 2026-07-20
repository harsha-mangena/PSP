/**
 * 13. Get the day of the week of a specific date.
 *
 * Test Data: 2017-01-25 -> Wednesday
 */
import java.time.LocalDate;

public class Q13DayOfWeekOfSpecificDate {

    public static void main(String[] args) {
        LocalDate date = LocalDate.of(2017, 1, 25);
        System.out.println(date + " is a " + date.getDayOfWeek());
    }
}

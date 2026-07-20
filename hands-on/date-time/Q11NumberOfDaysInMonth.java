/**
 * 11. Get the number of days of a month.
 *
 * Test Data: month 2 (February), year 2016 -> 29 days
 */
import java.time.YearMonth;

public class Q11NumberOfDaysInMonth {

    static int daysInMonth(int year, int month) {
        return YearMonth.of(year, month).lengthOfMonth();
    }

    public static void main(String[] args) {
        System.out.println("Number of days in month 2/2016: " + daysInMonth(2016, 2));
        System.out.println("Number of days in the current month: "
                + YearMonth.now().lengthOfMonth());
    }
}

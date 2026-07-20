/**
 * 8. Get the last date (day-of-month number) of the current month.
 */
import java.time.YearMonth;

public class Q08LastDateOfMonth {

    public static void main(String[] args) {
        YearMonth ym = YearMonth.now();
        System.out.println("The current month has " + ym.lengthOfMonth() + " days.");
        System.out.println("The last date of the month is: " + ym.atEndOfMonth().getDayOfMonth());
    }
}

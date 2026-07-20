/**
 * 3. Get the maximum value of the year, month, week, date from the current
 * date of a default calendar.
 */
import java.util.Calendar;

public class Q03MaxCalendarFields {

    public static void main(String[] args) {
        Calendar cal = Calendar.getInstance();

        System.out.println("Maximum year: " + cal.getActualMaximum(Calendar.YEAR));
        System.out.println("Maximum month: " + cal.getActualMaximum(Calendar.MONTH));
        System.out.println("Maximum week of year: " + cal.getActualMaximum(Calendar.WEEK_OF_YEAR));
        System.out.println("Maximum date (days in this month): "
                + cal.getActualMaximum(Calendar.DATE));
    }
}
